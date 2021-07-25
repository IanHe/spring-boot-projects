package com.hr.service;

import com.hr.domain.Employee;
import com.hr.exception.HrException;
import com.hr.vo.AppBean;
import com.hr.vo.EmployeeBean;
import com.hr.vo.SalaryBean;

import java.util.List;

public interface ManagerService {
    /**
     * 新增员工
     *
     * @param emp 新增的员工
     * @param mgr 员工所属的经理
     */
    void addEmp(Employee emp, String mgr) throws HrException;

    /**
     * 根据经理返回所有的部门上个月工资
     *
     * @param mgr 新增的员工名
     * @return 部门上个月工资
     */
    List<SalaryBean> getSalaryByMgr(String mgr);

    /**
     * 根据经理返回该部门的全部员工
     *
     * @param mgr 经理名
     * @return 经理的全部下属
     */
    List<EmployeeBean> getEmployeesByMgr(String mgr);

    /**
     * 根据经理返回该部门的没有批复的申请
     *
     * @param mgr 经理名
     * @return 该部门的全部申请
     */
    List<AppBean> getAppsByMgr(String mgr);

    /**
     * 处理申请
     *
     * @param appid   申请ID
     * @param mgrName 经理名字
     * @param result  是否通过
     */
    void check(int appid, String mgrName, boolean result);
}
