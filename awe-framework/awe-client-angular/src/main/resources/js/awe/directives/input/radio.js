import { aweApplication } from "./../../awe";
import "../../services/checkboxRadio";

// Radio directive
aweApplication.directive('aweInputRadio',
  ['ServerData', 'CheckboxRadio',
    function (serverData, CheckboxRadio) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/radio');
        },
        scope: {
          'criterionId': '@inputRadioId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          scope.initialized = new CheckboxRadio(scope, scope.criterionId, elem).asRadio();
        }
      };
    }
  ]);

