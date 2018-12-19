package com.almis.awe.annotation.entities.security;

import com.almis.awe.annotation.aspect.HashAnnotation;
import com.almis.awe.annotation.classload.SecurityAnnotationProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Hashing annotation to hash fields and input parameters
 *
 * @see SecurityAnnotationProcessor
 * @see HashAnnotation
 * @author dfuentes
 * Created by dfuentes on 15/03/2017.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Hash {

    /**
     * Possible hashing algorithms to choose from
     */
    enum HashingAlgorithm{
        MD5("MD5"),
        SHA_1("SHA-1"),
        SHA_256("SHA-256");

        private final String algorithm;
        HashingAlgorithm(String algorithm){
            this.algorithm = algorithm;
        }

        /**
         * Get selected algorithm name
         *
         * @return
         */
        public String getAlgorithm(){
            return this.algorithm;
        }
    }

    /**
     * Algorithm to use when hashing
     * @return
     */
    HashingAlgorithm algorithm() default HashingAlgorithm.SHA_256; //TODO make parametrizable?

    /**
     * Salt to apply when hashing
     *
     * @return
     */
    String salt() default "";//TODO add default salt from application property?
}
