package com.almis.awe.test.builder;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.screen.*;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.screen.Include;
import com.almis.awe.model.entities.screen.Message;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.View;
import com.almis.awe.model.entities.screen.component.Dialog;
import com.almis.awe.model.entities.screen.component.Resizable;
import com.almis.awe.model.entities.screen.component.TagList;
import com.almis.awe.model.entities.screen.component.Window;
import com.almis.awe.model.entities.screen.component.pivottable.PivotTable;
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
}