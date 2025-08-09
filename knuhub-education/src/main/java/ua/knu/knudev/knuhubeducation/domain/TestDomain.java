package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

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
@Table(schema = "education", name = "test")
public class TestDomain {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean isProtectedMode;

    private LocalDateTime deadline;

    private Integer durationMinutes;

    @Column(nullable = false)
    private UUID creatorId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "testDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "testDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Image> images = new HashSet<>();

    @OneToMany(mappedBy = "testDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<TestAttempt> attempts = new HashSet<>();

    @Transient
    public Boolean isExpired() {
        return deadline != null && deadline.isBefore(LocalDateTime.now());
    }

    @Transient
    public BigDecimal getMaxMark() {
        return questions.stream()
                .map(Question::getMaxMark)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
