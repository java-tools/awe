import { aweApplication } from "./../../awe";

// Time directive
aweApplication.directive('aweInputTime',
  ['ServerData', 'DateTime',
    function (serverData, DateTime) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/time');
        },
        scope: {
          'criterionId': '@inputTimeId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          scope.initialized = new DateTime(scope, scope.criterionId, elem).asTime();
        }
      };
    }
  ]);