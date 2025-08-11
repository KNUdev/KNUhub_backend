package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchQuestion;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(schema = "education", name = "image")
public class Image {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String filename;

    @ManyToOne
    @JoinColumn(name = "test_id", referencedColumnName = "id")
    private TestDomain testDomain;

    @ManyToOne
    @JoinColumn(name = "option_question_id", referencedColumnName = "id")
    private OptionQuestion optionQuestion;

    @ManyToOne
    @JoinColumn(name = "text_question_id", referencedColumnName = "id")
    private TextQuestion textQuestion;

    @ManyToOne
    @JoinColumn(name = "match_question_id", referencedColumnName = "id")
    private MatchQuestion matchQuestion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "option_id")
    private Option option;
}
