package ua.knu.knudev.knuhubcommon.mapper;

import org.mapstruct.Context;
import org.mapstruct.IterableMapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

public interface BaseMapper<Domain, Dto> {

    Domain toDomain(Dto dto);

    @Named("toDomainWithContext")
    Domain toDomain(Dto dto, @Context CycleAvoidingMappingContext context);

    Dto toDto(Domain domain);

    @Named("toDtoWithContext")
    Dto toDto(Domain domain, @Context CycleAvoidingMappingContext context);

    List<Domain> toDomains(List<Dto> dtos);

    @Named("toDomainsWithContext")
    @IterableMapping(qualifiedByName = "toDomainWithContext")
    List<Domain> toDomains(List<Dto> dtos, @Context CycleAvoidingMappingContext context);

    List<Dto> toDtos(List<Domain> domains);

    @Named("toDtosWithContext")
    @IterableMapping(qualifiedByName = "toDtoWithContext")
    List<Dto> toDtos(List<Domain> domains, @Context CycleAvoidingMappingContext context);

    Set<Dto> toDtos(Set<Domain> domains);

    @Named("toDtosSetWithContext")
    @IterableMapping(qualifiedByName = "toDtoWithContext")
    Set<Dto> toDtos(Set<Domain> domains, @Context CycleAvoidingMappingContext context);

    Set<Domain> toDomains(Set<Dto> dtos);

    @Named("toDomainsSetWithContext")
    @IterableMapping(qualifiedByName = "toDomainWithContext")
    Set<Domain> toDomains(Set<Dto> dtos, @Context CycleAvoidingMappingContext context);
}