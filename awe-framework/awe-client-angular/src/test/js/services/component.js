import {DefaultSettings} from "../../../main/resources/js/awe/data/options";

describe('awe-framework/awe-client-angular/src/test/js/services/component.js', function () {
  let $rootScope, $injector, $control, $utilities, Component, $httpBackend;
  let originalTimeout;
  let address = {"component": "comp1", "view": "report"};
  let scopedFunctions = {};
  let scope = {view: "report", $parent: {$parent: {}}, $on: (k, fn) => scopedFunctions[k] = fn, $emit: () => null};
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
      $httpBackend = $injector.get("$httpBackend");

      controller = {visible: true};
      model = {selected: "text", records: 14, model: [{value:"text", label:"Visible text"}]};

      spyOn($control, "checkComponent").and.returnValue(true);
      spyOn($control, "getAddressModel").and.returnValue(model);
      spyOn($control, "getAddressController").and.returnValue(controller);

      $httpBackend.when('POST', 'settings').respond(DefaultSettings);
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
    expect(comp.alive).toBe(true);
    expect(comp.attributeMethods.totalValues(comp)).toBe(0);

  });

  it('should destroy a component', function () {
    // Prepare
    spyOn($utilities, "clearListeners");
    delete model.records;
    let comp = new Component(scope, "comp2");
    comp.init();
    comp.helpNode = {off: jasmine.createSpy('helpNodeOFF')};

    // Call destroy
    scopedFunctions["$destroy"]();

    // Assert
    expect(comp.alive).toBe(false);
    expect(comp.helpNode.off).toHaveBeenCalled();
    expect($utilities.clearListeners).toHaveBeenCalled();
  });

  it('should initialize a help node', function () {
    // Prepare
    spyOn($control, "publish");
    spyOn($utilities, "timeout").and.callFake(fn => fn());
    delete model.records;
    let comp = new Component(scope, "comp2");
    let help = {node: document.createElement("div")};
    comp.init();
    comp.initHelpNode(help);
    $(help.node).trigger("mouseover");
    $(help.node).trigger("mouseout");

    // Assert
    expect(comp.alive).toBe(true);
    expect($utilities.timeout).toHaveBeenCalled();
    expect($control.publish).toHaveBeenCalledTimes(2);
  });

  it('should call a help node with a destroyed object', function () {
    // Prepare
    spyOn($control, "publish");
    spyOn($utilities, "timeout").and.callFake(fn => fn());
    delete model.records;
    let comp = new Component(scope, "comp2");
    let help = {node: document.createElement("div")};
    comp.init();
    comp.initHelpNode(help);
    comp.alive = false;
    $(help.node).trigger("mouseover");
    $(help.node).trigger("mouseout");

    // Assert
    expect($utilities.timeout).toHaveBeenCalledTimes(0);
    expect($control.publish).toHaveBeenCalledTimes(1);
  });

  it('should call a help node with a disabled object when showing', function () {
    // Prepare
    spyOn($control, "publish");
    delete model.records;
    let comp = new Component(scope, "comp2");
    let help = {node: document.createElement("div")};
    spyOn($utilities, "timeout").and.callFake(fn => {
      comp.isDisabled = () => true;
      fn();
    });
    comp.init();
    comp.isDisabled = () => false;
    comp.initHelpNode(help);
    $(help.node).trigger("mouseover");
    $(help.node).trigger("mouseout");

    // Assert
    expect($utilities.timeout).toHaveBeenCalledTimes(1);
    expect($control.publish).toHaveBeenCalledTimes(1);
  });

  it('should call a help node with a component not hover when showing', function () {
    // Prepare
    spyOn($control, "publish");
    delete model.records;
    let comp = new Component(scope, "comp2");
    let help = {node: document.createElement("div")};
    spyOn($utilities, "timeout").and.callFake(fn => {
      comp.helpOver = false;
      fn();
    });
    comp.init();
    comp.initHelpNode(help);
    $(help.node).trigger("mouseover");
    $(help.node).trigger("mouseout");

    // Assert
    expect($utilities.timeout).toHaveBeenCalledTimes(1);
    expect($control.publish).toHaveBeenCalledTimes(1);
  });
});