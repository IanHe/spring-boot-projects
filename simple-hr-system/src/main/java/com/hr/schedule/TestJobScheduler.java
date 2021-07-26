package com.hr.schedule;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestJobScheduler {
    private Scheduler scheduler;

    @Autowired
    public TestJobScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void start() throws SchedulerException {
        var job = buildJob();
        var trigger = buildTrigger(job);
        scheduler.scheduleJob(job, trigger);
    }

    private JobDetail buildJob() {
        return JobBuilder.newJob(TestJob.class)
                .withIdentity("test-job", "test-job-group")
                .withDescription("test job scheduler")
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail) {
        var scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForTotalCount(20);
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "test-job-triggers")
                .withSchedule(scheduleBuilder)
                .build();
    }

    static class TestJob extends QuartzJobBean {
        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            log.info("run schedule test job ");
        }
    }
}
