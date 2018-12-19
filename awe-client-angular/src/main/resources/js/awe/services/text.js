import { aweApplication } from "./../awe";

// Text service
aweApplication.factory('Text',
  ['Criterion', 'AweUtilities', 'Control',
    /**
     * Criterion generic methods
     * @param {Service} Criterion
     * @param {Service} Utilities Utilities service
     * @param {Control} Control Control service
     */
    function (Criterion, Utilities, Control) {
      /**
       * Text constructor
       * @param {Scope} scope Numeric scope
       * @param {String} id Numeric id
       * @param {String} element Numeric element
       */
      function Text(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Criterion(this.scope, this.id, this.element);
        var text = this;
        this.component.asText = function () {
          return text.init();
        };
        return this.component;
      }
      Text.prototype = {
        /**
         * Initialize text
         */
        init: function () {
          // Initialize criterion
          var component = this.component;
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          // Update icon class
          component.scope.iconClass = " fa fa-" + component.controller.icon;

          /**********************************************************************
           * PRIVATE METHODS
           **********************************************************************/

          /**
           * Retrieves visible value for the selector
           * @returns {string} visible value
           */
          function fixModel(changed) {
            var selected = component.model.selected;
            var values = component.model.values;

            // Changed selected
            if ("selected" in changed) {
              values[0] = {value: selected, label: selected || ""};
            // Changed values
            } else if ("values" in changed && values.length > 0) {
              component.model.selected = values[0].value;
            }
          }

          /**********************************************************************
           * SCOPE METHODS
           **********************************************************************/

          /**
           * Launch click event
           */
          component.scope.onClick = function () {
            Utilities.timeout(function () {
              component.storeEvent('click');
            });
          };

          /**********************************************************************
           * COMPONENT METHODS
           **********************************************************************/

          /**
           * Retrieves visible value for the selector
           * @returns {string} visible value
           */
          component.getVisibleValue = function () {
            var visibleValue = "";
            if (component.model.values.length > 0) {
              visibleValue = component.model.values[0].label;
            } else {
              visibleValue = component.model.selected;
            }
            return visibleValue;
          };

          /**
           * Update the model on model changed
           */
          component.onModelChanged = function (changed) {
            // Fill data
            fixModel(changed || {});

            // Fill data
            component.updateVisibleValue();
          };

          // Fix model on load
          if (component.model.values.length > 0) {
            component.onModelChanged({values: true});
          } else if (!Utilities.isEmpty(component.model.selected)) {
            component.onModelChanged({selected: true});
          }

          /**********************************************************************
           * EVENTS
           **********************************************************************/

          // Action listener definition
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, service: component, method: "onModelChanged"});

          // Initialization ok
          return true;
        }
      };
      return Text;
    }
  ]);
