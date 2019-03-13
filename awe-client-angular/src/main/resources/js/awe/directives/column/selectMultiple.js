import { aweApplication } from "./../../awe";
import { templateSelectorColumn } from "../../services/selector";

// Column select multiple directive
aweApplication.directive('aweColumnSelectMultiple',
  ['ServerData', 'Column', 'Selector',
    function (serverData, Column, Selector) {
      return {
        restrict: 'E',
        replace: true,
        template: templateSelectorColumn,
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Selector(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asSelectMultiple()) {
            // Update visible value on generation
            component.updateVisibleValue();
          }
        }
      };
    }
  ]);
