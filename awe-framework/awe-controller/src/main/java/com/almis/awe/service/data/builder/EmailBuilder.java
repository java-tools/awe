package com.almis.awe.service.data.builder;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.type.EmailMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dfuentes on 28/04/2017.
 */
@Component
@Scope("prototype")
public class EmailBuilder extends ServiceConfig {

  // Autowired services
  protected JavaMailSender mailSender;

  @Value("${" + AweConstants.PROPERTY_APPLICATION_ENCODING + ":" + AweConstants.APPLICATION_ENCODING + "}")
  private String encoding;

  private String emailContentType;
  private InternetAddress from;
  private InternetAddress sender;
  private List<InternetAddress> to;
  private List<InternetAddress> cc;
  private List<InternetAddress> cco;
  private List<InternetAddress> replyTo;
  private EmailMessageType messageType;
  private String subject;
  private String body;
  private Map<String, File> attachments;

  private static final String CRLF = "\n";

  /**
   *  Builder constructor
   * @param mailSender Email sender
   */
  @Autowired
  public EmailBuilder(JavaMailSender mailSender) {
    this.mailSender = mailSender;

    initialize();
    setDefaults();
  }

  /**
   * Initialize maps and arrays variables
   */
  protected void initialize() {
    to = new ArrayList<>();
    cc = new ArrayList<>();
    cco = new ArrayList<>();
    replyTo = new ArrayList<>();
    attachments = new HashMap<>();
  }

  /**
   * Set defaults to variables
   */
  protected void setDefaults() {
    setMessageType(EmailMessageType.HTML);
    setEmailContentType("text/html");
    setDefaultMailSender();
  }

  /**
   * Create JavaMailSender based on user configuration or default if no specific configuration defined
   */
  protected void setDefaultMailSender() {
    this.setMailSender(mailSender);
  }

  /**
   * Get current encoding
   *
   * @return
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * Set email content encoding
   *
   * @param encoding
   *
   * @return
   */
  public EmailBuilder setEncoding(String encoding) {
    this.encoding = encoding;
    return this;
  }

  /**
   * Get message content type
   *
   * @return
   */
  public String getEmailContentType() {
    return emailContentType;
  }

  /**
   * Set email content type
   *
   * @param emailContentType
   *
   * @return
   */
  public EmailBuilder setEmailContentType(String emailContentType) {
    this.emailContentType = emailContentType;
    return this;
  }

  /**
   * Get mail sender
   *
   * @return
   */
  public JavaMailSender getMailSender() {
    return mailSender;
  }

