import { aweApplication } from "./../../awe";

// Dependency plugin
aweApplication.directive('uiDependency',
  ['DependencyController', 'AweUtilities',
    function (DependencyController, Utilities) {
      return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
          var initialized = false;
          // Watch for controller changes
          var initWatch = scope.$watch(attrs.uiDependency, initPlugin);
          /**
           * Plugin initialization
           * @param {object} component plugin parameters
           */
          function initPlugin(component) {
            if (!initialized && component && component.dependencies && component.dependencies.length > 0) {
              // Initialize dependencies
              DependencyController.register(component.dependencies, component);
              initialized = true;
              // Unwatch initialization
              initWatch();
            }
          }
        }
      };
    }
  ]);
