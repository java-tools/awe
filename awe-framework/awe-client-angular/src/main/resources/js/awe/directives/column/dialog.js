import { aweApplication } from "./../../awe";
import "./../../services/dialog";

// Column dialog directive
aweApplication.directive('aweColumnDialog',
  ['ServerData', 'Column', 'Dialog',
    function (serverData, Column, Dialog) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/dialog');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Dialog(scope, column.id, elem);

          // Initialize criterion and column
          column.init(component).asDialog();
        }
      };
    }
  ]);
