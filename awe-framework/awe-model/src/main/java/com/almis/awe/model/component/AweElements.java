package com.almis.awe.model.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dao.AweElementsDao;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.access.Profile;
import com.almis.awe.model.entities.actions.Action;
import com.almis.awe.model.entities.actions.Actions;
import com.almis.awe.model.entities.email.Email;
import com.almis.awe.model.entities.email.Emails;
import com.almis.awe.model.entities.enumerated.Enumerated;
import com.almis.awe.model.entities.enumerated.EnumeratedGroup;
import com.almis.awe.model.entities.maintain.Maintain;
import com.almis.awe.model.entities.maintain.Target;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.queries.Queries;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.entities.queues.Queue;
import com.almis.awe.model.entities.queues.Queues;
import com.almis.awe.model.entities.screen.Include;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.Tag;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.services.Service;
import com.almis.awe.model.entities.services.Services;
import com.almis.awe.model.type.LaunchPhaseType;
import com.almis.awe.model.util.log.LogUtil;
import lombok.NonNull;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.almis.awe.model.constant.AweConstants.*;

/**
 * @author pgarcia
 */
public class AweElements {

  // Autowired services
  private final WebApplicationContext context;
  private final LogUtil logger;
  private final AweElementsDao elementsDao;
  private final Environment environment;

  // Elements
  private Map<String, EnumeratedGroup> enumeratedList;
  private Map<String, Query> queryList;
  private Map<String, Queue> queueList;
  private Map<String, Target> maintainList;
  private Map<String, Email> emailList;
  private Map<String, Service> serviceList;
  private Map<String, Action> actionList;
  private Map<String, Profile> profileList;
  private Map<String, Menu> menuList;
  private Map<String, Screen> screenMap;

  // Application language list
  @Value("#{'${language.list:en,es,fr}'.split(',')}")
  private List<String> languages;

  // Default language
  @Value("${language.default:en}")
  private String defaultLanguage;

  // XML extension
  @Value("#{'${extensions.xml:.xml}'.split(',')}")
  private String xmlExtension;

  // Application files path
  @Value("${application.paths.application:application/}")
  private String applicationPath;

  // Global files path
  @Value("${application.paths.global:/global/}")
  private String globalPath;

  // Menu files path
  @Value("${application.paths.menu:/menu/}")
  private String menuPath;

  // Profile files path
  @Value("${application.paths.profile:/profile/}")
  private String profilePath;

  // Screen files path
  @Value("${application.paths.screen:/screen/}")
  private String screenPath;

  // Locale files path
  @Value("${application.paths.locale:/locale/}")
  private String localePath;

  // Enumerated file
  @Value("${application.files.enumerated:Enumerated}")
  private String enumeratedFileName;

  // Query file
  @Value("${application.files.query:Query}")
  private String queryFileName;

  // Queue file
  @Value("${application.files.queue:Queue}")
  private String queueFileName;

  // Maintain file
  @Value("${application.files.maintain:Maintain}")
  private String maintainFileName;

  // Email file
  @Value("${application.files.email:Email}")
  private String emailFileName;

  // Service file
  @Value("${application.files.service:Service}")
  private String serviceFileName;

  // Action file
  @Value("${application.files.action:Action}")
  private String actionFileName;

  // Public menu file
  @Value("${application.files.menu.public:public}")
  private String publicMenuFileName;

  // Private menu file
  @Value("${application.files.menu.private:private}")
  private String privateMenuFileName;

  // Locale file
  @Value("${application.files.locale:Locale-}")
  private String localeFileName;

  // Preload screens on startup
  @Value("${application.parameter.preload.screens:true}")
  private boolean preloadScreens;

  // Locale list
  private Map<String, Map<String, String>> localeList;

  private static final String NOT_FOUND = " not found: ";

