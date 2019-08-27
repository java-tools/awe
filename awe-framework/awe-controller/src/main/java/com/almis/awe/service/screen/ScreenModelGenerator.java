package com.almis.awe.service.screen;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.screen.component.MenuContainer;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.entities.screen.component.grid.Grid;
import com.almis.awe.model.entities.screen.data.AweThreadInitialization;
import com.almis.awe.model.entities.screen.data.ComponentModel;
import com.almis.awe.model.entities.screen.data.ScreenComponent;
import com.almis.awe.model.entities.screen.data.ScreenData;
import com.almis.awe.model.type.InputType;
import com.almis.awe.model.type.LoadType;
import com.almis.awe.service.InitialLoadService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;

/**
 * Generate the component model of the screen
 */
public class ScreenModelGenerator extends ServiceConfig {

  // Autowired services
  private ScreenRestrictionGenerator screenRestrictionGenerator;
  private InitialLoadService initialLoadService;

  @Value("${settings.dataSuffix:.data}")
  private String dataSuffix;

  /**
   * Autowired constructor
   * @param screenRestrictionGenerator Screen restriction generator
   * @param initialLoadService Initial load service
   */
  @Autowired
  public ScreenModelGenerator(ScreenRestrictionGenerator screenRestrictionGenerator, InitialLoadService initialLoadService) {
    this.screenRestrictionGenerator = screenRestrictionGenerator;
    this.initialLoadService = initialLoadService;
  }

  /**
   * Add screen target to initialization list
   *
   * @param initializationList Initialization list
   * @param screenTarget Screen target
   */
  void addScreenTarget(List<AweThreadInitialization> initializationList, String screenTarget) {
    initializationList.add(new AweThreadInitialization().setParameters(getRequest().getParametersSafe()).setTarget(screenTarget.trim()).setInitialLoadType(LoadType.SCREEN));
  }

  /**
   * Add screen configuration to initialization list
   * @return Screen configuration thread
   */
  AweThreadInitialization getScreenConfigurationThread() {
    return new AweThreadInitialization().setParameters(getRequest().getParametersSafe().put(AweConstants.COMPONENT_MAX, "0")).setTarget(AweConstants.SCREEN_CONFIGURATION_QUERY);
  }

  /**
   * Add component target to initialization list
   *
   * @param initializationList Initialization list
   * @param component Component
   */
  void addMenuTarget(List<AweThreadInitialization> initializationList, Component component) {
    // Set initialization list
    initializationList.add(new AweThreadInitialization()
            .setTarget(AweConstants.SCREEN_RESTRICTION_QUERY)
            .setParameters(getRequest().getParametersSafe().put(AweConstants.COMPONENT_MAX, "0"))
            .setComponentId(component.getElementKey())
            .setInitialLoadType(LoadType.MENU));
  }

  /**
   * Add component target to initialization list
   *
   * @param initializationList Initialization list
   * @param component Component
   */
  void addComponentTarget(List<AweThreadInitialization> initializationList, Component component) {
    // Calculate elements per page
    ObjectNode parameters = getRequest().getParametersSafe();

    // Elements per page
    parameters.put(AweConstants.COMPONENT_MAX, component.getMax());

    // Get sort if stored
    String specialAttributesKey = component.getElementKey() + dataSuffix;
    if (parameters.has(specialAttributesKey)) {
      ObjectNode specialAttributesNode = (ObjectNode) parameters.get(specialAttributesKey);

      // Specific max
      if (specialAttributesNode.has(AweConstants.COMPONENT_MAX)) {
        parameters.set(AweConstants.COMPONENT_MAX, specialAttributesNode.get(AweConstants.COMPONENT_MAX));
      }

      // Specific page
      if (specialAttributesNode.has(AweConstants.COMPONENT_PAGE)) {
        parameters.set(AweConstants.COMPONENT_PAGE, specialAttributesNode.get(AweConstants.COMPONENT_PAGE));
      }

      // Specific sort
      if (specialAttributesNode.has(AweConstants.COMPONENT_SORT)) {
        parameters.set(AweConstants.COMPONENT_SORT, specialAttributesNode.get(AweConstants.COMPONENT_SORT));
      }
    }

    // Load all
    if (component.isLoadAll()) {
      parameters.put(AweConstants.COMPONENT_MAX, 0);
    }

    // Set initialization list
    initializationList.add(new AweThreadInitialization()
      .setParameters(parameters)
      .setComponentId(component.getElementKey())
      .setTarget(component.getTargetAction())
      .setInitialLoadType(LoadType.valueOf(component.getInitialLoad().toUpperCase())));
  }

