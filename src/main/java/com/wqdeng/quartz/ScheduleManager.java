/*
 * @(#) ScheduleManager.java
 * @Author:wqdeng(mail) 2014-11-27
 * @Copyright (c) 2002-2014 wqdeng Limited. All rights reserved.
 */
package com.wqdeng.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;

/**
  * @author wqdeng(mail) 2014-11-27
  * @version 1.0
  * @Function 计划任务管理类
  */
public class ScheduleManager {

    private static final Logger LOG = Logger.getLogger(ScheduleManager.class);

    private Scheduler scheduler;

    /**
     * 
      * initScheduleJob(初始化JOB)
     */
    public void initScheduleJob() {
        // 获取任务信息数据 从数据库获取 这里从文件获取
        List<ScheduleJob> jobList = DataWorkContext.getAllJob();
        try {
            for (ScheduleJob scheduleJob : jobList) {

                String jobName = scheduleJob.getJobName();
                if (StringUtils.isBlank(jobName)) {
                    continue;
                }
                jobName = jobName.trim();

                String jobGroup = scheduleJob.getJobGroup();
                if (StringUtils.isBlank(jobGroup)) {
                    jobGroup = null;
                } else {
                    jobGroup = jobGroup.trim();
                }

                TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

                // 不存在,创建一个
                if (null == trigger) {
                    JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(jobName, jobGroup).build();
                    jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

                    // 表达式调度构建器
                    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

                    // 按cronExpression表达式构建一个新的trigger
                    trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();

                    scheduler.scheduleJob(jobDetail, trigger);
                } else {
                    // Trigger已存在，那么更新相应的定时设置
                    // 表达式调度构建器
                    CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

                    // 按新的cronExpression表达式重新构建trigger
                    trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                    // 按新的trigger重新设置job执行
                    scheduler.rescheduleJob(triggerKey, trigger);
                }
            }
        } catch (SchedulerException e) {
            LOG.error("initScheduleJob", e);
        }
    }

    /**
     * 
      * addScheduleJob(添加任务)
      * @param scheduleJob
     */
    public void addScheduleJob(ScheduleJob scheduleJob) {
        if (scheduleJob == null || StringUtils.isBlank(scheduleJob.getJobName())) {
            return;
        }
        String jobName = scheduleJob.getJobName().trim();
        String jobGroup = scheduleJob.getJobGroup();
        if (StringUtils.isBlank(jobGroup)) {
            jobGroup = null;
        } else {
            jobGroup = jobGroup.trim();
        }

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            if (trigger != null) {
                return;
            }

            JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(jobName, jobGroup).build();
            jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

            // 按cronExpression表达式构建一个新的trigger
            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("addScheduleJob", e);
        }
    }

    /**
     * 
      * deleteScheduleJob(删除任务)
      * @param scheduleJob
      * @throws SchedulerException
     */
    public void deleteScheduleJob(ScheduleJob scheduleJob) {
        if (scheduleJob == null || StringUtils.isBlank(scheduleJob.getJobName())) {
            return;
        }

        String jobName = scheduleJob.getJobName().trim();
        String jobGroup = scheduleJob.getJobGroup();
        if (StringUtils.isBlank(jobGroup)) {
            jobGroup = null;
        } else {
            jobGroup = jobGroup.trim();
        }

        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("deleteScheduleJob", e);
        }
    }

    /**
     * 
      * refresh(更新任务)
      * @param scheduleJob
      * @throws SchedulerException
     */
    public void refreshScheduleJob(ScheduleJob scheduleJob) {
        if (scheduleJob == null || StringUtils.isBlank(scheduleJob.getJobName())) {
            return;
        }

        String jobName = scheduleJob.getJobName().trim();
        String jobGroup = scheduleJob.getJobGroup();
        if (StringUtils.isBlank(jobGroup)) {
            jobGroup = null;
        } else {
            jobGroup = jobGroup.trim();
        }

        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

            // 获取Trigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            LOG.error("refreshScheduleJob", e);
        }
    }

    /**
     * 
      * stopScheduleJob(停止任务)
      * @param scheduleJob
      * @throws SchedulerException
     */
    public void stopScheduleJob(ScheduleJob scheduleJob) {
        if (scheduleJob == null || StringUtils.isBlank(scheduleJob.getJobName())) {
            return;
        }

        String jobName = scheduleJob.getJobName().trim();
        String jobGroup = scheduleJob.getJobGroup();
        if (StringUtils.isBlank(jobGroup)) {
            jobGroup = null;
        } else {
            jobGroup = jobGroup.trim();
        }

        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("stopScheduleJob", e);
        }
    }

    /**
     * 
      * resumeScheduleJob(重新启动任务)
      * @param scheduleJob
      * @throws SchedulerException
     */
    public void resumeScheduleJob(ScheduleJob scheduleJob) {
        if (scheduleJob == null || StringUtils.isBlank(scheduleJob.getJobName())) {
            return;
        }

        String jobName = scheduleJob.getJobName().trim();
        String jobGroup = scheduleJob.getJobGroup();
        if (StringUtils.isBlank(jobGroup)) {
            jobGroup = null;
        } else {
            jobGroup = jobGroup.trim();
        }

        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("resumeScheduleJob", e);
        }
    }

    /**
     * 
      * immediateExecution(立即执行任务)
      * @param scheduleJob
      * @throws SchedulerException
     */
    public void immediateExecution(ScheduleJob scheduleJob) {
        if (scheduleJob == null || StringUtils.isBlank(scheduleJob.getJobName())) {
            return;
        }

        String jobName = scheduleJob.getJobName().trim();
        String jobGroup = scheduleJob.getJobGroup();
        if (StringUtils.isBlank(jobGroup)) {
            jobGroup = null;
        } else {
            jobGroup = jobGroup.trim();
        }

        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            LOG.error("immediateExecution", e);
        }
    }

    /**
     * 
      * getScheduleJob(获取计划中的任务)
      * @return List<ScheduleJob>
     */
    public List<ScheduleJob> getScheduleJob() {
        List<ScheduleJob> scheduleJobs = new ArrayList<ScheduleJob>();
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    ScheduleJob scheduleJob = new ScheduleJob();
                    scheduleJob.setJobName(jobKey.getName());
                    scheduleJob.setJobGroup(jobKey.getGroup());
                    scheduleJob.setDescription("触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    scheduleJob.setJobStatus(triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        scheduleJob.setCronExpression(cronExpression);
                    }
                    scheduleJobs.add(scheduleJob);
                }
            }
        } catch (SchedulerException e) {
            LOG.error("getScheduleJob", e);
        }

        return scheduleJobs;
    }

    /**
     * 
      * getExecutingJob(获取运行中的任务)
      * @return List<ScheduleJob>
     */
    public List<ScheduleJob> getExecutingJob() {
        List<ScheduleJob> scheduleJobs = new ArrayList<ScheduleJob>();
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext executingJob : executingJobs) {
                ScheduleJob schedule = new ScheduleJob();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                schedule.setJobName(jobKey.getName());
                schedule.setJobGroup(jobKey.getGroup());
                schedule.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                schedule.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    schedule.setCronExpression(cronExpression);
                }
                scheduleJobs.add(schedule);
            }
        } catch (SchedulerException e) {
            LOG.error("getExecutingJob", e);
        }

        return scheduleJobs;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

}
