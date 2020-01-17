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

  /**
   * Generate a dependency to test over it
   * @param dependencyValues
   * @param component
   * @returns {*}
   */
  function generateDependency(dependencyValues, component) {
    let dependency = new Dependency(dependencyValues, component);
    spyOn($storage, "get").and.callFake(action => {
      switch (action) {
        case "model":
          return {report: {"tutu": {}, "lala":{"tutu":{"cells":{"tutu":"epa"}}}, "otro":{}, "lilo":{}, "otro2":{}, "eepa":{}}};
        case "api":
          return {report: {"tutu": () => null, "lala": {getAttribute: () => "2"}, "otro": () => 3, "lilo": () => null,"otro2": () => null,"eepa": () => null}};
      }
    });
    spyOn($utilities, "getAttribute").and.callFake((address, attribute) => {
      switch (address.component) {
        case "tutu": return null;
        case "lala": return "1";
        case "otro": return "otro";
        case "lilo": return "lilo";
        case "otro2": return "otro2";
        case "eepa": return "eepa";
        default: return "lala";
      }
    });
    return dependency;
  }

  // Compare equal values
  it('should compare equal values', function() {
    // Init
    let component = {address:{component: "epa", view: "report"}};
    let dependencyValues = {elements: [{id:"tutu"},{id: "lala", condition: "contains", value: "tutu"},{id: "otro", condition: "not contains", value: "tutu"}]};
    let dependency = generateDependency(dependencyValues, component);

    // Run
    dependency.init();
    dependency.destroy();

    // Assert
    expect(dependency.values["tutu"]).toBe(null);
    expect(dependency.values["lala"]).toBe("1");
    expect(dependency.values["otro"]).toBe("otro");
  });

  // Compare test in and not in
  it('should compare in and not in', function() {
    // Init
    let component = {address:{component: "epa", view: "report"}};
    let dependencyValues = {elements: [{id:"tutu"},{id: "lala", condition: "in", value: "tutu,lala,lele"},{id: "lilo", condition: "in", value: ["tutu", "lolo", "lele"]},{id: "otro", condition: "not in", value: "tutu,lala,lele"},{id: "otro2", condition: "not in", value: ["otro2","lala","lele"]},{id: "eepa", condition: "in", value: 2}]};
    let dependency = generateDependency(dependencyValues, component);

    // Run
    dependency.init();
    dependency.destroy();

    // Assert
    expect(dependency.values["tutu"]).toBe(null);
    expect(dependency.values["lala"]).toBe("1");
    expect(dependency.values["otro"]).toBe("otro");
    expect(dependency.values["lilo"]).toBe("lilo");
    expect(dependency.values["otro2"]).toBe("otro2");
    expect(dependency.values["eepa"]).toBe("eepa");
  });

  // Check current row value
  it('should check a currentRowValue', function() {
    // Init
    let component = {address:{component: "epa", view: "report"}};
    let dependencyValues = {elements: [{id:"tutu"},{id: "lala", column1: "col1", row1: "1", attribute1:"currentRowValue", condition: "eq", value: "1"}]};
    let dependency = generateDependency(dependencyValues, component);

    // Run
    dependency.init();
    dependency.check({"lala":{selected: 1}});
    dependency.destroy();

    // Assert
    expect(dependency.values["tutu"]).toBe(null);
    expect(dependency.values["lala"]).toBe("1");
  });

  // Check current row value on grid
  it('should check a currentRowValue on grid', function() {
    // Init
    let component = {address:{component: "lala", view: "report", column: "col1", row: "2"}};
    let dependencyValues = {elements: [{id:"tutu"},{id: "lala", column1: "col1", attribute1:"currentRowValue", condition: "eq", value: "1"}]};
    let dependency = generateDependency(dependencyValues, component);

    // Run
    dependency.init();
    dependency.check({"lala":{selected: 1}});
    dependency.destroy();

    // Assert
    expect(dependency.values["tutu"]).toBe(null);
    expect(dependency.values["lala"]).toBe("1");
  });
});