package com.almis.awe.test;

import com.almis.awe.builder.client.chart.AddChartSeriesActionBuilder;
import com.almis.awe.builder.client.chart.RemoveChartSeriesActionBuilder;
import com.almis.awe.builder.client.chart.ReplaceChartSeriesActionBuilder;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;
import com.almis.awe.model.entities.screen.component.chart.ChartSeriePoint;
import com.almis.awe.model.util.security.EncodeUtil;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * File Chart class
 *
 * @author pvidal
 */
@Service
public class Chart extends ServiceConfig {

  private static final String SCREEN_TEST = "ChrLinTst";

  /**
   * Test method to replace the series of chart
   *
   * @param userList (Dummy list for build name of series)
   * @return Service output
   */
  public ServiceData replaceSeriesChart(List<String> userList) {
    // Create action replace series of chart
    return new ServiceData()
      .addClientAction(new ReplaceChartSeriesActionBuilder(SCREEN_TEST, getSerieList(userList)).build());
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
    // Create action replace series of chart
    return new ServiceData()
      .addClientAction(new AddChartSeriesActionBuilder(SCREEN_TEST, getSerieList(userList)).build());
  }

  /**
   * Test method to remove the series of chart
   *
   * @param userList (Dummy list for build name of series)
   * @return Service data
   */
  public ServiceData removeSeriesChart(List<String> userList) {
    // Create action replace series of chart
    return new ServiceData()
      .addClientAction(new RemoveChartSeriesActionBuilder(SCREEN_TEST, getSerieList(userList)).build());
  }

  /**
   * Build dummy month list
   *
   * @return Dummy month list
   */
  private List<String> builDummyMonthList() {
    // Dumy xAxis with months
    return Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
  }

}
