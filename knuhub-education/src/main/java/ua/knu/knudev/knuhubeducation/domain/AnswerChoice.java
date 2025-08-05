package ua.knu.knudev.knuhubeducation.domain;

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
@Table(schema = "education", name = "answer_choice")
public class AnswerChoice {

    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_option_id", referencedColumnName = "id", nullable = false)
    private QuestionOption questionOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_option_id", referencedColumnName = "id", nullable = false)
    private MatchedText matchedText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_attempt_id", referencedColumnName = "id", nullable = false)
    private TestAttempt testAttempt;

}
