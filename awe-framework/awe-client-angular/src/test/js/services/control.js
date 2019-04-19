describe('awe-framework/awe-client-angular/src/test/js/services/control.js', function() {
  let $injector, $control, $utilities, $storage, $log;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $control = $injector.get('Control');
      $utilities = $injector.get('AweUtilities');
      $storage = $injector.get('Storage');
      $log = $injector.get('$log');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Get address type
  it('should retrieve address type', function() {
    // Assert
    expect($control.getAddressType({component: "tutu", view: "lala", column: "Des", row: "2"})).toBe("cell");
    expect($control.getAddressType({component: "tutu", view: "lala"})).toBe("viewAndComponent");
    expect($control.getAddressType({component: "tutu"})).toBe("component");
    expect($control.getAddressType({})).toBe("invalid");
  });

  // Get target
  it('should retrieve a target from storage', function() {
    // Mock
    spyOn($storage, "has").and.returnValue(true);
    spyOn($storage, "get").and.returnValue({"lala":{"tutu":{"cells":{"tutu":"epa"}}, "otro":{}}});
    spyOn($utilities, "getCellId").and.returnValue("tutu");
    spyOn(_, "each").and.callThrough();

    // Assert
    expect($control.getTarget({component: "tutu", view: "lala", column: "Des", row: "2"}, "model")).toEqual("epa");
    expect($control.getTarget({component: "tutu", view: "lala"}, "model")).toEqual({cells:{tutu:"epa"}});
    expect($control.getTarget({component: "tutu"}, "model")).toEqual({cells:{tutu:"epa"}});
    expect($control.getTarget({}, "model")).toBe(null);
  });

  // Get target
  it('should retrieve a target from storage but storage has not the key', function() {
    // Mock
    spyOn($storage, "has").and.returnValue(false);
    spyOn($storage, "get").and.returnValue({"lala":{"tutu":{"cells":{"tutu":"epa"}}, "otro":{}}});
    spyOn($utilities, "getCellId").and.returnValue("tutu");

    // Assert
    expect($control.getTarget({component: "tutu", view: "lala", column: "Des", row: "2"}, "model")).toEqual(null);
    expect($control.getTarget({component: "tutu", view: "lala"}, "model")).toEqual(null);
    expect($control.getTarget({component: "tutu"}, "model")).toEqual(null);
    expect($control.getTarget({}, "model")).toBe(null);
  });

  // Get target
  it('should retrieve a target from storage but element is not found', function() {
    // Mock
    spyOn($storage, "has").and.returnValue(true);
    spyOn($storage, "get").and.returnValue({"lala":{"tutu":{"cells":{"tutu":"epa"}}, "otro":{}}});
    spyOn($utilities, "getCellId").and.returnValue("lolo");

    // Assert
    expect($control.getTarget({component: "tutu", view: "lala", column: "Des", row: "2"}, "model")).toEqual(null);
    expect($control.getTarget({component: "asda", view: "lala"}, "model")).toEqual(null);
    expect($control.getTarget({component: "wwww"}, "model")).toEqual(null);
    expect($control.getTarget({}, "model")).toBe(null);
  });
});