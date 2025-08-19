package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(schema = "people_management", name = "deanery_worker")
public class DeaneryWorker extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_profession_id", nullable = false)
    @ToString.Exclude
    private WorkerProfession workerProfession;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}