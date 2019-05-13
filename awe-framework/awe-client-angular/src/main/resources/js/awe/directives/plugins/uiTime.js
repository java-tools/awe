import { aweApplication } from "./../../awe";
import "bootstrap-timepicker";

// Time plugin
aweApplication.directive('uiTime',
  ['AweUtilities',
    /**
     * @param {service} Utilities
     */
    function (Utilities) {
      //Init options
      let options = {};

      return {
        // This directive only works when used in element's attribute (e.g: ui-time)
        restrict: 'A',
        priority: 1,
        compile: function () {
          return function (scope, elem, attrs) {
            let initialized = false;

            // Watch for controller changes
            let initWatch = scope.$watch(attrs.initialized, initPlugin);

            /**
             * Plugin initialization
             * @param {object} startPlugin flag that indicates whether to start plugin or not
             */
            function initPlugin(startPlugin) {
              let controller = scope[attrs.uiTime];
              if (!initialized && controller && startPlugin) {
                // Initialize element as time with options
                let opts = _.merge({}, options, controller);
                elem.timepicker(opts);
                initialized = true;

                // Add methods to component
                elem.timepicker().on('keydown.timepicker', hideTimepicker);

                // Unwatch initialization
                initWatch();
              }
            }

            /**
             * Add methods to component
             */
            function hideTimepicker(e) {
              if (e.which === 9) {
                elem.timepicker('hideWidget');
              }
            }
          };
        }
      };
    }]);
