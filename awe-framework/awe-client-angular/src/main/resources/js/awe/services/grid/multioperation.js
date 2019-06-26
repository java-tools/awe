import { aweApplication } from "./../../awe";

// Multioperation grid service
aweApplication.factory('GridMultioperation',
  ['Control', 'AweUtilities', 'GridEditable',
    /**
     * Multioption Grid generic methods
     * @param {service} Control Control service
     * @param {service} Utilities Awe utilities service
     * @param {service} GridEditable Editable grid service
     */
    function (Control, Utilities, GridEditable) {

      // Define 'constants'
      var constants = {};
      constants["ROW_TYPE_NAME"] = "RowTyp";
      constants["ROW_TYPE_ICON"] = "RowIco";
      constants["ROW_ACTIONS"] = {
        INSERT: {
          value: "INSERT",
          label: "Added",
          icon: "fa-plus"
        },
        UPDATE: {
          value: "UPDATE",
          label: "Updated",
          icon: "fa-edit"
        },
        DELETE: {
          value: "DELETE",
          label: "Deleted",
          icon: "fa-trash"
        },
        NONE: {
          value: undefined,
          icon: ""
        }
      };

      var GridMultioperation = function (component) {
        this.component = component;
        component.gridEditable = new GridEditable(component);
      };

      /*********
       * METHODS
       *********/
      GridMultioperation.prototype = {
        /**
         * Initialize grid
         */
        init: function () {
          // Define constants
          var component = this.component;

          // Initialize as editable
          if (!component.gridEditable.init()) {
            return false;
          }

          // Add new constants
          _.merge(component.constants, constants);

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * Add extra column model data
           */
          component.addExtraColumnModelData = function () {
            // Add new columns to the grid
            component.addMultioperationColumns();
          };

          /**
           * Add a new row
           * @param {integer} position
           * @param {Object} data
           * @param {String} selectedRow selected row
           * @return {String} new row Id
           */
          component.addRow = function (position, data, selectedRow) {
            // Hide context menu if showing
            component.hideContextMenu();

            // Define insert action
            var action = component.constants.ROW_ACTIONS.INSERT;

            // Generate row data
            var rowData = data || {};

            // Store old values
            _.each(component.controller.columnModel, function (column) {
              rowData[column.id] = column.checked ? column.value || 1 : rowData[column.id] || column.value || null;
            });

            // Update RowTyp column value
            rowData[component.constants.ROW_CLASS_FIELD] = action.value;
            rowData[component.constants.ROW_TYPE_NAME] = action.value;
            rowData[component.constants.ROW_TYPE_ICON] = {
              selected: action.value,
              values: [action]
            };

            // Restore row values
            component.addRowSpecific(selectedRow, position, rowData).then(function (rowId) {
              // Edit the new row
              component.editRowSpecific(rowId);
            });
          };

          /**
           * Load a row from the parent
           * @param {object} row
           * @param {object} parent
           * @return {String} rowId
           */
          component.addChildRow = function (row, parent) {
            // Hide context menu if showing
            component.hideContextMenu();

            // Define insert action
            var action = component.constants.ROW_ACTIONS.NONE;

            // Update RowTyp and RowIco column value
            updateModelOperation(action, row);

            // Restore row values
            component.addChildRowSpecific(row, parent);
          };

          /**
           * Delete the current row
           */
          component.deleteRow = function () {
            // Hide context menu if showing
            component.hideContextMenu();

            // Get selected row
            let selectedRow = component.getSelectedRows();

            // If selectedRow is not null, remove row
            _.each(selectedRow, function (row) {
              // Calculate rowIndex
              let rowIndex = Control.getRowIndex(component.model.values, row, component.constants.ROW_IDENTIFIER);

              // If row has been added previously, delete row
              if (component.model.values[rowIndex][component.constants.ROW_TYPE_NAME] === component.constants.ROW_ACTIONS.INSERT.value) {
                component.deleteRowSpecific(row);
                // Else mark row as deleted
              } else {
                changeRowOperation(row, component.constants.ROW_ACTIONS.DELETE);
              }
            });
          };

          /**
           * Save the row
           */
          component.saveRow = function () {
            var selected = component.model.selected;
            if (selected !== null && selected.length > 0) {
              // Change icon and disable buttons
              component.setRowButtonWorking("save");

              // Retrieve first selected row
              var selectedRow = component.getSelectedRow();

              // Hide context menu if showing
              component.hideContextMenu();

              // Save row values
              component.saveRowValues(selectedRow);
            }
          };

          /**
           * Save the row
           */
          component.updateRowStatus = function (rowIndex, rowId) {
            // If row has been added previously, delete row
            if (component.model.values[rowIndex][component.constants.ROW_TYPE_NAME] === component.constants.ROW_ACTIONS.NONE.value) {
              changeRowOperation(rowId, component.constants.ROW_ACTIONS.UPDATE);
            }
          };

          /**
           * Retrieve column data
           * @param {Service} column Column identifier
           * @param {Boolean} getAll Get all values
           * @returns {Object} Data from column
           */
          component.getColumnData = function (column, getAll) {
            // Initialize data
            var data = {};
            var columnData = [];
            var selectedRowData = [];
            var selected = Utilities.asArray(component.model.selected);

            _.each(component.model.values, function (row) {
              var rowId = row[component.constants.ROW_IDENTIFIER];
              var cellValue = component.getCellData(row[column], component.constants.CELL_VALUE);
              if (getAll || row[component.constants.ROW_TYPE_NAME] !== component.constants.ROW_ACTIONS.NONE.value) {
                columnData.push(cellValue);
              }

              // Get selected rows if there is only one row selected
              if (selected.indexOf(rowId) > -1) {
                selectedRowData.push(cellValue);
              }
            });
            // Format data list
            data[column] = columnData;

            // Store selected data
            component.getSelectedCellData(data, column, selectedRowData);

            return data;
          };

          /**
           * Retrieve identifier column data
           * @returns {Object} Data from column
           */
          component.getIdentifierColumnData = function () {
            // Initialize data
            var data = {};
            var columnData = [];

            _.each(component.model.values, function (row) {
              var rowId = row[component.constants.ROW_IDENTIFIER];
              if (row[component.constants.ROW_TYPE_NAME] !== component.constants.ROW_ACTIONS.NONE.value) {
                columnData.push(rowId);
              }
            });

            // Store as identifier list
            data[component.address.component + "-id"] = columnData;

            return data;
          };

          /**
           * Get data function, to retrieve grid data
           * @returns {Object} Data from grid
           */
          component.getExtraData = function () {
            // Initialize data
            var data = {};
            var operations = 0;

            // Add operation column
            var operationData = [];
            _.each(component.model.values, function (row) {
              if (row[component.constants.ROW_TYPE_NAME] !== component.constants.ROW_ACTIONS.NONE.value) {
                operationData.push(component.getCellData(row[component.constants.ROW_TYPE_NAME], component.constants.CELL_VALUE));
                operations++;
              }
            });

            data[component.address.component + "-" + component.constants.ROW_TYPE_NAME] = operationData;
            data[component.address.component] = operations;
            return data;
          };

          /**
           * Add the multioperation columns
           */
          component.addMultioperationColumns = function () {
            // Add operation icon column
            var colModel = {
              id: component.constants.ROW_TYPE_ICON,
              name: component.constants.ROW_TYPE_ICON,
              width: 24,
              editable: false,
              sortable: false,
              sendable: false,
              align: "center",
              label: "",
              model: component.constants.ROW_ACTIONS.NONE,
              component: "icon",
              style: "multioperation-icon",
              enableColumnResizing: false,
              enableColumnMoving: false,
              enableColumnMenu: false,
              enableFiltering: false,
              enableSorting: false
            };

            component.addToColumnModel(colModel);
          };

          /**
           * Update cell model
           * @param {object} cell Cell component
           */
          component.updateCellModel = function (cell) {
            // Get cell address
            var address = cell.address;

            // Get grid row index
            var rowIndex = Control.getRowIndex(component.model.values, address.row, component.constants.ROW_IDENTIFIER);

            // If rowIndex is -1, the row is the footer row
            if (rowIndex !== -1 && component.checkChanges(address, cell.model.selected, rowIndex)) {
              // Set row as updated
              component.updateRowStatus(rowIndex, address.row);

              // Set value to values
              component.model.values[rowIndex][address.column] = cell.model.selected;

              // Publish model changed
              Control.publishModelChanged(component.address, {values: component.model.values});
            }
          };

          /******************************************************************************
           * PRIVATE METHODS
           *****************************************************************************/

          /**
           * Change row operation
           * @param {type} rowId Row identifier
           * @param {type} action New action
           */
          function changeRowOperation(rowId, action) {
            // Calculate rowIndex
            var rowIndex = Control.getRowIndex(component.model.values, rowId, component.constants.ROW_IDENTIFIER);

            // Update RowTyp and RowIco column value
            updateModelOperation(action, component.model.values[rowIndex]);
            component.addRowStyle(rowId, action.value);

            // Update RowIco column value
            var address = _.cloneDeep(component.address);
            address.column = component.constants.ROW_TYPE_ICON;
            address.row = rowId;
            Control.changeModelAttribute(address, {values: [action], selected: action});
          }

          /**
           * Update the model of an operation
           * @param {type} action Operation
           * @param {type} row Row
           */
          function updateModelOperation(action, row) {
            row[component.constants.ROW_TYPE_NAME] = action.value;
            row[component.constants.ROW_TYPE_ICON] = action;
          }

          return true;
        }
      };

      return GridMultioperation;
    }
  ]);