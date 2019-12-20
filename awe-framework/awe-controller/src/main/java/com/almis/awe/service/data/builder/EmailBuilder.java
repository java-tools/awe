package com.almis.awe.service.data.builder;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.model.entities.email.ParsedEmail;
import com.almis.awe.model.type.EmailMessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.mail.internet.InternetAddress;
import java.io.File;

/**
 * Created by dfuentes on 28/04/2017.
 */
@Getter
@Setter
@Accessors(chain = true)
public class EmailBuilder extends ServiceConfig {
  private ParsedEmail parsedEmail;

  /**
   * Builder constructor
   */
  public EmailBuilder() {
    parsedEmail = new ParsedEmail();
    parsedEmail.setMessageType(EmailMessageType.HTML);
    parsedEmail.setEmailContentType("text/html");
  }

  /**
   * add replyTo
   *
   * @param replyTo email replyTo
   * @return add replyTo
   */
  public EmailBuilder addReplyTo(InternetAddress replyTo) {
    parsedEmail.getReplyTo().add(replyTo);
    return this;
  }

  /**
   * Add to
   *
   * @param to To address
   * @return add To clause
   */
  public EmailBuilder addTo(InternetAddress to) {
    parsedEmail.getTo().add(to);
    return this;
  }

  /**
   * Add cc
   *
   * @param cc <code>CC</code> address
   * @return add <code>CC</code> address
   */
  public EmailBuilder addCc(InternetAddress cc) {
    parsedEmail.getCc().add(cc);
    return this;
  }

  /**
   * Add cco
   *
   * @param cco <code>CCO</code> address
   * @return add <code>CCO</code> address
   */
  public EmailBuilder addCco(InternetAddress cco) {
    parsedEmail.getCco().add(cco);
    return this;
  }

  /**
   * Add attachment file
   *
   * @param name       name of attachment
   * @param attachment file attachment
   * @return
   */
  public EmailBuilder addAttachment(String name, File attachment) {
    parsedEmail.getAttachments().put(name, attachment);
    return this;
  }

  /**
   * Retrieve email parsed
   *
   * @return Parsed email
   */
  public ParsedEmail build() {
    return parsedEmail;
  }
}