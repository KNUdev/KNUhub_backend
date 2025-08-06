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
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "option_id", referencedColumnName = "id")
    private Option option;
}
