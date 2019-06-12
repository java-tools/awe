package com.almis.awe.annotation.autoconfigure;

import com.almis.awe.annotation.aspect.*;
import com.almis.awe.annotation.processor.locale.LocaleProcessor;
import com.almis.awe.annotation.processor.session.SessionProcessor;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.AweSession;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Class load annotation processor definitions
 *
 * Also enables Aspect definitions with Auto-proxying
 *
 * @author dfuentes
 * Created by dfuentes on 26/05/2017.
 */
@Configuration
@EnableAspectJAutoProxy
public class AnnotationConfig {

  /////////////////////////////////////////////
  // PROCESSORS
  /////////////////////////////////////////////

  /**
   * Locale processor
   * @param aweSessionObjectFactory
   * @param aweElementsObjectFactory
   * @return Locale processor bean
   */
  @Bean
  @ConditionalOnMissingBean
  public LocaleProcessor localeProcessor(final ObjectFactory<AweSession> aweSessionObjectFactory,
                                         final ObjectFactory<AweElements> aweElementsObjectFactory) {
    return new LocaleProcessor(aweSessionObjectFactory, aweElementsObjectFactory);
  }

  /**
   * Session processor
   * @param aweSessionObjectFactory
   * @return Session processor bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SessionProcessor sessionProcessor(final ObjectFactory<AweSession> aweSessionObjectFactory) {
    return new SessionProcessor(aweSessionObjectFactory);
  }

  /////////////////////////////////////////////
  // ANNOTATIONS
  /////////////////////////////////////////////

  /**
   * Audit annotation
   * @return Audit annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AuditAnnotation auditAnnotation() {
    return new AuditAnnotation();
  }

  /**
   * Crypto annotation
   * @return Crypto annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public CryptoAnnotation cryptoAnnotation() {
    return new CryptoAnnotation();
  }

  /**
   * Download Annotation
   * @return Download Annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public DownloadAnnotation downloadAnnotation() {
    return new DownloadAnnotation();
  }

  /**
   * GoTo Annotation
   * @return GoTo Annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public GoToAnnotation goToAnnotation() {
    return new GoToAnnotation();
  }

  /**
   * Hash Annotation
   * @return Hash Annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public HashAnnotation hashAnnotation() {
    return new HashAnnotation();
  }

  /**
   * Locale Annotation
   * @param localeProcessor Locale processor
   * @return Locale Annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public LocaleAnnotation localeAnnotation(LocaleProcessor localeProcessor) {
    return new LocaleAnnotation(localeProcessor);
  }

  /**
   * Session Annotation
   * @param sessionProcessor Session processor
   * @return Session annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SessionAnnotation sessionAnnotation(SessionProcessor sessionProcessor) {
    return new SessionAnnotation(sessionProcessor);
  }
}

