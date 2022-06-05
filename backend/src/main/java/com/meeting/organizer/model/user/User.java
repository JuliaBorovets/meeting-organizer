package com.meeting.organizer.model.user;

import com.meeting.organizer.model.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long userId;

    @Column(name = "email", unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "username", unique = true)
    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private LocalDate birthday;

    private String bio;

    private String imagePath;

    private String phoneNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp creationDate;

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private VerificationToken token;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Builder.Default
    private Boolean userGoogle2fa = false;

    private String google2FaSecret;

    @Transient
    private Boolean google2faRequired = true;

    @Builder.Default
    private Boolean accountNonExpired = true;

    @Builder.Default
    private Boolean accountNonLocked = true;

    @Builder.Default
    private Boolean credentialsNonExpired = true;

    @Builder.Default
    private Boolean enabled = false;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Library> libraries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Event> events = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "usersFavorite")
    private List<Library> favoriteLibraries = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "givenAccessList")
    private Set<Library> givenAccessLibraries = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "givenAccessList")
    private Set<Event> givenAccessEvents = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "usersFavorite")
    private List<Event> favoriteEvents = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "visitors")
    private Set<Event> visitedEvents = new HashSet<>();

    @Override
    public void eraseCredentials() {
        setPassword(null);
    }

    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .map(authority -> new SimpleGrantedAuthority(authority.getPermission()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}