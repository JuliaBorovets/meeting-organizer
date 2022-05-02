package com.meeting.organizer.config.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.context.SpringManagedContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    private final static String TOKENS_MAP = "tokens";

    @Bean
    public Config config() {
        Config config = new Config();

        config.setInstanceName("meeting-organizer-instance")
                .setManagedContext(managedContext())
                .addMapConfig(tokensConfig());

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config hazelCastConfig) {
        return Hazelcast.newHazelcastInstance(hazelCastConfig);
    }

    @Bean
    public SpringManagedContext managedContext() {
        return new SpringManagedContext();
    }

    private MapConfig tokensConfig() {
        MapConfig mapConfig = new MapConfig(TOKENS_MAP);
        mapConfig.setTimeToLiveSeconds(0); //infinity
        return mapConfig;
    }
}
