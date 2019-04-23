import { aweApplication } from "./../../awe";

// File viewer
aweApplication.directive('aweFileViewer',
  ['ServerData', 'AweUtilities', 'AweSettings', 'Component', 'Connection',
    function (serverData, Utilities, $settings, Component, Connection) {

      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('fileViewer');
        },
        scope: {
          'widgetId': '@fileViewerId'
        },
        /**
         * Link function
         * @param {Object} scope Directive scope
         * @param {Object} elem Directive node
         */
        link: function (scope, elem) {
          // Init as component
          var component = new Component(scope, scope.widgetId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          // Retrieve widget values
          var stickBottom = false;
          var initial = true;
          if (component.controller.parameters) {
            stickBottom = "stickBottom" in component.controller.parameters ? component.controller.parameters["stickBottom"] : stickBottom;
          }

          /**
           * Reload data from element
           */
          component.reload = function () {
            var parameters = serverData.getFormValues();
            var action = component.controller[$settings.get("serverActionKey")];
            var target = component.controller[$settings.get("targetActionKey")];
            var url = serverData.getGenericFileUrl(action, target);
            Connection.post(url, parameters).then(function (response) {
              if (response.data && response.status === 201) {
                var moveScroll = elem.scrollTop() + elem.height() >= elem[0].scrollHeight;
                scope.content = response.data;
                if (stickBottom) {
                  if (moveScroll || initial) {
                    initial = false;
                    Utilities.timeout(function () {
                      elem.animate({scrollTop: elem[0].scrollHeight});
                    });
                  }
                }
              }
            });

            // Check autorefresh
            component.checkAutoRefresh();
          };
        }
      };
    }]);
