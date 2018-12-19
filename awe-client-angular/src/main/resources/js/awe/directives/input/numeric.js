import { aweApplication } from "./../../awe";

// Numeric directive
aweApplication.directive('aweInputNumeric',
  ['ServerData', 'Numeric',
    function (serverData, Numeric) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/numeric');
        },
        scope: {
          'criterionId': '@inputNumericId'
        },
        link: function (scope, elem, attrs) {
          scope.initialized = new Numeric(scope, scope.criterionId, elem).asNumeric();
        }
      };
    }
  ]);
