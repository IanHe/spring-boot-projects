package com.hr.repository;

import com.hr.domain.Employee;
import com.hr.domain.Payment;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepositoryImplementation<Payment, Long> {
    List<Payment> findByEmployee(Employee employee);

    Optional<Payment> findByPayMonthAndEmployee(String payMonth, Employee employee);
}
