import { aweApplication } from "./../awe";

// Downloader directive
aweApplication.directive('downloader',
  ['AweUtilities', 'LoadingBar', 'AweSettings', 'Connection',
    function (Utilities, LoadingBar, $settings, $connection) {

      return {
        scope: {
          file: '=',
          onLoad: '&',
          onFail: '&'
        },
        link: function (scope, element) {
          // Start loading bar task
          LoadingBar.startTask();

          /**
           * On file downloaded
           * @returns {undefined}
           */
          var onSuccess = function () {
            // End loading bar task
            LoadingBar.endTask();

            if (scope.onLoad) {
              Utilities.timeout(function () {
                scope.onLoad(scope.file);
              });
            }
          };

          /**
           * On file download failed
           * @returns {undefined}
           */
          var onFail = function () {
            // End loading bar task
            LoadingBar.endTask();

            if (scope.onFail) {
              Utilities.timeout(function () {
                scope.onFail(scope.file);
              });
            }
          };

          // Download the file
          var data = scope.file.data || {};
          data[$settings.get("downloadIdentifier")] = scope.file.index;
          scope.file.data = data;
          var deferred = $.fileDownload(scope.file.url, {
            httpMethod: "POST",
            data: $connection.serializeParameters(data),
            container: element,
            abortCallback: onSuccess,
            failCallback: onFail
          });

          // On file downloaded, abort the promise
          scope.$on("/action/file-downloaded/" + scope.file.index, function (event, action) {
            deferred.abort();
            action.accept();
          });
        }
      };
    }
  ]);
