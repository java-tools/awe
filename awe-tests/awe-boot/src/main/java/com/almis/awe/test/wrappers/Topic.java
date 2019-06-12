package com.almis.awe.test.wrappers;

import com.almis.awe.builder.client.chart.AddPointsActionBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.DateUtil;

import java.util.Arrays;

/**
 * Test wrapper for JMS sending
 * 
 * @author pgarcia and pvidal
 */
public class Topic implements ResponseWrapper {

  private String date;
  private Float serie1;
  private Float serie2;

  @Override
  public ServiceData toServiceData() throws AWException {
    ServiceData serviceData = new ServiceData();

    DataList dataList = new DataList();
    long dateMs = DateUtil.web2TimestampWithMs(getDate()).getTime();
    DataListUtil.addColumn(dataList, "date", Arrays.asList(dateMs));
    DataListUtil.addColumn(dataList, "serie1", Arrays.asList(getSerie1()));
    DataListUtil.addColumn(dataList, "serie2", Arrays.asList(getSerie2()));

    return serviceData.addClientAction(new AddPointsActionBuilder("target", dataList).build());
  }

  /**
   * Get date
   * @return date
   */
  public String getDate() {
    return date;
  }

  /**
   * Set date
   * @param date Date
   * @return this
   */
  public Topic setDate(String date) {
    this.date = date;
    return this;
  }

  /**
   * Get serie 1
   * @return Serie 1
   */
  public Float getSerie1() {
    return serie1;
  }

  /**
   * Set serie 1
   * @param serie1 Serie 1
   * @return this
   */
  public Topic setSerie1(Float serie1) {
    this.serie1 = serie1;
    return this;
  }

  /**
   * Get serie 2
   * @return Serie 2
   */
  public Float getSerie2() {
    return serie2;
  }

  /**
   * Set serie 2
   * @param serie2 Serie 2
   * @return this
   */
  public Topic setSerie2(Float serie2) {
    this.serie2 = serie2;
    return this;
  }
}
