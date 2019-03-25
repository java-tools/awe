package com.almis.awe.model.constant;

import java.util.regex.Pattern;

/**
 * Application constants
 *
 * @author pgarcia
 */
public class AweConstants {

  private AweConstants() {
  }

  // TEXT
  public static final String MESSAGE = "message";
  public static final String ERROR = "error";

  // NUMBERS
  public static final int PREPARATION_TIME = 1;
  public static final int EXECUTION_TIME = 2;
  public static final int RESULTS_TIME = 3;


  // BASIC
  public static final String EXPIRED = "EXPIRED";
  public static final String APPLICATION_ENCODING = "UTF-8";
  public static final String NO_KEY = "NO_KEY";
  public static final String NO_TAG = "NO_TAG";
  public static final String COMMA_SEPARATOR = ",";
  public static final String FILE_SEPARATOR = "/";

  // Criteria query parameters
  public static final String CRITERIA_LABEL = "label";

  // Views
  public static final String BASE_VIEW = "base";
  public static final String REPORT_VIEW = "report";

  // Parameters
  public static final String PARAMETER_VIEW = "view";
  public static final String PARAMETER_OPTION = "option";
  public static final String PARAMETER_ADDRESS = "address";
  public static final String PARAMETER_DESTINATION = "destination";
  public static final String PARAMETER_TARGET = "target";
  public static final String PARAMETER_MESSAGE = MESSAGE;

  // Address
  public static final String ADDRESS_VIEW = "view";
  public static final String ADDRESS_COMPONENT = "component";
  public static final String ADDRESS_ROW = "row";
  public static final String ADDRESS_COLUMN = "column";
  public static final String ADDRESS_SESSION = "session";

  // Client actions
  public static final String CLIENT_ACTION_MESSAGE = MESSAGE;

  // Screen Templates
  public static final String TEMPLATE_TAG = "tag";
  public static final String TEMPLATE_INFO = "info";
  public static final String TEMPLATE_COMPONENT = "component";
  public static final String TEMPLATE_VIEW = "view";
  public static final String TEMPLATE_TAGLIST = "taglist";
  public static final String TEMPLATE_BREADCRUMB = "breadcrumb";
  public static final String TEMPLATE_EMPTY = "empty";
  public static final String TEMPLATE_CHILDREN = "children";
  public static final String TEMPLATE_TITLE = "title";
  public static final String TEMPLATE_E = "e";
  public static final String TEMPLATE_BREADCRUMBS = "breadcrumbs";
  public static final String TEMPLATE_LEVEL = "level";
  public static final String TEMPLATE_CONTENT = "content";
  public static final String TEMPLATE_MESSAGE = MESSAGE;


  // Help Templates
  public static final String HELP_TEMPLATE = "help";
  public static final String TEMPLATE_HELP = "help";
  public static final String EMPTY_TEMPLATE = "empty";
  public static final String TEMPLATE_HELP_APPLICATION = "helpApplication";
  public static final String TEMPLATE_HELP_OPTION = "helpOption";
  public static final String TEMPLATE_HELP_SCREEN = "helpScreen";
  public static final String TEMPLATE_HELP_CRITERION = "helpCriterion";
  public static final String TEMPLATE_HELP_BUTTON = "helpButton";
  public static final String TEMPLATE_HELP_BUTTON_GRID = "helpButtonGrid";
  public static final String TEMPLATE_HELP_COLUMN = "helpColumn";
  public static final String TEMPLATE_HELP_CHART = "helpChart";
  public static final String TEMPLATE_HELP_DIALOG = "helpDialog";
  public static final String TEMPLATE_HELP_GRID = "helpGrid";
  public static final String TEMPLATE_HELP_ELEMENT = "helpElement";
  public static final String TEMPLATE_HELP_EMPTY = "helpEmpty";
  public static final String TEMPLATE_HELP_CONTAINER = "helpContainer";

  // Error
  public static final String ERROR_SCREEN = ERROR;
  public static final String ERROR_OPTION = ERROR;
  public static final String ERROR_TEMPLATE = ERROR;

