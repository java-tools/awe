import { aweApplication } from "./../../awe";

// Help viewer
aweApplication.directive('aweHelpViewer',
  ['ServerData', 'Connection', 'AweSettings', 'Component', '$compile', 'Storage', '$templateCache',
    function (serverData, Connection, $settings, Component, $compile, Storage, $templateCache) {

      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('helpViewer');
        },
        scope: {
          'widgetId': '@helpViewerId'
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

          // Generate function to load the file
          var updateContents = function(data) {
            // assign it into the current DOM
            elem.html(data);

            // compile the new DOM and link it to the current scope.
            $compile(elem.contents())(scope);
          };

          let serverAction = component.controller[$settings.get("serverActionKey")];
          let screen = Storage.get("screen")[component.address.view];
          let option = serverAction === "application-help" ? null : screen.option || screen.name;
          let helpUrl = serverData.getHelpUrl(option);
          let helpData = $templateCache.get(helpUrl);
          if (helpData) {
            updateContents(helpData);
          } else {
            Connection.get(serverData.getHelpUrl(option), "text/html").then(function (response) {
              if (response.data && response.status === 200) {
                updateContents(response.data);
                $templateCache.put(helpUrl, response.data);
              }
            });
          }
        }
      };
    }
  ]);
