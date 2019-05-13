package com.almis.awe.test;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * File test class
 *
 * @author pgarcia
 */
@Service
public class Grid extends ServiceConfig {

  @Autowired
  QueryService queryService;

  /**
   * Add some columns to a grid
   *
   * @return Service output
   */
  public ServiceData addColumns() {
    // Generate service data
    final String ADD_COLUMNS = "add-columns";
    ServiceData serviceData = new ServiceData();
    List<ClientAction> clientActionList = serviceData.getClientActionList();

    // Add columns to some grids
    clientActionList.add(getAction(ADD_COLUMNS, "GrdMus"));
    clientActionList.add(getAction(ADD_COLUMNS, "GrdSta"));
    clientActionList.add(getAction(ADD_COLUMNS, "GrdEdi"));
    clientActionList.add(getAction(ADD_COLUMNS, "GrdMuo"));

    // Set variables
    serviceData.setClientActionList(clientActionList);
    return serviceData;
  }

  /**
   * Replace all columns from a grid
   *
   * @param fechas Dates
   * @return Service data
   */
  public ServiceData replaceColumns(List<Date> fechas) {
    // Generate service data
    ServiceData serviceData = new ServiceData();
    List<ClientAction> clientActionList = serviceData.getClientActionList();

    // Replace columns to multiselect grid
    clientActionList.add(getAction("replace-columns", "GrdMus"));

    // Set variables
    serviceData.setClientActionList(clientActionList);
    return serviceData;
  }

  /**
   * Action list to change columns over some grids
   *
   * @return Client action
   */
  private ClientAction getAction(String action, String gridId) {
    List<Column> columns = new ArrayList<>();

    // Add first column
    columns.add((Column) new Column()
            .setAlign("right")
            .setCharLength(20)
            .setValue("1")
            .setName(gridId + "-newColumn1")
            .setLabel("BUTTON_NEW ELEMENT_TYPE_COLUMN 1"));

    // Add second column
    columns.add((Column) new Column()
            .setAlign("left")
            .setCharLength(20)
            .setValue("aaaa")
            .setName(gridId + "-newColumn2")
            .setLabel("BUTTON_NEW ELEMENT_TYPE_COLUMN 2"));

    // Add columns to the grid
    ClientAction addColumnsGrid = new ClientAction(action);
    addColumnsGrid.setTarget(gridId);
    addColumnsGrid.addParameter("columns", new CellData(columns));

    return addColumnsGrid;
  }

  /**
   * Get a result from a query
   * @param query Query
   * @return ServiceData
   * @throws AWException Error retrieving query data
   */
  public ServiceData getServiceData(String query) throws AWException {
    return queryService.launchQuery(query, "1", "0");
  }

  /**
   * Get a result from a query
   * @param selectedRows Selected rows
   * @return ServiceData
   * @throws AWException Error retrieving query data
   */
  public ServiceData convertIcon(List<Integer> selectedRows) throws AWException {
    ServiceData serviceData = new ServiceData();
    for (Integer selectedRow : selectedRows) {
      ComponentAddress address = new ComponentAddress("report", "GrdEdi", selectedRow.toString(),  "Ico");
      ObjectNode nodeData = JsonNodeFactory.instance.objectNode();
      nodeData.put("icon", "fa-empire");
      nodeData.put("style", "text-danger");
      nodeData.put("value", "fa-empire");
      serviceData.addClientAction(new ClientAction("update-cell")
              .setAddress(address)
              .setSilent(true)
              .setAsync(true)
              .addParameter("data", nodeData));
    }

    return serviceData;
  }
}
