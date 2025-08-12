package ua.knu.knudev.knuhubeducation.domain;

import jakarta.persistence.*;
import lombok.*;
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
@Builder
@Table(schema = "education", name = "option_question")
public class OptionQuestion {

    @Id
    @UuidGenerator
    private UUID id;

    private String text;

    @Column(nullable = false, precision = 6, scale = 3)
    private BigDecimal maxMark;

    @OneToMany(mappedBy = "optionQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Option> options = new HashSet<>();

    @OneToMany(mappedBy = "optionQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<Image> images;

    @OneToMany(mappedBy = "optionQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private Set<OptionAnswer> answers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private TestDomain test;
}
