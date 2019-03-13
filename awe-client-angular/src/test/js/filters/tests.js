describe('Filters', function() {
  var $filter, $translate, $control, $utilities, $settings;
  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');
    inject(["$filter", "$translate", "Control", "AweUtilities", "AweSettings",
    function (_$filter_, _$translate_, _$control_, _$utilities_, _$settings_) {
      $filter = _$filter_;
      $translate = _$translate_;
      $control = _$control_;
      $utilities = _$utilities_;
      $settings = _$settings_;
    }]);
  });

  // Reverse test
  it('should reverse an array', function() {
    // Arrange
    let foo = ['232', '323', 'sddf', 2, 'aa'];

    // Act
    let result = $filter('reverse')(foo);

    // Assert
    expect(result).toEqual(['aa', 2, 'sddf', '323', '232']);
  });

  // Allow all options
  it('should allow option', function() {
    // Arrange
    let options = [
      {name: "option1", visible: true},
      {name: "option2", visible: true, restricted: false}
    ];

    // Act
    let result = $filter('allowedOption')(options);

    // Assert
    expect(result).toEqual(options);
  });

  // Disallow one option
  it('should disallow one option', function() {
    // Arrange
    let options = [
      {name: "option1", visible: true},
      {name: "option2", visible: true, restricted: true}
    ];

    // Act
    let result = $filter('allowedOption')(options);

    // Assert
    expect(result).toEqual([{name: "option1", visible: true}]);
  });

  // Disallow all options
  it('should disallow all options', function() {
    // Arrange
    let options = [
      {name: "option1", visible: false},
      {name: "option2", visible: true, restricted: true}
    ];

    // Act
    let result = $filter('allowedOption')(options);

    // Assert
    expect(result).toEqual([]);
  });

  // Translate a value (without defined translations)
  it('should translate a value', function() {
    // Arrange
    let textToTranslate = "text to translate";

    // Act
    let result = $filter('translateMultiple')(textToTranslate);

    // Assert
    expect(result).toEqual("text to translate");
  });

  // Translate a value (with defined translations)
  it('should translate a value', function() {
    // Arrange
    let textToTranslate = "text to  translate";
    spyOn($translate, "instant").and.callFake((text) => {
      switch (text) {
        case "text":
          return "LALA";
        case "to":
          return "2";
        case "translate":
          return "TRANSLATED!";
        default:
          return "";
      }
    });

    // Act
    let result = $filter('translateMultiple')(textToTranslate);

    // Assert
    expect(result).toEqual("LALA 2  TRANSLATED!");
  });

  // Translate a null value
  it('should translate a null value', function() {
    // Arrange
    let textToTranslate = null;

    // Act
    let result = $filter('translateMultiple')(textToTranslate);

    // Assert
    expect(result).toEqual("");
  });

  // Format a number
  it('should format a number', function() {
    // Arrange
    let numberToFormat = 12234.21;
    spyOn($settings, "getLanguage").and.returnValue('ES');

    // Act
    let result = $filter('formatNumber')(numberToFormat, '0,0.00');

    // Assert
    expect(result).toEqual("12.234,21");
  });

  // Format a number w/out decimals
  it('should format a number without decimals', function() {
    // Arrange
    let numberToFormat = 12234.21;
    spyOn($settings, "getLanguage").and.returnValue('ES');

    // Act
    let result = $filter('formatNumber')(numberToFormat, '0,0');

    // Assert
    expect(result).toEqual("12.234");
  });
});