package com.almis.awe.dao;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

/*
 * File Imports
 */

/**
 * Initial load runner
 * Launches initial load values
 *
 * @author Pablo GARCIA - 20/MAR/2017
 */
public class TemplateDao {

  // Autowired services
  private MenuService menuService;
  private STGroup helpTemplateGroup;

  /**
   * Autowired constructor
   * @param menuService Menu service
   * @param helpTemplateGroup Help templates
   */
  @Autowired
  public TemplateDao(MenuService menuService,
                         @Qualifier("helpTemplateGroup") STGroup helpTemplateGroup) {
    this.menuService = menuService;
    this.helpTemplateGroup = helpTemplateGroup;
  }

  /**
   * Generate option template
   *
   * @param option Option
   * @param level Option level
   * @param developers Help for developers
   * @return Screen template
   * @throws AWException Error generating breadcrumbs
   */
  @Async("threadHelpPoolTaskExecutor")
  public Future<ST> generateOptionHelpAsync(Option option, Integer level, boolean developers) throws AWException {
    // Retrieve code
    return new AsyncResult<>(generateOptionHelp(option, level, developers));
  }

  /**
   * Generate option template
   *
   * @param option Option
   * @param level Option level
   * @param developers Help for developers
   * @return Screen template
   * @throws AWException Error generating breadcrumbs
   */
  public ST generateOptionHelp(Option option, Integer level, boolean developers) throws AWException {
    // Generate template from screen
    ST optionTemplate = helpTemplateGroup.createStringTemplate(helpTemplateGroup.rawGetTemplate(AweConstants.TEMPLATE_HELP_OPTION));

    String optionLabel = option.getLabel();
    if (option.getScreen() != null) {
      Screen screen = menuService.getOptionScreen(option.getName());
      optionLabel = screen.getLabel();
      optionTemplate.add(AweConstants.TEMPLATE_CONTENT, generateScreenHelp(screen, developers));
    }

    // Add screen title
    optionTemplate.add(AweConstants.TEMPLATE_E, option);
    optionTemplate.add(AweConstants.TEMPLATE_TITLE, optionLabel);
    optionTemplate.add(AweConstants.TEMPLATE_LEVEL, level);

    // Retrieve code
    return optionTemplate;
  }

  /**
   * Generate screen template
   *
   * @param screen Screen
   * @param developers Help for developers
   * @return Screen template
   * @throws AWException Error generating breadcrumbs
   */
  private ST generateScreenHelp(Screen screen, boolean developers) {
    // Generate template from screen
    ST screenTemplate = helpTemplateGroup.createStringTemplate(helpTemplateGroup.rawGetTemplate(AweConstants.TEMPLATE_HELP_SCREEN));
    List<ST> contents = new CopyOnWriteArrayList<>();

    // Call generate method on all sources
    for (Element element : screen.getElementList()) {
      // Generate the children
      contents.add(element.generateHelpTemplate(helpTemplateGroup, null, developers));
    }

    // Add screen title
    screenTemplate.add(AweConstants.TEMPLATE_E, screen);
    screenTemplate.add(AweConstants.TEMPLATE_CONTENT, contents);

    // Retrieve code
    return screenTemplate;
  }
}