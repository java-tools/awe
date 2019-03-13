import { aweApplication } from "./../../awe";
import { templateSelector } from "../../services/selector";

// Select multiple directive
aweApplication.directive('aweInputSelectMultiple',
  ['ServerData', 'Selector',
    function (serverData, Selector) {
      return {
        restrict: 'E',
        replace: true,
        template: templateSelector,
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

