package ua.knu.knudev.knuhubeducation.domain.matching;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ua.knu.knudev.knuhubeducation.domain.Question;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(schema = "education", name = "match_question")
public class MatchQuestion extends Question {

    @OneToMany(mappedBy = "matchQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchAnswer> answers = new HashSet<>();

    @OneToMany(mappedBy = "matchQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchingPair> correctMatchingPairs = new HashSet<>();
}
