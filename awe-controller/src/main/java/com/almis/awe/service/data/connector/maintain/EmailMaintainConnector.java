package com.almis.awe.service.data.connector.maintain;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.QueryParameter;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.email.Email;
import com.almis.awe.model.entities.maintain.MaintainQuery;
import com.almis.awe.model.entities.queries.DatabaseConnection;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.service.data.builder.XMLEmailBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by dfuentes on 28/04/2017.
 */
public class EmailMaintainConnector extends ServiceConfig implements MaintainConnector {

  // Autowired services
  private XMLEmailBuilder emailBuilder;

  /**
   * Autowired constructor
   * @param emailBuilder Email builder
   */
  @Autowired
  public EmailMaintainConnector(XMLEmailBuilder emailBuilder) {
    this.emailBuilder = emailBuilder;
  }

  @Override
  public <T extends MaintainQuery> ServiceData launch(T query, DatabaseConnection databaseConnection, Map<String, QueryParameter> parameterMap) throws AWException {

    // Get email
    Email email = new Email(getElements().getEmail(query.getId()));

    // Initialize needed variables variables
    ServiceData serviceData = new ServiceData();
    DataList qryDat = new DataList();

    // Set defaults
    serviceData.setType(AnswerType.OK);
    qryDat.setRecords(1);

    // Build message
    serviceData = emailBuilder
      .setEmail(email)
      .parseEmail()
      .sendMail(true);

    // Launch email
    return serviceData;
  }
}
