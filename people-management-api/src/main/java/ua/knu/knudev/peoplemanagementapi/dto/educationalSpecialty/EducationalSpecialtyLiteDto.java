package ua.knu.knudev.peoplemanagementapi.dto.educationalSpecialty;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

@Builder
public record EducationalSpecialtyLiteDto(
        String codeName,
        MultiLanguageFieldDto name
) {
}
