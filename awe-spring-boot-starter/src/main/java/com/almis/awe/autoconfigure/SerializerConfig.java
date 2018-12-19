package com.almis.awe.autoconfigure;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.almis.awe.model.component.XStreamSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Initialize serializer beans
 * 
 * @author pgarcia
 */
@Configuration
public class SerializerConfig {

  /**
   * XStream serializer
   * @return Serializer
   */
  @Bean
  @ConditionalOnMissingBean
  public XStreamSerializer xStreamSerializer() {
    return new XStreamSerializer();
  }

  /**
   * Get XML management Engine
   * 
   * @return XStream Marshaller
   */
  @Bean
  @ConditionalOnMissingBean
  public XStreamMarshaller xStreamMarshaller() {
    XStreamMarshaller xstreamMarshaller = new XStreamMarshaller();
    xstreamMarshaller.setStreamDriver(new DomDriver(null, new NoNameCoder()));
    return xstreamMarshaller;
  }

  /**
   * Get Javascript management Engine
   * 
   * @return Javascript engine
   */
  @Bean
  @ConditionalOnMissingBean
  public ScriptEngine javascriptEngine() {
    return new ScriptEngineManager().getEngineByName("JavaScript");
  }
}
