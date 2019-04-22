package com.almis.awe.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

/**
 * Class used to start AWE as a microservice
 */
@Configuration
@ConditionalOnProperty(name = "awe.microservice.enabled", havingValue = "true")
@EnableEurekaClient
public class MicroserviceConfig {
}
