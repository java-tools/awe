import { aweApplication } from "./../../awe";

// Text view directive
aweApplication.directive('aweInputTextView',
  ['ServerData', 'Text',
    function (serverData, Text) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/textView');
        },
        scope: {
          'criterionId': '@inputTextViewId'
        },
        link: function (scope, elem) {
          // Initialize criterion
          scope.initialized = new Text(scope, scope.criterionId, elem).asText();
        }
      };
    }
  ]);
