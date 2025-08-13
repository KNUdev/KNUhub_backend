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
@Table(schema = "education", name = "matching_pair")
public class MatchingPair {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_left_id", referencedColumnName = "id", nullable = false)
    private MatchingLeft matchingLeft;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matching_right_id", referencedColumnName = "id", nullable = false)
    private MatchingRight matchingRight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_question_id", referencedColumnName = "id")
    private MatchQuestion matchQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_answer_id", referencedColumnName = "id")
    @ToString.Exclude
    private MatchAnswer matchAnswer;
}
