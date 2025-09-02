package ua.knu.knudev.peoplemanagement.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ua.knu.knudev.knuhubcommon.mapper.BaseMapper;
import ua.knu.knudev.peoplemanagement.domain.User;
import ua.knu.knudev.peoplemanagementapi.dto.user.UserLiteDto;

@Mapper(componentModel = "spring")
public interface UserLiteMapper extends BaseMapper<User, UserLiteDto> {

    @Mappings({
            @Mapping(source = "fullName.firstName.uk", target = "fullName.firstName.uk"),
            @Mapping(source = "fullName.firstName.en", target = "fullName.firstName.en"),
            @Mapping(source = "fullName.lastName.uk", target = "fullName.lastName.uk"),
            @Mapping(source = "fullName.lastName.en", target = "fullName.lastName.en"),
            @Mapping(source = "fullName.middleName.uk", target = "fullName.middleName.uk"),
            @Mapping(source = "fullName.middleName.en", target = "fullName.middleName.en")
    })
    UserLiteDto toDto(User user);

    @Mappings({
            @Mapping(source = "fullName.firstName.uk", target = "fullName.firstName.uk"),
            @Mapping(source = "fullName.firstName.en", target = "fullName.firstName.en"),
            @Mapping(source = "fullName.lastName.uk", target = "fullName.lastName.uk"),
            @Mapping(source = "fullName.lastName.en", target = "fullName.lastName.en"),
            @Mapping(source = "fullName.middleName.uk", target = "fullName.middleName.uk"),
            @Mapping(source = "fullName.middleName.en", target = "fullName.middleName.en")
    })
    User toDomain(UserLiteDto userLiteDto);
}
