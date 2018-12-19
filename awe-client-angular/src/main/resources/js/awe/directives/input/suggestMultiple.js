import { aweApplication } from "./../../awe";

// Suggest multiple directive
aweApplication.directive('aweInputSuggestMultiple',
  ['ServerData', 'Selector',
    function (serverData, Selector) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/select');
        },
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
