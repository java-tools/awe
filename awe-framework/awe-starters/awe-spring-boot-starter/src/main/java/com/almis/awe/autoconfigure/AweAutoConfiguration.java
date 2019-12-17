package com.almis.awe.autoconfigure;

import com.almis.ade.api.ADE;
import com.almis.awe.dao.InitialLoadDao;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.AweRequest;
import com.almis.awe.model.component.XStreamSerializer;
import com.almis.awe.model.dao.AweElementsDao;
import com.almis.awe.model.util.data.NumericUtil;
import com.almis.awe.model.util.data.QueryUtil;
import com.almis.awe.model.util.file.FileUtil;
import com.almis.awe.model.util.log.LogUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import com.almis.awe.service.*;
import com.almis.awe.service.connector.JavaConnector;
import com.almis.awe.service.connector.MicroserviceConnector;
import com.almis.awe.service.connector.RestConnector;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.service.data.builder.EnumBuilder;
import com.almis.awe.service.data.builder.ServiceBuilder;
import com.almis.awe.service.data.connector.maintain.MaintainLauncher;
import com.almis.awe.service.data.connector.maintain.ServiceMaintainConnector;
import com.almis.awe.service.data.connector.query.EnumQueryConnector;
import com.almis.awe.service.data.connector.query.QueryLauncher;
import com.almis.awe.service.data.connector.query.ServiceQueryConnector;
import com.almis.awe.service.report.ReportDesigner;
import com.almis.awe.service.report.ReportGenerator;
import com.almis.awe.service.screen.ScreenComponentGenerator;
import com.almis.awe.service.screen.ScreenConfigurationGenerator;
import com.almis.awe.service.screen.ScreenModelGenerator;
import com.almis.awe.service.screen.ScreenRestrictionGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;

/**
 * AWE Autoconfiguration
 */
@Configuration
@EnableCaching
public class AweAutoConfiguration {

  // Autowired beans
  private WebApplicationContext context;

  /**
   * Autowired constructor
   *
   * @param context     Context
   * @param environment Environment
   */
  @Autowired
  public AweAutoConfiguration(WebApplicationContext context, Environment environment) {
    this.context = context;

    // Initialize static utilities
    NumericUtil.init(environment);
    EncodeUtil.init(environment);
  }

  /**
   * Awe Request
   *
   * @return Request beans
   */
  @Bean
  @ConditionalOnMissingBean
  @RequestScope
  public AweRequest aweRequest() {
    return new AweRequest();
  }

  /**
   * Awe Elements
   *
   * @param logger Logger
   * @return Awe Elements bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AweElements aweElements(LogUtil logger, AweElementsDao elementsDao) {
    return new AweElements(context, logger, elementsDao);
  }

  /////////////////////////////////////////////
  // DAO
  /////////////////////////////////////////////

  /**
   * Awe Elements DAO
   *
   * @param serializer XStream serializer
   * @return Awe Elements bean
   */
  @Bean
  @ConditionalOnMissingBean
  public AweElementsDao aweElementsDao(XStreamSerializer serializer) {
    return new AweElementsDao(serializer, context);
  }

  /**
   * Initial load DAO
   *
   * @param queryService Query service
   * @return Initial load DATA ACCESS OBJECT
   */
  @Bean
  @ConditionalOnMissingBean
  public InitialLoadDao initialLoadDao(QueryService queryService) {
    return new InitialLoadDao(queryService);
  }

  /////////////////////////////////////////////
  // UTILITIES
  /////////////////////////////////////////////

  /**
   * Log utilities
   *
   * @return Log utilities bean
   */
  @Bean
  @ConditionalOnMissingBean
  public LogUtil logUtil() {
    return new LogUtil(context);
  }

  /**
   * Query utilities
   *
   * @return Query utilities bean
   */
  @Bean
  @ConditionalOnMissingBean
  public QueryUtil queryUtil() {
    return new QueryUtil();
  }

  /**
   * File utilities
   *
   * @return File utilities bean
   */
  @Bean
  @ConditionalOnMissingBean
  public FileUtil fileUtil() {
    return new FileUtil();
  }

  /////////////////////////////////////////////
  // SERVICES
  /////////////////////////////////////////////

  /**
   * Launcher service
   *
   * @return Launcher service
   */
  @Bean
  @ConditionalOnMissingBean
  public LauncherService launcherService() {
    return new LauncherService(context);
  }

