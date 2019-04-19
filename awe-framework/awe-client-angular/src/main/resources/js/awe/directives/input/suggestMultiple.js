import { aweApplication } from "./../../awe";
import { templateSelector } from "../../services/selector";

// Suggest multiple directive
aweApplication.directive('aweInputSuggestMultiple',
  ['ServerData', 'Selector',
    function (serverData, Selector) {
      return {
        restrict: 'E',
        replace: true,
        template: templateSelector,
        scope: {
          'criterionId': '@inputSuggestMultipleId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          new Selector(scope, scope.criterionId, elem).asSuggestMultiple(true);
        }
      };
    }
  ]);
