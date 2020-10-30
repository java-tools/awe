describe('awe-framework/awe-client-angular/src/test/js/services/utilities.js', function() {
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

  it('should get state', function() {
    // Assert
    expect($utilities.getState("/screen/public/home/tutu", false)).toEqual({to: 'public.screen', parameters: {screenId: "home", subScreenId: "tutu"}});
    expect($utilities.getState("/screen/private/home/tutu", false)).toEqual({to: 'private.screen', parameters: {screenId: "home", subScreenId: "tutu"}});
    expect($utilities.getState("/screen/tutu", false)).toEqual({to: 'global', parameters: {screenId: "tutu"}});
    expect($utilities.getState("/", false)).toEqual({to: 'index', parameters: {}});
    expect($utilities.getState("/ascasc/Asvasv", false)).toEqual({to: 'index', parameters: {}});
    expect($utilities.getState("/screen/public/home/tutu", true).to).toEqual('public.screen');
    expect($utilities.getState("/screen/public/home/tutu", true).parameters.screenId).toEqual('home');
    expect($utilities.getState("/screen/public/home/tutu", true).parameters.subScreenId).toContain('tutu?');
  });

  it('should get row index', function() {
    // Generate model
    let values = [
      {id: 1, _ID: "1"},
      {id: 2, _ID: "3"},
      {id: 3, _ID: "5"},
      {id: 4, _ID: "6"},
      {id: 5, _ID: "7"}
    ];

    // Assert
    expect($utilities.getRowIndex(values, 2, "_ID")).toEqual(-1);
    expect($utilities.getRowIndex(values, 3, "_ID")).toEqual(1);
    expect($utilities.getRowIndex(values, 7, "_ID")).toEqual(4);
    expect($utilities.getRowIndex(values, "1", "_ID")).toEqual(0);
    expect($utilities.getRowIndex(values, 2, "id")).toEqual(1);
    expect($utilities.getRowIndex(values, "a", "id")).toEqual(-1);
    expect($utilities.getRowIndex(values, "3", "id")).toEqual(2);
    expect($utilities.getRowIndex(values, 1, "id")).toEqual(0);
  });

  it('should decode data', function () {
    let data = '{"test": 1}';

    // Assert
    expect($utilities.decodeData($utilities.encodeSymetric(data), "1")).toEqual({"test": 1});
    expect($utilities.decodeData(data, "0")).toEqual({"test": 1});
  });
});