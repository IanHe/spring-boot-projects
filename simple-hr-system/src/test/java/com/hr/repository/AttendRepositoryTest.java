package com.hr.repository;

import com.hr.domain.Attend;
import com.hr.domain.AttendType;
import com.hr.domain.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AttendRepositoryTest {
    @Autowired
    AttendRepository attendRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    AttendTypeRepository attendTypeRepository;

    @Test
    public void test_findByEmployeeAndMonth() {
        prepareSomeData();
        var employee1 = employeeRepository.findByName("someName1").get();
        var employee2 = employeeRepository.findByName("someName2").get();
        var attends = attendRepository.findByEmployeeAndMonth(employee1, "2020-08");
        assertThat(attends.size()).isEqualTo(2);
        attends = attendRepository.findByEmployeeAndMonth(employee2, "2020-08");
        assertThat(attends.size()).isEqualTo(1);
        attends = attendRepository.findByEmployeeAndMonth(employee2, "2020-09");
        assertThat(attends).isEmpty();
    }

    @Test
    public void test_findByEmployeeUnAttend() {
        prepareSomeData();
        var employee = employeeRepository.findByName("someName1").get();
        var attendType = attendTypeRepository.findByName("attend").get();
        List<Attend> unAttends = attendRepository.findByEmployeeUnAttendOnLastThreeDays(employee, attendType);
        assertThat(unAttends.size()).isEqualTo(2);
    }

    private void prepareSomeData() {
        var type1 = AttendType.builder().name("attend").amerce(0).build();
        var type2 = AttendType.builder().name("unAttend").amerce(100).build();
        attendTypeRepository.saveAll(Arrays.asList(type1, type2));

        var employee1 = Employee.builder().name("someName1").pass("somePass1").salary(11000).build();
        var employee2 = Employee.builder().name("someName2").pass("somePass2").salary(11000).build();
        employeeRepository.saveAll(Arrays.asList(employee1, employee2));

        var attend1 = Attend.builder().dutyDay("2020-08-09").employee(employee1).isCome(true).type(type1).build();
        var attend2 = Attend.builder().dutyDay("2020-08-20").employee(employee1).isCome(false).type(type2).build();
        var attend3 = Attend.builder().dutyDay("2020-08-05").employee(employee2).isCome(true).type(type2).build();
        var today = new java.sql.Date(System.currentTimeMillis()).toString();
        var attend4 = Attend.builder().dutyDay(today).employee(employee1).isCome(true).type(type1).build();
        var attend5 = Attend.builder().dutyDay(today).employee(employee1).isCome(false).type(type2).build();
        var attend6 = Attend.builder().dutyDay(today).employee(employee1).isCome(false).type(type2).build();
        attendRepository.saveAll(Arrays.asList(attend1, attend2, attend3, attend4, attend5, attend6));
    }
}