  /**
   * Add column target to initialization list
   *
   * @param initializationList Initialization list
   * @param gridId Grid identifier
   * @param column Column
   */
  void addColumnTarget(List<AweThreadInitialization> initializationList, String gridId, Column column) {
    ObjectNode parameters = getRequest().getParametersSafe();
    parameters.put(AweConstants.COMPONENT_MAX, column.getMax());

    initializationList.add(new AweThreadInitialization()
      .setParameters(parameters)
      .setComponentId(gridId)
      .setColumnId(column.getName())
      .setTarget(column.getTargetAction())
      .setInitialLoadType(LoadType.valueOf(column.getInitialLoad().toUpperCase())));
  }

  /**
   * Launch initial load list in threads and update screen component values
   *
   * @param initializationList Initialization thread list
   * @param componentMap Component map
   * @param data Screen data
   * @throws AWException Error launching initial load list
   */
  void launchInitialLoadList(List<AweThreadInitialization> initializationList, Map<String, ScreenComponent> componentMap, ScreenData data) throws AWException {
    Map<String, Future<ServiceData>> taskScreenMap = new HashMap<>();
    Map<String, Future<ServiceData>> taskComponentMap = new HashMap<>();
    Map<String, Map<String, Future<ServiceData>>> taskColumnMap = new HashMap<>();

    // Generate a thread for each initialization
    for (AweThreadInitialization initializationData : initializationList) {
      // Launch
      Future<ServiceData> taskResult = initialLoadService.launchInitialLoad(initializationData);

      // If load type is screen, store screen results in list, otherwise store in a component map
      if (LoadType.MENU.equals(initializationData.getInitialLoadType())) {
        MenuContainer menuContainer = (MenuContainer) componentMap.get(initializationData.getComponentId()).getController();
        storeMenuRestrictions(taskResult, menuContainer, data);
      } else if (LoadType.SCREEN.equals(initializationData.getInitialLoadType())) {
        taskScreenMap.put(initializationData.getTarget(), taskResult);
      } else if (initializationData.getColumnId() != null) {
        if (!taskColumnMap.containsKey(initializationData.getComponentId())) {
          taskColumnMap.put(initializationData.getComponentId(), new HashMap<>());
        }
        taskColumnMap.get(initializationData.getComponentId()).put(initializationData.getColumnId(), taskResult);
      } else {
        taskComponentMap.put(initializationData.getComponentId(), taskResult);
      }
    }

    // Store screen target data
    storeScreenTargetData(taskScreenMap, componentMap, data);

    // Store component data
    storeComponentTargetData(taskComponentMap, componentMap, data);

    // Store component data
    storeColumnTargetData(taskColumnMap, componentMap, data);
  }

