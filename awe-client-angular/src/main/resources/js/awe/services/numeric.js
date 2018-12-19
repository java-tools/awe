import { aweApplication } from "./../awe";

// Numeric service
aweApplication.factory('Numeric',
  ['Criterion', 'AweUtilities', 'AweSettings', 'Control',
    /**
     * Numeric generic methods
     * @param {Service} Criterion
     * @param {Service} Utilities
     * @param {Service} $settings
     * @param {Service} Control
     */
    function (Criterion, Utilities, $settings, Control) {
      /**
       * Numeric constructor
       * @param {Scope} scope Numeric scope
       * @param {String} id Numeric id
       * @param {String} element Numeric element
       */
      function Numeric(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Criterion(this.scope, this.id, this.element);
        var numeric = this;
        this.component.asNumeric = function () {
          return numeric.init();
        };
        return this.component;
      }
      Numeric.prototype = {
        /**
         * Initialize numeric criteria
         */
        init: function () {
          // Initialize criterion
          var component = this.component;
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          /**********************************************************************/
          /* PRIVATE METHODS                                                    */
          /**********************************************************************/

          // Update number format
          var updateNumberFormat = function () {
            var controller = Control.getAddressController(component.address);
            var numberFormat;
            switch (typeof controller.numberFormat) {
              case "string":
                numberFormat = Utilities.evalJSON(controller.numberFormat);
                break;
              case "object":
                numberFormat = _.cloneDeep(controller.numberFormat);
                break;
              default:
                numberFormat = {};
            }
            if (!_.isEqual(component.scope.aweNumericOptions, numberFormat)) {
              component.scope.aweNumericOptions = numberFormat;
              component.scope.$broadcast("updateNumberFormat", numberFormat);
            }
          };

          /**
           * Sanitize selected value
           * @param {object | array | number} Selected value
           * @return Sanitized value
           */
          var sanitizeModel = function (selectedValue) {
            var sanitizedValue = selectedValue;
            // Check array
            if (Array.isArray(selectedValue) && selectedValue.length > 0) {
              // Get first value
              sanitizedValue = selectedValue[0];
            }

            // Check object
            if (angular.isObject(sanitizedValue) && "value" in sanitizedValue) {
              sanitizedValue = sanitizedValue.value;
            }

            // Check object
            return sanitizedValue;
          }

          /**********************************************************************/
          /* COMPONENT METHODS                                                  */
          /**********************************************************************/

          /**
           * Extra data function (overwrite criterion getData function)
           * @returns {Object} Data from criteria
           */
          component.getData = function () {
            // Initialize data
            var data = {};
            if (component.model.selected === null) {
              data[component.address.component] = null;
            } else {
              data[component.address.component] = parseFloat(component.model.selected);
            }

            if (component.getExtraData) {
              data[component.address.component + $settings.get("dataSuffix")] = component.getExtraData();
            }
            return data;
          };

          /**
           * Call update model
           * @returns {undefined}
           */
          component.onModelChanged = function () {
            // 1.- Sanitize model
            component.model.selected = sanitizeModel(component.model.selected);

            // 2.- Update model value
            if (component.updateModel) {
              component.updateModel();
            }
          };

          /**
           * Retrieves visible value for the numeric
           * @returns {string} visible value
           */
          component.getVisibleValue = function () {
            var visibleValue = "";
            var model = Control.getAddressModel(component.address);
            // Refresh visible value if not generated yet
            if (component.updateModel) {
              component.updateModel();
            }
            if (model.values.length > 0 && model.values[0].label) {
              visibleValue = model.values[0].label;
            }
            return visibleValue;
          };


          /**********************************************************************/
          /* API METHODS                                                        */
          /**********************************************************************/
          /**
           * API link to update the model values
           * @param {object} data New model data attributes
           */
          component.api.updateModelValues = function (data) {
            var model = Control.getAddressModel(component.address);
            if (model) {
              _.merge(model, data);
              if (component.updateModel) {
                component.updateModel(model);
              }
            }
          };

          /******************************************************************************
           * EVENT LISTENERS
           *****************************************************************************/
          component.listeners = component.listeners || {};

          // Action listener definition
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, check: ["selected"], service: component, method: "onModelChanged"});

          // On number format change launch dependency
          component.listeners["controllerChange"] = component.scope.$on("controllerChange", function (event, parameters) {
            if (_.isEqual(parameters.address, component.scope.address) && "numberFormat" in parameters.controller) {
              updateNumberFormat();
            }
          });

          // Update number format at start
          updateNumberFormat();

          // Finish initialization
          return true;
        }
      };

      return Numeric;
    }
  ]);