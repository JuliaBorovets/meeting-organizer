package com.meeting.organizer.config;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class RestConfiguration {

    private final ApplicationParameters applicationParameters;


    private static SocketConfig getSocketConfig() {
        return SocketConfig.custom()
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .build();
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = clientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory =
                new BufferingClientHttpRequestFactory(requestFactory);
        return new RestTemplate(bufferingClientHttpRequestFactory);
    }

    private HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(getHttpClient());
        return requestFactory;
    }

    private CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .disableAutomaticRetries()
                .setDefaultRequestConfig(getRequestConfig())
                .setConnectionManager(pollingConnectionManager())
                .setDefaultSocketConfig(getSocketConfig())
                .build();
    }

    private RequestConfig getRequestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(applicationParameters.getConnectionRequestTimeout())
                .setConnectTimeout(applicationParameters.getConnectionTimeout())
                .setSocketTimeout(applicationParameters.getSocketTimeout())
                .build();
    }

    private PoolingHttpClientConnectionManager pollingConnectionManager() {
        PoolingHttpClientConnectionManager
                poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(applicationParameters.getTotalConnections());
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(applicationParameters.getTotalPerRouteConnections());
        return poolingHttpClientConnectionManager;
    }
}
