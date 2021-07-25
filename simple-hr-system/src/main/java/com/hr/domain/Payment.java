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

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_inf")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment implements Serializable {
    private static final long serialVersionUID = 88L;

    @Id
    @Column(name = "pay_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_month", nullable = false, length = 50)
    private String payMonth;

    @Column(name = "pay_amount", nullable = false)
    private double amount;

    @ManyToOne(targetEntity = Employee.class)
    @JoinColumn(name = "emp_id", nullable = false)
    private Employee employee;
}