  /**
   * Store screen target data in components
   *
   * @param taskScreenMap Screen task map
   * @param componentMap Component map
   * @param data Screen data
   */
  private void storeScreenTargetData(Map<String, Future<ServiceData>> taskScreenMap, Map<String, ScreenComponent> componentMap, ScreenData data) {
    // Retrieve screen data if defined
    for (Entry<String, Future<ServiceData>> entry : taskScreenMap.entrySet()) {
      try {
        ServiceData screenTargetOutput = entry.getValue().get();
        DataList screenTargetData = (DataList) screenTargetOutput.getVariableMap().get(AweConstants.ACTION_DATA).getObjectValue();

        // Add all client actions generated
        data.getActions().addAll(screenTargetOutput.getClientActionList());

        // For each column, store value in components
        addScreenTargetDataToComponent(screenTargetData, componentMap);
      } catch (Exception exc) {
        String errorMessage = getLocale("ERROR_MESSAGE_RETRIEVING_INITIAL_DATA", entry.getKey());
        data.addError(new AWException(getLocale("ERROR_TITLE_SCREEN_GENERATION_ERROR"), errorMessage, exc));
        getLogger().log(ScreenModelGenerator.class, Level.ERROR, errorMessage, exc);
      }
    }
  }

  /**
   * Store menu restrictions
   *
   * @param taskResult Task result
   * @param menuContainer Menu container
   * @param data Screen data
   */
  private void storeMenuRestrictions(Future<ServiceData> taskResult, MenuContainer menuContainer, ScreenData data) {
    try {
      ServiceData screenConfigurationOutput = taskResult.get();
      DataList screenRestriction = (DataList) screenConfigurationOutput.getVariableMap().get(AweConstants.ACTION_DATA).getObjectValue();
      screenRestrictionGenerator.applyScreenRestriction(screenRestriction, menuContainer.getMenu());
    } catch (Exception exc) {
      String screen = data.getScreenProperties().get(AweConstants.JSON_SCREEN);
      String errorMessage = getLocale("ERROR_MESSAGE_SCREEN_RESTRICTIONS", screen);
      data.addError(new AWException(getLocale("ERROR_TITLE_SCREEN_GENERATION_ERROR"), errorMessage, exc));
      getLogger().log(ScreenModelGenerator.class, Level.ERROR, errorMessage + screen, exc);
    }
  }

  /**
   * Store component target data
   *
   * @param taskComponentMap Component task map
   * @param componentMap Component map
   * @param data Screen data
   */
  private void storeComponentTargetData(Map<String, Future<ServiceData>> taskComponentMap, Map<String, ScreenComponent> componentMap, ScreenData data) {
    // Retrieve component initial data
    for (Entry<String, Future<ServiceData>> entry : taskComponentMap.entrySet()) {
      Future<ServiceData> serviceDataFuture = entry.getValue();
      ScreenComponent component = componentMap.get(entry.getKey());

      // Retrieve future data
      storeDataInComponent(serviceDataFuture, component, data);
    }
  }

  /**
   * Store column target data
   *
   * @param taskColumnMap Component task map
   * @param componentMap Component map
   * @param data Screen data
   */
  private void storeColumnTargetData(Map<String, Map<String, Future<ServiceData>>> taskColumnMap, Map<String, ScreenComponent> componentMap, ScreenData data) {
    // Retrieve component initial data
    for (Entry<String, Map<String, Future<ServiceData>>> entryMap : taskColumnMap.entrySet()) {
      Map<String, Future<ServiceData>> columnServiceDataMap = entryMap.getValue();
      Grid grid = (Grid) componentMap.get(entryMap.getKey()).getController();
      for (Entry<String, Future<ServiceData>> entry: columnServiceDataMap.entrySet()) {
        // Retrieve future data
        storeDataInComponent(entry.getValue(), grid.getColumnById(entry.getKey()), data);
      }
    }
  }

