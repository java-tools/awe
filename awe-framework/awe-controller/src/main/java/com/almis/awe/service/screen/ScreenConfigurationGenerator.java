package com.almis.awe.service.screen;


import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.type.AttributeRestrictionType;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Manage the specific configuration of a screen
 */
public class ScreenConfigurationGenerator extends ServiceConfig {

  /**
   * Store screen target data in components
   * @param configurationTask Screen configuration future
   * @param screen Screen bean
   * @throws AWException
   */
  void applyScreenConfiguration(Future<ServiceData> configurationTask, Screen screen) throws AWException {
    // Retrieve screen configuration if defined
    try {
      ServiceData screenConfigurationOutput = configurationTask.get();
      DataList screenConfiguration = (DataList) screenConfigurationOutput.getVariableMap().get(AweConstants.ACTION_DATA).getObjectValue();

      // For each column, store value in components
      addScreenConfigurationToComponents(screenConfiguration, screen);
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_SCREEN_GENERATION_ERROR"), getLocale("ERROR_MESSAGE_SCREEN_CONFIGURATION_DATA", screen.getId()), exc);
    }
  }

  /**
   * Add screen configuration to components
   * @param data Screen configuration data
   */
  private void addScreenConfigurationToComponents(DataList data, Screen screen) {
    // For each column, store value in components
    for (Map<String, CellData> rule : data.getRows()) {
      String componentId = rule.get("component").getStringValue();
      String attribute = rule.get("attribute").getStringValue();
      String value = rule.get("value").getStringValue();
      List elements = screen.getElementsById(componentId);
      if (!elements.isEmpty()) {
        applyRule(attribute, value, (Component) elements.get(0));
      }
    }
  }

  /**
   * Apply rule to component
   * @param attribute Attribute to change
   * @param value Value to set
   * @param component Component to change
   * @return Reinitialize component initial load or not
   */
  private void applyRule(String attribute, String value, Component component) {
    // Get accessor
    PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(component);

    // Set property value
    myAccessor.setPropertyValue(attribute, value);

    // Manage special cases
    switch (AttributeRestrictionType.getEnum(attribute)) {
      // Visible / Hidden in columns
      case VISIBLE:
        if (component instanceof Column) {
          myAccessor.setPropertyValue(AttributeRestrictionType.HIDDEN.toString(), String.valueOf(!Boolean.parseBoolean(value)));
        }
        break;
      default:
    }
  }
}
