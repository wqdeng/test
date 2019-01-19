/*
 * @(#) ScheduleJob.java
 * @Author:wqdeng(mail) 2014-11-27
 * @Copyright (c) 2002-2014 wqdeng Limited. All rights reserved.
 */
package com.wqdeng.quartz;

/**
  * @author wqdeng(mail) 2014-11-27
  * @version 1.0
  * @Function 任务实体类
  */
public class ScheduleJob {

    /**
     * 任务ID
     */
    private String jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 任务状态
     */
    private String jobStatus;

    /**
     * 任务运行时间表达式
     */
    private String cronExpression;

    /**
     * 描述
     */
    private String description;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
