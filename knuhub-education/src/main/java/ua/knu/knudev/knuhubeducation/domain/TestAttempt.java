package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchAnswer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(schema = "education", name = "test_attempt")
public class TestAttempt {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime submitTime;

    @Column(precision = 6, scale = 3)
    private BigDecimal mark;

    @Column(nullable = false)
    private UUID studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    private TestDomain test;

    @OneToMany(mappedBy = "testAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<OptionAnswer> optionAnswers = new HashSet<>();

    @OneToMany(mappedBy = "testAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<TextAnswer> textAnswers = new HashSet<>();

    @OneToMany(mappedBy = "testAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<MatchAnswer> matchAnswers = new HashSet<>();

    public void removeOptionAnswerIfExists(UUID questionId) {
        optionAnswers.removeIf(answer ->
                answer.getQuestion() != null && questionId.equals(answer.getQuestion().getId()));
    }

    public void addOptionAnswer(OptionAnswer answer) {
        removeOptionAnswerIfExists(answer.getQuestion().getId());

        optionAnswers.add(answer);
        answer.setTestAttempt(this);
    }

    public void removeTextAnswerIfExists(UUID questionId) {
        textAnswers.removeIf(answer ->
                answer.getQuestion() != null && questionId.equals(answer.getQuestion().getId()));
    }

    public void addTextAnswer(TextAnswer answer) {
        removeTextAnswerIfExists(answer.getQuestion().getId());

        textAnswers.add(answer);
        answer.setTestAttempt(this);
    }

    public void removeMatchAnswerIfExists(UUID questionId) {
        matchAnswers.removeIf(answer ->
                answer.getMatchQuestion() != null && questionId.equals(answer.getMatchQuestion().getId()));
    }

    public void addMatchAnswer(MatchAnswer answer) {
        removeMatchAnswerIfExists(answer.getMatchQuestion().getId());

        matchAnswers.add(answer);
        answer.setTestAttempt(this);
    }
}
