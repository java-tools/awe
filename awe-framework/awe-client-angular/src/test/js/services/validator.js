describe('Validator service', function() {
  let $control, $utilities, $settings, $validator, $window;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');
    inject(["$injector", function($injector) {
      $utilities = $injector.get("AweUtilities")
      $control = $injector.get('Control');
      $settings = $injector.get("AweSettings");
      $window = $injector.get('$window');
      $validator = $injector.get('Validator');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // A simple test to verify the controller exists
  it('should exist', function() {
    expect($validator).toBeDefined();
  });

  // Show validation error
  it('should show a validation error without timeout', function(done) {
    spyOn($.fn, "offset").and.returnValue({top:0});
    spyOn($settings, "get").and.returnValue({validate: 0});
    spyOn($utilities, "timeout").and.callFake((fn) => {
      fn();
      expect(scope.showValidation).toBe(true);
      done();
    });
    let scope = {};
    $validator.showValidationError(scope, {element: $window});
  });

  // Show validation error
  it('should show a validation error with timeout', function(done) {
    spyOn($.fn, "offset").and.returnValue({top:0});
    spyOn($settings, "get").and.returnValue({validate: 2000});
    spyOn($utilities, "timeout").and.callFake((fn) => {
      fn();
      if (!scope.showValidation) {
        done();
      }
    });
    let scope = {};
    $validator.showValidationError(scope, {element: $window});
  });
});