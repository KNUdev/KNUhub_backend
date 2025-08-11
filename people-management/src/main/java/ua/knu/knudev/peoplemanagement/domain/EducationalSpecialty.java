package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(schema = "people_management", name = "educational_specialty")
public class EducationalSpecialty {

    @Id
    @Column(nullable = false, unique = true)
    private String codeName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_name")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_name"))
    })
    private MultiLanguageField name;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "educational_specialties_to_faculties",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "educational_specialty_code_name"),
            inverseJoinColumns = @JoinColumn(name = "faculty_id")
    )
    @ToString.Exclude
    private Set<Faculty> faculties = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "educational_specialties_to_educational_groups",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "educational_specialty_code_name"),
            inverseJoinColumns = @JoinColumn(name = "educational_group_id")
    )
    @ToString.Exclude
    private Set<EducationalGroup> groups = new HashSet<>();

    @ManyToMany(mappedBy = "specialties")
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "specialties")
    @ToString.Exclude
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "educationalSpecialty", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<TeachingAssigment> teachingAssigments = new HashSet<>();


}
