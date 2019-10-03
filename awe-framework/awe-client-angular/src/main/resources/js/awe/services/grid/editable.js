import { aweApplication } from "./../../awe";

// Editable grid service
aweApplication.factory('GridEditable',
  ['Control', 'GridComponents', 'GridEvents', 'AweUtilities',
    /**
     * Grid generic methods
     * @param {service} Control Control service
     * @param {service} GridComponents Grid components
     * @param {service} GridEvents Grid events
     * @param {service} Utilities Awe utilities
     */
    function (Control, GridComponents, GridEvents, Utilities) {
      var GridEditable = function (component) {
        this.component = component;
        component.editable = true;
        component.savingRow = false;
        component.rowAction = "none";
        component.gridComponents = new GridComponents(component);

        this.selectedRowValues = {grid: null, cells: null};
      };

      /*********
       * METHODS
       *********/
      GridEditable.prototype = {
        /**
         * Initialize grid
         */
        init: function () {
          var component = this.component;
          var grid = this;

          // Initialize with components
          if (!component.gridComponents.init()) {
            return false;
          }

          /******************************************************************************
           * SCOPE METHODS
           *****************************************************************************/

          /**
           * On save event
           */
          component.scope.onSaveRow = function () {
            // Launch events
            GridEvents.onBeforeSaveRow(component);

            // Remove expanding attribute
            component.expanding = null;
          };

          /**
           * On cancel event
           */
          component.scope.onCancelRow = function () {
            // Launch events
            GridEvents.onBeforeCancelRow(component);

            // Remove expanding attribute
            component.expanding = null;
          };

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * Unselect all rows
           */
          component.unselectRows = function () {
            // Unselect the rows
            component.resetSelection();

            // Change icon and disable buttons
            component.restoreIcons();
          };

          /**
           * Restore icons
           */
          component.restoreIcons = function () {
            component.savingRow = false;
            component.rowAction = "none";
          };

          /**
           * Set row button as working
           * @param {String} action
           */
          component.setRowButtonWorking = function (action) {
            // Change icon and disable buttons
            component.savingRow = true;
            component.rowAction = action;
          };

          /**
           * On select rows event
           * @param {Array} selectedRows Selected rows
           */
          component.onSelectRows = function (selectedRows) {
            if (!_.isEqual(selectedRows, component.currentSelection)) {
              // Select base rows
              component.selectRows(selectedRows);

              // Store row model
              if (selectedRows.length === 1) {
                storeRowModel(selectedRows[0]);
              }

              // Reposition save button
              updateSaveButton();
            }
          };

          /**
           * Save the current row
           */
          component.saveRow = function () {
            // Change icon and disable buttons
            component.setRowButtonWorking("save");

            // Hide context menu if showing
            component.hideContextMenu();

            var selectedRow = component.getSelectedRow();
            if (selectedRow !== null) {
              // Save row values
              component.saveRowValues(selectedRow);
            }
          };

          /**
           * Cancel the current row
           */
          component.cancelRow = function () {
            // Change icon and disable buttons
            component.setRowButtonWorking("cancel");

            // Hide context menu if showing
            component.hideContextMenu();

            // Restore row values
            var selectedRow = component.getSelectedRow();
            if (selectedRow !== null) {
              restoreRowModel(selectedRow);
            }
          };

          /**
           * Delete the current row
           */
          component.deleteRow = function () {
            // Hide context menu if showing
            component.hideContextMenu();

            // Restore row values
            var selectedRow = component.getSelectedRow();
            if (selectedRow !== null) {
              component.deleteRowSpecific(selectedRow).then(updateSaveButton);
            }
          };

          /**
           * Add a new row
           * @param {integer} position
           * @param {object} data
           * @param {String} selectedRow selected row
           * @return {String} new row Id
           */
          component.addRow = function (position, data, selectedRow) {
            // Hide context menu if showing
            component.hideContextMenu();

            // Generate row data
            var rowData = data || {};
            // Store old values
            _.each(component.controller.columnModel, function (column) {
              if ("id" in column) {
                rowData[column.id] = column.checked ? column.value || 1 : rowData[column.id] || column.value || null;
              }
            });

            // Add the new row
            component.addRowSpecific(selectedRow, position, rowData).then(function (rowId) {
              component.editRow(rowId);
            });
          };

          /**
           * Edit a row
           * @param {String} rowId row Id
           */
          component.editRow = function (rowId) {
            // Restore icons
            component.restoreIcons();

            // Restore row values
            component.editRowSpecific(rowId);
          };

          /**
           * Store component model
           * @param {string} rowId Row identifier
           * @param {object} values Row values
           */
          component.setRowValues = function (rowId, values) {
            // Calculate rowIndex
            var rowIndex = Control.getRowIndex(component.model.values, rowId, component.constants.ROW_IDENTIFIER);

            // Retrieve selected row values
            component.model.values[rowIndex] = {...values};

            // Publish model changed
            Control.publishModelChanged(component.address, {values: component.model.values});
          };
          /**
           * Retrieve component model
           * @param {string} rowId Row identifier
           */
          component.saveRowValues = function (rowId) {
            // Get grid model
            var rowIndex = Control.getRowIndex(component.model.values, rowId, component.constants.ROW_IDENTIFIER);
            var rowValues = component.getRowValues(rowId);

            // Store old values
            var rowModel = component.model.cells;
            var address = {...component.address, row: rowId};
            _.each(component.controller.columnModel, function (column) {
              if ("id" in column && column.id in rowValues) {
                address.column = column.id;
                var cellId = Utilities.getCellId(address);
                if (cellId in rowModel) {
                  rowValues[column.id] = rowModel[cellId].selected;
                }
              }
            });

            // Store row values
            component.model.values[rowIndex] = rowValues;
          };
          /**
           * Check if there has been changes
           * @param {string} address Cell address
           * @param {object} value Data changed
           * @param {object} rowIndex Row data
           */
          component.checkChanges = function (address, value, rowIndex) {
            var previousValue;
            var selectedRowValues = grid.selectedRowValues || {};
            if (address.row === selectedRowValues.row) {
              previousValue = selectedRowValues.grid[address.column];
            } else {
              previousValue = component.model.values[rowIndex][address.column];
            }
            return !angular.equals(value || "#empty#", previousValue || "#empty#");
          };
          /******************************************************************************
           * PRIVATE METHODS
           *****************************************************************************/

          /**
           * Store row model
           * @param {string} rowId Row identifier
           */
          function storeRowModel(rowId) {
            var cells = {};
            var address = _.merge({row: rowId}, component.address);
            _.each(component.controller.columnModel, function (column) {
              if ("id" in column) {
                address.column = column.id;
                var cellId = Utilities.getCellId(address);
                var cellModel = component.model.cells[cellId];
                if (cellModel && "selected" in cellModel && "values" in cellModel) {
                  cells[column.id] = _.cloneDeep(component.model.cells[cellId]);
                }
              }
            });
            grid.selectedRowValues = {
              grid: _.cloneDeep(component.getRowValues(rowId)),
              cells: cells
            };
          }

          /**
           * Retrieve component model
           * @param {string} rowId Row identifier
           */
          function restoreRowModel(rowId) {
            // Restore grid values
            component.setRowValues(rowId, grid.selectedRowValues.grid);

            // Restore cell component values
            var rowModel = component.model.cells;
            var address = _.merge({row: rowId}, component.address);
            var storedRow = grid.selectedRowValues.cells;
            if (storedRow) {
              _.each(component.controller.columnModel, function (column) {
                if (column.id in storedRow) {
                  address.column = column.id;
                  var cellId = Utilities.getCellId(address);
                  rowModel[cellId].selected = _.cloneDeep(storedRow[column.id].selected);
                  rowModel[cellId].values = _.cloneDeep(storedRow[column.id].values);

                  // Publish model changed
                  Control.publishModelChanged(address, {selected: rowModel[cellId].selected,
                    values: rowModel[cellId].values});
                }
              });
            }
          }

          /**
           * Reposition save button
           */
          function updateSaveButton() {
            // Reposition save button
            component.repositionSaveButton();
          }

          /**********************************************************************/
          /* EVENTS                                                             */
          /**********************************************************************/

          // Map grid actions
          GridEvents.mapEditableActions(component);

          return true;
        }
      };
      return GridEditable;
    }
  ]);