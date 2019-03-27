describe('awe-client-angular/src/test/js/services/connection.js', function() {
  let $injector, $ajax, $connection;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $ajax = $injector.get('Ajax');
      $connection = $injector.get('Connection');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Send a message
  it('should send a message', function() {
    // Mock
    spyOn($ajax, "sendMessage");

    // Launch
    $connection.sendMessage({values: {serverAction: "test"}});

    // Assert
    expect($ajax.sendMessage).toHaveBeenCalled();
  });

  // Send a message
  it('should send', function() {
    // Mock
    spyOn($ajax, "send");

    // Launch
    $connection.send({values: {serverAction: "test"}});

    // Assert
    expect($ajax.send).toHaveBeenCalled();
  });

  // Send a get request
  it('should launch a get request', function() {
    // Mock
    spyOn($ajax, "get");

    // Launch
    $connection.get("http://server/action/test", "application/json");

    // Assert
    expect($ajax.get).toHaveBeenCalled();
  });

  // Send a post request
  it('should launch a post request', function() {
    // Mock
    spyOn($ajax, "post");

    // Launch
    $connection.post("http://server/action/test", {}, "application/json");

    // Assert
    expect($ajax.post).toHaveBeenCalled();
  });

  // Serialize parameters
  it('should serialize parameters', function() {
    // Mock
    spyOn($ajax, "serializeParameters");

    // Launch
    $connection.serializeParameters({tutu:"lala"});

    // Assert
    expect($ajax.serializeParameters).toHaveBeenCalled();
  });
});