  /**
   * Property service
   *
   * @param queryService            Query service
   * @param configurableEnvironment Configurable environment
   * @return Property service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public PropertyService propertyService(QueryService queryService, ConfigurableEnvironment configurableEnvironment) {
    return new PropertyService(queryService, configurableEnvironment);
  }

  /**
   * Init service
   *
   * @param launcherService Launcher service
   * @return Init service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public InitService initService(LauncherService launcherService) {
    return new InitService(launcherService);
  }

  /**
   * Action service
   *
   * @param launcherService Launcher service
   * @return Action service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ActionService actionService(LauncherService launcherService) {
    return new ActionService(launcherService);
  }

  /**
   * Query service
   *
   * @param queryLauncher Query launcher
   * @param queryUtil     Query utilities
   * @return Query service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public QueryService queryService(QueryLauncher queryLauncher, QueryUtil queryUtil) {
    return new QueryService(queryLauncher, queryUtil);
  }

  /**
   * Maintain service
   *
   * @param maintainLauncher Maintain launcher
   * @param accessService    Access service
   * @param queryUtil        Query utilities
   * @return Maintain service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public MaintainService maintainService(MaintainLauncher maintainLauncher, AccessService accessService, QueryUtil queryUtil) {
    return new MaintainService(maintainLauncher, accessService, queryUtil);
  }

  /**
   * Menu service
   *
   * @param queryService               Query service
   * @param screenRestrictionGenerator Screen Restriction generator
   * @param screenComponentGenerator   Screen component generator
   * @param initialLoadDao             Initial load service
   * @return Menu service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public MenuService menuService(QueryService queryService, ScreenRestrictionGenerator screenRestrictionGenerator,
                                 ScreenComponentGenerator screenComponentGenerator, InitialLoadDao initialLoadDao) {
    return new MenuService(queryService, screenRestrictionGenerator, screenComponentGenerator, initialLoadDao);
  }

  /**
   * Screen service
   *
   * @param menuService              Menu service
   * @param maintainService          Maintain service
   * @param screenComponentGenerator Screen component generator
   * @return Screen service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ScreenService screenService(MenuService menuService, MaintainService maintainService, ScreenComponentGenerator screenComponentGenerator, ApplicationEventPublisher eventPublisher) {
    return new ScreenService(menuService, maintainService, screenComponentGenerator, eventPublisher);
  }

  /**
   * File service
   *
   * @param broadcastService Broadcast service
   * @param fileUtil         File util
   * @param logger           Logger
   * @param request          Request
   * @return File service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public FileService fileService(BroadcastService broadcastService, FileUtil fileUtil, LogUtil logger, AweRequest request) {
    return new FileService(broadcastService, fileUtil, logger, request);
  }

  /**
   * Locale service
   *
   * @return Locale service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public LocaleService localeService() {
    return new LocaleService();
  }

  /**
   * Log service
   *
   * @param queryUtil Query utilities
   * @return Log service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public LogService logService(QueryUtil queryUtil) {
    return new LogService(queryUtil);
  }

  /**
   * Report service
   *
   * @param queryService    Query service
   * @param menuService     Menu service
   * @param reportGenerator Report generator
   * @return Report service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ReportService reportService(QueryService queryService, MenuService menuService, ReportGenerator reportGenerator) {
    return new ReportService(queryService, menuService, reportGenerator);
  }

  /**
   * Printer service
   *
   * @return Printer service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public PrinterService printerService() {
    return new PrinterService();
  }

  /**
   * System service
   *
   * @return System service bean
   */
  @Bean
  @ConditionalOnMissingBean
  public SystemService systemService() {
    return new SystemService();
  }

  /////////////////////////////////////////////
  // LAUNCHERS
  /////////////////////////////////////////////

  /**
   * Query launcher
   *
   * @return Query launcher bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public QueryLauncher queryLauncher() {
    return new QueryLauncher();
  }

  /**
   * Maintain launcher
   *
   * @return Maintain launcher bean
   */
  @Bean
  @ConditionalOnMissingBean
  @Scope("prototype")
  public MaintainLauncher maintainLauncher() {
    return new MaintainLauncher();
  }


  /////////////////////////////////////////////
  // GENERATORS
  /////////////////////////////////////////////

