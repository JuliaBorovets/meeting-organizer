package com.meeting.organizer.model;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "actions")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long actionId;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private Long userId;

    private String description;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
