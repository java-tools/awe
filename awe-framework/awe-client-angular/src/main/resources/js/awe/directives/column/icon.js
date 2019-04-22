import { aweApplication } from "./../../awe";

// Column icon directive
aweApplication.directive('aweColumnIcon',
  ['ServerData', 'Column', 'Component',
    function (serverData, Column, Component) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/icon');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Component(scope, column.id);

          // Initialize criterion and column
          if (column.init(component).asComponent()) {
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
