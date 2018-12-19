package com.almis.awe.autoconfigure;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.service.HelpService;
import com.almis.awe.service.MenuService;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * Manage template configuration
 */
@Configuration
@Order(HIGHEST_PRECEDENCE)
public class TemplateConfig {

  // Application modules
  @Value("#{'${modules.list:awe}'.split(',')}")
  private List<String> modules;

  // Application module prefix
  @Value("${modules.prefix:module.}")
  private String modulePrefix;

  // Application files path
  @Value("${application.paths.application:application/}")
  private String applicationPath;

  // Template path
  @Value("${application.paths.templates:templates/}")
  private String templatePath;

  // Autowired services
  private Environment environment;

  // HTML Extension
  @Value("${extensions.html:.html}")
  private String htmlExtension;

  /**
   * Autowired constructor
   * @param environment Environment
   */
  @Autowired
  public TemplateConfig(Environment environment) {
    this.environment = environment;
  }

  /**
   * Returns the list of paths found in all modules defined
   * 
   * @param filePath suffix of file
   * @return Path list
   */
  private List<String> getPaths(String filePath) {
    List<String> paths = new ArrayList<>();

    for (String module : modules) {
      String modulePath = environment.getProperty(modulePrefix + module) + AweConstants.FILE_SEPARATOR;
      String path = applicationPath + modulePath + templatePath + filePath;
      ClassPathResource resource = new ClassPathResource(path);
      if (resource.exists()) {
        paths.add("classpath:" + resource.getPath());
      }
    }

    return paths;
  }

  /**
   * Retrieve elements template group
   * 
   * @return Partials template group
   */
  @Bean
  public STGroup elementsTemplateGroup() {
    STGroup group = new STGroup('$', '$');
    for (String path : getPaths("screen/elements.stg")) {
      group.loadGroupFile("", path);
    }

    return group;
  }

  /**
   * Retrieve help template group
   * 
   * @return Partials template group
   */
  @Bean
  public STGroup helpTemplateGroup() {
    STGroup group = new STGroup('$', '$');
    for (String path : getPaths("screen/help.stg")) {
      group.loadGroupFile("", path);
    }

    return group;
  }

  /**
   * Retrieve screens template group
   * 
   * @return Partials template group
   */
  @Bean
  public STGroup screensTemplateGroup() {
    STGroup group = new STGroup('$', '$');
    for (String path : getPaths("screen/templates.stg")) {
      group.loadGroupFile("", path);
    }

    return group;
  }

  /////////////////////////////////////////////
  // SERVICES
  /////////////////////////////////////////////

  /**
   * Template service
   * @param menuService Menu service
   * @param elementsTemplateGroup Elements template group
   * @param helpTemplateGroup Help template group
   * @param screensTemplateGroup Screens template group
   * @param queryService Query service
   * @return Template service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public TemplateService templateService(MenuService menuService, STGroup elementsTemplateGroup, STGroup helpTemplateGroup, STGroup screensTemplateGroup,
                                         QueryService queryService) {
    return new TemplateService(menuService, elementsTemplateGroup, helpTemplateGroup, screensTemplateGroup, queryService);
  }

  /**
   * Help service
   * @param templateService Template service
   * @return Help service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public HelpService helpService(TemplateService templateService) {
    return new HelpService(templateService);
  }
}
