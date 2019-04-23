import { aweApplication } from "./../awe";
import { DefaultSpin } from "./../data/options";

// Chart directive
aweApplication.directive('aweChart',
  ['ServerData', 'Chart',
    function (serverData, Chart) {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('chart');
        },
        scope: {
          'chartId': '@'
        },
        link: function (scope, element) {
          // Set spin options
          scope.spinOptions = DefaultSpin.big;

          // Init as component
          var component = new Chart(scope, scope.chartId, element);
          if (component.asChart()) {
            // Get chart controller
            scope.chartOptions = component.controller.chartModel;
          }
        }
      };
    }]);
