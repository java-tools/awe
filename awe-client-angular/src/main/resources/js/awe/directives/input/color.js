import { aweApplication } from "./../../awe";

// Add requirements
aweApplication.requires.push("colorpicker.module");

// Colorpicker directive
aweApplication.directive('aweInputColor',
  ['ServerData', 'Criterion',
    function (serverData, Criterion) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/color');
        },
        scope: {
          'criterionId': '@inputColorId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          scope.initialized = new Criterion(scope, scope.criterionId, elem).asCriterion();
        }
      };
    }
  ]);
