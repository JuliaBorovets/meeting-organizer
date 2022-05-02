package com.meeting.organizer.client.zoom.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.meeting.organizer.client.zoom.model.ZoomInvitation;
import com.meeting.organizer.client.zoom.model.ZoomMeeting;
import com.meeting.organizer.client.zoom.service.ZoomClientService;
import com.meeting.organizer.config.ApplicationParameters;
import com.meeting.organizer.exception.custom.*;
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
import java.util.Date;

@Slf4j
@Service
public class ZoomClientServiceImpl implements ZoomClientService {

    private final ApplicationParameters applicationParameters;
    private final RestTemplate restTemplate;
    private final RetryTemplate nonIdempotentRetryTemplate;
    private final RetryTemplate idempotentRetryTemplate;

    @Value("${zoom.url.meeting-create}")
    private String meetingCreateUrl;
    @Value("${zoom.url.meeting-delete}")
    private String meetingDeleteUrl;
    @Value("${zoom.url.meeting-get}")
    private String meetingGetUrl;
    @Value("${zoom.url.meeting-update}")
    private String meetingUpdateUrl;
    @Value("${zoom.url.meeting-invitation}")
    private String meetingInvitationGetUrl;
    @Value("${zoom.url.check-user-email}")
    private String checkUserEmailUrl;
    @Value("${zoom.url.host_id}")
    private String zoomHostId;

    public ZoomClientServiceImpl(ApplicationParameters applicationParameters,
                                 RestTemplate restTemplate,
                                 @Qualifier("nonIdempotentRetryTemplate") RetryTemplate nonIdempotentRetryTemplate,
                                 @Qualifier("idempotentRetryTemplate") RetryTemplate idempotentRetryTemplate) {
        this.applicationParameters = applicationParameters;
        this.restTemplate = restTemplate;
        this.nonIdempotentRetryTemplate = nonIdempotentRetryTemplate;
        this.idempotentRetryTemplate = idempotentRetryTemplate;
    }

    @Override
    public ZoomMeeting createMeeting(ZoomMeeting createEntity) {
        createEntity.setHost_id(zoomHostId);
        HttpHeaders headers = createHeaders();
        HttpEntity<ZoomMeeting> httpEntity = new HttpEntity<>(createEntity, headers);

        try {
            URI uri = UriComponentsBuilder.fromUriString(meetingCreateUrl)
                    .buildAndExpand(createEntity.getHost_id())
                    .toUri();

            log.info("Creating meeting, url: {}, httpEntity {}", uri, httpEntity);

            ResponseEntity<ZoomMeeting> responseEntity = idempotentRetryTemplate.execute(retryContext ->
                    restTemplate.exchange(uri, HttpMethod.POST, httpEntity, ZoomMeeting.class)
            );

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new MeetingCanNotCreateException(e);
        }
    }

    @Override
    public void deleteMeeting(Long id) {
        HttpHeaders headers = createHeaders();
        HttpEntity<ZoomMeeting> httpEntity = new HttpEntity<>(headers);

        try {
            String uri = UriComponentsBuilder.fromUriString(meetingDeleteUrl)
                    .buildAndExpand(id)
                    .toUriString();

            log.info("Deleting meeting, url: {}, httpEntity {}", uri, httpEntity);

            nonIdempotentRetryTemplate.execute(retryContext -> {
                        restTemplate.delete(uri, httpEntity);
                        return null;
                    }
            );
        } catch (HttpClientErrorException e) {
            throw new MeetingCanNotDeleteException(e);
        }
    }

    @Override
    public ZoomMeeting getById(Long id) {
        HttpHeaders headers = createHeaders();
        HttpEntity<ZoomMeeting> httpEntity = new HttpEntity<>(headers);

        try {
            URI uri = UriComponentsBuilder.fromUriString(meetingGetUrl)
                    .buildAndExpand(id)
                    .toUri();

            log.info("Getting meeting by id, url: {}, httpEntity {}", uri, httpEntity);

            ResponseEntity<ZoomMeeting> responseEntity = nonIdempotentRetryTemplate.execute(retryContext ->
                    restTemplate.exchange(uri, HttpMethod.GET, httpEntity, ZoomMeeting.class)
            );

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new MeetingNotFoundException(e);
        }
    }

    @Override
    public ZoomMeeting updateMeeting(ZoomMeeting updateEntity) {
        HttpHeaders headers = createHeaders();
        HttpEntity<ZoomMeeting> httpEntity = new HttpEntity<>(updateEntity, headers);

        try {
            URI uri = UriComponentsBuilder.fromUriString(meetingUpdateUrl)
                    .buildAndExpand(updateEntity.getId())
                    .toUri();

            log.info("Patching meeting, url: {}, httpEntity {}", uri, httpEntity);

            ResponseEntity<ZoomMeeting> responseEntity = idempotentRetryTemplate.execute(retryContext ->
                    restTemplate.exchange(uri, HttpMethod.PATCH, httpEntity, ZoomMeeting.class)
            );

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new MeetingCanNotUpdateException(e);
        }
    }

    @Override
    public ZoomInvitation getMeetingInvitation(Long meetingId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<ZoomMeeting> httpEntity = new HttpEntity<>(headers);

        try {
            URI uri = UriComponentsBuilder.fromUriString(meetingInvitationGetUrl)
                    .buildAndExpand(meetingId)
                    .toUri();

            log.info("Getting meeting invitation by id, url: {}, httpEntity {}", uri, httpEntity);

            ResponseEntity<ZoomInvitation> responseEntity = nonIdempotentRetryTemplate.execute(retryContext ->
                    restTemplate.exchange(uri, HttpMethod.GET, httpEntity, ZoomInvitation.class)
            );

            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            throw new MeetingInvitationNotFoundException(e);
        }
    }

    @Override
    public Boolean checkZoomUserExistsByEmail(String email) {
        HttpHeaders headers = createHeaders();
        HttpEntity<ZoomMeeting> httpEntity = new HttpEntity<>(headers);

        try {
            URI uri = UriComponentsBuilder.fromUriString(checkUserEmailUrl)
                    .buildAndExpand(email)
                    .toUri();

            log.info("Check if Zoom user exists by id, url: {}, httpEntity {}", uri, httpEntity);

            nonIdempotentRetryTemplate.execute(retryContext ->
                    restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Object.class)
            );
            return true;
        } catch (HttpClientErrorException e) {
            log.info("Zoom user does not exists by email " + email);
        }
        return false;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + generateZoomJWTToken());
        headers.add("content-type", "application/json");
        return headers;
    }

    private String generateZoomJWTToken() {
        Date tokenExpiry = new Date(System.currentTimeMillis() + (1000 * 60));
        return JWT.create().withClaim("iss", applicationParameters.getZoomApiKey())
                .withClaim("exp", tokenExpiry)
                .sign(Algorithm.HMAC256(applicationParameters.getZoomApiSecret()));
    }
}