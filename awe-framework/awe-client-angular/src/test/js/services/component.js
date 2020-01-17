describe('awe-framework/awe-client-angular/src/test/js/services/component.js', function () {
  let $rootScope, $injector, $control, $utilities, Component;
  let originalTimeout;
  let address = {"component": "comp1", "view": "report"};
  let scope = {view: "report", $parent: {$parent: {}}, $on: () => null, $emit: () => null};
  let controller = {visible: true};

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      $rootScope = $injector.get('$rootScope');
      $control = $injector.get('Control');
      $utilities = $injector.get('AweUtilities');
      Component = $injector.get('Component');

      spyOn($control, "checkComponent").and.returnValue(true);
      spyOn($control, "getAddressModel").and.returnValue({selected: null, model: []});
      spyOn($control, "getAddressController").and.returnValue(controller);
    }]);


    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function () {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Generate a component
  it('should generate a component', function () {
    // Prepare
    let comp = new Component(scope, "comp1");

    // Assert
    expect(comp.id).toEqual("comp1");
  });

  // Initialize a component
  it('should initialize a component', function () {
    // Prepare
    let comp = new Component(scope, "comp1");
    comp.init();

    // Assert
    expect(comp.address).toEqual(address);
  });

  // Check component visibility
  it('should check component visibility', function () {
    // Prepare
    controller["invisible"] = false;
    let comp = new Component(scope, "comp1");
    comp.init();

    // Assert
    expect(comp.attributeMethods.visible(comp)).toBe(true);
  });

  // Check component visibility if invisible
  it('should check component visibility', function () {
    // Prepare
    controller["invisible"] = true;
    let comp = new Component(scope, "comp2");
    comp.init();

    // Assert
    expect(comp.attributeMethods.visible(comp)).toBe(false);
  });
});