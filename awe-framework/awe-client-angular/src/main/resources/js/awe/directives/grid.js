import { aweApplication } from "./../awe";
import { DefaultGridOptions, DefaultSpin } from "../data/options";
import "./../services/grid/base";
import "./gridHeader";

// Grid directive
aweApplication.directive('aweGrid',
  ['ServerData', 'AweSettings', 'GridBase', 'AweUtilities',
    function (serverData, $settings, GridBase, $utilities) {
      // Retrieve default $settings

      // Set default options
      let options = {
        ...DefaultGridOptions,
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
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('grid');
        },
        scope: {
          'gridId': '@'
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
              var component = new GridBase(scope, scope.gridId, elem);
              if (component.asGrid()) {
                // Set bigGrid value
                component.bigGrid = true;

                // Set spin options
                scope.spinOptions = DefaultSpin.big;

                // Update grid styles
                component.gridStyle = "grid-" + scope.size + " " + (component.controller.style || "");
                component.gridButtonClass = "btn btn-" + scope.size;
                component.gridPaginationClass = "pagination pagination-" + scope.size;
                component.enableSorting = !$utilities.isEmpty(component.controller.targetAction) || component.controller.loadAll;

                // Fix column model
                component.fixColumnModel(true);
                scope.gridOptions = _.assign({}, options, {
                  columnDefs: component.controller.columnModel,
                  data: [],
                  enableRowSelection: true,
                  enableFullRowSelection: true,
                  enableRowHeaderSelection: false,
                  enablePagination: true,
                  enablePaginationControls: false,
                  paginationPageSize: component.getMax(),
                  multiSelect: component.controller.multiselect,
                  noUnselect: component.controller.editable && !component.controller.multiselect,
                  enableColumnResizing: true,
                  enableFiltering: component.controller.enableFilters,
                  enableSorting: component.enableSorting,
                  enableColumnMoving: true,
                  useExternalSorting: !component.controller.loadAll,
                  useExternalPagination: !component.controller.loadAll,
                  fastWatch: true,
                  flatEntityAccess: true,
                  rowTemplate: "grid/row",
                  paginationTemplate: "grid/pagination",
                  rowHeight: (parseInt(component.controller.rowHeight, 10) || 27),
                  enableMinHeightCheck: false,
                  excessRows: 50,
                  excessColumns: 10,
                  scrollThreshold: 25,
                  virtualizationThreshold: 50,
                  //headerHeight: 28,
                  node: elem,
                  //rowNum: scope.controller.max,
                  showColumnFooter: component.controller.showTotals,
                  icons: {
                    menu: '<i class="fa fa-bars"/>',
                    filter: '<i class="fa fa-filter"/>',
                    sortAscending: '<i class="fa fa-sort-alpha-asc"/>',
                    sortDescending: '<i class="fa fa-sort-alpha-desc"/>'
                  },
                  onRegisterApi: function (gridApi) {
                    component.grid.api = gridApi;
                    // Init grid
                    component.initGrid();
                  },
                  customScroller: (uiGridViewport, scrollHandler) => component.customScroller(uiGridViewport, scrollHandler)
                });
                scope.onGoToPageChanged = function(event) {
                  if(event.type === "keypress") {
                    // If key pressed is ENTER
                    if(event.keyCode === 13) {
                      component.setPage(parseInt(event.currentTarget.value, 10));
                      event.stopPropagation();
                      event.preventDefault();
                      event.currentTarget.value = "";
                    }
                  } else if(event.type === "blur"){
                    var page = parseInt(event.currentTarget.value, 10);
                    if(component.model.page !== page) {
                      component.setPage(page);
                      event.currentTarget.value = "";
                    }
                  }
                };
              } else {
                scope.gridOptions = {data: []};
              }
            }
          };
        }
      };
    }
  ]);
