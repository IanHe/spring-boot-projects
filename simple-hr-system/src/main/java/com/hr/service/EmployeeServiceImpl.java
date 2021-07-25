package com.hr.service;

import com.hr.domain.Application;
import com.hr.domain.Attend;
import com.hr.domain.AttendType;
import com.hr.domain.Employee;
import com.hr.domain.Payment;
import com.hr.repository.ApplicationRepository;
import com.hr.repository.AttendRepository;
import com.hr.repository.AttendTypeRepository;
import com.hr.repository.EmployeeRepository;
import com.hr.repository.ManagerRepository;
import com.hr.repository.PaymentRepository;
import com.hr.vo.AttendBean;
import com.hr.vo.PaymentBean;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private ApplicationRepository applicationRepository;
    private AttendRepository attendRepository;
    private AttendTypeRepository attendTypeRepository;
    //    private CheckBackRepository checkBackRepository;
    private EmployeeRepository employeeRepository;
    private ManagerRepository managerRepository;
    private PaymentRepository paymentRepository;

    private SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM");
//    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public EmployeeServiceImpl(
            ApplicationRepository applicationRepository,
            AttendRepository attendRepository,
            AttendTypeRepository attendTypeRepository,
//            CheckBackRepository checkBackRepository,
            EmployeeRepository employeeRepository,
            ManagerRepository managerRepository,
            PaymentRepository paymentRepository) {
        this.applicationRepository = applicationRepository;
        this.attendRepository = attendRepository;
        this.attendTypeRepository = attendTypeRepository;
//        this.checkBackRepository = checkBackRepository;
        this.employeeRepository = employeeRepository;
        this.managerRepository = managerRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public int validLogin(Employee employee) {
        if (managerRepository.findByNameAndPass(employee.getName(), employee.getPass()).isPresent()) {
            return LOGIN_MGR;
        } else if (employeeRepository.findByNameAndPass(employee.getName(), employee.getPass()).isPresent()) {
            return LOGIN_EMP;
        }
        return LOGIN_FAIL;
    }

    /**
     * Auto punch cards, insert absence records for employee at 7 am
     */
    @Override
    public void autoPunch() {
        log.info("auto log absence");
        List<Employee> employees = employeeRepository.findAll();
        var type = attendTypeRepository.getById(6L);
        var dutyDay = new java.sql.Date(System.currentTimeMillis()).toString();
        employees.forEach(employee -> {
            // isCome: true -> on duty, false -> off duty
            var isCome = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < AM_LIMIT;
            Attend attend = Attend.builder().dutyDay(dutyDay).type(type).isCome(isCome).employee(employee).build();
            attendRepository.save(attend);
        });
    }

    /**
     * Auto pay last month salary on the 1st on month
     */
    @Override
    public void autoPay() {
        log.info("auto pay salary");
        List<Employee> employees = employeeRepository.findAll();
        // get date time of last month
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -15);
        String payMonth = dateFormat1.format(calendar.getTime());
        employees.forEach(employee -> {
            var amount = employee.getSalary();
            List<Attend> attends = attendRepository.findByEmployeeAndMonth(employee, payMonth);
            var totalAmerce = attends.stream().map(attend -> attend.getType().getAmerce()).reduce(0.0, (a, b) -> a + b);
            amount += totalAmerce;
            var payment = Payment.builder().employee(employee).payMonth(payMonth).amount(amount).build();
            paymentRepository.save(payment);
        });
    }

    /**
     * Validate if a employee has punched cards
     *
     * @param user    员工名
     * @param dutyDay 日期
     * @return
     */
    @Override
    public int validPunch(String user, String dutyDay) {
        var employee = employeeRepository.findByName(user);
        if (employee.isEmpty()) return NO_PUNCH;
        List<Attend> attends = attendRepository.findByEmployeeAndDutyDay(employee.get(), dutyDay);
        if (attends == null && attends.isEmpty()) {
            return NO_PUNCH;
        }
        // punch card - on duty
        if (attends.size() == 1 && attends.get(0).isCome() && attends.get(0).getPunchTime() == null) {
            return COME_PUNCH;
        }
        // punch card - off duty
        if (attends.size() == 1 && attends.get(0).getPunchTime() == null) {
            return LEAVE_PUNCH;
        }
        if (attends.size() == 2) {
            // can punch card on both
            if (attends.get(0).getPunchTime() == null
                    && attends.get(1).getPunchTime() == null) {
                return BOTH_PUNCH;
            }
            // punch card - off duty
            else if (attends.get(1).getPunchTime() == null) {
                return LEAVE_PUNCH;
            }

        }
        return NO_PUNCH;
    }

    /**
     * punch card
     *
     * @param user    员工名
     * @param dutyDay 打卡日期
     * @param isCome  是否是上班打卡
     * @return
     */
    @Override
    public int punch(String user, String dutyDay, boolean isCome) {
        var employee = employeeRepository.findByName(user);
        if (employee.isEmpty()) return PUNCH_FAIL;
        var attend = attendRepository.findByEmployeeAndDutyDayAndIsCome(employee.get(), dutyDay, isCome);
        if (attend.isEmpty()) return PUNCH_FAIL;
        if (attend.get().getPunchTime() != null) return PUNCHED;
        log.info("===================Punch Card=================");
        int punchHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        attend.get().setPunchTime(new Date());
        if (isCome) {
            // punch card on duty
            if (punchHour < COME_LIMIT) {
                // 9点之前算正常
                attend.get().setType(attendTypeRepository.getById(1L));
            } else if (punchHour < LATE_LIMIT) {
                // 9～11点之间算迟到
                attend.get().setType(attendTypeRepository.getById(4L));
            }
        } else {
            // punch card off duty
            if (punchHour >= LEAVE_LIMIT) {
                // 18点之后算正常
                attend.get().setType(attendTypeRepository.getById(1L));
            } else if (punchHour >= EARLY_LIMIT) {
                // 16~18点之间算早退
                attend.get().setType(attendTypeRepository.getById(5L));
            }
        }
        attendRepository.save(attend.get());
        return PUNCH_SUCC;
    }

    /**
     * view employee salary
     *
     * @param empName 员工名
     * @return
     */
    @Override
    public List<PaymentBean> empSalary(String empName) {
        var employee = employeeRepository.findByName(empName);
        if (employee.isEmpty()) return new ArrayList<>();
        List<Payment> payments = paymentRepository.findByEmployee(employee.get());
        return payments.stream().map(payment -> new PaymentBean(payment.getPayMonth(), payment.getAmount())).collect(Collectors.toList());
    }

    /**
     * view employee abnormal punch cards records
     *
     * @param empName 员工名
     * @return
     */
    @Override
    public List<AttendBean> unAttend(String empName) {
        var type = attendTypeRepository.getById(1L);
        var employee = employeeRepository.findByName(empName);
        if (employee.isEmpty()) return new ArrayList<>();
        List<Attend> attends = attendRepository.findByEmployeeUnAttendOnLastThreeDays(employee.get(), type);
        return attends.stream().map(atd -> new AttendBean(atd.getId(), atd.getDutyDay(), atd.getType().getName(),
                atd.getPunchTime())).collect(Collectors.toList());
    }

    /**
     * get all attend types
     *
     * @return List<AttendType>
     */
    @Override
    public List<AttendType> getAllType() {
        return attendTypeRepository.findAll();
    }

    /**
     * Application for Attend
     *
     * @param attId  申请的出勤ID
     * @param typeId 申请的类型ID
     * @param reason 申请的理由
     * @return boolean
     */
    @Override
    public boolean addApplication(long attId, long typeId, String reason) {
        log.info("--------------" + attId);
        log.info("~~~~" + typeId);
        log.info("~~~~" + reason);
        var attend = attendRepository.getById(attId);
        var type = attendTypeRepository.getById(typeId);
        var application = Application.builder().attend(attend).type(type).build();
        if (reason != null) application.setReason(reason);
        log.info("=======aaaaaaaaa=======");
        applicationRepository.save(application);
        return true;
    }
}
