import { aweApplication } from "./../../awe";
import "./../../services/text";

// Textarea directive
aweApplication.directive('aweInputTextarea',
  ['ServerData', 'Criterion', '$log',
    function (serverData, Criterion, $log) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/textarea');
        },
        scope: {
          'criterionId': '@inputTextareaId'
        },
        link: function (scope, elem) {
          // Initialize criterion
          scope.initialized = new Criterion(scope, scope.criterionId, elem).asCriterion();
        }
      };
    }
  ]);