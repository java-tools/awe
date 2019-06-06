package com.almis.awe.builder.client.grid;

import com.almis.awe.builder.client.ClientActionBuilder;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Update cell action builder
 *
 * @author pgarcia
 */
public class UpdateCellActionBuilder extends ClientActionBuilder<UpdateCellActionBuilder> {

  private static final String TYPE = "update-cell";

  /**
   * Empty constructor
   */
  public UpdateCellActionBuilder() {
    setType(TYPE);
  }

  /**
   * Constructor with address and celldata
   * @param address Cell address
   * @param cellData Cell data
   */
  public UpdateCellActionBuilder(ComponentAddress address, CellData cellData) {
    setType(TYPE)
      .setAsync(true)
      .setSilent(true)
      .setAddress(address)
      .addParameter("data", cellData);
  }

  /**
   * Constructor with address and jsonNode
   * @param address Cell address
   * @param jsonData Json data
   */
  public UpdateCellActionBuilder(ComponentAddress address, JsonNode jsonData) {
    setType(TYPE)
      .setAsync(true)
      .setSilent(true)
      .setAddress(address)
      .addParameter("data", jsonData);
  }
}
