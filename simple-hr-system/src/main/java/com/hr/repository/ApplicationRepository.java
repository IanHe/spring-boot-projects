package com.hr.repository;

import com.hr.domain.Application;
import com.hr.domain.Employee;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface ApplicationRepository extends JpaRepositoryImplementation<Application, Long> {

    List<Application> findByAttend_Employee(Employee employee);
}
