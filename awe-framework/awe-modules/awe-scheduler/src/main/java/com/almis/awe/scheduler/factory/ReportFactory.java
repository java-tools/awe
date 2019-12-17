package com.almis.awe.scheduler.factory;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.scheduler.enums.ReportType;
import com.almis.awe.scheduler.job.report.BroadcastReportJob;
import com.almis.awe.scheduler.job.report.EmailReportJob;
import com.almis.awe.scheduler.job.report.MaintainReportJob;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;

/**
 * @author dfuentes
 */
@Log4j2
public class ReportFactory extends ServiceConfig {

  // Private constructor
  private ReportFactory() {
  }

  /**
   * Gets the correct job with the given job type
   *
   * @return IExecutionJob
   * @throws AWException
   */
  public static JobDetail getInstance(ReportType reportType, JobDataMap dataMap) {
    switch (reportType) {
      case EMAIL:
        return JobBuilder.newJob().ofType(EmailReportJob.class)
          .setJobData(dataMap)
          .build();
      case BROADCAST:
        return JobBuilder.newJob().ofType(BroadcastReportJob.class)
          .setJobData(dataMap)
          .build();
      case MAINTAIN:
        return JobBuilder.newJob().ofType(MaintainReportJob.class)
          .setJobData(dataMap)
          .build();
      default:
        return null;
    }
  }
}
