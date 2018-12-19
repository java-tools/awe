import { aweApplication } from "./../../awe";

// Column textView directive
aweApplication.directive('aweColumnTextView',
  ['ServerData', 'Column', 'Text',
    function (serverData, Column, Text) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/textView');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Text(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asText()) {
            // Update visible value on generation
            component.updateVisibleValue();
          }
        }
      };
    }
  ]);
