package com.almis.awe.scheduler.dao;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.file.Server;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.almis.awe.scheduler.constant.QueryConstants.SCHEDULER_SERVER_DATA;

@Repository
@Log4j2
public class ServerDAO {

  // Autowired services
  private QueryService queryService;
  private QueryUtil queryUtil;

  /**
   * Autowired constructor
   *
   * @param queryService
   */
  @Autowired
  public ServerDAO(QueryService queryService, QueryUtil queryUtil) {
    this.queryService = queryService;
    this.queryUtil = queryUtil;
  }

  /**
   * Find server on database
   *
   * @param serverId
   * @return
   */
  public Server findServer(Integer serverId, String database) throws AWException {
    // Generate parameters
    ObjectNode parameters = queryUtil.getParameters(database, "1", "0");
    parameters.put("serverId", serverId);

    // Retrieve server
    List<Server> serverList = DataListUtil.asBeanList(queryService.launchPrivateQuery(SCHEDULER_SERVER_DATA, parameters).getDataList(), Server.class);
    if (!serverList.isEmpty()) {
      return serverList.get(0);
    }

    return null;
  }
}
