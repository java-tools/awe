package com.almis.awe.model.entities.screen.component.criteria;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 * InfoCriteria Class
 *
 * Used to add an info with criteria element with XStream
 *
 *
 * @author Pablo GARCIA - 04/JUN/2012
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("info-criteria")
public class InfoCriteria extends AbstractCriteria {

  private static final long serialVersionUID = 6550382841315255907L;
  // Button Type (button, submit, reset)
  @JsonIgnore
  @XStreamAlias("info-style")
  @XStreamAsAttribute
  private String infoStyle;

  @Override
  public InfoCriteria copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  @Override
  @JsonIgnore
  public ST generateTemplate(STGroup group) {
    ST template = group.createStringTemplate(group.rawGetTemplate(AweConstants.TEMPLATE_INFO));
    ST children = super.generateTemplate(group);

    // Generate template
    template.add("e", this).add("children", children);

    // Retrieve code
    return template;
  }
}
