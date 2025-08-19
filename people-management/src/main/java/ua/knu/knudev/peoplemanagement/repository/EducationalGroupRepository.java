package ua.knu.knudev.peoplemanagement.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import ua.knu.knudev.peoplemanagement.domain.EducationalGroup;
import ua.knu.knudev.peoplemanagement.domain.QEducationalGroup;
import ua.knu.knudev.peoplemanagementapi.request.educationalGroup.EducationalGroupReceivingRequest;

import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.knuhubcommon.config.QEntityManagerUtil.getQueryFactory;

public interface EducationalGroupRepository extends JpaRepository<EducationalGroup, UUID> {

    QEducationalGroup qEducationalGroup = QEducationalGroup.educationalGroup;

    default Page<EducationalGroup> getFilteredEducationalGroups(
            EducationalGroupReceivingRequest request,
            Pageable pageable
    ) {
        BooleanBuilder predicate = new BooleanBuilder();

        buildPredicateBySearchQuery(request.searchQuery(), predicate);

        if (request.educationalSpecialtyCodeNames() != null) {
            predicate.and(qEducationalGroup.educationalSpecialties.any().codeName.in(request.educationalSpecialtyCodeNames()));
        }
        if (request.studentIds() != null) {
            predicate.and(qEducationalGroup.students.any().id.in(request.studentIds()));
        }
        if (request.teacherIds() != null) {
            predicate.and(qEducationalGroup.teachers.any().id.in(request.teacherIds()));
        }
        if (request.subjectIds() != null) {
            predicate.and(qEducationalGroup.subjects.any().id.in(request.subjectIds()));
        }

        JPAQuery<EducationalGroup> query = getQueryFactory().selectFrom(qEducationalGroup)
                .where(predicate)
                .orderBy(qEducationalGroup.name.uk.asc(), qEducationalGroup.name.en.asc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }

    private void buildPredicateBySearchQuery(String searchQuery, BooleanBuilder predicate) {
        if (StringUtils.isNotBlank(searchQuery)) {
            Arrays.stream(searchQuery.split("\\s+"))
                    .map(part ->
                            qEducationalGroup.name.en.containsIgnoreCase(part)
                                    .or(qEducationalGroup.name.uk.containsIgnoreCase(part))
                                    .or(qEducationalGroup.students.any().fullName.firstName.uk.containsIgnoreCase(part))
                                    .or(qEducationalGroup.students.any().fullName.firstName.en.containsIgnoreCase(part))
                                    .or(qEducationalGroup.students.any().fullName.lastName.en.containsIgnoreCase(part))
                                    .or(qEducationalGroup.students.any().fullName.lastName.uk.containsIgnoreCase(part))
                                    .or(qEducationalGroup.students.any().fullName.middleName.uk.containsIgnoreCase(part))
                                    .or(qEducationalGroup.students.any().fullName.middleName.en.containsIgnoreCase(part))
                                    .or(qEducationalGroup.teachers.any().fullName.firstName.uk.containsIgnoreCase(part))
                                    .or(qEducationalGroup.teachers.any().fullName.firstName.en.containsIgnoreCase(part))
                                    .or(qEducationalGroup.teachers.any().fullName.lastName.en.containsIgnoreCase(part))
                                    .or(qEducationalGroup.teachers.any().fullName.lastName.uk.containsIgnoreCase(part))
                                    .or(qEducationalGroup.teachers.any().fullName.middleName.uk.containsIgnoreCase(part))
                                    .or(qEducationalGroup.teachers.any().fullName.middleName.en.containsIgnoreCase(part))
                                    .or(qEducationalGroup.subjects.any().name.uk.containsIgnoreCase(part))
                                    .or(qEducationalGroup.subjects.any().name.en.containsIgnoreCase(part))
                                    .or(qEducationalGroup.educationalSpecialties.any().name.en.containsIgnoreCase(part))
                                    .or(qEducationalGroup.educationalSpecialties.any().name.uk.containsIgnoreCase(part)))
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
    }
}
