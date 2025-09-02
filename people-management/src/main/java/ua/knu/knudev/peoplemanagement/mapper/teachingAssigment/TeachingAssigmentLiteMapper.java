package ua.knu.knudev.peoplemanagement.mapper.teachingAssigment;

import org.mapstruct.Mapper;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.TeachingAssigment;
import ua.knu.knudev.peoplemanagementapi.dto.teachingAssigment.TeachingAssigmentLiteDto;

@Mapper(componentModel = "spring")
public interface TeachingAssigmentLiteMapper extends BaseMapper<TeachingAssigment, TeachingAssigmentLiteDto> {
}
