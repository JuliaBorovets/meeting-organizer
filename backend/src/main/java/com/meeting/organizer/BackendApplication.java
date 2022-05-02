package com.meeting.organizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaRepositories
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}


//todo share access to library and stream (for students-teachers), private/public
// library/stream/event can be saved to favorites
// user can subscribe to notifications


// add favorites list

// for activities like updating add event -> listener -> add activity stream


// restrict access to library/event -> add people that can see them -> otherwise - error

// public events display in separate list