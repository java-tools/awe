package com.almis.awe.service.data.connector.maintain;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.almis.awe.service.EmailService;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by dfuentes on 28/04/2017.
 */
public class EmailMaintainConnector extends ServiceConfig implements MaintainConnector {

  // Autowired services
  private final EmailService emailService;

  /**
   * Autowired constructor
   *
   * @param emailService Email service
   */
  public EmailMaintainConnector(EmailService emailService) {
    this.emailService = emailService;
  }

  @Override
  public <T extends MaintainQuery> ServiceData launch(T query, DatabaseConnection databaseConnection, ObjectNode parameters) throws AWException {
    try {
      // Send email
      return emailService.sendEmail(query.getId(), parameters).get();
    } catch (Exception exc) {
      throw new AWException("Error sending email", exc);
    }
  }
}
