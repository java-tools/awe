package com.almis.awe.autoconfigure;

import com.almis.awe.listener.TemplateErrorListener;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.service.HelpService;
import com.almis.awe.service.MenuService;
import com.almis.awe.service.QueryService;
import com.almis.awe.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

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
  private List<STGroupFile> getPaths(String filePath) {
    List<STGroupFile> paths = new ArrayList<>();

    for (String module : modules) {
      String modulePath = environment.getProperty(modulePrefix + module) + AweConstants.FILE_SEPARATOR;
      String path = applicationPath + modulePath + templatePath + filePath;
      ClassPathResource resource = new ClassPathResource(path);
      if (resource.exists()) {
        paths.add(new STGroupFile(resource.getPath()));
      }
    }

    return paths;
  }

  /**
   * Define String Template group
   * @param errorListener Error listener to attach
   * @return Template group
   */
  private STGroup defineGroup(STErrorListener errorListener, String filePath) {
    STGroup group = new STGroup('$', '$');

    // Attach listener
    group.setListener(errorListener);

    // Retrieve group files
    for (STGroupFile file : getPaths(filePath)) {
      group.loadGroupFile("", file.url.toExternalForm());
    }

    return group;
  }

  /**
   * Retrieve template error listener
   *
   * @param logger Logger
   * @return Error listener
   */
  @Bean
  public STErrorListener templateErrorListener(LogUtil logger) {
    return new TemplateErrorListener(logger);
  }

  /**
   * Retrieve elements template group
   *
   * @param errorListener Error listener
   * @return Partials template group
   */
  @Bean("elementsTemplateGroup")
  public STGroup elementsTemplateGroup(STErrorListener errorListener) {
    return defineGroup(errorListener, "screen/elements.stg");
  }

  /**
   * Retrieve help template group
   * 
   * @return Partials template group
   */
  @Bean("helpTemplateGroup")
  public STGroup helpTemplateGroup(STErrorListener errorListener) {
    return defineGroup(errorListener, "screen/help.stg");
  }

  /**
   * Retrieve screens template group
   * 
   * @return Partials template group
   */
  @Bean("screensTemplateGroup")
  public STGroup screensTemplateGroup(STErrorListener errorListener) {
    return defineGroup(errorListener, "screen/templates.stg");
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
  public TemplateService templateService(MenuService menuService,
                                         @Qualifier("elementsTemplateGroup") STGroup elementsTemplateGroup,
                                         @Qualifier("helpTemplateGroup") STGroup helpTemplateGroup,
                                         @Qualifier("screensTemplateGroup") STGroup screensTemplateGroup,
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
