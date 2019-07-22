package com.almis.awe.autoconfigure;

import com.almis.awe.model.component.XStreamSerializer;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
  public XStreamSerializer xStreamSerializer(XStreamMarshaller xStreamMarshaller) {
    return new XStreamSerializer(xStreamMarshaller);
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
