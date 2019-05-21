package com.almis.awe.service.connector;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.services.ServiceInputParameter;
import com.almis.awe.model.entities.services.ServiceJava;
import com.almis.awe.model.entities.services.ServiceType;
import org.apache.logging.log4j.Level;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Launches a Java service
 */
public class JavaConnector extends AbstractServiceConnector {

  @Override
  public ServiceData launch(ServiceType service, Map<String, Object> paramsMapFromRequest) throws AWException {
    // Variable definition
    Object serviceInstance = null;
    Method serviceMethod = null;
    String qualifierAtritbute = null;
    ServiceData outData = null;
    Object[] paramsToInvoke = null;
    Class[] paramsClassesToInvoke = null;

    List<ServiceInputParameter> paramsFromXml = null;
    String classNameReceived = null;
    String methodReceived = null;

    if (service != null) {
      paramsFromXml = service.getParameterList();
      classNameReceived = ((ServiceJava) service).getClassName();
      methodReceived = ((ServiceJava) service).getMethod();
      qualifierAtritbute = ((ServiceJava) service).getQualifier();
    }

    try {
      // Generate parameters if any
      if (paramsFromXml != null) {
        paramsToInvoke = new Object[paramsFromXml.size()];
        paramsClassesToInvoke = new Class[paramsFromXml.size()];

        extractParameters(paramsFromXml, paramsMapFromRequest, paramsToInvoke, paramsClassesToInvoke);
      }
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_READING_PARAMETERS"),
              getLocale("ERROR_MESSAGE_READING_PARAMETERS", classNameReceived, methodReceived), exc);
    }

    // Object generation
    try {
      serviceInstance = qualifierAtritbute != null ? getBean(qualifierAtritbute) : getBean(Class.forName(classNameReceived));
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_ERROR_INITIALIZING_INSTANCE"),
        getLocale("ERROR_MESSAGE_ERROR_INITIALIZING_INSTANCE", classNameReceived), exc);
    }

    // Method generation
    try {
      getLogger().log(JavaConnector.class, Level.DEBUG, "[Java Service]: Calling Java Service {0} Method {1}", classNameReceived, methodReceived);
      serviceMethod = serviceInstance.getClass().getMethod(methodReceived, paramsClassesToInvoke);
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_METHOD_NOT_DEFINED"),
              getLocale("ERROR_MESSAGE_METHOD_NOT_DEFINED", methodReceived), exc);
    }

    // Method call
    try {
      // Call the method
      outData = (ServiceData) serviceMethod.invoke(serviceInstance, paramsToInvoke);
    } catch (IllegalAccessException exc) {
      throw new AWException(getLocale("ERROR_TITLE_ERROR_CALLING_METHOD"), exc);
    } catch (InvocationTargetException exc) {
      if (exc.getTargetException() instanceof AWException) {
        throw (AWException) exc.getTargetException();
      } else {
        Throwable primaryException = exc.getTargetException();
        throw new AWException(getLocale("ERROR_TITLE_ERROR_CALLING_METHOD"), primaryException.getMessage(), primaryException);
      }
    }

    // Return service output
    return outData;
  }

  /**
   * Launches a subscription to a service
   *
   * @param query Subscribed query
   * @return Service data
   */
  @Override
  public ServiceData subscribe(Query query) {

    // Return service output
    return new ServiceData();
  }
}
