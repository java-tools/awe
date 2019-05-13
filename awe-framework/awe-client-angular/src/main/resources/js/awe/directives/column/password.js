import { aweApplication } from "./../../awe";
import "ng-caps-lock";

// Add requirements
aweApplication.requires.push("ngCapsLock");

// Column password directive
aweApplication.directive('aweColumnPassword',
  ['ServerData', 'Column', 'Criterion',
    function (serverData, Column, Criterion) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/password');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Criterion(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asCriterion()) {
            // Set visible value
            component.updateVisibleValue = function () {
              component.visibleValue = "******";
            };

            // Update visible value on generation
            component.updateVisibleValue();
          }
        }
      };
    }
  ]);
