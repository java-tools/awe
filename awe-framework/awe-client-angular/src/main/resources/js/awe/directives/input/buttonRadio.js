import { aweApplication } from "./../../awe";

// Button radio directive
aweApplication.directive('aweInputButtonRadio',
  ['ServerData', 'CheckboxRadio',
    function (serverData, CheckboxRadio) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/button-radio');
        },
        scope: {
          'criterionId': '@inputButtonRadioId'
        },
        link: function (scope, elem, attrs) {
          // Initialize checkbox
          var component = new CheckboxRadio(scope, scope.criterionId, elem);
          component.specialClass = "btn-" + scope.size;
          scope.initialized = component.asRadio();
        }
      };
    }
  ]);

