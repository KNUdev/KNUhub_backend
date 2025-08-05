package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
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
@Builder
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
            inverseJoinColumns = @JoinColumn(name = "educational_specialty_id")
    )
    @ToString.Exclude
    private Set<EducationalSpecialty> specialties = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "educational_group_id")
    private EducationalGroup educationalGroup;

    @OneToOne(mappedBy = "headman")
    private EducationalGroup headmanGroup;

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