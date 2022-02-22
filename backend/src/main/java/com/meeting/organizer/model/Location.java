package com.meeting.organizer.model;

import com.meeting.organizer.model.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long locationId;

    private String country;

    private String city;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp creationDate;

    @Builder.Default
    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "location", fetch = FetchType.LAZY)
    private List<Event> events = new ArrayList<>();
}