  /**
   * Autowired constructor
   *
   * @param context Context
   * @param logger  Logger
   */
  public AweElements(WebApplicationContext context, LogUtil logger, AweElementsDao elementsDao) {
    this.context = context;
    this.logger = logger;
    this.elementsDao = elementsDao;
    this.environment = context.getEnvironment();
  }

  /**
   * Read all XML files and store them in the component
   */
  @PostConstruct
  public void init() {
    logger.log(AweElements.class, Level.INFO, LOG_LINE);
    logger.log(AweElements.class, Level.INFO, "----------------------------- AWE starting ... -----------------------------------");
    logger.log(AweElements.class, Level.INFO, LOG_LINE);

    // Initialize Awe Elements (Read all XML sources)
    logger.log(AweElements.class, Level.INFO, "=============================");
    logger.log(AweElements.class, Level.INFO, "===== Reading XML Files =====");
    logger.log(AweElements.class, Level.INFO, "=============================");

    // Initialize global files
    logger.log(AweElements.class, Level.INFO, " ===== Initializing global and screen files ===== ");
    waitForTermination(initGlobalFiles(), "global");
    logger.log(AweElements.class, Level.INFO, " ===== Finished loading global and screen files  ===== ");

    // Initialize menu files
    logger.log(AweElements.class, Level.INFO, " ===== Initializing menu files ===== ");
    initMenuFiles();
    logger.log(AweElements.class, Level.INFO, " ===== Finished loading menu files  ===== ");

    // Initialize locale files
    logger.log(AweElements.class, Level.INFO, " ===== Initializing locale files ===== ");
    waitForTermination(initLocaleFiles(), "locale");
    logger.log(AweElements.class, Level.INFO, " ===== Finished loading locale files  ===== ");
  }

  /**
   * Wait executor for termination
   *
   * @param results future results
   */
  protected void waitForTermination(List<Future<String>> results, String fileType) {
    for (Future<String> result : results) {
      try {
        result.get();
      } catch (InterruptedException | ExecutionException exc) {
        logger.log(AweElements.class, Level.ERROR, " ===== ERROR loading {0} Files  ===== ", exc, fileType);
        Thread.currentThread().interrupt();
      }
    }
  }

  /**
   * Read all XML files and store them in the component
   */
  private List<Future<String>> initGlobalFiles() {
    List<Future<String>> results = new ArrayList<>();

    // Init enumerated
    enumeratedList = new ConcurrentHashMap<>();
    String path = globalPath + enumeratedFileName + xmlExtension;
    results.add(elementsDao.readXmlFilesAsync(Enumerated.class, enumeratedList, path));

    // Init queries
    queryList = new ConcurrentHashMap<>();
    path = globalPath + queryFileName + xmlExtension;
    results.add(elementsDao.readXmlFilesAsync(Queries.class, queryList, path));

    // Init queues
    queueList = new ConcurrentHashMap<>();
    path = globalPath + queueFileName + xmlExtension;
    results.add(elementsDao.readXmlFilesAsync(Queues.class, queueList, path));

    // Init maintains
    maintainList = new ConcurrentHashMap<>();
    path = globalPath + maintainFileName + xmlExtension;
    results.add(elementsDao.readXmlFilesAsync(Maintain.class, maintainList, path));

    // Init emails
    emailList = new ConcurrentHashMap<>();
    path = globalPath + emailFileName + xmlExtension;
    results.add(elementsDao.readXmlFilesAsync(Emails.class, emailList, path));

    // Init service
    serviceList = new ConcurrentHashMap<>();
    path = globalPath + serviceFileName + xmlExtension;
    results.add(elementsDao.readXmlFilesAsync(Services.class, serviceList, path));

    // Init actions
    actionList = new ConcurrentHashMap<>();
    path = globalPath + actionFileName + xmlExtension;
    results.add(elementsDao.readXmlFilesAsync(Actions.class, actionList, path));

    // Init profiles
    profileList = new ConcurrentHashMap<>();
    results.add(elementsDao.readFolderXmlFilesAsync(Profile.class, profilePath, profileList));

    // Init screens
    screenMap = new ConcurrentHashMap<>();
    if (preloadScreens) {
      results.add(elementsDao.readFolderXmlFilesAsync(Screen.class, screenPath, screenMap));
    }

    return results;
  }

