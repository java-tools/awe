package com.almis.awe.service.screen;


import com.almis.awe.config.ServiceConfig;
import com.almis.awe.dao.InitialLoadDao;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dao.AweElementsDao;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.dto.SortColumn;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.Tag;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.screen.component.MenuContainer;
import com.almis.awe.model.entities.screen.component.action.AbstractAction;
import com.almis.awe.model.entities.screen.component.action.ButtonAction;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.entities.screen.component.grid.Grid;
import com.almis.awe.model.entities.screen.data.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.almis.awe.model.constant.AweConstants.NO_TAG;

/**
 * Generate the component controllers of the screen
 */
public class ScreenComponentGenerator extends ServiceConfig {

  // Autowired services
  private final AweRequest aweRequest;
  private final ScreenModelGenerator screenModelGenerator;
  private final ScreenConfigurationGenerator screenConfigurationGenerator;
  private final InitialLoadDao initialLoadDao;
  private final AweElementsDao aweElementsDao;

  @Value("${settings.dataSuffix:.data}")
  private String dataSuffix;

  /**
   * Autowired constructor
   *
   * @param request                      Request
   * @param screenModelGenerator         Screen model generator
   * @param screenConfigurationGenerator Screen configuration generator
   * @param initialLoadDao               Initial load service
   * @param aweElementsDao               AWE Elements DAO
   */
  public ScreenComponentGenerator(AweRequest request, ScreenModelGenerator screenModelGenerator,
                                  ScreenConfigurationGenerator screenConfigurationGenerator, InitialLoadDao initialLoadDao,
                                  AweElementsDao aweElementsDao) {
    this.aweRequest = request;
    this.screenModelGenerator = screenModelGenerator;
    this.screenConfigurationGenerator = screenConfigurationGenerator;
    this.initialLoadDao = initialLoadDao;
    this.aweElementsDao = aweElementsDao;
  }

  /**
   * Generate component map
   *
   * @param screen         Screen object
   * @param data           Screen data
   * @param menu           Current menu
   * @param storedCriteria Stored criteria
   * @return Component map
   */
  public Map<String, ScreenComponent> generateComponentMap(Screen screen, ScreenData data, Menu menu, ObjectNode storedCriteria) {
    Map<String, ScreenComponent> componentMap = new LinkedHashMap<>();
    List<AweThreadInitialization> initializationList = new ArrayList<>();

    // Generate screen configuration
    AweThreadInitialization screenConfigurationThread = screenModelGenerator.getScreenConfigurationThread();

    // Generate screen target (if it exists)
    if (screen.getTarget() != null) {
      // Add a screen target for each
      String[] screenTargetList = screen.getTarget().split(AweConstants.COMMA_SEPARATOR);
      for (String screenTarget : screenTargetList) {
        screenModelGenerator.addScreenTarget(initializationList, screenTarget);
      }
    }

    try {
      // Launch configuration thread first
      Future<ServiceData> configurationResult = initialLoadDao.launchInitialLoad(screenConfigurationThread);

      // Apply screen configuration
      screenConfigurationGenerator.applyScreenConfiguration(configurationResult, screen);

      // Add component
      List<Component> components = screen.getElementsByType(Component.class);
      for (Component component : components) {
        generateComponent(screen.getId(), component, initializationList, componentMap, storedCriteria, menu);
      }

      // Launch all initial load actions and store the values
      screenModelGenerator.launchInitialLoadList(initializationList, componentMap, data);
    } catch (AWException error) {
      data.addError(error);
      getLogger().log(ScreenComponentGenerator.class, Level.ERROR, getLocale("ERROR_MESSAGE_SCREEN_COMPONENT_MAP"), error);
    } catch (Exception error) {
      data.addError(new AWException(getLocale("ERROR_TITLE_SCREEN_GENERATION_ERROR"), error));
      getLogger().log(ScreenComponentGenerator.class, Level.ERROR, getLocale("ERROR_MESSAGE_SCREEN_COMPONENT_MAP"), error);
    }

    return componentMap;
  }

  /**
   * Generate component
   *
   * @param containerId        Container id
   * @param component          Component
   * @param initializationList Initialization list
   * @param componentMap       Component map
   * @param storedCriteria     Stored criteria
   * @param menu               Current menu
   * @throws AWException
   */
  private void generateComponent(String containerId, Component component, List<AweThreadInitialization> initializationList, Map<String, ScreenComponent> componentMap, ObjectNode storedCriteria, Menu menu) throws AWException {
    if (!NO_TAG.equalsIgnoreCase(component.getComponentTag())) {
      // Generate component model
      ScreenComponent screenComponent = new ScreenComponent();
      screenComponent
        .setId(component.getElementKey())
        .setController(component.copy())
        .setModel(new ComponentModel());

      // If the component has an initial value, add it to the model
      if (component instanceof AbstractCriteria) {
        screenModelGenerator.generateScreenCriterionModel((AbstractCriteria) screenComponent.getController(), screenComponent, storedCriteria);
      }

      // Store initial load
      if (component.getInitialLoad() != null) {
        screenModelGenerator.addComponentTarget(initializationList, component);
      }

      // Generate grid
      if (component instanceof Grid) {
        generateScreenGrid((Grid) screenComponent.getController(), screenComponent, initializationList);
      }

      // Generate menu
      if (component instanceof MenuContainer) {
        generateScreenMenu((MenuContainer) screenComponent.getController(), menu, initializationList);
      }

      // Add screenComponent to map if not duplicated
      if (!componentMap.containsKey(component.getElementKey())) {
        componentMap.put(component.getElementKey(), screenComponent);
      } else {
        throw new AWException(getLocale("ERROR_TITLE_SCREEN_GENERATION_ERROR"),
          getLocale("ERROR_MESSAGE_DUPLICATED_IDENTIFIER", component.getElementKey(), containerId));
      }
    }
  }

