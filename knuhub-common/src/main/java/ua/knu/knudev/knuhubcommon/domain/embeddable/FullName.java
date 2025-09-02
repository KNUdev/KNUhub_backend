package ua.knu.knudev.knuhubcommon.domain.embeddable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class FullName {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_firstName")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_firstName"))
    })
    private MultiLanguageField firstName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_middleName")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_middleName"))
    })
    private MultiLanguageField middleName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "en_lastName")),
            @AttributeOverride(name = "uk", column = @Column(name = "uk_lastName"))
    })
    private MultiLanguageField lastName;
}
