import { aweApplication } from "./../../awe";
import Slider from "bootstrap-slider";

// Slider plugin
aweApplication.directive('uiSlider',
  ['Control', 'AweSettings', 'AweUtilities',
    /**
     * Slider plugin
     *
     * @param {object} Control
     * @param {object} $settings
     * @param {object} Utilities
     */
    function (Control, $settings, Utilities) {
      // Init options
      let options = _.cloneDeep($settings.get("numericOptions"));

      return {
        // This directive only works when used in element's attribute (e.g: ui-slider)
        restrict: 'A',
        priority: 1,
        link: function (scope, elem, attrs) {
          // Read options
          let opts = options;
          let initialized = false;

          // Slider definition
          let mySlider;

          // Util functions to set options
          //-----------------------------------------------------------------------------------------------------
          /**
           * Set float value
           * @param {object} options Options object
           * @param {type} key
           * @param {type} value
           * @param {type} defaultValue
           * @returns {object} Options updates
           */
          function setFloatOption(options, key, value, defaultValue) {
            options[key] = value ? parseFloat(value) : defaultValue;
            return options;
          }
          /**
           * Set boolean value
           * @param {object} options Options object
           * @param {type} key
           * @param {type} value
           * @param {type} defaultValue
           * @returns {object} Options updates
           */
          function setBooleanOption(options, key, value, defaultValue) {
            options[key] = value ? String(value) === 'true' : defaultValue;
            return options;
          }
          //------------------------------------------------------------------------------------------------------

          // Watch for numeric options changes
          let initWatch = scope.$watch(attrs.uiSlider, initPlugin);

          /**
           * Plugin initialization
           *
           * @param {object}
           * newValues plugin parameters
           */
          function initPlugin(newValues) {
            if (newValues) {

              // Initialize slider with options
              opts = _.merge({}, options, newValues);

              // Check if numeric criteria has selected value
              if (scope.$parent.model) {
                let selectedValue = scope.$parent.model.selected;
                setFloatOption(opts, "value", selectedValue, 0);
              }

              // Check if numeric criteria is readonly
              if (scope.$parent.controller) {
                let readonly = scope.$parent.controller.readonly;
                if (readonly) {
                  setBooleanOption(opts, "enabled", false, false);
                }
              }

              if (initialized) {
                // Destroy previously initialized slider
                mySlider.destroy();
              }

              // Instantiate a slider
              mySlider = new Slider(elem[0], opts);
              initialized = true;

              // Build slider events
              buildSliderEvents();

              // Unwatch initialization
              initWatch();
            }
          }

          /**
           * Update slider value
           *
           * @param {string} value to update
           *
           */
          function updateValueSlider(value) {
            mySlider.setAttribute('value', parseFloat(value));
            mySlider.refresh();
            buildSliderEvents();
          }

          /**
           * Update slider options when dependency is launched
           *
           * @param {object} parameters The attribute changes
           *
           */
          function updateOptionsSlider(parameters) {
            // Check controller attributes to update slider
            if (parameters.controller) {
              // Check readonly
              if ("readonly" in parameters.controller) {
                let readonly = parameters.controller.readonly;
                if (readonly === true) {
                  mySlider.disable();
                  opts.enabled = false;
                  mySlider.setAttribute("enabled", false);
                } else {
                  mySlider.enable();
                  opts.enabled = true;
                  mySlider.setAttribute("enabled", true);
                }
              }
            }
          }

          /**
           * Build slider events
           */
          function buildSliderEvents() {
            // This event fires when the dragging stops or has been clicked on
            mySlider.on('slideStop', function () {
              // Change selected attribute
              let selected = {
                selected: mySlider.getValue()
              };
              Control.changeModelAttribute(scope.component.address, selected, true);
            });
          }

          /**
           * Update the model on model changed
           * @param {Object} changes
           */
          scope.onModelChanged = function (changes) {
            if (initialized) {
              updateValueSlider(changes.selected);
            }
          };

          /**
           * Event listeners
           */
          let listeners = {};

          // Action listener definition
          Utilities.defineModelChangeListeners(listeners, {scope: scope, check: ["selected"], service: scope, method: "onModelChanged"});

          // On number format change launch dependency
          listeners["updateNumberFormat"] = scope.$on("updateNumberFormat", function (event, parameters) {
            if (initialized) {
              initPlugin(parameters);
            }
          });

          /**
           * Resize the slider
           * @returns {undefined}
           */
          function resize() {
            if (initialized) {
              updateValueSlider(mySlider.getAttribute('value'));
            }
          }

          // On resize refresh
          listeners["resize"] = scope.$on("resize", resize);
          listeners["resize-action"] = scope.$on("resize-action", resize);

          // On model change launch dependency
          listeners["controllerChange"] = scope.$on("controllerChange", function (event, parameters) {
            if (initialized) {
              if (_.isEqual(parameters.address, scope.component.address)) {
                updateOptionsSlider(parameters);
              }
            }
          });

          /**
           * Destroy plugin
           */
          function destroy() {
            mySlider.destroy();
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
