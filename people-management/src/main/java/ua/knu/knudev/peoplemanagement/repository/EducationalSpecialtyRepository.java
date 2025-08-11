package ua.knu.knudev.peoplemanagement.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import ua.knu.knudev.peoplemanagement.domain.EducationalSpecialty;
import ua.knu.knudev.peoplemanagement.domain.QEducationalSpecialty;
import ua.knu.knudev.peoplemanagementapi.request.EducationalSpecialtyReceivingRequest;

import java.util.Arrays;

import static ua.knu.knudev.knuhubcommon.config.QEntityManagerUtil.getQueryFactory;

public interface EducationalSpecialtyRepository extends JpaRepository<EducationalSpecialty, String> {
    boolean existsByCodeName(String codeName);

    QEducationalSpecialty qEducationalSpecialty = QEducationalSpecialty.educationalSpecialty;

    default Page<EducationalSpecialty> findAllBySearchQuery(EducationalSpecialtyReceivingRequest request, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();

        buildPredicateBySearchQuery(request.searchQuery(), predicate);

        if (request.educationalGroupIds() != null) {
            predicate.and(qEducationalSpecialty.groups.any().id.in(request.educationalGroupIds()));
        }
        if (request.studentIds() != null) {
            predicate.and(qEducationalSpecialty.students.any().id.in(request.studentIds()));
        }
        if (request.teacherIds() != null) {
            predicate.and(qEducationalSpecialty.teachers.any().id.in(request.teacherIds()));
        }
        if (request.teachingAssigmentIds() != null) {
            predicate.and(qEducationalSpecialty.teachingAssigments.any().id.in(request.teachingAssigmentIds()));
        }

        JPAQuery<EducationalSpecialty> query = getQueryFactory().selectFrom(qEducationalSpecialty)
                .where(predicate)
                .orderBy(qEducationalSpecialty.name.uk.asc(), qEducationalSpecialty.name.en.asc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }

    private void buildPredicateBySearchQuery(String searchQuery, BooleanBuilder predicate) {
        if (StringUtils.isNotBlank(searchQuery)) {
            Arrays.stream(searchQuery.split("\\s+"))
                    .map(part ->
                            qEducationalSpecialty.name.en.containsIgnoreCase(part)
                                    .or(qEducationalSpecialty.name.uk.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.faculties.any().name.en.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.faculties.any().name.uk.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.students.any().fullName.firstName.en.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.students.any().fullName.firstName.uk.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.students.any().fullName.lastName.en.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.students.any().fullName.lastName.uk.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.students.any().fullName.middleName.en.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.students.any().fullName.middleName.uk.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.teachers.any().fullName.firstName.en.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.teachers.any().fullName.firstName.uk.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.teachers.any().fullName.lastName.en.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.teachers.any().fullName.lastName.uk.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.teachers.any().fullName.middleName.en.containsIgnoreCase(part))
                                    .or(qEducationalSpecialty.teachers.any().fullName.middleName.uk.containsIgnoreCase(part)))
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
    }
}
