// Tabcontainer directive
aweApplication.directive('aweFileManager',
  ['ServerData', 'Component', 'Connection',
    function (serverData, Component, Connection) {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('fileManager');
        },
        scope: {
          widgetId: '@fileManagerId'
        },
        compile: function () {
          return {
            /**
             * Pregeneration function
             * @param {Object} scope
             */
            pre: function (scope) {
              // Init as component
              var component = new Component(scope, scope.widgetId);
              if (!component.asComponent()) {
                // If component initialization is wrong, cancel initialization
                return false;
              }

              // Define template url
              scope.fileManagerUrl = Connection.getRawUrl() + "/fm/home";
            }
          };
        }
      };
    }
  ]);
