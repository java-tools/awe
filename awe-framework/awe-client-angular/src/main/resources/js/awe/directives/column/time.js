import { aweApplication } from "./../../awe";

// Column timepicker directive
aweApplication.directive('aweColumnTime',
  ['ServerData', 'Column', 'DateTime',
    function (serverData, Column, DateTime) {

      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/time');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new DateTime(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asTime()) {
            // Update visible value on generation
            component.updateVisibleValue();
          }
        }
      };
    }
  ]);
