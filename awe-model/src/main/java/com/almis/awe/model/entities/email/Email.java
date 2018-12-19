package com.almis.awe.model.entities.email;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;
import java.util.regex.Pattern;

/*
 * File Imports
 */

/**
 * Email Class
 *
 * Used to parse the Email.xml file with XStream
 * This class is used to parse a single email inside the file
 *
 * @author Pablo GARCIA - 28/JL/2011
 */

@XStreamAlias("email")
public class Email extends XMLWrapper implements Copyable {

  protected static final long serialVersionUID = 1L;

  // Email id
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Email query
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

  // Send method
  @XStreamAlias("async")
  @XStreamAsAttribute
  private String async;

  // Email from
  @XStreamAlias("from")
  private EmailItem from = null;

  // Email to
  @XStreamImplicit(itemFieldName = "to")
  private List<EmailItem> toList;

  // Email cc
  @XStreamImplicit(itemFieldName = "cc")
  private List<EmailItem> ccList;

  // Email cco
  @XStreamImplicit(itemFieldName = "cco")
  private List<EmailItem> ccoList;

  // Email body
  @XStreamImplicit(itemFieldName = "subject")
  private List<EmailMessage> subjectList;

  // Email body
  @XStreamImplicit(itemFieldName = "body")
  private List<EmailMessage> bodyList;

  // Email attachments
  @XStreamImplicit(itemFieldName = "attachment")
  private List<EmailItem> attachmentList;

  // Email variables
  @XStreamImplicit(itemFieldName = "variable")
  private List<Variable> variableList;

  // Target Service Data
  @XStreamOmitField
  private ServiceData serviceData = null;

  // End of line (CRLF)
  @XStreamOmitField
  private static final String CRLF = "\n";
  // PATTERNS
  @XStreamOmitField
  private static final Pattern wildcard = Pattern.compile("\\[#([a-zA-Z]*)#\\]");

  /**
   * Default constructor
   */
  public Email() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Email(Email other) throws AWException {
    super(other);
    this.id = other.id;
    this.query = other.query;
    this.async = other.async;
    this.from = other.from == null ? null : new EmailItem(other.from);
    this.toList = ListUtil.copyList(other.toList);
    this.ccList = ListUtil.copyList(other.ccList);
    this.ccoList = ListUtil.copyList(other.ccoList);
    this.attachmentList = ListUtil.copyList(other.attachmentList);
    this.subjectList = ListUtil.copyList(other.subjectList);
    this.bodyList = ListUtil.copyList(other.bodyList);
    this.variableList = ListUtil.copyList(other.variableList);
  }

  /**
   * Returns the Email Service Result
   *
   * @return Email Service Result
   */
  public ServiceData getResult() {
    return serviceData;
  }

  /**
   * Stores the Email Service Result
   *
   * @param serviceData Email Service Result
   */
  public void setResult(ServiceData serviceData) {
    this.serviceData = serviceData;
  }

  /**
   * Returns the email id
   *
   * @return Email id
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the email id
   *
   * @param id Email id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the email query
   *
   * @return Email query
   */
  public String getQuery() {
    return query;
  }

  /**
   * Stores the email query
   *
   * @param query Email query
   */
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Returns true if send method is asynchronous
   *
   * @return Email async
   */
  public boolean isAsync() {
    return "true".equalsIgnoreCase(this.async);
  }

  /**
   * Stores the email send method
   *
   * @param async Email async
   */
  public void setAsync(String async) {
    this.async = async;
  }

  /**
   * Returns the Email "From" parameter
   *
   * @return Email "From" parameter
   */
  public EmailItem getFrom() {
    return from;
  }

  /**
   * Stores the Email "From" parameter
   *
   * @param from Email "From" parameter
   */
  public void setFrom(EmailItem from) {
    this.from = from;
  }

  /**
   * Returns the Email "To" list
   *
   * @return Email "To" list
   */
  public List<EmailItem> getToList() {
    return toList;
  }

  /**
   * Stores the Email "To" list
   *
   * @param toList Email "To" list
   */
  public void setToList(List<EmailItem> toList) {
    this.toList = toList;
  }

  /**
   * Returns the Email "Cc" list
   *
   * @return Email "Cc" list
   */
  public List<EmailItem> getCcList() {
    return ccList;
  }

  /**
   * Stores the Email "Cc" list
   *
   * @param ccList Email "Cc" list
   */
  public void setCcList(List<EmailItem> ccList) {
    this.ccList = ccList;
  }

  /**
   * Returns the Email "Cco" list
   *
   * @return Email "Cco" list
   */
  public List<EmailItem> getCcoList() {
    return ccoList;
  }

  /**
   * Stores the Email "Cco" list
   *
   * @param ccoList Email "Cco" list
   */
  public void setCcoList(List<EmailItem> ccoList) {
    this.ccoList = ccoList;
  }

  /**
   * Returns the Email "Subject" List
   *
   * @return Email "Subject" List
   */
  public List<EmailMessage> getSubjectList() {
    return subjectList;
  }

  /**
   * Stores the Email "Subject" List
   *
   * @param subjectList Email "Subject" List
   */
  public void setSubjectList(List<EmailMessage> subjectList) {
    this.subjectList = subjectList;
  }

  /**
   * Returns the Email "Body" list
   *
   * @return Email "Body" list
   */
  public List<EmailMessage> getBodyList() {
    return bodyList;
  }

  /**
   * Stores the Email "Body" list
   *
   * @param bodyList Email "Body" list
   */
  public void setBodyList(List<EmailMessage> bodyList) {
    this.bodyList = bodyList;
  }

  /**
   * Returns the Email "Attachment" list
   *
   * @return Email "Attachment" list
   */
  public List<EmailItem> getAttachmentList() {
    return attachmentList;
  }

  /**
   * Stores the Email "Attachment" list
   *
   * @param attachmentList Email "Attachment" list
   */
  public void setAttachmentList(List<EmailItem> attachmentList) {
    this.attachmentList = attachmentList;
  }

  /**
   * Returns the Email "Variable" list
   *
   * @return Email "Variable" list
   */
  public List<Variable> getVariableList() {
    return variableList;
  }

  /**
   * Stores the Email "Variable" list
   *
   * @param variableList Email "Variable" list
   */
  public void setVariableList(List<Variable> variableList) {
    this.variableList = variableList;
  }

  /**
   * Returns if identifier belongs to the element
   *
   * @param ide
   * @return true if the identifier belongs to the element
   */
  @Override
  public boolean isElement(String ide) {
    return this.getId().equals(ide);
  }

  /**
   * Return the XML Element Key
   *
   * @return the elementKey
   */
  @Override
  public String getElementKey() {
    return this.getId();
  }

  /**
   * Get email wildcard to replace
   *
   * @return
   */
  public static Pattern getWildcard() {
    return wildcard;
  }

  @Override
  public Email copy() throws AWException {
    return new Email(this);
  }
}
