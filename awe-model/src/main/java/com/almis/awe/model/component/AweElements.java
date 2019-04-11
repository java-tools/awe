package com.almis.awe.model.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.entities.access.Profile;
import com.almis.awe.model.entities.actions.Action;
import com.almis.awe.model.entities.actions.Actions;
import com.almis.awe.model.entities.email.Email;
import com.almis.awe.model.entities.email.Emails;
import com.almis.awe.model.entities.enumerated.Enumerated;
import com.almis.awe.model.entities.enumerated.EnumeratedGroup;
import com.almis.awe.model.entities.locale.Locales;
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
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.model.util.log.LogUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.almis.awe.model.constant.AweConstants.NO_KEY;
import static com.almis.awe.model.constant.AweConstants.NO_TAG;

/**
 * @author pgarcia
 */
public class AweElements {

  // Autowired services
  private XStreamSerializer serializer;
  private WebApplicationContext context;
  private LogUtil logger;
  private Environment environment;

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

  // Application modules
  @Value("#{'${modules.list:awe}'.split(',')}")
  private List<String> modules;

  // Application module prefix
  @Value("${modules.prefix:module.}")
  private String modulePrefix;

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
  private String profilePath; // Profile files path

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

  // Locale list
  private Map<String, Map<String, String>> localeList;

  private static final String OK = " - OK";
  private static final String KO = " - NOT_FOUND";
  private static final String READING = "Reading ''{0}''";
  private static final String READING_FILES_FROM = "Reading files from ''{0}''";
  private static final String ERROR_PARSING_XML = "Error parsing XML - ''{0}''";
  private static final String NOT_FOUND = " not found: ";

  /**
   * Autowired constructor
   * @param streamSerializer Serializer
   * @param context Context
   * @param logger Logger
   */
  @Autowired
  public AweElements(XStreamSerializer streamSerializer, WebApplicationContext context, LogUtil logger) {
    this.serializer = streamSerializer;
    this.context = context;
    this.logger = logger;
    this.environment = context.getEnvironment();
  }

  /**
   * Read all XML files and store them in the component
   */
  public void init() {
    // Initialize global files
    logger.log(AweElements.class, Level.INFO, " ===== Initializing global files ===== ");
    initGlobalFiles();

    // Initialize menu files
    logger.log(AweElements.class, Level.INFO, " ===== Initializing menu files ===== ");
    initMenuFiles();

    // Initialize locale files
    logger.log(AweElements.class, Level.INFO, " ===== Initializing locale files ===== ");
    initLocaleFiles();
  }

