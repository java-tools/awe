import { aweApplication } from "./../awe";
import download from "downloadjs";

// Downloader directive
aweApplication.directive('downloader',
  ['AweUtilities', 'LoadingBar', 'AweSettings', '$http', 'ActionController',
    function ($utilities, LoadingBar, $settings, $http, $actionController) {
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
          let onSuccess = function () {
            // End loading bar task
            LoadingBar.endTask();

            if (scope.onLoad) {
              $utilities.timeout(function () {
                scope.onLoad(scope.file);
              });
            }
          };

          /**
           * On file download failed
           * @returns {undefined}
           */
          let onFail = function () {
            // End loading bar task
            LoadingBar.endTask();

            if (scope.onFail) {
              $utilities.timeout(function () {
                scope.onFail(scope.file);
              });
            }
          };

          // Download the file
          let data = {
            ...scope.file.data,
            d: scope.file.index,
            token: $settings.getToken()
          };

          scope.file.data = data;

          $http({
            method: 'POST',
            url: scope.file.url,
            data: data,
            responseType: 'arraybuffer'
          }).success((data, status, headers)  => {
            download(new Blob([data], {type:headers("Content-Type")}), headers("Filename"), headers("Content-Type"));
            onSuccess();
          }).error(onFail);

          // On file downloaded, abort the promise
          scope.$on("/action/file-downloaded/" + scope.file.index, function (event, action) {
            $actionController.acceptAction(action);
            onSuccess();
          });
        }
      };
    }
  ]);