  /**
   * Generate menu for component
   *
   * @param menuContainer Menu container
   * @param currentMenu   Current menu
   */
  private void generateScreenMenu(MenuContainer menuContainer, Menu currentMenu, List<AweThreadInitialization> initializationList) throws AWException {
    // If the component is a menu container, store the menu in the component
    Menu menu = currentMenu.copy();
    menuContainer.setMenu(menu);

    // Add menu restrictions to initialization list
    screenModelGenerator.addMenuTarget(initializationList, menuContainer);

    // Apply option actions
    applyOptionActions(menu);
  }

  /**
   * Apply option actions to menu
   *
   * @param menu Menu
   */
  public void applyOptionActions(Menu menu) {
    // For each option, add actions
    List<Option> options = menu.getElementsByType(Option.class);
    for (Option option : options) {
      if (option.getActionList() == null) {
        String serverAction = option.getServerAction() != null ? option.getServerAction() : menu.getDefaultAction();
        String context = option.getScreenContext() != null ? option.getScreenContext() : menu.getScreenContext();
        AbstractAction action = ButtonAction.builder()
          .serverAction(serverAction)
          .screenContext(context)
          .target(option.getName())
          .build();

        option.setActionList(Arrays.asList(action));
      }
    }
  }

  /**
   * Generate grid component
   *
   * @param grid               Grid component
   * @param initializationList Initialization list
   */
  private void generateScreenGrid(Grid grid, ScreenComponent screenComponent, List<AweThreadInitialization> initializationList) throws AWException {
    // If the component is a grid, store columns' initial load
    List<Column> columns = grid.getElementsByType(Column.class);
    List<ScreenColumn> columnList = new ArrayList<>();
    Map<String, String> sortMap = new HashMap<>();
    String gridId = grid.getElementKey();

    // Get sort if stored
    ObjectNode parameters = aweRequest.getParameterList();
    String specialAttributesKey = gridId + dataSuffix;
    if (parameters.has(specialAttributesKey)) {
      ObjectNode specialAttributesNode = (ObjectNode) parameters.get(specialAttributesKey);

      // Specific sort
      if (specialAttributesNode.has(AweConstants.COMPONENT_SORT)) {
        ArrayNode sortNodeList = (ArrayNode) specialAttributesNode.get(AweConstants.COMPONENT_SORT);
        for (JsonNode sortColumnNode : sortNodeList) {
          SortColumn sortColumn = new SortColumn((ObjectNode) sortColumnNode);
          sortMap.put(sortColumn.getColumnId(), sortColumn.getDirection());
        }
      }
    }

    // If grid has data, store in model
    screenModelGenerator.generateComponentModelFromDataList(grid.getDataList(), screenComponent);

    // Get grid columns
    for (Column column : columns) {
      // Generate component model
      ScreenColumn columnComponent = new ScreenColumn();
      Column columnController = column.copy();

      // Add sort if defined
      if (sortMap.containsKey(column.getName())) {
        columnController.setSortColumn(sortMap.get(column.getName()));
      }

      // Generate column
      columnComponent
        .setGridId(gridId)
        .setController(columnController)
        .setId(column.getName())
        .setModel(new ComponentModel());

      // Store column data if defined
      screenModelGenerator.generateComponentModelFromDataList(column.getDataList(), columnComponent);

      // Add screen component to grid
      columnList.add(columnComponent);

      // Initialize column data
      if (column.getInitialLoad() != null) {
        screenModelGenerator.addColumnTarget(initializationList, gridId, column);
      }
    }

    // Set column list
    grid.setColumnList(columnList);
  }


  /**
   * Generate taglist component map
   *
   * @param templateList Taglist replaced templates
   * @return Taglist component map
   */
  public List<Element> generateTagListElements(List<String> templateList) {
    // Render template into taglist
    return templateList.stream().map(s -> aweElementsDao.parseTemplate(Element.class, s)).collect(Collectors.toList());
  }

  /**
   * Generate taglist component map
   *
   * @param templateElements Taglist replaced elements
   * @return Taglist component map
   */
  public Map<String, ScreenComponent> generateTagListComponentMap(String tagListId, List<Element> templateElements) throws AWException {
    Map<String, ScreenComponent> taglistComponents = new HashMap<>();

    // Add component
    List<Component> components = new Tag().setElementList(templateElements).getElementsByType(Component.class);
    for (Component component : components) {
      generateComponent(tagListId, component, Collections.emptyList(), taglistComponents, JsonNodeFactory.instance.objectNode(), null);
    }

    // Retrieve taglist components
    return taglistComponents;
  }
}
