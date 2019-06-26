import { aweApplication } from "./../../awe";
import { ClientActions } from "../../data/actions";

// Grid events service
aweApplication.factory('GridEvents',
  ['ActionController', 'Control', '$translate',
    /**
     * Grid generic methods
     *
     * @param {service} $actionController Action controller service
     * @param {service} Control Awe control service
     * @param {service} $translate Translate service
     */
    function ($actionController, Control, $translate) {
      /**
       * Add a row
       * @param {Object} parameters Action parameters
       * @param {Object} scope Scope
       * @param {String} position Where to add
       * @param {Object} rowData Row data
       */
      function addRow(parameters, scope, position, rowData) {
        // Add a row after selected or at the end
        var component = scope.component;
        component.hideContextMenu();
        var selectedRow = parameters.selectedRow || component.getSelectedRow() || null;
        component.addRow(position, rowData, selectedRow);

        // Launch action after save row
        var afterAddRow = {
          type : 'after-add-row',
          silent : true
        };

        // Send action list
        $actionController.addActionList([afterAddRow], true, {address: component.address, context: component.context});

        // Store event
        component.storeEvent('add-row');
      }

      /*************************************************************************
       * EVENTS
       ************************************************************************/
      var GridEvents = {
        /**
         * Map common actions
         *
         * @param {Object} component
         */
        mapCommonActions: function (component) {
          // Capture reset action
          component.listeners['resetScope'] = component.scope.$on('reset-scope', function (event, view) {
            if (view === component.address.view) {
              component.resetGrid();
              Control.publishModelChanged(component.address, {selected: component.model.selected});
            }
          });

          // Capture restore action
          component.listeners['restoreScope'] = component.scope.$on('restore-scope', function (event, view) {
            if (view === component.address.view) {
              component.resetGrid();
              Control.publishModelChanged(component.address, {selected: component.model.selected});
            }
          });

          component.listeners['languageChanged'] = component.scope.$on('languageChanged', function () {
            component.updatePaginationText();
          });

          // Watch for element resize
          component.listeners['resize'] = component.scope.$on("resize", component.resize);
          component.listeners['resize-action'] = component.scope.$on("resize-action", component.resize);
          component.listeners['model-changed'] = component.scope.$on("modelChanged", function (event, launchers) {
            if (launchers && component.address.component in launchers && "values" in launchers[component.address.component]) {
              // Update totals
              component.calculateTotals();
            }
          });

          // Action listener definition
          $actionController.defineActionListeners(component.listeners, ClientActions.grid.commons, component.scope, GridEvents);
        },
        /**
         * Map base actions
         *
         * @param {object} component
         */
        mapBaseActions: function (component) {
        },
        /**
         * Map tree actions
         *
         * @param {object} component
         */
        mapTreeActions: function (component) {
          $actionController.defineActionListeners(component.listeners, ClientActions.grid.tree, component.scope, GridEvents);
        },
        /**
         * Map editable actions
         *
         * @param {object} component
         */
        mapEditableActions: function (component) {
          $actionController.defineActionListeners(component.listeners, ClientActions.grid.editable, component.scope, GridEvents);
        },
        /**
         * Reset grid
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onReset: function (parameters, scope) {
          var component = scope.component;
          component.resetGrid();
        },
        /**
         * Restore grid
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onRestore: function (parameters, scope) {
          var component = scope.component;
          component.resetGrid();
        },
        /**
         * Validate selected row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        validateSelectedRow: function (parameters, scope) {
          // Launch action for saving row
          var component = scope.component;
          var actions = [];
          var validate = {type: 'validate', silent: true};

          // Add validate row if defined on grid
          validate.target = `grid-${component.address.component} .ui-grid-row-selected`;
          actions.push(validate);

          // Send action list
          $actionController.addActionList(actions, false, {address: component.address, context: component.context});
        },
        /**
         * Before save row
         *
         * @param {object} component
         */
        onBeforeSaveRow: function (component) {
          // Launch action for saving row
          var actions = [];
          var validate = {type: 'validate-selected-row', silent: true};
          var saveRow = {type: 'save-row', silent: true};

          // Add validate row if defined on grid
          var controller = Control.getAddressController(component.address);
          if (controller.validateOnSave) {
            actions.push(validate);
          }
          // Add save row action
          actions.push(saveRow);

          // Send action list
          $actionController.addActionList(actions, true, {address: component.address, context: component.context});

          // Store event
          component.storeEvent('before-save-row');
        },
        /**
         * Save row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onSaveRow: function (parameters, scope) {
          // Save the row
          var component = scope.component;
          component.saveRow();

          // Launch action after save row
          var afterSaveRow = {type: 'after-save-row', silent: true};

          // Send action list
          $actionController.addActionList([afterSaveRow], true, {address: component.address, context: component.context});

          // Store event
          component.storeEvent('save-row');
        },
        /**
         * After save row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAfterSaveRow: function (parameters, scope) {
          // Store event
          var component = scope.component;
          component.storeEvent('after-save-row');

          // Call select rows event
          component.unselectRows();
        },
        /**
         * Before cancel row
         *
         * @param {object} component
         */
        onBeforeCancelRow: function (component) {
          // Launch action for saving row
          var disableDependencies = {type: 'disable-dependencies', silent: true};
          var enableDependencies = {type: 'enable-dependencies', silent: true};
          var cancelRow = {type: 'cancel-row', silent: true};

          // Send action list
          $actionController.addActionList([disableDependencies, cancelRow, enableDependencies], true, {address: component.address, context: component.context});

          // Store event
          component.storeEvent('before-cancel-row');
        },
        /**
         * Cancel row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCancelRow: function (parameters, scope) {
          // Save the row
          var component = scope.component;
          component.cancelRow();

          // Launch action after save row
          var afterCancelRow = {type: 'after-cancel-row', silent: true};

          // Send action list
          $actionController.addActionList([afterCancelRow], true, {address: component.address, context: component.context});

          // Store event
          component.storeEvent('cancel-row');
        },
        /**
         * After cancel row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAfterCancelRow: function (parameters, scope) {
          // Store event
          var component = scope.component;
          component.storeEvent('after-cancel-row');

          // Call select rows event
          component.unselectRows();
        },
        /**
         * Delete row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onDeleteRow: function (parameters, scope) {
          // Remove the selected row
          var component = scope.component;
          component.deleteRow();

          // Call select rows event
          component.unselectRows();

          // Launch action after save row
          var afterDeleteRow = {type: 'after-delete-row', silent: true};

          // Send action list
          $actionController.addActionList([afterDeleteRow], true, {address: component.address, context: component.context});

          // Store event
          component.storeEvent('delete-row');
        },
        /**
         * After delete row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAfterDeleteRow: function (parameters, scope) {
          // Store event
          var component = scope.component;
          component.storeEvent('after-delete-row');
        },
        /**
         * Add row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAddRow : function(parameters, scope) {
          addRow(parameters, scope, "child", parameters.row || {});
        },
            /**
         * Add row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAddRowTop : function(parameters, scope) {
          addRow(parameters, scope, "first", parameters.row || {});
        },
        /**
         * Add row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAddRowBeforeSelected : function(parameters, scope) {
          addRow(parameters, scope, "before", parameters.row || {});
        },
        /**
         * Add row after selected
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAddRowAfterSelected : function(parameters, scope) {
          addRow(parameters, scope, "after", parameters.row || {});
        },
        /**
         * Copy row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCopyRow : function(parameters, scope) {
          addRow(parameters, scope, "child", scope.component.getSelectedRowData());
        },
        /**
         * Copy row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCopyRowTop : function(parameters, scope) {
          addRow(parameters, scope, "first", scope.component.getSelectedRowData());
        },
        /**
         * Copy row before selected
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCopyRowBeforeSelected : function(parameters, scope) {
          addRow(parameters, scope, "before", scope.component.getSelectedRowData());
        },
        /**
         * Copy row after selected
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCopyRowAfterSelected : function(parameters, scope) {
          addRow(parameters, scope, "after", scope.component.getSelectedRowData());
        },
        /**
         * Copy selected rows to clipboard
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCopySelectedRowsToClipboard: function(parameters, scope) {
          // Hide context menu if showing
          var component = scope.component;
          component.hideContextMenu();

          // We get visible columns and filter rowNum and chkBox
          var visibleColumns = component.getVisibleColumns();
          var filteredVisibleColumns = [];
          for(var col in visibleColumns) {
            if(visibleColumns.hasOwnProperty(col)) {
              if(col !== "rowNum" && col !== "chkBox" && col !== "RowIco") {
                filteredVisibleColumns.push({"id": col});
              }
            }
          }

          var rowsPrintData = [];
          rowsPrintData.push([]);

          // We fill the first row with column's names
          _.each(filteredVisibleColumns, function(column) {
            rowsPrintData[0].push($translate.instant(component.getColumn(column.id).label));
          });

          // For each column, we get all the values from the selected columns
          _.each(filteredVisibleColumns, function(column) {
            var columnPrintData = component.getColumnPrintData(column.id);
            var selectedColumnPrintData = columnPrintData[column.id + component.constants.SELECTED_TAIL] || [];

            // For each value, we add it to its location (at the beggining, the
            // array corresponding to the row won't be created
            _.each(selectedColumnPrintData, function(cellPrintData, index) {
              // Index + 1 because first row is for column's names
              if (!rowsPrintData[index + 1]) {
                rowsPrintData.push([]);
              }
              rowsPrintData[index + 1].push(cellPrintData);
            });

          });

          // We join all values of a row and separate them with tabs
          _.each(rowsPrintData, function(rowPrintData, index) {
            rowsPrintData[index] = rowPrintData.join("\t");
          });

          // We join all rows and separate them with new line feed
          var printData = rowsPrintData.join("\n");

          // Event handler for copy event
          var eventHandler = function(clipEvent) {
            // Add data information to event and stop it
            clipEvent.stopPropagation();
            clipEvent.preventDefault();

            var cd = clipEvent.originalEvent.clipboardData || window.clipboardData;
            cd.setData("text", printData);

            // Remove copy event handler
            document.removeEventListener("copy", eventHandler);
          };

          // We launch the copy event
          $(document).on("copy", eventHandler);
          try {
            document.execCommand("copy");
          } catch (e) {
            $log.error("Copying to clipboard is not allowed");
          }
        },
        /**
         * Update row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onUpdateRow: function (parameters, scope) {
          // Add a row after selected or at the end
          var component = scope.component;
          if (parameters.rowId) {
            component.updateRow(parameters.rowId, parameters.row, parameters.style);
          } else {
            component.updateSelectedRow(parameters.row, parameters.style);
          }

          // Store event
          component.storeEvent('update-row');
        },
        /**
         * After add row
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAfterAddRow: function (parameters, scope) {
          // Store event
          var component = scope.component;
          component.storeEvent('after-add-row');
        },
        /**
         * Check if any records are being edited
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCheckRecordSaved: function (parameters, scope) {
          // Store event
          var component = scope.component;
          component.storeEvent('check-records-saved');

          if (component.isEditing) {
            // Send grid message
            GridEvents.sendGridMessage('warning', 'GRID_CHECK_ALL_SAVED_TITLE', 'GRID_CHECK_ALL_SAVED_MESSAGE');
          }
        },
        /**
         * Select first row of the grid
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onSelectFirstRow: function (parameters, scope) {
          var rows = [];
          var component = scope.component;
          // Retrieve first row identifier
          if (component.model.values && component.model.values.length > 0) {
            var rowIndex = 0;
            var row = component.model.values[rowIndex];

            // Store row identifier
            rows.push(String(row[component.constants.ROW_IDENTIFIER]));

            // Select rows
            component.setSelection(rows);
          }
        },
        /**
         * Select last row of the grid
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onSelectLastRow: function (parameters, scope) {
          var rows = [];
          var component = scope.component;
          var values = component.model.values;
          // Retrieve last row identifier
          if (values && values.length > 0) {
            var rowIndex = values.length - 1;
            var row = values[rowIndex];

            // Store row identifier
            rows.push(String(row[component.constants.ROW_IDENTIFIER]));

            // Select rows
            component.setSelection(rows);
          }
        },
        /**
         * Select all rows of the grid
         *
         * @param {object} parameters
         * @param {scope} scope
         */
        onSelectAllRows: function (parameters, scope) {
          var rows = [];
          var component = scope.component;
          var values = component.model.values;
          if (values) {
            // Retrieve first row identifier
            _.each(values, function (row) {
              // Store row identifier
              rows.push(String(row[component.constants.ROW_IDENTIFIER]));
            });
          }
          // Select rows
          component.setSelection(rows);
        },
        /**
         * Unselect all rows of the grid
         *
         * @param {object} parameters
         * @param {scope} scope
         */
        onUnselectAllRows: function (parameters, scope) {
          // Select rows
          var component = scope.component;
          component.setSelection([]);
        },
        /**
         * Check if there are some rows generated
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCheckRecordsGenerated: function (parameters, scope) {
          // Store event
          var component = scope.component;
          component.storeEvent('check-records-generated');

          if (component.model.records === 0) {
            // Send grid message
            GridEvents.sendGridMessage('warning', 'GRID_CHECK_RECORDS_GENERATED_TITLE', 'GRID_CHECK_RECORDS_GENERATED_MESSAGE');
          }
        },
        /**
         * Check one row selected
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCheckOneRowSelected: function (parameters, scope) {
          // Store event
          var component = scope.component;
          component.storeEvent('check-one-selected');

          // Get number of selected rows
          var rowCount = component.currentSelection.length;

          // Cancel action stack and send message
          if (rowCount !== 1) {
            // Send grid message
            GridEvents.sendGridMessage('warning', 'GRID_CHECK_ONE_SELECTED_TITLE', 'GRID_CHECK_ONE_SELECTED_MESSAGE');
          }
        },
        /**
         * Check some row selected
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onCheckSomeRowSelected: function (parameters, scope) {
          // Store event
          var component = scope.component;
          component.storeEvent('check-some-selected');

          // Get number of selected rows
          var rowCount = component.currentSelection.length;

          // Cancel action stack and send message
          if (rowCount === 0) {
            // Send grid message
            GridEvents.sendGridMessage('warning', 'GRID_CHECK_SOME_SELECTED_TITLE', 'GRID_CHECK_SOME_SELECTED_MESSAGE');
          }
        },
        /**
         * Add columns to the grid
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onAddColumns: function (parameters, scope) {
          // Add the columns to the grid
          scope.component.addColumns(parameters.columns);
        },
        /**
         * Replace all columns to the grid
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onReplaceColumns: function (parameters, scope) {
          // Replace the columns to the grid
          scope.component.replaceColumns(parameters.columns);
        },
        /**
         * Show columns
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onShowColumns: function (parameters, scope) {
          // Add the columns to the grid
          (parameters.columns || []).forEach((column) => scope.component.showColumn(column));
        },
        /**
         * Hide columns
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onHideColumns: function (parameters, scope) {
          // Add the columns to the grid
          (parameters.columns || []).forEach((column) => scope.component.hideColumn(column));
        },
        /**
         * Expand branch
         *
         * @param {object} parameters
         * @param {object} scope
         */
        onBranchExpand: function (parameters, scope) {
          // Replace the columns to the grid
          var component = scope.component;
          component.onBranchExpand(parameters.datalist);
        },
        /**
         * Update cell data
         *
         * @param {object} parameters
         * @param {object} scope
         * @param {object} address
         */
        onUpdateCell: function (parameters, scope, address) {
          // Update cell data
          var component = scope.component;
          component.updateCell(address.row, address.column, parameters.data);
        },
        /**
         * Send grid message
         *
         * @param {String} type
         * @param {String} title
         * @param {String} content
         */
        sendGridMessage: function (type, title, content) {
          // Create send message action
          var messageAction = {type: 'message', silent: false};

          // Create cancel action
          var cancelAction = {type: 'cancel', silent: false};

          // Add message to action
          messageAction.parameters = {
            type: type,
            title: title,
            message: content
          };

          // Send action send message
          $actionController.addActionList([messageAction, cancelAction], false, {address: component.address, context: component.context});
        }
      };
      return GridEvents;
    }
  ]);
