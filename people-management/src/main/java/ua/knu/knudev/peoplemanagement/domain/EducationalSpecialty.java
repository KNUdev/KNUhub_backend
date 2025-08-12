package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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

    @PrePersist
    @PreUpdate
    @PreRemove
    public void associateAllInjectedEntitiesWithEducationalSpecialty() {
        if (this.faculties != null) this.faculties.forEach(faculty -> faculty.setEducationalSpecialties(new HashSet<>(Set.of(this))));
        if (this.groups != null) this.groups.forEach(group -> group.setEducationalSpecialties(new HashSet<>(Set.of(this))));
        if (this.students != null) this.students.forEach(student -> student.setSpecialties(new HashSet<>(Set.of(this))));
        if (this.teachers != null) this.teachers.forEach(teacher -> teacher.setSpecialties(new HashSet<>(Set.of(this))));
        if (this.teachingAssigments != null) this.teachingAssigments.forEach(teachingAssigment -> teachingAssigment.setEducationalSpecialty(this));
    }

    public void addFaculties(Collection<Faculty> faculties) {
        Set<Faculty> toAdd = faculties.stream()
                .filter(f -> !this.faculties.contains(f))
                .collect(Collectors.toSet());
        this.faculties.addAll(toAdd);
    }

    public void addStudents(Collection<Student> students) {
        Set<Student> toAdd = students.stream()
                .filter(s -> !this.students.contains(s))
                .collect(Collectors.toSet());
        this.students.addAll(toAdd);
    }

    public void addTeachers(Collection<Teacher> teachers) {
        Set<Teacher> toAdd = teachers.stream()
                .filter(t -> !this.teachers.contains(t))
                .collect(Collectors.toSet());
        this.teachers.addAll(toAdd);
    }

    public void addGroups(Collection<EducationalGroup> groups) {
        Set<EducationalGroup> toAdd = groups.stream()
                .filter(g -> !this.groups.contains(g))
                .collect(Collectors.toSet());
        this.groups.addAll(toAdd);
    }

    public void addTeachingAssigments(Collection<TeachingAssigment> teachingAssigments) {
        Set<TeachingAssigment> toAdd = teachingAssigments.stream()
                .filter(t -> !this.teachingAssigments.contains(t))
                .collect(Collectors.toSet());
        this.teachingAssigments.addAll(toAdd);
    }

    public void deleteFaculties(Collection<Faculty> faculties) {
        this.faculties.removeAll(faculties);
    }

    public void deleteStudents(Collection<Student> students) {
        this.students.removeAll(students);
    }

    public void deleteTeachers(Collection<Teacher> teachers) {
        this.teachers.removeAll(teachers);
    }

    public void deleteGroups(Collection<EducationalGroup> groups) {
        this.groups.removeAll(groups);
    }

    public void deleteTeachingAssigments(Collection<TeachingAssigment> teachingAssigments) {
        this.teachingAssigments.removeAll(teachingAssigments);
    }
}