package com.meeting.organizer.model;

import com.meeting.organizer.model.user.User;
import lombok.*;

import javax.persistence.*;
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
@Table(name = "libraries")
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long libraryId;

    private String name;

    private String description;

    private String accessToken;

    @Lob
    private Byte[] image;

    @Builder.Default
    private Boolean isPrivate = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "library", fetch = FetchType.LAZY)
    private List<Stream> streams = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "library", fetch = FetchType.LAZY)
    private List<Event> events = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "library_user_fav",
            joinColumns = {@JoinColumn(name = "library_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> usersFavorite = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "library_user_access",
            joinColumns = {@JoinColumn(name = "library_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> givenAccessList = new HashSet<>();
}