  /**
   * Set mail sender
   *
   * @param mailSender
   * @return
   */
  public EmailBuilder setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
    return this;
  }

  /**
   * Get from
   *
   * @return
   */
  public InternetAddress getFrom() {
    return from;
  }

  /**
   * Set from
   *
   * @param from
   *
   * @return
   */
  public EmailBuilder setFrom(InternetAddress from) {
    this.from = from;
    return this;
  }

  /**
   * Get sender, by default same as from
   *
   * @return
   */
  public InternetAddress getSender() {
    return sender == null ? getFrom() : sender;
  }

  /**
   * Set sender, by default same as from
   *
   * @param sender
   *
   * @return
   */
  public EmailBuilder setSender(InternetAddress sender) {
    this.sender = sender;
    return this;
  }

  /**
   * Get ReplyTo
   *
   * @return
   */
  public List<InternetAddress> getReplyTo() {
    return replyTo;
  }

  /**
   * add replyTo
   *
   * @param replyTo
   *
   * @return
   */
  public EmailBuilder addReplyTo(InternetAddress replyTo) {
    this.replyTo.add(replyTo);
    return this;
  }

  /**
   * Set replyTo
   *
   * @param replyTo
   *
   * @return
   */
  public EmailBuilder setReplyTo(List<InternetAddress> replyTo) {
    this.replyTo = replyTo;
    return this;
  }

  /**
   * Get to
   *
   * @return
   */
  public List<InternetAddress> getTo() {
    return to;
  }

  /**
   * Add to
   *
   * @param to
   *
   * @return
   */
  public EmailBuilder addTo(InternetAddress to) {
    this.to.add(to);
    return this;
  }

  /**
   * Set to
   *
   * @param to
   *
   * @return
   */
  public EmailBuilder setTo(List<InternetAddress> to) {
    this.to = to;
    return this;
  }

  /**
   * Get cc
   *
   * @return
   */
  public List<InternetAddress> getCc() {
    return cc;
  }

  /**
   * Add cc
   *
   * @param cc
   *
   * @return
   */
  public EmailBuilder addCc(InternetAddress cc) {
    this.cc.add(cc);
    return this;
  }

  /**
   * Set cc
   *
   * @param cc
   *
   * @return
   */
  public EmailBuilder setCc(List<InternetAddress> cc) {
    this.cc = cc;
    return this;
  }

  /**
   * Get cco
   *
   * @return
   */
  public List<InternetAddress> getCco() {
    return cco;
  }

  /**
   * Add cco
   *
   * @param cco
   *
   * @return
   */
  public EmailBuilder addCco(InternetAddress cco) {
    this.cco.add(cco);
    return this;
  }

  /**
   * Set cco
   *
   * @param cco
   *
   * @return
   */
  public EmailBuilder setCco(List<InternetAddress> cco) {
    this.cco = cco;
    return this;
  }

  /**
   * Get message type
   *
   * @return
   */
  public EmailMessageType getMessageType() {
    return messageType;
  }

  /**
   * Set message type
   *
   * @param messageType
   *
   * @return
   */
  public EmailBuilder setMessageType(EmailMessageType messageType) {
    this.messageType = messageType;
    return this;
  }

  /**
   * Get subject
   *
   * @return
   */
  public String getSubject() {
    return subject;
  }

  /**
   * Set subject
   *
   * @param subject
   *
   * @return
   */
  public EmailBuilder setSubject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get body
   *
   * @return
   */
  public String getBody() {
    return body;
  }

  /**
   * Set body
   *
   * @param body
   *
   * @return
   */
  public EmailBuilder setBody(String body) {
    this.body = body;
    return this;
  }

  /**
   * get Attachment files
   *
   * @return
   */
  public Map<String, File> getAttachments() {
    return attachments;
  }

  /**
   * Add attachment file
   *
   * @param attachment
   * @param name
   *
   * @return
   */
  public EmailBuilder addAttachment(String name, File attachment) {
    this.attachments.put(name, attachment);
    return this;
  }

  /**
   * Set attachment files
   *
   * @param attachments
   *
   * @return
   */
  public EmailBuilder setAttachments(Map<String, File> attachments) {
    this.attachments = attachments;
    return this;
  }

  /**
   * Send email
   * @param async Async send
   * @return Mail send status
   * @throws AWException Error sending mail
   */
  public ServiceData sendMail(boolean async) throws AWException {
    ServiceData serviceData = new ServiceData();
    try {

      MimeMessage message = this.mailSender.createMimeMessage();

      // Set email recipients
      setRecipients(message);

      // Set message subject
      message.setSubject(getLocale(getSubject()));

      // Generate message parts
      Multipart multipart = generateMultipartMessage();

      // Append to message
      // sets the multipart as message's content
      message.setText(getLocale(getBody()).replace("\\n", CRLF));
      message.setContent(multipart, "html");

      if (async) {
        sendAsync(message);
      } else {
        send(message);
      }

      serviceData.setType(AnswerType.OK);
      serviceData.setTitle("OK_TITLE_EMAIL_OPERATION");
      serviceData.setMessage("OK_MESSAGE_EMAIL_OPERATION");

    } catch (MessagingException | IOException exc) {
      throw new AWException(getLocale("ERROR_TITLE_DURING_EMAIL_SEND"),
              getLocale("ERROR_MESSAGE_DURING_EMAIL_SEND"), exc);
    }
    return serviceData;
  }

  /**
   * Send email to recipients asynchronously
   *
   * @param message Message
   */
  protected void sendAsync(final MimeMessage message) {
    final JavaMailSender javaMailSender = getMailSender();
    ExecutorService executor = Executors.newFixedThreadPool(5);
    Runnable mailRunnable = () -> javaMailSender.send(message);
    executor.execute(mailRunnable);
    executor.shutdown();
  }

  /**
   * Send email to recipients synchronously
   *
   * @param message
   * @throws com.almis.awe.exception.AWException
   */
  protected void send(final MimeMessage message) throws AWException {
    try {
      final JavaMailSender javaMailSender = getMailSender();

      javaMailSender.send(message);
    } catch (MailException e) {
      throw new AWException(getLocale("ERROR_TITLE_DURING_EMAIL_SEND"),
              getLocale("ERROR_MESSAGE_DURING_EMAIL_SEND"), e);
    }
  }

  /**
   * Generate email message parts
   *
   * @return Multipart
   *
   * @throws MessagingException Error generating message
   * @throws IOException Error retrieving file
   */
  protected Multipart generateMultipartMessage() throws MessagingException, IOException {
    // Message content type
    String messageContentType = getMessageType() + "; charset=" + getEncoding();

    // Set the message body
    Multipart multipart = new MimeMultipart();

    // creates body part for the message
    MimeBodyPart messageBodyPart = new MimeBodyPart();

    // Content type
    if (getMessageType() == EmailMessageType.HTML) {
      messageBodyPart.setText(getLocale(getBody()), null, messageContentType);
    } else {
      messageBodyPart.setText(getLocale(getBody()));
    }

    // Add body to multiPart
    multipart.addBodyPart(messageBodyPart);

    // Add attachments
    if (!getAttachments().isEmpty()) {
      generateMultipartAttachments(multipart);
    }

    return multipart;
  }

  /**
   * Append attachments
   *
   * @param multipart Attachments
   * @throws javax.mail.MessagingException
   * @throws java.io.IOException
   */
  protected void generateMultipartAttachments(Multipart multipart) throws MessagingException, IOException {
    for (String fileName : getAttachments().keySet()) {
      File file = getAttachments().get(fileName);

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
   * Get file extension
   * @param fileName File name
   * @return Extension
   */
  private String getFileExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf('.'));
  }

  /**
   * Set email recipients
   *
   * @param message
   *
   * @throws MessagingException
   */
  protected void setRecipients(MimeMessage message) throws MessagingException {
    // Set from
    message.setFrom(getFrom());

    message.setSender(getFrom());

    if (!getReplyTo().isEmpty()) {
      message.setReplyTo(getReplyTo().toArray(new InternetAddress[getReplyTo().size()]));
    }

    // Set to
    message.setRecipients(MimeMessage.RecipientType.TO, getTo().toArray(new InternetAddress[getTo().size()]));

    // Set cc
    if (!getCc().isEmpty()) {
      message.setRecipients(MimeMessage.RecipientType.CC, getCc().toArray(new InternetAddress[getCc().size()]));
    }

    // Set Cco
    if (!getCco().isEmpty()) {
      message.setRecipients(MimeMessage.RecipientType.BCC, getCco().toArray(new InternetAddress[getCco().size()]));
    }
  }
}