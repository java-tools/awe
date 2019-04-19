import { aweApplication } from "./../../awe";

// Checkbox directive
aweApplication.directive('aweInputCheckbox',
  ['ServerData', 'CheckboxRadio',
    function (serverData, CheckboxRadio) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/checkbox');
        },
        scope: {
          'criterionId': '@inputCheckboxId'
        },
        link: function (scope, elem, attrs) {
          // Initialize checkbox
          scope.initialized = new CheckboxRadio(scope, scope.criterionId, elem).asCheckbox();
        }
      };
    }
  ]);
