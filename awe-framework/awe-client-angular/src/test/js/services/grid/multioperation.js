describe('awe-framework/awe-client-angular/src/test/js/services/grid/multioperation.js', function () {
  let $injector, GridMultioperation, GridEditable, $rootScope, $control;
  let originalTimeout;
  const currentModel = {};
  const currentController = {columnModel: []};

  const getDefaultComponent = () => ({
    constants: {
      ROW_IDENTIFIER: "id",
      CELL_VALUE: "value",
      CELL_LABEL: "label",
      CELL_TITLE: "title",
      CELL_STYLE: "cell-style",
      CELL_ICON: "icon",
      CELL_IMAGE: "image"
    },
    controller: {columnModel: []},
    enableSorting: true,
    scope: {$on: () => null, charSize: 7, gridOptions: {}},
    listeners: {},
    api: {},
    model: {
      records: 6,
      total: 1,
      page: 1,
      values: [{id: 1, value: "tutu", RowTyp: "INSERT"}, {id: 2, value: "lala"}, {
        id: 4,
        value: "lele",
        other: "asda",
        RowTyp: "UPDATE"
      }, {id: 5, value: "lili"}, {id: 6, value: "lolo", RowTyp: "DELETE"}, {id: 7, value: "lulu"}],
      selected: [4]
    },
    address: {view: "viewId", component: "componentId"},
    resetSelection: () => null,
    selectRow: () => null,
    storeEvent: () => null,
    attributeMethods: {}
  });

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      GridMultioperation = $injector.get('GridMultioperation');
      GridEditable = $injector.get('GridEditable');
      $control = $injector.get('Control');
      $rootScope = $injector.get('$rootScope');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function () {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should initialize a multioperation grid', function () {
    // Mock
    let component = getDefaultComponent();
    let multioperation = new GridMultioperation(component);
    spyOn(component.gridEditable, "init").and.returnValue(true);
    expect(multioperation.init()).toBe(true);
  });

  describe('once initialized', function() {
    let component;

    // Mock module
    beforeEach(function () {
      // Mock
      component = getDefaultComponent();
      let multioperation = new GridMultioperation(component);
      spyOn(component.gridEditable, "init").and.returnValue(true);
      multioperation.init();
      component.hideContextMenu = jasmine.createSpy("hideContextMenu");
      component.deleteRowCells = jasmine.createSpy("deleteRowCells");
      component.deleteRowSpecific = jasmine.createSpy("deleteRowSpecific");
      component.addRowStyle = jasmine.createSpy("addRowStyle");
      spyOn($control, "changeModelAttribute");
      component.getSelectedRows = () => [4];
    });

    it('should delete a row', function () {
      // Assert
      component.deleteRow(4);
    });

    it('should delete the selected row', function () {
      // Assert
      component.deleteRow();
    });

    it('should delete a non existent row', function () {
      // Assert
      component.deleteRow(3);
    });
  });
});