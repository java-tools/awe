package com.almis.awe.scheduler.builder.task;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import lombok.extern.log4j.Log4j2;

import static com.almis.awe.scheduler.constant.TaskConstants.MANUAL_GROUP;

@Log4j2
public class ManualTaskBuilder extends TaskBuilder {

  /**
   * Constructor
   *
   * @param data Task data
   */
  public ManualTaskBuilder(DataList data) throws AWException {
    setData(data);
    getTask().setGroup(MANUAL_GROUP);
  }
}
