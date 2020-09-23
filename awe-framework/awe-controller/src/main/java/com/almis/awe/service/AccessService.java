package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweSession;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.security.EncodeUtil;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.almis.awe.model.constant.AweConstants.*;

/**
 * Manage application accesses
 */
public class AccessService extends ServiceConfig {

  @Value("${security.master.key:fdvsd4@sdsa08}")
  private String masterKey;

  @Value("${language.default:en}")
  private String defaultLanguage;

  @Value("${application.theme:sky}")
  private String defaultTheme;

  @Value("${jasypt.encryptor.algorithm:PBEWithMD5AndDES}")
  private String jasyptAlgorithm;

  @Value("${jasypt.encryptor.keyObtentionIterations:1000}")
  private Integer jasyptKeyObtentionIterations;

  @Value("${jasypt.encryptor.poolSize:1}")
  private Integer jasyptPoolSize;

  @Value("${jasypt.encryptor.providerName:SunJCE}")
  private String jasyptProviderName;

  @Value("${jasypt.encryptor.saltGeneratorClassname:org.jasypt.salt.RandomSaltGenerator}")
  private String jasyptSaltGeneratorClassname;

  @Value("${jasypt.encryptor.stringOutputType:base64}")
  private String jasyptStringOutputType;

  // Autowire
  private final MenuService menuService;

  /**
   * Autowired constructor
   *
   * @param menuService menu service
   */
  public AccessService(MenuService menuService) {
    this.menuService = menuService;
  }

  /**
   * Performs the login action
   *
   * @return serviceData the result of the login
   * @throws AWException Error generating login
   */
  public ServiceData login() throws AWException {

    // Variable definition
    AweSession session = getSession();
    ServiceData serviceData = new ServiceData();
    AWException exc = (AWException) session.getParameter(SESSION_FAILURE);

    // Something failed
    if (exc != null) {
      session.setParameter(SESSION_FAILURE, null);
      exc.setType(AnswerType.WARNING);
      throw exc;
    }

    // User authenticated. No fails
    if (isAuthenticated()) {
      // Store screen parameters
      Menu menu = menuService.getMenu();
      String initialURL = "/" + menu.getScreenContext() + "/" + session.getParameter(SESSION_INITIAL_SCREEN);
      session.setParameter(SESSION_INITIAL_URL, initialURL);

      serviceData
        .addClientAction(new ClientAction("screen")
          .addParameter(SESSION_CONNECTION_TOKEN, UUID.randomUUID())
          .addParameter(JSON_SCREEN, initialURL))
        .addClientAction(new ClientAction("change-language")
          .addParameter(SESSION_LANGUAGE, session.getParameter(SESSION_LANGUAGE)))
        .addClientAction(new ClientAction("change-theme")
          .addParameter(SESSION_THEME, session.getParameter(SESSION_THEME)));
    }
    return serviceData;
  }

  /**
   * Performs the logout action
   *
   * @return serviceData the result of the login
   */
  public ServiceData logout() {
    // Return to home screen
    return new ServiceData()
      .addClientAction(new ClientAction("screen")
        .addParameter(SESSION_CONNECTION_TOKEN, UUID.randomUUID())
        .addParameter(JSON_SCREEN, "/"))
      .addClientAction(new ClientAction("change-language")
        .addParameter(SESSION_LANGUAGE, defaultLanguage))
      .addClientAction(new ClientAction("change-theme")
        .addParameter(SESSION_THEME, defaultTheme));
  }

  /**
   * Check if user is authenticated
   *
   * @return User is authenticated
   * @throws AWException Error checking authentication
   */
  public boolean isAuthenticated() throws AWException {
    return getSession().isAuthenticated();
  }

  /**
   * Get profile restriction list
   *
   * @return Profile restriction file list
   * @throws AWException Error retrieving profile list
   */
  public ServiceData getProfileNameFileList() throws AWException {
    // Get profiles file list
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();

    Set<String> profileList = getElements().getProfileList();
    for (String profile : profileList) {
      Map<String, CellData> row = new HashMap<>();
      // Set screen name
      row.put(AweConstants.JSON_VALUE_PARAMETER, new CellData(profile));

      // Store screen label
      row.put(AweConstants.JSON_LABEL_PARAMETER, new CellData(profile));

      // Store row
      dataList.addRow(row);
    }

    // Set records
    dataList.setRecords(dataList.getRows().size());

    // Sort results
    DataListUtil.sort(dataList, AweConstants.JSON_LABEL_PARAMETER, "asc");

    // Set datalist to service
    serviceData.setDataList(dataList);
    return serviceData;
  }

  /**
   * Encrypts a text parameter with algorithm RipEmd160
   *
   * @param textToEncrypt text to encrypt
   * @param phraseKey     phrase key
   * @return Service Data with text encrypted
   * @throws AWException Error
   */
  public ServiceData encryptText(String textToEncrypt, String phraseKey) throws AWException {

    // Encode the text
    String textEncripted = EncodeUtil.encryptRipEmd160WithPhraseKey(textToEncrypt, phraseKey);
    DataList dataList = new DataList();
    DataListUtil.addColumnWithOneRow(dataList, "encoded", textEncripted);
    return new ServiceData()
      .setDataList(dataList);
  }

  /**
   * Encrypts a text parameter with algorithm RipEmd160
   *
   * @param textToEncrypt text to encrypt
   * @param phraseKey     phrase key
   * @return Service Data with text encrypted
   */
  public ServiceData encryptProperty(String textToEncrypt, String phraseKey) {

    String key = masterKey;
    if (phraseKey != null && !phraseKey.isEmpty()) {
      key = phraseKey;
    }

    // Get encode bean
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword(key);
    config.setAlgorithm(jasyptAlgorithm);
    config.setKeyObtentionIterations(jasyptKeyObtentionIterations);
    config.setPoolSize(jasyptPoolSize);
    config.setProviderName(jasyptProviderName);
    config.setSaltGeneratorClassName(jasyptSaltGeneratorClassname);
    config.setStringOutputType(jasyptStringOutputType);
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    encryptor.setConfig(config);

    // Encode the text
    String textEncripted = "ENC(" + encryptor.encrypt(textToEncrypt) + ")";
    DataList dataList = new DataList();
    DataListUtil.addColumnWithOneRow(dataList, "encoded", textEncripted);
    return new ServiceData()
      .setDataList(dataList);
  }
}
