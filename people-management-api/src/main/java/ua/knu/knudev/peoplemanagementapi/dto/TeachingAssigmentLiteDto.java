package ua.knu.knudev.peoplemanagementapi.dto;

import lombok.Builder;
import ua.knu.knudev.knuhubcommon.constant.Semester;
import ua.knu.knudev.knuhubcommon.constant.StudyCourse;

import java.util.UUID;

@Builder
public record TeachingAssigmentLiteDto(
        UUID id,
        StudyCourse studyCourse,
        Semester semester
) {
}