  /**
   * Screen restriction generator
   *
   * @return Screen restriction generator bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ScreenRestrictionGenerator screenRestrictionGenerator() {
    return new ScreenRestrictionGenerator();
  }

  /**
   * Screen configuration generator
   *
   * @return Screen configuration generator bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ScreenConfigurationGenerator screenConfigurationGenerator() {
    return new ScreenConfigurationGenerator();
  }

  /**
   * Screen model generator
   *
   * @param screenRestrictionGenerator Screen restriction generator
   * @param initialLoadDao             Initial load service
   * @return Screen model generator bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ScreenModelGenerator screenModelGenerator(ScreenRestrictionGenerator screenRestrictionGenerator,
                                                   InitialLoadDao initialLoadDao) {
    return new ScreenModelGenerator(screenRestrictionGenerator, initialLoadDao);
  }

  /**
   * Screen component generator
   *
   * @param request                      Request
   * @param screenModelGenerator         Screen model
   * @param screenConfigurationGenerator Screen configuration
   * @param initialLoadDao               Initial load service
   * @return Screen component generator bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ScreenComponentGenerator screenComponentGenerator(AweRequest request, ScreenModelGenerator screenModelGenerator,
                                                           ScreenConfigurationGenerator screenConfigurationGenerator,
                                                           InitialLoadDao initialLoadDao) {
    return new ScreenComponentGenerator(request, screenModelGenerator, screenConfigurationGenerator, initialLoadDao);
  }

  /////////////////////////////////////////////
  // REPORTING
  /////////////////////////////////////////////

  /**
   * Report generator
   *
   * @param reportDesigner Report designer
   * @param ade            ADE Api
   * @return Report generator bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ReportGenerator reportGenerator(ReportDesigner reportDesigner, ADE ade) {
    return new ReportGenerator(reportDesigner, ade);
  }

  /**
   * Report designer
   *
   * @param queryService Query service
   * @return Report designer bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ReportDesigner reportDesigner(QueryService queryService) {
    return new ReportDesigner(queryService);
  }

  /////////////////////////////////////////////
  // CONNECTORS
  /////////////////////////////////////////////

  /**
   * Java connector
   *
   * @return Java connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public JavaConnector javaConnector() {
    return new JavaConnector();
  }

  /**
   * Microservice connector
   *
   * @param logUtil        logger
   * @param requestFactory Request factory
   * @return Microservice connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public MicroserviceConnector microserviceConnector(LogUtil logUtil, ClientHttpRequestFactory requestFactory) {
    return new MicroserviceConnector(logUtil, requestFactory);
  }

  /**
   * REST connector
   *
   * @param logUtil        logger
   * @param requestFactory Request factory
   * @return REST connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public RestConnector restConnector(LogUtil logUtil, ClientHttpRequestFactory requestFactory) {
    return new RestConnector(logUtil, requestFactory);
  }

  /**
   * EnumQuery connector
   *
   * @param queryUtil Query utilities
   * @return EnumQuery connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public EnumQueryConnector enumQueryConnector(QueryUtil queryUtil) {
    return new EnumQueryConnector(queryUtil);
  }

  /**
   * Service Query connector
   *
   * @param queryUtil Query utilities
   * @return Service Query connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ServiceQueryConnector serviceQueryConnector(QueryUtil queryUtil) {
    return new ServiceQueryConnector(queryUtil);
  }

  /**
   * Service Maintain connector
   *
   * @return Service Maintain connector bean
   */
  @Bean
  @ConditionalOnMissingBean
  public ServiceMaintainConnector serviceMaintainConnector() {
    return new ServiceMaintainConnector();
  }

  /////////////////////////////////////////////
  // BUILDERS
  /////////////////////////////////////////////

  /**
   * Data list builder
   *
   * @return Data list builder bean
   */
  @Bean
  @Scope("prototype")
  public DataListBuilder dataListBuilder() {
    return new DataListBuilder();
  }

  /**
   * Enum builder
   *
   * @return Enum builder bean
   */
  @Bean
  @Scope("prototype")
  public EnumBuilder enumBuilder() {
    return new EnumBuilder();
  }

  /**
   * Service builder
   *
   * @param launcherService Launcher service
   * @return Service builder bean
   */
  @Bean
  @Scope("prototype")
  public ServiceBuilder serviceBuilder(LauncherService launcherService, QueryUtil queryUtil) {
    return new ServiceBuilder(launcherService, queryUtil);
  }
}
