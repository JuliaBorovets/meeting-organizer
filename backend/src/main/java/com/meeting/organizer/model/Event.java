package com.meeting.organizer.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "event")
    @Builder.Default
    private List<Reaction> reactions = new ArrayList<>();
}
