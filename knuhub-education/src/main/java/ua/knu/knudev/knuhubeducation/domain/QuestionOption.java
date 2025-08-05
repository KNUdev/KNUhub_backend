package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(schema = "education", name = "question_option")
public class QuestionOption {

    @Id
    @UuidGenerator
    private UUID id;

    private String text;

    @Column(nullable = false)
    private Boolean isCorrect;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "matched_text_id", referencedColumnName = "id", nullable = false)
    private MatchedText matchedText;

    @OneToMany(mappedBy = "questionOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TestImage> images;

    @OneToMany(mappedBy = "questionOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnswerChoice> answersChoices;

}
