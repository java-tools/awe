import { aweApplication } from "./../../awe";

// Column numeric directive
aweApplication.directive('aweColumnNumeric',
  ['ServerData', 'Column', 'Numeric',
    function (serverData, Column, Numeric) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/numeric');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Numeric(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asNumeric()) {
            // Update visible value on generation
            component.updateVisibleValue();
          }
        }
      };
    }
  ]);
