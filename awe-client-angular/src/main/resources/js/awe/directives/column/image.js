import { aweApplication } from "./../../awe";

// Column image directive
aweApplication.directive('aweColumnImage',
  ['ServerData', 'Column', 'Component',
    function (serverData, Column, Component) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/image');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Component(scope, column.id);
          component.address = column.address;

          // Initialize criterion and column
          column.init(component).asComponent();
        }
      };
    }
  ]);
