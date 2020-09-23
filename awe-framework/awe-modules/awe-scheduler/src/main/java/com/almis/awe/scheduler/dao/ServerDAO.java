package com.almis.awe.scheduler.dao;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.scheduler.bean.file.Server;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static com.almis.awe.scheduler.constant.QueryConstants.SCHEDULER_SERVER_DATA;

@Log4j2
public class ServerDAO {

  // Autowired services
  private final QueryService queryService;
  private final QueryUtil queryUtil;

  /**
   * Autowired constructor
   *
   * @param queryService Query service
   * @param queryUtil    Query utilities
   */
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
