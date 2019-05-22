import { aweApplication } from "./../../awe";
import "../../services/dateTime";

// Column datepicker directive
aweApplication.directive('aweColumnDate',
  ['ServerData', 'Column', 'DateTime',
    function (serverData, Column, DateTime) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/date');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new DateTime(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asDate()) {
            // Update visible value on generation
            component.updateVisibleValue();
          }
        }
      };
    }
  ]);
