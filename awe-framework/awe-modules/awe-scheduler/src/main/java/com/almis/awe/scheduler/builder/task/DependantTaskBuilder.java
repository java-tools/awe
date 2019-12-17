package com.almis.awe.scheduler.builder.task;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import lombok.extern.log4j.Log4j2;

import static com.almis.awe.scheduler.constant.TaskConstants.DEPENDENCY_GROUP;

@Log4j2
public class DependantTaskBuilder extends TaskBuilder {

  /**
   * Constructor
   *
   * @param data Task data
   */
  public DependantTaskBuilder(DataList data) throws AWException {
    setData(data);
    getTask().setGroup(DEPENDENCY_GROUP);
  }
}
