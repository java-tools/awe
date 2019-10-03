package com.almis.awe.test;

import com.almis.awe.builder.client.FillActionBuilder;
import com.almis.awe.builder.client.SelectActionBuilder;
import com.almis.awe.builder.client.grid.UpdateCellActionBuilder;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.test.bean.Denominations;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * File test class
 *
 * @author pgarcia
 */
@Service
public class Dialog extends ServiceConfig {

  private static final String INTEGER = "INTEGER";
  private static final String STRING = "STRING";

  /**
   * Fill a test dialog
   *
   * @param denominations
   * @return
   * @throws AWException
   */
  public ServiceData fillTestDialog(DataList denominations, String columnToUpdate) throws AWException {
    if (denominations == null) {
      // Create a datalist to fill the grid
      denominations = new DataListBuilder()
        .addColumn("id", Arrays.asList("1", "2", "3", "4"), STRING)
        .addColumn("valor", Arrays.asList(100, 50, 10, 5), INTEGER)
        .addColumn("numero", Arrays.asList(1, 1, 1, 1), INTEGER)
        .addColumn("cantidad", Arrays.asList(100, 50, 10, 5), INTEGER)
        .build();
    }

    // Set variables
    return new ServiceData()
      .addClientAction(new FillActionBuilder("denominations", denominations).build())
      .addClientAction(new SelectActionBuilder("columnToUpdate", columnToUpdate).build());
  }

  /**
   * Fill a test dialog
   *
   * @param denominations
   * @param address
   * @return
   * @throws AWException
   */
  public ServiceData storeTestDialog(Denominations denominations, JsonNode address, String columnName) throws AWException {
    // Create a datalist to fill the grid
    DataList denominationsDataList = new DataListBuilder()
      .addColumn("id", denominations.getId(), STRING)
      .addColumn("valor", denominations.getValor(), INTEGER)
      .addColumn("numero", denominations.getNumero(), INTEGER)
      .addColumn("cantidad", denominations.getCantidad(), INTEGER)
      .build();

    ObjectNode result = JsonNodeFactory.instance.objectNode();
    result.putPOJO("value", denominationsDataList);

    ComponentAddress cellAddress = new ComponentAddress(address);
    cellAddress.setColumn(columnName);

    // Set variables
    return new ServiceData().addClientAction(new UpdateCellActionBuilder(cellAddress, result).build());
  }
}
