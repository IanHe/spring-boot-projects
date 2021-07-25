package com.hr.repository;

//import com.hr.H2JpaConfig;

import com.hr.domain.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    public void test_create_and_save() throws Exception {
        var employee = new Employee();
        employee.setName("someName");
        employee.setPass("somePass");
        employee.setSalary(11000);
        var savedEmployee = employeeRepository.save(employee);
        assertThat(savedEmployee.getId()).isNotNull();
    }
    
    @Test
    public void test_throw_exception_when_save_two_employees_with_the_same_name() {
        prepareSomeEmployeeRecords();
        var employee = Employee.builder().name("someName").pass("somePass3").salary(11000).build();
        var exception = assertThrows(DataIntegrityViolationException.class, () -> {
            employeeRepository.save(employee);
        });
        assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void test_find_by_name_and_pass() throws Exception {
        prepareSomeEmployeeRecords();
        var emp = employeeRepository.findByNameAndPass("someName", "somePass");
        assertThat(emp).isPresent();
        emp = employeeRepository.findByNameAndPass("notAvailable", "notAvailable");
        assertThat(emp).isNotPresent();
    }

    private void prepareSomeEmployeeRecords() {
        var employee = Employee.builder().name("someName").pass("somePass").salary(11000).build();
        var employee1 = Employee.builder().name("someName1").pass("somePass1").salary(11000).build();
        var employee2 = Employee.builder().name("someName2").pass("somePass2").salary(11000).build();
        employeeRepository.saveAll(Arrays.asList(employee, employee1, employee2));
    }
}