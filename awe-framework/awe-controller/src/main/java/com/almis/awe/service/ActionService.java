package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.Action;
import com.almis.awe.model.entities.actions.Answer;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.actions.Parameter;
import com.almis.awe.model.type.AnswerType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * Manage application action calls
 */
public class ActionService extends ServiceConfig {

  // Autowired services
  private final LauncherService launcherService;

  // Password parameter
  @Value("${screen.parameter.password:pwd_usr}")
  private String passwordParameter;

  private static final String SECURITY_MASK = "*****";

  /**
   * Autowired constructor
   *
   * @param launcherService Launcher service
   */
  public ActionService(LauncherService launcherService) {
    this.launcherService = launcherService;
  }

  /**
   * Launch action (without target)
   *
   * @param actionId Action identifier
   * @return Action
   * @throws AWException Action has not been found
   */
  public Action getAction(String actionId) throws AWException {
    return getElements().getAction(actionId).copy();
  }

  /**
   * Launch the action
   *
   * @param actionId Action identifier
   * @return Client action list
   */
  public List<ClientAction> launchAction(String actionId) {
    List<ClientAction> actionList = null;

    // Retrieve action
    Action action = null;
    try {
      action = getAction(actionId);
    } catch (AWException exc) {
      actionList = launchDefaultError(exc);
    }

    // Launch action if retrieved
    if (action != null) {
      try {
        actionList = launchAction(action);
      } catch (AWException exc) {
        actionList = launchError(action, exc);
      }
    }

    return actionList;
  }

  /**
   * Launch action (without target)
   *
   * @param action Action to be launched
   * @return Client action list
   * @throws AWException Action error
   */
  public List<ClientAction> launchAction(Action action) throws AWException {
    // Get service
    ServiceData serviceData;

    // Get service
    String serviceId = action.getCall().getService();

    // Launch service
    serviceData = launcherService.callService(serviceId, null);

    // Get answer and set it into output actions
    Answer answer = (serviceData == null) ? action.getAnswer(AnswerType.OK) : action.getAnswer(serviceData.getType());
    List<ClientAction> actionList;
    if (answer != null) {
      actionList = answer.getResponseList();

      if (serviceData != null) {
        // Replace variable in actionList with serviceData variables
        Map<String, CellData> variableMap = serviceData.getVariableMap();
        actionList = getClientActionVariables(actionList, variableMap);

        // Add extra client actions
        actionList.addAll(serviceData.getClientActionList());
      }
    } else {
      actionList = new ArrayList<>();
    }

    // Set output actions
    return actionList;
  }

  /**
   * Launch an error (action failed)
   *
   * @param action    Action launched
   * @param exception Exception with the error
   * @return Client action list
   */
  public List<ClientAction> launchError(Action action, AWException exception) {
    try {
      // Get answer and set it into output actions
      Answer answer = action.getAnswer(exception.getType());
      List<ClientAction> actionList = answer.getResponseList();
      actionList = getClientActionVariables(actionList, getExceptionVariables(exception));

      // Set output actions
      return actionList;
    } catch (AWException exc) {
      exception = exc;
      return new ArrayList<>();
    } finally {
      // Log current variables
      getLogger().log(ActionService.class, Level.ERROR, "Call parameters: {0}", getParameterListAsString());

      // Log exception error
      exception.log();
    }
  }

  /**
   * Launch default error action
   *
   * @param exception Exception with the error description
   * @return Client action list
   */
  public List<ClientAction> launchDefaultError(AWException exception) {
    List<ClientAction> actionList = null;
    // Get default error action
    try {
      actionList = launchError(getAction("DEFAULT_ERROR"), exception);
    } catch (AWException exc) {
      // No encontrado el error por defecto. Error fatal!
      getLogger().log(ActionService.class, Level.FATAL, "Default error not found", exc);
    }
    return actionList;
  }

