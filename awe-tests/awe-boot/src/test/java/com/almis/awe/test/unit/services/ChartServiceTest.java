package com.almis.awe.test.unit.services;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.Tag;
import com.almis.awe.model.entities.screen.component.chart.*;
import com.almis.awe.service.ChartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChartServiceTest {

  @InjectMocks
  private ChartService chartService;

  @Mock
  private ApplicationContext context;

  @Mock
  private AweElements aweElements;

  @Mock
  private RestTemplate restTemplate;

  private final Screen testScreen = new Screen()
    .addElement(new Tag()
      .addElement(new Chart()
        .setXAxisList(Collections.singletonList((ChartAxis) new ChartAxis().setTitle("Fechas").setType("datetime")))
        .setYAxisList(Arrays.asList((ChartAxis) new ChartAxis().setLabel("Temperaturas (ºC)"), (ChartAxis) new ChartAxis().setOpposite(true).setLabel("Lluvias (mm)")))
        .setSerieList(Arrays.asList((ChartSerie) new ChartSerie()
            .setYAxis("0")
            .setXValue("dates")
            .setYValue("serie1")
            .setColor("#A8E0A6")
            .setType("column")
            .setId("serie-1"),
          (ChartSerie) new ChartSerie()
            .setYAxis("1")
            .setXValue("dates")
            .setYValue("serie2")
            .setZValue("serie3")
            .setType("spline")
            .setId("serie-2")
        ))
        .setId("LineChartTest")
        .setType("mixed")
      )
    )
    .addElement(new Chart()
      .setChartTooltip(new ChartTooltip().setSuffix("%"))
      .setSerieList(Arrays.asList(
        (ChartSerie) new ChartSerie()
          .setXValue("category")
          .setYValue("serie1")
          .setParameterList(Arrays.asList(
            (ChartParameter) new ChartParameter().setName("size").setValue("51%").setType("string"),
            (ChartParameter) ((ChartParameter) new ChartParameter().setName("dataLabels").setType("object"))
              .setParameterList(Arrays.asList(
                (ChartParameter) new ChartParameter().setName("distance").setValue("-30").setType("integer"),
                (ChartParameter) new ChartParameter().setName("color").setValue("#ffffff").setType("string"),
                (ChartParameter) new ChartParameter().setName("format").setValue("<b>{point.name}</b>").setType("string")
              ))
          ))
          .setId("main"),
        (ChartSerie) new ChartSerie()
          .setXValue("category")
          .setYValue("subserie1")
          .setParameterList(Arrays.asList(
            (ChartParameter) new ChartParameter().setName("datasource").setValue("detail").setType("string"),
            (ChartParameter) new ChartParameter().setName("size").setValue("80%").setType("string"),
            (ChartParameter) new ChartParameter().setName("innerSize").setValue("60%").setType("string")
          ))
          .setId("detail")
      ))
      .setParameterList(Collections.singletonList(
        (ChartParameter) new ChartParameter().setName("plotOptions")
          .setParameterList(Collections.singletonList(
            (ChartParameter) new ChartParameter().setName("pie")
              .setParameterList(Arrays.asList(
                (ChartParameter) new ChartParameter().setName("size").setValue("75%").setType("string"),
                (ChartParameter) new ChartParameter().setName("shadow").setValue("false").setType("boolean"),
                (ChartParameter) new ChartParameter().setName("center")
                  .setParameterList(Arrays.asList(
                    (ChartParameter) new ChartParameter().setValue("50%").setType("string"),
                    (ChartParameter) new ChartParameter().setValue("50%").setType("string")
                  ))
                  .setType("array")
              ))
              .setType("object")
          ))
          .setType("object")

      ))
      .setId("DrillDownTest")
      .setType("pie")
    );

  /**
   * Initializes json mapper for tests
   */
  @Before
  public void initBeans() throws Exception {
    chartService.setApplicationContext(context);
    doReturn(aweElements).when(context).getBean(AweElements.class);
    doReturn(restTemplate).when(context).getBean(RestTemplate.class);
    given(aweElements.getScreen(anyString())).willReturn(testScreen);
    PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(chartService);
    myAccessor.setPropertyValue("exportServerUrl", "http://export.highcharts.com");
  }

  @Test
  public void renderChart() throws Exception {
    when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(ResponseEntity.ok("Hello World"));
    DataList data = new DataList();
    Map<String, CellData> row = new HashMap<>();
    row.put("dates", new CellData(1366927200000L));
    row.put("id", new CellData(1));
    row.put("serie1", new CellData(10));
    row.put("serie2", new CellData(6));
    row.put("serie3", new CellData(0));
    data.addRow(row);
    row = new HashMap<>();
    row.put("dates", new CellData(1367013600000L));
    row.put("id", new CellData(2));
    row.put("serie1", new CellData(5));
    row.put("serie2", new CellData(7));
    row.put("serie3", new CellData(-2));
    data.addRow(row);
    row = new HashMap<>();
    row.put("dates", new CellData(1367186400000L));
    row.put("id", new CellData(3));
    row.put("serie1", new CellData(10));
    row.put("serie2", new CellData(6));
    row.put("serie3", new CellData(10));
    data.addRow(row);

    assertEquals("Hello World", chartService.renderChart("Chart", "LineChartTest", data));
  }

  @Test
  public void renderChartWithDetails() throws AWException {
    when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(ResponseEntity.ok("Hello World"));
    DataList data = new DataList();
    HashMap<String, CellData> row = new HashMap<>();
    row.put("id", new CellData(1));
    row.put("category", new CellData("asphalt"));
    row.put("serie1", new CellData(3));
    data.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(2));
    row.put("category", new CellData("clean"));
    row.put("serie1", new CellData(5));
    data.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(3));
    row.put("category", new CellData("default"));
    row.put("serie1", new CellData(2));
    data.addRow(row);

    DataList detail = new DataList();
    row = new HashMap<>();
    row.put("id", new CellData(1));
    row.put("parent", new CellData("asphalt"));
    row.put("category", new CellData("asphalt1"));
    row.put("subserie1", new CellData(1f));
    detail.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(2));
    row.put("parent", new CellData("asphalt"));
    row.put("category", new CellData("asphalt2"));
    row.put("subserie1", new CellData(0.5f));
    detail.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(3));
    row.put("parent", new CellData("asphalt"));
    row.put("category", new CellData("asphalt3"));
    row.put("subserie1", new CellData(1.5f));
    detail.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(4));
    row.put("parent", new CellData("clean"));
    row.put("category", new CellData("Don Limpio"));
    row.put("subserie1", new CellData(4f));
    detail.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(5));
    row.put("parent", new CellData("clean"));
    row.put("category", new CellData("Mr Proper"));
    row.put("subserie1", new CellData(1f));
    detail.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(6));
    row.put("parent", new CellData("default"));
    row.put("category", new CellData("Pepe"));
    row.put("subserie1", new CellData(0.2f));
    detail.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(7));
    row.put("parent", new CellData("default"));
    row.put("category", new CellData("Blue"));
    row.put("subserie1", new CellData(0.9f));
    detail.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(8));
    row.put("parent", new CellData("default"));
    row.put("category", new CellData("Vegeta666"));
    row.put("subserie1", new CellData(0.4f));
    detail.addRow(row);
    row = new HashMap<>();
    row.put("id", new CellData(9));
    row.put("parent", new CellData("default"));
    row.put("category", new CellData("El Rubius"));
    row.put("subserie1", new CellData(0.5f));
    detail.addRow(row);

    Map<String, DataList> datasources = new HashMap<>();
    datasources.put("main", data);
    datasources.put("detail", detail);

    assertEquals("Hello World", chartService.renderChart("Chart", "DrillDownTest", datasources));
  }

  @Test
  public void renderChartNotFound() throws Exception {
    assertNull(chartService.renderChart("Chart", "NotFoundChart", new DataList()));
  }

  @Test(expected = AWException.class)
  public void renderChartNot200() throws Exception {
    when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(ResponseEntity.notFound().build());
    chartService.renderChart("Chart", "LineChartTest", new DataList());
  }
}