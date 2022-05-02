package com.meeting.organizer.client.webex.service.impl;

import com.meeting.organizer.client.webex.model.WebexMeeting;
import com.meeting.organizer.client.webex.service.WebexClientService;
import com.meeting.organizer.client.webex.service.WebexTokenService;
import com.meeting.organizer.client.zoom.model.ZoomMeeting;
import com.meeting.organizer.config.ApplicationParameters;
import com.meeting.organizer.exception.custom.MeetingCanNotCreateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
public class WebexClientServiceImpl implements WebexClientService {

    private final ApplicationParameters applicationParameters;
    private final RestTemplate restTemplate;
    private final RetryTemplate nonIdempotentRetryTemplate;
    private final RetryTemplate idempotentRetryTemplate;
    private final WebexTokenService webexTokenService;

    @Value("${webex.url.meeting-create}")
    private String meetingCreateUrl;

    public WebexClientServiceImpl(ApplicationParameters applicationParameters,
                                  RestTemplate restTemplate,
                                  @Qualifier("nonIdempotentRetryTemplate") RetryTemplate nonIdempotentRetryTemplate,
                                  @Qualifier("idempotentRetryTemplate") RetryTemplate idempotentRetryTemplate,
                                  WebexTokenService webexTokenService) {
        this.applicationParameters = applicationParameters;
        this.restTemplate = restTemplate;
        this.idempotentRetryTemplate = idempotentRetryTemplate;
        this.nonIdempotentRetryTemplate = nonIdempotentRetryTemplate;
        this.webexTokenService = webexTokenService;
    }

    @Override
    public void deleteMeeting(Long id) {

    }

    @Override
    public WebexMeeting createMeeting(WebexMeeting createEntity) {
        HttpHeaders headers = createHeaders();
        HttpEntity<WebexMeeting> httpEntity = new HttpEntity<>(createEntity, headers);

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
