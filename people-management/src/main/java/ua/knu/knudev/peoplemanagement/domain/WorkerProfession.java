package ua.knu.knudev.peoplemanagement.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.knu.knudev.knuhubcommon.domain.embeddable.MultiLanguageField;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(schema = "people_management", name = "worker_profession")
public class WorkerProfession {

    @Id
    @UuidGenerator
    private UUID id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_name")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_name"))
    })
    private MultiLanguageField name;

    @OneToMany(mappedBy = "workerProfession")
    @ToString.Exclude
    private Set<DeaneryWorker> deaneryWorkers = new HashSet<>();
}