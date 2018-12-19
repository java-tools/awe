import { aweApplication } from "./../../awe";

// Select multiple directive
aweApplication.directive('aweInputSelectMultiple',
  ['ServerData', 'Selector',
    function (serverData, Selector) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/select');
        },
        scope: {
          'criterionId': '@inputSelectMultipleId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          scope.initialized = new Selector(scope, scope.criterionId, elem).asSelectMultiple();
        }
      };
    }
  ]);

