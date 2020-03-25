describe('awe-framework/awe-client-angular/src/test/js/services/grid/tree.js', function () {
  let $injector, GridTree, GridEvents, $rootScope;
  let originalTimeout;
  const currentModel = {};
  const currentController = {columnModel: []};
  const getDefaultComponent = () => ({
    id: "id",
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

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      GridTree = $injector.get('GridTree');
      GridEvents = $injector.get('GridEvents');
      $rootScope = $injector.get('$rootScope');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function () {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should initialize a tree', function () {
    // Mock
    let $scope = $rootScope.$new();
    let tree = new GridTree($scope, "id", {});
    spyOn(tree, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    tree.api = {};
    tree.model = currentModel;
    tree.controller = currentController;

    // Assert
    expect(tree.asTree()).toBe(true);
  });

  it('should expand a tree branch', function () {
    // Mock
    let $scope = $rootScope.$new();
    $scope.gridOptions = {icons: {}};
    let tree = new GridTree($scope, "id", {});
    spyOn(tree, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    tree.api = {};
    tree.model = currentModel;
    tree.controller = currentController;
    tree.asTree();

    let row = {};
    tree.toggleTreeRow(row, true);

    // Assert
    expect(row.$$expanded).toBe(true);
  });

  it('should expand a tree branch on load all', function () {
    // Mock
    let $scope = $rootScope.$new();
    $scope.gridOptions = {icons: {}};
    let tree = new GridTree($scope, "id", {});
    spyOn(tree, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    tree.api = {};
    tree.model = currentModel;
    tree.controller = {...currentController, loadAll: true};
    tree.asTree();
    tree.grid.api = {treeBase:{expandRow: jasmine.createSpy("expandRow")}};

    let row = {};
    tree.grid.api.treeBase.expandRow = jasmine.createSpy("expandRow");
    tree.toggleTreeRow(row, true);

    // Assert
    expect(row.$$expanded).toBe(true);
    expect(tree.grid.api.treeBase.expandRow).toHaveBeenCalled();
  });

  it('should collapse a tree branch on load all', function () {
    // Mock
    let $scope = $rootScope.$new();
    $scope.gridOptions = {icons: {}};
    let tree = new GridTree($scope, "id", {});
    spyOn(tree, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    tree.api = {};
    tree.model = currentModel;
    tree.controller = {...currentController, loadAll: true};
    tree.asTree();
    tree.grid.api = {treeBase:{collapseRow: jasmine.createSpy("collapseRow")}};

    let row = {};
    tree.toggleTreeRow(row, false);

    // Assert
    expect(row.$$expanded).toBe(false);
    expect(tree.grid.api.treeBase.collapseRow).toHaveBeenCalled();
  });
});