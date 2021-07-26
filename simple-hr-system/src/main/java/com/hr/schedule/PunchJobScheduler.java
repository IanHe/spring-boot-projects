package com.hr.schedule;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

@Component
public class PunchJobScheduler {

    private Scheduler scheduler;

    public PunchJobScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void start() throws SchedulerException {
        var job = buildJobDetail();
        var trigger = buildTrigger(job);
        scheduler.scheduleJob(job, trigger);
    }

    private JobDetail buildJobDetail(){
        return JobBuilder.newJob(PunchJob.class)
                .withIdentity("cronTriggerPunch", "punch-jobs")
                .withDescription("auto punch employee at 7am and 12pm on Mon-Fri")
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail) {
        var cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 7,12 ? * MON-FRI");
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "punch-job-triggers")
                .withDescription("trigger auto punch jobs")
                .build();
    }
}
