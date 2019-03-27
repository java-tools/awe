describe('awe-client-angular/src/test/js/services/comet.js', function() {
  let $injector, $utilities, $actionController, $comet;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $utilities = $injector.get('AweUtilities');
      $actionController = $injector.get('ActionController');
      $comet = $injector.get('Comet');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should tell if it is connected', function() {
    expect($comet.isConnected()).toBe(false);
  });

  it('should init', function() {
    spyOn($comet, "_connect");
    $comet.init();
    expect($comet._connect).toHaveBeenCalled();
  });

  it('should init with connection', function() {
    spyOn($comet, "getConnection").and.returnValue({});
    spyOn($comet, "_reconnect");
    $comet.init();
    expect($comet._reconnect).toHaveBeenCalled();
  });

  it('should connect', function() {
    let connection = {connect: (a, b, fn, c) => fn(), subscribe: (fn) => fn};
    spyOn($comet, "getConnection").and.returnValue(connection);
    spyOn(connection, "subscribe");
    $comet._connect();
    expect(connection.subscribe).toHaveBeenCalledTimes(2);
  });

  it('should connect with fails', function() {
    let connection = {connect: (a, b, fn, error) => error(), subscribe: (fn) => fn()};
    spyOn($comet, "getConnection").and.returnValue(connection);
    spyOn($comet, "_disconnect");
    $comet._connect();
    expect($comet._disconnect).toHaveBeenCalled();
  });

  it('should disconnect', function() {
    let connection = {disconnect: (fn) => fn()};
    spyOn($comet, "getConnection").and.returnValue(connection);
    spyOn($comet, "setConnection");
    $comet._disconnect();
    expect($comet.setConnection).toHaveBeenCalled();
  });

  it('should disconnect and reconnect', function() {
    let connection = {disconnect: (fn) => fn()};
    spyOn($comet, "getConnection").and.returnValue(connection);
    spyOn($comet, "_connect");
    $comet._reconnect();
    expect($comet._connect).toHaveBeenCalled();
  });

  it('should reconnect', function() {
    spyOn($comet, "init");
    $comet._reconnect();
    expect($comet.init).toHaveBeenCalled();
  });

  it('should manage a broadcast call', function() {
    spyOn($utilities, "decodeData").and.returnValue("");
    spyOn($actionController, "addActionList");
    $comet.manageBroadcast({});
    expect($actionController.addActionList).not.toHaveBeenCalled();
  });

  it('should manage a broadcast call with actions', function() {
    spyOn($utilities, "decodeData").and.returnValue([{}]);
    spyOn($actionController, "addActionList");
    $comet.manageBroadcast({});
    expect($actionController.addActionList).toHaveBeenCalled();
  });

  it('should close the connection', function() {
    spyOn($comet, "_disconnect");
    $comet._close();
    expect($comet._disconnect).toHaveBeenCalled();
  });
});