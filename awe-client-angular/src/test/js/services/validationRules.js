describe('awe-client-angular/src/test/js/services/validationRules.js', function() {
  let $control, $settings, $rules;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');
    inject(["$injector", function($injector) {
      $rules = $injector.get('ValidationRules');
      $control = $injector.get('Control');
      $settings = $injector.get("AweSettings");
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // A simple test to verify the controller exists
  it('should exist', function() {
    expect($rules).toBeDefined();
  });

  /**
   * Test a validation
   * @param validation Validation
   * @param value Value to add
   * @param parameters Parameters
   * @param expected Expected result
   */
  function testValidation(validation, value, parameters, expected) {
    let result = $rules.validate(validation, value, parameters, {view: "base", component: "cod_usr", group: "test"});
    expect(result).toEqual(expected);
  }

  // Require validation
  it('should launch a require validation OK', function() {
    testValidation("required", "test", true, null);
  });

  // Require validation failed
  it('should launch a require validation KO', function() {
    testValidation("required", "", {value: true}, { message: 'VALIDATOR_MESSAGE_REQUIRED' });
  });

  // Require validation group
  it('should launch a group require validation OK', function() {
    spyOn($control, "getAddressController").and.returnValue({group: "test"});
    spyOn($control, "getAddressModel").and.returnValue({selected: "tutu"});
    testValidation("required", null, true, null);
  });

  // Require validation group
  it('should launch a group require validation KO', function() {
    spyOn($control, "getAddressController").and.returnValue({group: "test"});
    spyOn($control, "getAddressModel").and.returnValue(null);
    testValidation("required", null, {value: true}, { message: "VALIDATOR_MESSAGE_REQUIRED" } );
  });

  // Require validation multiple
  it('should launch a multiple require validation OK', function() {
    spyOn($control, "getAddressController").and.returnValue({});
    testValidation("required", ["tutu", "lala"], true, null);
  });

  // Require validation group
  it('should launch a multiple require validation KO', function() {
    spyOn($control, "getAddressController").and.returnValue({});
    testValidation("required", [], {value: true}, { message: "VALIDATOR_MESSAGE_REQUIRED" } );
  });

  // Text validation
  it('should launch a text validation OK', function() {
    testValidation("text", "asda", "", null);
  });

  // Text validation
  it('should launch a text validation KO', function() {
    testValidation("text", "45225", {message: "Naaaah, no es igual"}, { message: 'Naaaah, no es igual' });
  });

  // Text with spaces validation
  it('should launch a text with spaces validation OK', function() {
    testValidation("textWithSpaces", "asda as dtr", "", null);
  });

  // Text with spaces validation
  it('should launch a text validation KO', function() {
    testValidation("textWithSpaces", "asda", {message: "Naaaah, {value1} no tiene espacios"}, { message: 'Naaaah, asda no tiene espacios' });
  });

  // Number validation
  it('should launch a number validation OK', function() {
    testValidation("number", "123.12", "", null);
  });

  // Number validation
  it('should launch a number validation KO', function() {
    testValidation("number", "as33sd", {message: "Naaaah, no es numerico"}, { message: 'Naaaah, no es numerico' });
  });

  // Integer validation
  it('should launch a integer validation OK', function() {
    testValidation("integer", "1232", "", null);
  });

  // Integer validation
  it('should launch a integer validation KO', function() {
    testValidation("integer", "1232.21", {message: "Naaaah, no es entero"}, { message: 'Naaaah, no es entero' });
  });

  // Digits validation
  it('should launch a digits validation OK', function() {
    testValidation("digits", "071232", "", null);
  });

  // Digits validation
  it('should launch a digits validation KO', function() {
    testValidation("digits", "1232.21", {message: "Naaaah, no son solo digitos"}, { message: 'Naaaah, no son solo digitos' });
  });

  // Email validation
  it('should launch a email validation OK', function() {
    testValidation("email", "test@awe.com", "", null);
  });

  // Email validation
  it('should launch a email validation KO', function() {
    testValidation("email", "tutu@alal@aea.qwq", {message: "Naaaah, {value1} no es válido"}, { message: 'Naaaah, tutu@alal@aea.qwq no es válido' });
  });

  // Date validation
  it('should launch a date validation OK', function() {
  testValidation("date", "21/01/2018", "", null);
  });

  // Date validation
  it('should launch a date validation KO', function() {
  testValidation("date", "29/02/2019", {message: "Naaaah, {value1} no es válido"}, { message: 'Naaaah, 29/02/2019 no es válido' });
  });

  // Date validation
  it('should launch a date validation KO (bad formatted)', function() {
  testValidation("date", "29-02-2019", {message: "Naaaah, {value1} no es válido"}, { message: 'Naaaah, 29-02-2019 no es válido' });
  });

  // Time validation
  it('should launch a time validation OK', function() {
  testValidation("time", "23:11:02", "", null);
  });

  // Time validation
  it('should launch a time validation KO', function() {
  testValidation("time", "25:11:02", {message: "Naaaah, {value1} no es válido"}, { message: 'Naaaah, 25:11:02 no es válido' });
  });

  // Equal validation
  it('should launch an equal validation OK', function() {
    spyOn($control, "getAddressModel").and.returnValue({selected: "asda"});
    testValidation("eq", "asda", {criterion: "tutu"}, null);
  });

  // Equal validation
  it('should launch an equal validation KO', function() {
    spyOn($control, "getAddressModel").and.returnValue({selected: "aasdasd"});
    testValidation("eq", "asda", {criterion: "tutu", message: "Naaaah, no es igual"}, { message: 'Naaaah, no es igual' });
  });

  // NE validation
  it('should launch a ne validation OK', function() {
    spyOn($settings, "get").and.returnValue("askxlsa");
    testValidation("ne", "asda", {setting: "lala"}, null);
  });

  // NE validation
  it('should launch a ne validation KO', function() {
    spyOn($control, "getAddressModel").and.returnValue({selected: "asda"});
    testValidation("ne", "asda", {criterion: "tutu", message: "Naaaah, no es igual"}, { message: 'Naaaah, no es igual' });
  });

  // LT validation
  it('should launch a lt validation OK', function() {
    testValidation("lt", "11", {value: "12"}, null);
  });

  // LT validation
  it('should launch a lt validation OK because the second parameter is null', function() {
    testValidation("lt", "123", {otros: "0"}, null);
  });

  // LT validation
  it('should launch a lt validation KO', function() {
    testValidation("lt", "11", {value: "11"}, { message: 'VALIDATOR_MESSAGE_LESS_THAN' });
  });

  // LE validation
  it('should launch a le validation OK', function() {
    testValidation("le", "11/10/2009", {value: "12/10/2009", type: "date"}, null);
  });

  // LE validation
  it('should launch a le validation OK because the second parameter is null', function() {
    testValidation("le", "11/10/2009", {value: "", type: "date"}, null);
  });

  // LE validation
  it('should launch a le validation KO', function() {
    testValidation("le", "11/10/2009", {value: "12/09/2009", type: "date"}, { message: 'VALIDATOR_MESSAGE_LESS_OR_EQUAL' });
  });

  // GT validation
  it('should launch a gt validation OK', function() {
    testValidation("gt", "13", {value: "12", type: "integer"}, null);
  });

  // GT validation
  it('should launch a gt validation OK because the second parameter is null', function() {
    testValidation("gt", "123", {otros: "0", type: "integer"}, null);
  });

  // GT validation
  it('should launch a gt validation KO', function() {
    testValidation("gt", "11", {value: "11", type: "float"}, { message: 'VALIDATOR_MESSAGE_GREATER_THAN' });
  });

  // GE validation
  it('should launch a ge validation OK', function() {
    testValidation("ge", "11/10/2009", {value: "10/10/2008", type: "date"}, null);
  });

  // GE validation
  it('should launch a ge validation OK because the second parameter is null', function() {
    testValidation("ge", "112.12", {value: "", type: "float"}, null);
  });

  // GE validation
  it('should launch a ge validation KO', function() {
    testValidation("ge", "11/10/2009", {value: "10/11/2011", type: "date"}, { message: 'VALIDATOR_MESSAGE_GREATER_OR_EQUAL' });
  });

  // MOD validation
  it('should launch a mod validation OK', function() {
    testValidation("mod", "6", {value: "2", type: "integer"}, null);
  });

  // MOD validation
  it('should launch a mod validation OK because the second parameter is null', function() {
    testValidation("mod", "6", {value: null, type: "integer"}, null);
  });

  // MOD validation
  it('should launch a mod validation KO', function() {
    testValidation("mod", "5", {value: "2", type: "integer"}, { message: 'VALIDATOR_MESSAGE_DIVISIBLE_BY' });
  });

  // RANGE validation
  it('should launch a range validation OK', function() {
    testValidation("range", "6", {from: {value: 2}, to: {value: 7}, type: "integer"}, null);
  });

  // RANGE validation
  it('should launch a range validation KO', function() {
    testValidation("range", "5", {from: {value: "2"}, to: {value: 4}, type: "integer"}, { message: 'VALIDATOR_MESSAGE_RANGE' });
  });

  // Date RANGE validation
  it('should launch a date range validation OK', function() {
    testValidation("range", "23/10/2016", {from: {value: "25/08/2015"}, to: {value: "01/11/2016"}, type: "date"}, null);
  });

  // Date RANGE validation
  it('should launch a date range validation OK because one value is empty', function() {
    testValidation("range", "23/10/2016", {from: {value: "25/08/2015"}, to: {other: "01/11/2016"}, type: "date"}, null);
  });

  // Date RANGE validation
  it('should launch a date range validation KO', function() {
    testValidation("range", "26/11/2014", {from: {value: "25/08/2015"}, to: {value: "01/11/2016"}, type: "date"}, { message: 'VALIDATOR_MESSAGE_RANGE' });
  });

  // Equal length validation
  it('should launch a equal length validation OK', function() {
    testValidation("equallength", "23/10/2016", {value: 10}, null);
  });

  // Equal length validation
  it('should launch a equal length validation OK because one value is empty', function() {
    testValidation("equallength", "23/10/2016", {other: null}, null);
  });

  // Equal length validation
  it('should launch a equal length validation KO', function() {
    testValidation("equallength", "26/11/2014", {value: 8}, { message: 'VALIDATOR_MESSAGE_EQUAL_LENGTH' });
  });

  // Maxlength validation
  it('should launch a maxlength validation OK', function() {
    testValidation("maxlength", "23/10/2016", {value: 10}, null);
  });

  // Maxlength validation
  it('should launch a maxlength validation OK because one value is empty', function() {
    testValidation("maxlength", "23/10/2016", {other: null}, null);
  });

  // Maxlength validation
  it('should launch a maxlength validation KO', function() {
    testValidation("maxlength", "26/11/2014", {value: 8}, { message: 'VALIDATOR_MESSAGE_MAXLENGTH' });
  });

  // Minlength validation
  it('should launch a minlength validation OK', function() {
    testValidation("minlength", "23/10/2016", {value: 8}, null);
  });

  // Minlength validation
  it('should launch a minlength validation OK because one value is empty', function() {
    testValidation("minlength", "23/10/2016", {other: null}, null);
  });

  // Minlength validation
  it('should launch a minlength validation KO', function() {
    testValidation("minlength", "26/11/2014", {value: 11}, { message: 'VALIDATOR_MESSAGE_MINLENGTH' });
  });

  // Pattern validation
  it('should launch a pattern validation OK', function() {
    testValidation("pattern", "aadrsasa", {value: "^[a-z]+$"}, null);
  });

  // Pattern validation
  it('should launch a password pattern validation OK', function() {
    spyOn($settings, "get").and.returnValue("^[a-z]+$");
    testValidation("pattern", "aadrsasa", "", null);
  });

  // Pattern validation
  it('should launch a pattern validation KO', function() {
    testValidation("pattern", "aEDaxsa", {value: "^[a-z]+$"}, { message: 'VALIDATOR_MESSAGE_PATTERN' });
  });

  // Check at least validation
  it('should launch a checkAtLeast validation OK', function() {
    spyOn($control, "getAddressViewController").and.returnValue({component1: {group: "test"}, component2: {group: "test"}});
    spyOn($control, "getAddressModel").and.returnValue({selected: true});
    testValidation("checkAtLeast", null, {value: "2"}, null);
  });

  // Check at least validation
  it('should launch a checkAtLeast validation OK because second parameter is not a number', function() {
    spyOn($control, "getAddressViewController").and.returnValue({component1: {group: "test"}, component2: {group: "test"}});
    spyOn($control, "getAddressModel").and.returnValue({selected: true});
    testValidation("checkAtLeast", null, {value: null}, null);
  });

  // Check at least validation
  it('should launch a checkAtLeast validation KO', function() {
    spyOn($control, "getAddressViewController").and.returnValue({component1: {group: "test"}, component2: {group: "test"}, component3: {group: "other"}});
    spyOn($control, "getAddressModel").and.callFake((address) => ({selected: address.component === "component2"}));
    testValidation("checkAtLeast", null, {value: "2"}, { message: 'VALIDATOR_MESSAGE_CHECK_AT_LEAST' });
  });

  // Invalid validation
  it('should launch a invalid validation not active', function() {
    testValidation("invalid", "test", "", null);
  });

  // Invalid validation
  it('should launch a invalid validation active', function() {
    testValidation("invalid", "test", {value: true, message: "Invalid validation for {value1}"}, {message: "Invalid validation for test"});
  });
});