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
@Table(name = "checkback_inf")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CheckBack implements Serializable {
    private static final long serialVersionUID = 88L;

    @Id
    @Column(name = "check_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_result", nullable = false)
    private boolean result;

    @Column(name = "check_reason", length = 255)
    private String reason;

    @OneToOne(targetEntity = Application.class)
    @JoinColumn(name = "app_id", nullable = false, unique = true)
    private Application app;

    @ManyToOne(targetEntity = Manager.class)
    @JoinColumn(name = "mgr_id", nullable = false)
    private Manager manager;

}
