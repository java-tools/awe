package com.almis.awe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.Tag;
import com.almis.awe.model.entities.screen.component.TagList;
import com.almis.awe.model.type.LoadType;

/**
 * Manage AWE screen access
 */
public class TemplateService extends ServiceConfig {

  // Autowired services
  private MenuService menuService;
  private STGroup elementsTemplateGroup;
  private STGroup helpTemplateGroup;
  private STGroup screensTemplateGroup;
  private QueryService queryService;

  /**
   * Autowired constructor
   * @param menuService Menu service
   * @param elementsTemplateGroup Element templates
   * @param helpTemplateGroup Help templates
   * @param screensTemplateGroup Screen templates
   * @param queryService Query service
   */
  @Autowired
  public TemplateService(MenuService menuService,
                         @Qualifier("elementsTemplateGroup") STGroup elementsTemplateGroup,
                         @Qualifier("helpTemplateGroup") STGroup helpTemplateGroup,
                         @Qualifier("screensTemplateGroup") STGroup screensTemplateGroup,
                         QueryService queryService) {
    this.menuService = menuService;
    this.elementsTemplateGroup = elementsTemplateGroup;
    this.helpTemplateGroup = helpTemplateGroup;
    this.screensTemplateGroup = screensTemplateGroup;
    this.queryService = queryService;
  }

  /**
   * Retrieve a screen template for the default option
   *
   * @return Template
   * @throws AWException error generating template
   */
  @Cacheable(value = "screenTemplates", key = "'default'")
  public String getTemplate() throws AWException {

    // Get screen from option
    Screen screen = menuService.getDefaultScreen();

    // Generate screen template
    return generateScreenTemplate(screen, AweConstants.BASE_VIEW, null);
  }

  /**
   * Retrieve a screen template
   *
   * @param view Screen view
   * @param optionId Screen option identifier
   * @return Template
   * @throws AWException error generating template
   */
  public String getTemplate(String view, String optionId) throws AWException {
    // Get screen from option
    Screen screen = menuService.getAvailableOptionScreen(optionId);
    if (screen == null) {
      throw new AWException(getLocale("ERROR_TITLE_OPTION_NOT_DEFINED"),
        getLocale("ERROR_MESSAGE_OPTION_HAS_NOT_BEEN_DEFINED", optionId));
    }

    // Generate screen template
    return generateScreenTemplate(screen, view, optionId);
  }

  /**
   * Generate screen template
   *
   * @param screen Screen object
   * @param view Screen view
   * @param optionId Option identifier
   * @return Screen template
   * @throws AWException Error generating breadcrumbs
   */
  @Cacheable(value = "screenTemplates", key = "{ #p1, #p2 }")
  public String generateScreenTemplate(Screen screen, String view, String optionId) throws AWException {
    // Generate template from screen
    ST screenTemplate = screensTemplateGroup.createStringTemplate(screensTemplateGroup.rawGetTemplate(screen.getTemplate()));

    // Call generate method on all sources
    List<Tag> tagList = screen.getElementList();
    for (Tag tag : tagList) {
      // Generate the children
      screenTemplate.add(tag.getSource(), tag.generateTemplate(elementsTemplateGroup));
    }

    // Add screen title
    screenTemplate.add(AweConstants.TEMPLATE_TITLE, screen.getLabel());

    // Add breadcrumbs
    if (AweConstants.REPORT_VIEW.equalsIgnoreCase(view)) {
      screenTemplate.add(AweConstants.TEMPLATE_BREADCRUMBS, generateBreadcrumbTemplate(optionId));
    }

    // Retrieve code
    return screenTemplate.render();
  }

  /**
   * Generates screen breadcrumbs
   *
   * @param optionId Option identifier
   * @return Breadcrumbs
   * @throws AWException Error generating breadcrumbs
   */
  private List<ST> generateBreadcrumbTemplate(String optionId) throws AWException {
    // Variable definition
    List<ST> breadcrumbs = new ArrayList<>();
    Option option = menuService.getOptionByName(optionId);

    // Generate breadcrumbs
    if (option != null) {
      Option nextOption = option;
      while (nextOption.getParent() instanceof Option) {
        ST breadcrumb = elementsTemplateGroup.createStringTemplate(elementsTemplateGroup.rawGetTemplate(AweConstants.TEMPLATE_BREADCRUMB));

        nextOption = (Option) nextOption.getParent();
        breadcrumb.add("text", nextOption.getLabel());
        breadcrumbs.add(0, breadcrumb);
      }
    }
    return breadcrumbs;
  }

  /**
   * Generate application help template
   *
   * @param developers Help for developers
   * @return Application help template
   * @throws AWException Error generating breadcrumbs
   */
  public String generateApplicationHelpTemplate(boolean developers) throws AWException {
    Menu menu = menuService.getMenu();

    // Generate template from screen
    ST screenTemplate = screensTemplateGroup.createStringTemplate(screensTemplateGroup.rawGetTemplate(AweConstants.HELP_TEMPLATE));
    ST applicationTemplate = helpTemplateGroup.createStringTemplate(helpTemplateGroup.rawGetTemplate(AweConstants.TEMPLATE_HELP_APPLICATION));
    List<ST> contents = generateMenuHelp(menu.getElementList(), 1, developers);

    // Add application template
    applicationTemplate.add(AweConstants.TEMPLATE_CONTENT, contents);
    screenTemplate.add(AweConstants.TEMPLATE_HELP, applicationTemplate);

    // Retrieve code
    return screenTemplate.render();
  }

