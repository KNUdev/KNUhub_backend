package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(schema = "education", name = "text_answer")
public class TextAnswer extends Answer {

    @Column(nullable = false)
    private String answer;

    private Boolean isMarkedAsCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "text_question_id", nullable = false)
    @ToString.Exclude
    private TextQuestion question;

    @Transient
    public Boolean isCorrect() {
        if (isMarkedAsCorrect != null) {
            return isMarkedAsCorrect;
        }

        String formattedAnswer = question.getIsCaseSensitive() ? answer : answer.toLowerCase();

        for (String correctAnswer : question.getCorrectAnswers()) {
            String formattedCorrectAnswer = question.getIsCaseSensitive() ? correctAnswer : correctAnswer.toLowerCase();

            if (formattedCorrectAnswer.equals(formattedAnswer)) {
                return true;
            }
        }

        return false;
    }
}
