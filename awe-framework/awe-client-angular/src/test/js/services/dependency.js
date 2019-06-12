describe('awe-framework/awe-client-angular/src/test/js/services/dependency.js', function() {
  let $injector, Dependency, $storage, $utilities;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      Dependency = $injector.get('Dependency');
      $storage = $injector.get('Storage');
      $utilities = $injector.get('AweUtilities');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Compare equal values
  it('should init a dependency', function() {
    // Init
    let component = {address:{component: "epa", view: "report"}};
    let dependencyValues = {elements: [{id:"tutu"},{id: "lala", condition: "contains", value: "tutu"},{id: "otro", condition: "not contains", value: "tutu"}]};
    let dependency = new Dependency(dependencyValues, component);
    spyOn($storage, "get").and.callFake(action => {
      switch (action) {
        case "model":
          return {report: {"tutu": {}, "lala":{"tutu":{"cells":{"tutu":"epa"}}}, "otro":{}}};
        case "api":
          return {report: {"tutu": () => null, "lala": () => "tutu", "otro": () => 3}};
      }
    });

    spyOn($utilities, "getAttribute").and.callFake((address, attribute) => {
      switch (address.component) {
        case "tutu": return null;
        case "lala": return "tutu";
        case "otro": return 1211;
        default: return "lala";
      }
    });

    // Run
    dependency.init();
    dependency.destroy();

    // Assert
    expect(dependency.values["tutu"]).toBe(null);
    expect(dependency.values["lala"]).toBe("tutu");
    expect(dependency.values["otro"]).toBe(1211);
  });
});