  /**
   * Read all XML files and store them in the component
   */
  private void initMenuFiles() {
    // Init menus in cache
    try {
      menuList = new ConcurrentHashMap<>();
      menuList.put(publicMenuFileName, readMenuFile(publicMenuFileName));
      menuList.put(privateMenuFileName, readMenuFile(privateMenuFileName));
    } catch (AWException exc) {
      logger.log(AweElements.class, Level.ERROR, "Error initializing menus", exc);
      exc.log();
    }
  }

  /**
   * Read all XML files and store them in the component
   */
  private List<Future<String>> initLocaleFiles() {
    List<Future<String>> results = new ArrayList<>();

    // Initialize locales
    localeList = new ConcurrentHashMap<>();
    // For each language read local files
    for (String language : languages) {
      // Get file name
      String fileName = localeFileName + language.toUpperCase();
      String path = localePath + fileName + xmlExtension;

      // Read locale file for language
      results.add(elementsDao.readLocaleAsync(path, language, localeList));
    }

    return results;
  }

  /**
   * Parse a locale
   *
   * @param locale Locale
   * @return locale parsed
   */
  public String parseLocale(Global locale) {
    return elementsDao.parseLocale(locale);
  }

  /**
   * Get an EnumeratedGroup with the identifier
   *
   * @param groupId Group identifier
   * @return The ENUMERATED group corresponding the groupId
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "enumerated", key = "#p0")
  public EnumeratedGroup getEnumerated(String groupId) throws AWException {
    try {
      // Clone from list
      return enumeratedList.get(groupId);
    } catch (Exception exc) {
      throw new AWException("Enumerated group" + NOT_FOUND + groupId, exc);
    }
  }

  /**
   * Get a Query with the identifier
   *
   * @param queryId Query identifier
   * @return The query corresponding the queryId
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "query", key = "#p0")
  public Query getQuery(String queryId) throws AWException {
    try {
      // Clone from list
      return queryList.get(queryId);
    } catch (Exception exc) {
      throw new AWException("Query" + NOT_FOUND + queryId, exc);
    }
  }

  /**
   * Get a Queue with the identifier
   *
   * @param queueId Queue id
   * @return The query corresponding the queryId
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "queue", key = "#p0")
  public Queue getQueue(String queueId) throws AWException {
    try {
      // Clone from list
      return queueList.get(queueId);
    } catch (Exception exc) {
      throw new AWException("Queue" + NOT_FOUND + queueId, exc);
    }
  }

  /**
   * Get a MAINTAIN operation with the identifier
   *
   * @param maintainId Maintain operation identifier
   * @return The MAINTAIN operation corresponding the maintainId
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "maintain", key = "#p0")
  public Target getMaintain(String maintainId) throws AWException {
    try {
      // Clone from list
      return maintainList.get(maintainId);
    } catch (Exception exc) {
      throw new AWException("Maintain" + NOT_FOUND + maintainId, exc);
    }
  }

  /**
   * Get a email operation with the identifier
   *
   * @param emailId Email operation identifier
   * @return The email operation corresponding the mntId
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "email", key = "#p0")
  public Email getEmail(String emailId) throws AWException {
    try {
      // Clone from list
      return emailList.get(emailId);
    } catch (Exception exc) {
      throw new AWException("Email" + NOT_FOUND + emailId, exc);
    }
  }

  /**
   * Get a service with the identifier
   *
   * @param serviceId Service identifier
   * @return The service corresponding the serviceId
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "service", key = "#p0")
  public Service getService(String serviceId) throws AWException {
    try {
      // Clone from list
      return serviceList.get(serviceId);
    } catch (Exception exc) {
      throw new AWException("Service" + NOT_FOUND + serviceId, exc);
    }
  }

  /**
   * Get an action with the identifier
   *
   * @param actionId Action identifier
   * @return The action corresponding the actionId
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "action", key = "#p0")
  public Action getAction(String actionId) throws AWException {
    try {
      // Clone from list
      return actionList.get(actionId);
    } catch (Exception exc) {
      throw new AWException("Action" + NOT_FOUND + actionId, exc);
    }
  }

  /**
   * Get an screen with the identifier
   *
   * @param screenId Screen identifier
   * @return The screen corresponding the screenId
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "screen", key = "#p0")
  public Screen getScreen(String screenId) throws AWException {
    Screen screen;
    if (screenMap.containsKey(screenId) && screenMap.get(screenId).getId() != null) {
      screen = screenMap.get(screenId);
    } else {
      // Get Action
      screen = readScreen(screenId, new HashSet<>());

      // Set screen identifier
      screen.setId(screenId);

      // Store screen
      setScreen(screen);
    }

    // Retrieve screen
    return screen;
  }

  /**
   * Inserts or updates given screen
   *
   * @param screen Screen
   * @return Screen
   */
  @CachePut(value = "screen", key = "#p0.getId()")
  public Screen setScreen(@NonNull Screen screen) {
    // Store screen
    screenMap.put(screen.getId(), screen);

    // Retrieve screen
    return screen;
  }

