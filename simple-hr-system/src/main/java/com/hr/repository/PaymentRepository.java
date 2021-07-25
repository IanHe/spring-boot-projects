package com.hr.repository;

import com.hr.domain.Employee;
import com.hr.domain.Payment;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;

public interface PaymentRepository extends JpaRepositoryImplementation<Payment, Long> {
    List<Payment> findByEmployee(Employee employee);
}
