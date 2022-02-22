package com.meeting.organizer.model.user;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "authority")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityId;

    private String permission;

    @Builder.Default
    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles = new HashSet<>();
}