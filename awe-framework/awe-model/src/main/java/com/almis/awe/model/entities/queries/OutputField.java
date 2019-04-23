/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

/*
 * File Imports
 */

import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;

/**
 * OutputField Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 *
 *
 * Superclass of Field and Computed class
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamInclude({Field.class, Computed.class, Compound.class, Concat.class})
public abstract class OutputField extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -5029397784962648558L;

  // Field alias
  @XStreamAlias("alias")
  @XStreamAsAttribute
  private String alias = null;

  // Field is not printable
  @XStreamAlias("noprint")
  @XStreamAsAttribute
  private String noprint = null;

  // Output transformation (after formatting)
  @XStreamAlias("transform")
  @XStreamAsAttribute
  private String transform = null;

  // Pattern to apply to the field
  @XStreamAlias("pattern")
  @XStreamAsAttribute
  private String pattern = null;

  // Translate to apply to the field
  @XStreamAlias("translate")
  @XStreamAsAttribute
  private String translate = null;

  // Format for transform generic dates
  @XStreamAlias("format-from")
  @XStreamAsAttribute
  private String formatFrom = null;

  @XStreamAlias("format-to")
  @XStreamAsAttribute
  private String formatTo = null;

  /**
   * Default constructor
   */
  public OutputField() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public OutputField(OutputField other) {
    super(other);
    this.alias = other.alias;
    this.noprint = other.noprint;
    this.transform = other.transform;
    this.pattern = other.pattern;
    this.translate = other.translate;
    this.formatFrom = other.formatFrom;
    this.formatTo = other.formatTo;
  }

  /**
   * Returns the field alias
   *
   * @return Field alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Stores the field alias
   *
   * @param alias Field alias
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * Returns if the field is not printable
   *
   * @return Field is not printable
   */
  public boolean isNoprint() {
    return "true".equalsIgnoreCase(getNoprint());
  }

  /**
   * Returns if the field is not printable
   *
   * @return Field is not printable
   */
  public String getNoprint() {
    return noprint;
  }

  /**
   * Stores if the field is not printable
   *
   * @param noprint Field is not printable
   */
  public void setNoprint(String noprint) {
    this.noprint = noprint;
  }

  /**
   * Returns the field transformation method (DATE, TIME, FORMATTED_NUMBER)
   *
   * @return Field transformation method
   */
  public String getTransform() {
    return transform;
  }

  /**
   * Stores the field transformation method (DATE, TIME, TIMESTAMP, FORMATTED_NUMBER)
   *
   * @param transform Field transformation method
   */
  public void setTransform(String transform) {
    this.transform = transform;
  }

  /**
   * Returns the translate enumerated name for the field (Translates a value into a text)
   *
   * @return EnumeratedGroup Identifier
   */
  public boolean isTransform() {
    return this.transform != null;
  }

  /**
   * Returns the pattern to apply to the transformated field (###.###,##)
   *
   * @return Pattern to apply to the transformated field
   */
  public String getPattern() {
    return pattern;
  }

  /**
   * Stores the pattern to apply to the transformated field (###.###,##)
   *
   * @param pattern Pattern to apply to the transformated field
   */
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  /**
   * Returns the translate enumerated name for the field (Translates a value into a text)
   *
   * @return EnumeratedGroup Identifier
   */
  public String getTranslate() {
    return translate;
  }

  /**
   * Returns the translate enumerated name for the field (Translates a value into a text)
   *
   * @return EnumeratedGroup Identifier
   */
  public boolean isTranslate() {
    return this.translate != null;
  }

  /**
   * Stores the translate enumerated name for the field (Translates a value into a text)
   *
   * @param translate EnumeratedGroup Identifier
   */
  public void setTranslate(String translate) {
    this.translate = translate;
  }

  /**
   * Get the format of a date to transform from
   *
   * @return
   */
  public String getFormatFrom() {
    return formatFrom;
  }

  /**
   * Set the format of a date to transform from
   *
   * @param formatFrom
   */
  public void setFormatFrom(String formatFrom) {
    this.formatFrom = formatFrom;
  }

  /**
   * Get the format to transform to
   *
   * @return
   */
  public String getFormatTo() {
    return formatTo;
  }

  /**
   * Set the format to transform to
   *
   * @param formatTo
   */
  public void setFormatTo(String formatTo) {
    this.formatTo = formatTo;
  }

}
