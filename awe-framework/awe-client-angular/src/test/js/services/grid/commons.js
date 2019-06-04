describe('awe-framework/awe-client-angular/src/test/js/services/grid/commons.js', function() {
  let $injector, GridCommons;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      GridCommons = $injector.get('GridCommons');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Fix column
  it('should fix a column', function() {
    // Mock
    let component = {constants:{ROW_IDENTIFIER: "id"}, controller: {columnModel: []}, scope: {$on: () => null, charSize: 7}, enableSorting: true, listeners: {}, api: {}, model: {}, address: {view: "viewId", component: "componentId"}};
    let column = {id: "tutu", component: "text", enableFiltering: true, charlength: "0"};
    let column2 = {id: "tutu", hidden: true, component: "text", summaryType: "SUM", enableFiltering: true, index: "lala", charlength: 20, sortable: true};
    let column3 = {id: "tutu", hidden: true, summaryType: "SUM", sortable: false, index: "lala", charlength: 20};
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
    it('should select a list of rows in a grid', function() {
      // Mock
      var spy = jasmine.createSpy('spy');
      let component = {constants:{ROW_IDENTIFIER: "id"}, controller: {columnModel: []}, scope: {$on: () => null, charSize: 7}, enableSorting: true, listeners: {}, api: {}, model: {
        values: [{id: 1}, {id: 2}, {id: 4}, {id: 5}, {id: 6}, {id: 7}]
      }, address: {view: "viewId", component: "componentId"}, resetSelection: () => null, selectRow: () => null, storeEvent: () => null};
      let commons = new GridCommons(component);
      let selection = ["1", "2", "3", "4"];
      commons.init();
      component.onSelectRows = spy;

      // Launch
      component.setSelection(selection);

      // Assert
      expect(spy).toHaveBeenCalledWith([1,2,4]);
    });
});