import { aweApplication } from "./../../awe";

// Tabdrop plugin
aweApplication.directive('uiTabdrop',
  ['AweUtilities',
    /**
     * UI Tab drop
     * @param {type} Utilities
     */
    function (Utilities) {
      return {
        // This directive only works when used in element's attribute (e.g: ui-time)
        restrict: 'A',
        priority: 1,
        compile: function () {
          return function (scope, elem) {
            var initPlugin = function () {
              elem.tabdrop();
              Utilities.publishDelayed("resize-action", {});
            };
            //scope.$on('resize', initPlugin);
            Utilities.timeout(initPlugin);
          };
        }
      };
    }
  ]);