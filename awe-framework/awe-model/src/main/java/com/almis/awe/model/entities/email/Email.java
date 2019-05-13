package com.almis.awe.model.entities.email;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLNode;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Email Class
 *
 * Used to parse the Email.xml file with XStream
 * This class is used to parse a single email inside the file
 *
 * @author Pablo GARCIA - 28/JL/2011
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("email")
public class Email implements XMLNode, Copyable {

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
  private EmailItem from;

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
  private ServiceData serviceData;

  // End of line (CRLF)
  @XStreamOmitField
  private static final String CRLF = "\n";

  // PATTERNS
  @XStreamOmitField
  private static final Pattern wildcard = Pattern.compile("\\[#([a-zA-Z]*)#\\]");

  /**
   * Get email wildcard to replace
   *
   * @return
   */
  public static Pattern getWildcard() {
    return wildcard;
  }

  @JsonIgnore
  @Override
  public String getElementKey() {
    return getId();
  }

  @Override
  public Email copy() throws AWException {
    return this.toBuilder()
      .toList(ListUtil.copyList(getToList()))
      .ccList(ListUtil.copyList(getCcList()))
      .ccoList(ListUtil.copyList(getCcoList()))
      .subjectList(ListUtil.copyList(getSubjectList()))
      .bodyList(ListUtil.copyList(getBodyList()))
      .attachmentList(ListUtil.copyList(getAttachmentList()))
      .variableList(ListUtil.copyList(getVariableList()))
      .from(ListUtil.copyElement(getFrom()))
      .build();
  }
}
