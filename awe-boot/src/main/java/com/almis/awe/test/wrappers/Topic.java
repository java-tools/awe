package com.almis.awe.test.wrappers;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ResponseWrapper;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
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
    DataListUtil.addColumn(dataList, "date", Arrays.asList(new Long[]{dateMs}), "LONG");
    DataListUtil.addColumn(dataList, "serie1", Arrays.asList(new Float[]{getSerie1()}), "FLOAT");
    DataListUtil.addColumn(dataList, "serie2", Arrays.asList(new Float[]{getSerie2()}), "FLOAT");

    serviceData.addClientAction(new ClientAction("add-points")
            .setAsync(true)
            .setSilent(true)
            .addParameter("data", dataList));
    return serviceData;
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
