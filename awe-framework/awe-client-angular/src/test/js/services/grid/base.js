describe('awe-framework/awe-client-angular/src/test/js/services/grid/base.js', function () {
  let $injector, GridBase, GridEvents, $rootScope;
  let originalTimeout;
  const currentModel = {};
  const currentController = {columnModel: []};

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      GridBase = $injector.get('GridBase');
      GridEvents = $injector.get('GridEvents');
      $rootScope = $injector.get('$rootScope');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function () {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should initialize a grid', function () {
    // Mock
    let $scope = $rootScope.$new();
    let grid = new GridBase($scope, "id", {});
    spyOn(grid, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    grid.api = {};
    grid.model = currentModel;
    grid.controller = currentController;

    // Assert
    expect(grid.asGrid()).toBe(true);
  });

  it('should delete a specific row not existent', function () {
    // Mock
    let $scope = $rootScope.$new();
    $scope.gridOptions = {icons: {}};
    let grid = new GridBase($scope, "id", {});
    spyOn(grid, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    grid.api = {};
    grid.model = {
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
    };
    grid.controller = currentController;
    grid.asGrid();

    grid.deleteRowSpecific("3");

    // Assert
    expect(grid.model.values.length).toBe(6);
  });

  it('should delete a specific row', function () {
    // Mock
    let $scope = $rootScope.$new();
    $scope.gridOptions = {icons: {}};
    let grid = new GridBase($scope, "id", {});
    spyOn(grid, "asComponent").and.returnValue(true);
    spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
    spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
    grid.api = {};
    grid.model = {
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
    };
    grid.controller = currentController;
    grid.asGrid();
    grid.grid.measures = {};

    grid.deleteRowSpecific("4");

    // Assert
    expect(grid.model.values.length).toBe(5);
  });

});