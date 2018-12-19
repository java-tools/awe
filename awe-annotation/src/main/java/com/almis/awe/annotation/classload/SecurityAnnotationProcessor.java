package com.almis.awe.annotation.classload;

import com.almis.awe.annotation.entities.security.Crypto;
import com.almis.awe.annotation.entities.security.Hash;
import com.almis.awe.annotation.processor.security.CryptoProcessor;
import com.almis.awe.annotation.processor.security.HashProcessor;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

/**
 * Security annotations c processor class
 *
 * @see Crypto
 * @see Hash
 * @author dfuentes
 * Created by dfuentes on 15/03/2017.
 */
public class SecurityAnnotationProcessor extends ServiceConfig implements BeanPostProcessor {

  // Autowired services
    private CryptoProcessor cryptoProcessor;

  /**
   * Autowired constructor
   * @param cryptoProcessor Crypto processor
   */
  @Autowired
    public SecurityAnnotationProcessor(CryptoProcessor cryptoProcessor) {
      this.cryptoProcessor = cryptoProcessor;
    }

    /**
     * Process annotations before class load
     *
     * @param bean Bean
     * @param beanName Bean name
     * @return Object post processed
     */
    @Override
    public Object postProcessBeforeInitialization(final Object bean, String beanName)  {
      processHashAnnotation(bean);
      processCryptUtilAnnotation(bean);
      return bean;
    }

    /**
     * Process annotation after class load
     *
     * @param bean Bean
     * @param beanName Bean name
     * @return Object post processed
     */
    @Override
    public Object postProcessAfterInitialization(final Object bean, String beanName) {
        return bean;
    }

    /**
     * Process hash annotations
     *
     * @param bean Bean
     */
    private void processHashAnnotation(final Object bean) {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (field.getAnnotation(Hash.class) != null) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);

                Hash hash = field.getAnnotation(Hash.class);

                try {
                    field.set(bean, HashProcessor.processHashing(hash, (String) field.get(bean)));
                } catch (AWException e) {
                    getLogger().log(SecurityAnnotationProcessor.class, Level.ERROR,e.getMessage(),e);
                }

                field.setAccessible(isAccessible);
            }
        });
    }

    /**
     * Process Crypto annotation
     *
     * @param bean Bean
     */
    private void processCryptUtilAnnotation(final Object bean) {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            if (field.getAnnotation(Crypto.class) != null) {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Crypto annotation = field.getAnnotation(Crypto.class);

                field.set(bean, cryptoProcessor.processCrypto(annotation, (String) field.get(bean)));
                field.setAccessible(isAccessible);
            }
        });
    }
}
