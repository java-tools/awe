package com.almis.awe.scheduler.bean.report;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class Report implements Serializable {
  private Integer reportServerId;
  private String reportTitle;
  private String reportMessage;
  private List<String> reportEmailDestination;
  private List<String> reportUserDestination;
  private List<String> reportSendStatus;
  private String reportMaintainId;
}