  /**
   * Retrieve a screen and store identifiers
   *
   * @param screenId        Screen identifier
   * @param includedScreens Included screens
   * @return Screen retrieved
   * @throws AWException Clone not supported
   */
  private Screen readScreen(String screenId, Set<String> includedScreens) throws AWException {
    Screen screen;
    int identifier = 1;
    String path = screenPath + screenId + xmlExtension;

    // Clone from list
    if (screenMap.containsKey(screenId)) {
      screen = screenMap.get(screenId);
    } else {
      screen = elementsDao.readXmlFile(Screen.class, path);
    }

    if (screen == null) {
      throw new AWException("Screen" + NOT_FOUND + screenId);
    }

    // Clone screen
    screen = screen.copy();

    // Set screen identifier
    List<Element> elements = screen.getElementsByType(Element.class);
    for (Element element : elements) {

      // Generate component key if not defined
      if (element instanceof Component && NO_KEY.equalsIgnoreCase(element.getElementKey())) {
        Component component = (Component) element;
        if (!NO_TAG.equalsIgnoreCase(component.getComponentTag())) {
          element.setId(screenId + "-" + component.getComponentTag() + "-" + (identifier++));
        }
      }

      // If component is an include, retrieve included screen and add it
      if (element instanceof Include) {
        includeScreen(screenId, (Include) element, new HashSet<>(includedScreens));
      }
    }

    // Get Action
    return screen;
  }

  /**
   * Include a nested screen
   *
   * @param screenId        Screen id
   * @param include         Include tag
   * @param includedScreens Included screens
   * @throws AWException Error retrieving screen or tag
   */
  private void includeScreen(String screenId, Include include, Set<String> includedScreens) throws AWException {
    if (include.getTargetScreen() != null && include.getTargetSource() != null) {
      String includeKey = include.getTargetScreen() + "-" + include.getTargetSource();
      if (!includedScreens.contains(includeKey)) {
        // Add the key to the included screens
        includedScreens.add(includeKey);

        // Retrieve include screen
        Screen includeScreen = readScreen(include.getTargetScreen(), new HashSet<>(includedScreens));

        // Retrieve include screen source
        Tag source = getScreenSource(includeScreen, include.getTargetSource());

        // Store tag element list in include
        if (source != null) {
          include.setElementList(source.getElementList());
        } else {
          throw new AWException(getLocale("ERROR_TITLE_BAD_INCLUDE_DEFINITION"),
            getLocale("ERROR_MESSAGE_BAD_INCLUDE_DEFINITION", screenId, include.getTargetScreen(), include.getTargetSource()));
        }
      } else {
        throw new AWException(getLocale("ERROR_TITLE_NESTED_INCLUDE"),
          getLocale("ERROR_MESSAGE_NESTED_INCLUDE", screenId, include.getTargetScreen(), include.getTargetSource()));
      }
    } else {
      throw new AWException(getLocale("ERROR_TITLE_BAD_INCLUDE_DEFINITION"),
        getLocale("ERROR_MESSAGE_BAD_INCLUDE_DEFINITION", screenId, include.getTargetScreen(), include.getTargetSource()));
    }
  }

