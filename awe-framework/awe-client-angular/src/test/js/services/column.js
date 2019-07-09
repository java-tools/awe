describe('awe-framework/awe-client-angular/src/test/js/services/column.js', function() {
  let $injector, $control, $utilities, Column;
  let originalTimeout;
  let address = {"component": "grid", "view": "report", "row": "2", "column": "value"};
  let component = {controller: {}, model: {}, scope: {$on: () => null}, col: {grid: {appScope: {component: {model: {}}}}}};
  let attributes = {cellAddress: JSON.stringify(address), $observe: () => null};

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $control = $injector.get('Control');
      $utilities = $injector.get('AweUtilities');
      Column = $injector.get('Column');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Generate a column
  it('should generate a column', function() {
    // Prepare
    let column = new Column(attributes);

    // Assert
    expect(column.attributes).toEqual(attributes);
    expect(column.id).toEqual("grid-value-2");
    expect(column.address).toEqual(address);
  });

  // Initialize a column
  it('should initialize a column', function() {
    // Prepare
    let column = new Column(attributes);
    component.col.grid.appScope.component.checkInitialized = jasmine.createSpy("checkInitialized");
    component.col.grid.appScope.component.getModel = jasmine.createSpy("getModel");
    component.col.grid.appScope.component.getController = jasmine.createSpy("getController");
    column.init(component);

    // Assert
    expect(component.col.grid.appScope.component.checkInitialized).toHaveBeenCalled();
    expect(component.col.grid.appScope.component.getModel).toHaveBeenCalled();
    expect(component.col.grid.appScope.component.getController).toHaveBeenCalled();
  });

  // Initialize a column
  it('should change column model', function() {
    // Prepare
    let column = new Column(attributes);
    component.col.grid.appScope.component.checkInitialized = jasmine.createSpy("checkInitialized");
    component.col.grid.appScope.component.getModel = jasmine.createSpy("getModel");
    component.col.grid.appScope.component.getController = jasmine.createSpy("getController");
    column.init(component);
    component.model = {values: [], selected: null};
    spyOn(component, "modelChange");

    // Assert
    component.model.selected = "aaa";
    component.columnModelChange();
    expect(component.modelChange).toHaveBeenCalled();
    expect(component.model.values).toEqual([{value: "aaa", label: "aaa"}]);

    // Assert
    component.model.selected = "";
    component.columnModelChange();
    expect(component.modelChange).toHaveBeenCalled();
    expect(component.model.values).toEqual([{value: null, label: ""}]);

    // Assert
    component.model.selected = null;
    component.columnModelChange();
    expect(component.modelChange).toHaveBeenCalled();
    expect(component.model.values).toEqual([{value: null, label: ""}]);
  });
});