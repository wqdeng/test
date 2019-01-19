/*
 * @(#) DataWorkContext.java
 * @Author:wqdeng(mail) 2014-11-27
 * @Copyright (c) 2002-2014 wqdeng Limited. All rights reserved.
 */
package com.wqdeng.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
  * @author wqdeng(mail) 2014-11-27
  * @version 1.0
  * @Function 类功能说明
  */
public class DataWorkContext {

    /**
     * 计划任务map
     */
    private static Map<String, ScheduleJob> jobMap = new HashMap<String, ScheduleJob>();

    static {
        for (int i = 0; i < 5; i++) {
            ScheduleJob job = new ScheduleJob();
            job.setJobId("10001_" + i);
            job.setJobName("contentPullConversionTask_" + i);
            job.setJobGroup("dataWork");
            job.setJobStatus("1");
            job.setCronExpression("0/5 * * * * ?");
            job.setDescription("数据导入任务");
            addJob(job);
        }
    }

    /**
     * 添加任务
     *
     * @param scheduleJob
     */
    public static void addJob(ScheduleJob scheduleJob) {
        jobMap.put(scheduleJob.getJobGroup() + "_" + scheduleJob.getJobName(), scheduleJob);
    }

    /**
     * 获取任务
     *
     * @param jobId
     * @return
     */
    public static ScheduleJob getJob(String jobId) {
        return jobMap.get(jobId);
    }

    /**
     * 获取所有任务
     *
     * @return
     */
    public static List<ScheduleJob> getAllJob() {
        List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(jobMap.size());
        for (Iterator<Entry<String, ScheduleJob>> iterator = jobMap.entrySet().iterator(); iterator.hasNext();) {
            Entry<String, ScheduleJob> entry = iterator.next();
            jobList.add((ScheduleJob) entry.getValue());
        }
        return jobList;
    }
}
