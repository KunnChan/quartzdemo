package com.lapatyay.quartzdemo.controller;

import com.lapatyay.quartzdemo.model.JobRequest;
import com.lapatyay.quartzdemo.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import static com.lapatyay.quartzdemo.util.DateConverter.stringToDate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/job")
public class JobController {

    private final SchedulerService service;

    @PostMapping("/cron-schedule")
    public String cronSchedule(@RequestBody JobRequest jobRequest) throws SchedulerException {
        log.info("JobRequest : {} ", jobRequest);
        service.cronSchedule(jobRequest.jobName(), jobRequest.jobGroup(), jobRequest.cronExpression());
        return "Cron successfully scheduled!";
    }

    @PostMapping("/schedule")
    public String scheduleJob(@RequestBody JobRequest jobRequest) throws SchedulerException {
        service.scheduleJob(jobRequest.jobName(), jobRequest.jobGroup(), stringToDate(jobRequest.startTime()));
        return "Successfully scheduled!";
    }

    @PostMapping("/reschedule")
    public String rescheduleJob(@RequestBody JobRequest jobRequest) throws SchedulerException {
        service.rescheduleJob(jobRequest.jobName(), jobRequest.jobGroup(), stringToDate(jobRequest.startTime()));
        return "Successfully rescheduled!";
    }

    @PostMapping("/delete-schedule")
    public String deleteScheduledJob(@RequestBody JobRequest jobRequest) throws SchedulerException {
        service.deleteScheduledJob(jobRequest.jobName(), jobRequest.jobGroup());
        return "Successfully deleted scheduled job!";
    }

    @PostMapping("/interval-schedule")
    public String scheduleJobWithInterval(@RequestBody JobRequest jobRequest) throws SchedulerException {
        service.scheduleJobWithInterval(jobRequest.jobName(), jobRequest.jobGroup(), stringToDate(jobRequest.startTime()), stringToDate(jobRequest.endTime()), jobRequest.interval());
        return "Successfully scheduled job with interval!";
    }
    @PostMapping("/interval-reschedule")
    public String rescheduleJobWithInterval(@RequestBody JobRequest jobRequest) throws SchedulerException {
        service.rescheduleJobWithInterval(jobRequest.jobName(), jobRequest.jobGroup(), stringToDate(jobRequest.startTime()), stringToDate(jobRequest.endTime()), jobRequest.interval());
        return "Successfully rescheduled job with interval!";
    }

}
