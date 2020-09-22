package com.almis.awe.dao;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.screen.data.AweThreadInitialization;
import com.almis.awe.service.QueryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.concurrent.Future;

import static com.almis.awe.model.type.LoadType.ENUM;

/*
 * File Imports
 */

/**
 * Initial load runner
 * Launches initial load values
 *
 * @author Pablo GARCIA - 20/MAR/2017
 */
public class InitialLoadDao {

  // Autowired services
  private final QueryService queryService;

  /**
   * Autowired constructor
   *
   * @param queryService Query service
   */
  public InitialLoadDao(QueryService queryService) {
    this.queryService = queryService;
  }

  /**
   * Launch initial load runner
   *
   * @param initializationData Initialization data
   * @return Future service data
   * @throws AWException AWE exception
   */
  @Async("threadPoolTaskExecutor")
  public Future<ServiceData> launchInitialLoad(AweThreadInitialization initializationData) throws AWException {
    try {
      if (ENUM.equals(initializationData.getInitialLoadType())) {
        return new AsyncResult<>(queryService.launchEnumQuery(initializationData.getTarget()));
      } else {
        return new AsyncResult<>(queryService.launchQuery(initializationData.getTarget(), initializationData.getParameters()));
      }
    } catch (AWException exc) {
      throw exc;
    } catch (Exception exc) {
      throw new AWException("Error launching initial load", exc.getMessage(), exc);
    }
  }
}