import { aweApplication } from "./../../awe";
import { templateSelector } from "../../services/selector";

// Select directive
aweApplication.directive('aweInputSelect',
  ['ServerData', 'Selector',
    function (serverData, Selector) {
      return {
        restrict: 'E',
        replace: true,
        template: templateSelector,
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
