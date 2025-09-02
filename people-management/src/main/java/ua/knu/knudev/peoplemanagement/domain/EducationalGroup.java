package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(schema = "people_management", name = "educational_group")
public class EducationalGroup {

    @Id
    @UuidGenerator
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_name")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_name"))
    })
    private MultiLanguageField name;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "educational_groups_to_students",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "educational_group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "educational_groups_to_teachers",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "educational_group_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    @ToString.Exclude
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "educational_groups_to_subjects",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "educational_group_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @ToString.Exclude
    private Set<Subject> subjects = new HashSet<>();

    @ManyToMany(mappedBy = "groups",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    private Set<EducationalSpecialty> educationalSpecialties = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void associateAllInjectedEntitiesWithEducationalGroup() {
        if (this.students != null) this.students.forEach(student -> student.setEducationalGroups(new HashSet<>(Set.of(this))));
        if (this.teachers != null) this.teachers.forEach(teacher -> teacher.setEducationalGroups(new HashSet<>(Set.of(this))));
        if (this.subjects != null) this.subjects.forEach(subject -> subject.setEducationalGroups(new HashSet<>(Set.of(this))));
        if (this.educationalSpecialties != null) this.educationalSpecialties.forEach(
                educationalSpecialty -> educationalSpecialty.setGroups(new HashSet<>(Set.of(this))));
    }

    public void addStudents(Collection<Student> students) {
        Set<Student> toAdd = students.stream()
                .filter(t -> !this.students.contains(t))
                .collect(Collectors.toSet());
        this.students.addAll(toAdd);
    }

    public void deleteStudents(Collection<Student> students) {
        this.students.removeAll(students);
    }

    public void addTeachers(Collection<Teacher> teachers) {
        Set<Teacher> toAdd = teachers.stream()
                .filter(t -> !this.teachers.contains(t))
                .collect(Collectors.toSet());
        this.teachers.addAll(toAdd);
    }

    public void deleteTeachers(Collection<Teacher> teachers) {
        this.teachers.removeAll(teachers);
    }

    public void addSubjects(Collection<Subject> subjects) {
        Set<Subject> toAdd = subjects.stream()
                .filter(t -> !this.subjects.contains(t))
                .collect(Collectors.toSet());
        this.subjects.addAll(toAdd);
    }

    public void deleteSubjects(Collection<Subject> subjects) {
        this.subjects.removeAll(subjects);
    }

    public void addEducationalSpecialties(Collection<EducationalSpecialty> educationalSpecialties) {
        Set<EducationalSpecialty> toAdd = educationalSpecialties.stream()
                .filter(t -> !this.educationalSpecialties.contains(t))
                .collect(Collectors.toSet());
        this.educationalSpecialties.addAll(toAdd);
    }

    public void deleteEducationalSpecialties(Collection<EducationalSpecialty> educationalSpecialties) {
        this.educationalSpecialties.removeAll(educationalSpecialties);
    }

}
