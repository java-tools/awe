import { aweApplication } from "./../../awe";

// PDF viewer
aweApplication.directive('awePdfViewer',
  ['ServerData', 'Connection', 'AweSettings', 'Component',
    function (ServerData, Connection, $settings, Component) {

      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return ServerData.getAngularTemplateUrl('pdfViewer');
        },
        scope: {
          'widgetId': '@pdfViewerId'
        },
        /**
         * Link function
         * @param {Object} scope Directive scope
         */
        link: function (scope) {
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
            var parameters = ServerData.getFormValues();
            var targetAction = scope.controller[$settings.get("targetActionKey")];
            // Add a random value for reload
            parameters["r"] = Math.random();

            // Get parameters encoded
            var encodedParameters = ServerData.getEncodedParameters(parameters);
            var encodedParametersList = [];
            _.each(encodedParameters, function(value, key) {
              encodedParametersList.push(key + "=" + encodeURI(value));
            });

            // Generate url
            var fileData = ServerData.getFileUrl("stream/maintain/" + targetAction + "?" + encodedParametersList.join("&"));

            // Change url in iframe
            if (fileData !== null) {
              scope.urlPdf = fileData;
            }
          };
        }
      };
    }
  ]);