  /**
   * Read all XML files and store them in the component
   */
  private void initGlobalFiles() {
    // Init enumerated
    enumeratedList = new ConcurrentHashMap<>();
    String path = globalPath + enumeratedFileName + xmlExtension;
    readXmlFiles(Enumerated.class, enumeratedList, path);

    // Init queries
    queryList = new ConcurrentHashMap<>();
    path = globalPath + queryFileName + xmlExtension;
    readXmlFiles(Queries.class, queryList, path);

    // Init queues
    queueList = new ConcurrentHashMap<>();
    path = globalPath + queueFileName + xmlExtension;
    readXmlFiles(Queues.class, queueList, path);

    // Init maintains
    maintainList = new ConcurrentHashMap<>();
    path = globalPath + maintainFileName + xmlExtension;
    readXmlFiles(Maintain.class, maintainList, path);

    // Init emails
    emailList = new ConcurrentHashMap<>();
    path = globalPath + emailFileName + xmlExtension;
    readXmlFiles(Emails.class, emailList, path);

    // Init service
    serviceList = new ConcurrentHashMap<>();
    path = globalPath + serviceFileName + xmlExtension;
    readXmlFiles(Services.class, serviceList, path);

    // Init actions
    actionList = new ConcurrentHashMap<>();
    path = globalPath + actionFileName + xmlExtension;
    readXmlFiles(Actions.class, actionList, path);

    // Init profiles
    profileList = new ConcurrentHashMap<>();
    readFolderXmlFiles(Profile.class, profilePath, profileList);

    // Init screen map
    screenMap = new ConcurrentHashMap<>();
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
  private void initLocaleFiles() {
    // Initialize locales
    localeList = new ConcurrentHashMap<>();
    // For each language read local files
    for (String language : languages) {
      // Get file name
      String fileName = localeFileName + language.toUpperCase();
      String path = localePath + fileName + xmlExtension;

      // Create a local storage and read the locales from all modules
      Map<String, Global> localeLanguage = new ConcurrentHashMap<>();
      readXmlFiles(Locales.class, localeLanguage, path);

      // Parse the read locales and store them on the final storage
      Map<String, String> localeTranslated = new ConcurrentHashMap<>();
      for (Global locale : localeLanguage.values()) {
        localeTranslated.put(locale.getName(), parseLocale(locale));
      }

      // Store the translated locale in localeList
      localeList.put(language, localeTranslated);
    }
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param rootClass Root class
   * @param storage   Storage list
   * @param filePath  File path
   */
  protected void readXmlFiles(Class rootClass, Map storage, String filePath) {
    // For each module read XML files
    for (String module : modules) {
      // Get file path
      String modulePath = environment.getProperty(modulePrefix + module);
      String path = applicationPath + modulePath + filePath;

      try {
        // Unmarshall XML (if it exists)
        Resource resource = new ClassPathResource(path);
        if (resource.exists()) {
          InputStream resourceInputStream = resource.getInputStream();
          XMLWrapper fullXml = (XMLWrapper) serializer.getObjectFromXml(rootClass, resourceInputStream);

          // Read all XML elements
          readXmlElements(fullXml, storage);
          logger.log(AweElements.class, Level.INFO, READING + OK, path);
        } else {
          logger.log(AweElements.class, Level.INFO, READING + KO, path);
        }
      } catch (IOException exc) {
        logger.log(AweElements.class, Level.ERROR, ERROR_PARSING_XML, exc, path);
      }
    }
  }

  /**
   * Read all XML elements and store them in the component
   *
   * @param fullXml Full xml object
   * @param storage Storage map
   */
  protected void readXmlElements(XMLWrapper fullXml, Map storage) {
    // Read XML Elements and store them
    List<XMLWrapper> elementList = fullXml.getBaseElementList();
    for (XMLWrapper element : elementList) {
      if (element != null && !storage.containsKey(element.getElementKey())) {
        // Set parent
        element.setParent(fullXml);

        // Store element list
        storage.put(element.getElementKey(), element);
      }
    }
  }

  /**
   * Read all XML files and store them in the component
   * @param clazz File class
   * @param filePath File path
   * @param <T> Class type
   * @return Xml file object
   */
  private <T> T readXmlFile(Class<T> clazz, String filePath) {
    T file = null;
    // For each module read XML files
    for (String module : modules) {
      if (file == null) {
        // Get file path
        String modulePath = environment.getProperty(modulePrefix + module);
        String path = applicationPath + modulePath + filePath;
        try {
          // Unmarshall XML
          Resource resource = new ClassPathResource(path);
          if (resource.exists()) {
            InputStream resourceInputStream = resource.getInputStream();
            file = (T) serializer.getObjectFromXml(clazz, resourceInputStream);
            logger.log(AweElements.class, Level.DEBUG, READING + OK, path);
          } else {
            logger.log(AweElements.class, Level.DEBUG, READING + KO, path);
          }
        } catch (IOException exc) {
          logger.log(AweElements.class, Level.ERROR, ERROR_PARSING_XML, exc, path);
        }
      }
    }
    return file;
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param clazz File class
   * @param path  Base directory path
   * @return Xml file object
   */
  private <T> Map<String, T> readFolderXmlFiles(Class<T> clazz, String path, Map storage) {

    // For each module read XML files
    for (String module : modules) {
      // Get file path
      String modulePath = environment.getProperty(modulePrefix + module);
      String basePath = applicationPath + modulePath + path;
      try {
        // Check resource path
        Resource basePathResource = new ClassPathResource(basePath);
        if (basePathResource.exists()) {
          // Read files from path
          PathMatchingResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
          Resource[] resources = loader.getResources("classpath:" + basePath + "*" + xmlExtension);
          if (resources != null && resources.length > 0) {
            logger.log(AweElements.class, Level.INFO, READING_FILES_FROM + OK, basePath);
            for (Resource resource : resources) {
              readXmlFile(resource, clazz, basePath, storage);
            }
          }
        } else {
          logger.log(AweElements.class, Level.INFO, READING_FILES_FROM + KO, basePath);
        }
      } catch (IOException exc) {
        logger.log(AweElements.class, Level.ERROR, ERROR_PARSING_XML, exc, basePath);
      }
    }
    return storage;
  }

  /**
   * Read XML file from a resource
   *
   * @param resource Resource
   * @param clazz    Resource class
   * @param basePath Base path
   * @param storage  Storage
   * @throws IOException Erorr reading resource
   */
  private void readXmlFile(Resource resource, Class clazz, String basePath, Map storage) throws IOException {
    if (resource.exists()) {
      String fileName = resource.getFilename().replace(xmlExtension, "");
      if (!storage.containsKey(fileName)) {
        InputStream resourceInputStream = resource.getInputStream();
        storage.put(fileName, serializer.getObjectFromXml(clazz, resourceInputStream));
        logger.log(AweElements.class, Level.INFO, READING + OK, basePath + fileName + xmlExtension);
      }
    }
  }

  /**
   * Parse a locale
   *
   * @param locale Locale
   * @return locale parsed
   */
  public String parseLocale(Global locale) {
    String localParsed = locale.getMarkdown();

    if (localParsed == null || localParsed.isEmpty()) {
      // Retrieve local value
      localParsed = locale.getValue();
    } else {
      // Retrieve local markdown and parse it into html
      localParsed = StringUtil.evalMarkdown(localParsed);
    }

    return localParsed;
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
    if (screenMap.containsKey(screenId)) {
      screen = screenMap.get(screenId);
    } else {
      // Get Action
      screen = readScreen(screenId, new HashSet());

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
  public Screen setScreen(@NotNull Screen screen) {
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
  private Screen readScreen(String screenId, Set includedScreens) throws AWException {
    Screen screen;
    Integer identifier = 1;
    String path = screenPath + screenId + xmlExtension;

    // Clone from list
    screen = readXmlFile(Screen.class, path);
    if (screen == null) {
      throw new AWException("Screen" + NOT_FOUND + screenId);
    }

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
        includeScreen(screenId, (Include) element, new HashSet(includedScreens));
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
  private void includeScreen(String screenId, Include include, Set includedScreens) throws AWException {
    if (include.getTargetScreen() != null && include.getTargetSource() != null) {
      String includeKey = include.getTargetScreen() + "-" + include.getTargetSource();
      if (!includedScreens.contains(includeKey)) {
        // Add the key to the included screens
        includedScreens.add(includeKey);

        // Retrieve include screen
        Screen includeScreen = readScreen(include.getTargetScreen(), new HashSet(includedScreens));

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
   * @throws AWException
   */
  private Tag getScreenSource(Screen screen, String source) throws AWException {
    Tag sourceTag = null;
    for (Element child : screen.getElementList()) {
      if (child.getSource().equalsIgnoreCase(source)) {
        return new Tag((Tag) child);
      }
    }
    return sourceTag;
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
   * @param menu Menu
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
      menu = readXmlFile(Menu.class, path);
      if (menu != null) {
        // Set menu identifier
        menu.setId(menuId);
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
  private String getLanguage() {
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
  @Cacheable(value = "locale", key = "#p0")
  public String getLocale(String localeIdentifier) {
    String language = getLanguage();
    String locale = localeIdentifier;
    Map<String, String> locales = localeList.get(language);

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
  @Cacheable(value = "locale", key = "{ #p0, #p1.toString() }")
  public String getLocale(String localeIdentifier, Object... tokenList) {
    String locale = getLocale(localeIdentifier);
    Integer index = 0;

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
   * @param propertyIdentifier Property identifier
   * @param tClass Class to read the property value
   * @return Selected locale
   */
  public<T> T getProperty(String propertyIdentifier, Class<T> tClass) {
    return environment.getProperty(propertyIdentifier, tClass);
  }

  /**
   * Retrieve a property value
   *
   * @param <T> Return value class
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
   */
  public List<Service> getPhaseServices(LaunchPhaseType phase) throws AWException {

    // Variable definition
    List<Service> phaseServices = new ArrayList<>();

    // Search from application file to awe file
    for (Service service : serviceList.values()) {
      if (phase.toString().equalsIgnoreCase(service.getLaunchPhase())) {
        phaseServices.add(new Service(service));
      }
    }

    // Return object
    return phaseServices;
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
