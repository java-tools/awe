import { aweApplication } from "./../../awe";

// Add requirements
aweApplication.requires.push("ngCapsLock");

// Password directive
aweApplication.directive('aweInputPassword',
  ['ServerData', 'Criterion',
    function ($serverData, Criterion) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: () => $serverData.getAngularTemplateUrl('input/password'),
        scope: {
          'criterionId': '@inputPasswordId'
        },
        link: function (scope, elem) {
          // Initialize criterion
          scope.initialized = new Criterion(scope, scope.criterionId, elem).asCriterion();
        }
      };
    }
  ]);
