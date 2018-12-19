package com.almis.awe.annotation.autoconfigure;

import com.almis.awe.annotation.aspect.*;
import com.almis.awe.annotation.classload.SecurityAnnotationProcessor;
import com.almis.awe.annotation.processor.locale.LocaleProcessor;
import com.almis.awe.annotation.processor.security.CryptoProcessor;
import com.almis.awe.annotation.processor.session.SessionProcessor;
import com.almis.awe.model.util.file.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Class load annotation processor definitions
 *
 * Also enables Aspect definitions with Auto-proxying
 *
 * @see SecurityAnnotationProcessor
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
   * @return Locale processor bean
   */
  @Bean
  @ConditionalOnMissingBean
  public LocaleProcessor localeProcessor() {
    return new LocaleProcessor();
  }

  /**
   * Crypto processor
   * @return Crypto processor bean
   */
  @Bean
  @ConditionalOnMissingBean
  public CryptoProcessor cryptoProcessor() {
    return new CryptoProcessor();
  }

  /**
   * Session processor
   * @return Session processor bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SessionProcessor sessionProcessor() {
    return new SessionProcessor();
  }

  /**
   * Security annotation processor
   * @param cryptoProcessor Crypto processor
   * @return Security annotation processor bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SecurityAnnotationProcessor securityAnnotationProcessor(CryptoProcessor cryptoProcessor) {
    return new SecurityAnnotationProcessor(cryptoProcessor);
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
   * @param cryptoProcessor Crypto processor
   * @return Crypto annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public CryptoAnnotation cryptoAnnotation(CryptoProcessor cryptoProcessor) {
    return new CryptoAnnotation(cryptoProcessor);
  }

  /**
   * Download Annotation
   * @param fileUtil File utilities
   * @return Download Annotation bean
   */
  @Bean
  @ConditionalOnMissingBean
  public DownloadAnnotation downloadAnnotation(FileUtil fileUtil) {
    return new DownloadAnnotation(fileUtil);
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

