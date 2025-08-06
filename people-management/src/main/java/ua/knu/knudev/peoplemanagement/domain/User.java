package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubcommon.domain.embeddable.FullName;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Table(schema = "people_management", name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true)
    private String knuEmail;
    private String commonEmail;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Embedded
    private FullName fullName;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "users_to_faculties",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "faculty_id")
    )
    @ToString.Exclude
    private Set<Faculty> faculties = new HashSet<>();
}