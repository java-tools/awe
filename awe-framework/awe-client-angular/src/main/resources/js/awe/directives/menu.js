import { aweApplication } from "./../awe";
import { ClientActions } from "../data/actions";

// Menu directive
aweApplication.directive('aweMenu',
  ['ServerData', 'Component', '$document', '$window', '$filter', '$location', 'AweUtilities', 'ActionController',
    function (serverData, Component, $document, $window, $filter, $location, Utilities, $actionController) {
      return {
        restrict: 'E',
        replace: false,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('menu');
        },
        scope: {
          'menuId': '@'
        },
        link: function (scope, element) {
          // Init as component
          let component = new Component(scope, scope.menuId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          // Initialize options
          scope.menuType = scope.controller.style && scope.controller.style.indexOf("horizontal") !== -1 ? "horizontal" : "vertical";
          scope.visible = true;
          scope.status = {minimized: false, animating: false, resolution: "desktop"};
          scope.options = [];
          scope.selectedOption = {name: "", opened: {}};

          // Update children
          if (component.controller && "options" in component.controller) {
            scope.options = component.controller.options;
          }

          /******************************************************************************
           * SCOPE METHODS
           *****************************************************************************/

          /**
           * Act in case of option click
           */
          scope.onOptionClick = function () {
            switch (scope.menuType) {
              case "horizontal":
                scope.closeFirstLevel();
                break;

              case "vertical":
              default:
                switch (scope.status.resolution) {
                  case "mobile":
                    if (!scope.status.minimized) {
                      component.toggleMenu();
                    }
                    break;
                  case "tablet":
                  case "desktop":
                  default:
                    if (scope.status.minimized) {
                      scope.closeFirstLevel();
                    }
                    break;
                }
            }
          };

          /**
           * Check if option is opened
           */
          scope.isVisible = function () {
            return scope.visible && hasVisibleChildren();
          };

          /**
           * Clear first level opened options
           */
          scope.closeFirstLevel = function () {
            for (let option in scope.selectedOption.opened) {
              if (scope.selectedOption.opened[option]) {
                delete scope.selectedOption.opened[option];
              }
            }
          };

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * Toggle menu to visible or not visible
           * @param {Action} action Action received
           */
          component.toggleMenu = function (action) {
            // Toggle visibility depending on menu type
            // Change menu visibility
            if (scope.menuType === "horizontal") {
              scope.visible = !scope.visible;
              // Toggle mmc class to body
              $('body').toggleClass("mmc", !scope.visible);
            } else {
              // Close first level first (if resolution is tablet)
              scope.status.minimized = !scope.status.minimized;
              scope.status.animating = true;
              $('body').toggleClass("mmc", scope.status.minimized);
              switch (scope.status.resolution) {
                case "tablet":
                case "mobile":
                  $('body').toggleClass("mme", !scope.status.minimized);
                  break;
                default:
                  $('body').toggleClass("mme", false);
                  break;
              }

              // Close first level if not mobile
              if (scope.status.resolution !== "mobile") {
                scope.closeFirstLevel();
              }
            }

            // Finish toggle action
            Utilities.timeout(function () {
              component.onEndToggleMenu(action);
            }, 250);
          };

          /**
           * Finish toggle menu visibility
           * @param {Action} action Action received
           */
          component.onEndToggleMenu = function (action) {
            // End minimizing
            scope.status.animating = false;

            // Set resizing
            scope.status.resizing = true;

            // Publish layout event
            Utilities.publish("resize-action");

            // Finish screen action
            if (action) {
              $actionController.acceptAction(action);
            }
          };

          /**
           * Toggle navigation bar to visible or not visible
           * @param {Action} action Action received
           */
          component.toggleNavbar = function (action) {
            // main-navbar-collapse
            let node = angular.element($('#main-navbar-collapse')[0]);
            if (node.hasClass('collapse')) {
              node.removeClass('collapse');
            } else {
              node.addClass('collapse');
            }
            // Finish action
            $actionController.acceptAction(action);
          };

          /**
           * Change the menu
           * @param {Object} action Action parameters
           */
          component.changeMenu = function (action) {
            let parameters = action.attr("parameters");
            scope.options = parameters.options;
            // Finish action
            $actionController.acceptAction(action);
          };

          /******************************************************************************
           * SERVICE METHODS
           *****************************************************************************/

          /**
           * Check if option is opened
           */
          function hasVisibleChildren() {
            return $filter('allowedOption')(scope.options).length > 0;
          }

          /**
           * Checks if dropdown is visible
           * @returns {Boolean} Dropdown is visible or not
           */
          function isDropdownVisible() {
            let visible = false;
            for (let option in scope.selectedOption.opened) {
              if (scope.selectedOption.opened[option]) {
                visible = true;
              }
            }
            return visible;
          }

          /**
           * Finish toggle menu visibility
           */
          function onResize() {
            if (scope.menuType === "vertical") {
              // Tablet size
              if ($window.innerWidth > 640 &&
                $window.innerWidth <= 768) {
                scope.status.resolution = "tablet";
                if (!scope.status.minimized) {
                  component.toggleMenu();
                } else {
                  scope.closeFirstLevel();
                }
                // Mobile size
              } else if ($window.innerWidth <= 640) {
                scope.status.resolution = "mobile";
                if (!scope.status.minimized) {
                  component.toggleMenu();
                }
                // Desktop size
              } else {
                scope.status.resolution = "desktop";
                if (scope.status.minimized) {
                  scope.closeFirstLevel();
                }
              }
            }
          }

          // Watch click event if menu is dropdown
          $document.bind('click', function (event) {
            if (scope.menuType === "horizontal" ||
              (scope.status.minimized && scope.status.resolution !== "mobile")) {
              let isClickedElementChildOfDropdown = $(element).find(event.target).length > 0;
              if (!isClickedElementChildOfDropdown && isDropdownVisible()) {
                Utilities.timeout(function () {
                  scope.closeFirstLevel();
                });
              }
            }
          });

          /******************************************************************************
           * EVENT LISTENERS
           *****************************************************************************/
          let listeners = {};
          // Action listener definition
          _.each(ClientActions.menu, function (actionOptions, actionId) {
            listeners[actionId] = scope.$on("/action/" + actionId, function (event, action) {
              return component[actionOptions.method](action, scope);
            });
          });

          listeners['watchLocation'] = scope.$watch(function () {
            return $location.path();
          }, function (newLocation) {
            scope.selectedOption.name = newLocation.substring(newLocation.lastIndexOf("/") + 1, newLocation.length);
          });

          listeners['optionClick'] = scope.$on('optionClicked', function () {
            scope.onOptionClick();
          });

          listeners['resize'] = scope.$on('resize', function () {
            if (scope.status.resizing) {
              scope.status.resizing = false;
            } else {
              onResize();
            }
          });

          // Remove all listeners on unload
          listeners['resize'] = scope.$on("$destroy", function () {
            // Remove body classes
            $('body').removeClass("mmc mme");
            Utilities.clearListeners(listeners);
          });

          // Call onResize on load
          onResize();
        }
      };
    }
  ]);
