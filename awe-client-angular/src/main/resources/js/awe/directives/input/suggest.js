import { aweApplication } from "./../../awe";

// Suggest directive
aweApplication.directive('aweInputSuggest',
  ['ServerData', 'Selector',
    function (serverData, Selector) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/select');
        },
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
