package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Parameter Class
 * Used to parse the file Actions.xml with XStream
 * Client action parameter
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("parameter")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Parameter implements Copyable {

  private static final long serialVersionUID = -2633008018819827229L;

  // Parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Parameter value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Parameter label
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Parameter variable
  @XStreamAlias("variable")
  @XStreamAsAttribute
  private String variable;

  // Parameter parameter
  @XStreamAlias("parameter")
  @XStreamAsAttribute
  private String requestParameter;

  // Parameter value
  @XStreamOmitField
  private CellData cellData;

  /**
   * Constructor
   *
   * @param name     parameter name
   * @param cellData parameter data
   */
  public Parameter(String name, CellData cellData) {
    this.name = name;
    this.cellData = cellData;
  }

  @Override
  public Parameter copy() throws AWException {
    return this.toBuilder()
      .cellData(ListUtil.copyElement(getCellData()))
      .build();
  }
}