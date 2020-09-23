package com.almis.awe.scheduler.dao;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.service.QueryService;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class DatabaseDAO {

  private static final String DB_ALIAS_LIST = "databaseAliasList";

  // Autowired services
  private final QueryService queryService;

  /**
   * Autowired constructor
   *
   * @param queryService Query service
   */
  public DatabaseDAO(QueryService queryService) {
    this.queryService = queryService;
  }

  /**
   * Get database alias list
   *
   * @return
   * @throws AWException
   */
  public List<String> getDBAliasList() throws AWException {
    // Launch on the first connection
    return DataListUtil.getColumn(queryService.launchQuery(DB_ALIAS_LIST).getDataList(), "Als")
      .stream()
      .map(CellData::getStringValue)
      .collect(Collectors.toList());
  }
}
