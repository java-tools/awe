package com.almis.awe.model.dao;

import com.almis.awe.model.component.XStreamSerializer;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.XMLFile;
import com.almis.awe.model.entities.XMLNode;
import com.almis.awe.model.entities.locale.Locales;
import com.almis.awe.model.util.data.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@Repository
@Log4j2
public class AweElementsDao {

  // Autowired services
  private XStreamSerializer serializer;
  private Environment environment;

  /**
   * Autowired constructor
   *
   * @param serializer Serializer
   */
  @Autowired
  public AweElementsDao(XStreamSerializer serializer, WebApplicationContext context) {
    this.serializer = serializer;
    this.environment = context.getEnvironment();
  }

  private static final String OK = " - OK";
  private static final String KO = " - NOT FOUND";
  private static final String READING = "Reading ''{0}''{1}";
  private static final String READING_FILES_FROM = "Reading files from ''{0}''{1}";
  private static final String ERROR_PARSING_XML = "Error parsing XML - '{}'";

  // Application files path
  @Value("${application.paths.application:application/}")
  private String applicationPath;

  // Application module prefix
  @Value("${modules.prefix:module.}")
  private String modulePrefix;

  // XML extension
  @Value("#{'${extensions.xml:.xml}'.split(',')}")
  private String xmlExtension;

  // Application modules
  @Value("#{'${modules.list:awe}'.split(',')}")
  private List<String> modules;

