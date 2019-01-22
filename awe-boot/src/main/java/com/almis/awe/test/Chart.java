package com.almis.awe.test;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;
import com.almis.awe.model.entities.screen.component.chart.ChartSeriePoint;
import com.almis.awe.model.util.security.EncodeUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * File Chart class
 *
 * @author pvidal
 */
@Service
public class Chart extends ServiceConfig {

  private static final String SCREEN_TEST = "ChrLinTst";
  private static final String SERIES = "series";

  /**
   * Test method to replace the series of chart
   *
   * @param userList (Dummy list for build name of series)
   * @return Service output
   */
  public ServiceData replaceSeriesChart(List<String> userList) {

    // Init variables
    ServiceData serviceData = new ServiceData();

    // Create arrayNode of chart series
    List<ChartSerie> serieList = getSerieList(userList);

    // Create action replace series of chart
    serviceData.addClientAction(new ClientAction("replace-chart-series")
      .setTarget(SCREEN_TEST)
      .addParameter(SERIES, new CellData(serieList)));

    return serviceData;
  }

  /**
   * Retrieve serie list
   *
   * @param userList (Dummy list for build name of series)
   * @return Chart serie
   */
  private List<ChartSerie> getSerieList(List<String> userList) {
    // Init variables
    JsonNodeFactory factory = JsonNodeFactory.instance;
    List<ChartSerie> serieList = new ArrayList<>();

    // Get month list for xAxis
    List<String> months = builDummyMonthList();

    // Add json data of series
    for (String user : userList) {
      // New serie
      ChartSerie serie = new ChartSerie();
      serie.setId(user);
      serie.setName(user);

      // Add data to serie
      for (String month : months) {
        ChartSeriePoint point = new ChartSeriePoint(factory.textNode(month), factory.numberNode(EncodeUtil.getSecureRandom().nextInt((10 - 0) + 1) + 0));
        // Add point to serie [x,y]
        serie.getData().add(point);
      }
      // Add serie
      serieList.add(serie);
    }
    return serieList;
  }

  /**
   * Test method to add the series of chart
   *
   * @param userList (Dummy list for build name of series)
   * @return Service output
   */
  public ServiceData addSeriesChart(List<String> userList) {

    // Init variables
    ServiceData serviceData = new ServiceData();

    // Create arrayNode of chart series
    List<ChartSerie> serieList = getSerieList(userList);

    // Create action replace series of chart
    serviceData.addClientAction(new ClientAction("add-chart-series")
      .setTarget(SCREEN_TEST)
      .addParameter(SERIES, new CellData(serieList)));

    return serviceData;
  }

  /**
   * Test method to remove the series of chart
   *
   * @param userList (Dummy list for build name of series)
   * @return Service data
   */
  public ServiceData removeSeriesChart(List<String> userList) {

    // Init variables
    ServiceData serviceData = new ServiceData();

    // Create arrayNode of chart series
    List<ChartSerie> serieList = new ArrayList<>();

    // Add json data of series
    for (String user : userList) {
      ChartSerie serie = new ChartSerie();
      serie.setId(user);
      // Add serie
      serieList.add(serie);
    }

    // Create action replace series of chart
    serviceData.addClientAction(new ClientAction("remove-chart-series")
      .setTarget(SCREEN_TEST)
      .addParameter(SERIES, new CellData(serieList)));

    return serviceData;
  }

  /**
   * Build dummy month list
   *
   * @return Dummy month list
   */
  private List<String> builDummyMonthList() {
    // Dumy xAxis with months
    List<String> months = new ArrayList<>();
    months.add("Jan");
    months.add("Feb");
    months.add("Mar");
    months.add("Apr");
    months.add("May");
    months.add("Jun");
    months.add("Jul");
    months.add("Aug");
    months.add("Sep");
    months.add("Oct");
    months.add("Nov");
    months.add("Dec");
    return months;
  }

}
