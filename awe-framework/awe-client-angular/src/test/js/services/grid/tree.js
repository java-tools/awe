describe('awe-framework/awe-client-angular/src/test/js/services/grid/tree.js', function () {
  let $injector, GridTree, GridEvents, $rootScope;
  let originalTimeout;
  const currentModel = {};
  const currentController = {columnModel: []};

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

  it('should delete a specific row not existent', function () {
    // Mock
    let $scope = $rootScope.$new();
    $scope.gridOptions = {icons: {}};
    let tree = new GridTree($scope, "id", {});
    spyOn(tree, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    tree.api = {};
    tree.model = {
      records: 6,
        total: 1,
        page: 1,
        values: [{_ID: 1, value: "tutu", RowTyp: "INSERT"}, {_ID: 2, value: "lala"}, {
          _ID: 4,
        value: "lele",
        other: "asda",
        RowTyp: "UPDATE"
      }, {_ID: 5, value: "lili"}, {_ID: 6, value: "lolo", RowTyp: "DELETE"}, {_ID: 7, value: "lulu"}],
        selected: [4]
    };
    tree.controller = {...currentController, loadAll: true};
    tree.asTree();
    tree.grid.api = {treeBase:{collapseRow: jasmine.createSpy("collapseRow")}};

    tree.deleteRowSpecific("3");

    // Assert
    expect(tree.model.values.length).toBe(6);
  });

  it('should delete a specific row', function () {
    // Mock
    let $scope = $rootScope.$new();
    $scope.gridOptions = {icons: {}};
    let tree = new GridTree($scope, "id", {});
    spyOn(tree, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    tree.api = {};
    tree.model = {
      records: 6,
      total: 1,
      page: 1,
      values: [{_ID: 1, value: "tutu", RowTyp: "INSERT"}, {_ID: 2, value: "lala"}, {
        _ID: 4,
        value: "lele",
        other: "asda",
        RowTyp: "UPDATE",
        $$parent: {$$children:[], $$isLeaf: false},
        $$children: [],
        $$treeLevel: 2
      }, {_ID: 5, value: "lili"}, {_ID: 6, value: "lolo", RowTyp: "DELETE"}, {_ID: 7, value: "lulu"}],
      selected: [4]
    };
    tree.controller = {...currentController, loadAll: true};
    tree.asTree();
    tree.grid.api = {treeBase:{collapseRow: jasmine.createSpy("collapseRow")}};

    tree.deleteRowSpecific("4");

    // Assert
    expect(tree.model.values.length).toBe(5);
  });
});