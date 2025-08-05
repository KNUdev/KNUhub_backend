package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubcommon.constant.Semester;
import ua.knu.knudev.knuhubcommon.constant.StudyCourse;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(schema = "people_management", name = "teaching_assignment")
public class TeachingAssigment {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudyCourse studyCourse;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Semester semester;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "teaching_assignments_to_teachers",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "teaching_assignment_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @ToString.Exclude
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "teaching_assignments_to_educational_groups",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "teaching_assignment_id"),
            inverseJoinColumns = @JoinColumn(name = "educational_group_id")
    )
    @ToString.Exclude
    private Set<EducationalGroup> groups = new HashSet<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "educational_specialty_code_name", referencedColumnName = "codeName")
    @ToString.Exclude
    private EducationalSpecialty educationalSpecialty;
}