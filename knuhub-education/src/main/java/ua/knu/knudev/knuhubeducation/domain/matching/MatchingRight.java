package ua.knu.knudev.knuhubeducation.domain.matching;

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
@Table(schema = "education", name = "matching_right")
public class MatchingRight {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String text;

    @OneToMany(mappedBy = "matchingRight", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchingPair> matchingPairs;
}
