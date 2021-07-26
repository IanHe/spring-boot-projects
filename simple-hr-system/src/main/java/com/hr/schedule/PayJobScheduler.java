package com.hr.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayJobScheduler {
    private Scheduler scheduler;

    @Autowired
    public PayJobScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void start() throws SchedulerException {
        var jobDetail = buildJobDetail();
        var trigger = buildTrigger(jobDetail);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private JobDetail buildJobDetail() {
        return JobBuilder.newJob(PayJob.class)
                .withIdentity("cronTriggerPay", "pay-jobs")
                .withDescription("trigger auto pay at 2am on 3rd of each month")
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail) {
        var cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 2 3 * ? *");
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "pay-job-triggers")
                .withDescription("auto pay trigger")
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
