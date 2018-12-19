import { aweApplication } from "./../awe";

// Manage the downloads
aweApplication.controller("DownloadController",
  ['$scope', 'AweUtilities', 'ActionController',
    /**
     * Control downloads data
     * @param {type} $scope
     * @param {type} Utilities
     * @param {type} ActionController
     */
    function ($scope, Utilities, ActionController) {
      // Define scope alerts
      $scope.downloads = [];
      /**
       * Remove the download from list
       * @param {type} file Download file
       */
      $scope.finishDownload = function (file) {
        Utilities.timeout(function () {
          $scope.downloads.splice(file.index, 1);
          // If action exists, accept it
          if (file.action) {
            file.action.accept();
          }
        });
      };
      /**
       * Remove the download from list
       * @param {type} file Download file
       */
      $scope.failDownload = function (file) {
        // Send error message
        ActionController.sendMessage($scope, 'error', 'ERROR_TITLE_FILE_ACCESS', 'ERROR_MESSAGE_FILE_NOT_DEFINED');
        // Finish the action
        $scope.finishDownload(file);
      };
      // Launch message action
      $scope.$on('download-file', function (event, file) {
        file.index = $scope.downloads.length;
        $scope.downloads.push(file);
      });
    }
  ]);
