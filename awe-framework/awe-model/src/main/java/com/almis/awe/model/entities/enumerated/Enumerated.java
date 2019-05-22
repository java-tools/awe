package com.almis.awe.model.entities.enumerated;

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
 * Enumerated Class
 *
 * Used to parse the file Enumerated.xml with XStream
 * Generates an enumerated group list
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("enumerated")
public class Enumerated implements XMLFile {

  private static final long serialVersionUID = -5779427444052366131L;

  // Enumerated group list
  @XStreamImplicit
  private List<EnumeratedGroup> groupList;

  /**
   * Returns an enumerated group from identifier
   *
   * @param ide Enumerated group identifier
   * @return Selected enumerated group
   */
  public EnumeratedGroup getGroup(String ide) {
    // Check group existence
    for (EnumeratedGroup group: getBaseElementList()) {
      if (ide.equals(group.getId())) {
        return group;
      }
    }
    return null;
  }

  @Override
  public List<EnumeratedGroup> getBaseElementList() {
    return groupList == null ? new ArrayList<>() : groupList;
  }
}
