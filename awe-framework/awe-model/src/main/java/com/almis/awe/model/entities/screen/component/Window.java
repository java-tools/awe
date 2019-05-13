package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Window Class
 *
 * Used to parse a window tag with XStream
 *
 *
 * Generates a window structure with header center and footing
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("window")
@JsonIgnoreProperties({"id"})
public class Window extends Component {

  private static final long serialVersionUID = 5159433149044786985L;
  // Window can be maximized or not
  @XStreamAlias("maximize")
  @XStreamAsAttribute
  private Boolean maximize;

  @Override
  public Window copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Returns is maximizable
   * @return Is maximizable
   */
  public boolean isMaximize() {
    return maximize != null && maximize;
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return component tag
   */
  @Override
  public String getComponentTag() {
    return "window";
  }

  /**
   * Returns if window allows to maximize/restore itself
   *
   * @return Window allows to maximize/restore itself
   */
  public boolean allowMaximize() {
    return isMaximize() && getLabel() != null;
  }

  /**
   * Returns if window allows to maximize/restore itself for JSON serialization
   *
   * @return Window allows to maximize/restore itself
   */
  @JsonGetter("maximize")
  public boolean getMaximizeConverter() {
    return allowMaximize();
  }

  /**
   * Get print element list (to be overwritten)
   *
   * @param printElementList Print element list
   * @param label            Previous label
   * @param parameters       Parameters
   * @param dataSuffix       Data suffix
   * @return Print bean
   */
  @JsonIgnore
  @Override
  public List<Element> getReportStructure(List<Element> printElementList, String label, ObjectNode parameters, String dataSuffix) {
    return super.getReportStructure(printElementList, getLabel() == null ? label : getLabel(), parameters, dataSuffix);
  }
}
