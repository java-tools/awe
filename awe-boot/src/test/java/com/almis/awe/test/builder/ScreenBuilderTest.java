package com.almis.awe.test.builder;

import com.almis.awe.builder.enumerates.OnClose;
import com.almis.awe.builder.screen.*;
import com.almis.awe.model.entities.screen.Include;
import com.almis.awe.model.entities.screen.Message;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.component.Dialog;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ScreenBuilderTest {

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
      .setTemplate("full");
    Screen screen = builder.build();

    assertEquals(screen.getHelp(), "HELP");
    assertEquals(screen.getHelpImage(), "HELP_IMAGE");
    assertEquals(screen.getKeepCriteria(), "true");
    assertEquals(screen.getLabel(), "LABEL");
    assertEquals(screen.getTarget(), "initial_target");
    assertEquals(screen.getTemplate(), "full");
  }

  /**
   * Build a single screen with an invalid id
   * @throws Exception
   */
  @Test
  public void buildScreenInvalidId() throws Exception {
    ScreenBuilder builder = new ScreenBuilder()
      .setId("aR!$fg");
    Screen screen = builder.build();
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
        .setType("div"));
    Screen screen = builder.build();
    assertEquals(screen.getElementList().get(0).getLabel(), "LABEL");
    assertEquals(screen.getElementList().get(0).getSource(), "center");
    assertEquals(screen.getElementList().get(0).getStyle(), "expand");
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
}