import { aweApplication } from "./../awe";
import "jquery-file-download";

// Downloader directive
aweApplication.directive('downloader',
  ['AweUtilities', 'LoadingBar', 'AweSettings',
    function (Utilities, LoadingBar, $settings) {
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
          let data = {
            ...scope.file.data,
            d: scope.file.index,
            token: $settings.getToken(),
          };

          scope.file.data = data;
          var deferred = $.fileDownload(scope.file.url, {
            httpMethod: "POST",
            data: data,
            container: element,
            abortCallback: onSuccess,
            failCallback: onFail
          });

          // On file downloaded, abort the promise
          scope.$on("/action/file-downloaded/" + scope.file.index, function (event, action) {
            action.accept();
            onSuccess();
          });
        }
      };
    }
  ]);