  /**
   * Retrieve screen source tag
   *
   * @param screen Screen
   * @param source Source
   * @return Tag
   * @throws AWException Error retrieving screen sourcce
   */
  private Tag getScreenSource(Screen screen, String source) throws AWException {
    for (Tag child : screen.getChildrenByType(Tag.class)) {
      if (source.equalsIgnoreCase(child.getSource())) {
        return child.copy();
      }
    }
    return null;
  }

  /**
   * Get menu object
   *
   * @param menuId Menu name
   * @return Menu object
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "menu", key = "#p0")
  public Menu getMenu(String menuId) throws AWException {
    try {
      // Clone from list
      return menuList.get(menuId);
    } catch (Exception exc) {
      throw new AWException("Menu" + NOT_FOUND + menuId, exc);
    }
  }

  /**
   * Inserts or updates given menu
   *
   * @param menuId menu id
   * @param menu   Menu
   * @return Menu
   */
  @CachePut(value = "menu", key = "#p0")
  public Menu setMenu(String menuId, Menu menu) {
    // Store menu in list
    menuList.put(menuId, menu);

    // Retrieve added menu
    return menu;
  }

  /**
   * Get menu object
   *
   * @param menuId Menu name
   * @return Menu object
   * @throws AWException Clone not supported
   */
  private Menu readMenuFile(String menuId) throws AWException {
    Menu menu;
    String path = menuPath + menuId + xmlExtension;
    try {
      // Clone from list
      menu = elementsDao.readXmlFile(Menu.class, path);
      if (menu != null) {
        // Set menu identifier
        menu = (Menu) menu.copy().setId(menuId);
      }
    } catch (Exception exc) {
      throw new AWException("Menu" + NOT_FOUND + menuId, exc);
    }

    // Get Action
    return menu;
  }

  /**
   * Get profile object
   *
   * @param profile Profile name
   * @return Profile object
   * @throws AWException Clone not supported
   */
  @Cacheable(value = "profile", key = "#p0")
  public Profile getProfile(String profile) throws AWException {
    try {
      // Clone from list
      return profileList.get(profile);
    } catch (Exception exc) {
      throw new AWException("Profile" + NOT_FOUND + profile, exc);
    }
  }

  /**
   * Get available profile list
   *
   * @return Profile object
   */
  public Set<String> getProfileList() {
    return profileList.keySet();
  }

  /**
   * Get available screen list
   *
   * @return Screen list
   */
  public Set<String> getScreenList() {
    return screenMap.keySet();
  }

  /**
   * Returns all locales
   *
   * @return locales
   */
  public Map<String, Map<String, String>> getLocales() {
    return localeList;
  }

  /**
   * Retrieve language
   *
   * @return Language
   */
  public String getLanguage() {
    String language;
    try {
      AweSession session = context.getBean(AweSession.class);
      language = session.getParameter(String.class, AweConstants.SESSION_LANGUAGE);
      language = language == null ? defaultLanguage : language;
    } catch (Exception exc) {
      language = defaultLanguage;
    }
    return language;
  }

