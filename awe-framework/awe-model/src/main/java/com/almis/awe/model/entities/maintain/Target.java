package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLNode;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Target Class
 * Used to parse the file Maintain.xml with XStream
 * Contains a list of maintain queries and allows to launch them all
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("target")
public class Target implements XMLNode, Copyable {

  private static final long serialVersionUID = 2101818729705484693L;

  // Target Name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Target Label (message title)
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Target Exclusive (target is exclusive)
  @XStreamAlias("exclusive")
  @XStreamAsAttribute
  private String exclusive;

  // Query can be launched out of session
  @XStreamAlias("public")
  @XStreamAsAttribute
  private Boolean isPublic;

  // Target Query List
  @XStreamImplicit
  private List<MaintainQuery> queryList;

  // Target Service Data
  @XStreamOmitField
  private ServiceData result;

  /**
   * Returns if is list
   * @return Is list
   */
  public boolean isPublic() {
    return isPublic != null && isPublic;
  }

  @JsonIgnore
  @Override
  public String getElementKey() {
    return getName();
  }

  @Override
  public Target copy() throws AWException {
    return this.toBuilder()
      .queryList(ListUtil.copyList(getQueryList()))
      .build();
  }
}
