package com.almis.awe.service;

import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWESessionException;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.access.Profile;
import com.almis.awe.model.entities.access.Restriction;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.component.panelable.Panelable;
import com.almis.awe.model.entities.screen.data.AweThreadInitialization;
import com.almis.awe.model.type.LoadType;
import com.almis.awe.model.type.RestrictionType;
import com.almis.awe.model.util.data.DataListUtil;
import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.service.data.builder.DataListBuilder;
import com.almis.awe.service.screen.ScreenComponentGenerator;
import com.almis.awe.service.screen.ScreenRestrictionGenerator;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.cache.annotation.CacheRemoveAll;
import java.util.*;
import java.util.concurrent.Future;

/**
 * Manage application Menus
 */
public class MenuService extends ServiceConfig {

  // Public menu
  @Value("${application.files.menu.public:public}")
  private String publicMenu;

  // Private menu
  @Value("${application.files.menu.private:private}")
  private String privateMenu;

  // Home screen
  @Value("${screen.configuration.home:home}")
  private String homeScreen;

  // Default restriction
  @Value("${security.default.restriction:general}")
  private String defaultRestriction;

  // Autowired services
  private QueryService queryService;
  private ScreenRestrictionGenerator screenRestrictionGenerator;
  private ScreenComponentGenerator screenComponentGenerator;
  private InitialLoadService initialLoadService;

  private static final String ERROR_TITLE_SCREEN_NOT_DEFINED = "ERROR_TITLE_SCREEN_NOT_DEFINED";

  /**
   * Autowired constructor
   * @param queryService Query service
   * @param screenRestrictionGenerator Screen restriction generator
   * @param screenComponentGenerator Screen component generator
   * @param initialLoadService Initial load service
   */
  @Autowired
  public MenuService(QueryService queryService, ScreenRestrictionGenerator screenRestrictionGenerator,
                     ScreenComponentGenerator screenComponentGenerator, InitialLoadService initialLoadService) {
    this.queryService = queryService;
    this.screenRestrictionGenerator = screenRestrictionGenerator;
    this.screenComponentGenerator = screenComponentGenerator;
    this.initialLoadService = initialLoadService;
  }

  /**
   * Retrieve the menu for the user
   *
   * @return Retrieved menu
   * @throws AWException Menu has not been found
   */
  public Menu getMenu() throws AWException {
    String menuId = publicMenu;
    try {
      boolean isAuthenticated = getSession().isAuthenticated();
      // Check if user is logged in
      if (isAuthenticated) {
        menuId = privateMenu;
      }
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_AUTHENTICATE"), getLocale("ERROR_MESSAGE_AUTHENTICATE"), exc);
    }

    return getMenu(menuId);
  }

  /**
   * Check if session has expired and user is trying to access a private option
   *
   * @return Session has expired
   * @throws AWException Menu has not been found
   */
  private boolean sessionExpired(String optionId) throws AWException {
    Menu menu = getMenu(privateMenu);
    return !getSession().isAuthenticated() && menu.getOptionByName(optionId) != null;
  }

  /**
   * Retrieve the menu for the user
   *
   * @return Retrieved menu
   * @throws AWException Menu has not been found
   */
  public Menu getMenuWithRestrictions() throws AWException {
    return getMenuWithRestrictions(getMenu());
  }

  /**
   * Retrieve the menu for the user
   *
   * @param menuType Menu type
   * @return Retrieved menu
   * @throws AWException Menu has not been found
   */
  public Menu getMenuWithRestrictions(String menuType) throws AWException {
    return getMenuWithRestrictions(getMenu(menuType));
  }

  /**
   * Retrieve the menu for the user
   * @param menu Menu
   * @return Retrieved menu
   * @throws AWException Menu has not been found
   */
  public Menu getMenuWithRestrictions(Menu menu) throws AWException {

    // Apply restrictions if logged in
    if (getSession().isAuthenticated()) {
      String module = getSession().getParameter(String.class, AweConstants.SESSION_MODULE);

      // Apply module restrictions
      screenRestrictionGenerator.applyModuleRestriction(module, menu);

      // Get restrictions
      ServiceData queryOutput = queryService.launchPrivateQuery(AweConstants.SCREEN_RESTRICTION_QUERY);
      DataList queryRestrictions = queryOutput.getDataList();

      // Apply restrictions
      screenRestrictionGenerator.applyScreenRestriction(queryRestrictions, menu);
    }

    return menu;
  }