  /**
   * Returns a locale based on its identifier
   *
   * @param localeIdentifier Local identifier
   * @return Selected locale
   */
  public String getLocale(String localeIdentifier) {
    return getLocaleWithLanguage(localeIdentifier, getLanguage());
  }

  /**
   * Returns a locale based on its identifier
   *
   * @param localeIdentifier Local identifier
   * @return Selected locale
   */
  public String getLocale(String localeIdentifier, Object... tokenList) {
    return getLocaleWithLanguage(localeIdentifier, getLanguage(), tokenList);
  }

  /**
   * Returns a locale based on its identifier
   *
   * @param localeIdentifier Local identifier
   * @return Selected locale
   */
  @Cacheable(value = "locale", key = "{ #p0, #p1 }")
  public String getLocaleWithLanguage(String localeIdentifier, String language) {
    String locale = localeIdentifier;
    Map<String, String> locales = localeList.get(language.toLowerCase());

    // Check if locale exists, and retrieve it
    if (localeIdentifier != null && locales.containsKey(localeIdentifier)) {
      locale = locales.get(localeIdentifier);
    }

    // Get Action
    return locale;
  }

  /**
   * Returns a locale based on its identifier replacing a set of tokens by a string array
   *
   * @param localeIdentifier Local identifier
   * @param tokenList        Token list to replace
   * @return Selected locale
   */
  @Cacheable(value = "locale", key = "{ #p0, #p1, #p2.toString() }")
  public String getLocaleWithLanguage(String localeIdentifier, String language, Object... tokenList) {
    String locale = getLocaleWithLanguage(localeIdentifier, language);
    int index = 0;

    // Escape HTML
    for (Object token : tokenList) {
      if (token instanceof String) {
        String stringToken = (String) token;
        tokenList[index++] = StringEscapeUtils.escapeHtml4(stringToken);
      }
    }

    return MessageFormat.format(locale, tokenList);
  }

  /**
   * Retrieve a property value
   *
   * @param propertyIdentifier Property identifier
   * @return Selected locale
   */
  public String getProperty(String propertyIdentifier) {
    return environment.getProperty(propertyIdentifier);
  }

  /**
   * Retrieve a property value reading as a class
   *
   * @param propertyIdentifier propertyIdentifier Property identifier
   * @param tClass             Class to read the property value
   * @param <T>                property class
   * @return Selected locale
   */
  public <T> T getProperty(String propertyIdentifier, Class<T> tClass) {
    return environment.getProperty(propertyIdentifier, tClass);
  }

  /**
   * Retrieve a property value
   *
   * @param propertyIdentifier Property identifier
   * @param defaultValue       Default value
   * @return Selected locale
   */
  public String getProperty(String propertyIdentifier, String defaultValue) {
    String propertyValue = environment.getProperty(propertyIdentifier);
    if (propertyValue == null) {
      propertyValue = defaultValue;
    }
    return propertyValue;
  }

  /**
   * Get phase SERVICES
   *
   * @param phase Phase to look into
   * @return Start Service list
   * @throws AWException Error starting service
   */
  public List<Service> getPhaseServices(LaunchPhaseType phase) throws AWException {

    // Variable definition
    List<Service> phaseServices = new ArrayList<>();

    // Search from application file to awe file
    for (Service service : serviceList.values()) {
      if (phase.toString().equalsIgnoreCase(service.getLaunchPhase())) {
        phaseServices.add(service.copy());
      }
    }

    // Return object
    return phaseServices;
  }

  /**
   * Retrieve query map (query name + query object)
   *
   * @return Query map
   */
  public Map<String, Query> getQueryMap() {
    return new TreeMap<>(queryList);
  }

  /**
   * Retrieve maintain map (maintain name + maintain object)
   *
   * @return Maintain map
   */
  public Map<String, Target> getMaintainMap() {
    return new TreeMap<>(maintainList);
  }

  /**
   * Retrieve the application context
   *
   * @return Application context
   */
  public ApplicationContext getApplicationContext() {
    return context;
  }
}
