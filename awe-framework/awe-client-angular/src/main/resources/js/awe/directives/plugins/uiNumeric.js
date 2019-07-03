import { aweApplication } from "./../../awe";
import "autonumeric";


/**
 * Process numeric options
 * @param options Object
 * @return Options processed
 */
function processNumericOptions(options) {
  let optionsProcess = options;

  // Change attribute names
  if ("min" in options) {
    optionsProcess.vMin = options.min;
  }
  if ("max" in options) {
    optionsProcess.vMax = options.max;
  }
  if ("precision" in options) {
    optionsProcess.mDec = Math.floor(options.precision);
  }
  return optionsProcess;
}

// Numeric plugin
aweApplication.directive('uiNumeric',
  ['AweSettings', 'AweUtilities', 'Control',
    /**
     * jquery Autonumeric angular wrapper
     * @param {Service} $settings
     * @param {Service} Utilities
     * @param {Service} Control
     */
    function ($settings, Utilities, Control) {
      // Declare a empty options object

      let options = _.cloneDeep($settings.get("numericOptions"));
      return {
        // This directive only works when used in element's attribute (e.g: ui-numeric)
        restrict: 'A',
        priority: 1,
        link: function (scope, elem, attrs) {
          // Read options
          let opts = options;
          let initialized = false;

          // Watch for numeric options changes
          let initWatch = scope.$watch(attrs.uiNumeric, initPlugin);

          /**
           * Plugin initialization
           * @param {object} newValues plugin parameters
           */
          function initPlugin(newValues) {
            if (newValues) {
              // Initialize element as autoNumeric with options
              opts = _.merge({}, options, newValues);

              // Process numeric options
              opts = processNumericOptions(opts);

              if (initialized) {
                elem.autoNumeric('update', opts);
              } else {
                // Set autonumeric
                elem.autoNumeric(opts);
                initialized = true;

                // Update the model
                updateModel();

                // Bind change event
                elem.on("change", function () {
                  Utilities.timeout(function () {
                    let model = Control.getAddressModel(scope.component.address);
                    model.selected = elem.autoNumeric('get');
                    updateModelValues();
                    scope.component.modelChange();
                  });
                });

                // Define management methods

                /**
                 * Component method links
                 */
                scope.component.updateModel = updateModel;

                /**
                 * API Links
                 */
                if (scope.component.api) {
                  /**
                   * API link to update the model values
                   * @param {object} data New model data attributes
                   */
                  scope.component.api.updateModelValues = function (data) {
                    let model = Control.getAddressModel(scope.component.address);
                    if (model) {
                      _.merge(model, data);
                      updateModel();
                    }
                  };
                }

                // Unwatch initialization
                initWatch();
              }
            }
          }

          /**
           * Helper method to update autoNumeric with new value.
           * @param {type} newVal New value
           */
          function updateElement(newVal) {
            // Only set value if value is numeric
            if (initialized) {
              if (newVal === null) {
                $(elem).val("");
              } else if ((opts.lZero === undefined || opts.lZero !== 'keep') && $.isNumeric(newVal)) {
                elem.autoNumeric('set', parseFloat(newVal));
              } else if ($.isNumeric(newVal)) {
                elem.autoNumeric('set', newVal);
              }
            }
          }

          /**
           * Update model values
           */
          function updateModelValues() {
            if (initialized) {
              let model = Control.getAddressModel(scope.component.address);

              // Update the model values
              model.values[0] = {
                value: model.selected,
                label: model.selected === null ? "" : elem.val()
              };
            }
          }

          /**
           * Update view value
           */
          function updateModel() {
            if (initialized) {
              let model = Control.getAddressModel(scope.component.address);

              // Update the element
              updateElement(Array.isArray(model.selected) && model.selected.length > 0 ? model.selected[0] : model.selected);

              // Update the model values
              updateModelValues();
            }
          }

          /**
           * Event listeners
           */
          let listeners = {};

          // On number format change launch dependency
          listeners["updateNumberFormat"] = scope.$on("updateNumberFormat", function (event, parameters) {
            if (initialized) {
              initPlugin(parameters);
              updateModelValues();
              scope.$emit("visibleValue");
            }
          });

          /**
           * Destroy plugin
           */
          function destroy() {
            elem.off('change');
            initialized = false;

            // Clear listeners
            Utilities.clearListeners(listeners);
          }

          // Observe destroy event
          elem.on("$destroy", destroy);
        }
      };
    }
  ]);
