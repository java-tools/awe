describe('Utilities service', function() {
  let $injector, $utilities
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $utilities = $injector.get('AweUtilities');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Compare equal values
  it('should compare equal values', function() {
    // Assert
    expect($utilities.compareEqualValues(null, "lala")).toBe(false);
    expect($utilities.compareEqualValues(null, null)).toBe(true);
    expect($utilities.compareEqualValues(123123, 23)).toBe(false);
    expect($utilities.compareEqualValues(1231, 1231)).toBe(true);
    expect($utilities.compareEqualValues("tutu lala", "tutu lala")).toBe(true);
    expect($utilities.compareEqualValues("tutu lala asa", "tutu lala")).toBe(false);
  });

  // Compare contain values
  it('should compare contain values', function() {
    // Assert
    expect($utilities.compareContainValues(null, "lala")).toBe(false);
    expect($utilities.compareContainValues(null, null)).toBe(true);
    expect($utilities.compareContainValues(123123, 23)).toBe(true);
    expect($utilities.compareContainValues("tutu lala ellele", "elle")).toBe(true);
    expect($utilities.compareContainValues("tutu lala ellele", "jander")).toBe(false);
    expect($utilities.compareContainValues("jander", "jander clander")).toBe(false);
  });
});