package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubcommon.constant.AnswersRevealTime;
import ua.knu.knudev.knuhubeducation.domain.matching.MatchQuestion;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswersRevealTime answersRevealTime;

    private LocalDateTime deadline;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private UUID creatorId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "testDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<OptionQuestion> optionQuestions = new HashSet<>();

    @OneToMany(mappedBy = "testDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<TextQuestion> textQuestions = new HashSet<>();

    @OneToMany(mappedBy = "testDomain", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchQuestion> matchQuestions = new HashSet<>();

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
}
