package com.almis.awe.test;

import com.almis.awe.builder.client.FillActionBuilder;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.service.data.builder.DataListBuilder;
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

  /**
   * Fill a modal dialog
   *
   * @param defaultNumber
   * @return
   * @throws AWException
   */
  public ServiceData modalDialog(Float defaultNumber) throws AWException {
    // Create a datalist to fill the grid
    DataList denominations = new DataListBuilder()
      .addColumn("id", Arrays.asList(1, 2, 3, 4), INTEGER)
      .addColumn("valor", Arrays.asList(100, 50, 10, 5), INTEGER)
      .addColumn("numero", Arrays.asList(1, 1, 1, 1), INTEGER)
      .addColumn("cantidad", Arrays.asList(100, 50, 10, 5), INTEGER)
      .build();

    // Set variables
    return new ServiceData().addClientAction(new FillActionBuilder("denominations", denominations).build());
  }
}
