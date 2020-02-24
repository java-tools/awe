import { aweApplication } from "./../../awe";
import "bootstrap-datepicker";

// Date plugin
aweApplication.directive('uiDate',
  ['AweUtilities',
    /**
     * Bootstrap datepicker wrapper
     * @param {object} Utilities
     */
    function (Utilities) {
      return {
        // This directive only works when used in element's attribute (e.g: ui-date)
        restrict: 'A',
        priority: 1,
        compile: function () {
          return function (scope, elem, attrs) {
            let initialized = false;
            let options = null;

            // Watch for controller changes
            let initWatch = scope.$watch(attrs.initialized, initPlugin);

            /**
             * Plugin initialization
             * @param {object} startPlugin flag that indicates whether to start plugin or not
             */
            function initPlugin(startPlugin) {
              let controller = options || scope[attrs.uiDate];
              if (!initialized && controller && startPlugin) {
                // Initialize element as date with options
                elem.datepicker(controller);
                options = controller;
                initialized = true;

                // Unwatch initialization
                initWatch();

                // Init events
                initEvents();

                // Add methods to component
                addMethodsToComponent();
              }
            }

            /**
             * Add methods to component
             */
            function addMethodsToComponent() {
              let component = scope.component;

              /**
               * Update selected value
               */
              component.updateModelSelected = function () {
                let selected = component.model.selected;
                if (elem.val() !== selected) {
                  elem.datepicker('setDate', selected);
                  if (selected !== elem.children().val()) {
                    component.model.selected = elem.children().val();
                  }
                }
              };

              /**
               * Update value list
               */
              component.updateModelValues = function () {
                elem.datepicker('update');
              };

              // Update initialization flag
              component.pluginInitialized = true;
            }

            /**
             * Event listeners
             */
            let listeners;

            /**
             * Event initialization
             */
            function initEvents() {
              let component = scope.component;
              listeners = {};

              // Watch for language change
              listeners["languageChanged"] = scope.$on('languageChanged', updatePlugin);

              // Update model on change
              elem.datepicker().on("changeDate", () => {
                component.model.selected = elem.children().val()
              });
            }

            /**
             * Destroy plugin
             */
            function destroy() {
              if (initialized) {
                elem.datepicker('destroy');
                initialized = false;

                // Clear listeners
                Utilities.clearListeners(listeners);
              }
            }

            /**
             * Update plugin
             */
            function updatePlugin(event, language) {
              // Update element as date with options
              destroy();
              options.language = language;
              initPlugin(options);
            }

            // Observe destroy event
            elem.on("$destroy", destroy);
          };
        }
      };
    }
  ]);
