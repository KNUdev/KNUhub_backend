package ua.knu.knudev.knuhubeducation.domain.matching;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.knu.knudev.knuhubeducation.domain.Answer;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(schema = "education", name = "match_answer")
public class MatchAnswer extends Answer {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_question_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private MatchQuestion matchQuestion;

    @OneToMany(mappedBy = "matchAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchingPair> matchingPairs = new HashSet<>();
}
