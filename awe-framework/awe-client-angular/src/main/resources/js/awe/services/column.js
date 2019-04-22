import { aweApplication } from "./../awe";

// Column service
aweApplication.factory('Column',
  ['Control', 'AweUtilities',
    /**
     * Criterion generic methods
     * @param {object} $control Control service
     * @param {object} $utilities Utilities service
     */
    function ($control, $utilities) {

      /**
       * Fix column address
       * @param {type} address
       * @returns {unresolved}
       */
      function fixAddress(address) {
        var addressFixed = $utilities.parseJSON(address);
        delete addressFixed.hash;
        return addressFixed;
      }

      /**
       * Column generation
       * @param {type} attrs
       * @returns {Column}
       */
      function Column(attrs) {
        this.attributes = attrs;
        this.address = fixAddress(attrs.cellAddress);
        this.id = $utilities.getCellId(this.address);
      }
      Column.prototype = {
        /**
         * Initialize column
         * @param {Component} component
         */
        init: function (component) {
          var column = this;
          component.editing = false;

          // Scope attributes
          component.scope.initialized = false;
          component.scope.iconLoader = "icon";
          component.scope.loaderIcon = "fa-spinner fa-spin";
          var $gridNode = $('#scope-' + column.address.component);
          column.gridScope = $gridNode.scope() || component.col.grid.appScope;
          var gridComponent = column.gridScope.component;

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * Publish model changed
           */
          component.columnModelChange = function () {
            // Change model value
            component.model.selected = component.model.selected !== '' ? component.model.selected : null;
            if (component.model.selected) {
              component.model.values = [{value: component.model.selected, label: component.model.selected}];
              component.modelChange();
            }
          };

          /**
           * Publish model changed
           */
          component.modelChange = function () {
            // Update visible value
            component.updateVisibleValue();

            // Change grid value
            gridComponent.updateCellModel(component);

            // Publish model changed
            $control.publishModelChanged(component.address, {selected: component.model.selected});
          };

          /**
           * Retrieves visible value for the array
           * @returns {string} visible value
           */
          component.getVisibleValue = function () {
            return component.model.selected;
          };

          /**
           * Reset and restore column
           */
          component.onReset = $utilities.noop;
          component.onRestore = $utilities.noop;

          /******************************************************************************
           * SCOPE METHODS
           *****************************************************************************/

          /**
           * On save row catch event
           * @param {event} event
           */
          component.scope.saveRow = function (event) {
            // Cancel event propagation
            $utilities.stopPropagation(event, true);

            // Launch save row
            gridComponent.scope.onSaveRow();
          };

          /**
           * On click event
           * @param {Object} e Event
           */
          component.scope.click = function (e) {
            if (component.editing) {
              component.scope.focus();
              // Cancel event propagation
              $utilities.stopPropagation(e);
            }
          };

          /******************************************************************************
           * PRIVATE METHODS
           *****************************************************************************/

          /**
           * Update cell edition mode
           * @param {boolean} editing Editing
           * @returns {undefined}
           */
          var changeCellVisibility = function (editing) {
            if (component.element) {
              if (editing) {
                component.element.find(".edition").show();
                component.element.find(".visible-value").hide();
              } else {
                component.element.find(".visible-value").show();
                component.element.find(".edition").hide();
              }
            }
          };

          /**
           * Retrieves visible value for the array
           */
          var updateEditing = function () {
            var editing = false;
            var selected = gridComponent.model.selected;
            if (selected && selected.length === 1 && String(selected[0]) === String(component.address.row)) {
              editing = true;

              // Initialize on select row
              component.scope.initialized = true;
            }
            if (editing !== component.editing) {
              component.editing = editing;
              changeCellVisibility(component.editing);

              if (editing) {
                $utilities.publishFromScope("editing-cell", component.address, component.scope);
              }
            }
          };

          /**
           * Update address and model (in case of rowId change)
           */
          var updateAddressAndModel = function () {
            // Get new address
            column.address = fixAddress(column.attributes.cellAddress);

            // Generate column id
            column.id = $utilities.getCellId(column.address);

            // Update component element
            component.id = column.id;
            component.address = column.address;


            //component.element = column.attributes.$$element;
            $control.setAddressApi(component.address, component.api);

            // Check if cell is already initialized
            var alreadyInitialized = gridComponent.checkInitialized(component.address);

            // Calculate scope view
            component.view = component.address.view;

            // Get model from grid model
            component.model = gridComponent.getModel(component.address);

            // Get controller from grid controller
            var controller = gridComponent.getController(component.address);

            // If controller, add id and type values
            if (controller) {
              controller.id = column.id;
              component.controller = controller;
              component.columnClass = "text-" + controller.align;
            }

            // Update scope
            component.scope.address = component.address;
            component.scope.model = component.model;
            component.scope.controller = component.controller;

            // Initialize the cell
            if (!alreadyInitialized && component.controller.dependencies && component.controller.dependencies.length > 0) {
              $utilities.publish("initialize-cell", component.address);
            }
          };

          /**
           * On row change or grid change, reinitialize the cell
           */
          var onRowChange = function () {
            updateAddressAndModel();
            updateEditing();
            component.updateVisibleValue();
          };

          // Update address, model an editing on load
          updateAddressAndModel();
          updateEditing();

          /**********************************************************************/
          /* API METHODS                                                        */
          /**********************************************************************/
          component.api = component.api || {};

          /**
           * API link to update the model values
           * @param {object} data New model data attributes
           */
          component.api.updateModelValues = function (data) {
            var model = $control.getAddressModel(component.address);
            if (model) {
              _.merge(model, data);
              component.updateVisibleValue();
            }

            component.api.updateComponentModelValues(data);
          };

          /******************************************************************************
           * EVENT LISTENERS
           *****************************************************************************/
          component.listeners = component.listeners || {};

          // Watch for row-uid changed and update address, model and visible values
          component.listeners['cellRowChanged'] = this.attributes.$observe("cellAddress", onRowChange);

          // watch for external changes to model and re-render element
          component.listeners['cellSelectionChanged'] = component.scope.$on("selectionChanged", function () {
            updateEditing();
          });

          // watch for external changes to model and re-render element
          component.listeners['cellFooterChanged'] = component.scope.$on("footer-changed", function (event, parameters) {
            var footer = parameters.footer;
            // Retrieve visible value
            if (component.address.row === "footer" && component.address.column in footer) {
              // Regenerate footer model
              component.model = gridComponent.getModel(component.address);

              // Store the cell value in the grid
              component.model.selected = footer[component.address.column];

              // Report the plugins of model changed
              component.modelChange();
            }
          });

          // On number format check new visible value
          component.listeners["cellModelChanged"] = component.scope.$on("modelChanged", function (event, launchers) {
            var changes = $utilities.modelChanged(component, launchers);
            if (changes) {
              // Change grid value
              component.api.updateModelValues(changes);
              component.updateVisibleValue();
              gridComponent.updateCellModel(component);
            }
          });

          // On number format check new visible value
          component.listeners["cellVisibleValue"] = component.scope.$on("visibleValue", function () {
            component.updateVisibleValue();
          });

          // Retrieve component
          return component;
        }
      };
      return Column;
    }
  ]);
