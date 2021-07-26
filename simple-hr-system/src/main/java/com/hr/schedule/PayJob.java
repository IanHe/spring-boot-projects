package com.hr.schedule;

import com.hr.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayJob extends QuartzJobBean {
    private boolean isRunning = false;
    private EmployeeService employeeService;

    @Autowired
    public PayJob(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if (!isRunning) {
            log.info("start schedule paying salary");
            isRunning = true;
            employeeService.autoPay();
            isRunning = false;
        }
    }
}
