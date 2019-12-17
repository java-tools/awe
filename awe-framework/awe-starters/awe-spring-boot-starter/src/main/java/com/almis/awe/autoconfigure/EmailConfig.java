package com.almis.awe.autoconfigure;

import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.service.EmailService;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.data.builder.XMLEmailBuilder;
import com.almis.awe.service.data.connector.maintain.EmailMaintainConnector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Email configuration
 * @author dfuentes
 * Created by dfuentes on 25/04/2017.
 */
@Configuration
@ConditionalOnProperty(name = "awe.mail.enabled", havingValue = "true")
public class EmailConfig {

  @Value ("${awe.mail.auth}")
  private boolean mailAuthentication;

  @Value ("${awe.mail.host}")
  private String mailHost;

  @Value ("${awe.mail.port:25}")
  private int mailPort;

  @Value ("${awe.mail.user}")
  private String mailUser;

  @Value ("${awe.mail.pass}")
  private String mailPassword;

  @Value ("${awe.mail.debug:false}")
  private boolean mailDebug;

  @Value ("${awe.mail.ssl:false}")
  private boolean mailSsl;

  @Value ("${awe.mail.tls:false}")
  private boolean mailTls;

  @Value ("${awe.mail.localhost:localhost}")
  private String mailLocalhost;

  /**
   * Default JavaMail configuration
   *
   * @return Mail sender
   */
  @Bean
  @ConditionalOnMissingBean
  public JavaMailSender defaultMail() {
    JavaMailSenderImpl javaMailSender;

    // Create JavaMailSender
    javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setHost(mailHost);
    javaMailSender.setPort(mailPort);

    // Add authentication
    if (mailAuthentication) {
      javaMailSender.setUsername(mailUser);
      javaMailSender.setPassword(mailPassword);
    }

    // Generate smtp properties
    Properties properties = new Properties();
    properties.put("mail.smtp.localhost", mailLocalhost);
    properties.put("mail.debug", mailDebug);
    properties.put("mail.smtp.starttls.enable", mailTls);
    properties.put("mail.smtp.ssl.enable", mailSsl);
    if (mailSsl){
      properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
      properties.put("mail.smtp.ssl.checkserveridentity", true);
    }

    // Add properties
    javaMailSender.setJavaMailProperties(properties);
    return javaMailSender;
  }

  /////////////////////////////////////////////
  // SERVICES
  /////////////////////////////////////////////

  /**
   * Email service
   *
   * @return Email service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public EmailService emailService(JavaMailSender mailSender, XMLEmailBuilder emailBuilder) {
    return new EmailService(mailSender, emailBuilder);
  }

  /////////////////////////////////////////////
  // CONNECTORS
  /////////////////////////////////////////////

  /**
   * Email Maintain connector
   * @param emailService Email service
   * @return Email Maintain connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public EmailMaintainConnector emailMaintainConnector(EmailService emailService) {
    return new EmailMaintainConnector(emailService);
  }


  /////////////////////////////////////////////
  // BUILDERS
  /////////////////////////////////////////////

  /**
   * XML Email builder
   * @return XML Email builder bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public XMLEmailBuilder xmlEmailBuilder(QueryService queryService, QueryUtil queryUtil) {
    return new XMLEmailBuilder(queryService, queryUtil);
  }

}
