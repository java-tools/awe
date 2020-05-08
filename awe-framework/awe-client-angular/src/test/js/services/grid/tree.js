describe('awe-framework/awe-client-angular/src/test/js/services/grid/tree.js', function () {
  let $injector, GridTree, GridEvents, $rootScope, $utilities;
  let originalTimeout;
  const currentModel = {
    records: 6,
    total: 1,
    page: 1,
    values: [
      {_ID: 1, value: "tutu", RowTyp: "INSERT", $$children: [], $$treeLevel: 0},
      {_ID: 2, value: "lala", $$children: [], $$treeLevel: 0},
      {_ID: 4, value: "lele", other: "asda", RowTyp: "UPDATE", $$parent: {$$children: [], $$isLeaf: false}, $$children: [], $$treeLevel: 2},
      {_ID: 5, value: "lili", $$children: [], $$treeLevel: 0},
      {_ID: 6, value: "lolo", RowTyp: "DELETE", $$children: [], $$treeLevel: 0},
      {_ID: 7, value: "lulu", $$children: [], $$treeLevel: 0}],
    selected: [4]
  };
  const currentController = {columnModel: []};

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      GridTree = $injector.get('GridTree');
      GridEvents = $injector.get('GridEvents');
      $rootScope = $injector.get('$rootScope');
      $utilities = $injector.get('AweUtilities');
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

  describe('once initialized', function () {
    function initTree(model, controller) {
      // Mock
      let $scope = $rootScope.$new();
      $scope.gridOptions = {icons: {}};
      let tree = new GridTree($scope, "id", {});
      spyOn(tree, "asComponent").and.returnValue(true);
      spyOn(GridEvents, "mapCommonActions").and.returnValue(true);
      spyOn(GridEvents, "mapTreeActions").and.returnValue(true);
      tree.api = {};
      tree.model = JSON.parse(JSON.stringify(model));
      tree.controller = JSON.parse(JSON.stringify(controller));
      tree.asTree();
      tree.grid.api = {
        core: {notifyDataChange: jasmine.createSpy("notifyDataChange")},
        treeBase: {
          expandRow: jasmine.createSpy("expandRow"),
          collapseRow: jasmine.createSpy("collapseRow")
        },
        selection: {clearSelectedRows: jasmine.createSpy("clearSelectedRows")}
      };
      return tree;
    }

    it('should expand a tree branch', function () {
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.toggleTreeRow(row, true);

      // Assert
      expect(row.$$expanded).toBe(true);
    });

    it('should expand a tree branch on load all', function () {
      let tree = initTree({...currentModel}, {...currentController, loadAll: true});
      let row = {};
      tree.toggleTreeRow(row, true);

      // Assert
      expect(row.$$expanded).toBe(true);
      expect(tree.grid.api.treeBase.expandRow).toHaveBeenCalled();
    });

    it('should collapse a tree branch on load all', function () {
      let tree = initTree({...currentModel}, {...currentController, loadAll: true});
      let row = {};
      tree.toggleTreeRow(row, false);

      // Assert
      expect(row.$$expanded).toBe(false);
      expect(tree.grid.api.treeBase.collapseRow).toHaveBeenCalled();
    });

    it('should add a child row', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(2);
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.addRowSpecific("4", "child", row);

      // Assert
      expect(tree.grid.api.core.notifyDataChange).toHaveBeenCalled();
      expect(tree.grid.api.selection.clearSelectedRows).toHaveBeenCalled();
    });

    it('should add a row after selected', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(2);
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.addRowSpecific("4", "after", row);

      // Assert
      expect(tree.grid.api.core.notifyDataChange).toHaveBeenCalled();
      expect(tree.grid.api.selection.clearSelectedRows).toHaveBeenCalled();
    });

    it('should add a row before selected', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(2);
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.addRowSpecific("4", "before", row);

      // Assert
      expect(tree.grid.api.core.notifyDataChange).toHaveBeenCalled();
      expect(tree.grid.api.selection.clearSelectedRows).toHaveBeenCalled();
    });

    it('should add a child row without parent', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(1);
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.addRowSpecific("2", "child", row);

      // Assert
      expect(tree.grid.api.core.notifyDataChange).toHaveBeenCalled();
      expect(tree.grid.api.selection.clearSelectedRows).toHaveBeenCalled();
    });

    it('should add a row after selected without parent', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(1);
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.addRowSpecific("2", "after", row);

      // Assert
      expect(tree.grid.api.core.notifyDataChange).toHaveBeenCalled();
      expect(tree.grid.api.selection.clearSelectedRows).toHaveBeenCalled();
    });

    it('should add a row before selected without parent', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(1);
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.addRowSpecific("2", "before", row);

      // Assert
      expect(tree.grid.api.core.notifyDataChange).toHaveBeenCalled();
      expect(tree.grid.api.selection.clearSelectedRows).toHaveBeenCalled();
    });

    it('should add a row on top', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(2);
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.addRowSpecific("4", "first", row);

      // Assert
      expect(tree.grid.api.core.notifyDataChange).toHaveBeenCalled();
      expect(tree.grid.api.selection.clearSelectedRows).toHaveBeenCalled();
    });

    it('should add a row at bottom', function () {
      let tree = initTree({...currentModel}, {...currentController});
      let row = {};
      tree.addRowSpecific(null, "child", row);

      // Assert
      expect(tree.grid.api.core.notifyDataChange).toHaveBeenCalled();
      expect(tree.grid.api.selection.clearSelectedRows).toHaveBeenCalled();
    });

    it('should delete a row without parent', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(1);
      let tree = initTree({...currentModel}, {...currentController, loadAll: true});
      tree.deleteRowSpecific("2");

      // Assert
      expect(tree.model.values.length).toBe(5);
    });

    it('should delete a row not existent', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(-1);
      let tree = initTree({...currentModel}, {...currentController, loadAll: true});
      tree.deleteRowSpecific("3");

      // Assert
      expect(tree.model.values.length).toBe(6);
    });

    it('should delete a row', function () {
      spyOn($utilities, "getRowIndex").and.returnValue(2);
      let tree = initTree({...currentModel}, {...currentController, loadAll: true});
      tree.deleteRowSpecific("4");

      // Assert
      expect(tree.model.values.length).toBe(5);
    });
  });
});