import { aweApplication } from "./../../awe";
import "./../../services/text";

// Column textarea directive
aweApplication.directive('aweColumnTextarea',
  ['ServerData', 'Column', 'Criterion',
    function (serverData, Column, Criterion) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/textarea');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Criterion(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asCriterion()) {
            // Update visible value on generation
            component.updateVisibleValue();
          }
        }
      };
    }
  ]);
