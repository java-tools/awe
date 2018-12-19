package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Computed Class
 *
 * Used to parse the file Queries.xml with XStream
 *
 *
 * Computed field from queries. Generates a new field using other fields
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("computed")
public class Computed extends OutputField {

  private static final long serialVersionUID = 8404251573226537433L;

  // Field format (p.e. '[alias1] - [alias2]')
  @XStreamAlias("format")
  @XStreamAsAttribute
  private String format;

  // Field format label (p.e. 'FORMAT_LABEL_1 ->[alias1] - [alias2]')
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Field evaluation (Default is false)
  @XStreamAlias("eval")
  @XStreamAsAttribute
  private String eval;

  // Null value treating (Default is blank)
  @XStreamAlias("nullValue")
  @XStreamAsAttribute
  private String nullValue;

  /**
   * Default constructor
   */
  public Computed() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Computed(Computed other) {
    super(other);
    this.format = other.format;
    this.label = other.label;
    this.eval = other.eval;
    this.nullValue = other.nullValue;
  }

  @Override
  public Computed copy() throws AWException {
    return new Computed(this);
  }

  /**
   * Returns the field format (p.e. '[alias1] - [alias2]')
   *
   * @return Field format
   */
  public String getFormat() {
    return format;
  }

  /**
   * Stores the field format (p.e. '[alias1] - [alias2]')
   *
   * @param format Field format
   */
  public void setFormat(String format) {
    this.format = format;
  }

  /**
   * Returns the field evaluation
   *
   * @return Field evaluation type
   */
  public boolean isEval() {
    return "true".equalsIgnoreCase(eval);
  }

  /**
   * Returns the field evaluation
   *
   * @return Field evaluation type
   */
  public String getEval() {
    return eval;
  }

  /**
   * Stores the field evaluation type
   *
   * @param eval Field evaluation type
   */
  public void setEval(String eval) {
    this.eval = eval;
  }

  /**
   * Returns the null value treatment
   *
   * @return Null value treatment
   */
  public String getNullValue() {
    return nullValue;
  }

  /**
   * Stores the null value treatment
   *
   * @param nullValue Null value treatment
   */
  public void setNullValue(String nullValue) {
    this.nullValue = nullValue;
  }

  /**
   * Retrieve i18n label format
   *
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Store i18n label format
   *
   * @param label the label to set
   */
  public void setLabel(String label) {
    this.label = label;
  }
}
