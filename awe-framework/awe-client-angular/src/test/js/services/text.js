describe('awe-framework/awe-client-angular/src/test/js/services/text.js', function() {
  let $injector, $control, $rootScope, $utilities, Text;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $rootScope = $injector.get('$rootScope');
      $control = $injector.get('Control');
      $utilities = $injector.get('AweUtilities');
      Text = $injector.get('Text');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should generate a text', function() {
    let $scope = $rootScope.$new();
    $scope.view = "report";
    $scope.context = "contexto";
    let text = new Text($scope, "tutu", {});
    spyOn($control, "getAddressModel").and.returnValue({values: [], selected: ""});
    spyOn($control, "getAddressController").and.returnValue({id: "tutu"});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($utilities, "defineModelChangeListeners").and.returnValue(true);
    text.asText();

    // Assert
    expect(text.asText()).toBe(true);
  });

  describe('once initialized', function() {
    let text;

    // Mock module
    beforeEach(function() {
      let $scope = $rootScope.$new();
      $scope.view = "report";
      $scope.context = "contexto";
      text = new Text($scope, "tutu", {});
      spyOn($control, "getAddressModel").and.returnValue({values: [], selected: ""});
      spyOn($control, "getAddressController").and.returnValue({id: "tutu"});
      spyOn($control, "checkComponent").and.returnValue(true);
      text.asText();
    });

    it('should get visible value \'\'', function() {
      // Change model
      text.model = {selected: null, values: []};
      text.onModelChanged({selected: true});

      // Assert
      expect(text.getVisibleValue()).toBe("");
    });

    it('should change visible value to \'text\'', function() {
      // Change model
      text.model = {selected: "text", values: []};
      text.onModelChanged({selected: true});

      // Assert
      expect(text.getVisibleValue()).toBe("text");
    });

    it('should change visible value to model value text', function() {
      // Change model
      text.model = {selected: "text", values: [{value:"text", label: "This is the right text", icon: "icon", title: "title"}]};
      text.onModelChanged({values: true});

      // Assert
      expect(text.getVisibleValue()).toBe("This is the right text");
    });
  });
});