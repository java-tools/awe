package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.email.Email;
import com.almis.awe.model.entities.email.ParsedEmail;
import com.almis.awe.service.data.builder.XMLEmailBuilder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

@Log4j2
public class EmailService extends ServiceConfig {

  // Constants
  private static final String CRLF = "\n";
  // Autowired services
  private final JavaMailSender mailSender;
  private final XMLEmailBuilder emailBuilder;
  @Value("${" + AweConstants.PROPERTY_APPLICATION_ENCODING + ":" + AweConstants.APPLICATION_ENCODING + "}")
  private String encoding;

  /**
   * Autowired constructor
   *
   * @param mailSender
   */
  public EmailService(JavaMailSender mailSender, XMLEmailBuilder emailBuilder) {
    this.mailSender = mailSender;
    this.emailBuilder = emailBuilder;
  }

  @Async("contextlessTaskExecutor")
  public Future<ServiceData> sendEmail(String emailName, ObjectNode parameters) throws AWException {
    // Get email
    Email email = getElements().getEmail(emailName).copy();

    // Initialize needed variables variables
    ServiceData serviceData = new ServiceData();

    // Build message
    ParsedEmail parsedEmail = emailBuilder
      .setEmail(email)
      .setParameters(parameters)
      .parseEmail()
      .build();

    // Send email
    sendEmail(parsedEmail);

    // Return ok
    return new AsyncResult<>(serviceData
      .setTitle("OK_TITLE_EMAIL_OPERATION")
      .setMessage("OK_MESSAGE_EMAIL_OPERATION"));
  }

  @Async("contextlessTaskExecutor")
  public void sendEmail(ParsedEmail email) {
    MimeMessage message = mailSender.createMimeMessage();

    try {
      // Set email recipients
      setRecipients(email, message);

      // Set message subject
      message.setSubject(getLocale(email.getSubject()));

      // Append to message
      message.setContent(generateMultipartMessage(email));

      // Send mail
      mailSender.send(message);
    } catch (MessagingException | IOException exc) {
      log.error("Error sending email message", exc);
    }
  }


  /**
   * Generate email message parts
   *
   * @return Multipart
   * @throws MessagingException Error generating message
   * @throws IOException        Error retrieving file
   */
  protected Multipart generateMultipartMessage(ParsedEmail email) throws MessagingException, IOException {
    // Set the message body
    Multipart multipart = new MimeMultipart();

    // creates text body part for the message
    MimeBodyPart textPart = new MimeBodyPart();
    textPart.setText(getLocale(email.getBody()).replace("\\n", CRLF), encoding);

    // creates html body part for the message
    MimeBodyPart htmlPart = new MimeBodyPart();
    htmlPart.setContent(getLocale(email.getBody()), "text/html;charset=" + encoding);

    // Add body to multiPart
    multipart.addBodyPart(textPart);
    multipart.addBodyPart(htmlPart);

    // Add attachments
    if (!email.getAttachments().isEmpty()) {
      generateMultipartAttachments(email, multipart);
    }

    return multipart;
  }

  /**
   * Append attachments
   *
   * @param multipart Attachments
   * @throws javax.mail.MessagingException Message exception
   * @throws java.io.IOException           IO exception
   */
  protected void generateMultipartAttachments(ParsedEmail email, Multipart multipart) throws MessagingException, IOException {
    for (String fileName : email.getAttachments().keySet()) {
      File file = email.getAttachments().get(fileName);

      // If extension is not the same as the one of the file, force extension
      if (!getFileExtension(fileName).equalsIgnoreCase(getFileExtension(file.getName()))) {
        fileName += getFileExtension(file.getName());
      }

      MimeBodyPart attachFilePart = new MimeBodyPart();
      attachFilePart.setDisposition(Part.ATTACHMENT);
      attachFilePart.attachFile(file);
      attachFilePart.setFileName(fileName);
      multipart.addBodyPart(attachFilePart);
    }
  }

  /**
   * Set email recipients
   *
   * @param message Mime message
   * @throws MessagingException
   */
  protected void setRecipients(ParsedEmail email, MimeMessage message) throws MessagingException {
    // Set from
    message.setFrom(email.getFrom());
    message.setSender(email.getFrom());

    // Set reply to
    if (!email.getReplyTo().isEmpty()) {
      message.setReplyTo(email.getReplyTo().toArray(new InternetAddress[0]));
    }

    // Set to
    message.setRecipients(Message.RecipientType.TO, email.getTo().toArray(new InternetAddress[0]));

    // Set cc
    if (!email.getCc().isEmpty()) {
      message.setRecipients(Message.RecipientType.CC, email.getCc().toArray(new InternetAddress[0]));
    }

    // Set Cco
    if (!email.getCco().isEmpty()) {
      message.setRecipients(Message.RecipientType.BCC, email.getCco().toArray(new InternetAddress[0]));
    }
  }

  /**
   * Get file extension
   *
   * @param fileName File name
   * @return Extension
   */
  private String getFileExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf('.'));
  }
}
