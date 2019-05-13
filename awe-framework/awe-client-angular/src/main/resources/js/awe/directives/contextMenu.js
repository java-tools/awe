import { aweApplication } from "./../awe";
import "../services/contextMenu";

const template =
`<ul ng-if="::contextMenu" ng-show="contextMenu.isVisible()" class="context-menu dropdown-menu ng-hide" role="menu" ng-cloak>
  <awe-context-option ng-repeat="option in contextMenuData track by option.id" option-id="{{::option.id}}" option="option"></awe-context-option>
</ul>`;

// Context menu directive
aweApplication.directive('aweContextMenu',
  ['ServerData', 'ContextMenu',
    function (serverData, ContextMenu) {
      return {
        restrict: 'E',
        replace: true,
        template: template,
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
