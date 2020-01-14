package com.almis.awe.test.unit.builder;

import com.almis.awe.builder.enumerates.ChartAxis;
import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.screen.*;
import com.almis.awe.builder.screen.accordion.AccordionBuilder;
import com.almis.awe.builder.screen.accordion.AccordionItemBuilder;
import com.almis.awe.builder.screen.button.ButtonActionBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.chart.*;
import com.almis.awe.builder.screen.context.ContextButtonBuilder;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.criteria.CheckboxCriteriaBuilder;
import com.almis.awe.builder.screen.criteria.FilteredCalendarCriteriaBuilder;
import com.almis.awe.builder.screen.dependency.DependencyActionBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.builder.screen.dependency.DependencyElementBuilder;
import com.almis.awe.builder.screen.grid.CalendarColumnBuilder;
import com.almis.awe.builder.screen.grid.CheckboxColumnBuilder;
import com.almis.awe.builder.screen.grid.GridBuilder;
import com.almis.awe.builder.screen.grid.GroupHeaderBuilder;
import com.almis.awe.builder.screen.info.InfoBuilder;
import com.almis.awe.builder.screen.info.InfoButtonBuilder;
import com.almis.awe.builder.screen.info.InfoCriteriaBuilder;
import com.almis.awe.builder.screen.tab.TabBuilder;
import com.almis.awe.builder.screen.tab.TabContainerBuilder;
import com.almis.awe.builder.screen.widget.WidgetBuilder;
import com.almis.awe.builder.screen.widget.WidgetParameterBuilder;
import com.almis.awe.builder.screen.wizard.WizardBuilder;
import com.almis.awe.builder.screen.wizard.WizardPanelBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.screen.View;
import com.almis.awe.model.entities.screen.*;
import com.almis.awe.model.entities.screen.component.*;
import com.almis.awe.model.entities.screen.component.action.ButtonAction;
import com.almis.awe.model.entities.screen.component.action.Dependency;
import com.almis.awe.model.entities.screen.component.action.DependencyAction;
import com.almis.awe.model.entities.screen.component.action.DependencyElement;
import com.almis.awe.model.entities.screen.component.button.Button;
import com.almis.awe.model.entities.screen.component.button.ContextButton;
import com.almis.awe.model.entities.screen.component.button.ContextSeparator;
import com.almis.awe.model.entities.screen.component.button.InfoButton;
import com.almis.awe.model.entities.screen.component.chart.*;
import com.almis.awe.model.entities.screen.component.container.TabContainer;
import com.almis.awe.model.entities.screen.component.container.WizardPanel;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import com.almis.awe.model.entities.screen.component.criteria.InfoCriteria;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.entities.screen.component.grid.Grid;
import com.almis.awe.model.entities.screen.component.grid.GroupHeader;
import com.almis.awe.model.entities.screen.component.panelable.Accordion;
import com.almis.awe.model.entities.screen.component.panelable.Tab;
import com.almis.awe.model.entities.screen.component.panelable.Wizard;
import com.almis.awe.model.entities.screen.component.pivottable.PivotTable;
import com.almis.awe.model.entities.screen.component.widget.Widget;
import com.almis.awe.model.entities.screen.component.widget.WidgetParameter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
   * @throws Exception exception
   */
  @Test
  public void buildClientAction() throws Exception {
    ServiceData serviceData = new ScreenBuilder()
      .setMenuType("public")
      .buildClientAction(aweElements);
    assertEquals("screen", serviceData.getClientActionList().get(0).getType());
  }

  /**
   * Build a single screen without elements
   *
   * @throws Exception exception
   */
  @Test
  public void build() throws Exception {
    ScreenBuilder builder = new ScreenBuilder();
    builder
      .setId(UUID.randomUUID().toString())
      .setKeepCriteria(true)
      .setTarget("initial_target")
      .setOnLoad("OnLoad")
      .setOnUnload("OnUnLoad")
      .setTemplate("full")
      .setLabel("LABEL")
      .setHelp("HELP")
      .setHelpImage("HELP_IMAGE");
    Screen screen = builder.build();

    assertEquals("HELP", screen.getHelp());
    assertEquals("HELP_IMAGE", screen.getHelpImage());
    assertSame(true, screen.isKeepCriteria());
    assertEquals("LABEL", screen.getLabel());
    assertEquals("initial_target", screen.getTarget());
    assertEquals("full", screen.getTemplate());
    assertEquals("OnLoad", screen.getOnLoad());
    assertEquals("OnUnLoad", screen.getOnUnload());
  }

  /**
   * Build a single screen with an invalid id
    * @throws AWException exception
   */
  @Test(expected = AWException.class)
  public void buildScreenInvalidId() throws AWException {
    ScreenBuilder builder = new ScreenBuilder().setId("aR!$fg");
    builder.build();
  }

  /**
   * Build a single screen with a tag
   *
   * @throws Exception exception
   */
  @Test
  public void addTag() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource(Source.CENTER.toString())
        .setType("div")
        .setLabel("LABEL")
        .setStyle("expand")
        .setExpandible(Expandible.VERTICAL));
    Screen screen = builder.build();
    assertEquals("LABEL", screen.getElementList().get(0).getLabel());
    assertEquals(Source.CENTER.toString(), screen.getElementsByType(Tag.class).get(0).getSource());
    assertEquals("expand", screen.getElementList().get(0).getStyle());
    assertTrue(Expandible.VERTICAL.equalsStr(screen.getElementList().get(0).getExpand()));
    assertEquals("div", screen.getElementList().get(0).getType());
  }

  /**
   * Build a single screen with a tag
   *
   * @throws Exception exception
   */
  @Test
  public void addMessage() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addMessage(new MessageBuilder()
        .setTitle("MESSAGE_TITLE")
        .setMessage("MESSAGE"));
    Screen screen = builder.build();
    assertEquals("MESSAGE_TITLE", screen.getElementList().get(0).getTitle());
    assertEquals("MESSAGE", ((Message) screen.getElementList().get(0)).getText());
  }

  /**
   * Build a single screen with a dialog
   *
   * @throws Exception exception
   */
  @Test
  public void addDialog() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addDialog(new DialogBuilder().setOnClose(OnClose.ACCEPT).setLabel("DIALOG_LABEL")));

    Screen screen = builder.build();
    assertEquals("DIALOG_LABEL", screen.getElementList().get(0).getElementList().get(0).getLabel());
    assertTrue(OnClose.ACCEPT.equalsStr(((Dialog) screen.getElementList().get(0).getElementList().get(0)).getOnClose()));
  }

  /**
   * Build a single screen with an include
   *
   * @throws Exception exception
   */
  @Test
  public void addInclude() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addInclude(new IncludeBuilder()
          .setTargetScreen("TargetScreen")
          .setTargetSource("center")));

    Screen screen = builder.build();
    assertEquals("TargetScreen", ((Include) screen.getElementList().get(0).getElementList().get(0)).getTargetScreen());
    assertEquals("center", ((Include) screen.getElementList().get(0).getElementList().get(0)).getTargetSource());
  }

  /**
   * Build a single screen with a view
   *
   * @throws Exception exception
   */
  @Test
  public void addView() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addView(new ViewBuilder()
          .setName("ViewName")));

    Screen screen = builder.build();
    assertEquals("ViewName", ((View) screen.getElementList().get(0).getElementList().get(0)).getName());
  }

  /**
   * Build a single screen with a tag list
   *
   * @throws Exception exception
   */
  @Test
  public void addTagList() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addTagList(new TagListBuilder()
          .setAutoload(true)
          .setAutorefresh(5)
          .setInitialLoad(InitialLoad.QUERY)
          .setMax(50)
          .setServerAction(ServerAction.DATA)
          .setTargetAction("TagListTargetAction")
          .setType("div")));

    Screen screen = builder.build();
    assertSame(true, ((TagList) screen.getElementList().get(0).getElementList().get(0)).isAutoload());
    assertSame(5, ((TagList) screen.getElementList().get(0).getElementList().get(0)).getAutorefresh());
    assertTrue(InitialLoad.QUERY.equalsStr(((TagList) screen.getElementList().get(0).getElementList().get(0)).getInitialLoad()));
    assertSame(50, ((TagList) screen.getElementList().get(0).getElementList().get(0)).getMax());
    assertTrue(ServerAction.DATA.equalsStr(((TagList) screen.getElementList().get(0).getElementList().get(0)).getServerAction()));
    assertEquals("TagListTargetAction", ((TagList) screen.getElementList().get(0).getElementList().get(0)).getTargetAction());
    assertEquals("div", screen.getElementList().get(0).getElementList().get(0).getType());
  }

  /**
   * Build a single screen with a resizable
   *
   * @throws Exception exception
   */
  @Test
  public void addResizable() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addResizable(new ResizableBuilder()
          .setDirections("left")
          .setLabel("LABEL")));

    Screen screen = builder.build();
    assertEquals("left", ((Resizable) screen.getElementList().get(0).getElementList().get(0)).getDirections());
    assertEquals("LABEL", screen.getElementList().get(0).getElementList().get(0).getLabel());
  }

  /**
   * Build a single screen with a pivot table
   *
   * @throws Exception exception
   */
  @Test
  public void addPivotTable() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addPivotTable(new PivotTableBuilder()
          .setAggregationField("agg")
          .setAggregator(Aggregator.AVERAGE)
          .setCols("Als,Nom")
          .setRows("Test, Tutu")
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
          .setTargetAction("targetAction")
          .setAutoload(true)));

    Screen screen = builder.build();
    assertEquals("agg", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getAggregationField());
    assertTrue(Aggregator.AVERAGE.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getAggregator()));
    assertSame(true, ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).isAutoload());
    assertSame("Als,Nom", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getCols());
    assertSame("Test, Tutu", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getRows());
    assertSame(4, ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getDecimalNumbers());
    assertEquals(",", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getDecimalSeparator());
    assertEquals(".", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getThousandSeparator());
    assertTrue(Renderer.COL_HEATMAP.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getRenderer()));
    assertTrue(TotalColumnPlacement.LEFT.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTotalColumnPlacement()));
    assertTrue(TotalRowPlacement.BOTTOM.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTotalRowPlacement()));
    assertTrue(SortMethod.ABSOLUTE.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getSortMethod()));
    assertTrue(InitialLoad.QUERY.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getInitialLoad()));
    assertSame(50, ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getMax());
    assertTrue(ServerAction.DATA.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getServerAction()));
    assertEquals("targetAction", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTargetAction());
  }

  /**
   * Build a single screen with a window
   *
   * @throws Exception exception
   */
  @Test
  public void addWindow() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addWindow(new WindowBuilder()
          .setMaximize(true)
          .setIcon("icon")
          .setLabel("LABEL")
          .setStyle("style")));

    Screen screen = builder.build();
    assertEquals("icon", ((Window) screen.getElementList().get(0).getElementList().get(0)).getIcon());
    assertEquals("LABEL", screen.getElementList().get(0).getElementList().get(0).getLabel());
    assertEquals("style", screen.getElementList().get(0).getElementList().get(0).getStyle());
    assertTrue(((Window) screen.getElementList().get(0).getElementList().get(0)).isMaximize());
  }

  /**
   * Build a single screen with a menu
   *
   * @throws Exception exception
   */
  @Test
  public void addMenu() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addMenuContainer(new MenuContainerBuilder()
          .setType(MenuType.HORIZONTAL)
          .setStyle("style")));

    Screen screen = builder.build();
    assertTrue(MenuType.HORIZONTAL.equalsStr(screen.getElementList().get(0).getElementList().get(0).getType()));
    assertEquals("style", screen.getElementList().get(0).getElementList().get(0).getStyle());
  }

  /**
   * Build a single screen with a frame
   *
   * @throws Exception exception
   */
  @Test
  public void addFrame() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addFrame(new FrameBuilder()
          .setScreen("screen")
          .setScreenVariable("var")
          .setScroll(true)
          .setServerAction(ServerAction.APPLICATION_HELP)));

    Screen screen = builder.build();
    assertEquals("screen", ((Frame) screen.getElementList().get(0).getElementList().get(0)).getScreen());
    assertEquals("var", ((Frame) screen.getElementList().get(0).getElementList().get(0)).getScreenVariable());
    assertSame(true, ((Frame) screen.getElementList().get(0).getElementList().get(0)).isScroll());
    assertTrue(ServerAction.APPLICATION_HELP.equalsStr(((Frame) screen.getElementList().get(0).getElementList().get(0)).getServerAction()));
  }

  /**
   * Build a single screen with an accordion
   *
   * @throws Exception exception
   */
  @Test
  public void addAccordion() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("center")
        .setLabel("LABEL")
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
    assertSame(true, ((Accordion) screen.getElementList().get(0).getElementList().get(0)).isAutocollapse());
    assertEquals("selected", ((Accordion) screen.getElementList().get(0).getElementList().get(0)).getSelected());
    assertEquals("notSelected", screen.getElementList().get(0).getElementList().get(0).getElementList().get(0).getId());
    assertEquals("LABEL", screen.getElementList().get(0).getElementList().get(0).getElementList().get(0).getLabel());
    assertEquals("selected", screen.getElementList().get(0).getElementList().get(0).getElementList().get(1).getId());
    assertEquals("OTHER_LABEL", screen.getElementList().get(0).getElementList().get(0).getElementList().get(1).getLabel());
  }

  /**
   * Build a single screen with a button
   *
   * @throws Exception exception
   */
  @Test
  public void addButton() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("buttons")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addButton(new ButtonBuilder()
          .setIcon("button_icon")
          .setSize("sm")
          .setId("button1")
          .addButtonAction(new ButtonActionBuilder()
            .setTargetAction("targetAction1")
            .setAsynchronous(true)
            .setTarget("target1")
            .setSilent(true)
            .setValue("buttonValue1")
            .setContext("home")
            .setLabel("label")
            .setType(Action.ADD_CLASS))
          .addButtonAction(new ButtonActionBuilder()
            .setServerAction(ServerAction.MAINTAIN_ASYNC)
            .setTargetAction("targetAction2")
            .setAsynchronous(false)
            .setSilent(false)
            .setTarget("target2")
            .setValue("buttonValue2")
            .setContext("home")
            .setType(Action.SERVER))
        ));

    Screen screen = builder.build();
    Button button = (Button) screen.getElementList().get(0).getElementList().get(0);
    assertEquals("button_icon", button.getIcon());
    assertEquals("sm", button.getSize());
    assertEquals("button1", button.getId());

    ButtonAction buttonAction = (ButtonAction) button.getElementList().get(0);
    assertNull(buttonAction.getServerAction());
    assertTrue(Action.ADD_CLASS.equalsStr(buttonAction.getType()));
    assertEquals("targetAction1", buttonAction.getTargetAction());
    assertEquals("target1", buttonAction.getTarget());
    assertSame(true, buttonAction.isAsync());
    assertSame(true, buttonAction.isSilent());
    assertEquals("buttonValue1", buttonAction.getValue());
    assertEquals("home", buttonAction.getScreenContext());
    assertEquals("label", buttonAction.getLabel());

    buttonAction = (ButtonAction) button.getElementList().get(1);
    assertTrue(ServerAction.MAINTAIN_ASYNC.equalsStr(buttonAction.getServerAction()));
    assertTrue(Action.SERVER.equalsStr(buttonAction.getType()));
    assertEquals("targetAction2", buttonAction.getTargetAction());
    assertEquals("target2", buttonAction.getTarget());
    assertSame(false, buttonAction.isAsync());
    assertSame(false, buttonAction.isSilent());
    assertEquals("buttonValue2", buttonAction.getValue());
    assertEquals("home", buttonAction.getScreenContext());
  }

  /**
   * Build a single screen with a chart
   *
   * @throws Exception exception
   */
  @Test
  public void addChart() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setSource("buttons")
        .setLabel("LABEL")
        .setStyle("expand")
        .setType("div")
        .addChart(new ChartBuilder()
          .setStockChart(true)
          .setAutoload(true)
          .setId("chart1")
          .setAutorefresh(5)
          .setEnableDataLabels(true)
          .setFormatDataLabels("formatDataLabels")
          .setIconLoading(IconLoading.CIRCLEBAR)
          .setStacking(Stacking.PERCENT)
          .setInverted(true)
          .setMax(45)
          .setTheme("chartTheme")
          .setVisible(false)
          .setSubtitle("SUBTITLE")
          .setChartType(ChartType.BUBBLE)
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
            .setDrillDown(true)
            .setColor("red")
            .setXAxis("xAxis")
            .setYAxis("yAxis")
            .setXValue("x")
            .setYValue("y")
            .setZValue("z")
            .setDrillDownSerie("drilldownSerie"))
          .addContextButton(new ContextButtonBuilder()
            .setButtonType(ButtonType.BUTTON)
            .setValue("value")
            .setIcon("icon")
            .setSize("sm")
            .setLabel("LABEL"))
          .addContextSeparator(new ContextSeparatorBuilder())
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
              .setType(Action.ADD_ROW)
              .setSilent(true)
              .setValue("value"))
            .addDependencyElement(new DependencyElementBuilder()
              .setAlias("alias")
              .setId("id")
              .setCancel(false)
              .setView(com.almis.awe.builder.enumerates.View.REPORT)
              .setAttribute(Attribute.CURRENT_ROW_VALUE)
              .setColumn("column")
              .setCondition(Condition.EQUALS)
              .setAttribute2(Attribute.EDITABLE)
              .setColumn2("column2")
              .setEvent(Event.AFTER_ADD_ROW)
              .setId2("id2")))
          .addAxis(new AxisBuilder()
            .setFormatterFunction(FormatterFunction.FORMAT_CURRENCY_MAGNITUDE)
            .setType(AxisDataType.CATEGORY))
          .addAxis(new AxisBuilder()
            .setAllowDecimal(true))
          .setChartType(ChartType.BUBBLE)));

    Screen screen = builder.build();
    Chart chart = (Chart) screen.getElementList().get(0).getElementList().get(0);

    assertSame(true, chart.isStockChart());
    assertSame(true, chart.isAutoload());
    assertEquals("chart1", chart.getId());
    assertSame(5, chart.getAutorefresh());
    assertSame(true, chart.isEnableDataLabels());
    assertEquals("formatDataLabels", chart.getFormatDataLabels());
    assertTrue(IconLoading.CIRCLEBAR.equalsStr(chart.getIconLoading()));
    assertTrue(Stacking.PERCENT.equalsStr(chart.getStacking()));
    assertSame(true, chart.isInverted());
    assertSame(45, chart.getMax());
    assertEquals("chartTheme", chart.getTheme());
    assertSame(false, chart.isVisible());
    assertEquals("SUBTITLE", chart.getSubTitle());
    assertEquals(chart.getType(), ChartType.BUBBLE.toString());
    assertTrue(ChartAxis.Y_AXIS.equalsStr(chart.getZoomType()));

    ChartLegend chartLegend = chart.getChartLegend();
    assertTrue(ChartLayout.HORIZONTAL.equalsStr(chartLegend.getLayout()));
    assertTrue(Align.CENTER.equalsStr(chartLegend.getAlign()));
    assertSame(true, chartLegend.isEnabled());
    assertSame(true, chartLegend.isFloating());
    assertSame(2, chartLegend.getBorderWidth());

    ChartTooltip chartTooltip = chart.getChartTooltip();
    assertTrue(ChartAxis.ALL.equalsStr(chartTooltip.getCrosshairs()));
    assertSame(true, chartTooltip.isEnabled());
    assertSame(4, chartTooltip.getNumberDecimals());
    assertEquals("pointFormat", chartTooltip.getPointFormat());
    assertEquals("pre", chartTooltip.getPrefix());
    assertEquals("post", chartTooltip.getSuffix());
    assertEquals("yyyymmdd", chartTooltip.getDateFormat());
    assertSame(true, chartTooltip.isShared());

    ChartParameter chartParameter = (ChartParameter) chart.getElementList().get(0);
    assertTrue(DataType.DOUBLE.equalsStr(chartParameter.getType()));
    assertEquals("parameterName", chartParameter.getName());
    assertEquals("0.1213", chartParameter.getValue());
    assertEquals(0.1213, chartParameter.getParameterValue(JsonNodeFactory.instance.objectNode()).asDouble(), 0.01);

    ChartSerie chartSerie = (ChartSerie) chart.getElementList().get(1);
    assertEquals("red", chartSerie.getColor());
    assertSame(true, chartSerie.isDrillDown());
    assertEquals("xAxis", chartSerie.getXAxis());
    assertEquals("yAxis", chartSerie.getYAxis());
    assertEquals("x", chartSerie.getXValue());
    assertEquals("y", chartSerie.getYValue());
    assertEquals("z", chartSerie.getZValue());
    assertEquals("drilldownSerie", chartSerie.getDrillDownSerie());

    ContextButton contextButton = (ContextButton) chart.getElementList().get(2);
    assertEquals("LABEL", contextButton.getLabel());
    assertTrue(ButtonType.BUTTON.equalsStr(contextButton.getButtonType()));
    assertEquals("icon", contextButton.getIcon());
    assertEquals("sm", contextButton.getSize());
    assertEquals("value", contextButton.getValue());

    ContextSeparator contextSeparator = (ContextSeparator) chart.getElementList().get(3);
    assertNotNull(contextSeparator);
    Dependency dependency = (Dependency) chart.getElementList().get(4);
    assertEquals("formule", dependency.getFormule());
    assertSame(true, dependency.isInitial());
    assertSame(true, dependency.isInvert());
    assertEquals("LABEL", dependency.getLabel());
    assertTrue(ServerAction.CONTROL.equalsStr(dependency.getServerAction()));
    assertTrue(SourceType.QUERY.equalsStr(dependency.getSourceType()));
    assertTrue(TargetType.ATTRIBUTE.equalsStr(dependency.getTargetType()));
    assertTrue(DependencyType.AND.equalsStr(dependency.getType()));
    assertEquals("value", dependency.getValue());

    DependencyAction dependencyAction = (DependencyAction) dependency.getElementList().get(0);
    assertTrue(ServerAction.GET_SERVER_FILE.equalsStr(dependencyAction.getServerAction()));
    assertEquals("TargetAction", dependencyAction.getTargetAction());
    assertEquals("target", dependencyAction.getTarget());
    assertSame(true, dependencyAction.isAsync());
    assertEquals("context", dependencyAction.getScreenContext());
    assertTrue(Action.ADD_ROW.equalsStr(dependencyAction.getType()));
    assertSame(true, dependencyAction.isSilent());
    assertEquals("value", dependencyAction.getValue());

    DependencyElement dependencyElement = (DependencyElement) dependency.getElementList().get(1);
    assertEquals("alias", dependencyElement.getAlias());
    assertEquals("id", dependencyElement.getId());
    assertSame(false, dependencyElement.isCancel());
    assertTrue(Attribute.CURRENT_ROW_VALUE.equalsStr(dependencyElement.getAttribute()));
    assertEquals("column", dependencyElement.getColumn());
    assertEquals(com.almis.awe.builder.enumerates.View.REPORT.toString(), dependencyElement.getView());
    assertEquals(Event.AFTER_ADD_ROW.toString(), dependencyElement.getEvent());
    assertTrue(Condition.EQUALS.equalsStr(dependencyElement.getCondition()));
    assertTrue(Attribute.EDITABLE.equalsStr(dependencyElement.getAttribute2()));
    assertEquals("column2", dependencyElement.getColumn2());
    assertEquals("id2", dependencyElement.getId2());
  }

  /**
   * Build a single screen with a criterion
   *
   * @throws Exception exception
   */
  @Test
  public void addCriterion() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("buttons")
        .setStyle("expand")
        .setType("div")
        .addCriteria(
          new CheckboxCriteriaBuilder()
            .setChecked(true)
            .setGroup("group")
            .setIcon("criterion_icon")
            .setSize("sm")
            .setAutoload(false)
            .setAutorefresh(5)
            .setCapitalize(true)
            .setCheckEmpty(true)
            .setCheckInitial(true)
            .setLeftLabel(20)
            .setPlaceholder("placeholder")
            .setPrintable(Printable.EXCEL)
            .setProperty("proper.ty")
            .setUnit("unit1")
            .setValidation("required")
            .setVariable("variable1")
            .setSpecific("specific")
            .setSession("sessionVariable")
            .setMessage("MESSAGE")
            .setValue("asada")
            .setId("criterion1"),
          new FilteredCalendarCriteriaBuilder()
            .setDateFormat("dd/mm/yyyy")
            .setDateShowTodayButton(true)
            .setDateViewMode(DateViewMode.MONTHS)
            .setIcon("criterion_icon")
            .setSize("lg")
            .setAutoload(false)
            .setAutorefresh(5)
            .setCapitalize(true)
            .setCheckEmpty(true)
            .setCheckInitial(true)
            .setLeftLabel(20)
            .setPlaceholder("placeholder")
            .setPrintable(Printable.EXCEL)
            .setReadonly(true)
            .setProperty("proper.ty")
            .setShowFutureDates(true)
            .setShowWeekends(true)
            .setUnit("unit1")
            .setValidation("required")
            .setVariable("variable1")
            .setSpecific("specific")
            .setSession("sessionVariable")
            .setMessage("MESSAGE")
            .setValue("dddd")
            .setId("criterion2")));

    Screen screen = builder.build();
    Criteria criterion = (Criteria) screen.getElementList().get(0).getElementList().get(0);
    Criteria criterion2 = (Criteria) screen.getElementList().get(0).getElementList().get(1);
    assertEquals("criterion_icon", criterion.getIcon());
    assertEquals("sm", criterion.getSize());
    assertSame(false, criterion.isAutoload());
    assertSame(5, criterion.getAutorefresh());
    assertSame(true, criterion.isCapitalize());
    assertSame(true, criterion.isChecked());
    assertSame(true, criterion.isCheckEmpty());
    assertSame(true, criterion.isCheckInitial());
    assertEquals(criterion.getComponentType(), Component.CHECKBOX.toString());
    assertEquals("group", criterion.getGroup());
    assertSame(20, criterion.getLeftLabel());
    assertEquals("placeholder", criterion.getPlaceholder());
    assertEquals(criterion.getPrintable(), Printable.EXCEL.toString());
    assertSame(false, criterion.isReadonly());
    assertSame(true, criterion.isStrict());
    assertEquals("proper.ty", criterion.getProperty());
    assertEquals("unit1", criterion.getUnit());
    assertEquals("required", criterion.getValidation());
    assertEquals("variable1", criterion.getVariable());
    assertSame(true, criterion.isVisible());
    assertEquals("specific", criterion.getSpecific());
    assertEquals("sessionVariable", criterion.getSession());
    assertEquals("MESSAGE", criterion.getMessage());
    assertEquals("asada", criterion.getValue());
    assertEquals("criterion1", criterion.getId());

    assertEquals("criterion_icon", criterion2.getIcon());
    assertEquals("lg", criterion2.getSize());
    assertSame(false, criterion2.isAutoload());
    assertSame(5, criterion2.getAutorefresh());
    assertTrue(criterion2.isCapitalize());
    assertTrue(criterion2.isCheckEmpty());
    assertTrue(criterion2.isCheckInitial());
    assertEquals(criterion2.getComponentType(), Component.FILTERED_CALENDAR.toString());
    assertEquals("dd/mm/yyyy", criterion2.getDateFormat());
    assertSame(true, criterion2.isShowTodayButton());
    assertEquals(criterion2.getDateViewMode(), DateViewMode.MONTHS.toString());
    assertSame(20, criterion2.getLeftLabel());
    assertEquals("placeholder", criterion2.getPlaceholder());
    assertEquals(criterion2.getPrintable(), Printable.EXCEL.toString());
    assertSame(true, criterion2.isReadonly());
    assertEquals("proper.ty", criterion2.getProperty());
    assertSame(true, criterion2.isShowFutureDates());
    assertSame(false, criterion2.isShowSlider());
    assertSame(true, criterion2.isShowWeekends());
    assertEquals("unit1", criterion2.getUnit());
    assertEquals("required", criterion2.getValidation());
    assertEquals("variable1", criterion2.getVariable());
    assertSame(true, criterion2.isVisible());
    assertEquals("specific", criterion2.getSpecific());
    assertEquals("sessionVariable", criterion2.getSession());
    assertEquals("MESSAGE", criterion2.getMessage());
    assertEquals("dddd", criterion2.getValue());
    assertEquals("criterion2", criterion2.getId());
  }

  /**
   * Build a single screen with a grid
   *
   * @throws Exception exception
   */
  @Test
  public void addGrid() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("buttons")
        .setStyle("expand")
        .setType("div")
        .addGrid(new GridBuilder()
          .setTreeGrid(true)
          .setAutoload(true)
          .setAutorefresh(5)
          .setCheckMultiselect(true)
          .setEditable(true)
          .setExpandColumn("name")
          .setIconCollapse("iconCollapse")
          .setIconLeaf("iconLeaf")
          .setIconLoading(IconLoading.CIRCLES)
          .setInitialLevel(2)
          .setInitialLoad(InitialLoad.QUERY)
          .setMultiselect(true)
          .setName("grid1")
          .setPagerValues("10,20,30")
          .setPaginationDisabled(true)
          .setRowNumbers(true)
          .setSendAll(true)
          .setSendOperations(false)
          .setServerAction(ServerAction.DATA)
          .setTargetAction("lalal")
          .setValidateOnSave(true)
          .setTreeId("treeId")
          .setLoadAll(true)
          .setShowTotals(true)
          .setTreeLeaf("treeLeaf")
          .addGroupHeader(new GroupHeaderBuilder()
            .setLabel("LABEL1")
            .addColumnList(new CheckboxColumnBuilder()
              .setIcon("criterion_icon")
              .setAutorefresh(5)
              .setAlign(Align.CENTER)
              .setChecked(true)
              .setCheckInitial(true)
              .setPrintable(Printable.EXCEL)
              .setReadonly(true)
              .setUnit("unit1")
              .setValidation("required")
              .setValue("asada")
              .setId("column1"),
            new CalendarColumnBuilder()
              .setDateFormat("dd/mm/yyyy")
              .setDateShowTodayButton(true)
              .setDateViewMode(DateViewMode.MONTHS)
              .setShowFutureDates(true)
              .setDataType(DataType.STRING)
              .setShowWeekends(true)
              .setPrintable(Printable.ALL)
              .setId("column2")
            ))
          .setId("grid1")));

    Screen screen = builder.build();
    Grid grid = (Grid) screen.getElementList().get(0).getElementList().get(0);
    GroupHeader header = grid.getGroupHeaderModel().get(0);
    Column column = (Column) header.getElementList().get(0);
    Column column2 = (Column) header.getElementList().get(1);

    assertSame(true, grid.isTreegrid());
    assertSame(true, grid.isAutoload());
    assertSame(5, grid.getAutorefresh());
    assertSame(true, grid.isCheckboxMultiselect());
    assertSame(true, grid.isEditable());
    assertEquals("name", grid.getExpandColumn());
    assertEquals("iconCollapse", grid.getIconCollapse());
    assertEquals("iconLeaf", grid.getIconLeaf());
    assertEquals(IconLoading.CIRCLES.toString(), grid.getIconLoading());
    assertSame(2, grid.getInitialLevel());
    assertEquals(InitialLoad.QUERY.toString(), grid.getInitialLoad());
    assertSame(true, grid.isMultiselect());
    assertEquals("grid1", grid.getName());
    assertEquals("10,20,30", grid.getPagerValues());
    assertSame(true, grid.isDisablePagination());
    assertSame(true, grid.isRowNumbers());
    assertSame(true, grid.isSendAll());
    assertSame(false, grid.isMultioperation());
    assertEquals(ServerAction.DATA.toString(), grid.getServerAction());
    assertEquals("lalal", grid.getTargetAction());
    assertSame(true, grid.isValidateOnSave());
    assertEquals("treeId", grid.getTreeId());
    assertSame(true, grid.isLoadAll());
    assertSame(true, grid.isShowTotals());
    assertEquals("treeLeaf", grid.getTreeLeaf());

    assertEquals("LABEL1", header.getLabel());

    assertEquals("criterion_icon", column.getIcon());
    assertSame(5, column.getAutorefresh());
    assertSame(true, column.isChecked());
    assertSame(true, column.isCheckInitial());
    assertEquals(Printable.EXCEL.toString(), column.getPrintable());
    assertSame(true, column.isReadonly());
    assertEquals("unit1", column.getUnit());
    assertEquals("required", column.getValidation());
    assertSame(true, column.isVisible());
    assertEquals("asada", column.getValue());
    assertEquals(Component.CHECKBOX.toString(), column.getComponentType());

    assertEquals(Component.DATE.toString(), column2.getComponentType());
    assertEquals("dd/mm/yyyy", column2.getDateFormat());
    assertSame(true, column2.isShowTodayButton());
    assertEquals(DateViewMode.MONTHS.toString(), column2.getDateViewMode());
    assertEquals(Printable.ALL.toString(), column2.getPrintable());
    assertSame(true, column2.isShowFutureDates());
    assertSame(true, column2.isShowWeekends());
  }

  /**
   * Build a single screen with an info
   *
   * @throws Exception exception
   */
  @Test
  public void addInfo() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addInfo(new InfoBuilder()
          .setDropdownStyle("popup")
          .setName("tutu")
          .addInfoButton(new InfoButtonBuilder()
            .setInfoStyle("infoStyle")
            .setType("button"))
          .addInfoCriteria(new InfoCriteriaBuilder()
            .setTitle("title")
            .setInfoStyle("infoStyle2")
            .setType("criteriaType"))));

    Screen screen = builder.build();
    Info info = (Info) screen.getElementList().get(0).getElementList().get(0);
    InfoButton infoButton = (InfoButton) info.getElementList().get(0);
    InfoCriteria infoCriteria = (InfoCriteria) info.getElementList().get(1);
    assertEquals("popup", info.getDropdownStyle());
    assertEquals("tutu", info.getName());
    assertEquals("infoStyle", infoButton.getInfoStyle());
    assertEquals("button", infoButton.getType());
    assertEquals("infoStyle2", infoCriteria.getInfoStyle());
    assertEquals("title", infoCriteria.getTitle());
    assertEquals("criteriaType", infoCriteria.getType());
  }

  /**
   * Build a single screen with a tab
   *
   * @throws Exception exception
   */
  @Test
  public void addTab() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addTab(new TabBuilder()
          .setInitialLoad(InitialLoad.ENUMERATED)
          .setMaximize(true)
          .setId("tab1")
          .addTabContainerList(new TabContainerBuilder()
            .setLabel("LABEL")
            .setType("div"))));

    Screen screen = builder.build();
    Tab tab = (Tab) screen.getElementList().get(0).getElementList().get(0);
    TabContainer tabContainer = (TabContainer) tab.getElementList().get(0);
    assertEquals(InitialLoad.ENUMERATED.toString(), tab.getInitialLoad());
    assertSame(true, tab.isMaximize());
    assertEquals("tab1", tab.getId());
    assertEquals("LABEL", tabContainer.getLabel());
    assertEquals("div", tabContainer.getType());
  }

  /**
   * Build a single screen with a widget
   *
   * @throws Exception exception
   */
  @Test
  public void addWidget() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addWidget(new WidgetBuilder()
          .setComponent(WidgetComponent.FILE_VIEWER)
          .setStyle("widgetStyle")
          .setId("widget1")
          .addWidgetParameter(new WidgetParameterBuilder()
            .setType(DataType.ARRAY)
            .setValue("12,24"))));

    Screen screen = builder.build();
    Widget widget = (Widget) screen.getElementList().get(0).getElementList().get(0);
    WidgetParameter widgetParameter = (WidgetParameter) widget.getElementList().get(0);
    assertEquals(WidgetComponent.FILE_VIEWER.toString(), widget.getComponentType());
    assertEquals("widgetStyle", widget.getStyle());
    assertEquals("widget1", widget.getId());
    assertEquals("12,24", widgetParameter.getValue());
    assertEquals(DataType.ARRAY.toString(), widgetParameter.getType());
  }

  /**
   * Build a single screen with a wizard
   *
   * @throws Exception exception
   */
  @Test
  public void addWizard() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId(UUID.randomUUID().toString())
      .addTag(new TagBuilder()
        .setLabel("LABEL")
        .setSource("center")
        .setStyle("expand")
        .setType("div")
        .addWizard(new WizardBuilder()
          .setInitialLoad(InitialLoad.ENUMERATED)
          .setId("tab1")
          .addWizardPanel(new WizardPanelBuilder()
            .setLabel("LABEL")
            .setType("div"))));

    Screen screen = builder.build();
    Wizard wizard = (Wizard) screen.getElementList().get(0).getElementList().get(0);
    WizardPanel wizardPanel = (WizardPanel) wizard.getElementList().get(0);
    assertEquals(InitialLoad.ENUMERATED.toString(), wizard.getInitialLoad());
    assertEquals("tab1", wizard.getId());
    assertEquals("LABEL", wizardPanel.getLabel());
    assertEquals("div", wizardPanel.getType());
  }

  /**
   * Set null screen
   */
  @Test(expected = NullPointerException.class)
  public void setNullScreen() {
    when(aweElements.setScreen(any())).thenCallRealMethod();
    aweElements.setScreen(null);
  }
}