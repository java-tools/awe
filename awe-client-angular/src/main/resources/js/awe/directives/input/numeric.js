import { aweApplication } from "./../../awe";
import { templateNumeric } from "../../services/numeric";

// Numeric directive
aweApplication.directive('aweInputNumeric',
  ['ServerData', 'Numeric',
    function (serverData, Numeric) {
      return {
        restrict: 'E',
        replace: true,
        template: templateNumeric,
        scope: {
          'criterionId': '@inputNumericId'
        },
        link: function (scope, elem, attrs) {
          scope.initialized = new Numeric(scope, scope.criterionId, elem).asNumeric();
        }
      };
    }
  ]);
