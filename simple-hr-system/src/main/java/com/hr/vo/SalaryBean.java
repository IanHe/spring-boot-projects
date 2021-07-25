package com.hr.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SalaryBean implements Serializable {
    private static final long serialVersionUID = 88L;
    private String empName;
    private double amount;
}
