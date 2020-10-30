package com.almis.awe.autoconfigure;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * Thymeleaf configuration.
 */
@Configuration
public class ThymeleafConfig {

  private static final String UTF8 = "UTF-8";
  private final WebApplicationContext applicationContext;

  @Value("${spring.thymeleaf.html.prefix:classpath:templates/}")
  private String htmlPrefix;

  @Value("${spring.thymeleaf.html.suffix:.html}")
  private String htmlSuffix;

  @Value("${spring.thymeleaf.html.cache:true}")
  private Boolean htmlCache;

  @Value("${spring.thymeleaf.css.prefix:classpath:static/css/}")
  private String cssPrefix;

  @Value("${spring.thymeleaf.js.prefix:classpath:static/js/}")
  private String jsPrefix;

  @Value("${spring.thymeleaf.css.cache:true}")
  private Boolean cssCache;

  @Value("${spring.thymeleaf.js.cache:true}")
  private Boolean jsCache;

  @Autowired
  public ThymeleafConfig(WebApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean("htmlTemplateResolver")
  public SpringResourceTemplateResolver htmlTemplateResolver() {
    SpringResourceTemplateResolver resolver = generateTemplateResolver(htmlPrefix, 0, htmlCache, TemplateMode.HTML);
    resolver.setSuffix(htmlSuffix);
    return resolver;
  }

  @Bean("cssTemplateResolver")
  public SpringResourceTemplateResolver cssTemplateResolver() {
    return generateTemplateResolver(cssPrefix, 1, cssCache, TemplateMode.CSS);
  }

  @Bean("javascriptTemplateResolver")
  public SpringResourceTemplateResolver javascriptTemplateResolver() {
    return generateTemplateResolver(jsPrefix, 2, jsCache, TemplateMode.JAVASCRIPT);
  }

  @Bean
  public ViewResolver htmlViewResolver(SpringTemplateEngine templateEngine) {
    return generateViewResolver(templateEngine, 0, "text/html", "*.html");
  }

  @Bean
  public ViewResolver cssViewResolver(SpringTemplateEngine templateEngine) {
    return generateViewResolver(templateEngine, 1, "text/css", "*.css");
  }

  @Bean
  public ViewResolver javascriptViewResolver(SpringTemplateEngine templateEngine) {
    return generateViewResolver(templateEngine, 2, "application/javascript", "*.js");
  }

  @Bean
  public SpringTemplateEngine templateEngine(ObjectProvider<ITemplateResolver> templateResolvers) {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    templateResolvers.orderedStream().forEach(engine::addTemplateResolver);
    return engine;
  }

  /**
   * Generate a template resolver
   *
   * @param prefix       Prefix
   * @param order        Order
   * @param cacheable    Cacheable
   * @param templateMode Template mode
   * @return Template resolver
   */
  private SpringResourceTemplateResolver generateTemplateResolver(String prefix, int order, boolean cacheable, TemplateMode templateMode) {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setOrder(order);
    resolver.setCheckExistence(true);
    resolver.setApplicationContext(applicationContext);
    resolver.setPrefix(prefix);
    resolver.setCacheable(cacheable);
    resolver.setTemplateMode(templateMode);
    return resolver;
  }

  /**
   * Generate a view resolver
   *
   * @param templateEngine Template engine
   * @param order          Order
   * @param contentType    Content type
   * @param viewNames      View names
   * @return View resolver
   */
  private ViewResolver generateViewResolver(SpringTemplateEngine templateEngine, int order, String contentType, String viewNames) {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine);
    resolver.setOrder(order);
    resolver.setContentType(contentType);
    resolver.setViewNames(new String[]{viewNames});
    resolver.setCharacterEncoding(UTF8);
    return resolver;
  }
}