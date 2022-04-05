package com.meeting.organizer.config.kafka;

import com.meeting.organizer.config.ApplicationParameters;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class KafkaConsumerConfiguration {

    private final ApplicationParameters applicationParameters;
}
