package com.almis.awe.model.entities.screen.component.criteria;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.screen.component.button.AbstractButton;
import com.almis.awe.model.entities.screen.component.grid.AbstractGrid;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.entities.screen.component.panelable.Panelable;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Abstract Criteria Class
 * Screen criteria
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
@XStreamInclude({Criteria.class, Panelable.class, AbstractGrid.class, Column.class, AbstractButton.class, InfoCriteria.class})
public abstract class AbstractCriteria extends Component {

  private static final long serialVersionUID = -8937301673779113798L;

  // Static VALUE
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Session VARIABLE
  @JsonIgnore
  @XStreamAlias("session")
  @XStreamAsAttribute
  private String session;

  // Variable
  @JsonIgnore
  @XStreamAlias("variable")
  @XStreamAsAttribute
  private String variable;

  // Property
  @JsonIgnore
  @XStreamAlias("property")
  @XStreamAsAttribute
  private String property;

  // Validation Format
  @XStreamAlias("validation")
  @XStreamAsAttribute
  private String validation;

  // Number Format
  @XStreamAlias("number-format")
  @XStreamAsAttribute
  private String numberFormat;

  // Show slider flag for numeric criteria
  @XStreamAlias("show-slider")
  @XStreamAsAttribute
  private Boolean showSlider;

  // Capitalize data in criteria
  @XStreamAlias("capitalize")
  @XStreamAsAttribute
  private Boolean capitalize;

  // Criteria is read-only
  @XStreamAlias("readonly")
  @XStreamAsAttribute
  private Boolean readonly;
  // Criteria is CHECKED
  @XStreamAlias("checked")
  @XStreamAsAttribute
  private Boolean checked;

  // Criteria is an STRICT VALUE
  @XStreamAlias("strict")
  @XStreamAsAttribute
  private Boolean strict;

  // Validation MESSAGE
  @XStreamAlias("message")
  @XStreamAsAttribute
  private String message;

  // Criteria Formule
  @XStreamAlias("formule")
  @XStreamAsAttribute
  private String formule;

  // Criteria is OPTIONAL
  @XStreamAlias("optional")
  @XStreamAsAttribute
  private Boolean optional;

  // Criteria is PRINTABLE
  @XStreamAlias("printable")
  @XStreamAsAttribute
  private String printable;

  // Criteria UNIT LABEL
  @XStreamAlias("unit")
  @XStreamAsAttribute
  private String unit;

  // Criteria will be CHECKED for empty values
  @XStreamAlias("check-empty")
  @XStreamAsAttribute
  private Boolean checkEmpty;

  // Suggest TYPE criteria initial query check
  @XStreamAlias("check-initial")
  @XStreamAsAttribute
  private Boolean checkInitial;

  // Suggest TYPE criteria initial query target
  @XStreamAlias("check-target")
  @XStreamAsAttribute
  private String checkTarget;

  // Criteria will be CHECKED for empty values
  @XStreamAlias("specific")
  @XStreamAsAttribute
  private String specific;

  // Criteria search TIMEOUT
  @XStreamAlias("timeout")
  @XStreamAsAttribute
  private Integer timeout;

  // Group name
  @XStreamAlias("group")
  @XStreamAsAttribute
  private String group;

  // Input PLACEHOLDER
  @XStreamAlias("placeholder")
  @XStreamAsAttribute
  private String placeholder;

  // Text area rows
  @XStreamAlias("area-rows")
  @XStreamAsAttribute
  private Integer areaRows;

  // Destination VALUE
  @XStreamAlias("destination")
  @XStreamAsAttribute
  private String destination;

  // Show weekends in Date criteria
  @XStreamAlias("show-weekends")
  @XStreamAsAttribute
  private Boolean showWeekends;

  // Show future dates in Date criteria
  @XStreamAlias("show-future-dates")
  @XStreamAsAttribute
  private Boolean showFutureDates;

  // Format of a date
  @XStreamAlias("date-format")
  @XStreamAsAttribute
  private String dateFormat;

  // Show today button in date criteria
  @XStreamAlias("date-show-today-button")
  @XStreamAsAttribute
  private Boolean showTodayButton;

  // Show days, months or years in date criteria
  @XStreamAlias("date-view-mode")
  @XStreamAsAttribute
  private String dateViewMode;

