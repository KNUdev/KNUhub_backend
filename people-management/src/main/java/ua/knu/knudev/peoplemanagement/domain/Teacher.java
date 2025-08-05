package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "people_management", name = "teacher")
public class Teacher extends User {

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "teachers_to_educational_specialties",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "educational_specialty_id")
    )
    @ToString.Exclude
    private Set<EducationalSpecialty> specialties = new HashSet<>();

    @ManyToMany(mappedBy = "teachers")
    @ToString.Exclude
    private Set<EducationalGroup> educationalGroups = new HashSet<>();

    @OneToOne(mappedBy = "teacher")
    private DeaneryWorker deaneryWorkerProfile;
}