package com.hr.repository;

import com.hr.domain.Manager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ManagerRepositoryTest {

    @Autowired
    ManagerRepository managerRepository;

    @Test
    public void test_find_by_name_and_pass() throws Exception {
        prepareSomeEmployeeRecords();
        var mgr = managerRepository.findByNameAndPass("manager", "manager");
        assertThat(mgr).isPresent();
        mgr = managerRepository.findByNameAndPass("invalidManager", "manager");
        assertThat(mgr).isNotPresent();
    }

    private void prepareSomeEmployeeRecords() {
        var manager = Manager.managerBuilder().name("manager").pass("manager").salary(11000).dept("someDepartment").build();
        var manager1 = Manager.managerBuilder().name("manager1").pass("manager1").salary(11000).dept("someDepartment").build();
        var manager2 = Manager.managerBuilder().name("manager2").pass("manager2").salary(11000).dept("someDepartment").build();
        managerRepository.saveAll(Arrays.asList(manager, manager1, manager2));
    }

}