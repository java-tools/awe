import { aweApplication } from "./../../awe";
import "./commons";

// Base grid service
aweApplication.factory('GridBase',
  ['AweUtilities', 'Control', 'Component', 'GridEvents', 'GridCommons', '$translate', '$filter', '$log',
    /**
     * Grid generic methods
     * @param {service} Utilities Utilities service
     * @param {service} Control Control service
     * @param {service} GridEvents Grid events
     * @param {service} GridCommons Grid common functions
     * @param {service} $translate Translation service
     * @param {service} $filter Filtering service
     * @param {service} $log Log service
     * @param {service} Component Component service
     */
    function (Utilities, Control, Component, GridEvents, GridCommons, $translate, $filter, $log) {

      /**
       * Grid constructor
       * @param {Scope} scope Criterion scope
       * @param {String} id Criterion id
       * @param {String} element Criterion element
       */
      var GridBase = function (scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Component(this.scope, this.id);
        this.component.element = element;
        this.component.currentPage = 1;
        this.component.grid = this;
        this.sorting = [];
        this.component.isTree = false;
        this.component.asGrid = function () {
          return this.grid.init();
        };

        return this.component;
      };

      GridBase.prototype = {
        /**
         * Initialize base grid
         */
        init: function () {
          // Get component
          var grid = this;
          var component = this.component;
          if (!component.asComponent()) {
            return false;
          }

          // Define 'CONSTANTS'
          component.constants = {
            SMALL_GRID: 750,
            ROW_IDENTIFIER: "id",
            ROW_CLASS_FIELD: "_style_",
            CELL_TITLE: "title",
            CELL_LABEL: "label",
            CELL_VALUE: "value",
            CELL_STYLE: "cell-style",
            CELL_ICON: "icon",
            CELL_IMAGE: "image"
          };

          // Initialize common methods
          this.commons = new GridCommons(this.component);

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * Retrieve row number
           * @param {type} row
           */
          component.getRowNumber = function (row) {
            return grid.api.core.getVisibleRows().indexOf(row) + getFirstRecord();
          };
          /**
           * Select all rows
           * @param {type} event
           */
          component.selectAllClick = function (event) {
            if (grid.api) {
              if (!grid.api.selection.getSelectAllState()) {
                grid.api.selection.clearSelectedRows(event);
              } else {
                grid.api.selection.selectAllRows(event);
              }
            }
          };

          /**
           * Change the page to the stored one
           */
          component.changePage = function () {
            component.setPage(component.currentPage);
          };

          /**
           * Set the current page
           * @param {number} page Page to set
           */
          component.setPage = function (page) {
            component.currentPage = page;
            if (page >= 1 && page <= component.model.total) {
              if (component.controller.loadAll) {
                component.controller.loading = true;
                if (grid.api) {
                  component.scope.gridOptions.totalItems = component.model.records;
                  grid.api.pagination.seek(page);
                }
              } else {
                pageUpdated();
              }
            }
          };

          /**
           * Grid initialization
           */
          component.initGrid = function () {
            // Get row identity
            component.scope.gridOptions.getRowIdentity = function (row) {
              return row.entity[component.constants.ROW_IDENTIFIER];
            };
            // Broadcast rows rendered
            grid.api.core.on.rowsRendered(grid.scope, function () {
              Utilities.timeout.cancel(grid.rowsRenderTimeout);
              grid.rowsRenderTimeout = Utilities.timeout(function () {
                Utilities.publishFromScope("rows-rendered", {grid: grid.id}, grid.scope);
              });
            });
            // Manage on cell select events
            grid.api.selection.on.rowSelectionChanged(grid.scope, onSelectRow);
            grid.api.selection.on.rowSelectionChangedBatch(grid.scope, onSelectRow);
            grid.api.colMovable.on.columnPositionChanged(grid.scope, onChangeColumns);
            grid.api.colResizable.on.columnSizeChanged(grid.scope, onChangeColumns);
            grid.api.core.on.filterChanged(grid.scope, launchFilter);
            grid.api.core.on.rowsVisibleChanged(grid.scope, onFilter);
            // Manage scroll events
            grid.api.core.on.scrollBegin(grid.scope, scrollStart);
            grid.api.core.on.scrollEnd(grid.scope, scrollEnd);
            // Manage sort
            grid.api.core.on.sortChanged(grid.scope, onSort);
            // Manage pagination
            grid.api.pagination.on.paginationChanged(grid.scope, onPagination);
            // End initialization
            Utilities.timeout(function () {
              // Initialize grid layers
              component.initLayers();
              // Initialize grid measures
              initMeasures();
              // Report changed columns
              component.reportChangedColumns();
              // Filter selection on initialization
              component.currentSelection = component.model.selected || [];
              // Initialize grid
              component.updateModel();
              component.initialized = true;
              // Resize grid
              component.resize();
            });
          };
          /**
           * Add initial columns
           */
          component.addInitialColumns = function () {
            // Add checkboxes
            if (component.controller.multiselect) {
              var checkboxes = {
                width: 26,
                label: "",
                displayName: "",
                name: "chkBox",
                frozen: component.hasFrozen,
                headerCellTemplate: "grid/headerCheckbox",
                cellTemplate: "grid/cellCheckbox",
                enableColumnResizing: false,
                enableColumnMoving: false,
                enableColumnMenu: false,
                enableFiltering: false,
                enableSorting: false
              };
              component.controller.columnModel.unshift(checkboxes);
            }
            // Add row numbers first
            if (component.controller.rowNumbers) {
              var rowNumbers = {
                minWidth: 30,
                width: 30,
                label: "",
                displayName: "",
                name: "rowNum",
                frozen: component.hasFrozen,
                headerCellTemplate: "grid/header",
                cellTemplate: "grid/cellRowNumber",
                enableColumnResizing: false,
                enableColumnMoving: false,
                enableColumnMenu: false,
                enableFiltering: false,
                enableSorting: false
              };
              component.controller.columnModel.unshift(rowNumbers);
            }
          };

          /**
           * Update the model (specific)
           */
          component.updateModelSpecific = function () {
            // Update model
            component.scope.gridOptions.data = component.model.values;
            component.currentPage = component.model.page;
            // On rows rendered, publish
            return deferRowsRendered(deferred => {
              // Update pagination text
              updatePaginationText();
              // Update row number column size
              updateRowNumberColumn();
              // Publish model changed
              Control.publishModelChanged(component.address, {values: component.model.values});
              //component.calculateTotals();
              component.updateGridScrollBars();
              // Store event
              component.storeEvent('change');
              // Resolve promise
              deferred.resolve();
            });
          };
          /**
           * Select the rows
           */
          component.resetSelection = function () {
            if (grid.api) {
              grid.api.selection.clearSelectedRows();
            }
          };
          /**
           * Get the selected rows
           */
          component.getSelection = function () {
            var selectedRows;
            if (grid.api) {
              selectedRows = grid.api.selection.getSelectedRows();
            } else {
              selectedRows = [];
            }
            return selectedRows;
          };
          /**
           * Select the row
           * @param {Object} row Row to select
           */
          component.selectRow = function (row) {
            // Reset selection
            if (grid.api) {
              grid.api.selection.selectRow(row);
            }
          };
          /**
           * Generate a sorting algorithm
           * @param {type} a
           * @param {type} b
           * @returns {function}
           */
          component.sortingAlgorithm = function (a, b) {
            var nulls = grid.api.core.sortHandleNulls(a, b);
            if (nulls !== null) {
              return nulls;
            } else {
              var valA, valB;
              if (typeof a === 'object' && typeof b === 'object') {
                valA = a.value;
                valB = b.value;
              } else {
                valA = a;
                valB = b;
              }

              if (valA < valB) {
                return -1;
              } else if (valA > valB) {
                return 1;
              } else {
                return 0;
              }
            }
          };
          /**
           * Show the column
           * @param {string} columnId Column to show
           */
          component.showColumn = function (columnId) {
            // Check if column is already shown
            var column = grid.api.grid.getColumn(columnId);
            if (column.colDef.hidden) {
            	column.colDef.hidden = false;
            	column.colDef.width = column.colDef.initialWidth;
            	column.width = column.colDef.initialWidth;
            	column.drawnWidth = column.colDef.initialWidth;
            	finishPendingActions();
              Utilities.timeout(onChangeColumns);
            }
          };
          /**
           * Hide the column
           * @param {string} columnId Column to hide
           */
          component.hideColumn = function (columnId) {
            // Check if column is already hidden
            var column = grid.api.grid.getColumn(columnId);
            if (!column.colDef.hidden) {
            	column.colDef.hidden = true;
            	column.colDef.width = 0;
            	column.width = 0;
            	column.drawnWidth = 0;
              finishPendingActions();
              Utilities.timeout(onChangeColumns);
            }
          };
          /**
           * Change the column label
           * @param {string} columnId Column to change the label to
           * @param {string} label New label
           */
          component.changeColumnLabel = function (columnId, label) {
            // Retrieve the column index
            var column = grid.api.grid.getColumn(columnId);
            if (column.displayName !== label) {
              column.displayName = label;
              finishPendingActions();
            }
          };

          /**
           * On grid resize
           */
          component.resize = function () {
            // Resize grid
            if (component.initialized) {
              Utilities.timeout.cancel(component.resizeTimer);
              component.resizeTimer = Utilities.timeout(resize);
            }
          };
          /**
           * Retrieve grid columns;
           */
          component.getColumns = function () {
            var columns = component.controller.columnModel;
            // Resize grid
            if (grid.api) {
              columns = grid.api.grid.columns;
            }
            return columns;
          };
          /**
           * Basic getSpecificFields function (To be overwritten on complex directives)
           * @returns {Object} Specific fields from component
           */
          component.getSpecificFields = function () {
            // Retrieve specific query data
            return {
              max: component.controller.loadAll ? 0 : component.getMax(),
              page: component.currentPage,
              sort: grid.sorting
            };
          };
          /**
           * Adds a new row
           * @param {string} row Selected row
           * @param {integer} position
           * @param {object} data initial data
           * @return {object} Promise
           */
          component.addRowSpecific = function (row, position, data) {
            // Get selected row
            var newId;
            // Remove previous selected rows
            component.unselectRows();
            // Create data for the row (empty)
            var rowData = data || {};
            newId = data && data.id ? data.id : "new-row-" + component.addedRows;
            component.addedRows++;
            data[component.constants.ROW_IDENTIFIER] = newId;
            // Generate new row
            var rowIndex = component.model.values.length;
            if (row) {
              var foundRowIndex = Control.getRowIndex(component.model.values, row, component.constants.ROW_IDENTIFIER);
              if (foundRowIndex != -1) {
                rowIndex = foundRowIndex;
              }
            }

            // Calculate rowIndex depending on position
            switch (position) {
              case "first":
                rowIndex = 0;
                break;
              case "after":
              case "child":
                rowIndex++;
                break;
              case "before":
              default:
                break;
            }

            // Add data to the model
            component.model.values.splice(rowIndex, 0, rowData);
            // Add a record
            changeRecords(component.model.records + 1);
            // Update the model
            component.updateModelSpecific();
            // Retrieve new id
            return deferRowsRendered(function (deferred) {
              // Resolve promise
              deferred.resolve(newId);
              // Show new row
              Utilities.timeout(function () {
                component.repositionSaveButton();
                if (grid.api) {
                  var gridRow = grid.api.grid.getRow(component.model.values[rowIndex]);
                  grid.api.core.scrollToIfNecessary(gridRow, null);
                }
              });
            });
          };
          /**
           * Removes the selected row
           * @param {string} row Selected row
           */
          component.deleteRowSpecific = function (row) {
            // If selectedRow is not null, remove row
            if (row) {
              // Calculate rowIndex
              var rowIndex = Control.getRowIndex(component.model.values, row, component.constants.ROW_IDENTIFIER);
              // Remove data from the model
              component.model.values.splice(rowIndex, 1);
              // Remove a record
              changeRecords(component.model.records - 1);
              // Publish model changed
              component.updateModelSpecific();
            }
            return deferRowsRendered();
          };
          /**
           * Updates the row
           */
          component.updateRowSpecific = function () {
            if (grid.api) {
              grid.api.core.notifyDataChange(uiGridConstants.dataChange.EDIT);
            }
          };
          /**
           * Check if grid size has changed & update new measures if changed
           * return {boolean} size has changed
           */
          component.gridSizeChanged = function () {
            var size = getGridSize();
            var changed = size.width > 0 && size.height > 0 && (
                grid.measures.height !== size.height || grid.measures.width !== size.width ||
                grid.measures.modelLength !== component.model.values.length);
            if (changed) {
              grid.measures = {
                ...size,
                modelLength: component.model.values.length
              };
            }
            return changed;
          };
          /**
           * Update grid scroll bars if changed visibility
           */
          component.updateGridScrollBars = function () {
            if (grid.api) {
              var contentSize = {width: 0, height: 0};
              $(component.layers["content"]).each(function () {
                contentSize.width += $(this).width();
                contentSize.height += $(this).height();
              });
              component.scope.gridOptions.enableHorizontalScrollbar = contentSize.width > component.layers["contentWrapper"].width() ? 1 : 0;
              component.scope.gridOptions.enableVerticalScrollbar = contentSize.height > component.layers["contentWrapper"].height() - component.layers["header"].height() ? 1 : 0;
              grid.api.core.handleWindowResize();
              grid.api.core.queueGridRefresh();
            }
          };

          /**
           * Change number of rows by page (pager)
           */
          component.updateRowsByPage = function () {
            // Set pagination size
            component.scope.gridOptions.paginationPageSize = component.getMax();

            // Reload grid
            pageUpdated();
          };
          /**
           * Check if grid is visible
           */
          component.isGridVisible = function () {
            return Utilities.isVisible(grid.element[0]);
          };

          /**
           * Update pagination text
           */
          component.updatePaginationText = function () {
            updatePaginationText();
          };
          /******************************************************************************
           * PRIVATE METHODS
           *****************************************************************************/
          /**
           * On scroll start
           */
          function scrollStart() {
            // Hide context menu if showing
            component.hideContextMenu();
            // Bind clickout scroll
            //component.layers["clickout"].on("scroll", onChangeLayout);
          }
          /**
           * On scroll end
           */
          function scrollEnd() {
            // Reposition save button if showing
            component.repositionSaveButton();
            // Unbind clickout scroll
            //component.layers["clickout"].off("scroll", onChangeLayout);
          }
          /**
           * Link to scope function (onSelectRow)
           */
          function onSelectRow() {
            // Update selected rows
            if (component.initialized) {
              updateSelectedRows();
            }
          }
          /**
           * Link to scope function (onChangeColumns)
           */
          function onChangeColumns() {
            // Update selected rows
            if (component.initialized) {
              component.reportChangedColumns();
              // Reposition save buttons
              component.repositionSaveButton();
            }
          }
          /**
           * Refresh model
           */
          function refreshModel() {
            // If loadAll sort the model values
            if (component.controller.loadAll) {
              if (component.currentPage && component.currentPage !== component.model.page) {
                component.model.page = component.currentPage;
              }
            }
            component.updateModel();
          }
          /**
           * Manage on sort event
           * @param {Grid} gridScope
           * @param {Array} sortColumns
           */
          function onSort(gridScope, sortColumns) {
            // Fix sort parameters
            grid.sorting = [];
            _.each(sortColumns, function (column) {
              grid.sorting.push({id: column.colDef.sortField, direction: column.sort.direction});
            });
            // Unselect rows (if any)
            grid.component.unselectRows();

            // Update the page
            pageUpdated();
          }
          /**
           * Manage on pagination event
           * @param {Integer} currentPage
           * @param {Integer} pageSize
           */
          function onPagination(currentPage, pageSize) {
            // Fix sort parameters
            component.model.page = currentPage;
            component.controller.max = pageSize;
            // Update the page
            pageUpdated();
          }

          /**
           * Manage on filter event
           */
          function isFiltering() {
            var filtering = false;
            var columns = component.getColumns();
            _.each(columns, function (column) {
              if (!Utilities.isEmpty(column.filters[0].term)) {
                filtering = true;
              }
            });
            return filtering;
          }

          /**
           * Manage on filter event
           */
          function launchFilter() {
            component.filterChanged = true;
            component.visibleRows = grid.api.core.getVisibleRows().length;
            if (isFiltering()) {
              if (!component.wasFiltering) {
                component.defaultPageSize = component.scope.gridOptions.paginationPageSize;
                component.previousPage = component.model.page;
                component.wasFiltering = true;
                component.scope.gridOptions.paginationPageSize = component.model.values.length;
                component.model.page = 1;
              }
            } else {
              component.wasFiltering = false;
              component.scope.gridOptions.paginationPageSize = component.defaultPageSize;
              component.model.page = component.previousPage;
            }
          }


          /**
           * Manage on filter event
           */
          function onFilter() {
            if (component.filterChanged && component.visibleRows !== grid.api.core.getVisibleRows().length) {
              var visibleRows = grid.api.core.getVisibleRows();
              if (isFiltering()) {
                changeRecords(visibleRows.length);
              } else {
                changeRecords(component.model.values.length);
              }
              // Update row number column size
              updateRowNumberColumn();
              if (visibleRows.length > 0) {
                var columns = component.getColumns();
                grid.api.core.scrollToIfNecessary(visibleRows[0], columns[0]);
              }
              component.filterChanged = false;
            }
          }

          /**
           * Initialize grid measures
           */
          function initMeasures() {
            grid.measures = {
              width: undefined,
              height: undefined
            };
          }

          /**
           * Calculate grid sizes
           * @returns {object} Grid width and height in px
           */
          function getGridSize() {
            return {
              width: grid.element[0].clientWidth,
              height: grid.element[0].clientHeight
            };
          }

          /**
           * Resize grid
           */
          function resize() {
            // Check whether is visible or not
            if (component.isGridVisible()) {
              if (component.gridSizeChanged()) {
                // Update pagination text
                updatePaginationText();

                // Check whether to show the scrollbar
                component.updateGridScrollBars();
              }
              // Reposition save button if showing
              component.repositionSaveButton();
            }
          }

          /**
           * Retrieve the first record number in the current page
           * @returns {Number}
           */
          function getFirstRecord() {
            return ((component.model.page - 1) * component.getMax()) + 1;
          }
          /**
           * Retrieve the last record number in the current page
           * @returns {Number}
           */
          function getLastRecord() {
            return Math.min(component.model.page * component.getMax(), component.model.records);
          }

          /**
           * Retrieve pagination text
           * @returns {undefined}
           */
          function updatePaginationText() {
            var paginationText, paginationTextSmall, footerButtonStyle, footerPaginationStyle, bigGrid;

            // Calculate grid width
            var gridWidth = grid.measures.width;
            if (gridWidth === undefined) {
              // Recalculate grid width
              return component.resize();
            }

            // Update total pages
            if (component.controller.loadAll && grid.api) {
              component.model.total = grid.api.pagination.getTotalPages();
            } else {
              var recordsPerPage = component.getMax();
              component.model.total = recordsPerPage === 0 ? 1 : Math.max(Math.ceil(component.model.records / recordsPerPage), 1);
            }
            // Update current page
            component.model.page = component.currentPage;
            // Retrieve filtered numbers
            var page = $filter('formatNumber')(component.model.page, '0,0');
            var total = $filter('formatNumber')(component.model.total, '0,0');
            var records = $filter('formatNumber')(component.model.records, '0,0');
            var first = $filter('formatNumber')(getFirstRecord(), '0,0');
            var last = $filter('formatNumber')(getLastRecord(), '0,0');
            // Generate pagination text
            paginationText = "";
            paginationText += $translate.instant('SCREEN_TEXT_GRID_VIEW');
            paginationText += " " + first;
            paginationText += " - " + last;
            paginationText += " " + $translate.instant('SCREEN_TEXT_GRID_OF');
            paginationText += " " + records;
            paginationTextSmall = page + "/" + total;
            // Generate pagination sizes
            if (component.controller.disablePagination) {
              if (component.controller.buttonModel.length > 0) {
                component.footerButtonStyle = "col-xs-12";
              } else {
                component.footerButtonStyle = "hidden";
              }
            } else {
              bigGrid = gridWidth > component.constants.SMALL_GRID;
              if (bigGrid) {
                footerButtonStyle = "col-xs-3";
                footerPaginationStyle = "col-xs-9";
              } else {
                if (component.controller.buttonModel.length > 0) {
                  footerButtonStyle = "col-xs-4";
                  footerPaginationStyle = "col-xs-8";
                } else {
                  footerButtonStyle = "hidden";
                  footerPaginationStyle = "col-xs-12";
                }
              }
            }

            // Set scope variables
            component.paginationText = paginationText;
            component.paginationTextSmall = paginationTextSmall;
            component.footerButtonStyle = footerButtonStyle;
            component.footerPaginationStyle = footerPaginationStyle;
            component.bigGrid = bigGrid;
          }
          /**
           * Launch a promise on rows rendered
           * @param {type} onDefer
           */
          function deferRowsRendered(onDefer) {
            var deferred = Utilities.q.defer();
            var startTime = new Date();
            var listener = component.scope.$on("rows-rendered", function (event, parameters) {
              if (parameters.grid === grid.id) {
                // Remove listener
                listener();
                // Resolve promise
                if (onDefer) {
                  onDefer(deferred);
                } else {
                  deferred.resolve();
                }

                var dateDiff = (new Date() - startTime) / 1000;
                $log.debug("[GRID ROWS] Grid rows have been COMPILED", {grid: grid.id, compilationTime: dateDiff + "s"});
              }
            });
            // Retrieve new id
            return deferred.promise;
          }
          /**
           * Update row number column size
           */
          function updateRowNumberColumn() {
            // Update row number column size
            if (component.controller.rowNumbers) {
              // Get string size
              var rowNumberColumnSize = (String(getLastRecord()).length + 1) * component.scope.charSize;
              // Change column width
              var column = grid.api.grid.getColumn("rowNum");
              var prevRowNumberWidth = column.width;
              if (prevRowNumberWidth !== rowNumberColumnSize) {
                column.width = Math.max(column.minWidth, rowNumberColumnSize);
                finishPendingActions();
              }
            }
          }
          /**
           * Finish the pending actions
           */
          function finishPendingActions() {
            if (grid.api) {
              grid.api.grid.refresh();
            }
          }
          /**
           * Change record number
           * @param {number} records New record number
           */
          function changeRecords(records) {
            component.model.records = records;
            if (records > 0 && component.model.page === 0) {
              component.model.page = 1;
            } else if (records === 0) {
              component.model.page = 0;
            }
            updatePaginationText();
          }

          /**
           * Change the page to the stored one
           */
          function pageUpdated() {
            if (component.controller.loadAll) {
              // Retrieve new id
              component.updateModel();
              if (component.api && component.api.endLoad) {
                var listener = component.scope.$on("rows-rendered", function (event, parameters) {
                  if (parameters.grid === grid.id) {
                    // Remove listener
                    listener();
                    component.api.endLoad();
                  }
                });
              }
            } else {
              component.reload();
            }
          }
          /**
           * Updates selected rows in grid
           */
          function updateSelectedRows() {
            var selectedList = [];
            if (grid.api) {
              var selectedRows = component.getSelection();
              _.each(selectedRows, function (row) {
                selectedList.push(row[component.constants.ROW_IDENTIFIER]);
              });
            }
            component.onSelectRows(selectedList);
          }

          /**********************************************************************/
          /* API                                                                */
          /**********************************************************************/
          /**
           * Update model values
           * @param {type} data
           */
          component.api.updateModelValues = function (data) {
            if (component.model) {
              _.assign(component.model, data);
              refreshModel();
            }
          };

          // Initialize common functions
          if (!this.commons.init()) {
            return false;
          }

          /**********************************************************************/
          /* EVENTS                                                             */
          /**********************************************************************/

          // Map grid actions
          GridEvents.mapBaseActions(component);

          // Initialized ok
          return true;
        }
      };
      return GridBase;
    }]);
