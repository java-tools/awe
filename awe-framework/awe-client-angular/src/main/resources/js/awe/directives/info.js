import { aweApplication } from "./../awe";

// Info directive
aweApplication.directive('aweInfo',
  ['ServerData',
    /**
     * Info directive
     * @param {Service} serverData Server call service
     */
    function (serverData) {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('info');
        },
        scope: {
          'infoTitle': '@',
          'infoStyle': '@'
        }
      };
    }
  ]);
