package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;

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
@Table(schema = "people_management", name = "faculty")
public class Faculty {

    @Id
    @UuidGenerator
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_name")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_name"))
    })
    private MultiLanguageField name;

    @ManyToMany(mappedBy = "faculties")
    @ToString.Exclude
    private Set<EducationalSpecialty> educationalSpecialties = new HashSet<>();

    @ManyToMany(mappedBy = "faculties")
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    @PrePersist
    @PreUpdate
    public void associateEducationalSpecialtiesAndUsersWithFaculty() {
        this.educationalSpecialties.forEach(educationalSpecial -> educationalSpecial.setFaculties(Set.of(this)));
        this.users.forEach(user -> user.setFaculties(Set.of(this)));
    }

    public void addEducationalSpecialties(Set<EducationalSpecialty> specialties) {
        Set<EducationalSpecialty> toAdd = specialties.stream()
                .filter(s -> !this.educationalSpecialties.contains(s))
                .collect(Collectors.toSet());

        this.educationalSpecialties.addAll(toAdd);
    }

    public void addUsers(Set<User> users) {
        Set<User> toAdd = users.stream()
                .filter(u -> !this.users.contains(u))
                .collect(Collectors.toSet());

        this.users.addAll(toAdd);
    }

    public void deleteEducationalSpecialties(Set<EducationalSpecialty> specialties) {
        this.educationalSpecialties.removeAll(specialties);
    }

    public void deleteUsers(Set<User> users) {
        this.users.removeAll(users);
    }

}