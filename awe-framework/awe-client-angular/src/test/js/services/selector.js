import {DefaultSettings} from "../../../main/resources/js/awe/data/options";

describe('Selector service', function() {
  let $injector, $utilities, $settings, $control, $rootScope, $translate, $httpBackend, $log, $actionController, $criterion, $selector;
  let originalTimeout;
  let controller = {};
  let model = {};

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $rootScope = $injector.get('$rootScope');
      $utilities = $injector.get('AweUtilities');
      $settings = $injector.get('AweSettings');
      $criterion = $injector.get('Criterion');
      $translate = $injector.get('$translate');
      $httpBackend = $injector.get('$httpBackend');
      $log = $injector.get('$log');
      $actionController = $injector.get('ActionController');
      $control = $injector.get('Control');
      $selector = $injector.get('Selector');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should init as select', function() {
    let $scope = $rootScope.$new();
    let selector = new $selector($scope, "tutu", {});
    spyOn(selector, "asCriterion").and.returnValue(true);
    selector.api = {};
    selector.model = model;
    selector.controller = controller;
    expect(selector.asSelect()).toBe(true);
  });

  it('should init as suggest', function() {
    let currentModel = {values:[], selected: "lala"};
    let $scope = $rootScope.$new();
    let selector = new $selector($scope, "tutu", {});
    spyOn($control, "getAddressModel").and.returnValue(currentModel);
    spyOn($actionController, "generateAction").and.returnValue({});
    spyOn($actionController, "addActionList").and.returnValue(null);
    spyOn(selector, "asCriterion").and.returnValue(true);
    spyOn(selector, "reload").and.returnValue(true);
    selector.api = {};
    selector.model = currentModel;
    selector.controller = controller;
    expect(selector.asSuggest("lala")).toBe(true);
  });

  it('should init as select multiple', function() {
    let $scope = $rootScope.$new();
    let selector = new $selector($scope, "tutu", {});
    spyOn(selector, "asCriterion").and.returnValue(true);
    selector.api = {};
    selector.model = model;
    selector.controller = controller;
    expect(selector.asSelectMultiple()).toBe(true);
  });

  it('should init as suggest multiple', function() {
    let $scope = $rootScope.$new();
    let selector = new $selector($scope, "tutu", {});
    spyOn(selector, "asCriterion").and.returnValue(true);
    selector.api = {};
    selector.model = model;
    selector.controller = controller;
    expect(selector.asSuggestMultiple()).toBe(true);
  });

  describe('once initialized as select', function() {
    let select;

    // Mock module
    beforeEach(function() {
      let $scope = $rootScope.$new();
      $scope.view = "report";
      $scope.context = "contexto";
      select = new $selector($scope, "tutu", {});
      spyOn($control, "getAddressModel").and.returnValue({values: [], selected: []});
      spyOn($control, "getAddressController").and.returnValue({id: "tutu"});
      spyOn($control, "checkComponent").and.returnValue(true);
      select.asSelect();
    });

    // As select
    it('should update model values', function() {
      // Define values to update
      let data = {values: [{value: 0, label: "No"}, {value: 1, label: "Yes"}], selected: [1]};
      let data2 = {values: [{value: "A", label: "tutu"}, {value: "B", label: "lala"}, {value: "C", label: "lolo"}], selected: ["B"]};
      let data3 = {selected: [0]};

      // Update model values
      select.api.updateModelValues(data);

      // Check values updated
      expect(select.model.values).toEqual(data.values);

      // Update model values
      select.api.updateModelValues(data2);

      // Check values updated
      expect(select.model.values).toEqual(data2.values);

      // Update model values
      select.api.updateModelValues(data);

      // Check values updated
      expect(select.model.values).toEqual(data.values);

      // Update selected values
      select.api.updateModelValues(data3);

      // Check selected updated
      expect(select.model.selected).toEqual("0");
    });
  });
});