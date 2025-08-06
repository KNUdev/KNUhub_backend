package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
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
@Table(schema = "education", name = "text_question")
public class TextQuestion extends Question {

    @ElementCollection
    @CollectionTable(
            schema = "education",
            name = "correct_text_answer",
            joinColumns = @JoinColumn(name = "text_question_id")
    )
    @Column(name = "answer")
    private Set<String> correctAnswers = new HashSet<>();

    private Boolean isCaseSensitive;

    @OneToMany(mappedBy = "textQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<TextAnswer> enteredAnswers = new HashSet<>();
}
