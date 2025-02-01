package com.lapatyay.quartzdemo.service;

import com.lapatyay.quartzdemo.job.ProcessingJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    private final Scheduler scheduler;

    @Override
    public void cronSchedule(String jobName, String jobGroup, String cronExpression) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ProcessingJob.class)
                .withIdentity(jobName, jobGroup)
                .build();

        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        log.info("Cron Job: {} has been scheduled", jobName);

    }

    @Override
    public void scheduleJob(String jobName, String jobGroup, Date triggerTime) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ProcessingJob.class)
                .withIdentity(jobName, jobGroup)
                .build();
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .startAt(triggerTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
        log.info("Job: {} scheduled at: {}", jobName, triggerTime);
    }

    @Override
    public void rescheduleJob(String jobName, String jobGroup, Date newTriggerTime) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        SimpleTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(newTriggerTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        scheduler.rescheduleJob(triggerKey, trigger);
        log.info("Job: {} rescheduled at: {}", jobName, newTriggerTime);

    }

    @Override
    public void deleteScheduledJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);
        log.info("Job: {} deleted at: {}", jobName, Instant.now());
    }

    @Override
    public void scheduleJobWithInterval(String jobName, String jobGroup, Date triggerTime, Date endTime, int intervalInSeconds) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ProcessingJob.class)
                .withIdentity(jobName, jobGroup)
                .build();

        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        SimpleTrigger trigger = getCalculatedTrigger(triggerTime, endTime, intervalInSeconds, triggerKey);

        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);

        log.info("Job: {} scheduled at: {} and stopping at: {}", jobName, triggerTime, endTime);
    }

    @Override
    public void rescheduleJobWithInterval(String jobName, String jobGroup, Date triggerTime, Date endTime, int intervalInSeconds) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
        SimpleTrigger trigger = getCalculatedTrigger(triggerTime, endTime, intervalInSeconds, triggerKey);

        scheduler.rescheduleJob(triggerKey, trigger);
        log.info("Job: {} rescheduled at: {} and stopping at: {}", jobName, triggerTime, endTime);

    }

    private static SimpleTrigger getCalculatedTrigger(Date startTime, Date endTime, int intervalInSeconds, TriggerKey triggerKey) {
        long durationInMillis = endTime.getTime() - startTime.getTime();
        int repeatCount = (int) (durationInMillis / TimeUnit.SECONDS.toMillis(intervalInSeconds));

        return TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .startAt(startTime)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(intervalInSeconds)
                        .withRepeatCount(repeatCount))
                .build();
    }
}
