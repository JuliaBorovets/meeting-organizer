package com.meeting.organizer.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ApplicationParameters {

    @Value("${spring.kafka.topic.topic-name}")
    private String kafkaTopic;

    @Value("${spring.kafka.topic.partition-count}")
    private int kafkaPartitionCount;

    @Value("${spring.kafka.topic.replication-factor}")
    private short kafkaReplicationFactor;

    @Value("${spring.kafka.retention-period}")
    private String kafkaRetentionPeriod;

    // zoom

    @Value("${zoom.user-id}")
    private String userId;

    @Value("${zoom.password}")
    private String zoomPassword;

    @Value("${zoom.host-email}")
    private String zoomHostEmail;

    @Value("${zoom.api-key}")
    private String zoomApiKey;

    @Value("${zoom.api-secret}")
    private String zoomApiSecret;

    // rest client

    @Value("${rest-client.connection-request-timeout}")
    private int connectionRequestTimeout;

    @Value("${rest-client.connection-timeout}")
    private int connectionTimeout;

    @Value("${rest-client.socket-timeout}")
    private int socketTimeout;

    @Value("${rest-client.total-connections}")
    private int totalConnections;

    @Value("${rest-client.total-per-route-connections}")
    private int totalPerRouteConnections;
}
