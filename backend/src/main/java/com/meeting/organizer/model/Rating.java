package com.meeting.organizer.model;

import com.meeting.organizer.model.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    private String username;

    private int score;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
