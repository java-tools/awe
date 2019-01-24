package com.almis.awe.test.builder;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.enumerates.ChartAxis;
import com.almis.awe.builder.screen.*;
import com.almis.awe.builder.screen.accordion.AccordionBuilder;
import com.almis.awe.builder.screen.accordion.AccordionItemBuilder;
import com.almis.awe.builder.screen.button.ButtonActionBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.chart.*;
import com.almis.awe.builder.screen.context.ContextButtonBuilder;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.dependency.DependencyActionBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.builder.screen.dependency.DependencyElementBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.screen.Include;
import com.almis.awe.model.entities.screen.Message;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.View;
import com.almis.awe.model.entities.screen.component.*;
import com.almis.awe.model.entities.screen.component.action.ButtonAction;
import com.almis.awe.model.entities.screen.component.action.Dependency;
import com.almis.awe.model.entities.screen.component.action.DependencyAction;
import com.almis.awe.model.entities.screen.component.action.DependencyElement;
import com.almis.awe.model.entities.screen.component.button.Button;
import com.almis.awe.model.entities.screen.component.button.ContextButton;
import com.almis.awe.model.entities.screen.component.button.ContextSeparator;
import com.almis.awe.model.entities.screen.component.chart.*;
import com.almis.awe.model.entities.screen.component.container.AccordionItem;
import com.almis.awe.model.entities.screen.component.panelable.Accordion;
import com.almis.awe.model.entities.screen.component.pivottable.PivotTable;
import com.almis.awe.model.type.ChartType;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ScreenBuilderTest {

  AweElements aweElements;

  @Before
  public void beforeEachTest() throws Exception {
    aweElements = Mockito.mock(AweElements.class);
    when(aweElements.getMenu(anyString())).thenReturn(new Menu());
  }

  /**
   * Build a single screen without elements
   *
   * @throws Exception
   */
  @Test
  public void buildClientAction() throws Exception {
    new ScreenBuilder()
      .setMenuType("public")
      .buildClientAction(aweElements);
  }

  /**
   * Build a single screen without elements
   *
   * @throws Exception
   */
  @Test
  public void build() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .setHelp("HELP")
      .setHelpImage("HELP_IMAGE")
      .setKeepCriteria(true)
      .setLabel("LABEL")
      .setTarget("initial_target")
      .setOnLoad("OnLoad")
      .setOnUnload("OnUnLoad")
      .setTemplate("full");
    Screen screen = builder.build();

    assertEquals(screen.getHelp(), "HELP");
    assertEquals(screen.getHelpImage(), "HELP_IMAGE");
    assertEquals(screen.getKeepCriteria(), "true");
    assertEquals(screen.getLabel(), "LABEL");
    assertEquals(screen.getTarget(), "initial_target");
    assertEquals(screen.getTemplate(), "full");
    assertEquals(screen.getOnLoad(), "OnLoad");
    assertEquals(screen.getOnUnload(), "OnUnLoad");
  }

  /**
   * Build a single screen with an invalid id
   *
   * @throws Exception
   */
  @Test
  public void buildScreenInvalidId() throws Exception {
    try {
      ScreenBuilder builder = new ScreenBuilder()
        .setId("aR!$fg");
    } catch (AWException exc) {
      assertTrue(true);
      return;
    }
    assertTrue(false);
  }

  /**
   * Build a single screen with a tag
   *
   * @throws Exception
   */
  @Test
  public void addTag() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setExpandible(Expandible.VERTICAL)
        .setType("div"));
    Screen screen = builder.build();
    assertEquals(screen.getElementList().get(0).getLabel(), "LABEL");
    assertEquals(screen.getElementList().get(0).getSource(), "center");
    assertEquals(screen.getElementList().get(0).getStyle(), "expand");
    assertTrue(Expandible.VERTICAL.equalsStr(screen.getElementList().get(0).getExpand()));
    assertEquals(screen.getElementList().get(0).getType(), "div");
  }

  /**
   * Build a single screen with a tag
   *
   * @throws Exception
   */
  @Test
  public void addMessage() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addMessage(new MessageBuilder()
        .setTitle("MESSAGE_TITLE")
        .setMessage("MESSAGE"));
    Screen screen = builder.build();
    assertEquals(((Message) screen.getElementList().get(0)).getTitle(), "MESSAGE_TITLE");
    assertEquals(((Message) screen.getElementList().get(0)).getMessage(), "MESSAGE");
  }

  /**
   * Build a single screen with a dialog
   *
   * @throws Exception
   */
  @Test
  public void addDialog() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addDialog(new DialogBuilder()
          .setLabel("DIALOG_LABEL")
          .setOnClose(OnClose.ACCEPT)));

    Screen screen = builder.build();
    assertEquals(((Dialog) screen.getElementList().get(0).getElementList().get(0)).getLabel(), "DIALOG_LABEL");
    assertTrue(OnClose.ACCEPT.equalsStr(((Dialog) screen.getElementList().get(0).getElementList().get(0)).getOnClose()));
  }

  /**
   * Build a single screen with an include
   *
   * @throws Exception
   */
  @Test
  public void addInclude() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addInclude(new IncludeBuilder()
          .setTargetScreen("TargetScreen")
          .setTargetSource("center")));

    Screen screen = builder.build();
    assertEquals(((Include) screen.getElementList().get(0).getElementList().get(0)).getTargetScreen(), "TargetScreen");
    assertEquals(((Include) screen.getElementList().get(0).getElementList().get(0)).getTargetSource(), "center");
  }

  /**
   * Build a single screen with a view
   *
   * @throws Exception
   */
  @Test
  public void addView() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addView(new ViewBuilder()
          .setName("ViewName")));

    Screen screen = builder.build();
    assertEquals(((View) screen.getElementList().get(0).getElementList().get(0)).getName(), "ViewName");
  }

  /**
   * Build a single screen with a tag list
   *
   * @throws Exception
   */
  @Test
  public void addTagList() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addTagList(new TagListBuilder()
          .setAutoload(true)
          .setAutorefresh(true)
          .setInitialLoad(InitialLoad.QUERY)
          .setMax(50)
          .setServerAction(ServerAction.DATA)
          .setTargetAction("TagListTargetAction")
          .setType("div")));

    Screen screen = builder.build();
    assertEquals(((TagList) screen.getElementList().get(0).getElementList().get(0)).getAutoload(), "true");
    assertEquals(((TagList) screen.getElementList().get(0).getElementList().get(0)).getAutorefresh(), "true");
    assertTrue(InitialLoad.QUERY.equalsStr(((TagList) screen.getElementList().get(0).getElementList().get(0)).getInitialLoad()));
    assertEquals(((TagList) screen.getElementList().get(0).getElementList().get(0)).getMax(), "50");
    assertTrue(ServerAction.DATA.equalsStr(((TagList) screen.getElementList().get(0).getElementList().get(0)).getServerAction()));
    assertEquals(((TagList) screen.getElementList().get(0).getElementList().get(0)).getTargetAction(), "TagListTargetAction");
    assertEquals(screen.getElementList().get(0).getElementList().get(0).getType(), "div");
  }

  /**
   * Build a single screen with a resizable
   *
   * @throws Exception
   */
  @Test
  public void addResizable() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addResizable(new ResizableBuilder()
          .setDirections("left")
          .setIcon("icon")
          .setLabel("LABEL")));

    Screen screen = builder.build();
    assertEquals(((Resizable) screen.getElementList().get(0).getElementList().get(0)).getDirections(), "left");
    assertEquals(((Resizable) screen.getElementList().get(0).getElementList().get(0)).getIcon(), "icon");
    assertEquals(((Resizable) screen.getElementList().get(0).getElementList().get(0)).getLabel(), "LABEL");
  }

  /**
   * Build a single screen with a pivot table
   *
   * @throws Exception
   */
  @Test
  public void addPivotTable() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addPivotTable(new PivotTableBuilder()
          .setAggregationField("agg")
          .setAggregator(Aggregator.AVERAGE)
          .setAutoload(true)
          .setCols(3)
          .setRows(4)
          .setDecimalNumbers(4)
          .setDecimalSeparator(",")
          .setThousandSeparator(".")
          .setRenderer(Renderer.COL_HEATMAP)
          .setTotalColumnPlacement(TotalColumnPlacement.LEFT)
          .setTotalRowPlacement(TotalRowPlacement.BOTTOM)
          .setSortMethod(SortMethod.ABSOLUTE)
          .setInitialLoad(InitialLoad.QUERY)
          .setMax(50)
          .setServerAction(ServerAction.DATA)
          .setTargetAction("targetAction")));

    Screen screen = builder.build();
    assertEquals(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getAggregationField(), "agg");
    assertTrue(Aggregator.AVERAGE.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getAggregator()));
    assertEquals(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getAutoload(), "true");
    assertEquals(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getCols(), "3");
    assertEquals(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getRows(), "4");
    assertSame(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getDecimalNumbers(), 4);
    assertEquals(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getDecimalSeparator(), ",");
    assertEquals(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getThousandSeparator(), ".");
    assertTrue(Renderer.COL_HEATMAP.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getRenderer()));
    assertTrue(TotalColumnPlacement.LEFT.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTotalColumnPlacement()));
    assertTrue(TotalRowPlacement.BOTTOM.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTotalRowPlacement()));
    assertTrue(SortMethod.ABSOLUTE.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getSortMethod()));
    assertTrue(InitialLoad.QUERY.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getInitialLoad()));
    assertEquals(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getMax(), "50");
    assertTrue(ServerAction.DATA.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getServerAction()));
    assertEquals(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTargetAction(), "targetAction");
  }

  /**
   * Build a single screen with a window
   *
   * @throws Exception
   */
  @Test
  public void addWindow() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addWindow(new WindowBuilder()
          .setIcon("icon")
          .setLabel("LABEL")
          .setStyle("style")
          .setMaximize(true)));

    Screen screen = builder.build();
    assertEquals(((Window) screen.getElementList().get(0).getElementList().get(0)).getIcon(), "icon");
    assertEquals(((Window) screen.getElementList().get(0).getElementList().get(0)).getLabel(), "LABEL");
    assertEquals(((Window) screen.getElementList().get(0).getElementList().get(0)).getStyle(), "style");
    assertEquals(((Window) screen.getElementList().get(0).getElementList().get(0)).getMaximize(), "true");
  }

  /**
   * Build a single screen with a menu
   *
   * @throws Exception
   */
  @Test
  public void addMenu() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addMenuContainer(new MenuContainerBuilder()
          .setType(MenuType.HORIZONTAL)
          .setStyle("style")));

    Screen screen = builder.build();
    assertTrue(MenuType.HORIZONTAL.equalsStr(screen.getElementList().get(0).getElementList().get(0).getType()));
    assertEquals(screen.getElementList().get(0).getElementList().get(0).getStyle(), "style");
  }

  /**
   * Build a single screen with a frame
   *
   * @throws Exception
   */
  @Test
  public void addFrame() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addFrame(new FrameBuilder()
          .setScreen("screen")
          .setScreenVariable("var")
          .setScroll(true)
          .setServerAction(ServerAction.APPLICATION_HELP)));

    Screen screen = builder.build();
    assertEquals(((Frame) screen.getElementList().get(0).getElementList().get(0)).getScreen(), "screen");
    assertEquals(((Frame) screen.getElementList().get(0).getElementList().get(0)).getScreenVariable(), "var");
    assertEquals(((Frame) screen.getElementList().get(0).getElementList().get(0)).getScroll(), "true");
    assertTrue(ServerAction.APPLICATION_HELP.equalsStr(((Frame) screen.getElementList().get(0).getElementList().get(0)).getServerAction()));
  }

  /**
   * Build a single screen with an accordion
   *
   * @throws Exception
   */
  @Test
  public void addAccordion() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addAccordion(new AccordionBuilder()
          .setAutocollapse(true)
          .setSelected("selected")
          .addAccordionItem(new AccordionItemBuilder()
            .setLabel("LABEL")
            .setId("notSelected"))
          .addAccordionItem(new AccordionItemBuilder()
            .setId("selected")
            .setLabel("OTHER_LABEL"))));

    Screen screen = builder.build();
    assertEquals(((Accordion) screen.getElementList().get(0).getElementList().get(0)).getAutocollapse(), "true");
    assertEquals(((Accordion) screen.getElementList().get(0).getElementList().get(0)).getSelected(), "selected");
    assertEquals(((AccordionItem) screen.getElementList().get(0).getElementList().get(0).getElementList().get(0)).getId(), "notSelected");
    assertEquals(((AccordionItem) screen.getElementList().get(0).getElementList().get(0).getElementList().get(0)).getLabel(), "LABEL");
    assertEquals(((AccordionItem) screen.getElementList().get(0).getElementList().get(0).getElementList().get(1)).getId(), "selected");
    assertEquals(((AccordionItem) screen.getElementList().get(0).getElementList().get(0).getElementList().get(1)).getLabel(), "OTHER_LABEL");
  }

  /**
   * Build a single screen with a button
   *
   * @throws Exception
   */
  @Test
  public void addButton() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("buttons")
        .setStyle("expand")
        .setType("div")
        .addButton(new ButtonBuilder()
          .setIcon("button_icon")
          .setSize("sm")
          .setId("button1")
          .addButtonAction(new ButtonActionBuilder()
            .setServerAction(ServerAction.MAINTAIN)
            .setTargetAction("targetAction1")
            .setAsynchronous(true)
            .setTarget("target1")
            .setSilent(true)
            .setValue("buttonValue1")
            .setContext("home")
            .setType(Action.ADD_CLASS))
          .addButtonAction(new ButtonActionBuilder()
            .setServerAction(ServerAction.MAINTAIN_ASYNC)
            .setTargetAction("targetAction2")
            .setAsynchronous(false)
            .setSilent(false)
            .setTarget("target2")
            .setValue("buttonValue2")
            .setContext("home")
            .setType(Action.SERVER))));

    Screen screen = builder.build();
    Button button = (Button) screen.getElementList().get(0).getElementList().get(0);
    assertEquals(button.getIcon(), "button_icon");
    assertEquals(button.getSize(), "sm");
    assertEquals(button.getId(), "button1");

    ButtonAction buttonAction = (ButtonAction) button.getElementList().get(0);
    assertTrue(ServerAction.MAINTAIN.equalsStr(buttonAction.getServerAction()));
    assertTrue(Action.ADD_CLASS.equalsStr(buttonAction.getType()));
    assertEquals(buttonAction.getTargetAction(), "targetAction1");
    assertEquals(buttonAction.getTarget(), "target1");
    assertEquals(buttonAction.getAsync(), "true");
    assertEquals(buttonAction.getSilent(), "true");
    assertEquals(buttonAction.getValue(), "buttonValue1");
    assertEquals(buttonAction.getScreenContext(), "home");

    buttonAction = (ButtonAction) button.getElementList().get(1);
    assertTrue(ServerAction.MAINTAIN_ASYNC.equalsStr(buttonAction.getServerAction()));
    assertTrue(Action.SERVER.equalsStr(buttonAction.getType()));
    assertEquals(buttonAction.getTargetAction(), "targetAction2");
    assertEquals(buttonAction.getTarget(), "target2");
    assertEquals(buttonAction.getAsync(), "false");
    assertEquals(buttonAction.getSilent(), "false");
    assertEquals(buttonAction.getValue(), "buttonValue2");
    assertEquals(buttonAction.getScreenContext(), "home");
  }

  /**
   * Build a single screen with a chart
   *
   * @throws Exception
   */
  @Test
  public void addChart() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("buttons")
        .setStyle("expand")
        .setType("div")
        .addChart(new ChartBuilder()
          .setStockChart(true)
          .setAutoload(true)
          .setId("chart1")
          .setAutorefresh(true)
          .setEnableDataLabels(true)
          .setFormatDataLabels("formatDataLabels")
          .setIconLoading(IconLoading.CIRCLEBAR)
          .setStacking(Stacking.PERCENT)
          .setInverted(true)
          .setMax(45)
          .setTheme("chartTheme")
          .setVisible(false)
          .setSubtitle("SUBTITLE")
          .setType(ChartType.AREASPLINE)
          .setZoomType(ChartAxis.Y_AXIS)
          .setChartLegend(new ChartLegendBuilder()
            .setChartLayout(ChartLayout.HORIZONTAL)
            .setAlign(Align.CENTER)
            .setEnabled(true)
            .setFloating(true)
            .setBorderWidth(2))
          .setChartTooltip(new ChartTooltipBuilder()
            .setCrosshairs(ChartAxis.ALL)
            .setEnabled(true)
            .setNumberDecimals(4)
            .setPointFormat("pointFormat")
            .setPrefix("pre")
            .setSuffix("post")
            .setDateFormat("yyyymmdd")
            .setShared(true))
          .addChartParameter(new ChartParameterBuilder()
            .setDataType(DataType.DOUBLE)
            .setName("parameterName")
            .setValue("0.1213"))
          .addChartSerieList(new ChartSerieBuilder()
            .setColor("red")
            .setDrilldown(true)
            .setxAxis("xAxis")
            .setyAxis("yAxis")
            .setxValue("x")
            .setyValue("y")
            .setzValue("z")
            .setDrilldownSerie("drilldownSerie"))
          .addContextButton(new ContextButtonBuilder()
            .setLabel("LABEL")
            .setButtonType(ButtonType.BUTTON)
            .setIcon("icon")
            .setSize("sm")
            .setValue("value"))
          .addContextButton(new ContextSeparatorBuilder())
          .addDependency(new DependencyBuilder()
            .setFormule("formule")
            .setInitial(true)
            .setInvert(true)
            .setLabel("LABEL")
            .setServerAction(ServerAction.CONTROL)
            .setSourceType(SourceType.QUERY)
            .setTargetType(TargetType.ATTRIBUTE)
            .setType(DependencyType.AND)
            .setValue("value")
            .addDependencyAction(new DependencyActionBuilder()
              .setServerAction(ServerAction.GET_SERVER_FILE)
              .setTargetAction("TargetAction")
              .setTarget("target")
              .setAsynchronous(true)
              .setContext("context")
              .setType(DependencyActionType.ADD_ROW)
              .setSilent(true)
              .setValue("value"))
            .addDependencyElement(new DependencyElementBuilder()
              .setAlias("alias")
              .setId("id")
              .setCancel(false)
              .setAttribute(Attribute.CURRENT_ROW_VALUE)
              .setColumn("column")
              .setCondition(Condition.EQUALS)
              .setAttribute2(Attribute.EDITABLE)
              .setColumn2("column2")
              .setId2("id2")))
          .addXAxis(new XAxisBuider())
          .addYAxis(new YAxisBuider())));

    Screen screen = builder.build();
    Chart chart = (Chart) screen.getElementList().get(0).getElementList().get(0);

    assertEquals(chart.getStockChart(), "true");
    assertEquals(chart.getAutoload(), "true");
    assertEquals(chart.getId(), "chart1");
    assertEquals(chart.getAutorefresh(), "true");
    assertEquals(chart.getEnableDataLabels(), "true");
    assertEquals(chart.getFormatDataLabels(), "formatDataLabels");
    assertTrue(IconLoading.CIRCLEBAR.equalsStr(chart.getIconLoading()));
    assertTrue(Stacking.PERCENT.equalsStr(chart.getStacking()));
    assertEquals(chart.getInverted(), "true");
    assertEquals(chart.getMax(), "45");
    assertEquals(chart.getTheme(), "chartTheme");
    assertEquals(chart.getVisible(), "false");
    assertEquals(chart.getSubTitle(), "SUBTITLE");
    assertEquals(chart.getType(), ChartType.AREASPLINE.toString());
    assertTrue(ChartAxis.Y_AXIS.equalsStr(chart.getZoomType()));

    ChartLegend chartLegend = chart.getChartLegend();
    assertTrue(ChartLayout.HORIZONTAL.equalsStr(chartLegend.getLayout()));
    assertTrue(Align.CENTER.equalsStr(chartLegend.getAlign()));
    assertEquals(chartLegend.getEnabled(), "true");
    assertEquals(chartLegend.getFloating(), "true");
    assertEquals(chartLegend.getBorderWidth(), "2");

    ChartTooltip chartTooltip = chart.getChartTooltip();
    assertTrue(ChartAxis.ALL.equalsStr(chartTooltip.getCrosshairs()));
    assertEquals(chartTooltip.getEnabled(), "true");
    assertEquals(chartTooltip.getNumberDecimals(), "4");
    assertEquals(chartTooltip.getPointFormat(), "pointFormat");
    assertEquals(chartTooltip.getPreffix(), "pre");
    assertEquals(chartTooltip.getSuffix(), "post");
    assertEquals(chartTooltip.getDateFormat(), "yyyymmdd");
    assertEquals(chartTooltip.getShared(), "true");

    ChartParameter chartParameter = (ChartParameter) chart.getElementList().get(0);
    assertTrue(DataType.DOUBLE.equalsStr(chartParameter.getType()));
    assertEquals(chartParameter.getName(), "parameterName");
    assertEquals(chartParameter.getValue(), "0.1213");
    assertEquals(0.1213, chartParameter.getParameterValue(JsonNodeFactory.instance.objectNode()).asDouble(), 0.01);

    ChartSerie chartSerie = (ChartSerie) chart.getElementList().get(1);
    assertEquals(chartSerie.getColor(), "red");
    assertEquals(chartSerie.getDrillDown(), "true");
    assertEquals(chartSerie.getxAxis(), "xAxis");
    assertEquals(chartSerie.getyAxis(), "yAxis");
    assertEquals(chartSerie.getxValue(), "x");
    assertEquals(chartSerie.getyValue(), "y");
    assertEquals(chartSerie.getzValue(), "z");
    assertEquals(chartSerie.getDrillDownSerie(), "drilldownSerie");

    ContextButton contextButton = (ContextButton) chart.getElementList().get(2);
    assertEquals(contextButton.getLabel(), "LABEL");
    assertTrue(ButtonType.BUTTON.equalsStr(contextButton.getType()));
    assertEquals(contextButton.getIcon(), "icon");
    assertEquals(contextButton.getSize(), "sm");
    assertEquals(contextButton.getValue(), "value");

    ContextSeparator contextSeparator = (ContextSeparator) chart.getElementList().get(3);
    Dependency dependency = (Dependency) chart.getElementList().get(4);
    assertEquals(dependency.getFormule(), "formule");
    assertEquals(dependency.getInitial(), "true");
    assertEquals(dependency.getInvert(), "true");
    assertEquals(dependency.getLabel(), "LABEL");
    assertTrue(ServerAction.CONTROL.equalsStr(dependency.getServerAction()));
    assertTrue(SourceType.QUERY.equalsStr(dependency.getSourceType()));
    assertTrue(TargetType.ATTRIBUTE.equalsStr(dependency.getTargetType()));
    assertTrue(DependencyType.AND.equalsStr(dependency.getType()));
    assertEquals(dependency.getValue(), "value");

    DependencyAction dependencyAction = (DependencyAction) dependency.getElementList().get(0);
    assertTrue(ServerAction.GET_SERVER_FILE.equalsStr(dependencyAction.getServerAction()));
    assertEquals(dependencyAction.getTargetAction(), "TargetAction");
    assertEquals(dependencyAction.getTarget(), "target");
    assertEquals(dependencyAction.getAsync(), "true");
    assertEquals(dependencyAction.getScreenContext(), "context");
    assertTrue(DependencyActionType.ADD_ROW.equalsStr(dependencyAction.getType()));
    assertEquals(dependencyAction.getSilent(), "true");
    assertEquals(dependencyAction.getValue(), "value");

    DependencyElement dependencyElement = (DependencyElement) dependency.getElementList().get(1);
    assertEquals(dependencyElement.getAlias(), "alias");
    assertEquals(dependencyElement.getId(), "id");
    assertEquals(dependencyElement.getCancel(), "false");
    assertTrue(Attribute.CURRENT_ROW_VALUE.equalsStr(dependencyElement.getAttribute()));
    assertEquals(dependencyElement.getColumn(), "column");
    assertTrue(Condition.EQUALS.equalsStr(dependencyElement.getCondition()));
    assertTrue(Attribute.EDITABLE.equalsStr(dependencyElement.getAttribute2()));
    assertEquals(dependencyElement.getColumn2(), "column2");
    assertEquals(dependencyElement.getId2(), "id2");

  }
}