  /**
   * Store data in component
   *
   * @param futureData Future
   * @param component Screen component
   * @param data Screen data
   */
  private void storeDataInComponent(Future<ServiceData> futureData, ScreenComponent component, ScreenData data) {
    // Retrieve future data
    String target = component.getController().getTargetAction();
    try {
      ServiceData componentTargetOutput = futureData.get();
      DataList componentData = (DataList) componentTargetOutput.getVariableMap().get(AweConstants.ACTION_DATA).getObjectValue();

      // Add all client actions generated
      data.getActions().addAll(componentTargetOutput.getClientActionList());

      // Store values in component
      ComponentModel model = component.getModel();
      if (LoadType.VALUE.toString().equalsIgnoreCase(component.getController().getInitialLoad())) {
        List<CellData> selectedData = getSelectedData(componentData, "value");
        model.setDefaultValues(selectedData);
        model.setSelected(selectedData);
      }

      // Set values, page, total and records
      model.setValues(componentData.getRows());
      model.setPage(componentData.getPage());
      model.setTotal(componentData.getTotal());
      model.setRecords(componentData.getRecords());

    } catch (Exception exc) {
      String errorMessage = getLocale("ERROR_MESSAGE_RETRIEVING_INITIAL_DATA_COMPONENT", target);
      data.addError(new AWException(errorMessage, exc));
      getLogger().log(ScreenModelGenerator.class, Level.ERROR, errorMessage, exc);
    }
  }

  /**
   * Add components to Screen Data
   *
   * @param data Screen initial data
   * @param componentMap Component map
   */
  private void addScreenTargetDataToComponent(DataList data, Map<String, ScreenComponent> componentMap) {
    // For each column, store value in components
    if (!data.getRows().isEmpty()) {
      Map<String, CellData> firstRow = data.getRows().get(0);
      for (String componentId : firstRow.keySet()) {
        if (componentMap.containsKey(componentId)) {
          ScreenComponent component = componentMap.get(componentId);
          component.getModel().setSelected(getSelectedData(data, componentId));
        }
      }
    }
  }

  /**
   * Get a list of selected data
   *
   * @param data Target action data
   * @param componentId Component identifier
   */
  private List<CellData> getSelectedData(DataList data, String componentId) {
    // For each column, store value in components
    List<CellData> selectedData = new ArrayList<>();
    for (Map<String, CellData> row : data.getRows()) {
      if (row.containsKey(componentId)) {
        selectedData.add(row.get(componentId));
      }
    }
    return selectedData;
  }

  /**
   * Generate static values from component and add them to screen component model
   *
   * @param criterion Criterion object
   * @param screenComponent Screen component
   * @param screenParameters Screen paremeters (stored from keep-criteria)
   */
  void generateScreenCriterionModel(AbstractCriteria criterion, ScreenComponent screenComponent, ObjectNode screenParameters) {
    ComponentModel model = screenComponent.getModel();
    List<CellData> defaultValues = getDefaultValues(criterion, screenParameters);
    if (defaultValues != null) {
      model.setSelected(new ArrayList<>(defaultValues));
      model.setDefaultValues(defaultValues);
    }

    // Initialize Checkbox and Radio models
    initializeCheckboxRadioModel(criterion, screenComponent);
  }

  /**
   * Generate static values from component and add them to screen component model
   *
   * @param criterion Criterion object
   * @param screenComponent Screen component
   */
  private void initializeCheckboxRadioModel(AbstractCriteria criterion, ScreenComponent screenComponent) {
    ComponentModel model = screenComponent.getModel();
    // Not Query or Enum
    String component = criterion.getComponentType();
    if (component != null) {
      component = component.replace('-', '_').toUpperCase();
      switch (InputType.valueOf(component)) {
        case CHECKBOX:
        case BUTTON_CHECKBOX:
          Map<String, CellData> checkboxValues = new HashMap<>();
          CellData checkboxValue = criterion.getValue() != null ? new CellData(criterion.getValue()) : new CellData(AweConstants.CHECKBOX_DEFAULT_VALUE);
          checkboxValues.put(AweConstants.JSON_VALUE_PARAMETER, checkboxValue);
          checkboxValues.put(AweConstants.JSON_LABEL_PARAMETER, checkboxValue);
          model.getValues().add(checkboxValues);

          // Check VARIABLE VALUE
          if (model.getSelected().isEmpty() && criterion.isChecked()) {
            // Check attribute CHECKED to add selected VALUE
            model.getSelected().add(checkboxValue);
            model.getDefaultValues().add(checkboxValue);
          }
          break;

        case RADIO:
        case BUTTON_RADIO:
          Map<String, CellData> radioValues = new HashMap<>();
          CellData radioValue = new CellData(criterion.getValue());
          radioValues.put(AweConstants.JSON_LABEL_PARAMETER, new CellData(criterion.getId()));
          radioValues.put(AweConstants.JSON_VALUE_PARAMETER, radioValue);
          model.getValues().add(radioValues);

          model.getSelected().clear();

          // Radio VARIABLE VALUE
          if (model.getSelected().isEmpty() && criterion.isChecked()) {
            // Check attribute CHECKED to add selected VALUE
            model.getSelected().add(radioValue);
          }
          break;
        default:
          break;
      }
    }
  }

