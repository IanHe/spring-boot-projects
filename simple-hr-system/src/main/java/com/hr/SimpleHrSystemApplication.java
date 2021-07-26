package com.hr;

import com.hr.schedule.PayJobScheduler;
import com.hr.schedule.PunchJobScheduler;
import com.hr.schedule.TestJobScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class SimpleHrSystemApplication implements ApplicationRunner {
    @Autowired
    private PayJobScheduler payJobScheduler;
    @Autowired
    private PunchJobScheduler punchJobScheduler;
    @Autowired
    private TestJobScheduler testJobScheduler;

    public static void main(String[] args) {
        SpringApplication.run(SimpleHrSystemApplication.class, args);
    }

    @Override
    @Profile("prod") // run it on prod, not on test
    public void run(ApplicationArguments args) throws Exception {
//        payJobScheduler.start();
//        punchJobScheduler.start();
        testJobScheduler.start();
    }
}