  /**
   * Retrieve a menu
   *
   * @param menuId Menu identifier
   * @return Menu retrieved
   * @throws AWException Menu has not been found
   */
  public Menu getMenu(String menuId) throws AWException {
    // Retrieve menu
    Menu menu = getElements().getMenu(menuId).copy();
    menu.defineRelationship();
    return menu;
  }

  /**
   * Retrieve the menu default screen
   *
   * @return Default screen
   * @throws AWException Default screen has not been defined
   */
  public Screen getDefaultScreen() throws AWException {
    // Get screen from option
    Menu menu = getMenu();

    // Get default screen identifier
    String defaultScreenId = menu.getScreen();

    // Check if option has screen
    if (defaultScreenId == null) {
      throw new AWException(getLocale(ERROR_TITLE_SCREEN_NOT_DEFINED), getLocale("ERROR_MESSAGE_MENU_HAS_NOT_DEFAULT_SCREEN"));
    }

    // Retrieve screen
    return getScreen(defaultScreenId);
  }

  /**
   * Retrieve an option screen from the user menu
   *
   * @param optionId Option identifier
   * @return Screen retrieved
   * @throws AWException Screen has not been found
   */
  public Screen getOptionScreen(String optionId) throws AWException {
    String screenId;

    if (optionId.equalsIgnoreCase(homeScreen)) {
      // Get current menu
      Menu menu = getMenu();

      // Get home screen from menu
      screenId = menu.getScreen();
    } else {
      // Find option
      Option option = getOptionByName(optionId);

      // Get screen identifier
      screenId = option.getScreen();
    }

    // Check if option has screen
    if (screenId == null) {
      throw new AWException(getLocale(ERROR_TITLE_SCREEN_NOT_DEFINED), getLocale("ERROR_MESSAGE_OPTION_HAS_NOT_SCREEN", optionId));
    }

    // Retrieve screen
    return getScreen(screenId);
  }

  /**
   * Retrieve an option screen from the user menu
   *
   * @param optionId Option identifier
   * @return Screen retrieved
   * @throws AWException Screen has not been found
   */
  public Screen getAvailableOptionScreen(String optionId) throws AWException {
    String screenId;

    if (optionId.equalsIgnoreCase(homeScreen)) {
      // Get current menu
      Menu menu = getMenu();

      // Get home screen from menu
      screenId = menu.getScreen();
    } else {
      // Find option
      Option option = getAvailableOptionByName(optionId);

      // Get screen identifier
      screenId = option.getScreen();
    }

    // Check if option has screen
    if (screenId == null) {
      throw new AWException(getLocale(ERROR_TITLE_SCREEN_NOT_DEFINED), getLocale("ERROR_MESSAGE_OPTION_HAS_NOT_SCREEN", optionId));
    }

    // Retrieve screen
    return getScreen(screenId);
  }

  /**
   * Retrieve an screen
   *
   * @param screenId Screen identifier
   * @return Screen retrieved
   * @throws AWException Screen has not been found
   */
  public Screen getScreen(String screenId) throws AWException {
    // Check if option has screen
    if (screenId == null) {
      throw new AWException(getLocale(ERROR_TITLE_SCREEN_NOT_DEFINED), getLocale("ERROR_MESSAGE_SCREEN_NOT_DEFINED", screenId));
    }

    // Get screen
    Screen screen = getElements().getScreen(screenId).copy();

    // Add screen data
    if (!screen.isInitialized()) {
      initializeScreen(screen);
    }

    // Retrieve screen
    return screen;
  }

  /**
   * Initialize an screen
   *
   * @param screen Screen
   * @return Screen retrieved
   * @throws AWException Screen has not been found
   */
  private void initializeScreen(Screen screen) throws AWException {
    // Find all panelables and retrieve enumerated information
    Map<Panelable, Future<ServiceData>> resultMap = getPanelableMap(screen);

    // Assign result to each panelable
    updatePanelableData(resultMap);

    // Set screen as initialized
    screen.setInitialized(true);
    getElements().setScreen(screen);
  }

