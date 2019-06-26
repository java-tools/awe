import { aweApplication } from "./../awe";

// Manage the downloads
aweApplication.controller("DownloadController",
  ['$scope', 'AweUtilities', 'ActionController',
    /**
     * Control downloads data
     * @param {type} $scope
     * @param {type} $utilities
     * @param {type} $actionController
     */
    function ($scope, $utilities, $actionController) {
      // Define controller
      let $ctrl = this;

      // Define scope alerts
      $ctrl.downloads = [];

      /**
       * Start downloading a file
       * @param {object} file Download file
       */
      $ctrl.startDownload = function(file) {
        file.index = $ctrl.downloads.length;
        $ctrl.downloads.push(file);
      };

      /**
       * Remove the download from list
       * @param {type} file Download file
       */
      $ctrl.finishDownload = function (file) {
        $utilities.timeout(function () {
          $ctrl.downloads.splice(file.index, 1);
          // If action exists, accept it
          if (file.action) {
            $actionController.acceptAction(file.action);
          }
        });
      };

      /**
       * Remove the download from list
       * @param {type} file Download file
       */
      $ctrl.failDownload = function (file) {
        // Send error message
        $actionController.sendMessage($scope, 'error', 'ERROR_TITLE_FILE_ACCESS', 'ERROR_MESSAGE_FILE_NOT_DEFINED');

        // Finish the action
        $ctrl.finishDownload(file);
      };

      // On event start downloading file
      $scope.$on('download-file', (event, file) => $ctrl.startDownload(file));
    }
  ]);
