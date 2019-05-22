import { aweApplication } from "./../../awe";
import "../../services/checkboxRadio";

// Button checkbox directive
aweApplication.directive('aweInputButtonCheckbox',
  ['ServerData', 'CheckboxRadio',
    function (serverData, CheckboxRadio) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/button-checkbox');
        },
        scope: {
          'criterionId': '@inputButtonCheckboxId'
        },
        link: function (scope, elem, attrs) {
          // Initialize checkbox
          var component = new CheckboxRadio(scope, scope.criterionId, elem);
          component.specialClass = "btn-" + scope.size;
          scope.initialized = component.asCheckbox();
        }
      };
    }
  ]);

