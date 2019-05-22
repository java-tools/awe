import { aweApplication } from "./../../awe";
import "../../services/button";

// Column button directive
aweApplication.directive('aweColumnButton',
  ['ServerData', 'Column', 'Button', 'AweUtilities', 'AweSettings', 'ActionController',
    function (serverData, Column, Button, Utilities, $settings, ActionController) {

      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/button');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Button(scope, column.id, elem);

          // Initialize criterion and column
          column.init(component).asButton();

          /**
           * Click button function
           * @param {Object} event Event
           */
          component.onClick = function (event) {
            // Cancel event propagation
            Utilities.stopPropagation(event);

            // Load dialog model (if not loaded yet)
            var values = {};
            values.type = scope.controller[$settings.get("serverActionKey")];
            values[$settings.get("targetActionKey")] = scope.controller[$settings.get("targetActionKey")];

            // Launch action list
            ActionController.addActionList([serverData.getServerAction(component.address, values, false, false)], true, scope);
            scope.storeEvent('click');
          };
        }
      };
    }
  ]);