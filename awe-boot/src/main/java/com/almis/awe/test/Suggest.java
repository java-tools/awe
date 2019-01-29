package com.almis.awe.test;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Suggest
 *
 * @author pvidal
 */
@Service
public class Suggest extends ServiceConfig {

  @Autowired
  QueryService queryService;

  /**
   * Get suggest with a delay
   * @param queryName Query name
   * @return User list
   * @throws AWException error in query
   * @throws InterruptedException Error in thread wait
   */
  public ServiceData delayedSuggest(String queryName) throws AWException, InterruptedException {
    Thread.sleep(1000);
    return queryService.launchQuery(queryName);
  }
}
