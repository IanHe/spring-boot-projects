package com.hr.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttendBean implements Serializable {
    private static final long serialVersionUID = 88L;
    private long id;
    private String dutyDay;
    private String unType;
    private Date time;
}
