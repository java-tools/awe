package com.almis.awe.autoconfigure;

import com.almis.awe.model.component.XStreamSerializer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Initialize serializer beans
 *
 * @author pgarcia
 */
@Configuration
public class SerializerConfig {

  @Value("${xml.parser.allowed.paths}")
  private String[] allowedPaths;
  private ThreadLocal<ScriptEngine> engineThread;

  /**
   * On construct initialize threadlocal
   */
  @PostConstruct
  public void onConstruct() {
    engineThread = ThreadLocal.withInitial(() -> new ScriptEngineManager().getEngineByName("graal.js"));
  }

  /**
   * On destroy remove threadlocal
   */
  @PreDestroy
  public void onDestroy() {
    engineThread.remove();
  }

  /**
   * XStream serializer
   *
   * @return Serializer
   */
  @Bean
  @ConditionalOnMissingBean
  public XStreamSerializer xStreamSerializer(XStreamMarshaller xStreamMarshaller) {
    // Configure xstream security
    XStream xstream = xStreamMarshaller.getXStream();
    XStream.setupDefaultSecurity(xstream); // to be removed after 1.5
    // allow any type from the same package
    xstream.allowTypesByWildcard(allowedPaths);

    // Retrieve serializer
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
  @Scope("prototype")
  public ScriptEngine javascriptEngine() {
    return engineThread.get();
  }
}
