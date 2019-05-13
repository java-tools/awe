package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.regex.Pattern;

/**
 * TagList Class
 *
 * Used to generate a tag list with XStream
 *
 *
 * @author Pablo GARCIA - 10/MAR/2014
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("tag-list")
public class TagList extends Component {

  private static final long serialVersionUID = 498908613159021940L;
  // PATTERNS
  @XStreamOmitField
  public static final Pattern wildcard = Pattern.compile("\\[([a-zA-Z_]*)\\]");

  @Override
  public TagList copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Retrieve component tag
   *
   * @return Template
   */
  @Override
  public String getTemplate() {
    return AweConstants.TEMPLATE_TAGLIST;
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return "tag-list";
  }
}
