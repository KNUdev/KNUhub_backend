package ua.knu.knudev.knuhubeducation.domain.matching;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubeducation.domain.TestAttempt;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(schema = "education", name = "match_answer")
public class MatchAnswer {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(precision = 6, scale = 3)
    private BigDecimal mark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_attempt_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private TestAttempt testAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_question_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private MatchQuestion matchQuestion;

    @OneToMany(mappedBy = "matchAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchingPair> matchingPairs = new HashSet<>();
}
