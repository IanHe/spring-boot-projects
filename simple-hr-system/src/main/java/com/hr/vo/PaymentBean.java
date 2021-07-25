package com.hr.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentBean implements Serializable {
    private static final long serialVersionUID = 88L;
    private String payMonth;
    private double amount;
}
