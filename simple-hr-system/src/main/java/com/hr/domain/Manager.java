package com.hr.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "t_manager")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DiscriminatorValue(value = "2")
public class Manager extends Employee implements Serializable {
    private static final long serialVersionUID = 88L;

    @Column(name = "dept_name", length = 50)
    private String dept;

    @OneToMany(targetEntity = Employee.class, mappedBy = "manager")
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(targetEntity = CheckBack.class, mappedBy = "manager")
    private Set<CheckBack> checks = new HashSet<>();

    public Manager() {
    }

    @Builder(builderMethodName = "managerBuilder")
    public Manager(Long id, String name, String pass, double salary, Manager manager, Set<Attend> attends, Set<Payment> payments, String dept, Set<Employee> employees, Set<CheckBack> checks) {
        super(id, name, pass, salary, manager, attends, payments);
        this.dept = dept;
        this.employees = employees;
        this.checks = checks;
    }
}
