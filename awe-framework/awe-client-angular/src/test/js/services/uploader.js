describe('awe-framework/awe-client-angular/src/test/js/services/uploader.js', function () {
  let $injector, $settings, $control, $rootScope, $uploader;
  let originalTimeout;

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      $rootScope = $injector.get('$rootScope');
      $settings = $injector.get('AweSettings');
      $control = $injector.get('Control');
      $uploader = $injector.get('Uploader');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function () {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should init as uploader', function () {
    let $scope = $rootScope.$new();
    let uploader = new $uploader($scope, "tutu", {});
    uploader.updateClasses = () => null;
    uploader.controller = {};
    uploader.model = {};
    spyOn(uploader, "asCriterion").and.returnValue(true);
    spyOn($control, "getAddressModel").and.returnValue({report: {values: [], selected: []}});
    spyOn($control, "getAddressController").and.returnValue({id: "tutu"});
    spyOn($control, "checkComponent").and.returnValue(true);
    expect(uploader.asUploader()).toBe(true);
  });

  describe('once initialized as uploader', function () {
    let uploader;
    let $scope;

    // Mock module
    beforeEach(function () {
      $scope = $rootScope.$new();
      $scope.view = "report";
      $scope.context = "contexto";
      uploader = new $uploader($scope, "tutu", {});
      uploader.updateClasses = () => null;
      uploader.controller = {};
      uploader.model = {};
      spyOn($control, "getAddressModel").and.returnValue({report: {values: [], selected: []}});
      spyOn($control, "getAddressController").and.returnValue({id: "tutu"});
      spyOn($control, "checkComponent").and.returnValue(true);
      uploader.asUploader();
    });

    it('should validate a file', function () {
      spyOn($settings, "get").and.returnValue(1);
      expect($scope.validate({size: 100})).toBe(true);
      expect($scope.validate({size: 1000000000})).toBe(false);
    });
  });
});