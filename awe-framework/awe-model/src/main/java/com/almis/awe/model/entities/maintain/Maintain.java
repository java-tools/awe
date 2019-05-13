package com.almis.awe.model.entities.maintain;

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
 * Maintain Class
 *
 * Used to parse the file Maintain.xml with XStream
 * Contains a list of maintain targets
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("maintain")
public class Maintain implements XMLFile {

  private static final long serialVersionUID = -1962157356340282704L;
  // Maintain Target List
  @XStreamImplicit
  private List<Target> maintainList;

  /**
   * Returns a a target from its identifier
   *
   * @param ide Target identifier
   * @return Selected Target
   */
  public Target getTarget(String ide) {
    for (Target target: getBaseElementList()) {
      if (ide.equals(target.getName())) {
        return target;
      }
    }
    return null;
  }

  @Override
  public List<Target> getBaseElementList() {
    return maintainList == null ? new ArrayList<>() : maintainList;
  }
}
