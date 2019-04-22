import { aweApplication } from "./../../awe";

// Column sparkline directive
aweApplication.directive('aweColumnSparkLine',
  ['ServerData', 'Column', 'SparkLine',
    function (serverData, Column, SparkLine) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/sparkline');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new SparkLine(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asSparkLine()) {
            // Initialize sparkline
          }
        }
      };
    }
  ]);
