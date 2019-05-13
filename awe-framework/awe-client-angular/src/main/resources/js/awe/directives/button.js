import { aweApplication } from "./../awe";
import "../services/button";

// Button directive
aweApplication.directive('aweButton',
  ['ServerData', 'Button',
    function ($serverData, Button) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: () => $serverData.getAngularTemplateUrl('button'),
        scope: {
          'buttonId': '@'
        },
        link: function (scope, element) {
          // Initialize button
          scope.initialized = new Button(scope, scope.buttonId, element).asButton();
        }
      };
    }]);
