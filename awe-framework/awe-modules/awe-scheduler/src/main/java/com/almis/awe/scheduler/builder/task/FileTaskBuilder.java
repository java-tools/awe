package com.almis.awe.scheduler.builder.task;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.scheduler.bean.file.File;
import lombok.extern.log4j.Log4j2;

import static com.almis.awe.scheduler.constant.TaskConstants.FILE_TRACKING_GROUP;

@Log4j2
public class FileTaskBuilder extends TaskBuilder {

  /**
   * Constructor
   *
   * @param data Task data
   */
  public FileTaskBuilder(DataList data) throws AWException {
    setData(data);
    getTask().setGroup(FILE_TRACKING_GROUP);
    getTask().setFile(DataListUtil.asBeanList(data, File.class).get(0));
  }
}
