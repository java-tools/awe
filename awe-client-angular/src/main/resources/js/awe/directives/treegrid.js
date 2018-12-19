import { aweApplication } from "./../awe";

// Treegrid directive
aweApplication.directive('aweTreeGrid',
  ['ServerData', 'GridTree', 'Options', 'AweSettings',
    function (serverData, GridTree, Options, $settings) {
      // Retrieve default $settings

      // Set default options
      var options = {
        // Elements per page
        rowNum: $settings.get("recordsPerPage"),
        // Total width
        totalWidth: true,
        /* **************************
         * Default formatting options
         * ***************************/
        formatOptions: {
          // Decimal separator
          decimalSeparator: $settings.get("numericOptions").aDec,
          // Thousands separator
          thousandsSeparator: $settings.get("numericOptions").aSep,
          // Decimal places
          decimalPlaces: $settings.get("numericOptions").mDec
        }
      };

      return {
        restrict: 'E',
        priority: 1001,
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('treegrid');
        },
        scope: {
          'gridId': '@treeGridId'
        },
        compile: function () {
          return {
            /**
             * Pregeneration function
             * @param {Object} scope
             * @param {Object} elem
             */
            pre: function (scope, elem) {
              // Init as component
              var component = new GridTree(scope, scope.gridId, elem);
              if (component.asTree()) {
                // Set bigGrid value
                component.bigGrid = true;

                // Set spin options
                scope.spinOptions = Options.spin.big;

                // Define default values
                component.controller.treeId = component.controller.treeId || "id";
                component.controller.treeParent = component.controller.treeParent || "parent";
                component.controller.treeLeaf = component.controller.treeLeaf || "isLeaf";

                // Update grid styles
                component.gridStyle = "grid-" + scope.size + " " + (component.controller.style || "");
                component.gridButtonClass = "btn btn-" + scope.size;

                // Fix column model
                component.fixColumnModel(true);
                scope.gridOptions = _.assign({}, options, {
                  columnDefs: component.controller.columnModel,
                  data: [],
                  enableRowSelection: true,
                  enableFullRowSelection: true,
                  enableRowHeaderSelection: false,
                  multiSelect: component.controller.multiselect,
                  noUnselect: component.controller.editable && !component.controller.multiselect,
                  enableColumnResizing: true,
                  enableFiltering: component.controller.enableFilters,
                  enableSorting: true,
                  useExternalSorting: !component.controller.loadAll,
                  useExternalPagination: !component.controller.loadAll,
                  fastWatch: true,
                  flatEntityAccess: true,
                  rowTemplate: "grid/row",
                  paginationTemplate: "grid/pagination",
                  rowHeight: 27,
                  //headerHeight: 28,
                  node: elem,
                  virtualizationThreshold: 50,
                  showColumnFooter: component.controller.showTotals,
                  icons: {
                    menu: '<i class="fa fa-bars"/>',
                    filter: '<i class="fa fa-filter"/>',
                    sortAscending: '<i class="fa fa-sort-alpha-asc"/>',
                    sortDescending: '<i class="fa fa-sort-alpha-desc"/>',
                    expand: "fa-" + (component.controller.iconExpand || 'plus-square'),
                    collapse: "fa-" + (component.controller.iconCollapse || 'minus-square'),
                    leaf: "fa-" + (component.controller.iconLeaf || 'dot-circle-o'),
                    loading: "fa-refresh fa-spin"
                  },
                  onRegisterApi: function (gridApi) {
                    component.grid.api = gridApi;
                    // Init grid
                    component.initGrid();
                  }
                });
              } else {
                scope.gridOptions = {data: []};
              }
            }
          };
        }
      };
    }
  ]);
