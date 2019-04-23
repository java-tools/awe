/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.regex.Pattern;

/*
 * File Imports
 */

/**
 * TagList Class
 *
 * Used to generate a tag list with XStream
 *
 *
 * @author Pablo GARCIA - 10/MAR/2014
 */
@XStreamAlias("tag-list")
public class TagList extends Component {

  private static final long serialVersionUID = 498908613159021940L;
  // PATTERNS
  @XStreamOmitField
  public static final Pattern wildcard = Pattern.compile("\\[([a-zA-Z_]*)\\]");

  /**
   * Default constructor
   */
  public TagList() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public TagList(TagList other) throws AWException {
    super(other);
  }

  @Override
  public TagList copy() throws AWException {
    return new TagList(this);
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
