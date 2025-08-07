package ua.knu.knudev.knuhubcommon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "Represents full name with first, middle, last names.")
@Builder
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class FullNameDto {
    @Schema(description = "First name")
    @NotNull(message = "First name field cannot be empty.")
    private MultiLanguageFieldDto firstName;

    @Schema(description = "Middle name")
    @NotNull(message = "Middle name field cannot be empty.")
    private MultiLanguageFieldDto middleName;

    @Schema(description = "Last name")
    @NotNull(message = "Last name field cannot be empty.")
    private MultiLanguageFieldDto lastName;
}