  // Data
  public static final String DATALIST_IDENTIFIER = "id";
  public static final Pattern DATALIST_COMPUTED_WILDCARD = Pattern.compile("\\[([\\w]*)\\]");
  public static final String DATALIST_STRING_TYPE = "STRING";
  public static final String DATALIST_STYLE_FIELD = "_style_";
  // Json
  public static final String JSON_LABEL_PARAMETER = "label";
  public static final String JSON_TEXT_PARAMETER = "text";
  public static final String JSON_VALUE_PARAMETER = "value";
  public static final String JSON_ICON_PARAMETER = "icon";
  public static final String JSON_IMAGE_PARAMETER = "image";
  public static final String JSON_STYLE_PARAMETER = "style";
  public static final String JSON_ID_PARAMETER = "id";
  public static final String JSON_SCREEN = "screen";
  public static final String JSON_OPTION = "option";
  public static final String JSON_RESTRICTED = "restricted";
  public static final String JSON_ALL = "all";
  public static final String JSON_VISIBLE_COLUMNS = "visibleColumns";
  public static final String JSON_PRINT_ORIENTATION = "visibleColumns";

  // Properties
  public static final String PROPERTY_SETTINGS_HEADER = "settings.";
  public static final String PROPERTY_SECURITY_MASTER_KEY = "security.master.key";
  public static final String PROPERTY_APPLICATION_ENCODING = "application.encoding";
  public static final String PROPERTY_NUMERIC_PATTERN_FORMATTED = "numeric.pattern.formatted";
  public static final String PROPERTY_NUMERIC_PATTERN_UNFORMATTED = "numeric.pattern.unformatted";
  public static final String PROPERTY_NUMERIC_FORMAT = "numeric.format";
  public static final String PROPERTY_NUMERIC_ROUND_TYPE = "numeric.round.type";
  public static final String PROPERTY_EMPTY_IF_NULL = "application.data.set.computed.empty.if.null";

  // Component parameters
  public static final String COMPONENT_SORT = "sort";
  public static final String COMPONENT_PAGE = "page";
  public static final String COMPONENT_MAX = "max";
  public static final String COMPONENT_DATABASE = "__database__";

  // Query variables
  public static final String QUERY_SORT = "_sort_";
  public static final String QUERY_PAGE = "_page_";
  public static final String QUERY_MAX = "_max_";

  // Sort attributes
  public static final String SORT_DIRECTION = "direction";

  // Session attributes
  public static final String SESSION_USER = "user";
  public static final String SESSION_FULLNAME = "fullname";
  public static final String SESSION_THEME = "theme";
  public static final String SESSION_LANGUAGE = "language";
  public static final String SESSION_INITIAL_SCREEN = "initialScreen";
  public static final String SESSION_LAST_SCREEN = "lastScreen";
  public static final String SESSION_CURRENT_SCREEN = "currentScreen";
  public static final String SESSION_KEEP_CRITERIA_HEADER = "keepCriteria-";
  public static final String SESSION_INITIAL_URL = "initialURL";
  public static final String SESSION_LAST_LOGIN = "lastLogin";
  public static final String SESSION_DATABASE = "database";
  public static final String SESSION_MODULE = "module";
  public static final String SESSION_PROFILE = "profile";
  public static final String SESSION_RESTRICTION = "restriction";
  public static final String SESSION_FAILURE = "sessionFailure";
  public static final String SESSION_CONNECTION_TOKEN = "token";
  public static final String SESSION_TOKEN = "sessionToken";
  public static final String SESSION_CONNECTION_HEADER = "Authorization";

