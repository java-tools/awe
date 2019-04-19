import { aweApplication } from "./../../awe";
import { templateSelector } from "../../services/selector";

// Suggest directive
aweApplication.directive('aweInputSuggest',
  ['ServerData', 'Selector',
    function (serverData, Selector) {
      return {
        restrict: 'E',
        replace: true,
        template: templateSelector,
        scope: {
          'criterionId': '@inputSuggestId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          new Selector(scope, scope.criterionId, elem).asSuggest(true);
        }
      };
    }
  ]);
