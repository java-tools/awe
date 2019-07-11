package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog Class
 *
 * Used to parse a dialog tag with XStream
 * Generates a dialog structure with header, center and footing
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("dialog")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dialog extends Component {

  private static final long serialVersionUID = 1589720259893098192L;
  // Load all the data initially or not
  @XStreamAlias("on-close")
  @XStreamAsAttribute
  private String onClose;

  @Override
  public Dialog copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * @return the onClose
   */
  @JsonGetter("acceptOnClose")
  public Boolean acceptOnClose() {
    return !"reject".equalsIgnoreCase(onClose);
  }

  /**
   * @return the onClose
   */
  @JsonGetter("accept")
  public Boolean acceptConverter() {
    return this.acceptOnClose();
  }

  /**
   * Retrieve component tag
   *
   * @return Component tag
   */
  @Override
  public String getComponentTag() {
    return "dialog";
  }

  /**
   * Returns the children element list of a desired TYPE
   * @param processDialog    flag to check dialog elements
   * @param elementClassList element class
   * @param <T> element type
   * @return Children List
   */
  @Override
  public <T> List<T> getElementsByType(Boolean processDialog, Class<T>... elementClassList) {
    if (processDialog) {
      return super.getElementsByType(processDialog, elementClassList);
    } else {
      return new ArrayList<>();
    }
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_DIALOG;
  }

  /**
   * Get print element list (to be overwritten)
   *
   * @param printElementList Print element list
   * @param label            Last label
   * @param parameters       Parameters
   * @param dataSuffix       Data suffix
   * @return Print bean
   */
  @JsonIgnore
  @Override
  public List<Element> getReportStructure(List<Element> printElementList, String label, ObjectNode parameters, String dataSuffix) {
    return printElementList;
  }
}
