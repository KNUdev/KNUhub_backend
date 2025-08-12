package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.dto.MultiLanguageFieldDto;

import java.util.List;
import java.util.UUID;

@Builder
public record WorkerProfessionDto(
        UUID id,
        MultiLanguageFieldDto name,
        List<DeaneryWorkerDto> deaneryWorkers
) {
}
