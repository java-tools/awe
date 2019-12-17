package com.almis.awe.scheduler.dao;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.service.QueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Log4j2
public class DatabaseDAO {

  private static final String DB_ALIAS_LIST = "databaseAliasList";

  // Autowired services
  private QueryService queryService;

  /**
   * Autowired constructor
   *
   * @param queryService
   */
  @Autowired
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