  // Put label at left and give it a size
  @XStreamAlias("left-label")
  @XStreamAsAttribute
  private Integer leftLabel;

  // Parameter VALUE
  @JsonIgnore
  @XStreamOmitField
  private String parameterValue;

  // Parameter VALUE
  @JsonIgnore
  @XStreamOmitField
  private transient List<String> parameterValueList;

  // Default VALUE
  @JsonIgnore
  @XStreamOmitField
  private String defaultValue;

  // Restricted VALUES from screen configuration
  @JsonIgnore
  @XStreamOmitField
  private String restrictedValueList;

  /**
   * Returns if criteria is checked
   * @return criteria is checked
   */
  @JsonGetter("checked")
  public boolean isChecked() {
    return getChecked() != null && getChecked();
  }

  /**
   * Returns if criteria is showing a slider
   * @return criteria show slider
   */
  public boolean isShowSlider() {
    return getShowSlider() != null && getShowSlider();
  }

  /**
   * Returns if criteria is readonly
   * @return criteria is readonly
   */
  @JsonGetter("readonly")
  public boolean isReadonly() {
    return getReadonly() != null && getReadonly();
  }

  /**
   * Returns if criteria is capitalize
   * @return criteria is capitalize
   */
  public boolean isCapitalize() {
    return getCapitalize() != null && getCapitalize();
  }

  /**
   * Returns if criteria is capitalize
   * @return criteria is capitalize
   */
  @JsonGetter("strict")
  public boolean isStrict() {
    return getStrict() == null || getStrict();
  }

  /**
   * Returns if criteria is optional
   * @return criteria is optional
   */
  @JsonGetter("optional")
  public boolean isOptional() {
    return getOptional() != null && getOptional();
  }

  /**
   * Returns if criteria is checkEmpty
   * @return criteria is checkEmpty
   */
  @JsonGetter("checkEmpty")
  public boolean isCheckEmpty() {
    return getCheckEmpty() != null && getCheckEmpty();
  }

  /**
   * Returns if criteria is checkInitial
   * @return criteria is checkInitial
   */
  @JsonGetter("checkInitial")
  public boolean isCheckInitial() {
    return getCheckInitial() == null || getCheckInitial();
  }

  /**
   * Returns if criteria is showWeekends
   * @return criteria is showWeekends
   */
  public boolean isShowWeekends() {
    return getShowWeekends() != null && getShowWeekends();
  }

  /**
   * Returns if criteria is showFutureDates
   * @return criteria is showFutureDates
   */
  public boolean isShowFutureDates() {
    return getShowFutureDates() != null && getShowFutureDates();
  }

  /**
   * Returns if criteria is showTodayButton
   * @return criteria is showTodayButton
   */
  public boolean isShowTodayButton() {
    return getShowTodayButton() != null && getShowTodayButton();
  }

  /**
   * Retrieve component tag
   *
   * @return Component tag
   */
  @Override
  public String getComponentTag() {
    return "input-" + getComponentType();
  }

  /**
   * If validation is set to required, returns true
   *
   * @return required required value
   */
  @JsonGetter("required")
  public boolean isRequiredConverter() {
    return getValidation() != null && getValidation().contains("required");
  }

  /**
   * Returns the element's TYPE
   *
   * @return Element TYPE
   */
  @JsonGetter("inputType")
  @Override
  public String getType() {
    return super.getType();
  }

  /**
   * Get elementGroup
   *
   * @return key
   */
  @JsonIgnore
  public String getElementGroup() {
    return getGroup();
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    if (AweConstants.HIDDEN_COMPONENT.equalsIgnoreCase(getComponentType())) {
      return AweConstants.TEMPLATE_HELP_EMPTY;
    } else {
      return AweConstants.TEMPLATE_HELP_CRITERION;
    }
  }

  /**
   * Check if component is printable
   * @return Component is printable
   */
  @JsonGetter("printable")
  public boolean isPrintable() {
    return getPrintable() == null || !"false".equalsIgnoreCase(getPrintable());
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
    if (getComponentType() != null && isPrintable() && !"hidden".equalsIgnoreCase(getComponentType())) {
      printElementList.add(this);
    }
    return printElementList;
  }
}
