package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(schema = "education", name = "answer")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Answer {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(precision = 6, scale = 3)
    private BigDecimal mark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_attempt_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private TestAttempt testAttempt;
}
