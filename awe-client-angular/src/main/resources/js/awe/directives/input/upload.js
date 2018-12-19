import { aweApplication } from "./../../awe";

// Upload directive
aweApplication.directive('aweInputUploader',
  ['ServerData', 'Uploader',
    function (serverData, Uploader) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/upload');
        },
        scope: {
          'criterionId': '@inputUploaderId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          scope.initialized = new Uploader(scope, scope.criterionId, elem).asUploader();
        }
      };
    }
  ]);
