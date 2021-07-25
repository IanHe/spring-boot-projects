package com.hr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DiscriminatorColumn(name = "emp_type",
        discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue(value = "1")
public class Employee implements Serializable {
    private static final long serialVersionUID = 88L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "emp_pass", nullable = false, length = 50)
    private String pass;

    @Column(name = "emp_salary", nullable = false)
    private double salary;

    @ManyToOne(targetEntity = Manager.class)
    @JoinColumn(name = "mgr_id")
    private Manager manager;

    @Builder.Default
    @OneToMany(targetEntity = Attend.class, mappedBy = "employee")
    private Set<Attend> attends = new HashSet<>();

    @Builder.Default
    @OneToMany(targetEntity = Payment.class, mappedBy = "employee")
    private Set<Payment> payments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(name, employee.name) && Objects.equals(pass, employee.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pass);
    }
}
