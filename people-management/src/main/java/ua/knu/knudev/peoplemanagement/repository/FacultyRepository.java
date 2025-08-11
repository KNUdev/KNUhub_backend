package ua.knu.knudev.peoplemanagement.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import ua.knu.knudev.peoplemanagement.domain.Faculty;
import ua.knu.knudev.peoplemanagement.domain.QFaculty;
import ua.knu.knudev.peoplemanagementapi.request.FacultyReceivingRequest;

import java.util.Arrays;
import java.util.UUID;

import static ua.knu.knudev.knuhubcommon.config.QEntityManagerUtil.getQueryFactory;

public interface FacultyRepository extends JpaRepository<Faculty, UUID> {

    QFaculty qFaculty = QFaculty.faculty;

    default Page<Faculty> findAllBySearchQuery(FacultyReceivingRequest request, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();

        buildPredicateBySearchQuery(request.searchQuery(), predicate);

        if (request.educationalSpecialtyCodeNames() != null) {
            predicate.and(qFaculty.educationalSpecialties.any().codeName.in(request.educationalSpecialtyCodeNames()));
        }
        if (request.educationalGroupIds() != null) {
            predicate.and(qFaculty.educationalSpecialties.any().groups.any().id.in(request.educationalGroupIds()));
        }
        if (request.userIds() != null) {
            predicate.and(qFaculty.users.any().id.in(request.userIds()));
        }

        JPAQuery<Faculty> query = getQueryFactory().selectFrom(qFaculty)
                .where(predicate)
                .orderBy(qFaculty.name.uk.asc(), qFaculty.name.en.asc())
                .offset(pageable.isUnpaged() ? 0 : pageable.getOffset())
                .limit(pageable.isUnpaged() ? Integer.MAX_VALUE : pageable.getPageSize());

        return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
    }

    private void buildPredicateBySearchQuery(String searchQuery, BooleanBuilder predicate) {
        if (StringUtils.isNoneBlank(searchQuery)) {
            Arrays.stream(searchQuery.split("\\s+"))
                    .map(part ->
                            qFaculty.name.en.containsIgnoreCase(part)
                                    .or(qFaculty.name.uk.containsIgnoreCase(part))
                                    .or(qFaculty.educationalSpecialties.any().name.en.equalsIgnoreCase(part))
                                    .or(qFaculty.educationalSpecialties.any().name.uk.equalsIgnoreCase(part))
                                    .or(qFaculty.educationalSpecialties.any().groups.any().name.en.equalsIgnoreCase(part))
                                    .or(qFaculty.educationalSpecialties.any().groups.any().name.uk.equalsIgnoreCase(part))
                                    .or(qFaculty.users.any().fullName.firstName.en.equalsIgnoreCase(part))
                                    .or(qFaculty.users.any().fullName.firstName.uk.equalsIgnoreCase(part))
                                    .or(qFaculty.users.any().fullName.lastName.en.equalsIgnoreCase(part))
                                    .or(qFaculty.users.any().fullName.lastName.uk.equalsIgnoreCase(part))
                                    .or(qFaculty.users.any().fullName.middleName.uk.equalsIgnoreCase(part))
                                    .or(qFaculty.users.any().fullName.middleName.en.equalsIgnoreCase(part)))
                    .reduce(BooleanExpression::and)
                    .ifPresent(predicate::and);
        }
    }
}
