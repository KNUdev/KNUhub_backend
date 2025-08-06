package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(schema = "education", name = "option_answer")
public class OptionAnswer extends Answer {

    @ManyToMany(mappedBy = "optionAnswers")
    @ToString.Exclude
    private Set<Option> chosenOptions;

}
