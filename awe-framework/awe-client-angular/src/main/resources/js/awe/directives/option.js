import { aweApplication } from "./../awe";

// Option directive
aweApplication.directive('aweOption',
  ['ServerData', 'ActionController', '$compile', '$filter',
    function (serverData, ActionController, $compile, $filter) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('option');
        },
        scope: {
          'optionName': '@',
          'optionTitle': '@',
          'optionStyle': '@',
          'optionIcon': '@',
          'optionText': '@',
          'controller': '=',
          'selectedOption': '=',
          'firstLevel': '=',
          'closeFirstLevel': '&',
          'menuType': '@',
          'onOptionClick': '&',
          'status': '='
        },
        compile: function (tElem) {
          var contents = tElem.contents().remove();
          var compiledContents;

          return {
            pre: function (scope, elem) {
              // Set opened as false
              scope.opened = false;
              scope.active = false;

              /**
               * Check if option is opened
               */
              scope.hasVisibleChildren = function () {
                return $filter('allowedOption')(scope.controller.options).length > 0 && !scope.controller.separator;
              };

              /**
               * Check if option is opened
               */
              var isOpened = function () {
                return scope.optionName in scope.selectedOption.opened;
              };

              /**
               * Check if option is opened
               */
              var isFloating = function () {
                return scope.firstLevel && scope.status.resolution !== 'mobile';
              };

              /**
               * Static option classes
               * @returns {Array}
               */
              scope.getStaticOptionClasses = function () {
                var classes = [];

                // Visible children
                if (scope.hasVisibleChildren()) {
                  classes.push('mm-dropdown');
                }

                // First level
                if (scope.firstLevel) {
                  classes.push('mm-dropdown-root');
                }

                // Separator
                if (scope.controller.separator) {
                  classes.push('divider');
                }

                return classes.join(" ");
              };

              /**
               * Dynamic option classes
               * @returns {Array}
               */
              scope.getOptionClasses = function () {
                var classes = [];
                // Opened status
                if (isOpened()) {
                  if (isFloating() && scope.status.minimized) {
                    classes.push('mmc-dropdown-open');
                  } else {
                    classes.push('open');
                  }
                }

                // Active status
                if (scope.selectedOption.name === scope.optionName) {
                  classes.push('active');
                }

                return classes.join(" ");
              };

              /**
               * Static submenu classes
               * @returns {Array}
               */
              scope.getStaticSubmenuClasses = function () {
                var classes = [];

                // First level classes
                if (scope.firstLevel) {
                  classes.push('mm-dropdown-first');
                  classes.push('menu-shadow');
                } else {
                  classes.push('mm-dropdown-target');
                }

                return classes.join(" ");
              };

              /**
               * Dynamic submenu classes
               * @returns {Array}
               */
              scope.getSubmenuClasses = function () {
                var classes = [];

                // Opened status
                if (isOpened()) {
                  if (isFloating() && scope.status.minimized) {
                    classes.push('mmc-dropdown-open-ul');
                  } else {
                    classes.push('opened');
                  }
                }

                // Hide if not is opened or is floating
                if (!(isOpened() || isFloating())) {
                  classes.push("ng-hide");
                }

                return classes.join(" ");
              };

              if (!compiledContents) {
                compiledContents = $compile(contents);
              }
              compiledContents(scope, function (clone) {
                elem.append(clone);
              });
            },
            post: function (scope) {
              /**
               * Click button function
               */
              scope.optionClick = function () {
                if (scope.hasVisibleChildren()) {
                  // Node is a branch
                  if (scope.optionName in scope.selectedOption.opened) {
                    // Node is an opened branch
                    scope.closeOption();
                  } else {
                    // Node is a closed branch
                    if (scope.firstLevel) {
                      scope.closeFirstLevel();
                    }
                    scope.opened = true;
                    scope.selectedOption.opened[scope.optionName] = scope.firstLevel;
                  }
                } else {
                  // Node is a leaf
                  if ("actions" in scope.controller) {
                    // Close all previous actions
                    ActionController.closeAllActions();

                    // All action list to stack
                    ActionController.addActionList(scope.controller.actions, true, {address: scope.address, context: scope.context});

                    // Emit option clicked
                    scope.$emit("optionClicked");
                  }
                }
              };

              /**
               * Closes the option and marks option as closed after animation
               */
              scope.closeOption = function () {
                delete scope.selectedOption.opened[scope.optionName];
                scope.opened = false;
              };
            }
          };
        }
      };
    }]);
