import { aweApplication } from "./../awe";

// Chart directive
aweApplication.directive('aweChart',
  ['ServerData', 'Chart', 'Options',
    function (serverData, Chart, Options) {
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
          scope.spinOptions = Options.spin.big;

          // Init as component
          var component = new Chart(scope, scope.chartId, element);
          if (component.asChart()) {
            // Get chart controller
            scope.chartOptions = component.controller.chartModel;
          }
        }
      };
    }]);