  /**
   * Generate option help template
   *
   * @param optionId Option identifier
   * @param developers Help for developers
   * @return Application help template
   * @throws AWException Error generating breadcrumbs
   */
  public String generateOptionHelpTemplate(String optionId, boolean developers) throws AWException {
    // Retrieve code
    Option option = menuService.getOptionByName(optionId);
    return generateOptionHelp(option, 1, developers).render();
  }

  /**
   * Generate error template
   *
   * @param exc Exception
   * @return Error screen template
   */
  public String generateErrorTemplate(AWException exc) {
    ST errorTemplate = screensTemplateGroup.createStringTemplate(screensTemplateGroup.rawGetTemplate(AweConstants.ERROR_TEMPLATE));
    errorTemplate.add(AweConstants.TEMPLATE_TITLE, exc.getTitle());
    errorTemplate.add(AweConstants.TEMPLATE_MESSAGE, exc.getMessage());

    // Retrieve error template
    return errorTemplate.render();
  }

  /**
   * Generate menu help
   *
   * @param elementList Option list
   * @param level Option level
   * @param developers Help for developers
   * @return Screen template
   * @throws AWException Error generating breadcrumbs
   */
  private List<ST> generateMenuHelp(List<Element> elementList, Integer level, boolean developers) throws AWException {
    List<ST> templateList = new ArrayList<>();

    // Call generate method on all elements
    if (elementList != null) {
      for (Element element : elementList) {
        Option option = (Option) element;
        if (option.getLabel() != null) {
          // Generate option template
          templateList.add(generateOptionHelp(option, level, developers));

          // Generate the children
          templateList.addAll(generateMenuHelp(option.getElementList(), level + 1, developers));
        }
      }
    }

    // Retrieve code
    return templateList;
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
  private ST generateOptionHelp(Option option, Integer level, boolean developers) throws AWException {
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
    List<ST> contents = new ArrayList<>();

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

  /**
   * Generates an taglist template from a screen and a taglist id
   *
   * @param tagListId Taglist identifier
   * @return Taglist template
   * @throws AWException error generating taglist template
   */
  public String generateTaglistTemplate(String tagListId) throws AWException {
    return generateTaglistTemplate(menuService.getDefaultScreen(), tagListId);
  }

  /**
   * Generates an taglist template from a screen and a taglist id
   *
   * @param optionId Option identifier
   * @param tagListId Taglist identifier
   * @return Taglist template
   * @throws AWException error generating taglist template
   */
  public String generateTaglistTemplate(String optionId, String tagListId) throws AWException {
    Screen screen = menuService.getOptionScreen(optionId);
    if (screen == null) {
      throw new AWException(getLocale("ERROR_TITLE_OPTION_NOT_DEFINED"), getLocale("ERROR_MESSAGE_OPTION_HAS_NOT_BEEN_DEFINED", optionId));
    }
    return generateTaglistTemplate(screen, tagListId);
  }

  /**
   * Generates an taglist template from a screen and a taglist id
   *
   * @param screen Screen
   * @param tagListId Taglist identifier
   * @return Taglist template
   * @throws AWException error generating taglist template
   */
  public String generateTaglistTemplate(Screen screen, String tagListId) throws AWException {

    TagList tagList = (TagList) (screen.getElementsById(tagListId).get(0));
    DataList data = null;

    // Check initial load attribute
    if (tagList.getInitialLoad() != null) {
      LoadType initialLoadValue = LoadType.valueOf(tagList.getInitialLoad().toUpperCase());
      switch (initialLoadValue) {
        case QUERY:
          data = queryService.launchQuery(tagList.getTargetAction(), "1", tagList.getMax() == null ? "0" : tagList.getMax()).getDataList();
          break;
        case ENUM:
        default:
          data = queryService.launchEnumQuery(tagList.getTargetAction(), "1", tagList.getMax() == null ? "0" : tagList.getMax()).getDataList();
          break;
      }
    }

    // For each row, generate the children code
    return generateTaglistData(data, renderTagList(tagList));
  }

  /**
   * Render taglist template
   * @param template Template
   * @return Template rendered
   */
  public String renderTagList(Element template) {
    StringBuilder builder = new StringBuilder();
    // Call generate method on all children
    for (Element child : template.getElementList()) {
      // Generate the children
      builder.append(child.generateTemplate(elementsTemplateGroup).render());
    }
    return builder.toString();
  }

  /**
   * Generate taglist data
   * @param data Data
   * @param template Row template
   */
  private String generateTaglistData(DataList data, String template) {
    StringBuilder builder = new StringBuilder();
    if (data != null) {
      for (Map<String, CellData> row : data.getRows()) {
        String rowString = template;
        // Create the matcher
        Matcher matcher = TagList.wildcard.matcher(rowString);
        while (matcher.find()) {
          for (int matIdx = 1, matTot = matcher.groupCount(); matIdx <= matTot; matIdx++) {
            String keyCol = matcher.group(matIdx);
            CellData dat = row.get(keyCol);
            rowString = rowString.replace("[" + keyCol + "]", dat.getStringValue());
          }
        }
        builder.append(rowString);
      }
    }
    return builder.toString();
  }

  /**
   * Generates an empty screen
   *
   * @return Empty screen
   */
  public String generateEmptyScreen() {
    // Get empty screen
    return AweConstants.EMPTY_TEMPLATE;
  }
}