import { aweApplication } from "./../../awe";

// Select directive
aweApplication.directive('aweInputSelect',
  ['ServerData', 'Selector',
    function (serverData, Selector) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/select');
        },
        scope: {
          'criterionId': '@inputSelectId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          scope.initialized = new Selector(scope, scope.criterionId, elem).asSelect();
        }
      };
    }
  ]);
