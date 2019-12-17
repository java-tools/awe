package com.almis.awe.scheduler.builder.task;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import lombok.extern.log4j.Log4j2;

import static com.almis.awe.scheduler.constant.TaskConstants.SCHEDULED_GROUP;

@Log4j2
public class ScheduledTaskBuilder extends TaskBuilder {

  /**
   * Constructor
   *
   * @param data Task data
   */
  public ScheduledTaskBuilder(DataList data) throws AWException {
    setData(data);
    getTask().setGroup(SCHEDULED_GROUP);
    getTask().setLauncher("scheduler");
  }
}
