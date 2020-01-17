describe('Action Controller singleton', function() {
  let $injector, $actionController, $settings, $utilities;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $actionController = $injector.get('ActionController');
      $settings = $injector.get('AweSettings');
      $utilities = $injector.get('AweUtilities');

      // Get settings
      spyOn($settings, "get").and.returnValue("");
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should finish an action', function() {
    // Mock
    let accept = jasmine.createSpy("accept");
    let reject = jasmine.createSpy("reject");
    let action = {accept: accept, reject: reject};

    // Launch and assert
    $actionController.finishAction(action, true);
    expect(action.accept).toHaveBeenCalled();

    $actionController.finishAction(action, false);
    expect(action.reject).toHaveBeenCalled();
  });

  it('should accept an action', function() {
    // Mock
    let accept = jasmine.createSpy("accept");
    let reject = jasmine.createSpy("reject");
    let action = {accept: accept, reject: reject};

    // Launch and assert
    $actionController.acceptAction(action);
    expect(action.accept).toHaveBeenCalled();
  });

  it('should reject an action', function() {
    // Mock
    let accept = jasmine.createSpy("accept");
    let reject = jasmine.createSpy("reject");
    let action = {accept: accept, reject: reject};

    // Launch and assert
    $actionController.rejectAction(action);
    expect(action.reject).toHaveBeenCalled();
  });

  it('should abort an action', function() {
    // Mock
    let accept = jasmine.createSpy("accept");
    let reject = jasmine.createSpy("reject");
    let abort = jasmine.createSpy("abort");
    let action = {accept: accept, reject: reject, abort: abort};

    // Launch and assert
    $actionController.abortAction(action);
    expect(action.abort).toHaveBeenCalled();
  });

  it('should resolve an action', function() {
    // Mock
    let accept = jasmine.createSpy("accept");
    let reject = jasmine.createSpy("reject");
    let abort = jasmine.createSpy("abort");
    let action = {accept: accept, reject: reject, abort: abort, attr: () => ({})};

    // Launch
    $actionController.resolveAction(action, {method: "lala", service: {"lala": () => null}});

    // Assert
    expect(action.accept).toHaveBeenCalled();
  });

  it("shouldn't resolve an action", function() {
    // Mock
    let accept = jasmine.createSpy("accept");
    let reject = jasmine.createSpy("reject");
    let abort = jasmine.createSpy("abort");
    let action = {accept: accept, reject: reject, abort: abort, attr: () => ({})};
    spyOn($utilities, "checkAddress").and.returnValue(false);

    // Launch
    $actionController.resolveAction(action, {method: "lala", service: {"lala": () => null}});

    // Assert
    expect(action.accept).not.toHaveBeenCalled();
  });

  it("manages null data on action list", function() {
    // Mock
    let accept = jasmine.createSpy("accept");
    let reject = jasmine.createSpy("reject");
    let abort = jasmine.createSpy("abort");
    let action = {accept: accept, reject: reject, abort: abort, attr: () => ({})};
    spyOn($actionController, "runNext").and.returnValue(false);

    // Launch
    $actionController.addActionList(null, true, {});

    // Assert
    expect($actionController.runNext).not.toHaveBeenCalled();
  });

  it("runs next action with action already running", function() {
    // Mock
    let accept = jasmine.createSpy("accept");
    let reject = jasmine.createSpy("reject");
    let abort = jasmine.createSpy("abort");
    let action = {accept: accept, reject: reject, abort: abort, attr: () => ({running: true, async: true})};
    $actionController.actionStackList[0] = [action];

    // Launch
    $actionController.runNext();
  });
});