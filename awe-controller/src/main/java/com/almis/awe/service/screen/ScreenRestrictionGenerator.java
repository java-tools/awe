package com.almis.awe.service.screen;


import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;

import java.util.Map;

/**
 * Manage the specific configuration of a screen
 */
public class ScreenRestrictionGenerator extends ServiceConfig {

  /**
   * Store screen target data in components
   * @param screenRestriction Screen restriction data
   * @param menu Component map
   */
  public void applyScreenRestriction(DataList screenRestriction, Menu menu) {
    // For each column, store value in components
    for (Map<String, CellData> rule : screenRestriction.getRows()) {
      String optionName = rule.get("option").getStringValue();
      String restricted = rule.get("restricted").getStringValue();
      Option option = menu.getOptionByName(optionName);

      // Apply restriction
      if (option != null) {
        option.setRestricted(Boolean.valueOf(restricted));
      }
    }
  }

  /**
   * Store screen target data in components
   * @param module Current module
   * @param menu Component map
   */
  public void applyModuleRestriction(String module, Menu menu) {
    // For each column, store value in components
    for (Option option: menu.getElementsByType(Option.class)) {
      if (option.getModule() != null && module != null && !module.equalsIgnoreCase(option.getModule())) {
        option.setRestricted(true);
        deepRestriction(option);
      }
    }
  }

  /**
   * Apply deep restriction
   * @param currentOption Current option
   */
  private void deepRestriction(Option currentOption) {
    // For each column, store value in components
    for (Option option: currentOption.getElementsByType(Option.class)) {
      // Apply restriction
      option.setRestricted(true);
    }
  }
}
