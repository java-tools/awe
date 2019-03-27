describe('awe-client-angular/src/test/js/services/ajax.js', function() {
  let $injector, $ajax, $settings, $httpBackend, $loadingBar, $actionController, Action;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $ajax = $injector.get('Ajax');
      $settings = $injector.get('AweSettings');
      $httpBackend = $injector.get('$httpBackend');
      $loadingBar = $injector.get('LoadingBar');
      $actionController = $injector.get('ActionController');
      Action = $injector.get('Action');

      // Get settings
      spyOn($settings, "get").and.returnValue("");

      // backend definition common for all tests
      $httpBackend.when('GET', 'http://server/action/test').respond({});
      $httpBackend.when('POST', 'http://server/action/test').respond({});
      $httpBackend.when('POST', 'http://server/action/bad-test').respond(404, { foo: 'bar' });
      $httpBackend.when('POST', 'settings').respond({});
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Check connection
  it('should be connected', function() {
    expect($ajax.isConnected()).toBe(true);
  });

  // Should init
  it('should init', function() {
    $ajax.init();
  });

  // Send a message
  it('should send a message', function() {
    // Mock
    spyOn($ajax, "manageMessage");
    spyOn($settings, "settingsLoaded");
    spyOn($loadingBar, "endTask");
    spyOn($loadingBar, "startTask");

    // Launch
    let action = new Action();
    action.attr("silent", false);
    $ajax.sendMessage({values: {serverAction: "test"}, action: action});
    $httpBackend.flush();

    // Assert
    expect($ajax.manageMessage).toHaveBeenCalled();
    expect($loadingBar.endTask).toHaveBeenCalled();
    expect($loadingBar.startTask).toHaveBeenCalled();
  });

  // Send a message with error retrieval
  it('should send a silent message with error', function() {
    // Mock
    spyOn($ajax, "manageError");
    spyOn($settings, "settingsLoaded");
    spyOn($loadingBar, "endTask");
    spyOn($loadingBar, "startTask");

    // Launch
    let action = new Action();
    action.attr("silent", true);
    $ajax.sendMessage({target: "tuut", values: {serverAction: "bad-test"}, action: action});
    $httpBackend.flush();

    // Assert
    expect($ajax.manageError).toHaveBeenCalled();
    expect($loadingBar.endTask).toHaveBeenCalled();
    expect($loadingBar.startTask).not.toHaveBeenCalled();
  });

  // Should launch a get request
  it('should launch a get request', function() {
    // Mock
    spyOn($settings, "settingsLoaded");

    // Launch
    let request = $ajax.get("http://server/action/test", "application/json");

    // Assert
    request.then(response => expect(response.data).toEqual({}));
    $httpBackend.flush();
  });

  // Should launch a get request
  it('should launch a post request', function() {
    // Mock
    spyOn($settings, "settingsLoaded");

    // Launch
    let request = $ajax.post("http://server/action/test", {}, "application/json");

    // Assert
    request.then(response => expect(response.data).toEqual({}));
    $httpBackend.flush();
  });

  // Should serialize the parameters
  it('should get action urls', function() {
    // Launch
    let result = $ajax.getActionUrl("ACTION", "TARGET");

    // Assert
    expect(result).toEqual("http://server/action/ACTION/TARGET");
  });

  // Should serialize the parameters
  it('should serialize the parameters', function() {
    // Launch
    let parameters = {tutu: "lala"};
    let result = $ajax.serializeParameters(parameters);

    // Assert
    expect(result).toEqual(parameters);
  });

  // Should manage a message
  it('should manage a message', function() {
    // Mock
    spyOn($actionController, "addActionList");

    // Launch
    let message = {data: [{tutu: "lala"}]};
    let action = new Action();
    $ajax.manageMessage(message, action)

    // Assert
    expect($actionController.addActionList).toHaveBeenCalled();
  });

  // Should manage a message
  it('should manage a message with a dead action', function() {
    // Mock
    spyOn($actionController, "addActionList");

    // Launch
    let message = {data: {tutu: "lala"}};
    let action = new Action();
    action.destroy();
    $ajax.manageMessage(message, action)

    // Assert
    expect($actionController.addActionList).not.toHaveBeenCalled();
  });

  // Should manage a message
  it('should manage a message without an array', function() {
    // Mock
    spyOn($actionController, "addActionList");

    // Launch
    let message = {data: {tutu: "lala"}};
    let action = new Action();
    $ajax.manageMessage(message, action)

    // Assert
    expect($actionController.addActionList).not.toHaveBeenCalled();
  });

  // Should manage an error
  it('should manage an error', function() {
    // Mock
    spyOn($actionController, "addActionList");

    // Launch
    let message = {data: [{tutu: "lala"}]};
    let action = new Action();
    $ajax.manageError(message, action)

    // Assert
    expect($actionController.addActionList).toHaveBeenCalled();
  });

  // Should manage an error
  it('should manage an error without data', function() {
    // Mock
    spyOn($actionController, "addActionList");

    // Launch
    let message = {};
    let action = new Action();
    action.destroy();
    $ajax.manageError(message, action)

    // Assert
    expect($actionController.addActionList).toHaveBeenCalled();
  });
});