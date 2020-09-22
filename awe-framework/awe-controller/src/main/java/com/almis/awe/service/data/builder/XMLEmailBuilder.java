package com.almis.awe.service.data.builder;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.email.Email;
import com.almis.awe.model.entities.email.EmailItem;
import com.almis.awe.model.entities.email.EmailMessage;
import com.almis.awe.model.entities.queries.Variable;
import com.almis.awe.model.type.EmailMessageType;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.service.QueryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringEscapeUtils;

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
@Getter
@Setter
@Accessors(chain = true)
public class XMLEmailBuilder extends EmailBuilder {
  private final QueryService queryService;
  private final QueryUtil queryUtil;
  private Email email;
  private Map<String, Variable> variables;
  private ObjectNode parameters;

  private static final String BREAK_LINE_START = "<br>";
  private static final String BREAK_LINE = "<br/>";

  /**
   * Builder constructor
   *
   * @param queryService Query service
   * @param queryUtil    Query utilities
   */
  public XMLEmailBuilder(QueryService queryService, QueryUtil queryUtil) {
    super();
    this.queryService = queryService;
    this.queryUtil = queryUtil;
    this.variables = new HashMap<>();
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
   * @return this
   */
  public XMLEmailBuilder addVariable(Variable variable) {
    this.variables.put(variable.getId(), variable);
    return this;
  }

  /**
   * Set variables
   *
   * @param variables Variables
   * @return this
   */
  public XMLEmailBuilder setVariables(List<Variable> variables) throws AWException {
    if (variables != null) {
      for (Variable variable : variables) {
        this.variables.put(variable.getId(), variable);
        variable.setValue(getVariableAsString(variable));
      }
    }

    return this;
  }

  /**
   * Parse attachments from email template
   */
  private void parseAttachments() throws AWException {
    if (this.email.getAttachmentList() != null) {
      for (EmailItem attachment : this.email.getAttachmentList()) {

        // Get variable names
        String path = attachment.getValue();
        String name = attachment.getLabel();

        // Get variable values
        String filePath = this.variables.get(path) == null ? "" : getVariableAsString(variables.get(path));
        String fileName = this.variables.get(name) == null ? "" : getVariableAsString(variables.get(name));

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
      parseMailElement(getParsedEmail().getCco(), email.getCcoList());
    }
  }

  /**
   * Parse cc element from email template
   */
  private void parseCC() throws AWException {
    if (this.email.getCcList() != null) {
      parseMailElement(getParsedEmail().getCc(), email.getCcList());
    }
  }

  /**
   * Parse to element from email template
   */
  private void parseTo() throws AWException {
    if (this.email.getToList() != null) {
      parseMailElement(getParsedEmail().getTo(), email.getToList());
    }
  }

  /**
   * Parse from element from email template
   */
  private void parseFrom() throws AWException {
    getParsedEmail().setFrom(parseMailElement(this.email.getFrom()));
  }

  private InternetAddress parseMailElement(EmailItem item) throws AWException {
    try {
      InternetAddress address = new InternetAddress();

      String emailAddress = getVariableAsString(variables.get(item.getValue()));
      String name = getVariableAsString(variables.get(item.getLabel()));

      address.setAddress(emailAddress);
      address.setPersonal(name);

      return address;
    } catch (UnsupportedEncodingException e) {
      throw new AWException(getLocale("ERROR_TITLE_DURING_EMAIL_SEND"), getLocale("ERROR_MESSAGE_EMAIL_GENERATE_DESTINATION"), e);
    }
  }

  private void parseMailElement(List<InternetAddress> addresses, List<EmailItem> items) throws AWException {
    try {
      for (EmailItem item : items) {
        JsonNode emailAddressJson = getVariable(variables.get(item.getValue()));
        JsonNode emailNameJson = getVariable(variables.get(item.getLabel()));
        if (emailAddressJson.isArray()) {
          for (int i = 0, t = emailAddressJson.size(); i < t; i++) {
            InternetAddress address = new InternetAddress();
            address.setPersonal(emailNameJson.get(i).asText());
            address.setAddress(emailAddressJson.get(i).asText());
            addresses.add(address);
          }
        } else {
          InternetAddress address = new InternetAddress();
          address.setAddress(emailAddressJson.asText());
          address.setPersonal(emailNameJson.asText());
          addresses.add(address);
        }
      }
    } catch (UnsupportedEncodingException e) {
      throw new AWException(getLocale("ERROR_TITLE_DURING_EMAIL_SEND"), getLocale("ERROR_MESSAGE_EMAIL_GENERATE_DESTINATION"), e);
    }
  }

  /**
   * Parse body list
   *
   * @param bodyList
   */
  private void parseBody(List<EmailMessage> bodyList) throws AWException {
    switch (getParsedEmail().getMessageType()) {
      case TEXT:
        parseBody(EmailMessageType.TEXT, bodyList);

        // remove HTML characters
        String bodyMessage = getParsedEmail().getBody();
        bodyMessage = StringEscapeUtils.unescapeHtml(bodyMessage);

        // Change <br/> to new lines
        bodyMessage = bodyMessage.replace(BREAK_LINE, "\n");
        bodyMessage = bodyMessage.replace(BREAK_LINE_START, "\n");

        getParsedEmail().setBody(bodyMessage);
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
  private void parseBody(EmailMessageType type, List<EmailMessage> bodyList) throws AWException {
    StringBuilder textBodyBuilder = new StringBuilder();

    for (EmailMessage body : bodyList) {
      String currentTextBody = "";

      if (body.getType().equalsIgnoreCase(type.toString())) {
        currentTextBody = getLocale(body.getLabel());
        String value = body.getValue();

        Variable variable = variables.get(value);

        if (variable != null) {
          currentTextBody = getVariableAsString(variable);
        }
      }
      textBodyBuilder.append(currentTextBody);
    }

    getParsedEmail().setBody(replaceWildcards(textBodyBuilder.toString()));
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
      String currentSubjectMessage = getLocale(subject.getLabel());

      Variable value = getVariables().get(subject.getValue());
      if (value != null) {
        currentSubjectMessage = value.getValue();
      }

      // append to subject message
      subjectMessageBuilder.append(currentSubjectMessage);
    }

    getParsedEmail().setSubject(replaceWildcards(subjectMessageBuilder.toString()));
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

    //
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
            variable.setValue(entry.getValue().getStringValue());
            addVariable(variable);
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
      for (int matIdx = 1, matTot = mat.groupCount(); matIdx <= matTot; matIdx++) {
        // Retrieve wildcard value
        String wldStr = mat.group(matIdx);
        String val = getVariables().get(wldStr).getValue();
        if (val == null || val.trim().isEmpty()) {
          val = "";
        }

        messageOut = StringUtil.replaceWildcard(messageOut, wldStr, val, "\\[#", "#\\]");
      }
    }

    return messageOut;
  }

  private JsonNode getVariable(Variable variable) throws AWException {
    return queryUtil.getParameter(variable, getParameters());
  }

  private String getVariableAsString(Variable variable) throws AWException {
    return getVariable(variable).asText();
  }
}
