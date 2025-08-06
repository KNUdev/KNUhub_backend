package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(schema = "education", name = "option")
public class Option {

    @Id
    @UuidGenerator
    private UUID id;

    private String text;

    @Column(nullable = false)
    private Boolean isCorrect;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Image> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_question_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private OptionQuestion question;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "options_to_option_answers",
            schema = "education",
            joinColumns = @JoinColumn(name = "option_id"),
            inverseJoinColumns = @JoinColumn(name = "option_answer_id")
    )
    @ToString.Exclude
    private Set<OptionAnswer> optionAnswer;
}
