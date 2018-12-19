import { aweApplication } from "./../awe";

// Context menu directive
aweApplication.directive('aweContextMenu',
  ['ServerData', 'ContextMenu',
    function (serverData, ContextMenu) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('contextMenu');
        },
        link: function (scope, elem) {
          // Watch for context menu
          let removeComponentWatch = scope.$watch("contextMenuData", initContextMenu);

          /**
           * Initialize context menu
           * @param contextMenuList Context menu data
           */
          function initContextMenu(contextMenuList) {
            if (contextMenuList && contextMenuList.length > 0) {
              scope.contextMenu = new ContextMenu(scope, elem, contextMenuList);
              scope.contextMenu.init();
            }
            removeComponentWatch();
          }
        }
      };
    }
  ]);
