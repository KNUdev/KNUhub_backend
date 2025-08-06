package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(schema = "education", name = "matching_left")
public class MatchingLeft {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String text;

    @OneToMany(mappedBy = "matchingLeft", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchingPair> matchingPairs;
}
