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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Manage the specific configuration of a screen
 */
@Log4j2
public class ScreenConfigurationGenerator extends ServiceConfig {

  /**
   * Store screen target data in components
   * @param configurationTask Screen configuration future
   * @param screen Screen bean
   * @throws AWException Awe exception
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
   */
  private void applyRule(String attribute, String value, Component component) {
    // Get restriction
    AttributeRestrictionType attributeRestriction = AttributeRestrictionType.getEnum(attribute);

    // If property doesn't belong to the component, log it and return
    if (!attributeRestriction.getComponentClass().isAssignableFrom(component.getClass())) {
      log.warn("WARNING: Screen configuration parameter '{}' is not a property from '{}' component. Please remove it from screen configuration", attribute, component.getClass().getSimpleName());
      return;
    }

    // Get accessor
    PropertyAccessor myAccessor = PropertyAccessorFactory.forDirectFieldAccess(component);

    // Manage special cases
    switch (attributeRestriction) {
      // Visible / Hidden in columns
      case VISIBLE:
        if (component instanceof Column) {
          myAccessor.setPropertyValue(AttributeRestrictionType.HIDDEN.toString(), String.valueOf(!Boolean.parseBoolean(value)));
        } else {
          myAccessor.setPropertyValue(attribute, value);
        }
        break;
      case REQUIRED:
        myAccessor.setPropertyValue(AttributeRestrictionType.VALIDATION.toString(), String.format("{required:%b}", Boolean.parseBoolean(value)));
        break;
      default:
        // Set property value
        myAccessor.setPropertyValue(attribute, value);
        break;
    }
  }
}
