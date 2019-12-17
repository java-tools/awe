package com.almis.awe.scheduler.builder.server;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.scheduler.bean.file.Server;
import com.almis.awe.scheduler.bean.task.Task;

public class ServerBuilder {

  private DataList serverData;
  private Task task;

  /**
   * Server builder constructor
   * @param data
   */
  public ServerBuilder(DataList data) {
    serverData = data;
  }

  /**
   * Build server bean
   * @return Server bean
   * @throws AWException
   */
  public Server build() throws AWException {
    return DataListUtil.asBeanList(serverData, Server.class).get(0);
  }
}