  /**
   * Fill client action variables
   *
   * @param actionList  Action list
   * @param variableMap Variable map
   */
  private List<ClientAction> getClientActionVariables(List<ClientAction> actionList, Map<String, CellData> variableMap) throws AWException {
    List<ClientAction> filledActionList = new ArrayList<>();

    // Replace variable in actionList with serviceData variables
    for (ClientAction response : actionList) {
      // Get new response
      ClientAction clientAction = response.copy();

      // Fill new response with variables and parameters
      if (clientAction.getParameterList() != null) {
        for (Parameter parameter : clientAction.getParameterList()) {
          // Store parameter value
          clientAction.addParameter(parameter.getName(), getParameterValue(parameter, variableMap));
        }
      }

      // Add the newly created action
      filledActionList.add(clientAction);
    }
    // Retrieve upgraded action list
    return filledActionList;
  }

  /**
   * Get parameter value
   *
   * @param parameter   Parameter
   * @param variableMap Variable map
   * @return Parameter value
   */
  private Object getParameterValue(Parameter parameter, Map<String, CellData> variableMap) {
    Object value;
    if (parameter.getLabel() != null) {
      // Label as parameter
      value = getLocale(parameter.getLabel());
    } else if (parameter.getVariable() != null) {
      // Retrieve variable parameter
      value = variableMap.get(parameter.getVariable());
    } else if (parameter.getRequestParameter() != null) {
      // Retrieve input parameter
      value = getRequest().getParameter(parameter.getRequestParameter());
    } else if (parameter.getCellData() != null) {
      // Get static object value
      value = parameter.getCellData();
    } else {
      // Get static string value
      value = parameter.getValue();
    }

    return value;
  }

  /**
   * Retrieves filtered parameter list
   *
   * @return Parameter list filtered
   */
  private String getParameterListAsString() {

    // Variable definition
    StringBuilder builder = new StringBuilder();

    // Retrieve parameter list
    try {
      ObjectNode parameterList = getRequest().getParameterList();
      String password = getRequest().getParameterAsString(passwordParameter);

      // Remove nonsense values from parameter list
      Iterator<String> fieldNames = parameterList.fieldNames();
      while (fieldNames.hasNext()) {
        String key = fieldNames.next();
        String value = getRequest().getParameterAsString(key);
        buildParameterString(builder, key, value, password);
      }
    } catch (Exception exc) {
      getLogger().log(ActionService.class, Level.ERROR, "Error filtering parameter list", exc);
    }

    // Return parameter list string
    return builder.toString();
  }

  /**
   * Build a parameter as string
   *
   * @param builder  String builder
   * @param key      Parameter key
   * @param value    Parameter value
   * @param password Password value
   */
  private void buildParameterString(StringBuilder builder, String key, String value, String password) {
    if (key.endsWith("_text") || key.startsWith("OUTPUT_")) {
      // Do nothing
    } else {
      String replacement = value;
      if (key.equalsIgnoreCase(passwordParameter)) {
        replacement = SECURITY_MASK;
      } else if (password != null && value != null && value.contains(password)) {
        // Store parameter
        replacement = value.replaceAll(password, SECURITY_MASK);
      }
      // Store parameter
      builder.append(builder.length() == 0 ? "" : ", ");
      builder.append(key).append("=").append(replacement);
    }
  }

  /**
   * Retrieve the exception variables
   *
   * @param exc Exception
   * @return variableMap
   */
  public Map<String, CellData> getExceptionVariables(AWException exc) {
    Map<String, CellData> variableMap = new HashMap<>();
    variableMap.put(AweConstants.ACTION_MESSAGE_TYPE, new CellData(exc.getType().toString()));
    variableMap.put(AweConstants.ACTION_MESSAGE_TITLE, new CellData(exc.getTitle()));
    variableMap.put(AweConstants.ACTION_MESSAGE_DESCRIPTION, new CellData(exc.getMessage()));
    return variableMap;
  }
}
