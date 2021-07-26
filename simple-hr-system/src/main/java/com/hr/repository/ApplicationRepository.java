package com.hr.repository;

import com.hr.domain.Application;
import com.hr.domain.Employee;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface ApplicationRepository extends JpaRepositoryImplementation<Application, Long> {

    default List<Application> findByEmployee(Employee employee) {
        return findAll(((root, query, criteriaBuilder) ->
                query.where(
                        criteriaBuilder.equal(root.get("attend").get("employee"), employee)
                ).getRestriction()
        ));
    }
}
