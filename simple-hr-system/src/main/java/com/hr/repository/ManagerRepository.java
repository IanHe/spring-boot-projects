package com.hr.repository;

import com.hr.domain.Manager;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepositoryImplementation<Manager, Long> {
    Optional<Manager> findByName(String name);

    Optional<Manager> findByNameAndPass(String name, String pass);
}
