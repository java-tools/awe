package com.almis.awe.service.data.builder;

import com.almis.awe.annotation.entities.locale.Locale;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.email.Email;
import com.almis.awe.model.entities.email.EmailItem;
import com.almis.awe.model.entities.email.EmailMessage;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.type.EmailMessageType;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

/**
 * Created by dfuentes on 10/05/2017.
 */
public class XMLEmailBuilder extends EmailBuilder {
  private QueryService queryService;
  private Email email;
  private Map<String, Variable> variables;

  private static final String BREAK_LINE_START = "<br>";
  private static final String BREAK_LINE = "<br/>";

  /**
   * Builder constructor
   *
   * @param queryService
   * @param mailSender
   */
  @Autowired
  public XMLEmailBuilder(QueryService queryService, JavaMailSender mailSender) {
    super(mailSender);
    this.queryService = queryService;
  }

  @Override
  protected void initialize() {
    super.initialize();
    // Initialize arrays and maps
    variables = new HashMap<>();
  }

  @Override
  protected void setDefaultMailSender() {
    setMailSender(mailSender);
  }

  /**
   * Parse given XML email template
   *
   * @return 
   * @throws AWException
   */
  public XMLEmailBuilder parseEmail() throws AWException {
    // Collect variables from email and queries
    collectVariables();

    // Parse message subject
    parseSubject(this.email.getSubjectList());

    // Parse message body
    parseBody(this.email.getBodyList());

    // Configure user elements
    parseFrom();
    parseTo();
    parseCC();
    parseCCo();

    // Append attachments
    parseAttachments();

    return this;
  }

  /**
   * Get variables
   *
   * @return
   */
  public Map<String, Variable> getVariables() {
    return variables;
  }

  /**
   * Set variables
   *
   * @param variable Variable
   * @param value    Value
   * @return this
   */
  public XMLEmailBuilder addVariable(Variable variable, String value) {
    this.variables.put(variable.getId(), variable);

    // Add parameters to context
    getRequest().setParameter(variable.getId(), JsonNodeFactory.instance.textNode(value));

    return this;
  }

  /**
   * Set variables
   *
   * @param variables Variables
   * @return this
   */
  public XMLEmailBuilder setVariables(List<Variable> variables) {
    if (variables != null) {
      for (Variable variable : variables) {
        this.variables.put(variable.getId(), variable);
        getRequest().setParameter(variable.getId(), JsonNodeFactory.instance.textNode(variable.getValue()));
      }
    }

    return this;
  }

  /**
   * Get Email object
   *
   * @return
   */

  public Email getEmail() {
    return email;
  }

  /**
   * Set email object
   *
   * @param email Email
   * @return this
   */
  public XMLEmailBuilder setEmail(Email email) {
    this.email = email;
    return this;
  }

  /**
   * Parse attachments from email template
   */
  private void parseAttachments() {
    if (this.email.getAttachmentList() != null) {
      for (EmailItem attachment : this.email.getAttachmentList()) {

        // Get variable names
        String path = attachment.getValue();
        String name = attachment.getLabel();

        // Get variable values
        String filePath = this.variables.get(path) == null ? "" : this.variables.get(path).getValue();
        String fileName = this.variables.get(name) == null ? "" : this.variables.get(name).getValue();

        // Insert new attachment for each email item
        if (!filePath.isEmpty() && !fileName.isEmpty()) {
          addAttachment(fileName, new File(fileName));
        }
      }
    }
  }

  /**
   * Parse cco element from email template
   */
  private void parseCCo() throws AWException {
    if (this.email.getCcoList() != null) {
      for (EmailItem cco : this.email.getCcoList()) {
        addCco(parseMailElement(cco));
      }
    }
  }

  /**
   * Parse cc element from email template
   */
  private void parseCC() throws AWException {
    if (this.email.getCcList() != null) {
      for (EmailItem cc : this.email.getCcList()) {
        addCc(parseMailElement(cc));
      }
    }
  }

  /**
   * Parse to element from email template
   */
  private void parseTo() throws AWException {
    if (this.email.getToList() != null) {
      for (EmailItem to : this.email.getToList()) {
        addTo(parseMailElement(to));
      }
    }
  }

  /**
   * Parse from element from email template
   */
  private void parseFrom() throws AWException {
    setFrom(parseMailElement(this.email.getFrom()));
  }

  private InternetAddress parseMailElement(EmailItem item) throws AWException {
    try {
      InternetAddress address = new InternetAddress();

      String emailAddress = variables.get(item.getValue()).getValue();
      String name = variables.get(item.getLabel()).getValue();

      address.setAddress(emailAddress);
      address.setPersonal(name);

      return address;
    } catch (UnsupportedEncodingException e) {
      throw new AWException(getLocale("ERROR_TITLE_DURING_EMAIL_SEND"), getLocale("ERROR_MESSAGE_EMAIL_GENERATE_DESTINATION"), e);
    }
  }

