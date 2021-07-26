package com.hr.service;

import com.hr.domain.Application;
import com.hr.domain.CheckBack;
import com.hr.domain.Employee;
import com.hr.domain.Manager;
import com.hr.exception.HrException;
import com.hr.repository.ApplicationRepository;
import com.hr.repository.AttendRepository;
import com.hr.repository.CheckBackRepository;
import com.hr.repository.EmployeeRepository;
import com.hr.repository.ManagerRepository;
import com.hr.repository.PaymentRepository;
import com.hr.vo.ApplicationBean;
import com.hr.vo.EmployeeBean;
import com.hr.vo.SalaryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {
    private ApplicationRepository applicationRepository;
    private AttendRepository attendRepository;
    private CheckBackRepository checkBackRepository;
    private EmployeeRepository employeeRepository;
    private ManagerRepository managerRepository;
    private PaymentRepository paymentRepository;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

    @Autowired
    public ManagerServiceImpl(
            ApplicationRepository applicationRepository,
            AttendRepository attendRepository,
            CheckBackRepository checkBackRepository,
            EmployeeRepository employeeRepository,
            ManagerRepository managerRepository,
            PaymentRepository paymentRepository) {
        this.applicationRepository = applicationRepository;
        this.attendRepository = attendRepository;
        this.checkBackRepository = checkBackRepository;
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.paymentRepository = paymentRepository;
    }

    /**
     * @param emp 新增的员工
     * @param mgr 员工所属的经理
     * @throws HrException
     */
    @Override
    @Transactional
    public void addEmp(Employee emp, String mgr) throws HrException {
        var manager = getManager(mgr);
        emp.setManager(manager);
        employeeRepository.save(emp);
    }

    /**
     * get department last month salary by manager
     *
     * @param mgr 新增的员工名
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<SalaryBean> getSalaryByMgr(String mgr) throws HrException {
        Set<Employee> employees = getEmployeesByManager(mgr);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        String payMonth = dateFormat.format(calendar.getTime());
        return employees.stream()
                .map(employee -> {
                    var payment = paymentRepository.findByPayMonthAndEmployee(payMonth, employee);
                    return payment.map(pay -> new SalaryBean(employee.getName(), pay.getAmount()));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * get department employee by manager
     *
     * @param mgr 经理名
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeBean> getEmployeesByMgr(String mgr) {
        Set<Employee> employees = getEmployeesByManager(mgr);
        return employees.stream()
                .map(e -> new EmployeeBean(e.getName(), e.getPass(), e.getSalary()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationBean> getAppsByMgr(String mgr) {
        Set<Employee> employees = getEmployeesByManager(mgr);
        var results = employees.stream()
                .flatMap(e -> {
                    List<Application> apps = applicationRepository.findByEmployee(e);
                    return apps.stream()
                            .filter(app -> !app.isResult())
                            .map(app -> {
                                var attend = app.getAttend();
                                return new ApplicationBean(app.getId(), e.getName(), attend.getType().getName(),
                                        app.getType().getName(), app.getReason());
                            });
                })
                .collect(Collectors.toList());
        return results;
    }

    /**
     * Checkback applicaiton
     *
     * @param appid   申请ID
     * @param mgrName 经理名字
     * @param result  是否通过
     */
    @Override
    @Transactional
    public void check(long appid, String mgrName, boolean result) {
        var manager = getManager(mgrName);
        var application = applicationRepository.getById(appid);
        var check = CheckBack.builder().app(application).manager(manager).result(result).build();
        application.setResult(result);
        applicationRepository.save(application);
        if (result) {
            var attend = application.getAttend();
            attend.setType(application.getType());
            attendRepository.save(attend);
        }
        checkBackRepository.save(check);
    }

    private Manager getManager(String mgr) throws HrException {
        var manager = managerRepository.findByName(mgr);
        if (manager.isEmpty())
            throw new HrException("Are you manager? have you logged in?");
        return manager.get();
    }

    private Set<Employee> getEmployeesByManager(String mgr) throws HrException {
        var manager = getManager(mgr);
        Set<Employee> employees = manager.getEmployees();
        if (employees == null || employees.isEmpty())
            throw new HrException("The department has no employees");
        return employees;
    }
}
