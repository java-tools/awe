package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWESessionException;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweClientTracker;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.enumerated.EnumeratedGroup;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.screen.Message;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.screen.data.ScreenComponent;
import com.almis.awe.model.entities.screen.data.ScreenData;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.service.screen.ScreenComponentGenerator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.almis.awe.model.constant.AweConstants.NO_KEY;

/**
 * Manage AWE screen access
 */
public class ScreenService extends ServiceConfig {

  // Autowired services
  private MenuService menuService;
  private MaintainService maintainService;
  private ScreenComponentGenerator screenComponentGenerator;

  /**
   * Autowired constructor
   * @param menuService Menu service
   * @param maintainService Maintain service
   * @param screenComponentGenerator Screen component generator
   */
  @Autowired
  public ScreenService(MenuService menuService, MaintainService maintainService, ScreenComponentGenerator screenComponentGenerator) {
    this.menuService = menuService;
    this.maintainService = maintainService;
    this.screenComponentGenerator = screenComponentGenerator;
  }

  /**
   * Generates an screen data
   *
   * @return Screen data
   */
  public ServiceData getScreenDataAction() {
    String optionId = this.getRequest().getParameterAsString(AweConstants.PARAMETER_OPTION);
    ScreenData screenData;
    try {
      screenData = generateScreenData(optionId);
    } catch (AWESessionException exc) {
      screenData = generateScreenDataError(exc);
      screenData.addError(exc);
      screenData.addAction(new ClientAction("logout"));
    } catch (AWException exc) {
      screenData = generateScreenDataError(exc);
    }

    // Store screen data
    ServiceData serviceData = new ServiceData();
    serviceData.addVariable(AweConstants.ACTION_SCREEN_DATA, new CellData(screenData));

    return serviceData;
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

  /**
   * Generates an screen data
   *
   * @param optionId Option identifier
   * @return Screen data
   * @throws AWException Screen data generation failed
   */
  public ScreenData generateScreenData(String optionId) throws AWException {
    // Check Screen Access
    Screen screen = null;

    if (optionId != null) {
      screen = menuService.getOptionScreen(optionId);
    } else {
      screen = menuService.getDefaultScreen();
    }

    // Store current screen
    storeCurrentScreen(optionId == null ? screen.getId() : optionId, screen);

    // Store last screen criteria
    storeLastScreenCriteria();

    // Track navigation
    AweClientTracker clientTracker = getBean(AweClientTracker.class);
    clientTracker.navigateToScreen(optionId);

    // Launch on load
    launchScreenOnLoadEvent(screen);

    // Generate map with model and controller data
    return getScreenData(screen, optionId);
  }

  /**
   * Generates screen data
   *
   * @param screen Screen object
   * @param optionId Option identifier
   * @return Screen data
   * @throws AWException Error retrieving menu
   */
  private ScreenData getScreenData(Screen screen, String optionId) throws AWException {
    ScreenData data = new ScreenData();
    Menu menu = menuService.getMenu();

    // If criteria are cached, add them to parameter map
    ObjectNode storedParameters = getScreenParameters(optionId);

    // Important! Move first the parameter list over the stored parameters to avoid losing view, option and screen parameters
    updateParameterList(storedParameters, screen);

    // Generate component map with initial load data
    Map<String, ScreenComponent> componentMap = screenComponentGenerator.generateComponentMap(screen, data, menu, storedParameters);

    // Add components to screen data
    addComponentsToScreenData(data, componentMap);

    // Add messages to screen data
    addMessagesToScreenData(screen, data);

    // Add screen information to screen data
    addScreenInformationToScreenData(screen, data, optionId);
    return data;
  }

  /**
   * Update parameter list with stored parameters
   * @param storedParameters Stored parameters
   */
  private void updateParameterList(ObjectNode storedParameters, Screen screen) {
    // Add screen name
    getRequest().setParameter(AweConstants.JSON_SCREEN, screen.getId());

    ObjectNode requestParameters = getRequest().getParameterList();
    List<String> baseAttributes = Arrays.asList(AweConstants.PARAMETER_VIEW,
            AweConstants.PARAMETER_OPTION, AweConstants.JSON_SCREEN);

    // Keep basic values in stored parameters
    for (String attribute : baseAttributes) {
      if (requestParameters.has(attribute)) {
        storedParameters.set(attribute, requestParameters.get(attribute));
      }
    }

    // Update request parameters
    getRequest().setParameterList(storedParameters);
  }

  /**
   * Add components to Screen Data
   * 
   * @param data Screen data
   * @param componentMap Component map
   */
  private void addComponentsToScreenData(ScreenData data, Map<String, ScreenComponent> componentMap) {
    // Store component list
    for (ScreenComponent component : componentMap.values()) {
      // Add component
      data.addComponent(component);
    }
  }

  /**
   * Add messages to Screen Data
   * 
   * @param screen Screen object
   * @param data Screen data
   */
  private void addMessagesToScreenData(Screen screen, ScreenData data) {
    // Generate messages
    List<Message> messages = screen.getElementsByType(Message.class);
    for (Message message : messages) {
      // Add controller
      data.addMessage(message.getElementKey(), message);
    }
  }

  /**
   * Add screen info to Screen Data
   * 
   * @param screen Screen object
   * @param data Screen data
   * @param optionId Option identifier
   */
  private void addScreenInformationToScreenData(Screen screen, ScreenData data, String optionId) {
    // Generate screen properties
    Map<String, String> screenProperties = new HashMap<>();
    if (screen.getLabel() != null) {
      screenProperties.put("title", screen.getLabel());
      // Add Name
      screenProperties.put("name", screen.getId());
      // Add Option
      screenProperties.put("option", optionId);
      // Add Unload
      if (screen.getOnUnload() != null) {
        screenProperties.put("onunload", screen.getOnUnload());
      }
    }
    data.setScreenProperties(screenProperties);
  }

  /**
   * Generates screen data
   *
   * @param exc AWException
   * @return data
   */
  private ScreenData generateScreenDataError(AWException exc) {
    ScreenData data = new ScreenData();

    // Log error
    getLogger().log(ScreenService.class, Level.ERROR, exc.getTitle(), exc.getMessage(), exc);

    // Generate screen properties
    Map<String, String> screenProperties = new HashMap<>();
    // Add title
    screenProperties.put("title", exc.getTitle());
    // Add Name
    screenProperties.put("name", AweConstants.ERROR_SCREEN);
    // Add option
    screenProperties.put("option", AweConstants.ERROR_OPTION);
    data.setScreenProperties(screenProperties);

    return data;
  }

  /**
   * Launches screen on load event
   *
   * @param screen Screen
   * @throws AWException Error launching on load event
   */
  private void launchScreenOnLoadEvent(Screen screen) throws AWException {
    // Launch on load maintain if defined
    if (screen.getOnLoad() != null) {
      maintainService.launchMaintain(screen.getOnLoad());
    }
  }

  /**
   * Stores the last screen criteria if keep last screen criteria is true
   */
  private void storeLastScreenCriteria() {
    String lastScreen = getSession().getParameter(String.class, AweConstants.SESSION_LAST_SCREEN);

    // Get last screen if stored
    if (lastScreen != null) {
      // Set parameters to get'em cached
      setScreenParameters(lastScreen);
    }
  }

  /**
   * Retrieve screen parameters and add them to cache
   * 
   * @param optionName Option name
   * @return Option parameters
   */
  private ObjectNode getScreenParameters(String optionName) {
    ObjectNode screenParameters;
    AweSession session = getSession();
    String keepCriteriaKey = AweConstants.SESSION_KEEP_CRITERIA_HEADER + optionName;
    if (optionName != null) {
      try {
        screenParameters = (ObjectNode) session.getParameter(keepCriteriaKey);
        screenParameters = screenParameters == null ? JsonNodeFactory.instance.objectNode() : screenParameters;
      } catch (Exception exc) {
        screenParameters = JsonNodeFactory.instance.objectNode();
      }
    } else {
      screenParameters = JsonNodeFactory.instance.objectNode();
    }

    // Log screen parameters
    getLogger().log(ScreenService.class, Level.INFO, "Screen parameters retrieved - Session: {0} - {1} - {2}", session.getSessionId(), keepCriteriaKey, screenParameters.toString());
    return screenParameters;
  }

  /**
   * Retrieve screen parameters and add them to cache
   * 
   * @param optionName Option name
   */
  private void setScreenParameters(String optionName) {
    // Log screen parameters
    String keepCriteriaKey = AweConstants.SESSION_KEEP_CRITERIA_HEADER + optionName;
    ObjectNode screenParameters = getRequest().getParametersSafe();
    getSession().setParameter(keepCriteriaKey, screenParameters);
    getLogger().log(ScreenService.class, Level.INFO, "Screen parameters set - Session: {0} - {1} - {2}", getSession().getSessionId(), keepCriteriaKey, screenParameters.toString());
  }

  /**
   * Stores into session the current screen name
   *
   * @param option option name
   */
  private void storeCurrentScreen(String option, Screen screen) {
    AweSession session = getSession();

    // Store last screen in session
    session.setParameter(AweConstants.SESSION_LAST_SCREEN, session.getParameter(AweConstants.SESSION_CURRENT_SCREEN));

    // Store option name in session
    session.setParameter(AweConstants.SESSION_CURRENT_SCREEN, screen.keepCriteria() ? option : null);
  }

  /**
   * Get screen component list names
   * 
   * @param screenId Screen identifier
   * @param suggest Written suggest
   * @return Screen component list
   * @throws AWException Error retrieving screen element list
   */
  public ServiceData getScreenElementList(@NotNull String screenId, String suggest) throws AWException {
    // Get screen from option
    Screen screen = menuService.getScreen(screenId);
    List<Component> componentList = screen.getElementsByType(Component.class);
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    for (Component component : componentList) {
      if (!NO_KEY.equalsIgnoreCase(component.getElementKey())) {
        Map<String, CellData> row = new HashMap<>();

        String componentLabel = component.getLabel() == null ? component.getElementKey() : getLocale(component.getLabel()) + " (" + component.getElementKey() + ")";

        // Add component if matches with component id or locale
        if (StringUtil.containsIgnoreCase(componentLabel, suggest.trim())) {
          // Set screen name
          row.put(AweConstants.JSON_VALUE_PARAMETER, new CellData(component.getElementKey()));

          // Store screen label
          row.put(AweConstants.JSON_LABEL_PARAMETER, new CellData(componentLabel));

          // Store row
          dataList.addRow(row);
        }
      }
    }

    // Set records
    dataList.setRecords(dataList.getRows().size());

    // Sort results
    DataListUtil.sort(dataList, AweConstants.JSON_LABEL_PARAMETER, "asc");

    // Set datalist to service
    serviceData.setDataList(dataList);
    return serviceData;
  }

  /**
   * Get screen attribute list names
   * 
   * @param suggest Written suggest
   * @return Screen component list
   * @throws AWException Error retrieving screen element list
   */
  public ServiceData getAttributeNameList(String suggest) throws AWException {
    // Get screen from option
    EnumeratedGroup attributeList = new EnumeratedGroup(getElements().getEnumerated(AweConstants.ATTRIBUTE_LIST));
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    for (Global option : attributeList.getOptionList()) {
      Map<String, CellData> row = new HashMap<>();

      String attributeLabel = getLocale(option.getLabel());

      // Add component if matches with component id or locale
      if (StringUtil.containsIgnoreCase(attributeLabel, suggest.trim())) {
        // Set screen name
        row.put(AweConstants.JSON_VALUE_PARAMETER, new CellData(option.getValue()));

        // Store screen label
        row.put(AweConstants.JSON_LABEL_PARAMETER, new CellData(attributeLabel));

        // Store row
        dataList.addRow(row);
      }
    }

    // Set records
    dataList.setRecords(dataList.getRows().size());

    // Sort results
    DataListUtil.sort(dataList, AweConstants.JSON_LABEL_PARAMETER, "asc");

    // Set datalist to service
    serviceData.setDataList(dataList);
    return serviceData;
  }
}