  /**
   * Check screen parameters
   *
   * @param parameterId Parameter identifier
   * @return Check if VALUE is
   */
  private boolean checkParameter(String parameterId, ObjectNode parameterList) {
    boolean valueIsGood = false;
    if (parameterId != null && parameterList.has(parameterId)) {
      valueIsGood = checkParameterValue(parameterList.get(parameterId));
    }
    return valueIsGood;
  }

  /**
   * Check screen parameters
   *
   * @param parameter Parameter
   * @return Check if VALUE is
   */
  private boolean checkParameterValue(JsonNode parameter) {
    boolean valueIsGood = false;
    if (parameter != null) {
      String variableAsText = parameter.toString();
      valueIsGood = !variableAsText.isEmpty() && !"[]".equalsIgnoreCase(variableAsText);
    }
    return valueIsGood;
  }

  /**
   * Get a list of default values
   *
   * @param criterion Criterion object
   * @return List of criterion values
   */
  private List<CellData> getDefaultValues(AbstractCriteria criterion, ObjectNode storedParameters) {
    List<CellData> defaultValues = null;
    AweRequest request = getRequest();
    AweSession session = getSession();

    // ----------------------------------------------------------------------
    // 1º) Check VARIABLES from context
    // ----------------------------------------------------------------------
    if (checkParameter(criterion.getVariable(), request.getParameterList())) {
      defaultValues = request.getParameterAsCellDataList(criterion.getVariable());
      // ----------------------------------------------------------------------
      // 2º) Check VARIABLES from stored criteria (keep-criteria)
      // ----------------------------------------------------------------------
    } else if (checkParameter(criterion.getElementKey(), storedParameters)) {
      defaultValues = request.getParameterAsCellDataList(storedParameters.get(criterion.getElementKey()));
      // ----------------------------------------------------------------------
      // 3º) Check attribute SESSION
      // ----------------------------------------------------------------------
    } else if (criterion.getSession() != null && session.getParameter(criterion.getSession()) != null) {
      String sessionValue = session.getParameter(String.class, criterion.getSession());
      defaultValues = getDefaultValuesAsString(sessionValue);
      // ----------------------------------------------------------------------
      // 4º) Check attribute PROPERTY
      // ----------------------------------------------------------------------
    } else if (criterion.getProperty() != null && getElements().getProperty(criterion.getProperty()) != null) {
      defaultValues = getDefaultValuesAsString(getElements().getProperty(criterion.getProperty()));
      // ----------------------------------------------------------------------
      // 5º) Check attribute VALUE or VALUE list
      // ----------------------------------------------------------------------
      // Generic criteria
    } else if (criterion.getValue() != null) {
      defaultValues = getDefaultValuesAsString(criterion.getValue());
    }
    return defaultValues;
  }

  /**
   * Get default values as a list of cellData string values
   *
   * @param initialValue Default initial value
   * @return Default value list
   */
  private List<CellData> getDefaultValuesAsString(String initialValue) {
    // Initialize list
    List<CellData> defaultValues = new ArrayList<>();
    if (initialValue != null) {
      String[] valueList = initialValue.trim().split(AweConstants.COMMA_SEPARATOR);
      // Trim values and add them
      for (String value : valueList) {
        defaultValues.add(new CellData(value.trim()));
      }
    }

    // Retrieve default values
    return defaultValues;
  }
}
