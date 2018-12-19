import { aweApplication } from "./../../awe";

// Text directive
aweApplication.directive('aweInputText',
  ['ServerData', 'Criterion',
    function ($serverData, Criterion) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: () => $serverData.getAngularTemplateUrl('input/text'),
        scope: {
          'criterionId': '@inputTextId'
        },
        link: function (scope, elem) {
          // Initialize criterion
          scope.initialized = new Criterion(scope, scope.criterionId, elem).asCriterion();
        }
      };
    }
  ]);