  /**
   * Initialize an screen
   *
   * @param screen Screen
   * @return Screen retrieved
   * @throws AWException Screen has not been found
   */
  private Map<Panelable, Future<ServiceData>> getPanelableMap(Screen screen) throws AWException {
    // Find all panelables and retrieve enumerated information
    List<Panelable> panelableList = screen.getElementsByType(Panelable.class);
    Map<Panelable, Future<ServiceData>> resultMap = new HashMap<>();

    // Generate a thread for each initialization
    for (Panelable panelable : panelableList) {
      if (panelable.getInitialLoad() != null) {
        // Launch
        Future<ServiceData> taskResult = initialLoadService.launchInitialLoad(new AweThreadInitialization()
                        .setInitialLoadType(LoadType.valueOf(panelable.getInitialLoad().toUpperCase()))
                        .setTarget(panelable.getTargetAction()));
        resultMap.put(panelable, taskResult);
      }
    }

    return resultMap;
  }

  /**
   * Update retrieved data into each panelable
   *
   * @param resultMap Result map
   */
  private void updatePanelableData(Map<Panelable, Future<ServiceData>> resultMap) {
    // Assign result to each panelable
    for (Map.Entry<Panelable, Future<ServiceData>> entry : resultMap.entrySet()) {
      Panelable panelable = entry.getKey();
      try {
        ServiceData result = entry.getValue().get();
        DataList componentData = (DataList) result.getVariableMap().get(AweConstants.ACTION_DATA).getObjectValue();
        Map<String, String> panelableData = new HashMap<>();
        // For each column, store value in components
        if (!componentData.getRows().isEmpty()) {
          for (Map<String, CellData> row : componentData.getRows()) {
            panelableData.put(row.get(AweConstants.JSON_VALUE_PARAMETER).getStringValue(), row.get(AweConstants.JSON_LABEL_PARAMETER).getStringValue());
          }
        }

        // Set data into panelable
        panelable.setTabValues(panelableData);
      } catch (Exception exc) {
        getLogger().log(MenuService.class, Level.ERROR, getLocale("ERROR_MESSAGE_RETRIEVING_INITIAL_DATA", panelable.getTargetAction()), exc);
      }
    }
  }

  /**
   * Retrieve an option screen from the user menu
   *
   * @param optionId Option identifier
   * @return Option retrieved
   * @throws AWException Option has not been found
   */
  public Option getOptionByName(String optionId) throws AWException {
    // Get screen from option
    Menu menu = getMenu();

    // Find option
    Option option = menu.getOptionByName(optionId);

    // If option hasn't been found, search in public menu
    if (option == null) {
      option = getMenu(publicMenu).getOptionByName(optionId);
    }

    // Check if option is defined
    if (option == null) {
      // Check if session expired
      if (sessionExpired(optionId)) {
        throw new AWESessionException(getLocale("ERROR_TITLE_SESSION_EXPIRED"), getLocale("ERROR_MESSAGE_SESSION_EXPIRED"));
      } else {
        throw new AWException(getLocale("ERROR_TITLE_OPTION_NOT_DEFINED"), getLocale("ERROR_MESSAGE_OPTION_HAS_NOT_BEEN_DEFINED", optionId));
      }
    }

    // Retrieve option
    return option;
  }

  /**
   * Retrieve an option screen from the user menu
   *
   * @param optionId Option identifier
   * @return Option retrieved
   * @throws AWException Option has not been found
   */
  public Option getAvailableOptionByName(String optionId) throws AWException {

    // Find option
    Option option = getAvailableOption(optionId);

    // Check if option is defined
    if (option == null) {
      throw new AWException(getLocale("ERROR_TITLE_OPTION_NOT_DEFINED"), getLocale("ERROR_MESSAGE_OPTION_HAS_NOT_BEEN_DEFINED", optionId));
    }

    // Retrieve option
    return option;
  }

