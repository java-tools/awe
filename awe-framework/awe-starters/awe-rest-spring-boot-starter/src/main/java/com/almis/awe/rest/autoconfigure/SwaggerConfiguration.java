package com.almis.awe.rest.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration
 */
@Configuration
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true")
@EnableSwagger2
public class SwaggerConfiguration {
  /**
   * Configures how the API Rest documentation is generated, it uses the Swagger framework for this.
   *
   * @return Docket configuration object
   */
  @Bean
  public Docket apiDocumentGeneration() {
    return new Docket(DocumentationType.SWAGGER_2)
      .select()
      .apis(RequestHandlerSelectors.basePackage("com.almis.awe.rest"))
      .paths(PathSelectors.any())
      .build();
  }
}
