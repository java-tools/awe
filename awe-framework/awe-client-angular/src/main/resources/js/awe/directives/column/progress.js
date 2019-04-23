import { aweApplication } from "./../../awe";

// Column progress directive
aweApplication.directive('aweColumnProgress',
  ['ServerData', 'Column', 'Component',
    function (serverData, Column, Component) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/progress');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Component(scope, column.id);

          // Initialize criterion and column
          if (column.init(component).asComponent()) {

            // Set reload async and silent
            component.reloadAsync = true;
            component.reloadSilent = true;

            /**
             * Retrieves visible value for the icon
             * @returns {string} visible value
             */
            component.getVisibleValue = function () {
              var model = scope.model.values;
              if (model && model.length > 0) {
                return model[0].value;
              }
            };
          }
        }
      };
    }
  ]);
