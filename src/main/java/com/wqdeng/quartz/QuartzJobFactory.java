/*
 * @(#) QuartzJobFactory.java
 * @Author:wqdeng(mail) 2014-11-27
 * @Copyright (c) 2002-2014 wqdeng Limited. All rights reserved.
 */
package com.wqdeng.quartz;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
  * @author wqdeng(mail) 2014-11-27
  * @version 1.0
  * @Function 任务入口
  */
@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {

    private static final Logger LOG = Logger.getLogger(QuartzJobFactory.class);

    /*
     * @param context
     * @throws JobExecutionException
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOG.info("---------------");
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        LOG.info(scheduleJob.getJobName());
        LOG.info(scheduleJob.getJobGroup());
    }
}