  // Action variables
  public static final String ACTION_MESSAGE_TYPE = "MESSAGE_TYPE";
  public static final String ACTION_MESSAGE_TITLE = "MESSAGE_TITLE";
  public static final String ACTION_MESSAGE_DESCRIPTION = "MESSAGE_DESCRIPTION";
  public static final String ACTION_MESSAGE_TYPE_ATTRIBUTE = "type";
  public static final String ACTION_MESSAGE_TITLE_ATTRIBUTE = "title";
  public static final String ACTION_MESSAGE_DESCRIPTION_ATTRIBUTE = MESSAGE;
  public static final String ACTION_MESSAGE_RESULT_DETAILS = "MESSAGE_RESULT_DETAILS";
  public static final String ACTION_LANGUAGE = "LANGUAGE";
  public static final String ACTION_LOCALS = "LOCALS";
  public static final String ACTION_DATA = "DATA";
  public static final String ACTION_ROWS = "ROWS";
  public static final String ACTION_MENU_OPTIONS = "MENU_OPTIONS";
  public static final String ACTION_SCREEN_DATA = "SCREEN_DATA";
  public static final String ACTION_FILE_NAME = "FILE_NAME";
  public static final String ACTION_FILE_SIZE = "FILE_SIZE";
  public static final String ACTION_FILE_PATH = "FILE_PATH";
  public static final String ACTION_FILE_TYPE = "FILE_TYPE";

  // User parameters
  public static final String USER_FULLNAME = "fullname";
  public static final String USER_LANGUAGE = "language";
  public static final String USER_PROFILE = "profile";
  public static final String USER_THEME = "userTheme";
  public static final String PROFILE_THEME = "profileTheme";
  public static final String USER_INITIAL_SCREEN = "userInitialScreen";
  public static final String PROFILE_INITIAL_SCREEN = "profileInitialScreen";
  public static final String USER_RESTRICTION = "userRestriction";
  public static final String PROFILE_RESTRICTION = "profileRestriction";

  // Application parameters
  public static final String AWE_DATABASE_PROPERTIES = "aweDatabaseProperties";
  public static final String PARAMETER_NAME = "name";
  public static final String PARAMETER_VALUE = "value";

  // Querys
  public static final String USER_DETAIL_QUERY = "userDetails";
  public static final String SCREEN_CONFIGURATION_QUERY = "getScreenConfiguration";
  public static final String SCREEN_RESTRICTION_QUERY = "getScreenRestrictions";
  public static final String SCREEN_DATABASE_RESTRICTION_QUERY = "getOptionRestrictionFromDatabase";
  public static final String APPLICATION_PARAMETERS_QUERY = "getApplicationParameters";

  // Enumerated
  public static final String ATTRIBUTE_LIST = "ScrCnfAtrTyp";

  // Menu names
  public static final String PUBLIC_MENU = "public";
  public static final String PRIVATE_MENU = "private";

  // Service connectors
  public static final String JAVA_CONNECTOR = "javaConnector";
  public static final String MICROSERVICE_CONNECTOR = "microserviceConnector";
  public static final String REST_CONNECTOR = "restConnector";

  // Component default values
  public static final String CHECKBOX_DEFAULT_VALUE = "1";
  public static final String HIDDEN_COMPONENT = "hidden";

  // Email
  public static final String QUERY_GET_MAIL_SERVER_BY_USER = "GetEmlSrvByOpe";

  // DATABASE
  public static final String DATABASE_CONNECTIONS_QUERY = "DatabaseConnections";
  public static final String DATABASE_BEAN_TRANSLATION = "DatabaseBeanTranslation";

  // JMS
  public static final String JMS_CONNECTIONS_QUERY = "JmsConnections";

  // XML
  public static final String XMLNS = "http://www.w3.org/2001/XMLSchema-instance";
  public static final String XSD_LOCALES = "@awe.schemas@/locale.xsd";

  // Reporting
  public static final String PRINT_SCREEN = "PrnScr";
  public static final String PRINT_ACTION = "ActPrn";
  public static final String PRINT_TABS = "TypPrn";
  public static final String PRINT_DOUBLE_FORMATTING = "DblFmtPrn";
  public static final String PRINT_FORMATS = "FmtPrn";
  public static final String PRINT_DESTINATION_USERS = "UsrPrn";
  public static final String PRINT_ORIENTATION = "reportOrientation";
  public static final Float SVG_SCALE = 0.67f;
  public static final Float SVG_SCALE_LANDSCAPE = 0.30f;

  // Media types
  public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";
  public static final String APPLICATION_WORD = "application/vnd.ms-word";
  public static final String APPLICATION_RTF = "application/rtf";

  // Random algorithm
  public static final String RANDOM_ALGORITHM = "SHA1PRNG";
}
