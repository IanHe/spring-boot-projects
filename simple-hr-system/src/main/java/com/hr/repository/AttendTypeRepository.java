package com.hr.repository;

import com.hr.domain.AttendType;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendTypeRepository extends JpaRepositoryImplementation<AttendType, Long> {
    Optional<AttendType> findByName(String name);
}
