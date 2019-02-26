package com.almis.awe.test.unit.builder;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.enumerates.ChartAxis;
import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.screen.*;
import com.almis.awe.builder.screen.accordion.AccordionBuilder;
import com.almis.awe.builder.screen.accordion.AccordionItemBuilder;
import com.almis.awe.builder.screen.button.ButtonActionBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.chart.*;
import com.almis.awe.builder.screen.context.ContextButtonBuilder;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.criteria.CriteriaBuilder;
import com.almis.awe.builder.screen.dependency.DependencyActionBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.builder.screen.dependency.DependencyElementBuilder;
import com.almis.awe.builder.screen.grid.ColumnBuilder;
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
import com.almis.awe.model.entities.screen.component.button.InfoButton;
import com.almis.awe.model.entities.screen.component.chart.*;
import com.almis.awe.model.entities.screen.component.container.AccordionItem;
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
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.Assert.*;
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
    ServiceData serviceData = new ScreenBuilder()
      .setMenuType("public")
      .buildClientAction(aweElements);
    assertEquals("screen", serviceData.getClientActionList().get(0).getType());
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

    assertEquals("HELP", screen.getHelp());
    assertEquals("HELP_IMAGE", screen.getHelpImage());
    assertEquals("true", screen.getKeepCriteria());
    assertEquals("LABEL", screen.getLabel());
    assertEquals("initial_target", screen.getTarget());
    assertEquals("full", screen.getTemplate());
    assertEquals("OnLoad", screen.getOnLoad());
    assertEquals("OnUnLoad", screen.getOnUnload());
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
        .setSource(Source.CENTER)
        .setStyle("expand")
        .setExpandible(Expandible.VERTICAL)
        .setType("div"));
    Screen screen = builder.build();
    assertEquals("LABEL", screen.getElementList().get(0).getLabel());
    assertEquals(Source.CENTER.toString(), screen.getElementList().get(0).getSource());
    assertEquals("expand", screen.getElementList().get(0).getStyle());
    assertTrue(Expandible.VERTICAL.equalsStr(screen.getElementList().get(0).getExpand()));
    assertEquals("div", screen.getElementList().get(0).getType());
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
    assertEquals("MESSAGE_TITLE", ((Message) screen.getElementList().get(0)).getTitle());
    assertEquals("MESSAGE", ((Message) screen.getElementList().get(0)).getMessage());
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
    assertEquals("DIALOG_LABEL", ((Dialog) screen.getElementList().get(0).getElementList().get(0)).getLabel());
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
    assertEquals("TargetScreen", ((Include) screen.getElementList().get(0).getElementList().get(0)).getTargetScreen());
    assertEquals("center", ((Include) screen.getElementList().get(0).getElementList().get(0)).getTargetSource());
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
    assertEquals("ViewName", ((View) screen.getElementList().get(0).getElementList().get(0)).getName());
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
    assertEquals("true", ((TagList) screen.getElementList().get(0).getElementList().get(0)).getAutoload());
    assertEquals("true", ((TagList) screen.getElementList().get(0).getElementList().get(0)).getAutorefresh());
    assertTrue(InitialLoad.QUERY.equalsStr(((TagList) screen.getElementList().get(0).getElementList().get(0)).getInitialLoad()));
    assertEquals("50", ((TagList) screen.getElementList().get(0).getElementList().get(0)).getMax());
    assertTrue(ServerAction.DATA.equalsStr(((TagList) screen.getElementList().get(0).getElementList().get(0)).getServerAction()));
    assertEquals("TagListTargetAction", ((TagList) screen.getElementList().get(0).getElementList().get(0)).getTargetAction());
    assertEquals("div", screen.getElementList().get(0).getElementList().get(0).getType());
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
    assertEquals("left", ((Resizable) screen.getElementList().get(0).getElementList().get(0)).getDirections());
    assertEquals("icon", ((Resizable) screen.getElementList().get(0).getElementList().get(0)).getIcon());
    assertEquals("LABEL", ((Resizable) screen.getElementList().get(0).getElementList().get(0)).getLabel());
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
    assertEquals("agg", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getAggregationField());
    assertTrue(Aggregator.AVERAGE.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getAggregator()));
    assertEquals("true", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getAutoload());
    assertEquals("3", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getCols());
    assertEquals("4", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getRows());
    assertSame(4, ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getDecimalNumbers());
    assertEquals(",", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getDecimalSeparator());
    assertEquals(".", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getThousandSeparator());
    assertTrue(Renderer.COL_HEATMAP.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getRenderer()));
    assertTrue(TotalColumnPlacement.LEFT.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTotalColumnPlacement()));
    assertTrue(TotalRowPlacement.BOTTOM.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTotalRowPlacement()));
    assertTrue(SortMethod.ABSOLUTE.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getSortMethod()));
    assertTrue(InitialLoad.QUERY.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getInitialLoad()));
    assertEquals("50", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getMax());
    assertTrue(ServerAction.DATA.equalsStr(((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getServerAction()));
    assertEquals("targetAction", ((PivotTable) screen.getElementList().get(0).getElementList().get(0)).getTargetAction());
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
    assertEquals("icon", ((Window) screen.getElementList().get(0).getElementList().get(0)).getIcon());
    assertEquals("LABEL", ((Window) screen.getElementList().get(0).getElementList().get(0)).getLabel());
    assertEquals("style", ((Window) screen.getElementList().get(0).getElementList().get(0)).getStyle());
    assertEquals("true", ((Window) screen.getElementList().get(0).getElementList().get(0)).getMaximize());
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
    assertEquals("style", screen.getElementList().get(0).getElementList().get(0).getStyle());
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
    assertEquals("screen", ((Frame) screen.getElementList().get(0).getElementList().get(0)).getScreen());
    assertEquals("var", ((Frame) screen.getElementList().get(0).getElementList().get(0)).getScreenVariable());
    assertEquals("true", ((Frame) screen.getElementList().get(0).getElementList().get(0)).getScroll());
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
    assertEquals("true", ((Accordion) screen.getElementList().get(0).getElementList().get(0)).getAutocollapse());
    assertEquals("selected", ((Accordion) screen.getElementList().get(0).getElementList().get(0)).getSelected());
    assertEquals("notSelected", ((AccordionItem) screen.getElementList().get(0).getElementList().get(0).getElementList().get(0)).getId());
    assertEquals("LABEL", ((AccordionItem) screen.getElementList().get(0).getElementList().get(0).getElementList().get(0)).getLabel());
    assertEquals("selected", ((AccordionItem) screen.getElementList().get(0).getElementList().get(0).getElementList().get(1)).getId());
    assertEquals("OTHER_LABEL", ((AccordionItem) screen.getElementList().get(0).getElementList().get(0).getElementList().get(1)).getLabel());
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
    assertEquals("button_icon", button.getIcon());
    assertEquals("sm", button.getSize());
    assertEquals("button1", button.getId());

    ButtonAction buttonAction = (ButtonAction) button.getElementList().get(0);
    assertTrue(ServerAction.MAINTAIN.equalsStr(buttonAction.getServerAction()));
    assertTrue(Action.ADD_CLASS.equalsStr(buttonAction.getType()));
    assertEquals("targetAction1", buttonAction.getTargetAction());
    assertEquals("target1", buttonAction.getTarget());
    assertEquals("true", buttonAction.getAsync());
    assertEquals("true", buttonAction.getSilent());
    assertEquals("buttonValue1", buttonAction.getValue());
    assertEquals("home", buttonAction.getScreenContext());

    buttonAction = (ButtonAction) button.getElementList().get(1);
    assertTrue(ServerAction.MAINTAIN_ASYNC.equalsStr(buttonAction.getServerAction()));
    assertTrue(Action.SERVER.equalsStr(buttonAction.getType()));
    assertEquals("targetAction2", buttonAction.getTargetAction());
    assertEquals("target2", buttonAction.getTarget());
    assertEquals("false", buttonAction.getAsync());
    assertEquals("false", buttonAction.getSilent());
    assertEquals("buttonValue2", buttonAction.getValue());
    assertEquals("home", buttonAction.getScreenContext());
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
          .setType(ChartType.BUBBLE)
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
              .setView(com.almis.awe.builder.enumerates.View.REPORT)
              .setAttribute(Attribute.CURRENT_ROW_VALUE)
              .setColumn("column")
              .setCondition(Condition.EQUALS)
              .setAttribute2(Attribute.EDITABLE)
              .setColumn2("column2")
              .setEvent(Event.AFTER_ADD_ROW)
              .setId2("id2")))
          .addXAxis(new AxisBuilder()
            .setFormatterFunction(FormatterFunction.FORMAT_CURRENCY_MAGNITUDE)
            .setType(AxisDataType.CATEGORY))
          .addYAxis(new AxisBuilder()
            .setAllowDecimal(true))
          .setType(ChartType.BUBBLE)));

    Screen screen = builder.build();
    Chart chart = (Chart) screen.getElementList().get(0).getElementList().get(0);

    assertEquals("true", chart.getStockChart());
    assertEquals("true", chart.getAutoload());
    assertEquals("chart1", chart.getId());
    assertEquals("true", chart.getAutorefresh());
    assertEquals("true", chart.getEnableDataLabels());
    assertEquals("formatDataLabels", chart.getFormatDataLabels());
    assertTrue(IconLoading.CIRCLEBAR.equalsStr(chart.getIconLoading()));
    assertTrue(Stacking.PERCENT.equalsStr(chart.getStacking()));
    assertEquals("true", chart.getInverted());
    assertEquals("45", chart.getMax());
    assertEquals("chartTheme", chart.getTheme());
    assertEquals("false", chart.getVisible());
    assertEquals("SUBTITLE", chart.getSubTitle());
    assertEquals(chart.getType(), ChartType.BUBBLE.toString());
    assertTrue(ChartAxis.Y_AXIS.equalsStr(chart.getZoomType()));

    ChartLegend chartLegend = chart.getChartLegend();
    assertTrue(ChartLayout.HORIZONTAL.equalsStr(chartLegend.getLayout()));
    assertTrue(Align.CENTER.equalsStr(chartLegend.getAlign()));
    assertEquals("true", chartLegend.getEnabled());
    assertEquals("true", chartLegend.getFloating());
    assertEquals("2", chartLegend.getBorderWidth());

    ChartTooltip chartTooltip = chart.getChartTooltip();
    assertTrue(ChartAxis.ALL.equalsStr(chartTooltip.getCrosshairs()));
    assertEquals("true", chartTooltip.getEnabled());
    assertEquals("4", chartTooltip.getNumberDecimals());
    assertEquals("pointFormat", chartTooltip.getPointFormat());
    assertEquals("pre", chartTooltip.getPreffix());
    assertEquals("post", chartTooltip.getSuffix());
    assertEquals("yyyymmdd", chartTooltip.getDateFormat());
    assertEquals("true", chartTooltip.getShared());

    ChartParameter chartParameter = (ChartParameter) chart.getElementList().get(0);
    assertTrue(DataType.DOUBLE.equalsStr(chartParameter.getType()));
    assertEquals("parameterName", chartParameter.getName());
    assertEquals("0.1213", chartParameter.getValue());
    assertEquals(0.1213, chartParameter.getParameterValue(JsonNodeFactory.instance.objectNode()).asDouble(), 0.01);

    ChartSerie chartSerie = (ChartSerie) chart.getElementList().get(1);
    assertEquals("red", chartSerie.getColor());
    assertEquals("true", chartSerie.getDrillDown());
    assertEquals("xAxis", chartSerie.getxAxis());
    assertEquals("yAxis", chartSerie.getyAxis());
    assertEquals("x", chartSerie.getxValue());
    assertEquals("y", chartSerie.getyValue());
    assertEquals("z", chartSerie.getzValue());
    assertEquals("drilldownSerie", chartSerie.getDrillDownSerie());

    ContextButton contextButton = (ContextButton) chart.getElementList().get(2);
    assertEquals("LABEL", contextButton.getLabel());
    assertTrue(ButtonType.BUTTON.equalsStr(contextButton.getType()));
    assertEquals("icon", contextButton.getIcon());
    assertEquals("sm", contextButton.getSize());
    assertEquals("value", contextButton.getValue());

    ContextSeparator contextSeparator = (ContextSeparator) chart.getElementList().get(3);
    Dependency dependency = (Dependency) chart.getElementList().get(4);
    assertEquals("formule", dependency.getFormule());
    assertEquals("true", dependency.getInitial());
    assertEquals("true", dependency.getInvert());
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
    assertEquals("true", dependencyAction.getAsync());
    assertEquals("context", dependencyAction.getScreenContext());
    assertTrue(DependencyActionType.ADD_ROW.equalsStr(dependencyAction.getType()));
    assertEquals("true", dependencyAction.getSilent());
    assertEquals("value", dependencyAction.getValue());

    DependencyElement dependencyElement = (DependencyElement) dependency.getElementList().get(1);
    assertEquals("alias", dependencyElement.getAlias());
    assertEquals("id", dependencyElement.getId());
    assertEquals("false", dependencyElement.getCancel());
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
   * @throws Exception
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
        .addCriteria(new CriteriaBuilder()
          .setIcon("criterion_icon")
          .setSize("sm")
          .setAreaRows(3)
          .setAutoload(false)
          .setAutorefresh(true)
          .setCapitalize(true)
          .setChecked(true)
          .setCheckEmpty(true)
          .setCheckInitial(true)
          .setCheckTarget("checkTarget")
          .setComponent(Component.CHECKBOX)
          .setDateFormat("dd/mm/yyyy")
          .setDateShowTodayButton(true)
          .setDateViewMode(DateViewMode.MONTHS)
          .setDestination("destination")
          .setGroup("group")
          .setLeftLabel("leftLabel")
          .setNumberFormat("numberFormat")
          .setPlaceholder("placeholder")
          .setPrintable(Printable.EXCEL)
          .setReadonly(true)
          .setStrict(true)
          .setProperty("proper.ty")
          .setShowFutureDates(true)
          .setShowSlider(false)
          .setShowWeekends(true)
          .setUnit("unit1")
          .setValidation("required")
          .setVariable("variable1")
          .setVisible(true)
          .setTimeout(10)
          .setSpecific("specific")
          .setSession("sessionVariable")
          .setOptional(true)
          .setMessage("MESSAGE")
          .setValue("asada")
          .setId("criterion1")));

    Screen screen = builder.build();
    Criteria criterion = (Criteria) screen.getElementList().get(0).getElementList().get(0);
    assertEquals("criterion_icon", criterion.getIcon());
    assertEquals("sm", criterion.getSize());
    assertEquals("3", criterion.getAreaRows());
    assertEquals("false", criterion.getAutoload());
    assertEquals("true", criterion.getAutorefresh());
    assertEquals("true", criterion.getCapitalize());
    assertEquals("true", criterion.getChecked());
    assertEquals("true", criterion.getCheckEmpty());
    assertEquals("true", criterion.getCheckInitial());
    assertEquals("checkTarget", criterion.getCheckTarget());
    assertEquals(criterion.getComponentType(), Component.CHECKBOX.toString());
    assertEquals("dd/mm/yyyy", criterion.getDateFormat());
    assertEquals("true", criterion.getShowTodayButton());
    assertEquals(criterion.getDateViewMode(), DateViewMode.MONTHS.toString());
    assertEquals("destination", criterion.getDestination());
    assertEquals("group", criterion.getGroup());
    assertEquals("leftLabel", criterion.getLeftLabel());
    assertEquals("numberFormat", criterion.getNumberFormat());
    assertEquals("placeholder", criterion.getPlaceholder());
    assertEquals(criterion.getPrintable(), Printable.EXCEL.toString());
    assertEquals("true", criterion.getReadonly());
    assertEquals("true", criterion.getStrict());
    assertEquals("proper.ty", criterion.getProperty());
    assertEquals("true", criterion.getShowFutureDates());
    assertEquals("false", criterion.getShowSlider());
    assertEquals("true", criterion.getShowWeekends());
    assertEquals("unit1", criterion.getUnit());
    assertEquals("required", criterion.getValidation());
    assertEquals("variable1", criterion.getVariable());
    assertEquals("true", criterion.getVisible());
    assertEquals("10", criterion.getTimeout());
    assertEquals("specific", criterion.getSpecific());
    assertEquals("sessionVariable", criterion.getSession());
    assertEquals("true", criterion.getOptional());
    assertEquals("MESSAGE", criterion.getMessage());
    assertEquals("asada", criterion.getValue());
    assertEquals("criterion1", criterion.getId());
  }

  /**
   * Build a single screen with a grid
   *
   * @throws Exception
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
          .setAutorefresh(true)
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
            .setName("groupHeader1")
            .setLabel("LABEL1")
            .addColumnList(new ColumnBuilder()
              .setIcon("criterion_icon")
              .setAutorefresh(true)
              .setChecked(true)
              .setCheckInitial(true)
              .setComponent(Component.CHECKBOX)
              .setDateFormat("dd/mm/yyyy")
              .setDateShowTodayButton(true)
              .setDateViewMode(DateViewMode.MONTHS)
              .setNumberFormat("numberFormat")
              .setPrintable(Printable.EXCEL)
              .setReadonly(true)
              .setStrict(true)
              .setShowFutureDates(true)
              .setShowWeekends(true)
              .setUnit("unit1")
              .setValidation("required")
              .setVisible(true)
              .setOptional(true)
              .setValue("asada")
              .setId("column1")))
          .setId("grid1")));

    Screen screen = builder.build();
    Grid grid = (Grid) screen.getElementList().get(0).getElementList().get(0);
    GroupHeader header = grid.getGroupHeaderModel().get(0);
    Column column = (Column) header.getElementList().get(0);

    assertEquals(true, grid.isTreegrid());
    assertEquals("true", grid.getAutoload());
    assertEquals("true", grid.getAutorefresh());
    assertEquals("true", grid.getCheckboxMultiselect());
    assertEquals("true", grid.getEditable());
    assertEquals("name", grid.getExpandColumn());
    assertEquals("iconCollapse", grid.getIconCollapse());
    assertEquals("iconLeaf", grid.getIconLeaf());
    assertEquals(IconLoading.CIRCLES.toString(), grid.getIconLoading());
    assertEquals("2", grid.getInitialLevel());
    assertEquals(InitialLoad.QUERY.toString(), grid.getInitialLoad());
    assertEquals("true", grid.getMultiselect());
    assertEquals("grid1", grid.getName());
    assertEquals("10,20,30", grid.getPagerValues());
    assertEquals("true", grid.getDisablePagination());
    assertEquals("true", grid.getRowNumbers());
    assertEquals("true", grid.getSendAll());
    assertEquals("false", grid.getSendOperations());
    assertEquals(ServerAction.DATA.toString(), grid.getServerAction());
    assertEquals("lalal", grid.getTargetAction());
    assertEquals(true, grid.isValidateOnSave());
    assertEquals("treeId", grid.getTreeId());
    assertEquals("true", grid.getLoadAll());
    assertEquals("true", grid.getShowTotals());
    assertEquals("treeLeaf", grid.getTreeLeaf());

    assertEquals("LABEL1", header.getLabel());

    assertEquals("criterion_icon", column.getIcon());
    assertEquals("true", column.getAutorefresh());
    assertEquals("true", column.getChecked());
    assertEquals("true", column.getCheckInitial());
    assertEquals(Component.CHECKBOX.toString(), column.getComponentType());
    assertEquals("dd/mm/yyyy", column.getDateFormat());
    assertEquals("true", column.getShowTodayButton());
    assertEquals(DateViewMode.MONTHS.toString(), column.getDateViewMode());
    assertEquals("numberFormat", column.getNumberFormat());
    assertEquals(Printable.EXCEL.toString(), column.getPrintable());
    assertEquals("true", column.getReadonly());
    assertEquals("true", column.getStrict());
    assertEquals("true", column.getShowFutureDates());
    assertEquals("true", column.getShowWeekends());
    assertEquals("unit1", column.getUnit());
    assertEquals("required", column.getValidation());
    assertEquals("true", column.getVisible());
    assertEquals("true", column.getOptional());
    assertEquals("asada", column.getValue());
  }

  /**
   * Build a single screen with an info
   *
   * @throws Exception
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
   * @throws Exception
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
    assertEquals("true", tab.getMaximize());
    assertEquals("tab1", tab.getId());
    assertEquals("LABEL", tabContainer.getLabel());
    assertEquals("div", tabContainer.getType());
  }

  /**
   * Build a single screen with a widget
   *
   * @throws Exception
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
   * @throws Exception
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
}