describe('awe-framework/awe-client-angular/src/test/js/services/component.js', function () {
  let $rootScope, $injector, $control, $utilities, Component;
  let originalTimeout;
  let address = {"component": "comp1", "view": "report"};
  let scope = {view: "report", $parent: {$parent: {}}, $on: () => null, $emit: () => null};
  let controller;
  let model;
  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      $rootScope = $injector.get('$rootScope');
      $control = $injector.get('Control');
      $utilities = $injector.get('AweUtilities');
      Component = $injector.get('Component');

      controller = {visible: true};
      model = {selected: "text", records: 14, model: [{value:"text", label:"Visible text"}]};

      spyOn($control, "checkComponent").and.returnValue(true);
      spyOn($control, "getAddressModel").and.returnValue(model);
      spyOn($control, "getAddressController").and.returnValue(controller);
    }]);


    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function () {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should generate a component', function () {
    // Prepare
    let comp = new Component(scope, "comp1");

    // Assert
    expect(comp.id).toEqual("comp1");
  });

  it('should initialize a component', function () {
    // Prepare
    let comp = new Component(scope, "comp1");
    comp.init();

    // Assert
    expect(comp.address).toEqual(address);
  });

  it('should check component is visible', function () {
    // Prepare
    controller["invisible"] = false;
    let comp = new Component(scope, "comp1");
    comp.init();

    // Assert
    expect(comp.attributeMethods.visible(comp)).toBe(true);
  });

  it('should check component is not visible', function () {
    // Prepare
    controller["invisible"] = true;
    let comp = new Component(scope, "comp2");
    comp.init();

    // Assert
    expect(comp.attributeMethods.visible(comp)).toBe(false);
  });

  it('should check component visible value', function () {
    // Prepare
    let comp = new Component(scope, "comp2");
    comp.init();

    // Assert
    expect(comp.attributeMethods.text(comp)).toBe("text");
  });

  it('should check component value', function () {
    // Prepare
    let comp = new Component(scope, "comp2");
    comp.init();

    // Assert
    expect(comp.attributeMethods.value(comp)).toBe("text");
  });

  it('should check component total values', function () {
    // Prepare
    let comp = new Component(scope, "comp2");
    comp.init();

    // Assert
    expect(comp.attributeMethods.totalValues(comp)).toBe(14);
  });

  it('should check component default total values', function () {
    // Prepare
    delete model.records;
    let comp = new Component(scope, "comp2");
    comp.init();

    // Assert
    expect(comp.attributeMethods.totalValues(comp)).toBe(0);
  });
});