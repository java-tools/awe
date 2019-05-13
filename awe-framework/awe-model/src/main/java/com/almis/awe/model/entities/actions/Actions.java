package com.almis.awe.model.entities.actions;

import com.almis.awe.model.entities.XMLFile;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Actions Class
 *
 * Used to parse the Actions.xml file with XStream
 * This class is used to parse the action list
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("actions")
public class Actions implements XMLFile {

  private static final long serialVersionUID = -6519438900878392915L;
  // Action list
  @XStreamImplicit(itemFieldName = "action")
  private List<Action> actionList;

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
    return actionList == null ? new ArrayList<>() : actionList;
  }
}
