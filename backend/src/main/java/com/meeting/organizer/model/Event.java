package com.meeting.organizer.model;

import com.meeting.organizer.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer maxNumberParticipants;

    private String name;

    @Lob
    private Byte[] photo;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    private State state;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    private String externalMeetingId;

    private String joinUrl;

    @Builder.Default
    private Boolean isPrivate = false;

    @Builder.Default
    @ManyToMany(mappedBy = "events")
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

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
    private List<Reaction> reactions = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "event_user_fav",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> usersFavorite = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "event_user_access",
            joinColumns = {@JoinColumn(name = "event_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> givenAccessList = new HashSet<>();

    private String accessToken;
}
