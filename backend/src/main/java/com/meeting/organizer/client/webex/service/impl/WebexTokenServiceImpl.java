package com.meeting.organizer.client.webex.service.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.meeting.organizer.client.webex.model.WebexRefreshTokenResponse;
import com.meeting.organizer.client.webex.service.WebexTokenService;
import com.meeting.organizer.exception.custom.CanNotRefreshTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class WebexTokenServiceImpl implements WebexTokenService {

    private final IMap<String, String> tokenMap;
    private final RetryTemplate idempotentRetryTemplate;
    private final RestTemplate restTemplate;

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";

    @Value("${webex.refresh-token}")
    private String refreshToken;
    @Value("${webex.url.refresh-token}")
    private String refreshTokenUrl;
    @Value("${webex.user-id}")
    private String clientId;
    @Value("${webex.client-secret}")
    private String clientSecret;

    public WebexTokenServiceImpl(HazelcastInstance hazelcastInstance,
                                 @Qualifier("idempotentRetryTemplate") RetryTemplate idempotentRetryTemplate,
                                 RestTemplate restTemplate) {
        this.tokenMap = hazelcastInstance.getMap("tokens");
        this.idempotentRetryTemplate = idempotentRetryTemplate;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void initHazelcastMap() {
        setRefreshToken(refreshToken);
    }

    @Scheduled(fixedRate = 86400000) // refresh every day
    public void refreshToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<WebexRefreshTokenResponse> responseEntity = idempotentRetryTemplate.execute(retryContext ->
                    restTemplate.exchange(refreshTokenUrl,
                            HttpMethod.POST,
                            httpEntity,
                            WebexRefreshTokenResponse.class)
            );

            WebexRefreshTokenResponse refreshTokenResponse = responseEntity.getBody();

            if (refreshTokenResponse != null) {
                setAccessToken(refreshTokenResponse.getAccessToken());
                setRefreshToken(refreshTokenResponse.getRefreshToken());
            }

        } catch (HttpClientErrorException e) {
            throw new CanNotRefreshTokenException(e);
        }
    }

    @Override
    public String getAccessToken() {
        return tokenMap.get(ACCESS_TOKEN);
    }

    private void setAccessToken(String token) {
        this.tokenMap.put(ACCESS_TOKEN, token);
    }

    private String getRefreshToken() {
        return tokenMap.get(REFRESH_TOKEN);
    }

    private void setRefreshToken(String token) {
        this.tokenMap.put(REFRESH_TOKEN, token);
    }
}
