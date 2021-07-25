package com.hr.service;

import com.hr.domain.AttendType;
import com.hr.domain.Employee;
import com.hr.vo.AttendBean;
import com.hr.vo.PaymentBean;

import java.util.List;

public interface EmployeeService {
    // login fail
    public static final int LOGIN_FAIL = 0;
    // normal employee login
    public static final int LOGIN_EMP = 1;
    // manager login
    public static final int LOGIN_MGR = 2;

    // cannot punch card
    public static final int NO_PUNCH = 0;
    // punch card at work place
    public static final int COME_PUNCH = 1;
    // punch card when leave work
    public static final int LEAVE_PUNCH = 2;
    // punch card before or after work
    public static final int BOTH_PUNCH = 3;

    // punch card fail
    public static final int PUNCH_FAIL = 0;
    // punched card
    public static final int PUNCHED = 1;
    // punch success
    public static final int PUNCH_SUCC = 2;

    // morning time is before 11
    public static final int AM_LIMIT = 11;


    // work time at 9
    public static final int COME_LIMIT = 9;
    // being late time between 9 and 11
    public static final int LATE_LIMIT = 11;
    // off-duty time 18
    public static final int LEAVE_LIMIT = 18;
    // leave early: 16 ~ 18
    public static final int EARLY_LIMIT = 16;

    /**
     * 以经理身份来验证登录
     *
     * @param mgr 登录的经理身份
     * @return 登录后的身份确认:0为登录失败，1为登录emp 2为登录mgr
     */
    int validLogin(Employee employee);

    /**
     * 自动打卡，周一到周五，早上7：00为每个员工插入旷工记录
     */
    void autoPunch();

    /**
     * 自动结算工资，每月1号，结算上个月工资
     */
    void autoPay();


    /**
     * 验证某个员工是否可打卡
     *
     * @param user    员工名
     * @param dutyDay 日期
     * @return 可打卡的类别
     */
    int validPunch(String user, String dutyDay);

    /**
     * 打卡
     *
     * @param user    员工名
     * @param dutyDay 打卡日期
     * @param isCome  是否是上班打卡
     * @return 打卡结果
     */
    public int punch(String user, String dutyDay, boolean isCome);

    /**
     * 根据员工浏览自己的工资
     *
     * @param empName 员工名
     * @return 该员工的工资列表
     */
    List<PaymentBean> empSalary(String empName);

    /**
     * 员工查看自己的最近三天非正常打卡
     *
     * @param empName 员工名
     * @return 该员工的最近三天的非正常打卡
     */
    List<AttendBean> unAttend(String empName);

    /**
     * 返回全部的出勤类别
     *
     * @return 全部的出勤类别
     */
    List<AttendType> getAllType();

    /**
     * 添加申请
     *
     * @param attId  申请的出勤ID
     * @param typeId 申请的类型ID
     * @param reason 申请的理由
     * @return 添加的结果
     */
    boolean addApplication(long attId, long typeId, String reason);
}
