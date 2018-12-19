package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

/**
 * Target Class
 * Used to parse the file Maintain.xml with XStream
 * Contains a list of maintain queries and allows to launch them all
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@XStreamAlias("target")
public class Target extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 2101818729705484693L;

  // Target Name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name = null;

  // Target Label (message title)
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label = null;
  // Target Exclusive (target is exclusive)
  @XStreamAlias("exclusive")
  @XStreamAsAttribute
  private String exclusive = null;

  // Query can be launched out of session
  @XStreamAlias("public")
  @XStreamAsAttribute
  private String isPublic = null;
  // Target Query List
  @XStreamImplicit
  private List<MaintainQuery> queryList;

  // Target Service Data
  @XStreamOmitField
  private ServiceData serviceData = null;

  /**
   * Default constructor
   */
  public Target() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Target(Target other) throws AWException {
    super(other);
    this.name = other.name;
    this.label = other.label;
    this.exclusive = other.exclusive;
    this.isPublic = other.isPublic;
    this.queryList = ListUtil.copyList(other.queryList);
  }

  /**
   * Stores the name of the target
   *
   * @param nam Name of the target
   */
  public void setName(String nam) {
    this.name = nam;
  }

  /**
   * Returns the name of the target
   *
   * @return Name of the target
   */
  public String getName() {
    return this.name;
  }

  /**
   * Stores the message title
   *
   * @param label Message title
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns the message title
   *
   * @return Message title
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Returns if query can be launched off session
   *
   * @return If query can be launched off session
   */
  public String getPublic() {
    return isPublic;
  }

  /**
   * Stores if query can be launched off session
   *
   * @param isPublic query can be launched off session
   */
  public void setPublic(String isPublic) {
    this.isPublic = isPublic;
  }

  /**
   * Stores the Query List
   *
   * @param queryList Query List
   */
  public void setQueryList(List<MaintainQuery> queryList) {
    this.queryList = queryList;
  }

  /**
   * Check if target is public
   *
   * @return Target is public
   */
  public boolean isPublic() {
    return "true".equalsIgnoreCase(getPublic());
  }

  /**
   * Returns the Query List
   *
   * @return Query List
   */
  public List<MaintainQuery> getQueryList() {
    return this.queryList;
  }

  /**
   * Returns the maintain query in the selected position
   *
   * @param idx Query Position
   * @return Maintain Query selected
   */
  public MaintainQuery getQuery(int idx) {
    return queryList.get(idx);
  }

  /**
   * @return the exclusive
   */
  public String getExclusive() {
    return exclusive;
  }

  /**
   * @return the exclusive
   */
  public boolean isExclusive() {
    return "true".equalsIgnoreCase(exclusive);
  }

  /**
   * @param exclusive the exclusive to set
   */
  public void setExclusive(String exclusive) {
    this.exclusive = exclusive;
  }

  /**
   * Returns the Target Service Result
   *
   * @return Target Service Result
   */
  public ServiceData getResult() {
    return serviceData;
  }

  /**
   * Stores the Target Service Result
   *
   * @param serviceData Target Service Result
   */
  public void setResult(ServiceData serviceData) {
    this.serviceData = serviceData;
  }

  /**
   * Returns if identifier belongs to the element
   *
   * @param ide Element identifier
   * @return true if the identifier belongs to the element
   */
  @Override
  public boolean isElement(String ide) {
    return this.getName().equals(ide);
  }

  /**
   * Return the XML Element Key
   *
   * @return the elementKey
   */
  @Override
  public String getElementKey() {
    return this.getName();
  }

  @Override
  public Target copy() throws AWException {
    return new Target(this);
  }
}
