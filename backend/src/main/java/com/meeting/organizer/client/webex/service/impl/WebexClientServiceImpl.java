package com.meeting.organizer.client.webex.service.impl;

import com.meeting.organizer.client.webex.model.WebexCreateMeeting;
import com.meeting.organizer.client.webex.model.WebexMeeting;
import com.meeting.organizer.client.webex.service.WebexClientService;
import com.meeting.organizer.client.webex.service.WebexTokenService;
import com.meeting.organizer.client.zoom.model.ZoomMeeting;
import com.meeting.organizer.exception.custom.MeetingCanNotCreateException;
import com.meeting.organizer.exception.custom.MeetingCanNotDeleteException;
import com.meeting.organizer.exception.custom.MeetingNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class WebexClientServiceImpl implements WebexClientService {

    private final RestTemplate restTemplate;
    private final RetryTemplate nonIdempotentRetryTemplate;
    private final RetryTemplate idempotentRetryTemplate;
    private final WebexTokenService webexTokenService;

    @Value("${webex.url.meeting-create}")
    private String meetingCreateUrl;
    @Value("${webex.url.meeting-get}")
    private String meetingGetUrl;
    @Value("${webex.url.meeting-delete}")
    private String meetingDeleteUrl;

    public WebexClientServiceImpl(RestTemplate restTemplate,
                                  @Qualifier("nonIdempotentRetryTemplate") RetryTemplate nonIdempotentRetryTemplate,
                                  @Qualifier("idempotentRetryTemplate") RetryTemplate idempotentRetryTemplate,
                                  WebexTokenService webexTokenService) {
        this.restTemplate = restTemplate;
        this.idempotentRetryTemplate = idempotentRetryTemplate;
        this.nonIdempotentRetryTemplate = nonIdempotentRetryTemplate;
        this.webexTokenService = webexTokenService;
    }

    @Override
    public void deleteMeeting(String id) {
        HttpHeaders headers = createHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        try {
            String uri = UriComponentsBuilder.fromUriString(meetingDeleteUrl)
                    .buildAndExpand(id)
                    .toUriString();

            log.info("Deleting webex meeting, url: {}, httpEntity {}", uri, httpEntity);

            nonIdempotentRetryTemplate.execute(retryContext -> {
                        restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
                        return null;
                    }
            );
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("Not found with id={}", id);
                return;
            }
            throw new MeetingCanNotDeleteException(e);
        }
    }

    @Override
    public WebexMeeting getById(String id) {

        HttpHeaders headers = createHeaders();
        HttpEntity<WebexMeeting> httpEntity = new HttpEntity<>(headers);

        try {
            URI uri = UriComponentsBuilder.fromUriString(meetingGetUrl)
                    .buildAndExpand(id)
                    .toUri();

            log.info("Getting meeting by id, url: {}, httpEntity {}", uri, httpEntity);

            ResponseEntity<WebexMeeting> responseEntity = nonIdempotentRetryTemplate.execute(retryContext ->
                    restTemplate.exchange(uri, HttpMethod.GET, httpEntity, WebexMeeting.class)
            );

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new MeetingNotFoundException(e);
        }

    }

    @Override
    public WebexMeeting createMeeting(WebexCreateMeeting createEntity) {
        HttpHeaders headers = createHeaders();
        HttpEntity<WebexCreateMeeting> httpEntity = new HttpEntity<>(createEntity, headers);

        try {
            log.info("Creating meeting, url: {}, httpEntity {}", meetingCreateUrl, httpEntity);

            ResponseEntity<WebexMeeting> responseEntity = idempotentRetryTemplate.execute(retryContext ->
                    restTemplate.exchange(meetingCreateUrl,
                            HttpMethod.POST,
                            httpEntity,
                            WebexMeeting.class)
            );

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new MeetingCanNotCreateException(e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + webexTokenService.getAccessToken());
        headers.add("content-type", "application/json");
        return headers;
    }
}
