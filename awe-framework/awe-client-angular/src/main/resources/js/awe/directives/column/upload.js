import { aweApplication } from "./../../awe";
import "../../services/uploader";

// Column uploader directive
aweApplication.directive('aweColumnUploader',
  ['ServerData', 'Column', 'Uploader',
    function (serverData, Column, Uploader) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('column/upload');
        },
        link: function (scope, elem, attrs) {
          // Create column, criterion and component
          var column = new Column(attrs);
          var component = new Uploader(scope, column.id, elem);

          // Initialize criterion and column
          if (column.init(component).asUploader()) {
            // Update visible value on generation
            component.updateVisibleValue();
          }
        }
      };
    }
  ]);
