package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(schema = "education", name = "text_question")
public class TextQuestion {

    @Id
    @UuidGenerator
    private UUID id;

    private String text;

    @Column(nullable = false, precision = 6, scale = 3)
    private BigDecimal maxMark;

    @ElementCollection
    @CollectionTable(
            schema = "education",
            name = "correct_text_answer",
            joinColumns = @JoinColumn(name = "text_question_id")
    )
    @Column(name = "answer")
    private Set<String> correctAnswers = new HashSet<>();

    @Column(nullable = false)
    private Boolean isCaseSensitive;

    @OneToMany(mappedBy = "textQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<TextAnswer> enteredAnswers = new HashSet<>();

    @OneToMany(mappedBy = "textQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Image> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private TestDomain test;
}
