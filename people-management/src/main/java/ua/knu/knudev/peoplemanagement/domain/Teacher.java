package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "people_management", name = "teacher")
public class Teacher extends User {

    private String scientificMotto;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinTable(
            name = "teachers_to_educational_specialties",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "educational_specialty_code_name")
    )
    @ToString.Exclude
    private Set<EducationalSpecialty> specialties = new HashSet<>();

    @ManyToMany(mappedBy = "teachers",
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @ToString.Exclude
    private Set<EducationalGroup> educationalGroups = new HashSet<>();

    @OneToOne(mappedBy = "teacher")
    private DeaneryWorker deaneryWorkerProfile;
}