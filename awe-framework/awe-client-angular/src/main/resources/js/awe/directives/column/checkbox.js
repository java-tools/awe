import { aweApplication } from "./../../awe";

// Column checkbox directive
aweApplication.directive('aweColumnCheckbox',
  ['ServerData', 'Column', 'CheckboxRadio',
    function (serverData, Column, CheckboxRadio) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/checkbox');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new CheckboxRadio(scope, column.id, elem);

          // Initialize criterion and column
          column.init(component).asCheckbox();
        }
      };
    }
  ]);
