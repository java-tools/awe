package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.component.chart.Chart;
import com.almis.awe.model.entities.screen.component.chart.ChartParameter;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;
import com.almis.awe.model.entities.screen.component.chart.ChartSeriePoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class ChartService extends ServiceConfig {

  // Global files path
  @Value("${highcharts.server.url:http://export.highcharts.com}")
  private String exportServerUrl;

  /**
   * Render chart with highcharts export server
   *
   * @param screenName Screen name where chart is
   * @param chartName  Chart identifier
   * @param data       Data to fill the chart
   * @return SVG image as string
   * @throws AWException Error rendering chart
   */
  public String renderChart(String screenName, String chartName, DataList data) throws AWException {
    return renderChart(getChart(screenName, chartName), data);
  }

  /**
   * Render chart with highcharts export server
   *
   * @param screenName  Screen name where chart is
   * @param chartName   Chart identifier
   * @param datasources Data sources map
   * @return SVG image as string
   * @throws AWException Error rendering chart
   */
  public String renderChart(String screenName, String chartName, Map<String, DataList> datasources) throws AWException {
    return renderChart(getChart(screenName, chartName), datasources);
  }

  /**
   * Render chart with highcharts export server
   *
   * @param chart Chart
   * @param data  Data to fill the chart
   * @return SVG image as string
   * @throws AWException Error rendering chart
   */
  public String renderChart(Chart chart, DataList data) throws AWException {
    Map<String, DataList> datasources = new HashMap<>();
    datasources.put("main", data);
    return renderChart(chart, datasources);
  }

  /**
   * Render chart with highcharts export server
   *
   * @param chart       Chart
   * @param datasources Data sources map
   * @return SVG image as string
   * @throws AWException Error rendering chart
   */
  public String renderChart(Chart chart, Map<String, DataList> datasources) throws AWException {
    return renderChartWithDatasources(chart, datasources);
  }

  /**
   * Retrieve a chart from a screen and a name
   *
   * @param screenName Screen name
   * @param chartName  Chart name
   * @return Found chart or null
   */
  public Chart getChart(String screenName, String chartName) throws AWException {
    Screen screen = getElements().getScreen(screenName).copy();
    return (Chart) screen.getElementsById(chartName).stream().findFirst().orElse(null);
  }

  /**
   * Render chart with highcharts export server
   *
   * @param chart       Chart
   * @param datasources Data sources map
   * @return SVG image as string
   * @throws AWException Error rendering chart
   */
  private String renderChartWithDatasources(Chart chart, Map<String, DataList> datasources) throws AWException {
    if (chart != null) {
      // Add data to chart model
      generateData(chart, datasources);

      // Generate chart in server
      return generateChartInServer(chart);
    }
    return null;
  }

  /**
   * Call server and retrieve chart data
   *
   * @param chart Chart
   * @return Chart in server
   */
  private String generateChartInServer(Chart chart) throws AWException {
    RestTemplate restTemplate = getBean(RestTemplate.class);

    // Generate parameters
    Map<String, Object> chartData = new HashMap<>();
    chartData.put("infile", chart.getChartModel());
    chartData.put("type", "image/svg+xml");

    // Generate request
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    ResponseEntity<String> responseEntity = restTemplate.postForEntity(exportServerUrl, new HttpEntity<>(chartData, headers), String.class);
    log.info("Generating chart with data: {}", new ObjectMapper().valueToTree(chartData));

    // Handle response status
    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
      throw new AWException(String.format("Error retrieving chart from server %d", responseEntity.getStatusCodeValue()));
    }

    return responseEntity.getBody();
  }

  /**
   * Generate chart data
   *
   * @param chart       Chart
   * @param datasources Data sources map
   */
  private void generateData(Chart chart, Map<String, DataList> datasources) {
    ChartParameter defaultParameter = new ChartParameter().setName("datasource").setValue("main");
    chart.setSerieList(chart.getSerieList()
      .stream()
      .map(serie -> serie.setData(getSerieData(serie, datasources.get(Optional.ofNullable(serie.getParameterList()).orElse(Collections.emptyList()).stream().filter(parameter -> parameter.getName().equals("datasource")).findFirst().orElse(defaultParameter).getValue()))))
      .collect(Collectors.toList()));
  }

  /**
   * Retrieve serie data from datalist
   *
   * @param serie Serie
   * @param data  Datalist
   * @return Serie data
   */
  private List<ChartSeriePoint> getSerieData(ChartSerie serie, DataList data) {
    List<ChartSeriePoint> result = new ArrayList<>();
    data.getRows().forEach(row -> {
      if (serie.getZValue() != null) {
        result.add(new ChartSeriePoint(row.get(serie.getXValue()).getValue(), row.get(serie.getYValue()).getValue(), row.get(serie.getZValue()).getValue()));
      } else {
        result.add(new ChartSeriePoint(row.get(serie.getXValue()).getValue(), row.get(serie.getYValue()).getValue()));
      }
    });
    return result;
  }
}
