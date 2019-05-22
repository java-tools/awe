package com.almis.awe.service;

import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/*
 * File Imports
 */
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.exception.AWException;

/**
 * PrinterService Class
 * Provides server printer information
 *
 * @author Aitor UGARTE - 04/JUN/2013
 */
public class PrinterService extends ServiceConfig {

  /**
   * Provides printer List
   *
   * @return Service Output Messages
   * @throws AWException Error retrieving printers
   */
  public ServiceData getPrinterList() throws AWException {
    ServiceData serviceData = new ServiceData();
    List<String> printerList = new ArrayList<>();

    try {
      // Retrieve accessible printers from server
      PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

      // For each printer, store path
      for (PrintService printer : printServices) {
        printerList.add(printer.getName());
      }
      DataList dataList = new DataList();
      DataListUtil.addColumn(dataList, "printer", printerList);
      dataList.setRecords(printerList.size());
      serviceData.setDataList(dataList);
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_GET_PRN"), getLocale("ERROR_MESSAGE_GET_PRN"), exc);
    }
    return serviceData;
  }
}