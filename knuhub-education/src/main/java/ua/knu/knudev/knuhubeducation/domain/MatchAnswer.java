package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @ManyToMany(mappedBy = "matchAnswer")
    @ToString.Exclude
    private Set<MatchingPair> matchingPairs = new HashSet<>();
}
