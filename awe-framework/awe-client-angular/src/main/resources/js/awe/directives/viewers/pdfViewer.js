import {aweApplication} from "../../awe";
import PDFObject from "pdfobject";

// PDF viewer
aweApplication.directive('awePdfViewer',
  ['ServerData', 'Connection', 'AweSettings', 'Component', '$window',
    function (ServerData, Connection, $settings, Component, $window) {

      return {
        restrict: 'E',
        replace: true,
        template: `<div class="pdf-viewer {{::controller.style}} expandible-vertical" ng-cloak></div>`,
        scope: {
          'widgetId': '@pdfViewerId'
        },
        /**
         * Link function
         * @param {Object} scope Directive scope
         * @param {object} $element Directive node
         */
        link: function (scope, $element) {
          // Init as component
          var component = new Component(scope, scope.widgetId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          /**
           * Reload data from element
           */
          component.reload = function () {

            // Get path of file
            let parameters = ServerData.getFormValues();
            let targetAction = scope.controller[$settings.get("targetActionKey")];
            // Add a random value for reload
            parameters["r"] = Math.random();

            Connection.getFile(ServerData.getFileUrl("stream/maintain/" + targetAction), parameters, "application/pdf", "blob")
                .then(response => {
                  let file = new Blob([response.data], {type: 'application/pdf'});
                  let url = $window.URL.createObjectURL(file);
                  PDFObject.embed(url, ".pdf-viewer");
                });
          };
        }
      };
    }
  ]);
