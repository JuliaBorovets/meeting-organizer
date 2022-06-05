package com.meeting.organizer.model;

import com.meeting.organizer.model.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long eventId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer maxNumberParticipants;

    private String name;

    private String description;

    private String imagePath;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    private State state;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    private String externalMeetingId;

    private String joinUrl;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp creationDate;

    @Builder.Default
    private Boolean isPrivate = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private Library library;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stream_id")
    private Stream stream;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "event")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "event_user_fav",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> usersFavorite = new ArrayList<>();

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "event_user_access",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> givenAccessList = new HashSet<>();

    private String accessToken;

    @ManyToMany
    @Builder.Default
    @JoinTable(name = "event_user_visitor",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> visitors = new ArrayList<>();
}
