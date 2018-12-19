package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.ArrayList;
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
@XStreamAlias("action")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientAction extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -6543304075077162963L;

  // Client action type
  @XStreamAlias("type")
  private String type = null;

  // Async action
  @XStreamAlias("async")
  @XStreamAsAttribute
  private String async = null;

  // Silent action
  @XStreamAlias("silent")
  @XStreamAsAttribute
  private String silent = null;

  // Client action parameter list
  @XStreamAlias("parameters")
  private List<Parameter> parameterList;

  // Client action parameter list
  @XStreamOmitField
  private transient Map<String, Object> parameterMap = null;

  // Client action destination
  @XStreamAlias("destination")
  private String destination;

  // Target action destination
  @XStreamAlias("target")
  private String target = null;

  // Target action destination (as a full address)
  @XStreamAlias("address")
  private ComponentAddress address = null;

  /**
   * Default constructor
   */
  public ClientAction() {
  }

  /**
   * Copy constructor
   *
   * @param other Action to copy
   */
  public ClientAction(ClientAction other) throws AWException {
    super(other);
    this.type = other.type;
    this.async = other.async;
    this.silent = other.silent;
    this.destination = other.destination;
    this.target = other.target;
    this.address = other.address == null ? null : new ComponentAddress(other.address);
    this.parameterList = ListUtil.copyList(other.parameterList);
    this.parameterMap = ListUtil.copyMap(other.parameterMap, Object.class);
  }

  /**
   * Client action constructor with type argument. Generates a client action
   *
   * @param type Client action type
   */
  public ClientAction(String type) {
    this.setType(type);
    this.parameterList = new ArrayList<>();
  }

  /**
   * Returns the client action type
   *
   * @return Client action type
   */
  public String getType() {
    return type;
  }

  /**
   * Stores the client action type
   *
   * @param type Client action type
   */
  private void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the answer destination
   *
   * @return Answer destination
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Stores the answer destination
   *
   * @param destination Answer destination
   * @return this
   */
  public ClientAction setDestination(String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Returns the client action parameter list
   *
   * @return Client action parameter list
   */
  @JsonIgnore
  public List<Parameter> getParameterList() {
    return parameterList;
  }

  /**
   * @return the parameterMap
   */
  @JsonGetter("parameters")
  public Map<String, Object> getParameterMap() {
    if (parameterMap == null) {
      parameterMap = new HashMap<>();
    }
    return parameterMap;
  }

  /**
   * @param parameterMap the parameterMap to set
   * @return this
   */
  public ClientAction setParameterMap(Map<String, Object> parameterMap) {
    this.parameterMap = parameterMap;
    return this;
  }

  /**
   * Retrieve async value
   *
   * @return async
   */
  public String getAsync() {
    return async;
  }

  /**
   * Store async action value
   *
   * @param async Async value
   * @return this
   */
  public ClientAction setAsync(String async) {
    this.async = async;
    return this;
  }

  /**
   * Store async action value
   *
   * @param async Async value
   * @return this
   */
  public ClientAction setAsync(boolean async) {
    this.async = Boolean.toString(async).toLowerCase();
    return this;
  }

  /**
   * If Client action is async
   *
   * @return Client action is async
   */
  public boolean isAsync() {
    return "true".equalsIgnoreCase(getAsync());
  }

  /**
   * Retrieve silent value
   *
   * @return silent
   */
  public String getSilent() {
    return silent;
  }

  /**
   * Store silent value
   *
   * @param silent Silent value
   * @return this
   */
  public ClientAction setSilent(String silent) {
    this.silent = silent;
    return this;
  }

  /**
   * Store silent value
   *
   * @param silent Silent value
   * @return this
   */
  public ClientAction setSilent(boolean silent) {
    this.silent = silent ? "true" : "false";
    return this;
  }

  /**
   * Check if action is silent
   *
   * @return is silent
   */
  public boolean isSilent() {
    return "true".equalsIgnoreCase(getSilent());
  }

  /**
   * Stores the client action parameter list
   *
   * @param parameterList Client action parameter list
   * @return this
   */
  public ClientAction setParameterList(List<Parameter> parameterList) {
    this.parameterList = parameterList;
    return this;
  }

  /**
   * Get the client action target
   *
   * @return Target value
   */
  public String getTarget() {
    return this.target;
  }

  /**
   * Stores the client action target
   *
   * @param target Action target
   * @return this
   */
  public ClientAction setTarget(String target) {
    this.target = target;
    return this;
  }

  /**
   * @return the address
   */
  public ComponentAddress getAddress() {
    return address;
  }

  /**
   * @param address the address to set
   * @return this
   */
  public ClientAction setAddress(ComponentAddress address) {
    this.address = address;
    return this;
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
    // Variable definition
    getParameterMap().put(name, value);
    return this;
  }

  @Override
  public ClientAction copy() throws AWException {
    return new ClientAction(this);
  }
}
