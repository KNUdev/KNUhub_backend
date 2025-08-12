package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import ua.knu.knudev.knuhubcommon.constant.StudyCourse;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Table(schema = "people_management", name = "student")
public class Student extends User {

    @Column(nullable = false, unique = true)
    private String studentCardNumber;

    @Enumerated(EnumType.STRING)
    private StudyCourse studyCourse;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "students_to_educational_specialties",
            schema = "people_management",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "educational_specialty_code_name")
    )
    @ToString.Exclude
    private Set<EducationalSpecialty> specialties = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    @ToString.Exclude
    private Set<EducationalGroup> educationalGroups = new HashSet<>();

    @Column(nullable = false)
    private Boolean isHeadman;

    @Scheduled(cron = "0 0 0 1 9 *", zone = "Europe/Kyiv")
    private void recalculateStudyCourse() {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        int creationYear = this.getCreatedAt().getYear();
        int creationMonth = this.getCreatedAt().getMonthValue();

        int studyCourseIndex = studyCourse.getIndex();

        if (dateTimeNow.getYear() > creationYear
                || (dateTimeNow.getYear() == creationYear
                && creationMonth < Month.JULY.getValue())) {
            this.studyCourse = StudyCourse.values()[studyCourseIndex];
        }
    }
}