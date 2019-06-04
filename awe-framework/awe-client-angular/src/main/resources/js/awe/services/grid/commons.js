import { aweApplication } from "./../../awe";
import "angular-ui-grid";
import "./components";
import "./editable";
import "./multioperation";
import "./events";

// Add requirements
aweApplication.requires.push.apply(aweApplication.requires, ["ui.grid", "ui.grid.resizeColumns", "ui.grid.pinning", "ui.grid.selection", "ui.grid.pagination", "ui.grid.moveColumns", "ui.grid.treeView"]);

// Grid commons service
aweApplication.factory('GridCommons', ['GridComponents', 'GridEditable', 'GridMultioperation', 'GridEvents', '$translate', 'AweSettings', 'Control', 'AweUtilities',
  /**
   * Grid common methods
   *
   * @param {service} GridComponents Grid components
   * @param {service} GridEditable Grid editable
   * @param {service} GridMultioperation Grid multioperation
   * @param {service} GridEvents Grid events
   * @param {service} $translate Translate service
   * @param {service} $settings AWE $settings
   * @param {service} Control Control service
   * @param {service} Utilities AWE Utilities
   */
  function (GridComponents, GridEditable, GridMultioperation, GridEvents, $translate, $settings, Control, Utilities) {
    // Retrieve $settings

    /**
     * Retrieve number of selected lines in grid
     *
     * @param {type} component Grid model
     * @returns {integer} Number of selected lines
     */
    const getSelectedLines = function (component) {
      var value;
      var model = component.model;
      if (model.selected === null) {
        value = 0;
      } else {
        value = model.selected.length;
      }
      return value;
    };

    /**
     * Retrieve the row value
     *
     * @param {type} model
     * @param {type} row
     * @param {type} column
     * @returns {undefined}
     */
    const getRowValue = function (model, row, column) {
      var value = null;
      if (row === "footer" || (row > -1 && row < model.length)) {
        var cell = model[row][column];
        if (angular.isObject(cell) && "value" in cell) {
          value = cell.value;
        } else {
          value = cell;
        }
      }
      return value;
    };

    /**
     * Retrieve header by column name
     *
     * @param {type} headers
     * @param {type} columnName
     * @returns {undefined}
     */
    const getHeader = function (headers, columnName) {
      var selectedHeader = false;
      _.each(headers, function (header) {
        if (header.startColumnName === columnName) {
          selectedHeader = header;
        }
      });
      return selectedHeader;
    };

    /**
     * Sanitize selected values (retrieve values from row id)
     */
    const sanitizeSelection = function(selection, data, component) {
      let sanitized = [];
      _.each(selection, function (row) {
        var rowIndex = Control.getRowIndex(data, row, component.constants.ROW_IDENTIFIER);
        if (rowIndex > -1) {
          sanitized.push(data[rowIndex][component.constants.ROW_IDENTIFIER]);
        }
      });
      return sanitized;
    }

    /**
     * Grid constructor
     *
     * @param {Scope} component Component
     */
    const GridCommons = function (component) {
      this.component = component;

      // Generate grid cell controller / model
      component.constants.SELECTED_TAIL = ".selected";
      component.hasFrozen = false;
      component.addedRows = 0;
      component.isEditing = false;
      component.columnModelStringified = {};

      // Define attribute methods
      this.attributeMethods = {
        /**
         * Retrieve current row value
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @param {string} row Row
         * @returns {mixed} Current row value
         */
        currentRowValue: function (component, column, row) {
          var index = Control.getRowIndex(component.model.values, row, component.constants.ROW_IDENTIFIER);
          return getRowValue(component.model.values, index, column);
        },
        /**
         * Retrieve the value for the column of the previous row to the selected
         * one
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @param {string} row Row
         * @returns {mixed} Selected row value
         */
        prevCurrentRowValue: function (component, column, row) {
          var model = component.model.values, index, value = null;
          var prevRow = this.prevCurrentRow(component, column, row);
          if (prevRow !== null) {
            index = Control.getRowIndex(model, prevRow, component.constants.ROW_IDENTIFIER);
            value = getRowValue(model, index, column);
          }
          return value;
        },
        /**
         * Retrieve the value for the column of the next row to the selected one
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @param {string} row Row
         * @returns {mixed} Selected row value
         */
        nextCurrentRowValue: function (component, column, row) {
          var model = component.model.values, index, value = null;
          var nextRow = this.nextCurrentRow(component, column, row);
          if (nextRow !== null) {
            index = Control.getRowIndex(model, nextRow, component.constants.ROW_IDENTIFIER);
            value = getRowValue(model, index, column);
          }
          return value;
        },
        /**
         * Retrieve the value for the column of the selected row
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @returns {mixed} Selected row value
         */
        selectedRowValue: function (component, column) {
          var model = component.model.values;
          var selectedRow = this.selectedRow(component);
          var index = Control.getRowIndex(model, selectedRow, component.constants.ROW_IDENTIFIER);
          return getRowValue(model, index, column);
        },
        /**
         * Retrieve the value for the column of the previous row to the selected
         * one
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @returns {mixed} Selected row value
         */
        prevRowValue: function (component, column) {
          var model = component.model.values, index, value = null;
          var prevRow = this.prevRow(component);
          if (prevRow !== null) {
            index = Control.getRowIndex(model, prevRow, component.constants.ROW_IDENTIFIER);
            value = getRowValue(model, index, column);
          }
          return value;
        },
        /**
         * Retrieve the value for the column of the next row to the selected one
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @returns {mixed} Selected row value
         */
        nextRowValue: function (component, column) {
          var model = component.model.values, index, value = null;
          var nextRow = this.nextRow(component);
          if (nextRow !== null) {
            index = Control.getRowIndex(model, nextRow, component.constants.ROW_IDENTIFIER);
            value = getRowValue(model, index, column);
          }
          return value;
        },
        /**
         * Retrieve the value for the column of the footer
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @returns {mixed} Footer value
         */
        footerValue: function (component, column) {
          return getRowValue(component.model, "footer", column);
        },
        /**
         * Retrieve the current row identifier
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @param {string} row Row
         * @returns {string} Row identifier
         */
        currentRow: function (component, column, row) {
          return row;
        },
        /**
         * Retrieve the previous to the selected row identifier
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @param {string} row Row
         * @returns {string} Row identifier
         */
        prevCurrentRow: function (component, column, row) {
          var model = component.model.values, index, value = null;
          if (row !== null) {
            index = Control.getRowIndex(model, row, component.constants.ROW_IDENTIFIER);
            value = index > 0 ? model[index - 1][component.constants.ROW_IDENTIFIER] : null;
          }
          return value;
        },
        /**
         * Retrieve the next to the selected row identifier
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @param {string} row Row
         * @returns {string} Row identifier
         */
        nextCurrentRow: function (component, column, row) {
          var model = component.model.values, index, value = null;
          if (row !== null) {
            index = Control.getRowIndex(model, row, component.constants.ROW_IDENTIFIER);
            value = index > -1 && index < model.length - 1 ? model[index + 1][component.constants.ROW_IDENTIFIER] : null;
          }
          return value;
        },
        /**
         * Retrieve the selected row identifier
         *
         * @param {object} component Grid component
         * @returns {string} Row identifier
         */
        selectedRow: function (component) {
          return component.getSelectedRow();
        },
        /**
         * Retrieve the previous to the selected row identifier
         *
         * @param {object} component Grid component
         * @returns {string} Row identifier
         */
        prevRow: function (component) {
          var model = component.model.values, index, value = null;
          var selectedRow = this.selectedRow(component);
          if (selectedRow !== null) {
            index = Control.getRowIndex(model, selectedRow, component.constants.ROW_IDENTIFIER);
            value = index > 0 ? model[index - 1][component.constants.ROW_IDENTIFIER] : null;
          }
          return value;
        },
        /**
         * Retrieve the next to the selected row identifier
         *
         * @param {object} component Grid component
         * @returns {string} Row identifier
         */
        nextRow: function (component) {
          var model = component.model.values, index, value = null;
          var selectedRow = this.selectedRow(component);
          if (selectedRow !== null) {
            index = Control.getRowIndex(model, selectedRow, component.constants.ROW_IDENTIFIER);
            value = index > -1 && index < model.length - 1 ? model[index + 1][component.constants.ROW_IDENTIFIER] : null;
          }
          return value;
        },
        /**
         * Retrieve total number of rows
         *
         * @param {object} component Grid component
         * @returns {integer} Number of rows
         */
        totalRows: function (component) {
          return component.model.records;
        },
        /**
         * Retrieve if column is empty or not
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @returns {boolean} Column is empty
         */
        emptyDataColumn: function (component, column) {
          var filledRows = component.getFilledRows(column);
          return filledRows === 0 ? true : null;
        },
        /**
         * Retrieve if column has data or not
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @returns {boolean} Column has data
         */
        hasDataColumn: function (component, column) {
          var totalRows = component.getTotalRows();
          var filledRows = component.getFilledRows(column);
          return totalRows >= filledRows && filledRows > 0 ? true : null;
        },
        /**
         * Retrieve if column is full (all cells with data) or not
         *
         * @param {object} component Grid component
         * @param {string} column Column
         * @returns {boolean} Column is full
         */
        fullDataColumn: function (component, column) {
          var totalRows = component.getTotalRows();
          var filledRows = component.getFilledRows(column);
          return totalRows === filledRows ? true : null;
        },
        /**
         * Retrieve modified lines on multioperation grids
         *
         * @param {type} component
         * @returns {integer}
         */
        modifiedRows: function (component) {
          var modifiedRows = getSelectedLines(component);
          if (component.getExtraData) {
            var data = component.getExtraData();
            modifiedRows = data[component.address.component];
          }
          return modifiedRows;
        },
        // Retrieve selected rows number
        selectedRows: getSelectedLines,
        value: getSelectedLines
      };

      // Add attribute methods
      _.merge(this.component.attributeMethods, this.attributeMethods);
      return this;
    };

    GridCommons.prototype = {
      /**
       * Initialize base grid
       */
      init: function () {
        // Initialize common methods
        var component = this.component;

        // Check frozen attribute
        component.hasFrozen = this.hasFrozenColumns();

        /**
         * Custom scroll management
         * @param uiGridViewport Viewport
         * @param scrollHandler Scroll handler
         */
        component.customScroller = function(uiGridViewport, scrollHandler) {
          uiGridViewport.on('scroll', (event) => {
            component.repositionSaveButton();
            scrollHandler(event);
          });
        };

        /**
         * Initialize grid layers
         *
         * @param {type} elem Grid elem
         */
        component.initLayers = function (elem) {
          component.layers = {};
          // Calculate layers
          component.layers["grid"] = elem;
          component.layers["gridNode"] = elem.find(".ui-grid");
          component.layers["footer"] = elem.find(".ui-grid-footer-cell-row");
          component.layers["content"] = elem.find(".ui-grid-canvas");
          component.layers["header"] = elem.find(".ui-grid-header");
          component.layers["clickout"] = elem.find('.ui-grid-viewport');
          component.layers["contentWrapper"] = elem.find('.ui-grid-contents-wrapper');
          component.layers["container"] = elem.find('.ui-grid-render-container-body');
          setTimeout(function () {
            component.layers["save"] = elem.find('.save-button');
          });
          component.scope.gridOptions.layers = component.layers;
        };
        /**
         * Specific getAttribute function (To be overwritten on complex
         * directives)
         *
         * @param {type} attribute Attribute to check
         * @param {type} column column to check
         * @param {type} row row to check
         * @returns {mixed} Attribute value
         */
        component.getAttribute = function (attribute, column, row) {
          return component.attributeMethods[attribute](component, column, row);
        };
        /**
         * Reposition the save button
         */
        component.repositionSaveButton = function () {
          // Initialize layers if not initialized
          if (!component.layers) {
            component.initLayers();
          }
          if (component.layers && component.layers["save"]) {
            // Change flag to editing
            component.isEditing = false;
            // Define initial button position
            var buttonPosition = {
              top: -10000,
              left: -10000
            };
            if (component.editable && component.currentSelection && component.currentSelection.length === 1) {
              // Calculate button position if editing
              buttonPosition = component.getButtonPosition();
              // Change flag to editing
              component.isEditing = true;
            }

            // Set button position
            component.layers["save"].css(buttonPosition);
          }
        };
        /**
         * Update button position
         *
         * @returns {object} button position
         */
        component.getButtonPosition = function () {
          var buttonPosition = {
            top: -10000
          };
          // Find selected row
          var selectedRows = component.getSelection();
          if (selectedRows.length > 0) {
            var selectedRow = component.layers["content"].find(".ui-grid-row[id='" + selectedRows[0][component.constants.ROW_IDENTIFIER] + "']").last();
            if (selectedRow.length > 0) {
              var headerHeight = component.layers["header"].outerHeight(true);
              var clickout = component.layers["clickout"];
              var buttonWidth = component.layers["save"].outerWidth(true);
              var clickoutMeasures = clickout.first().offset();
              var rowMeasures = selectedRow.offset();
              rowMeasures.height = selectedRow.outerHeight(true);
              rowMeasures.width = selectedRow.outerWidth();
              rowMeasures.left -= selectedRow.outerWidth(true) - rowMeasures.width;
              clickoutMeasures.height = clickout.height();
              clickoutMeasures.width = clickout.last().width();
              clickoutMeasures.scrollLeft = clickout.last().scrollLeft();
              // Calculate new button position
              buttonPosition.left = (rowMeasures.left + clickoutMeasures.scrollLeft + Math.min(rowMeasures.width, clickoutMeasures.width) - buttonWidth) - clickoutMeasures.left;
              buttonPosition.top = (rowMeasures.top + rowMeasures.height + headerHeight) - clickoutMeasures.top;
              // If top position is offbound, move out
              buttonPosition.top = buttonPosition.top < headerHeight || buttonPosition.top > headerHeight + clickoutMeasures.height + 2 ? -10000 : buttonPosition.top;
            }
          }

          // Return button position
          return buttonPosition;
        };
        /**
         * Generate the cell data
         *
         * @param {type} cellValue
         */
        component.getCellObject = function (cellValue) {
          var cellObject = cellValue;
          if (cellValue === null) {
            cellObject = {
              value: null,
              label: ""
            };
          } else if (typeof cellValue !== "object") {
            cellObject = {
              value: cellValue,
              label: cellValue
            };
          } else if (angular.isArray(cellValue)) {
            cellObject = {
              value: cellValue,
              label: cellValue.split(", ")
            };
          } else if ("value" in cellObject && !("label" in cellObject)) {
            cellObject.label = cellObject.value;
          }

          return cellObject;
        };

        /**
         * Retrieve cell data
         *
         * @param {object} value Model value
         * @returns {object} Grid cell
         */
        component.getCell = function (value) {
          var cell;
          if (Utilities.isEmpty(value)) {
            cell = {};
            cell[component.constants.CELL_VALUE] = "";
            cell[component.constants.CELL_LABEL] = "";
            cell[component.constants.CELL_TITLE] = "";
            cell[component.constants.CELL_STYLE] = "";
          } else if (typeof value === "object") {
            cell = value;
          } else {
            cell = {};
            cell[component.constants.CELL_VALUE] = value;
            cell[component.constants.CELL_LABEL] = value;
            cell[component.constants.CELL_TITLE] = value;
            cell[component.constants.CELL_STYLE] = "";
          }
          return cell;
        };

        /**
         * Retrieve cell data
         *
         * @param {object} cell Cell to retrieve
         * @param {object} attribute Cell attribute
         * @returns {mixed} Grid data parsed
         */
        component.getCellData = function (cell, attribute) {
          return component.getCell(cell)[attribute];
        };

        /**
         * Retrieve column data
         *
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
            var isSelected = selected.indexOf(rowId) > -1;
            if (getAll || component.controller.sendAll || isSelected) {
              columnData.push(cellValue);
            }

            // Get selected rows if there is only one row selected
            if (isSelected) {
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
         *
         * @returns {Object} Data from column
         */
        component.getIdentifierColumnData = function () {
          // Initialize data
          var data = {};
          var columnData = [];
          var selected = Utilities.asArray(component.model.selected);

          _.each(component.model.values, function (row) {
            var rowId = row[component.constants.ROW_IDENTIFIER];
            var isSelected = selected.indexOf(rowId) > -1;
            if (component.controller.sendAll || isSelected) {
              columnData.push(rowId);
            }
          });

          // Store as identifier list
          data[component.address.component + "-id"] = columnData;

          return data;
        };

        /**
         * Retrieve selected row data
         *
         * @param {type} data
         * @param {type} column
         * @param {type} cellData
         * @returns {Object} Data from grid
         */
        component.getSelectedCellData = function (data, column, cellData) {
          var cellValue = cellData.length === 0 ? null : cellData.length === 1 ? cellData[0] : cellData;
          data[column + component.constants.SELECTED_TAIL] = cellValue;
        };

        /**
         * Get data function, to retrieve grid data
         *
         * @returns {Object} Data from grid
         */
        component.getData = function () {
          // Initialize data
          var data = {};

          // Selected rows
          data[component.address.component] = component.model.selected;

          // Sort and order fields
          data[component.address.component + $settings.get("dataSuffix")] = component.getSpecificFields();

          // Column data
          _.each(component.controller.columnModel, function (column) {
            if (column.sendable && "id" in column) {
              _.merge(data, component.getColumnData(column.id));
            }
          });

          // Identifier data
          _.merge(data, component.getIdentifierColumnData());

          // Add extra data
          if (component.getExtraData) {
            _.merge(data, component.getExtraData());
          }

          return data;
        };

        /**
         * Get data function, to retrieve grid data
         *
         * @returns {Object} Data from grid
         */
        component.getPrintData = function () {
          // Initialize data
          var data = {};

          // Sort and order fields
          data[component.address.component + $settings.get("dataSuffix")] = component.getSpecificFields();

          // Column data
          _.each(component.controller.columnModel, function (column) {
            if (column.sendable && "id" in column) {
              _.merge(data, component.getColumnPrintData(column.id, column.component));
            }
          });

          // Identifier data
          _.merge(data, component.getIdentifierColumnData());

          // Add extra data
          if (component.getExtraData) {
            _.merge(data, component.getExtraData());
          }

          // Add visible columns information
          data[component.address.component + $settings.get("dataSuffix")].visibleColumns = component.getVisibleColumns();

          return data;
        };

        /**
         * Retrieve column data
         *
         * @param {Service} column Column identifier
         * @returns {Object} Data from column
         */
        component.getColumnPrintData = function (columnId) {
          // Initialize data
          var data = {};
          var columnData = [];
          var address = angular.extend({column: columnId}, component.address);
          var column = component.getColumn(columnId);

          _.each(component.model.values, function (row) {
            var rowId = row[component.constants.ROW_IDENTIFIER];
            address.row = rowId;
            var cellValue = component.getVisibleData(address, row, column);
            columnData.push(cellValue);
          });

          // Format data list
          data[columnId] = columnData;
          return data;
        };

        /**
         * Retrieve column visible data
         *
         * @returns {String} Visible column data
         */
        component.getVisibleData = function (address, row, column) {
          var data = component.getCellData(row[address.column], component.constants.CELL_LABEL);
          switch (column.type) {
            case "float":
            case "integer":
              data = component.getCellData(row[address.column], component.constants.CELL_VALUE);
              break;
            default:
              switch (column.component) {
                case "select":
                case "suggest":
                case "select-multiple":
                case "suggest-multiple":
                case "progress":
                case "numeric":
                  if (component.getApi) {
                    var componentApi = component.getApi(address);
                    if (componentApi && componentApi.getVisibleValue) {
                      data = componentApi.getVisibleValue();
                    }
                  }
                  break;
                case "checkbox":
                  data = component.getCellData(row[address.column], component.constants.CELL_VALUE);
                  data = data == "0" ? false : true;
                  break;
                case "image":
                  data = component.getCellData(row[address.column], component.constants.CELL_IMAGE);
                  break;
                case "image":
                  data = component.getCellData(row[address.column], component.constants.CELL_ICON);
                  break;
                case "password":
                  data = "****";
                  break;
                default:
                  break;
              }
          }

          return data;
        };

        /**
         * Retrieve column header information
         *
         * @returns {array} Visible column list
         */
        component.getVisibleColumns = function () {
          var columns = {};
          _.each(component.controller.columnModel, function (column) {
            if (!column.hidden) {
              columns[column.name] = $translate.instant(column.label);
            }
          });
          return columns;
        };
        /**
         * Retrieve component model
         *
         * @param {string} rowId Row identifier
         */
        component.getRowValues = function (rowId) {
          // Calculate rowIndex
          var rowIndex = Control.getRowIndex(component.model.values, rowId, component.constants.ROW_IDENTIFIER);

          // Retrieve selected row values
          return component.model.values[rowIndex];
        };
        /**
         * Retrieve selected row
         *
         * @return {String} Selected row
         */
        component.getSelectedRow = function () {
          var selected = component.model.selected;
          return angular.isArray(selected) && selected.length > 0 ? selected[0] : null;
        };
        /**
         * Retrieve selected rows
         *
         * @return {String} Selected row
         */
        component.getSelectedRows = function () {
          var selected = component.model.selected;
          return angular.isArray(selected) && selected.length > 0 ? selected : null;
        };
        /**
         * Retrieve selected row data
         */
        component.getSelectedRowData = function () {
          var selectedRowData = {};
          var selectedRow = component.getSelectedRow();
          if (selectedRow !== null) {
            selectedRowData = _.cloneDeep(component.getRowValues(selectedRow));
          }
          // Retrieve selected row values
          return selectedRowData;
        };
        /**
         * Update the model
         */
        component.updateModel = function () {
          // Update model
          component.updateModelAndSelectRows();
        };
        /**
         * Update the model and select rows
         */
        component.updateModelAndSelectRows = function () {
          // Update model
          component.updateModelSpecific().then(function () {
            // Call select rows event
            component.filterSelection();
            component.setSelection(component.currentSelection);
          });
        };
        /**
         * On update cell data
         *
         * @param {type} rowId
         * @param {type} columnId
         * @param {type} data
         */
        component.updateCell = function (rowId, columnId, data) {
          var value = null, style = null;
          var model = {};
          if (typeof data === "object") {
            value = "value" in data ? data.value : null;
            style = "style" in data ? data.style : null;
            model.selected = value;
            model.values = [data];
          } else {
            value = data;
            model.selected = value;
          }

          if (!Utilities.isEmpty(value)) {
            // Calculate rowIndex
            var rowIndex = Control.getRowIndex(component.model.values, rowId, component.constants.ROW_IDENTIFIER);

            // Update value data
            component.model.values[rowIndex][columnId] = data;

            // Publish model changed
            var address = _.cloneDeep(component.address);
            address.column = columnId;
            address.row = rowId;
            Control.publishModelChanged(address, model);
          }

          // Update cell specific
          component.updateCellSpecific(rowId, columnId, value, style);
        };
        /**
         * On update cell data
         *
         * @param {type} rowId
         * @param {type} columnId
         * @param {type} value
         * @param {type} style
         */
        component.updateCellSpecific = function (rowId, columnId, value, style) {
        };
        /**
         * Retrieve column index
         *
         * @param {string} columnId Column identifier
         * @returns {integer} Selected column index in columns array
         */
        component.getColumnIndex = function (columnId) {
          var colIndex = -1;
          _.each(component.controller.columnModel, function (column, index) {
            if (column.name === columnId) {
              colIndex = index;
            }
          });
          return colIndex;
        };
        /**
         * Retrieve column index
         *
         * @param {string} columnId Column identifier
         * @returns {integer} Selected column index in columns array
         */
        component.getColumn = function (columnId) {
          var columnObject = null;
          _.each(component.controller.columnModel, function (column) {
            if (column.name === columnId) {
              columnObject = column;
            }
          });
          return columnObject;
        };
        /**
         * Retrieve column value list (if defined)
         *
         * @param {string} address Row index
         * @returns {array} Column value list
         */
        component.getColumnValueList = function (address) {
          // Retrieve values from cells if it exists
          var cellId = Utilities.getCellId(address);
          var values = _.get(component, ["model", "cells", cellId], null);
          if (values === null) {
            // Retrieve values from column model if it exists
            var columnModel = component.controller.columnModel;
            var columnIndex = component.getColumnIndex(address.column);
            if ('model' in columnModel[columnIndex]) {
              values = columnModel[columnIndex].model.values;
            }
          }

          return values;
        };
        /**
         * Retrieve total loaded rows
         */
        component.getTotalRows = function () {
          // Retrieve total rows
          return component.model.values.length;
        };
        /**
         * Retrieve filled loaded rows for a column
         *
         * @param {string} column Column to check
         */
        component.getFilledRows = function (column) {
          var filledRows = 0;
          // For each row, check the cell value
          _.each(component.model.values, function (row) {
            // Retrieve cell value
            var cellValue = component.getCellData(row[column], component.constants.CELL_VALUE);
            // Add 1 to filledRows if cell is not empty
            filledRows += cellValue !== null && cellValue !== "" ? 1 : 0;
          });
          return filledRows;
        };
        /**
         * Mark the rows as selected
         *
         * @param {object} selectedRows
         */
        component.selectRows = function (selectedRows) {
          // Store selected rows
          if (!_.isEqual(component.currentSelection, selectedRows)) {
            component.currentSelection = selectedRows;
            component.model.selected = selectedRows.length === 0 ? null : selectedRows;
            // Store event
            component.storeEvent('select-row');
            // Publish model change
            Control.publishModelChanged(component.address, {
              selected: component.model.selected
            });
            component.scope.$broadcast("selectionChanged");
          }
        };
        /**
         * Unselect all rows
         */
        component.unselectRows = function () {
          // Unselect the rows
          component.resetSelection();
        };
        /**
         * Select the rows
         *
         * @param {Array} selection Selection
         */
        component.setSelection = function (selection) {
          // Reset selection
          component.resetSelection();
          if (selection !== null) {
            // Get selection as array
            if (typeof selection === "string") {
              selection = [selection];
            }
            // Set selection
            var data = component.model.values;

            // Sanitize selection
            selection = sanitizeSelection(selection, data, component);

            // Select rows
            _.each(selection, function (row) {
              var rowIndex = Control.getRowIndex(data, row, component.constants.ROW_IDENTIFIER);
              if (rowIndex > -1) {
                component.selectRow(data[rowIndex]);
              }
            });
          }
          // Call select rows event
          component.onSelectRows(selection);
        };

        /**
         * Reset the grid
         */
        component.resetGrid = function () {
          // Change editing flag
          component.isEditing = false;
          // Clear grid data
          component.emptyGrid();
          // Udpate model
          component.updateModel();
        };
        /**
         * Empty the grid
         */
        component.emptyGrid = function () {
          component.currentSelection = [];
          component.model.selected = [];
          component.model.values = [];
          component.model.cells = {};
          component.model.footer = {};
          component.model.records = 0;
          component.model.total = 1;
          component.model.page = 1;
          // Reposition save button
          component.repositionSaveButton();
        };
        /**
         * Filter the selection with the model values
         */
        component.filterSelection = function () {
          // Store selected rows
          var selectedRows = Utilities.asArray(component.currentSelection);
          var availableSelectedRows = [];
          _.each(component.model.values, function (row) {
            var rowId = row[component.constants.ROW_IDENTIFIER];
            if (selectedRows.indexOf(rowId) > -1) {
              availableSelectedRows.push(rowId);
            }
          });

          // Select available rows
          component.selectRows(availableSelectedRows);
        };
        /**
         * On select rows event
         *
         * @param {Array} selectedRows Selected rows
         */
        component.onSelectRows = function (selectedRows) {
          if (!_.isEqual(selectedRows, component.currentSelection)) {
            component.selectRows(selectedRows);
          }
        };
        /**
         * Delete the current row
         */
        component.deleteRow = function () {
          // Restore row values
          var selectedRow = component.getSelectedRow();
          component.deleteRowSpecific(selectedRow);
        };
        /**
         * Add a new row
         *
         * @param {integer} position
         * @param {object} data
         * @param {String} selectedRow selected row
         */
        component.addRow = function (position, data, selectedRow) {
          // Add the new row
          component.addRowSpecific(selectedRow, position, data);
        };
        /**
         * Update the selected row
         *
         * @param {object} data
         */
        component.updateRow = function (rowId, data, style) {
          _.each(data, function (value, columnId) {
            component.updateCell(rowId, columnId, value);
          });

          // Update row style if defined
          if (style) {
            component.addRowStyle(rowId, style);
          }

          // Update row specific call
          if (component.updateRowSpecific) {
            component.updateRowSpecific(rowId, data, style);
          }
        };
        /**
         * Update the selected row
         * @param {object} data
         */
        component.updateSelectedRow = function (data, style) {
          // Update selected row
          var selectedRow = component.getSelectedRow();
          component.updateRow(selectedRow, data, style);
        };
        /**
         * Add column dependencies to grid
         *
         * @param {Object} column Column parameters
         * @public
         */
        component.addGridDependencies = function (column) {
          // Store cell specific dependencies
          var columnDependencies = [];
          // Transform show, hide and label dependencies into showColumn,
          // hideColumn and columnLabel dependencies
          _.each(column.dependencies, function (dependency) {

            // Set flag
            var addToGrid = false;
            // Transform target_type depending on previous target_type
            switch (dependency.target) {
              case "show":
                if (!column.component) {
                  dependency.target = "showColumn";
                  addToGrid = true;
                } else {
                  columnDependencies.push(dependency);
                }
                break;
              case "show-column":
                dependency.target = "showColumn";
                addToGrid = true;
                break;
              case "hide":
                if (!column.component) {
                  dependency.target = "hideColumn";
                  addToGrid = true;
                } else {
                  columnDependencies.push(dependency);
                }
                break;
              case "hide-column":
                dependency.target = "hideColumn";
                addToGrid = true;
                break;
              case "label":
                dependency.target = "columnLabel";
                addToGrid = true;
                break;
              default:
                columnDependencies.push(dependency);
                break;
            }

            // Add column name
            dependency.column = column.id;
            // Add dependency to grid dependencies
            if (addToGrid) {
              component.controller.dependencies.push(_.cloneDeep(dependency));
            }
          });
          // Store cell dependencies
          column.dependencies = columnDependencies;
        };

        /**
         * Add columns
         *
         * @param {Array} columns
         */
        component.addColumns = function (columns) {
          var columnsAdded = false;
          _.each(columns, function (column) {
            var added = component.addColumn(column);
            columnsAdded = columnsAdded || added;
          });

          // Notify changed columns and resize
          Utilities.timeout(function () {
            component.reportChangedColumns();
            component.resize();
          });
        };

        /**
         * Replace columns
         *
         * @param {Array} columns
         */
        component.replaceColumns = function (columns) {

          // Reset grid columns model
          component.controller.columnModel.splice(0, component.controller.columnModel.length);
          component.columnModelStringified = {};

          // Fix column model
          component.fixColumnModel(false);

          // Add columns
          component.addColumns(columns);
        };

        /**
         * Fix retrieved column model
         */
        component.fixColumnModel = function (fixEachColumn) {

          // Add initial columns model if exists
          if (component.addInitialColumns) {
            component.addInitialColumns();
          }

          // Add extra column model if exists
          if (component.addExtraColumnModelData) {
            component.addExtraColumnModelData();
          }

          // For each column, fix the column
          if (fixEachColumn) {
            _.each(component.controller.columnModel, component.fixColumn);
          }
        };

        /**
         * Fix retrieved column model to fit jquery grid colmodel
         *
         * @param {object} column Column model
         */
        component.fixColumn = function (column) {
          // Calculate width based on charlength
          if (column.charlength !== "0" && !column.width) {
            column.width = column.charlength * component.scope.charSize;
          }

          // Set default column width
          if (!column.width) {
            column.width = 20 * component.scope.charSize;
          }

          // Store initial width
          column.initialWidth = column.width;

          // If column is hidden, generate column but don't display it
          if (column.hidden) {
            column.width = 0;
            column.enableColumnResizing = false;
            column.enableColumnMoving = false;
          }

          // Other attributes
          column.displayName = column.label || "";
          column.headerClass = column.style || "";
          column.name = column.name || column.id;
          column.id = column.name;
          column.enableSorting = component.enableSorting && column.sortable;
          column.sortField = column.sortField || column.name;
          column.field = column.name;

          column.pinnedLeft = column.frozen;

          // Retrieve cell template
          column.cellClass = function (grid, row, col) {
            var value = "text-" + col.colDef.align + (col.colDef.style ? " " + col.colDef.style : "");
            var cellStyle = component.getCellStyle(row.entity[col.field]);
            value += cellStyle ? " " + cellStyle : "";
            return value;
          };

          // Column templates
          column.headerCellTemplate = column.headerCellTemplate || "grid/header";
          column.cellTemplate = column.cellTemplate || "grid/cell";
          column.footerCellTemplate = column.footerCellTemplate || "grid/footer";
          column.filterHeaderTemplate = column.filterHeaderTemplate || "grid/headerFilter";

          column.sortingAlgorithm = component.sortingAlgorithm;

          // Calculate fields with components
          if ("component" in column) {
            column.sortField = column.index || column.id;
            column.enableFiltering = false;
            column.cellTemplate = "<div class=\"ui-grid-cell-contents component {{col.cellClass}}\" title=\"TOOLTIP\" column-id=\"{{col.name}}\"><awe-column-" + column.component +
              " cell-address='{\"hash\":\"{{row.uid}}\", \"view\":\"" + component.address.view + "\", \"component\":\"" + component.address.component + "\", \"row\":\"{{row.entity." +
              component.constants.ROW_IDENTIFIER + "}}\", \"column\":\"{{col.name}}\"}'/></div>";
            column.footerCellTemplate = column.summaryType ? "<div class=\"ui-grid-cell-contents ui-grid-cell-footer {{::col.cellClass}}\" title=\"TOOLTIP\" column-id=\"{{col.name}}\"><awe-column-" +
              column.component + " cell-address='{\"hash\":\"footer-{{grid.appScope.model.page}}\", \"view\":\"" + component.address.view + "\", \"component\":\"" +
              component.address.component + "\", \"row\":\"footer\", \"column\":\"{{col.name}}\"}'/></div>" : column.footerCellTemplate;
          }

          // Remove dependencies and add them to the grid
          component.addGridDependencies(column);
        };
        /**
         * On column change
         */
        component.reportChangedColumns = function () {
          var columns = component.getColumns();
          var headerModel = component.controller.headerModel;
          if (headerModel.length > 0 && !component.fixedHeaders) {
            var fixedHeaders = {};
            var currentHeaderColumn = 0;
            var currentHeader = null;

            // For each column assign a header
            _.each(columns, function (column) {
              if (currentHeaderColumn > 0) {
                currentHeaderColumn--;
              } else {
                var header = getHeader(headerModel, column.name);
                if (header) {
                  currentHeader = header;
                  currentHeaderColumn = header.numberOfColumns - 1;
                } else {
                  currentHeader = null;
                }
              }

              // Stored each header for each column
              if (currentHeader !== null) {
                fixedHeaders[column.name] = currentHeader;
              }
            });

            // Store fixedHeaders
            component.fixedHeaders = fixedHeaders;
          }
          component.scope.gridOptions.columnHeaders = {
            columns: columns,
            headers: component.fixedHeaders
          };
          component.scope.$broadcast('columns-changed', component.scope.gridOptions.columnHeaders);
        };
        /**
         * Right click over row
         *
         * @param {type} event
         * @param {type} row
         */
        component.rowContextMenu = function (event, row) {
          component.selectRow(row.entity);
          component.showContextMenu(event);
        };
        /**
         * Click over viewport
         *
         * @param {type} event
         */
        component.viewportClick = function (event) {
          if (event.currentTarget === event.target) {
            component.hideContextMenu(event);
          }
        };
        /**
         * Right click over viewport
         *
         * @param {type} event
         */
        component.viewportContextMenu = function (event) {
          component.showContextMenu(event);
        };

        /**
         * Retrieves the cell value
         *
         * @param {type} cell
         * @returns {String}
         */
        component.getCellValue = function (cell) {
          return component.getCellData(cell, component.constants.CELL_LABEL);
        };

        /**
         * Retrieves the cell value
         *
         * @param {type} cell
         * @returns {String}
         */
        component.getCellStyle = function (cell) {
          return component.getCellData(cell, component.constants.CELL_STYLE);
        };
        /**
         * Edit a row
         *
         * @param {String} rowId row Identifier
         */
        component.editRowSpecific = function (rowId) {
          // Select the row
          var data = component.model.values;
          var rowIndex = Control.getRowIndex(data, rowId, component.constants.ROW_IDENTIFIER);
          if (rowIndex !== -1) {
            component.selectRow(data[rowIndex]);
            // Reposition buttons
            component.repositionSaveButton();
          }
        };
        /**
         * Add an style to a row
         *
         * @param {String} rowId row Identifier
         * @param {String} rowClass row class to add
         */
        component.addRowStyle = function (rowId, rowClass) {
          // Select the row
          var data = component.model.values;
          var rowIndex = Control.getRowIndex(data, rowId, component.constants.ROW_IDENTIFIER);
          data[rowIndex][component.constants.ROW_CLASS_FIELD] = rowClass;
        };
        /**
         * Add column to column model
         *
         * @param {Array} model Column to add
         */
        component.addToColumnModel = function (model) {
          component.controller.columnModel.push(model);
          component.columnModelStringified[model.id] = JSON.stringify(model);
        };
        /**
         * Add column to column model
         *
         * @param {Object} column
         * @returns {boolean} column has been added
         */
        component.addColumn = function (column) {
          var added = false;
          // Check whether column has been already added or not
          if (component.getColumnIndex(column.name) === -1) {
            // Fix the column
            component.fixColumn(column);
            // Add column to columnModel
            component.addToColumnModel(column);
            // Column has been added
            added = true;
          }
          return added;
        };

        /**
         * Calculate totals
         */
        component.calculateTotals = function () {
          if (component.controller.showTotals) {
            // Get the rows that can be summarized
            var summarizedCols = {};
            var footerData = {};
            var componentData = {};
            // Calculate columns to summarize
            _.each(component.controller.columnModel, function (column, columnIndex) {
              var columnName = column.name;
              var summaryType = column.summaryType || null;
              if (summaryType !== null) {
                summarizedCols[columnName] = {
                  name: columnName,
                  type: summaryType,
                  value: 0,
                  index: columnIndex,
                  component: column.component
                };
              }

              // Store as footer data
              if (column.component) {
                componentData[columnName] = null;
              } else {
                footerData[columnName] = "";
              }
            });
            // For each row, summarize the data
            _.each(component.model.values, function (row, rowIndex) {
              _.each(summarizedCols, function (column, columnId) {
                // Summarize value
                switch (column.type) {
                  case "sum":
                  default:
                    var value = parseFloat(getRowValue(component.model.values, rowIndex, columnId));
                    column.value += isNaN(value) ? 0 : value;
                    break;
                }
                // Store as footer data
                if (column.component) {
                  componentData[columnId] = column.value;
                } else {
                  footerData[columnId] = column.value;
                }
              });
            });
            component.model.footer = componentData;
            // Broadcast footer change
            Utilities.publishFromScope("footer-changed", {
              footer: component.model.footer
            }, component.scope);
          }
        };

        /** ******************************************************************* */
        /* EVENTS */
        /** ******************************************************************* */

        // Map grid actions
        GridEvents.mapCommonActions(component);
        /** ******************************************************************* */
        /* API METHODS */
        /** ******************************************************************* */

        /**
         * Apply dependency target
         *
         * @param {Object} dependency Dependency
         * @param {String} value Target value
         * @param {Boolean} update Condition updated
         */
        component.api.applyDependency = function (dependency, value, update) {
          var target = dependency.target || "none";
          switch (target) {
            // Show column if update
            case "showColumn":
              if (update) {
                component.showColumn(dependency.column);
              } else {
                component.hideColumn(dependency.column);
              }
              break;
              // Hide column if update
            case "hideColumn":
              if (update) {
                component.hideColumn(dependency.column);
              } else {
                component.showColumn(dependency.column);
              }
              break;
              // Change column label
            case "columnLabel":
              component.changeColumnLabel(dependency.column, value);
              break;
            default:
              break;
          }
        };

        /**
         * API link to finish loading
         */
        component.api.endLoad = function () {
          Control.changeControllerAttribute(component.address, {
            loading: false
          });
          Utilities.publishFromScope("compiled", component.address.view, component.scope);
        };

        /**
         * API link to update the selected values
         *
         * @param {Object} selectedValues
         */
        component.api.updateSelectedValue = function (selectedValues) {
          // Store value list
          var selectedList = [];
          _.each(selectedValues, function (selected) {
            if (typeof selected === "object" && "value" in selected) {
              selectedList.push(selected.value);
            } else {
              selectedList.push(selected);
            }
          });

          // Set selection
          component.setSelection(selectedList);
        };

        // Initialize as multioperation
        var initializationFlag = true;
        if (this.isMultioperation()) {
          component.gridMultioperation = new GridMultioperation(this.component);
          initializationFlag = component.gridMultioperation.init();
          // Initialize as editable
        } else if (this.isEditable()) {
          component.gridEditable = new GridEditable(this.component);
          initializationFlag = component.gridEditable.init();
          // Initialize with component management
        } else {
          component.gridComponents = new GridComponents(this.component);
          initializationFlag = component.gridComponents.init();
        }

        return initializationFlag;
      },
      /**
       * Check if column model has components
       *
       * @returns {Boolean}
       */
      hasComponents: function () {
        var hasComponents = false;
        _.each(this.component.controller.columnModel, function (column) {
          if ("component" in column) {
            hasComponents = true;
          }
        });
        return hasComponents;
      },
      /**
       * Check if grid is multioperation
       *
       * @returns {Boolean}
       */
      isMultioperation: function () {
        return this.component.controller.multioperation;
      },
      /**
       * Check if grid is editable
       *
       * @returns {Boolean}
       */
      isEditable: function () {
        return this.component.controller.editable;
      },
      /**
       * Check if grid is editable
       *
       * @returns {Boolean}
       */
      hasFrozenColumns: function () {
        var hasFrozen = false;
        _.each(this.component.controller.columnModel, function (column) {
          if (column.frozen) {
            hasFrozen = true;
          }
        });
        return hasFrozen || this.component.isTree;
      }
    };
    return GridCommons;
  }]);
