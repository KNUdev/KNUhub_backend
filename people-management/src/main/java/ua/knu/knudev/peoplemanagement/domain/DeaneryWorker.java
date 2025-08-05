package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "people_management", name = "deanery_worker")
public class DeaneryWorker extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_profession_id", referencedColumnName = "id")
    @ToString.Exclude
    private WorkerProfession workerProfession;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}