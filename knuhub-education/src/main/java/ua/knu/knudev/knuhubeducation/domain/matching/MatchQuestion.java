package ua.knu.knudev.knuhubeducation.domain.matching;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubeducation.domain.Image;
import ua.knu.knudev.knuhubeducation.domain.TestDomain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(schema = "education", name = "match_question")
public class MatchQuestion {

    @Id
    @UuidGenerator
    private UUID id;

    private String text;

    @Column(nullable = false, precision = 6, scale = 3)
    private BigDecimal maxMark;

    @OneToMany(mappedBy = "matchQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchAnswer> answers = new HashSet<>();

    @OneToMany(mappedBy = "matchQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<MatchingPair> correctMatchingPairs = new HashSet<>();

    @OneToMany(mappedBy = "matchQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Image> images = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private TestDomain test;

    public void addImages(Set<Image> images) {
        this.images.addAll(images);
    }

    public void removeAllImages() {
        this.images.clear();
    }

    public void addCorrectMatchingPairs(Set<MatchingPair> pairs) {
        for (MatchingPair pair : pairs) {
            pair.setMatchQuestion(this);
            this.correctMatchingPairs.add(pair);
        }
    }

    public void addCorrectMatchingPair(MatchingPair pair) {
        correctMatchingPairs.add(pair);
        pair.setMatchQuestion(this);
    }

    public void removeAllCorrectMatchingPairs() {
        for (MatchingPair pair : correctMatchingPairs) {
            pair.setMatchQuestion(null);
        }
        correctMatchingPairs.clear();
    }

    public void setTest(TestDomain test) {
        if (this.test != null) {
            this.test.getMatchQuestions().remove(this);
        }
        this.test = test;
        test.getMatchQuestions().add(this);
    }
}
