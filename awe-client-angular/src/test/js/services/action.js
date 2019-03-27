describe('awe-client-angular/src/test/js/services/action.js', function() {
  let $injector, Action, $log;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      Action = $injector.get('Action');
      $log = $injector.get('$log');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Show action info
  it('should show action information', function() {
    spyOn($log, "debug");
    new Action().showInfo();
    expect($log.debug).toHaveBeenCalled();
  });

  // Check action is alive
  it('should check action is alive', function() {
    expect(new Action().isAlive()).toBe(true);
  });

  // Check action accept
  it('should accept action', function() {
    let action = new Action();
    let deferred = action.attr("deferred");
    spyOn(deferred, "resolve");
    action.accept();
    expect(deferred.resolve).toHaveBeenCalled();
  });

  // Check action reject
  it('should reject action', function() {
    // Reject first
    let action = new Action();
    let deferred = action.attr("deferred");
    spyOn(deferred, "reject");
    action.reject();
    expect(deferred.reject).toHaveBeenCalled();

    // Reject with function
    action = new Action();
    action.onReject = () => null;
    spyOn(action, "onReject");
    action.reject();
    expect(action.onReject).toHaveBeenCalled();
  });

  // Check action abort
  it('should abort action', function() {
    let action = new Action();
    let deferred = action.attr("deferred");
    spyOn(deferred, "resolve");
    action.abort();
    expect(deferred.resolve).toHaveBeenCalled();
    expect(action.isAlive()).toBe(false);
  });

  // Check cancel abort
  it('should cancel action', function() {
    // Cancel first
    let action = new Action();
    action.cancel();
    expect(action.isAlive()).toBe(false);

    // Cancel with function
    action = new Action();
    action.onCancel = () => null;
    spyOn(action, "onCancel");
    action.cancel();
    expect(action.onCancel).toHaveBeenCalled();
  });
});