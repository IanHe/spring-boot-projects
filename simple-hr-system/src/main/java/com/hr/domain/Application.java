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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="application_inf")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Application implements Serializable {
    private static final long serialVersionUID = 88L;

    @Id
    @Column(name="app_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="app_reason", length = 50)
    private String reason;

    @Column(name="app_result")
    private boolean result;

    @ManyToOne(targetEntity = Attend.class)
    @JoinColumn(name = "attend_id", nullable = false)
    private Attend attend;

    @ManyToOne(targetEntity = AttendType.class)
    @JoinColumn(name="type_id", nullable = false)
    private AttendType type;

    @OneToOne(targetEntity = CheckBack.class, mappedBy = "app")
    private CheckBack check;
}
