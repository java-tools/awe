package com.almis.awe.builder.client;

import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.actions.ComponentAddress;

/**
 * Fill action builder
 *
 * @author pgarcia
 */
public class UpdateControllerActionBuilder extends ClientActionBuilder<UpdateControllerActionBuilder> {

  private static final String TYPE = "update-controller";
  private static final String ATTRIBUTE = "attribute";
  private static final String DATALIST = "datalist";
  private static final String VALUE = "value";

  /**
   * Empty constructor
   */
  public UpdateControllerActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with target and datalist
   *
   * @param target   Target
   * @param dataList Datalist
   */
  public UpdateControllerActionBuilder(String target, String attribute, DataList dataList) {
    setType(TYPE)
      .setTarget(target)
      .addParameter(ATTRIBUTE, attribute)
      .addParameter(DATALIST, dataList);
  }

  /**
   * Constructor with target and datalist
   *
   * @param target   Target
   * @param value Value
   */
  public UpdateControllerActionBuilder(String target, String attribute, Object value) {
    setType(TYPE)
      .setTarget(target)
      .addParameter(ATTRIBUTE, attribute)
      .addParameter(VALUE, value);
  }

  /**
   * Constructor with address and datalist
   *
   * @param address  Target
   * @param dataList Datalist
   */
  public UpdateControllerActionBuilder(ComponentAddress address, String attribute, DataList dataList) {
    setType(TYPE)
      .setAddress(address)
      .addParameter(ATTRIBUTE, attribute)
      .addParameter(DATALIST, dataList);
  }

  /**
   * Constructor with address and datalist
   *
   * @param address  Target
   * @param value Value
   */
  public UpdateControllerActionBuilder(ComponentAddress address, String attribute, Object value) {
    setType(TYPE)
      .setAddress(address)
      .addParameter(ATTRIBUTE, attribute)
      .addParameter(VALUE, value);
  }
}
