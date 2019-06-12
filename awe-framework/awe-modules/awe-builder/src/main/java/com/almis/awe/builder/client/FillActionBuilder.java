package com.almis.awe.builder.client;

import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.actions.ComponentAddress;

/**
 * Fill action builder
 *
 * @author pgarcia
 */
public class FillActionBuilder extends ClientActionBuilder<FillActionBuilder> {

  private static final String TYPE = "fill";

  /**
   * Empty constructor
   */
  public FillActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target and datalist
   *
   * @param target   Target
   * @param dataList Datalist
   */
  public FillActionBuilder(String target, DataList dataList) {
    setType(TYPE)
      .setTarget(target)
      .addParameter("datalist", dataList);
  }

  /**
   * Constructor with address and datalist
   *
   * @param address  Target
   * @param dataList Datalist
   */
  public FillActionBuilder(ComponentAddress address, DataList dataList) {
    setType(TYPE)
      .setAddress(address)
      .addParameter("datalist", dataList);
  }
}
