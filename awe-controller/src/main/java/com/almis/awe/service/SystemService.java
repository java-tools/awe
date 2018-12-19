package com.almis.awe.service;

import java.util.Date;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.dto.ServiceData;

/**
 * Context help generation service
 * @author dgutierrez, pgarcia
 */
public class SystemService extends ServiceConfig {

  /**
   * Returns the system date
   *
   * @return Service Data
   */
  public ServiceData getDate() {
    // Get system version
    ServiceData serviceData = new ServiceData();
    serviceData.setDataList(new DataList());
    DataListUtil.addColumnWithOneRow(serviceData.getDataList(), "value", new Date());

    return serviceData;
  }
}
