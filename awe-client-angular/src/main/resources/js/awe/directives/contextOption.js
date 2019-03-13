import { aweApplication } from "./../awe";

const template =
`<li ng-show="controller.visible || controller.separator" class="context-option {{controller.opened ? 'open' : ''}}" ng-class="::{'divider': controller.separator, 'dropdown-submenu': controller.hasChildren}" ui-dependency="dependencies" ng-cloak>
  <a ng-if="::!controller.separator" ng-disabled="isDisabled()" title="{{controller.label| translateMultiple}}" name="{{::controller.id}}" class="{{::controller.style}}"
     ng-click="onClick()">
    <i ng-if="::controller.icon" class="nav-icon fa fa-{{::controller.icon}} fa-fw"></i>
    <span ng-if="::controller.label" class="context-option-text" translate-multiple="{{::controller.label}}"></span>
  </a>
  <ul ng-if="::!controller.separator && controller.hasChildren" class="context-submenu dropdown-menu" ng-show="controller.opened">
    <awe-context-option ng-repeat="option in controller.contextMenu track by option.id" option-id="{{::option.id}}" option="option"></awe-context-option>
  </ul>
</li>`;

// Context option directive
aweApplication.directive('aweContextOption',
  ['ServerData', 'ActionController', '$compile', 'Component', 'AweUtilities', 'Storage',
    function (serverData, ActionController, $compile, Component, Utilities, Storage) {
      return {
        restrict: 'E',
        replace: true,
        template: template,
        scope: {
          'optionId': '@',
          'option': '='
        },
        compile: function (tElem) {
          var contents = tElem.contents().remove();
          var compiledContents;

          return {
            pre: function (scope, elem) {
              if (!compiledContents) {
                compiledContents = $compile(contents);
              }
              compiledContents(scope, function (clone) {
                elem.append(clone);
              });
            },
            post: function (scope, elem) {
              // Set opened as false
              scope.opened = false;
              scope.active = false;

              // Find a and ul
              var link, submenu, timer;

              // Init as component
              var component = new Component(scope, scope.optionId);
              if (!component.asComponent()) {
                // If component initialization is wrong, cancel initialization
                return false;
              }

              // Check if option has children
              if (component.controller) {
                component.controller.hasChildren = component.controller.contextMenu.length > 0;
                component.controller.separator = scope.option.separator;
              } else {
                component.controller = {
                  hasChildren: false,
                  separator: scope.option.separator
                };
              }

              /**
               * Show the submenu (if it exists)
               */
              var showSubmenu = function () {
                if (submenu.length > 0) {
                  Utilities.timeout.cancel(timer);
                  timer = Utilities.timeout(function () {
                    component.controller.opened = true;
                  });
                }
              };

              /**
               * Hide the submenu (if it exists)
               */
              var hideSubmenu = function () {
                if (submenu.length > 0) {
                  Utilities.timeout.cancel(timer);
                  timer = Utilities.timeout(function () {
                    component.controller.opened = false;
                  }, 200);
                }
              };

              /**
               * Initialize layers
               */
              var initLayers = function () {
                // Look for layers
                link = elem.children("a");
                submenu = elem.children("ul");

                // Add mouseenter and mouseleave events
                link.on("mouseenter", showSubmenu);
                link.on("mouseleave", hideSubmenu);
                if (submenu.length > 0) {
                  submenu.on("mouseenter", showSubmenu);
                  submenu.on("mouseleave", hideSubmenu);
                }
              };

              /**
               * Click option function
               */
              scope.onClick = function () {
                if (!scope.controller.disabled) {
                  ActionController.addActionList(component.controller.actions, true, {address: component.address, context: component.context});
                  component.storeEvent('click');
                  scope.$emit("hideContextMenu");
                }
              };

              /**
               * Check if option is disabled
               * @returns {boolean} option is disabled
               */
              scope.isDisabled = function () {
                return Storage.get("actions-running") || scope.$root.loading ||
                  (component.controller && component.controller.disabled);
              };

              // Disable option context menu
              elem.on('contextmenu mouseup', function (event) {
                // Cancel event propagation
                Utilities.stopPropagation(event, true);
              });

              // Initialize link and submenu layers
              Utilities.timeout(initLayers);
            }
          };
        }
      };
    }]);
