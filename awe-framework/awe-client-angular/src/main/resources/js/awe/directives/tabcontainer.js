import { aweApplication } from "./../awe";

// Tab container directive
aweApplication.directive('aweTabcontainer',
  ['ServerData', 'Panel',
    function (serverData, Panel) {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        require: '^aweInputTab',
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('tabcontainer');
        },
        scope: {
          tabcontainerId: '@'
        },
        link: function (scope, elem, attrs, controller) {
          Panel.init(controller, scope, scope.tabcontainerId);
        }
      };
    }
  ]);
