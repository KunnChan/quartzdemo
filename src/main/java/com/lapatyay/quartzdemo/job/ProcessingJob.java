package com.lapatyay.quartzdemo.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class ProcessingJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info("Doing some business logic...");
        try {
            Thread.sleep(1000 * 30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("Done: {}", Instant.now());
    }
}
