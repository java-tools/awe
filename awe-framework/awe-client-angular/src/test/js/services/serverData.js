describe('awe-framework/awe-client-angular/src/test/js/services/serverData.js', function() {
  let $injector, $serverData, $storage, $connection, $log;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $serverData = $injector.get('ServerData');
      $storage = $injector.get('Storage');
      $connection = $injector.get('Connection');
      $log = $injector.get('$log');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should retrieve form values for printing (empty)', function() {
    spyOn($storage, "get").and.returnValue({});
    expect($serverData.getFormValuesForPrinting()).toEqual({});
  });

  it('should retrieve form values for printing (some values)', function() {
    spyOn($storage, "get").and.returnValue({report:{reportOrientation:{selected:"LANDSCAPE", getPrintData: () => ({tutu: "lala"})}, otro: {}}});
    $log.debug($serverData.getFormValuesForPrinting());
    expect($serverData.getFormValuesForPrinting()).toEqual({tutu: "lala"});
  });

  it('should get template url', function() {
    spyOn($connection, "getRawUrl").and.returnValue("");
    expect($serverData.getTemplateUrl("option", "view")).toBe("/template/screen/view/option");
    expect($serverData.getTemplateUrl("", "view")).toBe("/template/screen");
  });
});