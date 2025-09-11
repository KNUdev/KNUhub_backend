package ua.knu.knudev.knuhubeducation.domain.matching;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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

    @OneToOne(mappedBy = "matchingLeft")
    @ToString.Exclude
    private MatchingPair matchingPairs;
}