  /**
   * Read all XML files and store them in the component
   *
   * @param rootClass Root class
   * @param storage   Storage list
   * @param filePath  File path
   */
  @Async("contextlessTaskExecutor")
  public <T extends XMLFile, N extends XMLNode> Future<String> readXmlFilesAsync(Class<T> rootClass, Map<String, N> storage, String filePath) {
    readXmlFiles(rootClass, storage, filePath);
    return new AsyncResult<>(null);
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param rootClass Root class
   * @param storage   Storage list
   * @param filePath  File path
   */
  public <T extends XMLFile, N extends XMLNode> void readXmlFiles(Class<T> rootClass, Map<String, N> storage, String filePath) {
    List<String> resultList = new ArrayList<>();
    // For each module read XML files
    for (String module : modules) {
      // Read module file
      resultList.add(readModuleFile(rootClass, storage, applicationPath + environment.getProperty(modulePrefix + module) + filePath));
    }

    // Log results
    for (String result : resultList) {
      log.info(result);
    }
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param rootClass Root class
   * @param storage   Storage list
   * @param filePath  File path
   */
  public <T extends XMLFile, N extends XMLNode> String readModuleFile(Class<T> rootClass, Map<String, N> storage, String filePath) {
    try {
      // Unmarshall XML (if it exists)
      Resource resource = new ClassPathResource(filePath);
      if (resource.exists()) {
        XMLFile fullXml = fromXML(rootClass, resource.getInputStream());

        // Read all XML elements
        readXmlElements(fullXml, storage);
        return MessageFormat.format(READING, filePath, OK);
      }
    } catch (IOException exc) {
      log.error(ERROR_PARSING_XML, filePath, exc);
    }

    return MessageFormat.format(READING, filePath, KO);
  }

  /**
   * Read all XML elements and store them in the component
   *
   * @param fullXml Full xml object
   * @param storage Storage map
   */
  private <N extends XMLNode> void readXmlElements(XMLFile fullXml, Map<String, N> storage) {
    // Read XML Elements and store them
    List<N> elementList = fullXml.getBaseElementList();
    for (N element : elementList) {
      if (element != null && !storage.containsKey(element.getElementKey())) {
        // Store element list
        storage.put(element.getElementKey(), element);
      }
    }
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param clazz    File class
   * @param filePath File path
   * @param <T>      Class type
   * @return Xml file object
   */
  public <T> T readXmlFile(Class<T> clazz, String filePath) {
    T file = null;
    List<String> messageList = new ArrayList<>();
    // For each module read XML files
    for (String module : modules) {
      if (file == null) {
        // Get file path
        String modulePath = environment.getProperty(modulePrefix + module);
        String path = applicationPath + modulePath + filePath;
        file = readModuleXmlFile(clazz, path, messageList);
      }
    }

    // Log results
    for (String result : messageList) {
      log.info(result);
    }

    return file;
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param clazz    File class
   * @param filePath File path
   * @param <T>      Class type
   * @return Xml file object
   */
  private <T> T readModuleXmlFile(Class<T> clazz, String filePath, List<String> messageList) {
    T file = null;
    try {
      // Unmarshall XML
      Resource resource = new ClassPathResource(filePath);
      if (resource.exists()) {
        InputStream resourceInputStream = resource.getInputStream();
        file = fromXML(clazz, resourceInputStream);
        messageList.add(MessageFormat.format(READING, filePath, OK));
      } else {
        messageList.add(MessageFormat.format(READING, filePath, KO));
      }
    } catch (IOException exc) {
      log.error(ERROR_PARSING_XML, filePath, exc);
    }

    return file;
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param clazz   File class
   * @param path    Base directory path
   * @param storage Storage to keep readed files
   */
  @Async("contextlessTaskExecutor")
  public <T> Future<String> readFolderXmlFilesAsync(Class<T> clazz, String path, Map<String, T> storage) {
    readFolderXmlFiles(clazz, path, storage);
    return new AsyncResult<>(null);
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param clazz   File class
   * @param path    Base directory path
   * @param storage Storage to keep readed files
   */
  public <T> void readFolderXmlFiles(Class<T> clazz, String path, Map<String, T> storage) {
    List<String> resultList = new ArrayList<>();
    // For each module read XML files
    for (String module : modules) {
      // Get file path
      String modulePath = environment.getProperty(modulePrefix + module);
      String basePath = applicationPath + modulePath + path;
      resultList.addAll(readModuleFolderXmlFile(clazz, basePath, storage));
    }

    // Log results
    for (String result : resultList) {
      log.info(result);
    }
  }

  /**
   * Read all XML files and store them in the component
   *
   * @param clazz File class
   * @param path  Base directory path
   * @return Xml file object
   */
  public <T> List<String> readModuleFolderXmlFile(Class<T> clazz, String path, Map<String, T> storage) {
    List<String> resultList = new ArrayList<>();
    try {
      // Check resource path
      Resource basePathResource = new ClassPathResource(path);
      if (basePathResource.exists()) {
        // Read files from path
        PathMatchingResourcePatternResolver loader = new PathMatchingResourcePatternResolver();
        Resource[] resources = loader.getResources("classpath:" + path + "*" + xmlExtension);
        if (resources != null && resources.length > 0) {
          resultList.add(MessageFormat.format(READING_FILES_FROM, path, OK));
          for (Resource resource : resources) {
            String message = readXmlResourceFile(resource, clazz, path, storage);
            if (message != null) {
              resultList.add(message);
            }
          }
        }
      }
    } catch (IOException exc) {
      log.info(ERROR_PARSING_XML, path, exc);
    }
    return resultList;
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
  private <T> String readXmlResourceFile(Resource resource, Class<T> clazz, String basePath, Map<String, T> storage) throws IOException {
    if (resource.exists()) {
      String fileName = resource.getFilename().replace(xmlExtension, "");
      if (!storage.containsKey(fileName)) {
        storage.put(fileName, fromXML(clazz, resource.getInputStream()));
        return MessageFormat.format(READING, basePath + fileName + xmlExtension, OK);
      }
    }
    return null;
  }

  /**
   * Read a locale file asynchronously
   * @param basePath
   * @param language
   * @param localeList
   */
  @Async("contextlessTaskExecutor")
  public Future<String> readLocaleAsync(String basePath, String language, Map<String, Map<String, String>> localeList) {
    // Create a local storage and read the locales from all modules
    Map<String, Global> localeLanguage = new ConcurrentHashMap<>();
    readXmlFiles(Locales.class, localeLanguage, basePath);

    // Parse the read locales and store them on the final storage
    Map<String, String> localeTranslated = new ConcurrentHashMap<>();
    for (Global locale : localeLanguage.values()) {
      localeTranslated.put(locale.getName(), parseLocale(locale));
    }

    // Store the translated locale in localeList
    localeList.put(language, localeTranslated);

    return new AsyncResult<>(null);
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
   * Deserialize XML
   * @param clazz Object class
   * @param stream XML Stream
   * @param <T>
   * @return Object deserialized
   */
  private synchronized <T> T fromXML(Class<T> clazz, InputStream stream) {
    return serializer.getObjectFromXml(clazz, stream);
  }
}
