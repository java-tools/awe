package com.almis.awe.model.component;

import lombok.extern.log4j.Log4j2;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author pgarcia
 */
@Log4j2
public class XStreamSerializer {

  // Xml Xstream factory
  private final XStreamMarshaller xmlXStreamMarshaller;

  private static final String ERROR_SERIALIZING = "[XStreamSerializer] The object {} cannot be serialized";
  private static final String ERROR_DESERIALIZING = "[XStreamSerializer] The object {} cannot be deserialized";

  /**
   * Autowired constructor
   *
   * @param xStreamMarshaller Marshaller
   */
  public XStreamSerializer(XStreamMarshaller xStreamMarshaller) {
    this.xmlXStreamMarshaller = xStreamMarshaller;
  }

  /**
   * Serialize an object to the given OutputStream as pretty-printed XML
   * (Object to Xml)
   *
   * @param wrapperClass Class with XStream annotations
   * @param object       Object to serialize
   * @param outputStream OutputStream to set
   */
  public <T> void writeXmlFromObject(Class<T> wrapperClass, Object object, OutputStream outputStream) {
    try {
      // Process annotations
      xmlXStreamMarshaller.getXStream().processAnnotations(wrapperClass);
      // Marshall object to Xml
      xmlXStreamMarshaller.marshalOutputStream(object, outputStream);
    } catch (IOException | XmlMappingException ex) {
      log.error(ERROR_SERIALIZING, object.getClass().getCanonicalName(), ex);
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
  public <T> void writeXmlFromObject(Class<T> wrapperClass, Object object, Writer writer) {
    try {
      // Process annotations
      xmlXStreamMarshaller.getXStream().processAnnotations(wrapperClass);
      // Marshall object to Xml
      xmlXStreamMarshaller.marshalWriter(object, writer);
    } catch (IOException | XmlMappingException ex) {
      log.error(ERROR_SERIALIZING, object.getClass().getCanonicalName(), ex);
    }
  }

  /**
   * Serialize an object to the given Writer as pretty-printed XML
   * (Object to Xml)
   *
   * @param wrapperClass Class with XStream annotations
   * @param object       Object to serialize
   */
  public <T> String writeStringFromObject(Class<T> wrapperClass, Object object) {
    String output = null;
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      writeXmlFromObject(wrapperClass, object, outputStream);
      output = outputStream.toString(StandardCharsets.UTF_8.toString());
    } catch (IOException | XmlMappingException ex) {
      log.error(ERROR_SERIALIZING, object.getClass().getCanonicalName(), ex);
    }
    return output;
  }

  /**
   * Deserialize an object from an XML reader
   * (Xml to Reader (Object))
   *
   * @param wrapperClass Class with XStream annotations
   * @param reader       Reader for deserialize
   * @param <T>          Object deserialize type
   * @return Object deserialize
   */
  public <T> T getObjectFromXml(Class<T> wrapperClass, Reader reader) {
    T object = null;
    try {
      // Process annotations
      xmlXStreamMarshaller.getXStream().processAnnotations(wrapperClass);
      // Marshall object to Xml
      object = (T) xmlXStreamMarshaller.unmarshalReader(reader);
    } catch (IOException | XmlMappingException exc) {
      log.error(ERROR_DESERIALIZING, wrapperClass.getCanonicalName(), exc);
    }
    return object;
  }

  /**
   * Deserialize an object from an XML InputStream
   * (Xml to InputStream (Object))
   *
   * @param wrapperClass Class with XStream annotations
   * @param inputStream  InputStream for deserialize
   * @param <T>          Object deserialize type
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
      log.error(ERROR_DESERIALIZING, wrapperClass.getCanonicalName(), exc);
    }
    return object;
  }

  /**
   * Deserialize an object from a string template
   * (Xml to InputStream (Object))
   *
   * @param wrapperClass Class with XStream annotations
   * @param template     Template for deserialize
   * @param <T>          Object deserialize type
   * @return Object deserialize
   */
  public <T> T getObjectFromTemplate(Class<T> wrapperClass, String template) {
    T object = null;
    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getBytes(StandardCharsets.UTF_8.toString()))) {
      object = getObjectFromXml(wrapperClass, inputStream);
    } catch (IOException | XmlMappingException exc) {
      log.error(ERROR_DESERIALIZING, wrapperClass.getCanonicalName(), exc);
    }
    return object;
  }
}
