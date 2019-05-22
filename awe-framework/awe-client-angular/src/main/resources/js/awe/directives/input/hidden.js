import { aweApplication } from "./../../awe";
import "./../../services/text";

// Hidden directive
aweApplication.directive('aweInputHidden',
  ['Criterion', 'ServerData', 'AweUtilities',
    function (Criterion, ServerData, Utilities) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return ServerData.getAngularTemplateUrl('input/hidden');
        },
        scope: {
          'criterionId': '@inputHiddenId'
        },
        link: function (scope, elem) {

          // Initialize criterion
          var component = new Criterion(scope, scope.criterionId, elem);
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          /**
           * Reset criterion (disabled)
           */
          component.onReset = Utilities.noop;
        }
      };
    }
  ]);
