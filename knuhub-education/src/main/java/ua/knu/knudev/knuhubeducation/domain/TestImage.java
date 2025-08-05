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
@Table(schema = "education", name = "test_image")
public class TestImage {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String filename;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private TestDomain testDomain;

    @ManyToOne
    @JoinColumn(name = "test_question_id", referencedColumnName = "id")
    private TestQuestion testQuestion;

    @ManyToOne
    @JoinColumn(name = "question_option_id", referencedColumnName = "id")
    private QuestionOption questionOption;

}
