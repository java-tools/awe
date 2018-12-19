import { aweApplication } from "./../awe";

// Window directive
aweApplication.directive('aweWindow',
  ['ServerData', 'Maximize', 'Component',
    function (ServerData, Maximize, Component) {
      return {
        restrict: 'E',
        transclude: true,
        replace: true,
        templateUrl: function () {
          return ServerData.getAngularTemplateUrl('window');
        },
        scope: {
          windowId: '@'
        },
        link: function (scope, elem) {
          // Init as component
          var component = new Component(scope, scope.windowId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          if (component.controller.maximize) {
            Maximize.initMaximize(component.scope, elem);
          }

          // Controller variables
          if (scope.controller) {
            component.scope.isExpandible = component.controller.style ? component.controller.style.indexOf("expand") !== -1 : false;
            component.scope.expandDirection = component.controller.expand || "vertical";
            component.scope.panelClass = component.controller.style ? component.controller.style : "";
            component.scope.panelTitle = component.controller.label ? component.controller.label : null;
            component.scope.panelIcon = component.controller.icon ? component.controller.icon : null;
          } else {
            component.scope.expandDirection = "vertical";
            component.scope.isExpandible = false;
            component.scope.panelClass = "";
            component.scope.panelTitle = null;
            component.scope.panelIcon = null;
          }
        }
      };
    }
  ]);
