package com.lapatyay.quartzdemo.service;

import org.quartz.SchedulerException;

import java.util.Date;

public interface SchedulerService {

    void cronSchedule(String jobName, String jobGroup, String cronExpression) throws SchedulerException;

    void scheduleJob(String jobName, String jobGroup, Date triggerTime) throws SchedulerException;
    void rescheduleJob(String jobName, String jobGroup, Date newTriggerTime) throws SchedulerException;

    void deleteScheduledJob(String jobName, String jobGroup) throws SchedulerException;

    void scheduleJobWithInterval(String jobName, String jobGroup, Date triggerTime, Date endTime, int intervalInSeconds) throws SchedulerException;
    void rescheduleJobWithInterval(String jobName, String jobGroup, Date triggerTime, Date endTime, int intervalInSeconds) throws SchedulerException;
}
