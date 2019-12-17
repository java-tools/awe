package com.almis.awe.scheduler.job.report;

import com.almis.awe.builder.client.MessageActionBuilder;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.scheduler.enums.TaskStatus;
import com.almis.awe.service.BroadcastService;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class BroadcastReportJob extends ReportJob {

  // Autowired services
  private BroadcastService broadcastService;

  /**
   * Autowired constructor
   */
  @Autowired
  public BroadcastReportJob(BroadcastService broadcastService) {
    this.broadcastService = broadcastService;
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    // Store task and execution
    super.execute(context);

    // Generate the message action
    ClientAction message = new MessageActionBuilder(getMessageType(), getTask().getReport().getReportMessage(), getExecution().getDescription()).build();
    broadcastService.broadcastMessageToUsers(message, getTask().getReport().getReportUserDestination().toArray(new String[0]));
  }

  /**
   * Get message type for broadcast message type
   *
   * @return String
   */
  private AnswerType getMessageType() {
    switch (TaskStatus.valueOf(getExecution().getStatus())) {
      case JOB_OK:
        return AnswerType.OK;
      case JOB_ERROR:
        return AnswerType.ERROR;
      case JOB_STOPPED:
      case JOB_QUEUED:
      case JOB_RUNNING:
        return AnswerType.INFO;
      case JOB_WARNING:
      case JOB_INTERRUPTED:
      default:
        return AnswerType.WARNING;
    }
  }
}
