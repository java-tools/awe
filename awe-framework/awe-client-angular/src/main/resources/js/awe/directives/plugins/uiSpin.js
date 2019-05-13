import { aweApplication } from "./../../awe";
import Spinner from "spin.js";

// Spinner plugin
aweApplication.directive('uiSpin',
  [function () {
      return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
          let initialized = false;

          // Watch for controller changes
          let initWatch = scope.$watch(attrs.uiSpin, initPlugin);

          /**
           * Plugin initialization
           * @param {object} newValues plugin parameters
           * @param {object} oldValues plugin last parameters
           */
          function initPlugin(newValues, oldValues) {
            if (newValues &&
              (!initialized || newValues !== oldValues)) {
              // Initialize element with options
              let spinner = new Spinner(newValues).spin();
              elem.append(spinner.el);
              initialized = true;

              // Unwatch initialization
              initWatch();
            }
          }

        }
      };
    }
  ]);
