package com.almis.awe.builder.client.chart;

import com.almis.awe.builder.client.ClientActionBuilder;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.actions.ComponentAddress;

/**
 * Fill action builder
 *
 * @author pgarcia
 */
public class AddPointsActionBuilder extends ClientActionBuilder<AddPointsActionBuilder> {

  private static final String TYPE = "add-points";

  /**
   * Empty constructor
   */
  public AddPointsActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target and datalist
   *
   * @param target   Target
   * @param dataList Datalist
   */
  public AddPointsActionBuilder(String target, DataList dataList) {
    setType(TYPE)
      .setTarget(target)
      .addParameter("data", dataList);
  }

  /**
   * Constructor with address and datalist
   *
   * @param address  Target
   * @param dataList Datalist
   */
  public AddPointsActionBuilder(ComponentAddress address, DataList dataList) {
    setType(TYPE)
      .setAddress(address)
      .addParameter("data", dataList);
  }
}
