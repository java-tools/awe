import { aweApplication } from "./../awe";

// Video directive
aweApplication.directive('aweVideo',
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
          return serverData.getAngularTemplateUrl('video');
        },
        scope: {
          'videoId': '@'
        },
        link: function (scope) {
          // Init as component
          var component = new Component(scope, scope.videoId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }
        }
      };
    }
  ]);
