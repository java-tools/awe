package com.almis.awe.model.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @author pgarcia
 */
public class XStreamSerializer {

  // Xml Xstream factory
  @Autowired
  private XStreamMarshaller xmlXStreamMarshaller;
  private final Logger logger = LogManager.getLogger(this.getClass());

  /**
   * Serialize an object to the given OutputStream as pretty-printed XML
   * (Object to Xml)
   *
   * @param wrapperClass Class with XStream annotations
   * @param object       Object to serialize
   * @param outputStream OutputStream to set
   */
  public void writeXmlFromObject(Class wrapperClass, Object object, OutputStream outputStream) {
    try {
      // Process annotations
      xmlXStreamMarshaller.getXStream().processAnnotations(wrapperClass);
      // Marshall object to Xml
      xmlXStreamMarshaller.marshalOutputStream(object, outputStream);
    } catch (IOException | XmlMappingException ex) {
      logger.error("[XStreamSerializer] The Object {0} cannot be serialized", object.getClass().getCanonicalName(), ex);
    }
  }

  /**
   * Serialize an object to the given Writer as pretty-printed XML
   * (Object to Xml)
   *
   * @param wrapperClass Class with XStream annotations
   * @param object       Object to serialize
   * @param writer       Writer to set
   */
  public void writeXmlFromObject(Class wrapperClass, Object object, Writer writer) {
    try {
      // Process annotations
      xmlXStreamMarshaller.getXStream().processAnnotations(wrapperClass);
      // Marshall object to Xml
      xmlXStreamMarshaller.marshalWriter(object, writer);
    } catch (IOException | XmlMappingException ex) {
      logger.error("[XStreamSerializer] The Object {0} cannot be serialized", object.getClass().getCanonicalName(), ex);
    }
  }

  /**
   * Deserialize an object from an XML reader
   * (Xml to Reader (Object))
   *
   * @param wrapperClass Class with XStream annotations
   * @param reader       Reader for deserialize
   * @return Object deserialize
   */
  public <T> T getObjectFromXml(Class<T> wrapperClass, Reader reader) {
    T object = null;
    try {
      // Proccess annotations
      xmlXStreamMarshaller.getXStream().processAnnotations(wrapperClass);
      // Marshall objecto to Xml
      object = (T) xmlXStreamMarshaller.unmarshalReader(reader);
    } catch (IOException | XmlMappingException exc) {
      logger.error("[XStreamSerializer] The Object {0} cannot be deserialized", wrapperClass.getCanonicalName(), exc);
    }
    return object;
  }

  /**
   * Deserialize an object from an XML InputStream
   * (Xml to InputStream (Object))
   *
   * @param wrapperClass Class with XStream annotations
   * @param inputStream  InputStream for deserialize
   * @return Object deserialize
   */
  public <T> T getObjectFromXml(Class<T> wrapperClass, InputStream inputStream) {
    T object = null;
    try {
      // Process annotations
      xmlXStreamMarshaller.getXStream().processAnnotations(wrapperClass);
      // Marshall object to Xml
      object = (T) xmlXStreamMarshaller.unmarshalInputStream(inputStream);
    } catch (IOException | XmlMappingException exc) {
      logger.error("[XStreamSerializer] The Object {0} cannot be deserialized", wrapperClass.getCanonicalName(), exc);
    }
    return object;
  }
}
