package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
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
    @JoinColumn(name = "match_question_id", referencedColumnName = "id", nullable = false)
    private MatchQuestion matchQuestion;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "matching_pairs_to_match_answers",
            schema = "education",
            joinColumns = @JoinColumn(name = "matching_pair_id"),
            inverseJoinColumns = @JoinColumn(name = "match_answer_id")
    )
    @ToString.Exclude
    private Set<MatchAnswer> matchAnswers = new HashSet<>();
}
