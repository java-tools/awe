package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClientAction Class
 *
 * Used to parse the file Actions.xml with XStream
 * Action sent to the browser to be executed at client side
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("action")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientAction implements Copyable {

  private static final long serialVersionUID = -6543304075077162963L;

  // Client action type
  @XStreamAlias("type")
  private String type;

  // Async action
  @XStreamAlias("async")
  @XStreamAsAttribute
  private Boolean async;

  // Silent action
  @XStreamAlias("silent")
  @XStreamAsAttribute
  private Boolean silent;

  // Client action parameter list
  @JsonIgnore
  @XStreamAlias("parameters")
  private List<Parameter> parameterList;

  // Client action parameter list
  @JsonIgnore
  @XStreamOmitField
  private transient Map<String, Object> parameterMap;

  // Client action destination
  @XStreamAlias("destination")
  private String destination;

  // Target action destination
  @XStreamAlias("target")
  private String target;

  // Target action destination (as a full address)
  @XStreamAlias("address")
  private ComponentAddress address;

  // Client action context
  @XStreamOmitField
  private String context;

  /**
   * Client action constructor with type argument. Generates a client action
   *
   * @param type Client action type
   */
  public ClientAction(String type) {
    this.setType(type);
  }

  /**
   * Adds a parameter
   *
   * @param name  Parameter name
   * @param value Parameter value
   * @return this
   */
  public ClientAction addParameter(String name, Object value) {
    return addParameter(name, new CellData(value));
  }

  /**
   * Adds a parameter
   *
   * @param name  Parameter name
   * @param value Parameter value
   * @return this
   */
  public ClientAction addParameter(String name, CellData value) {
    if (parameterMap == null) {
      parameterMap = new HashMap<>();
    }
    parameterMap.put(name, value);
    return this;
  }

  /**
   * Retrieve parameters map
   * @return Parameters map
   */
  @JsonGetter("parameters")
  public Map<String, Object> getParameters() {
    if (parameterMap == null) {
      parameterMap = new HashMap<>();
    }
    return parameterMap;
  }

  @Override
  public ClientAction copy() throws AWException {
    return this.toBuilder()
      .parameterList(ListUtil.copyList(getParameterList()))
      .parameterMap(ListUtil.copyMap(getParameterMap(), Object.class))
      .build();
  }
}
