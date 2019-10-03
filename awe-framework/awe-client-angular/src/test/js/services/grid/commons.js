describe('awe-framework/awe-client-angular/src/test/js/services/grid/commons.js', function () {
  let $injector, GridCommons;
  let originalTimeout;
  const getDefaultComponent = () => ({
    constants: {
      ROW_IDENTIFIER: "id",
      CELL_VALUE: "value",
      CELL_LABEL: "label",
      CELL_TITLE: "title",
      CELL_STYLE: "style",
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

  const getDefaultColumnModel = () => ([{
    id: "id",
    hidden: true,
    component: "text",
    enableFiltering: true,
    charlength: "0",
    sendable: true
  }, {
    id: "value",
    label: "Value",
    hidden: false,
    component: "text",
    summaryType: "SUM",
    enableFiltering: true,
    sortField: "lala",
    charlength: 20,
    sortable: true,
    sendable: true
  }, {
    id: "other",
    label: "Other thing",
    hidden: false,
    summaryType: "SUM",
    sortable: false,
    sortField: "lala",
    charlength: 20,
    sendable: true
  },{
    id: "other other",
    hidden: true,
    summaryType: "SUM",
    sortable: false,
    sortField: "lala",
    charlength: 20
  }]);

  /**
   * Test visible value method
   * @param component
   * @param row
   * @param column
   * @param type
   * @param componentType
   * @param cell
   * @param expected
   */
  const testVisibleValue = function(component, row, column, type, componentType, cell, expected) {
    column.type = type;
    column.component = componentType;
    component.getApi = () => ({getVisibleValue: () => cell.label});
    row["value"] = cell;
    let visibleData = component.getVisibleData({component: "componentId", row: 4, column: "value"}, row, column);
    expect(visibleData).toEqual(expected);
  };

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      GridCommons = $injector.get('GridCommons');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function () {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Fix column
  it('should fix a column', function () {
    // Mock
    let component = getDefaultComponent();
    let column = {id: "tutu", component: "text", enableFiltering: true, charlength: "0"};
    let column2 = {
      id: "tutu",
      hidden: true,
      component: "text",
      summaryType: "SUM",
      enableFiltering: true,
      sortField: "lala",
      charlength: 20,
      sortable: true
    };
    let column3 = {id: "tutu", hidden: true, summaryType: "SUM", sortable: false, sortField: "lala", charlength: 20};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    component.fixColumn(column);
    component.fixColumn(column2);
    component.fixColumn(column3);

    // Assert
    expect(column.sortField).toBe("tutu");
    expect(column.enableFiltering).toBe(false);
    expect(column.footerCellTemplate).toBe("grid/footer");
    expect(column.cellTemplate).toBe("<div class=\"ui-grid-cell-contents component {{col.cellClass}}\" title=\"TOOLTIP\" column-id=\"{{col.name}}\"><awe-column-text " +
      "cell-address='{\"hash\":\"{{row.uid}}\", \"view\":\"viewId\", \"component\":\"componentId\", \"row\":\"{{row.entity.id}}\", \"column\":\"{{col.name}}\"}'/></div>");

    expect(column2.enableSorting).toBe(true);
    expect(column2.sortField).toBe("lala");
    expect(column2.footerCellTemplate).toBe("<div class=\"ui-grid-cell-contents ui-grid-cell-footer {{::col.cellClass}}\" title=\"TOOLTIP\" column-id=\"{{col.name}}\"><awe-column-text " +
      "cell-address='{\"hash\":\"footer-{{grid.appScope.model.page}}\", \"view\":\"viewId\", \"component\":\"componentId\", \"row\":\"footer\", \"column\":\"{{col.name}}\"}'/></div>");

    expect(column3.enableSorting).toBe(false);

  });

  // Set selection
  it('should select a list of rows in a grid', function () {
    // Mock
    var spy = jasmine.createSpy('spy');
    let component = {
      constants: {ROW_IDENTIFIER: "id"},
      controller: {columnModel: []},
      scope: {$on: () => null, charSize: 7},
      enableSorting: true,
      listeners: {},
      api: {},
      model: {
        values: [{id: 1}, {id: 2}, {id: 4}, {id: 5}, {id: 6}, {id: 7}]
      },
      address: {view: "viewId", component: "componentId"},
      resetSelection: () => null,
      selectRow: () => null,
      storeEvent: () => null
    };
    let commons = new GridCommons(component);
    let selection = ["1", "2", "3", "4"];
    commons.init();
    component.onSelectRows = spy;

    // Launch
    component.setSelection(selection);

    // Assert
    expect(spy).toHaveBeenCalledWith([1, 2, 4]);
  });

  // Current row value
  it('should retrieve current row value', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.currentRowValue(component, "value", "2");
    let value2 = component.attributeMethods.currentRowValue(component, "value", "5");
    let value3 = component.attributeMethods.currentRowValue(component, "value", "1");
    let value4 = component.attributeMethods.currentRowValue(component, "value", "7");

    // Assert
    expect(value1).toBe("lala");
    expect(value2).toBe("lili");
    expect(value3).toBe("tutu");
    expect(value4).toBe("lulu");
  });

  // Previous to current row value
  it('should retrieve previous to current row value', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.prevCurrentRowValue(component, "value", "2");
    let value2 = component.attributeMethods.prevCurrentRowValue(component, "value", "5");
    let value3 = component.attributeMethods.prevCurrentRowValue(component, "value", "1");
    let value4 = component.attributeMethods.prevCurrentRowValue(component, "value", "7");

    // Assert
    expect(value1).toBe("tutu");
    expect(value2).toBe("lele");
    expect(value3).toBe(null);
    expect(value4).toBe("lolo");
  });

  // Previous to next row value
  it('should retrieve next to current row value', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.nextCurrentRowValue(component, "value", "2");
    let value2 = component.attributeMethods.nextCurrentRowValue(component, "value", "5");
    let value3 = component.attributeMethods.nextCurrentRowValue(component, "value", "1");
    let value4 = component.attributeMethods.nextCurrentRowValue(component, "value", "7");

    // Assert
    expect(value1).toBe("lele");
    expect(value2).toBe("lolo");
    expect(value3).toBe("lala");
    expect(value4).toBe(null);
  });

  // Selected row value
  it('should retrieve selected row value', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.selectedRowValue(component, "value");

    // Assert
    expect(value1).toBe("lele");
  });

  // Previous to selected row value
  it('should retrieve previous to selected row value', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.prevRowValue(component, "value");

    // Launch
    component.model.selected = [1];
    let value2 = component.attributeMethods.prevRowValue(component, "value");

    // Assert
    expect(value1).toBe("lala");
    expect(value2).toBe(null);
  });

  // Next to selected row value
  it('should retrieve next to selected row value', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.nextRowValue(component, "value");
    component.model.selected = [7];
    let value2 = component.attributeMethods.nextRowValue(component, "value");

    // Assert
    expect(value1).toBe("lili");
    expect(value2).toBe(null);
  });

  // Footer row value
  it('should retrieve footer row value', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.footerValue(component, "value");

    // Assert
    expect(value1).toBe("footerValue");
  });

  // Current row
  it('should retrieve current row', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.currentRow(component, "", "2");
    let value2 = component.attributeMethods.currentRow(component, "", null);
    let value3 = component.attributeMethods.currentRow(component, "", 2);

    // Assert
    expect(value1).toBe(2);
    expect(value2).toBe(null);
    expect(value3).toBe(2);
  });

  // Previous row
  it('should retrieve previous row', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.prevCurrentRow(component, "", "2");
    let value2 = component.attributeMethods.prevCurrentRow(component, "", "1");
    let value3 = component.attributeMethods.prevCurrentRow(component, "", null);

    // Assert
    expect(value1).toBe(1);
    expect(value2).toBe(null);
    expect(value3).toBe(null);
  });

  // Next row
  it('should retrieve next row', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.nextCurrentRow(component, "", "2");
    let value2 = component.attributeMethods.nextCurrentRow(component, "", "7");
    let value3 = component.attributeMethods.nextCurrentRow(component, "", null);

    // Assert
    expect(value1).toBe(4);
    expect(value2).toBe(null);
    expect(value3).toBe(null);
  });

  // Selected row
  it('should retrieve selected row', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.selectedRow(component);
    component.model.selected = [];
    let value2 = component.attributeMethods.selectedRow(component);

    // Assert
    expect(value1).toBe(4);
    expect(value2).toBe(null);
  });

  // Previous row to selected one
  it('should retrieve previous row to selected one', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.prevRow(component);
    component.model.selected = [];
    let value2 = component.attributeMethods.prevRow(component);

    // Assert
    expect(value1).toBe(2);
    expect(value2).toBe(null);
  });

  // Next row
  it('should retrieve next row to selected one', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.nextRow(component);
    component.model.selected = [];
    let value2 = component.attributeMethods.nextRow(component);

    // Assert
    expect(value1).toBe(5);
    expect(value2).toBe(null);
  });

  // Total rows
  it('should retrieve total rows', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.totalRows(component);
    component.model.records = 55;
    let value2 = component.attributeMethods.totalRows(component);

    // Assert
    expect(value1).toBe(6);
    expect(value2).toBe(55);
  });

  // Check empty column
  it('should check if a column is empty or not', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.emptyDataColumn(component, "value");
    let value2 = component.attributeMethods.emptyDataColumn(component, "other other");
    let value3 = component.attributeMethods.emptyDataColumn(component, "other");

    // Assert
    expect(value1).toBe(false);
    expect(value2).toBe(true);
    expect(value3).toBe(false);
  });

  // Check if column has data
  it('should check if column has data or not', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.hasDataColumn(component, "value");
    let value2 = component.attributeMethods.hasDataColumn(component, "other other");
    let value3 = component.attributeMethods.hasDataColumn(component, "other");

    // Assert
    expect(value1).toBe(true);
    expect(value2).toBe(false);
    expect(value3).toBe(true);
  });

  // Check if column has all rows filled with data
  it('should check if column has data in all rows or not', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.fullDataColumn(component, "value");
    let value2 = component.attributeMethods.fullDataColumn(component, "other");
    let value3 = component.attributeMethods.fullDataColumn(component, "other other");

    // Assert
    expect(value1).toBe(true);
    expect(value2).toBe(false);
    expect(value3).toBe(false);
  });

  // Modified rows
  it('should retrieve modified rows', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.modifiedRows(component);
    component.model.selected = [4, 5, 7];
    let value2 = component.attributeMethods.modifiedRows(component);

    component = getDefaultComponent();
    component.controller.multioperation = true;
    commons = new GridCommons(component);
    commons.init();
    let value3 = component.attributeMethods.modifiedRows(component);

    // Assert
    expect(value1).toBe(1);
    expect(value2).toBe(3);
    expect(value3).toBe(3);
  });

  // Selected rows
  it('should retrieve selected row number', function () {
    // Mock
    let component = getDefaultComponent();
    component.model.footer = {value: "footerValue"};
    component.model.selected = [4, 6, 7];
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let value1 = component.attributeMethods.selectedRows(component);
    let value2 = component.attributeMethods.value(component);
    component.model.selected = [];
    let value3 = component.attributeMethods.selectedRows(component);
    let value4 = component.attributeMethods.value(component);

    // Assert
    expect(value1).toBe(3);
    expect(value2).toBe(3);
    expect(value3).toBe(0);
    expect(value4).toBe(0);
  });

  // Custom scroller
  it('should initialize custom scroller', function (done) {
    // Mock
    let spy = jasmine.createSpy('spy');
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.repositionSaveButton = spy;

    // Launch
    component.customScroller({on: (event, fn) => fn()}, () => done());

    // Assert
    expect(spy).toHaveBeenCalled();
  });

  // Layers
  it('should initialize layers', function () {
    // Mock
    let spy = jasmine.createSpy('spy');
    let component = getDefaultComponent();
    component.element = {find: spy};
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    component.initLayers();

    // Assert
    expect(spy.calls.count()).toEqual(7);
    expect(component.layers).toBe(component.scope.gridOptions.layers);
  });

  // Reposition save button
  it('should reposition save button', function () {
    // Mock
    let css = jasmine.createSpy('spy');
    let buttonPosition = jasmine.createSpy('buttonPosition');
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.layers = {save: {css: css}};
    component.editable = true;
    component.getButtonPosition = buttonPosition;

    // Launch
    component.repositionSaveButton();
    component.currentSelection = [2];
    component.repositionSaveButton();

    // Assert
    expect(css.calls.count()).toEqual(2);
    expect(buttonPosition.calls.count()).toEqual(1);
  });

  // Get button position
  it('should retrieve button position', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.layers = {
      save: {outerWidth: () => 120},
      clickout: {
        height: () => 300,
        first: () => ({offset: () => ({top: 0, left: 0, width: 1000, height: 420, scrollLeft: 10})}),
        last: () => ({width: () => 1000, height: () => 1000, scrollLeft: () => 10})
      },
      header: {outerHeight: () => 20},
      content: {
        find: () => ({
          last: () => ({
            length: 1,
            offset: () => ({top: 50, left: 0, width: 1000, height: 480, scrollLeft: 10}),
            outerHeight: () => 20,
            outerWidth: () => 800
          })
        })
      }
    };
    component.editable = true;
    component.getSelection = () => [2];

    // Launch
    let buttonPosition = component.getButtonPosition();

    // Assert
    expect(buttonPosition.top).toEqual(90);
    expect(buttonPosition.left).toEqual(690);
  });

  // Get cell object
  it('should retrieve cell object', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let cellObject1 = component.getCellObject(null);
    let cellObject2 = component.getCellObject("tutu");
    let cellObject3 = component.getCellObject(["tutu", "lala"]);
    let cellObject4 = component.getCellObject({value: "tutu"});
    let cellObject5 = component.getCellObject({value: "tutu", label: "lala"});
    let cellObject6 = component.getCellObject(undefined);
    let cellObject7 = component.getCellObject("");

    // Assert
    expect(cellObject1).toEqual({value: null, label: ""});
    expect(cellObject2).toEqual({value: "tutu", label: "tutu"});
    expect(cellObject3).toEqual({value: ["tutu", "lala"], label: "tutu, lala"});
    expect(cellObject4).toEqual({value: "tutu", label: "tutu"});
    expect(cellObject5).toEqual({value: "tutu", label: "lala"});
    expect(cellObject6).toEqual({value: null, label: ""});
    expect(cellObject7).toEqual({value: null, label: ""});
  });

  // Get cell
  it('should retrieve cell', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let cellObject1 = component.getCell(null);
    let cellObject2 = component.getCell("tutu");
    let cellObject3 = component.getCell(["tutu", "lala"]);
    let cellObject4 = component.getCell({value: "tutu"});
    let cellObject5 = component.getCell({value: "tutu", label: "lala"});
    let cellObject6 = component.getCell(undefined);
    let cellObject7 = component.getCell("");

    // Assert
    expect(cellObject1).toEqual({value: null, label: "", title: "", style: "", icon: ""});
    expect(cellObject2).toEqual({value: "tutu", label: "tutu", title: "", style: "", icon: ""});
    expect(cellObject3).toEqual({value: ["tutu", "lala"], label: "tutu, lala", title: "", style: "", icon: ""});
    expect(cellObject4).toEqual({value: "tutu", label: "tutu", title: "", style: "", icon: ""});
    expect(cellObject5).toEqual({value: "tutu", label: "lala", title: "", style: "", icon: ""});
    expect(cellObject6).toEqual({value: null, label: "", title: "", style: "", icon: ""});
    expect(cellObject7).toEqual({value: null, label: "", title: "", style: "", icon: ""});
  });

  // Get column data
  it('should retrieve column data', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let columnDataValue = component.getColumnData("value");
    let columnDataOther = component.getColumnData("other");
    let columnDataId = component.getColumnData("id");
    component.controller.sendAll = true;
    let columnDataValueAll = component.getColumnData("value");
    let columnDataOtherAll = component.getColumnData("other");
    let columnDataIdAll = component.getColumnData("id");

    // Assert
    expect(columnDataValue).toEqual({value: ["lele"], "value.selected": "lele"});
    expect(columnDataOther).toEqual({other: ["asda"], "other.selected": "asda"});
    expect(columnDataId).toEqual({id: [4], "id.selected": 4});
    expect(columnDataValueAll).toEqual({
      value: ["tutu", "lala", "lele", "lili", "lolo", "lulu"],
      "value.selected": "lele"
    });
    expect(columnDataOtherAll).toEqual({other: [null, null, "asda", null, null, null], "other.selected": "asda"});
    expect(columnDataIdAll).toEqual({id: [1, 2, 4, 5, 6, 7], "id.selected": 4});
  });

  // Get column identifier data
  it('should retrieve identifier column data', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let identifierColumnData = component.getIdentifierColumnData();
    component.controller.sendAll = true;
    let identifierColumnDataAll = component.getIdentifierColumnData();
    component.model.selected = [];
    let identifierColumnDataNoSelection = component.getIdentifierColumnData();

    // Launch
    component.controller.sendAll = false;
    component.model.selected = [4];
    let identifierColumnDataMO = component.getIdentifierColumnData();
    component.model.selected = [];
    let identifierColumnDataNoSelectionMO = component.getIdentifierColumnData();

    // Assert
    expect(identifierColumnData).toEqual({"componentId-id": [4],
        "selectedRowAddress": { view: 'viewId', component: 'componentId', row: 4 }});
    expect(identifierColumnDataAll).toEqual({"componentId-id": [1, 2, 4, 5, 6, 7],
      "selectedRowAddress": { view: 'viewId', component: 'componentId', row: 4 }});
    expect(identifierColumnDataNoSelection).toEqual({"componentId-id": [1, 2, 4, 5, 6, 7]});

    expect(identifierColumnDataMO).toEqual({"componentId-id": [4],
      "selectedRowAddress": { view: 'viewId', component: 'componentId', row: 4 }});
    expect(identifierColumnDataNoSelectionMO).toEqual({"componentId-id": []});
  });

  // Get data
  it('should retrieve grid data', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.addColumns(getDefaultColumnModel());
    component.getSpecificFields = () => ({});

    // Launch
    let data = component.getData();
    component.controller.sendAll = true;
    component.getExtraData = () => ({});
    let allData = component.getData();

    // Assert
    expect(data).toEqual({
      id: [4],
      "id.selected": 4,
      value: ["lele"],
      "value.selected": "lele",
      other: ["asda"],
      "other.selected": "asda",
      "componentId-id": [4],
      "componentId": [4],
      "componentId.data": {},
      "selectedRowAddress": { view: 'viewId', component: 'componentId', row: 4 }
    });
    expect(allData).toEqual({
      id: [1, 2, 4, 5, 6, 7],
      "id.selected": 4,
      value: ["tutu", "lala", "lele", "lili", "lolo", "lulu"],
      "value.selected": "lele",
      other: [null, null, "asda", null, null, null],
      "other.selected": "asda",
      "componentId-id": [1, 2, 4, 5, 6, 7],
      "componentId": [4],
      "componentId.data": {},
      "selectedRowAddress": { view: 'viewId', component: 'componentId', row: 4 }
    });
  });

  // Get print data
  it('should retrieve print data', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    let extraData = {max: 30, page: 1};
    commons.init();
    component.addColumns(getDefaultColumnModel());
    component.getSpecificFields = () => ({});

    // Launch
    let data = component.getPrintData();
    component.getExtraData = () => extraData;
    let data2 = component.getPrintData();

    // Assert
    expect(data).toEqual({
      id: [1, 2, 4, 5, 6, 7],
      value: ["tutu", "lala", "lele", "lili", "lolo", "lulu"],
      other: ["", "", "asda", "", "", ""],
      "componentId-id": [4],
      "componentId.data": {visibleColumns: {value: "Value", other: "Other thing"}},
      "selectedRowAddress": { view: 'viewId', component: 'componentId', row: 4 }
    });

    expect(data2).toEqual({
      id: [1, 2, 4, 5, 6, 7],
      value: ["tutu", "lala", "lele", "lili", "lolo", "lulu"],
      other: ["", "", "asda", "", "", ""],
      "componentId-id": [4],
      "componentId.data": {visibleColumns: {value: "Value", other: "Other thing"}},
      "selectedRowAddress": { view: 'viewId', component: 'componentId', row: 4 },
      ...extraData
    });
  });

  // Get column visible data
  it('should retrieve column visible data', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.addColumns(getDefaultColumnModel());
    let column = getDefaultColumnModel()[1];
    let row = component.model.values[2];

    // Simple column
    let visibleData = component.getVisibleData({component: "componentId", row: 4, column: "value"}, row, column);
    expect(visibleData).toEqual("lele");

    // Float column
    testVisibleValue(component, row, column, "float", undefined, {value: 123114.123, label: ""}, 123114.123);

    // Integer column
    testVisibleValue(component, row, column, "integer", undefined, {value: 123114, label: ""}, 123114);

    // Component numeric column without api
    component.getApi = undefined;
    column.type = "component";
    column.component = "numeric";
    row["value"] = 123114.31;
    visibleData = component.getVisibleData({component: "componentId", row: 4, column: "value"}, row, column);
    expect(visibleData).toEqual(123114.31);

    // Component numeric column without visible value function
    column.type = "component";
    column.component = "numeric";
    component.getApi = () => ({});
    row["value"] = 123114.31;
    visibleData = component.getVisibleData({component: "componentId", row: 4, column: "value"}, row, column);
    expect(visibleData).toEqual(123114.31);

    // Component numeric column
    testVisibleValue(component, row, column, undefined, "numeric", {value: 123114.31, label: "123.114,31"}, "123.114,31");

    // Component select column
    testVisibleValue(component, row, column, undefined, "select", {value: 21, label: "tutu"}, "tutu");

    // Component suggest column
    testVisibleValue(component, row, column, undefined, "suggest", {value: 21, label: "tutu"}, "tutu");

    // Component select-multiple column
    testVisibleValue(component, row, column, undefined, "select-multiple", {value: [21, 22], label: "tutu, lala"}, "tutu, lala");

    // Component suggest-multiple column
    testVisibleValue(component, row, column, undefined, "suggest-multiple", {value: [21, 22], label: "tutu, lala"}, "tutu, lala");

    // Component progress column
    testVisibleValue(component, row, column, undefined, "progress", {value: 21, label: "21%"}, "21%");

    // Component checkbox column
    testVisibleValue(component, row, column, undefined, "checkbox", {value: 1, label: true}, true);
    testVisibleValue(component, row, column, undefined, "checkbox", {value: 0, label: false}, false);

    // Component image column
    testVisibleValue(component, row, column, undefined, "image", {image: "file://tutu/lala.png"}, "file://tutu/lala.png");

    // Component icon column
    testVisibleValue(component, row, column, undefined, "icon", {icon: "check"},  "check");

    // Component password column
    testVisibleValue(component, row, column, undefined, "password", {value: "check", label: "alalal"},  "****");
  });

  // Get row values
  it('should retrieve row values from a rowId', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let rowValues = component.getRowValues("4");

    // Assert
    expect(rowValues).toEqual({"id": 4, "value": "lele", "other": "asda", "RowTyp": "UPDATE"});
  });

  // Get selected rows
  it('should retrieve selected rows', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let selectedRows = component.getSelectedRows();
    component.model.selected = null;
    let noSelectedRows = component.getSelectedRows();
    component.model.selected = [];
    let emptySelectedRows = component.getSelectedRows();

    // Assert
    expect(selectedRows).toEqual([4]);
    expect(noSelectedRows).toEqual(null);
    expect(emptySelectedRows).toEqual(null);
  });

  // Get selected row data
  it('should retrieve selected row data', function () {
    // Mock
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();

    // Launch
    let selectedRowData = component.getSelectedRowData();
    component.model.selected = null;
    let noSelectedRowData = component.getSelectedRowData();

    // Assert
    expect(selectedRowData).toEqual({"id": 4, "value": "lele", "other": "asda", "RowTyp": "UPDATE"});
    expect(noSelectedRowData).toEqual({});
  });

  // Update model
  it('should update the model', function () {
    // Mock
    let spy = jasmine.createSpy('spy');
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.currentSelection = [4];
    component.updateModelSpecific = () => ({then: (fn) => fn()});
    component.setSelection = spy;

    // Launch
    component.updateModel();
    component.initialized = true;
    component.updateModel();

    // Assert
    expect(spy).toHaveBeenCalled();
  });

  // Update a cell
  it('should update a cell', function () {
    // Mock
    let spy = jasmine.createSpy('spy');
    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.updateCellSpecific = spy;


    // Launch
    component.updateCell("3", "value", "lele");
    expect(spy).not.toHaveBeenCalled();

    component.updateCell("4", "value", {value: "alla", label: "lala"});
    expect(component.model.values[2]["value"]).toEqual({value: "alla", label: "lala"});
    component.updateCell("4", "value", {style: "alla", label: "lala"});
    expect(component.model.values[2]["value"]).toEqual({style: "alla", label: "lala"});
    component.updateCell("4", "value", "tutu");
    expect(component.model.values[2]["value"]).toEqual("tutu");
    component.updateCell("4", "value", null);
    expect(component.model.values[2]["value"]).toEqual(null);

    // Assert
    expect(spy).toHaveBeenCalled();
  });

  // Retrieve column index
  it('should retrieve column index', function () {
    // Mock

    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.addColumns(getDefaultColumnModel());

    // Launch
    let columnIndex1 = component.getColumnIndex("value");
    let columnIndex2 = component.getColumnIndex("other");
    let columnIndex3 = component.getColumnIndex("lala");

    // Assert
    expect(columnIndex1).toEqual(1);
    expect(columnIndex2).toEqual(2);
    expect(columnIndex3).toEqual(-1);
  });

  // Retrieve column value list
  it('should retrieve column value list', function () {
    // Mock

    let component = getDefaultComponent();
    let commons = new GridCommons(component);
    commons.init();
    component.addColumns(getDefaultColumnModel());

    // Launch
    let columnValues1 = component.getColumnValueList({view: "viewId", component: "componentId", row: 4, column:"value"});

    // Assert
    expect(columnValues1).toEqual(null);
  });
});