/*
 * Package definition
 */
package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Collections;
import java.util.List;

/*
 * File Imports
 */

/**
 * Actions Class
 *
 * Used to parse the Actions.xml file with XStream
 * This class is used to parse the action list
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("actions")
public class Actions extends XMLWrapper {

  private static final long serialVersionUID = -6519438900878392915L;
  // Action list
  @XStreamImplicit(itemFieldName = "action")
  private List<Action> actionList;

  /**
   * Default constructor
   */
  public Actions() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Actions(Actions other) throws AWException {
    super(other);
    this.actionList = ListUtil.copyList(other.actionList);
  }

  /**
   * Returns the action list
   *
   * @return Action list
   */
  public List<Action> getActionList() {
    return actionList;
  }

  /**
   * Stores the action list
   *
   * @param actions Action list
   */
  public void setActionList(List<Action> actions) {
    this.actionList = actions;
  }

  /**
   * Returns an action
   *
   * @param ide Action identifier
   * @return Selected action
   */
  public Action getAction(String ide) {
    // Variable definition
    for (Action action : this.getBaseElementList()) {
      if (ide.equals(action.getId())) {
        return action;
      }
    }
    return null;
  }

  @Override
  public List<Action> getBaseElementList() {
    return actionList == null ? Collections.emptyList() : actionList;
  }
}