  /**
   * Retrieve a list of available screens
   *
   * @param suggest Screen typed
   * @return Screen list retrieved
   * @throws AWException Option has not been found
   */
  public ServiceData getAvailableScreenList(String suggest) throws AWException {
    // Get screen from option
    Set<String> addedScreens = new HashSet<>();
    List<Option> optionList = getAllAvailableOptions();
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    for (Option option : optionList) {
      String screenId = option.getScreen();
      if (screenId != null && !addedScreens.contains(screenId)) {
        // Add to list
        addOptionToList(suggest, screenId, option, dataList, addedScreens);
      }
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
   * Add an option to the datalist
   * @param suggest Suggest to search
   * @param id Option/screen identifier
   * @param option Option
   * @param dataList Datalist to add data
   * @param previouslyAdded Previously added identifiers
   * @throws AWException
   */
  private void addOptionToList(String suggest, String id, Option option, DataList dataList, Set<String> previouslyAdded) throws AWException {
    Map<String, CellData> row = new HashMap<>();

    // Get screen label
    String label = option.getLabel();
    String screenId = option.getScreen();
    if (label == null && screenId != null) {
      Screen screen = getScreen(screenId);
      label = screen.getLabel();
    }

    // Get option label locale
    label = label == null ? id : getLocale(label) + " (" + id + ")";

    // Add screen if matches with screen or locale
    if (StringUtil.containsIgnoreCase(label, suggest.trim())) {
      // Set screen name
      row.put(AweConstants.JSON_VALUE_PARAMETER, new CellData(id));

      // Store screen label
      row.put(AweConstants.JSON_LABEL_PARAMETER, new CellData(label));

      // Store row
      dataList.addRow(row);
      previouslyAdded.add(id);
    }
  }

  /**
   * Check if address is valid
   *
   * @param address Option to check
   * @return Screen list retrieved
   * @throws AWException Option has not been found
   */
  public boolean checkOptionAddress(String address) throws AWException {
    // Step 1: Check if option is private
    String optionId = address.startsWith(AweConstants.JSON_SCREEN) ? address.substring(address.lastIndexOf('/') + 1, address.length()) : null;
    if (address.startsWith(AweConstants.JSON_SCREEN + "/" + AweConstants.PRIVATE_MENU)) {
      return getSession().isAuthenticated() && isAvailableOption(optionId, AweConstants.PRIVATE_MENU);
    } else if (address.startsWith(AweConstants.JSON_SCREEN + "/"))  {
      return isAvailableOption(optionId, AweConstants.PUBLIC_MENU);
    } else {
      return false;
    }
  }

  /**
   * Retrieve if option is available
   *
   * @param optionId Option to check
   * @param menuType Menu type
   * @return Screen list retrieved
   * @throws AWException Option has not been found
   */
  public boolean isAvailableOption(String optionId, String menuType) throws AWException {
    boolean isAvailable = false;
    // Get screen from option
    Menu menu = getMenuWithRestrictions(menuType);
    List<Option> optionList = menu.getElementsByType(Option.class);
    for (Option option : optionList) {
      if (!option.isRestricted() && optionId.equalsIgnoreCase(option.getName())) {
        isAvailable = true;
      }
    }
    return isAvailable;
  }

  /**
   * Retrieve a list of available modules
   *
   * @return Module list retrieved
   * @throws AWException Error retrieving module list
   */
  public ServiceData getModuleList() throws AWException {
    // Get screen from option
    Set<String> modules = new HashSet<>();
    List<Option> optionList = getAllOptions();
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    for (Option option : optionList) {
      String module = option.getModule();
      if (module != null && !modules.contains(module)) {
        Map<String, CellData> row = new HashMap<>();
        // Set screen name
        row.put(AweConstants.JSON_VALUE_PARAMETER, new CellData(module));

        // Store screen label
        row.put(AweConstants.JSON_LABEL_PARAMETER, new CellData(module));

        // Store row
        dataList.addRow(row);
        modules.add(module);
      }
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
   * Retrieve a list of available options
   *
   * @param suggest Option typed
   * @return Screen list retrieved
   * @throws AWException Option has not been found
   */
  public ServiceData getNameOptionList(String suggest) throws AWException {
    // Get screen from option
    Set<String> addedOptions = new HashSet<>();
    List<Option> optionList = getAllOptions();
    ServiceData serviceData = new ServiceData();
    DataList dataList = new DataList();
    for (Option option : optionList) {
      String optionName = option.getName();
      if (optionName != null && !addedOptions.contains(optionName)) {
        // Add the option to the list
        addOptionToList(suggest, optionName, option, dataList, addedOptions);
      }
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
   * Retrieve a list of available screens
   *
   * @param restriction Restriction profile
   * @return Screen restriction list
   * @throws AWException Screen restriction retrieval failure
   */
  public ServiceData getScreenRestrictions(String restriction) throws AWException {
    // Step 1: Get profile restrictions
    String restrictionToSearch = restriction == null || restriction.isEmpty() ? defaultRestriction : restriction;
    Profile baseProfile = getElements().getProfile(restrictionToSearch).copy();

    // Step 2: Generate a datalist from base profile
    DataList baseRestrictions = new DataList();
    if (baseProfile.getRestrictions() != null) {
      for (Restriction baseRestriction : baseProfile.getRestrictions()) {
        Map<String, CellData> row = new HashMap<>();
        // Set option name
        row.put(AweConstants.JSON_OPTION, new CellData(baseRestriction.getOption()));

        // Store screen label
        row.put(AweConstants.JSON_RESTRICTED, new CellData(RestrictionType.R.equals(baseRestriction.getRestrictionType())));
        baseRestrictions.addRow(row);
      }
    }
    // Set records
    baseRestrictions.setRecords(baseRestrictions.getRows().size());

    // Step 3: Get database restrictions and apply profile restrictions if it doesn't exist
    ServiceData serviceData = queryService.launchPrivateQuery(AweConstants.SCREEN_DATABASE_RESTRICTION_QUERY);
    DataList databaseRestrictions = serviceData.getDataList();

    // Step 4: Merge datalists
    DataList result = getBean(DataListBuilder.class).addDataList(baseRestrictions).addDataList(databaseRestrictions).build();

    // Set datalist to service
    serviceData.setDataList(result);
    return serviceData;
  }

  /**
   * Generates an array node with all options in the menu
   *
   * @return Menu options
   * @throws AWException Error generating menu
   */
  @CacheRemoveAll(cacheName = "profile")
  public ServiceData refreshMenu() throws AWException {
    ServiceData serviceData = new ServiceData();
    Menu menu = getMenuWithRestrictions();

    // Generate json data with model and controller
    try {
      // Apply option actions
      screenComponentGenerator.applyOptionActions(menu);
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_MENU"), getLocale("ERROR_MESSAGE_MENU_NOT_CLONEABLE"), exc);
    }

    // Store json data as javascript
    serviceData.addVariable(AweConstants.ACTION_MENU_OPTIONS, new CellData(menu.getElementList()));

    // Return service data
    return serviceData;
  }

  /**
   * Retrieve a list of available options
   *
   * @return Screen list retrieved
   * @throws AWException Option has not been found
   */
  private List<Option> getAllOptions() throws AWException {
    // Get options
    Menu menu = getMenu();
    List<Option> optionList = menu.getElementsByType(Option.class);

    // Add public menu options if authenticated
    if (getSession().isAuthenticated()) {
      menu = getMenu(AweConstants.PUBLIC_MENU);
      optionList.addAll(menu.getElementsByType(Option.class));
    }

    return optionList;
  }

  /**
   * Retrieve a list of available options
   *
   * @return Screen list retrieved
   * @throws AWException Option has not been found
   */
  private List<Option> getAllAvailableOptions() throws AWException {
    // Get options
    Menu menu = getMenuWithRestrictions();
    List<Option> optionList = menu.getElementsByType(Option.class);

    // Add public menu options if authenticated
    if (getSession().isAuthenticated()) {
      menu = getMenuWithRestrictions(AweConstants.PUBLIC_MENU);
      optionList.addAll(menu.getElementsByType(Option.class));
    }

    return filterRestrictedOptions(optionList);
  }

  /**
   * Retrieve a map of available options
   *
   * @return Screen list retrieved
   * @throws AWException Option has not been found
   */
  private Option getAvailableOption(String optionName) throws AWException {
    // Get options
    Option optionFound = null;
    List<Option> optionList = getAllAvailableOptions();

    // Add public menu options if authenticated
    for (Option option: optionList) {
      if (optionName.equalsIgnoreCase(option.getName())) {
        optionFound = option;
      }
    }

    return optionFound;
  }

  /**
   * Retrieve a list of available options
   * @param optionList Option list
   * @return Option list filtered
   */
  private List<Option> filterRestrictedOptions(List<Option> optionList) {
    // Get options
    List<Option> availableOptionList = new ArrayList<>();
    for (Option option: optionList) {
      if (!option.isRestricted()) {
        availableOptionList.add(option);
      }
    }

    return availableOptionList;
  }
}