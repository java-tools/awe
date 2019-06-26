package com.almis.awe.test.unit.builder;

import com.almis.awe.builder.client.*;
import com.almis.awe.builder.client.chart.AddChartSeriesActionBuilder;
import com.almis.awe.builder.client.chart.AddPointsActionBuilder;
import com.almis.awe.builder.client.chart.RemoveChartSeriesActionBuilder;
import com.almis.awe.builder.client.chart.ReplaceChartSeriesActionBuilder;
import com.almis.awe.builder.client.css.AddCssClassActionBuilder;
import com.almis.awe.builder.client.css.RemoveCssClassActionBuilder;
import com.almis.awe.builder.client.grid.*;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.FileData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.screen.component.chart.ChartSerie;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.file.FileUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ClientActionBuilderTest {

  // Column list
  private List<Column> columnList = Arrays.asList(
    Column.builder().name("column1").build(),
    Column.builder().name("column2").build(),
    Column.builder().name("column3").build(),
    Column.builder().name("column4").build(),
    Column.builder().name("column5").build());

  // Column array
  private Column[] columnArray = new Column[]{
    Column.builder().name("column1").build(),
    Column.builder().name("column2").build(),
    Column.builder().name("column3").build(),
    Column.builder().name("column4").build(),
    Column.builder().name("column5").build()};

  // Chart serie list
  private List<ChartSerie> chartSeries = Arrays.asList(
    ChartSerie.builder().id("serie1").build(),
    ChartSerie.builder().id("serie2").build(),
    ChartSerie.builder().id("serie3").build(),
    ChartSerie.builder().id("serie4").build(),
    ChartSerie.builder().id("serie5").build());

  // Chart serie array
  private ChartSerie[] chartSeriesArray = new ChartSerie[]{
    ChartSerie.builder().id("serie1").build(),
    ChartSerie.builder().id("serie2").build(),
    ChartSerie.builder().id("serie3").build(),
    ChartSerie.builder().id("serie4").build(),
    ChartSerie.builder().id("serie5").build()};

  // Component address
  ComponentAddress address = new ComponentAddress("view", "component", "row", "column");

  /**
   * Build a screen action with ClientActionBuilder
   */
  @Test
  public void testScreenClientAction(){
    ClientAction action = new ScreenActionBuilder("testScreen").build();

    // Assertions
    assertEquals("screen", action.getType());
    assertEquals("testScreen", action.getTarget());
    checkSimpleClientActionBuilder("screen", new ScreenActionBuilder());
  }

  /**
   * Build a download action with ClientActionBuilder
   */
  @Test
  public void testDownloadAction() throws Exception {
    FileData fileData = new FileData();
    ClientAction action = new DownloadActionBuilder(fileData).build();

    // Assertions
    assertEquals("get-file", action.getType());
    assertEquals(FileUtil.fileDataToString(fileData), action.getParameters().get("filename"));
    checkSimpleClientActionBuilder("get-file", new DownloadActionBuilder());
  }

  /**
   * Build a fill action with ClientActionBuilder
   */
  @Test
  public void testFillAction() {
    DataList dataList = new DataList();
    ClientAction action = new FillActionBuilder("targetComponent", dataList).build();
    ClientAction addressedAction = new FillActionBuilder(address, dataList).build();

    // Assertions
    assertEquals("fill", action.getType());
    assertEquals("targetComponent", action.getTarget());
    assertEquals(dataList, action.getParameters().get("datalist"));

    assertEquals("fill", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(dataList, addressedAction.getParameters().get("datalist"));

    checkSimpleClientActionBuilder("fill", new FillActionBuilder());
  }

  /**
   * Build a filter action with ClientActionBuilder
   */
  @Test
  public void testFilterAction() {
    ClientAction action = new FilterActionBuilder("targetComponent").build();
    ClientAction addressedAction = new FilterActionBuilder(address).build();

    // Assertions
    assertEquals("filter", action.getType());
    assertEquals("targetComponent", action.getTarget());

    assertEquals("filter", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());

    checkSimpleClientActionBuilder("filter", new FilterActionBuilder());
  }

  /**
   * Build a update-controller action with ClientActionBuilder
   */
  @Test
  public void testUpdateControllerAction() {
    DataList dataList = new DataList();
    ClientAction action = new UpdateControllerActionBuilder("targetComponent", "attribute", dataList).build();
    ClientAction addressedAction = new UpdateControllerActionBuilder(address, "attribute", dataList).build();
    ClientAction actionValue = new UpdateControllerActionBuilder("targetComponent", "attribute", "value").build();
    ClientAction addressedActionValue = new UpdateControllerActionBuilder(address, "attribute", "value").build();

    // Assertions
    assertEquals("update-controller", action.getType());
    assertEquals("targetComponent", action.getTarget());
    assertEquals("attribute", action.getParameters().get("attribute"));
    assertEquals(dataList, action.getParameters().get("datalist"));

    assertEquals("update-controller", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals("attribute", addressedAction.getParameters().get("attribute"));
    assertEquals(dataList, addressedAction.getParameters().get("datalist"));

    assertEquals("update-controller", actionValue.getType());
    assertEquals("targetComponent", actionValue.getTarget());
    assertEquals("attribute", actionValue.getParameters().get("attribute"));
    assertEquals("value", actionValue.getParameters().get("value"));

    assertEquals("update-controller", addressedActionValue.getType());
    assertNull(addressedActionValue.getTarget());
    assertEquals(address, addressedActionValue.getAddress());
    assertEquals("attribute", addressedActionValue.getParameters().get("attribute"));
    assertEquals("value", addressedActionValue.getParameters().get("value"));

    checkSimpleClientActionBuilder("update-controller", new UpdateControllerActionBuilder());
  }

  /**
   * Build a select action with ClientActionBuilder
   */
  @Test
  public void testSelectAction() {
    List<String> values = Arrays.asList("aasd", "asxaf", "ggfd", "srgtf");
    Object[] valueList = new Object[]{"aasd", "asxaf", "ggfd", "srgtf"};
    ClientAction action = new SelectActionBuilder("targetComponent", values).build();
    ClientAction addressedAction = new SelectActionBuilder(address, values).build();
    ClientAction actionWithList = new SelectActionBuilder("targetComponent", valueList).build();
    ClientAction addressedActionWithList = new SelectActionBuilder(address, valueList).build();

    // Assertions
    assertEquals("select", action.getType());
    assertEquals("targetComponent", action.getTarget());
    assertEquals(values, action.getParameters().get("values"));

    assertEquals("select", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(values, addressedAction.getParameters().get("values"));

    // Assertions
    assertEquals("select", actionWithList.getType());
    assertEquals("targetComponent", actionWithList.getTarget());
    assertEquals(valueList, actionWithList.getParameters().get("values"));

    assertEquals("select", addressedActionWithList.getType());
    assertNull(addressedActionWithList.getTarget());
    assertEquals(address, addressedActionWithList.getAddress());
    assertEquals(valueList, addressedActionWithList.getParameters().get("values"));

    checkSimpleClientActionBuilder("select", new SelectActionBuilder());
  }

  /**
   * Build a message action with ClientActionBuilder
   */
  @Test
  public void testMessageAction() {
    ClientAction action = new MessageActionBuilder(AnswerType.WARNING, "Message title", "Message description").build();

    // Assertions
    assertEquals("message", action.getType());
    assertEquals("warning", action.getParameters().get("type"));
    assertEquals("Message title", action.getParameters().get("title"));
    assertEquals("Message description", action.getParameters().get("message"));
    checkSimpleClientActionBuilder("message", new MessageActionBuilder());
  }

  /**
   * Build an add-columns action with ClientActionBuilder
   */
  @Test
  public void testAddColumnsAction() {
    ClientAction action = new AddColumnsActionBuilder("testGrid", columnList).build();
    ClientAction addressedAction = new AddColumnsActionBuilder(address, columnList).build();
    ClientAction actionArray = new AddColumnsActionBuilder("testGrid", columnArray).build();
    ClientAction addressedActionArray = new AddColumnsActionBuilder(address, columnArray).build();

    // Assertions
    assertEquals("add-columns", action.getType());
    assertEquals("testGrid", action.getTarget());
    assertEquals(columnList, action.getParameters().get("columns"));

    assertEquals("add-columns", actionArray.getType());
    assertEquals("testGrid", actionArray.getTarget());
    assertEquals(columnArray, actionArray.getParameters().get("columns"));

    assertEquals("add-columns", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(columnList, addressedAction.getParameters().get("columns"));

    assertEquals("add-columns", addressedActionArray.getType());
    assertEquals(address, addressedActionArray.getAddress());
    assertEquals(columnArray, addressedActionArray.getParameters().get("columns"));

    checkSimpleClientActionBuilder("add-columns", new AddColumnsActionBuilder());
  }

  /**
   * Build a replace-columns action with ClientActionBuilder
   */
  @Test
  public void testReplaceColumnsAction() {
    ClientAction action = new ReplaceColumnsActionBuilder("testGrid", columnList).build();
    ClientAction addressedAction = new ReplaceColumnsActionBuilder(address, columnList).build();
    ClientAction actionArray = new ReplaceColumnsActionBuilder("testGrid", columnArray).build();
    ClientAction addressedActionArray = new ReplaceColumnsActionBuilder(address, columnArray).build();

    // Assertions
    assertEquals("replace-columns", action.getType());
    assertEquals("testGrid", action.getTarget());
    assertEquals(columnList, action.getParameters().get("columns"));

    assertEquals("replace-columns", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(columnList, addressedAction.getParameters().get("columns"));

    assertEquals("replace-columns", actionArray.getType());
    assertEquals("testGrid", actionArray.getTarget());
    assertEquals(columnArray, actionArray.getParameters().get("columns"));

    assertEquals("replace-columns", addressedActionArray.getType());
    assertNull(addressedActionArray.getTarget());
    assertEquals(address, addressedActionArray.getAddress());
    assertEquals(columnArray, addressedActionArray.getParameters().get("columns"));

    checkSimpleClientActionBuilder("replace-columns", new ReplaceColumnsActionBuilder());
  }

  /**
   * Build an show-columns action with ClientActionBuilder
   */
  @Test
  public void testShowColumnsAction() {
    List<String> columnNameList = Arrays.asList("column1", "column2");
    ClientAction action = new ShowColumnsActionBuilder("testGrid", columnNameList).build();
    ClientAction addressedAction = new ShowColumnsActionBuilder(address, columnNameList).build();
    ClientAction actionArray = new ShowColumnsActionBuilder("testGrid", "column1", "column2").build();
    ClientAction addressedActionArray = new ShowColumnsActionBuilder(address, "column1", "column2").build();

    // Assertions
    assertEquals("show-columns", action.getType());
    assertEquals("testGrid", action.getTarget());
    assertEquals(columnNameList, action.getParameters().get("columns"));

    assertEquals("show-columns", actionArray.getType());
    assertEquals("testGrid", actionArray.getTarget());
    assertArrayEquals(new String[]{"column1", "column2"}, (String[]) actionArray.getParameters().get("columns"));

    assertEquals("show-columns", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(columnNameList, addressedAction.getParameters().get("columns"));

    assertEquals("show-columns", addressedActionArray.getType());
    assertEquals(address, addressedActionArray.getAddress());
    assertArrayEquals(new String[]{"column1", "column2"}, (String[]) addressedActionArray.getParameters().get("columns"));

    checkSimpleClientActionBuilder("show-columns", new ShowColumnsActionBuilder());
  }

  /**
   * Build an hide-columns action with ClientActionBuilder
   */
  @Test
  public void testHideColumnsAction() {
    List<String> columnNameList = Arrays.asList("column1", "column2");
    ClientAction action = new HideColumnsActionBuilder("testGrid", columnNameList).build();
    ClientAction addressedAction = new HideColumnsActionBuilder(address, columnNameList).build();
    ClientAction actionArray = new HideColumnsActionBuilder("testGrid", "column1", "column2").build();
    ClientAction addressedActionArray = new HideColumnsActionBuilder(address, "column1", "column2").build();

    // Assertions
    assertEquals("hide-columns", action.getType());
    assertEquals("testGrid", action.getTarget());
    assertEquals(columnNameList, action.getParameters().get("columns"));

    assertEquals("hide-columns", actionArray.getType());
    assertEquals("testGrid", actionArray.getTarget());
    assertArrayEquals(new String[]{"column1", "column2"}, (String[]) actionArray.getParameters().get("columns"));

    assertEquals("hide-columns", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(columnNameList, addressedAction.getParameters().get("columns"));

    assertEquals("hide-columns", addressedActionArray.getType());
    assertEquals(address, addressedActionArray.getAddress());
    assertArrayEquals(new String[]{"column1", "column2"}, (String[]) addressedActionArray.getParameters().get("columns"));

    checkSimpleClientActionBuilder("hide-columns", new HideColumnsActionBuilder());
  }

  /**
   * Build a update-cell action with ClientActionBuilder
   */
  @Test
  public void testUpdateCellAction() {
    CellData cellData = new CellData("Test");
    ClientAction action = new UpdateCellActionBuilder(address, cellData).build();

    // Assertions
    assertEquals("update-cell", action.getType());
    assertEquals(address, action.getAddress());
    assertNull(action.getTarget());
    assertTrue(action.getSilent());
    assertTrue(action.getAsync());
    assertEquals(cellData, action.getParameters().get("data"));
    checkSimpleClientActionBuilder("update-cell", new UpdateCellActionBuilder());
  }

  /**
   * Build a add-chart-series action with ClientActionBuilder
   */
  @Test
  public void testAddChartSeriesAction() {
    ClientAction action = new AddChartSeriesActionBuilder("targetChart", chartSeries).build();
    ClientAction addressedAction = new AddChartSeriesActionBuilder(address, chartSeries).build();
    ClientAction actionArray = new AddChartSeriesActionBuilder("targetChart", chartSeriesArray).build();
    ClientAction addressedActionArray = new AddChartSeriesActionBuilder(address, chartSeriesArray).build();

    // Assertions
    assertEquals("add-chart-series", action.getType());
    assertEquals("targetChart", action.getTarget());
    assertEquals(chartSeries, action.getParameters().get("series"));

    assertEquals("add-chart-series", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(chartSeries, addressedAction.getParameters().get("series"));

    assertEquals("add-chart-series", actionArray.getType());
    assertEquals("targetChart", actionArray.getTarget());
    assertEquals(chartSeriesArray, actionArray.getParameters().get("series"));

    assertEquals("add-chart-series", addressedActionArray.getType());
    assertNull(addressedActionArray.getTarget());
    assertEquals(address, addressedActionArray.getAddress());
    assertEquals(chartSeriesArray, addressedActionArray.getParameters().get("series"));

    checkSimpleClientActionBuilder("add-chart-series", new AddChartSeriesActionBuilder());
  }

  /**
   * Build a replace-chart-series action with ClientActionBuilder
   */
  @Test
  public void testReplaceChartSeriesAction() {
    ClientAction action = new ReplaceChartSeriesActionBuilder("targetChart", chartSeries).build();
    ClientAction addressedAction = new ReplaceChartSeriesActionBuilder(address, chartSeries).build();
    ClientAction actionArray = new ReplaceChartSeriesActionBuilder("targetChart", chartSeriesArray).build();
    ClientAction addressedActionArray = new ReplaceChartSeriesActionBuilder(address, chartSeriesArray).build();

    // Assertions
    assertEquals("replace-chart-series", action.getType());
    assertEquals("targetChart", action.getTarget());
    assertEquals(chartSeries, action.getParameters().get("series"));

    assertEquals("replace-chart-series", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(chartSeries, addressedAction.getParameters().get("series"));

    assertEquals("replace-chart-series", actionArray.getType());
    assertEquals("targetChart", actionArray.getTarget());
    assertEquals(chartSeriesArray, actionArray.getParameters().get("series"));

    assertEquals("replace-chart-series", addressedActionArray.getType());
    assertNull(addressedActionArray.getTarget());
    assertEquals(address, addressedActionArray.getAddress());
    assertEquals(chartSeriesArray, addressedActionArray.getParameters().get("series"));

    checkSimpleClientActionBuilder("replace-chart-series", new ReplaceChartSeriesActionBuilder());
  }

  /**
   * Build a replace-chart-series action with ClientActionBuilder
   */
  @Test
  public void testRemoveChartSeriesAction() {
    ClientAction action = new RemoveChartSeriesActionBuilder("targetChart", chartSeries).build();
    ClientAction addressedAction = new RemoveChartSeriesActionBuilder(address, chartSeries).build();
    ClientAction actionArray = new RemoveChartSeriesActionBuilder("targetChart", chartSeriesArray).build();
    ClientAction addressedActionArray = new RemoveChartSeriesActionBuilder(address, chartSeriesArray).build();
    // Assertions
    assertEquals("remove-chart-series", action.getType());
    assertEquals("targetChart", action.getTarget());
    assertEquals(chartSeries, action.getParameters().get("series"));

    assertEquals("remove-chart-series", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(chartSeries, addressedAction.getParameters().get("series"));

    assertEquals("remove-chart-series", actionArray.getType());
    assertEquals("targetChart", actionArray.getTarget());
    assertEquals(chartSeriesArray, actionArray.getParameters().get("series"));

    assertEquals("remove-chart-series", addressedActionArray.getType());
    assertNull(addressedActionArray.getTarget());
    assertEquals(address, addressedActionArray.getAddress());
    assertEquals(chartSeriesArray, addressedActionArray.getParameters().get("series"));

    checkSimpleClientActionBuilder("remove-chart-series", new RemoveChartSeriesActionBuilder());
  }


  /**
   * Build an add-points action with ClientActionBuilder
   */
  @Test
  public void testAddPointsAction() {
    DataList dataList = new DataList();
    ClientAction action = new AddPointsActionBuilder("targetChart", dataList).build();
    ClientAction addressedAction = new AddPointsActionBuilder(address, dataList).build();

    // Assertions
    assertEquals("add-points", action.getType());
    assertEquals("targetChart", action.getTarget());
    assertEquals(dataList, action.getParameters().get("data"));

    assertEquals("add-points", addressedAction.getType());
    assertNull(addressedAction.getTarget());
    assertEquals(address, addressedAction.getAddress());
    assertEquals(dataList, addressedAction.getParameters().get("data"));
    checkSimpleClientActionBuilder("add-points", new AddPointsActionBuilder());
  }

  /**
   * Build an add-class action with ClientActionBuilder
   */
  @Test
  public void testAddCssClassAction() {
    ClientAction action = new AddCssClassActionBuilder("targetSelector", "class1", "class2").build();

    // Assertions
    assertEquals("add-class", action.getType());
    assertEquals("targetSelector", action.getTarget());
    assertEquals("class1 class2", action.getParameters().get("targetAction"));
    assertTrue(action.getAsync());
    assertTrue(action.getSilent());

    checkSimpleClientActionBuilder("add-class", new AddCssClassActionBuilder());
  }

  /**
   * Build a remove-class action with ClientActionBuilder
   */
  @Test
  public void testRemoveCssClassAction() {
    ClientAction action = new RemoveCssClassActionBuilder("targetSelector", "class1", "class2").build();

    // Assertions
    assertEquals("remove-class", action.getType());
    assertEquals("targetSelector", action.getTarget());
    assertEquals("class1 class2", action.getParameters().get("targetAction"));
    assertTrue(action.getAsync());
    assertTrue(action.getSilent());

    checkSimpleClientActionBuilder("remove-class", new RemoveCssClassActionBuilder());
  }

  /**
   * Build a confirm action with ClientActionBuilder
   */
  @Test
  public void testConfirmAction() {
    ClientAction action = new ConfirmActionBuilder("targetMessage").build();
    ClientAction actionWithMessage = new ConfirmActionBuilder("title confirm", "description confirm").build();

    // Assertions
    assertEquals("confirm", action.getType());
    assertEquals("targetMessage", action.getTarget());

    assertEquals("confirm", actionWithMessage.getType());
    assertNull(actionWithMessage.getTarget());
    assertEquals("title confirm", actionWithMessage.getParameters().get("title"));
    assertEquals("description confirm", actionWithMessage.getParameters().get("message"));

    checkSimpleClientActionBuilder("confirm", new ConfirmActionBuilder());
  }

  /**
   * Build a dialog action with ClientActionBuilder
   */
  @Test
  public void testDialogAction() {
    ClientAction action = new DialogActionBuilder("targetMessage").build();

    // Assertions
    assertEquals("dialog", action.getType());
    assertEquals("targetMessage", action.getTarget());

    checkSimpleClientActionBuilder("dialog", new DialogActionBuilder());
  }

  /**
   * Check a simple client action builder
   * @param type Action type
   * @param actionBuilder Action builder to test
   */
  private void checkSimpleClientActionBuilder(String type, ClientActionBuilder actionBuilder) {
    assertEquals(type, actionBuilder.build().getType());
  }
}