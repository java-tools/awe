package com.almis.awe.model.entities.email;

import com.almis.awe.model.type.EmailMessageType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Email Class
 * <p>
 * Used to parse the Email.xml file with XStream
 * This class is used to parse a single email inside the file
 *
 * @author Pablo GARCIA - 28/JL/2011
 */
@Data
@Accessors(chain = true)
public class ParsedEmail {
  private String encoding;

  private String emailContentType;
  private InternetAddress from;
  private InternetAddress sender;
  private List<InternetAddress> to = new ArrayList<>();
  private List<InternetAddress> cc = new ArrayList<>();
  private List<InternetAddress> cco = new ArrayList<>();
  private List<InternetAddress> replyTo = new ArrayList<>();
  private EmailMessageType messageType;
  private String subject;
  private String body;
  private Map<String, File> attachments = new HashMap<>();

  public ParsedEmail addAttachment(String filename, File file) {
    attachments.put(filename, file);
    return this;
  }
}
