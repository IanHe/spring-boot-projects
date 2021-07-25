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
public class AppBean implements Serializable {
    private static final long serialVersionUID = 88L;

    private int id;
    private String emp;
    private String unAttend;
    private String toAttend;
    private String reason;

}
