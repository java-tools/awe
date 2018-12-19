import { aweApplication } from "./../awe";

// Info dropdown directive
aweApplication.directive('aweInfoDropdown', ['ServerData', 'Component', 'ActionController',
  /**
   * Info directive
   * @param {Service} ServerData Server call service
   * @param {Service} Component
   * @param {Service} ActionController
   */
  function (ServerData, Component, ActionController) {
    return {
      restrict: 'E',
      replace: true,
      transclude: true,
      templateUrl: function () {
        return ServerData.getAngularTemplateUrl('infoDropdown');
      },
      scope: {
        'infoId': '@infoDropdownId'
      },
      link: function (scope) {
        // Init as component
        var component = new Component(scope, scope.infoId);
        if (!component.asComponent()) {
          // If component initialization is wrong, cancel initialization
          return false;
        }
        // Define extra controls
        var controller = {
          hasChildren: false
        };
        // Define controller
        if (component.controller) {
          _.merge(component.controller, controller);
        } else {
          component.controller = controller;
        }

        // Has children
        component.controller.hasChildren = component.controller.children > 0;

        /**
         * Click button function
         */
        component.scope.infoClick = function () {
          if (component.controller && component.controller.actions && component.controller.actions.length > 0) {
            ActionController.addActionList(component.controller.actions, true, {address: component.address, context: component.context});
          }
        };
      }
    };
  }]);
