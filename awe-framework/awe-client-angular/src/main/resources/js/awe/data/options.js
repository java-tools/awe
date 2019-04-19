/**
 * Retrieve an uuid
 * @return {string} UUID
 */
export function getUID() {
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' + s4() + '-' + s4() + s4() + s4();
}

// Default settings
export const DefaultSettings = {
  // Paths
  pathServer: "",
  initialURL: "",
  // Globals
  language: null,
  theme: "default",
  screen: "",
  charset: "UTF-8",
  applicationName: "AWE (Almis Web Engine)",
  serverActionKey: "serverAction",
  targetActionKey: "targetAction",
  screenKey: "screen",
  optionKey: "option",
  dataSuffix: ".data",
  homeScreen: "home",
  recordsPerPage: 30,
  pixelsPerCharacter: 7,
  defaultComponentSize: "md",
  shareSessionInTabs: false,
  reloadCurrentScreen: false,
  suggestTimeout: 200,
  // Connection
  connectionProtocol: "AJAX",
  connectionTransport: "websocket",
  connectionBackup: "streaming",
  connectionTimeout: 300000,
  cometUID: getUID(),
  // Upload / Download
  uploadIdentifier: 'u',
  uploadMaxSize: 500 * 1024 * 1024,
  downloadIdentifier: 'd',
  addressIdentifier: 'address',
  // Security
  passwordPattern: ".*",
  minlengthPassword: 4,
  encodeTransmission: false,
  encodeKey: "p",
  tokenKey: "t",
  // Debug
  actionsStack: 0,
  debug: "INFO",
  // Screen loading
  loadingTimeout: 20000,
  // Help
  helpTimeout: 1000,
  // Messages
  messageTimeout: {
    info: 2000,
    error: 0,
    validate: 2000,
    help: 4000,
    warning: 4000,
    ok: 2000,
    wrong: 0,
    chat: 0
  },
  // Numeric options
  numericOptions: {
    aSep: ",",
    dGroup: '3',
    aDec: ".",
    aSign: '',
    pSign: 's',
    vMin: "-9999999999.99",
    vMax: "9999999999.99",
    mDec: null,
    mRound: "S",
    aPad: false,
    wEmpty: 'empty'
  },
  // Pivot options
  pivotOptions: {
    numGroup: 5000
  },
  // Chart options
  chartOptions: {
    limitPointsSerie: 1000000
  }
};

// Default spin options
export const DefaultSpin = {
  big: {
    // The number of lines to draw
    lines: 13,
    // The length of each line
    length: 20,
    // The line thickness
    width: 8,
    // The radius of the inner circle
    radius: 30,
    // Corner roundness (0..1)
    corners: 1,
    // The rotation offset
    rotate: 0,
    // 1: clockwise, -1: counterclockwise
    direction: 1,
    // #rgb or #rrggbb or array of colors
    color: '#000',
    // Rounds per second
    speed: 1,
    // Afterglow percentage
    trail: 60,
    // Whether to render a shadow
    shadow: true,
    // Whether to use hardware acceleration
    hwaccel: true,
    // The CSS class to assign to the spinner
    className: 'spinner',
    // The z-index (defaults to 2000000000)
    zIndex: 1010,
    // Top position relative to parent
    top: '50%',
    // Left position relative to parent
    left: '50%'
  },
  medium: {
    // The number of lines to draw
    lines: 11,
    // The length of each line
    length: 5,
    // The line thickness
    width: 1,
    // The radius of the inner circle
    radius: 4,
    // Corner roundness (0..1)
    corners: 1,
    // The rotation offset
    rotate: 0,
    // 1: clockwise, -1: counterclockwise
    direction: 1,
    // #rgb or #rrggbb or array of colors
    color: '#666',
    // Rounds per second
    speed: 1,
    // Afterglow percentage
    trail: 80,
    // Whether to render a shadow
    shadow: false,
    // Whether to use hardware acceleration
    hwaccel: true,
    // The CSS class to assign to the spinner
    className: 'spinner',
    // The z-index (defaults to 2000000000)
    zIndex: 100,
    // Top position relative to parent
    top: '50%',
    // Left position relative to parent
    left: '50%'
  },
  small: {
    // The number of lines to draw
    lines: 9,
    // The length of each line
    length: 3,
    // The line thickness
    width: 1,
    // The radius of the inner circle
    radius: 2,
    // Corner roundness (0..1)
    corners: 1,
    // The rotation offset
    rotate: 0,
    // 1: clockwise, -1: counterclockwise
    direction: 1,
    // #rgb or #rrggbb or array of colors
    color: '#666',
    // Rounds per second
    speed: 1,
    // Afterglow percentage
    trail: 80,
    // Whether to render a shadow
    shadow: false,
    // Whether to use hardware acceleration
    hwaccel: true,
    // The CSS class to assign to the spinner
    className: 'spinner',
    // The z-index (defaults to 2000000000)
    zIndex: 100,
    // Top position relative to parent
    top: '9px',
    // Left position relative to parent
    left: '8px'
  }
};

// Default grid values
export const DefaultGridOptions = {
};