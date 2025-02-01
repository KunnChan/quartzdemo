package com.lapatyay.quartzdemo.model;

public record JobRequest(String jobName, String jobGroup, String startTime, String endTime, int interval, String cronExpression) {
}
