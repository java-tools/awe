import {DefaultSettings} from "../../../main/resources/js/awe/data/options";

describe('awe-framework/awe-client-angular/src/test/js/services/dateTime.js', function() {
  let $injector, $utilities, $settings, $control, $rootScope, $translate, $httpBackend, $log, $actionController, $criterion, $dateTime;
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
      $dateTime = $injector.get('DateTime');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should init as date', function() {
    let $scope = $rootScope.$new();
    let selector = new $dateTime($scope, "tutu", {});
    spyOn(selector, "asCriterion").and.returnValue(true);
    selector.api = {};
    selector.model = model;
    selector.controller = controller;
    expect(selector.asDate()).toBe(true);
  });

  it('should init as filtered date', function() {
    let currentModel = {values:[], selected: "lala"};
    let $scope = $rootScope.$new();
    let selector = new $dateTime($scope, "tutu", {});
    spyOn(selector, "asCriterion").and.returnValue(true);
    selector.api = {};
    selector.model = currentModel;
    selector.controller = controller;
    expect(selector.asFilteredDate()).toBe(true);
  });

  it('should init as time', function() {
    let $scope = $rootScope.$new();
    let selector = new $dateTime($scope, "tutu", {});
    spyOn(selector, "asCriterion").and.returnValue(true);
    selector.api = {};
    selector.model = model;
    selector.controller = controller;
    expect(selector.asTime()).toBe(true);
  });

  describe('once initialized', function() {
    let dateSelector;

    // Mock module
    beforeEach(function() {
      let $scope = $rootScope.$new();
      $scope.view = "report";
      $scope.context = "contexto";
      dateSelector = new $dateTime($scope, "tutu", {});
      spyOn($control, "getAddressModel").and.returnValue({values: [], selected: []});
      spyOn($control, "getAddressController").and.returnValue({id: "tutu"});
      spyOn($control, "checkComponent").and.returnValue(true);
      dateSelector.asFilteredDate();
    });

    // As select
    it('should filter some dates', function() {
      // Filter date without date list
      dateSelector.scope.aweDateOptions.beforeShowDay(new Date());

      // Filter date with date list
      dateSelector.model.values = ['23/10/2018'];
      dateSelector.scope.aweDateOptions.beforeShowDay(new Date());
    });
  });
});