package com.hr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "attend_inf")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attend implements Serializable {
    private static final long serialVersionUID = 88L;

    @Id
    @Column(name = "attend_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "duty_day", nullable = false, length = 50)
    private String dutyDay;

    @Column(name = "punch_time")
    private Date punchTime;

    @Column(name = "is_come", nullable = false)
    private boolean isCome;

    // Uni-directional
    @ManyToOne(targetEntity = AttendType.class)
    @JoinColumn(name = "type_id", nullable = false)
    private AttendType type;

    @ManyToOne(targetEntity = Employee.class)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attend attend = (Attend) o;
        return isCome == attend.isCome && Objects.equals(dutyDay, attend.dutyDay) && Objects.equals(employee, attend.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dutyDay, isCome, employee);
    }
}
