package com.hr.repository;

import com.hr.domain.Employee;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepositoryImplementation<Employee, Long> {
    Optional<Employee> findByNameAndPass(String name, String pass);

    Optional<Employee> findByName(String name);
}
