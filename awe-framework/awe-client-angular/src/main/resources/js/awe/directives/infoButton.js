import { aweApplication } from "./../awe";
import "../services/button";

// Info button directive
aweApplication.directive('aweInfoButton',
  ['ServerData', 'Button',
    /**
     * Info directive
     * @param {Service} ServerData Server call service
     * @param {Service} Button
     */
    function (ServerData, Button) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return ServerData.getAngularTemplateUrl('infoButton');
        },
        scope: {
          'infoId': '@infoButtonId'
        },
        link: function (scope, element) {
          // Initialize button
          scope.initialized = new Button(scope, scope.infoId, element).asButton();
        }
      };
    }]);
