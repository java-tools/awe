import { aweApplication } from "./../../awe";

// Grid components service
aweApplication.factory('GridComponents',
  ['Control', 'AweUtilities',
    /**
     * Grid generic methods
     * @param {service} Control Control service
     * @param {service} Utilities Utilities service
     */
    function (Control, Utilities) {

      var GridComponents = function (component) {
        this.component = component;

        // Initialize cells
        component.controller.cells = {};
        component.model.cells = {};
        component.api.cells = {};
      };

      /*********
       * METHODS
       *********/
      GridComponents.prototype = {
        /**
         * Initialize grid
         */
        init: function () {
          var component = this.component;

          // Define footer data if showTotals is active
          if (component.controller.showTotals) {
            component.model.footer = {};
          }

          /**
           * Update the model
           */
          component.updateModel = function () {
            if (component.initialized) {
              // Empty cells and footer
              component.controller.cells = {};
              component.model.cells = {};
              component.api.cells = {};
              component.model.footer = {};
            }
            // Update model
            component.updateModelAndSelectRows();
          };

          /**
           * Update cell model
           * @param {object} cell Cell component
           */
          component.updateCellModel = function (cell) {
            // Get cell address
            var address = cell.address;

            // Get grid row index
            var rowIndex = Utilities.getRowIndex(component.model.values, address.row, component.constants.ROW_IDENTIFIER);

            // If rowIndex is -1, the row is the footer row
            if (rowIndex !== -1) {
              component.model.values[rowIndex][address.column] = cell.model.values && cell.model.values.length !== 0 ? cell.model.values[0] : cell.model.selected;

              // Publish model changed
              Control.publishModelChanged(component.address, {values: component.model.values});
            }
          };
          /**
           * Check if cell is initialized
           * @param {object} address Component address
           */
          component.checkInitialized = function (address) {
            var cellId = Utilities.getCellId(address);
            return cellId in component.model.cells && component.model.cells[cellId].initialized;
          };
          /**
           * Retrieve component model
           * @param {string} address Cell address
           */
          component.getModel = function (address) {
            var cellId = Utilities.getCellId(address);
            var cellModel = component.model.cells;

            if (!(cellId in cellModel)) {
              // Calculate rowIndex and selected value
              var rowIndex = Utilities.getRowIndex(component.model.values, address.row, component.constants.ROW_IDENTIFIER);

              // Retrieve value list if exists
              var valueList = component.getColumnValueList(address);

              // If rowIndex is -1, the row is the footer row
              var cellValue;
              if (rowIndex === -1) {
                cellValue = component.model.footer[address.column];
              } else {
                cellValue = component.model.values[rowIndex][address.column];
              }

              // Define columnId controller if not defined
              var cell = component.getCellObject(cellValue);

              var model = {values: valueList || []};
              if ("selected" in cell && "values" in cell) {
                model = cell;
              } else {
                if ("value" in cell) {
                  model.selected = cell.value;
                }
                if (model.values.length === 0) {
                  model.values = angular.isArray(cell) ? cell : [cell];
                }
              }
              cellModel[cellId] = model;
              cellModel[cellId].initialized = true;
              Control.setAddressModel(address, cellModel[cellId]);
            }

            // Define columnId controller if not defined
            return cellModel[cellId];
          };
          /**
           * Retrieve component controller
           * @param {string} address Cell address
           */
          component.getController = function (address) {
            var cellId = Utilities.getCellId(address);
            var cellController = component.controller.cells;

            // Store as string if not done before
            if (!(address.column in component.columnModelStringified)) {
              var column = component.getColumn(address.column);
              component.columnModelStringified[address.column] = Utilities.stringifyJSON(column);
            }

            // Store specific row controller
            if (!(cellId in cellController)) {
              var columnModelString = component.columnModelStringified[address.column];
              cellController[cellId] = Utilities.parseJSON(columnModelString);
              Control.setAddressController(address, cellController[cellId]);
            }

            return cellController[cellId];
          };
          /**
           * Retrieve component api
           * @param {string} address Cell address
           */
          component.getApi = function (address) {
            var cellId = Utilities.getCellId(address);
            var cellApi = component.api.cells;
            if (!(cellId in cellApi)) {
              cellApi[cellId] = {};
              Control.setAddressApi(address, cellApi[cellId]);
            }
            return cellApi[cellId];
          };

          /**
           * Delete row for components with
           */
          component.deleteRow = function (rowId) {
            // Restore row values
            let selectedRow = rowId || component.getSelectedRow();

            // Remove all cells
            component.deleteRowCells(selectedRow);

            // Delete physically the row
            component.deleteRowSpecific(selectedRow);
          };

          /**
           * Delete row cells
           */
          component.deleteRowCells = function (rowId) {
            _.each(component.getColumns(), (column)  => {
              let address = {component: component.id, row: rowId, column: column.name};
              let cellId = Utilities.getCellId(address);
              delete component.model.cells[cellId];
              delete component.controller.cells[cellId];
              delete component.api.cells[cellId];
            });
          };

          return true;
        }
      };

      return GridComponents;
    }]);