  /**
   * Parse body list
   *
   * @param bodyList
   */
  private void parseBody(List<EmailMessage> bodyList) {
    switch (getMessageType()) {
      case TEXT:
        parseBody(EmailMessageType.TEXT, bodyList);

        // remove HTML characters
        String bodyMessage = getBody();
        bodyMessage = StringEscapeUtils.unescapeHtml4(bodyMessage);

        // Change <br/> to new lines
        bodyMessage = bodyMessage.replace(BREAK_LINE, "\n");
        bodyMessage = bodyMessage.replace(BREAK_LINE_START, "\n");

        setBody(bodyMessage);
        break;
      case HTML:
      default:
        parseBody(EmailMessageType.HTML, bodyList);
        break;
    }
  }

  /**
   * Parse body list of given type
   *
   * @param type     Message type
   * @param bodyList Body list
   */
  private void parseBody(EmailMessageType type, List<EmailMessage> bodyList) {
    StringBuilder textBodyBuilder = new StringBuilder();

    for (EmailMessage body : bodyList) {
      String currentTextBody = "";

      if (body.getType().equalsIgnoreCase(type.toString())) {
        currentTextBody = getLabelLocale(body.getLabel());
        String value = body.getValue();

        Variable variable = variables.get(value);

        if (variable != null) {
          currentTextBody = variable.getValue();
        }
      }
      textBodyBuilder.append(currentTextBody);
    }

    setBody(replaceWildcards(textBodyBuilder.toString()));
  }

  /**
   * Parse subject list from email template
   *
   * @param subjectList
   */
  private void parseSubject(List<EmailMessage> subjectList) {
    StringBuilder subjectMessageBuilder = new StringBuilder();

    for (EmailMessage subject : subjectList) {
      // Initialize
      String currentSubjectMessage = getLabelLocale(subject.getLabel());

      Variable value = getVariables().get(subject.getValue());
      if (value != null) {
        currentSubjectMessage = value.getValue();
      }

      // append to subject message
      subjectMessageBuilder.append(currentSubjectMessage);
    }

    setSubject(replaceWildcards(subjectMessageBuilder.toString()));
  }

  /**
   * Collect all variables from email xml template
   *
   * @throws AWException
   */
  private void collectVariables() throws AWException {
    // Add variables to variable list
    setVariables(this.email.getVariableList());

    // Add variables from queries
    launchQueries();
  }

  /**
   * Returns converted simple local
   *
   * @param local
   * @return
   */
  @Locale
  private String getLabelLocale(String local) {
    return local;
  }

  /**
   * Launch all queries from email template
   *
   * @throws AWException Error launching queries
   */
  private void launchQueries() throws AWException {
    List<EmailMessage> emailMessageList = new ArrayList<>();

    if (email.getFrom() != null) {
      emailMessageList.add(email.getFrom());
    }

    if (email.getToList() != null) {
      emailMessageList.addAll(email.getToList());
    }

    if (email.getCcList() != null) {
      emailMessageList.addAll(email.getCcList());
    }

    if (email.getCcoList() != null) {
      emailMessageList.addAll(email.getCcoList());
    }

    if (email.getSubjectList() != null) {
      emailMessageList.addAll(email.getSubjectList());
    }

    if (email.getBodyList() != null) {
      emailMessageList.addAll(email.getBodyList());
    }

    if (email.getAttachmentList() != null) {
      emailMessageList.addAll(email.getAttachmentList());
    }

    // TODO: launch in threads
    manageQueryResults(email.getQuery());
    for (EmailMessage message : emailMessageList) {
      manageQueryResults(message.getQuery());
    }
  }

  /**
   * Add query results to variable map
   *
   * @param query Query
   */
  private void manageQueryResults(String query) throws AWException {
    if (query != null) {
      ServiceData result = queryService.launchQuery(query);
      if (result.getDataList() != null && result.getDataList().getRows() != null) {
        for (Map<String, CellData> row : result.getDataList().getRows()) {
          for (Entry<String, CellData> entry : row.entrySet()) {
            Variable variable = new Variable();
            variable.setName(entry.getKey());
            addVariable(variable, entry.getValue().getStringValue());
          }
        }
      }
    }
  }

  /**
   * Replaces the wildcards inside a message
   *
   * @param msg Message
   * @return Message TEXT
   */
  private String replaceWildcards(String msg) {

    /* Get wildcards within label */
    Matcher mat = Email.getWildcard().matcher(msg);
    String messageOut = msg;

    while (mat.find()) {
      for (Integer matIdx = 1, matTot = mat.groupCount(); matIdx <= matTot; matIdx++) {
        // Retrieve wildcard value
        String wldStr = mat.group(matIdx);
        String val = getRequest().getParameterAsString(wldStr);
        if (val == null || val.trim().isEmpty()) {
          val = "";
        }

        messageOut = StringUtil.replaceWildcard(messageOut, wldStr, val, "\\[#", "#\\]");
      }
    }

    return messageOut;
  }
}
