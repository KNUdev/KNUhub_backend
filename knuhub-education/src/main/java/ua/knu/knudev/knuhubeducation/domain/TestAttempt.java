package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private TestDomain testDomain;

    @OneToMany(mappedBy = "testAttempt", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers;

}
