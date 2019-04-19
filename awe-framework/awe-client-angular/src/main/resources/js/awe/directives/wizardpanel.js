import { aweApplication } from "./../awe";

// Wizard panel directive
aweApplication.directive('aweWizardPanel',
  ['ServerData', 'Panel',
    function (serverData, Panel) {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        require: '^aweInputWizard',
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('wizardPanel');
        },
        scope: {
          wizardPanelId: '@'
        },
        link: function (scope, elem, attrs, controller) {
          Panel.init(controller, scope, scope.wizardPanelId);
        }
      };
    }
  ]);
