import { aweApplication } from "./../awe";

// Resizable directive
aweApplication.directive('aweResizable',
  ['ServerData', 'Component', 'AweUtilities',
    function (ServerData, Component, Utilities) {
      return {
        restrict: 'E',
        transclude: true,
        replace: true,
        templateUrl: function () {
          return ServerData.getAngularTemplateUrl('resizable');
        },
        scope: {
          resizableId: '@'
        },
        compile: function () {
          return {
            /**
             * Pregeneration function
             * @param {Object} scope
             */
            pre: function (scope) {
              // Init as component
              var component = new Component(scope, scope.resizableId);
              if (!component.asComponent()) {
                // If component initialization is wrong, cancel initialization
                return false;
              }

              // Generate style
              scope.resizableStyle = component.controller.style + " " + component.controller.directions;

              // Generate directions
              scope.directions = component.controller.directions ? component.controller.directions.split(" ") : [];

              // Set variables
              scope.flex = true;

              // On drag end
              scope.onDragEnd = function () {
                Utilities.publish("resize-action");
              };
            },
            /**
             * Link function
             * @param {Object} scope
             * @param {Object} element
             */
            post: function (scope, element) {
              var style = window.getComputedStyle(element[0], null),
                w,
                h,
                vx = scope.rCenteredX ? 2 : 1, // if centered double velocity
                vy = scope.rCenteredY ? 2 : 1, // if centered double velocity
                start,
                dragDir,
                axis;

              var drag = function (e) {
                var offset = axis === 'x' ? start - e.clientX : start - e.clientY;
                switch (dragDir) {
                  case 'top':
                    if (scope.flex) {
                      element[0].style.flexBasis = h + (offset * vy) + 'px';
                    }
                    else {
                      element[0].style.height = h + (offset * vy) + 'px';
                    }
                    break;
                  case 'right':
                    if (scope.flex) {
                      element[0].style.flexBasis = w - (offset * vx) + 'px';
                    }
                    else {
                      element[0].style.width = w - (offset * vx) + 'px';
                    }
                    break;
                  case 'bottom':
                    if (scope.flex) {
                      element[0].style.flexBasis = h - (offset * vy) + 'px';
                    }
                    else {
                      element[0].style.height = h - (offset * vy) + 'px';
                    }
                    break;
                  case 'left':
                    if (scope.flex) {
                      element[0].style.flexBasis = w + (offset * vx) + 'px';
                    }
                    else {
                      element[0].style.width = w + (offset * vx) + 'px';
                    }
                    break;
                }
              };
              scope.dragStart = function (e, direction) {
                dragDir = direction;
                axis = dragDir === 'left' || dragDir === 'right' ? 'x' : 'y';
                start = axis === 'x' ? e.clientX : e.clientY;
                w = parseInt(style.getPropertyValue("width"));
                h = parseInt(style.getPropertyValue("height"));

                //prevent transition while dragging
                element.addClass('no-transition');

                document.addEventListener('mouseup', function () {
                  document.removeEventListener('mousemove', drag, false);
                  element.removeClass('no-transition');
                  if (scope.onDragEnd) {
                    scope.onDragEnd();
                  }
                });
                document.addEventListener('mousemove', drag, false);

                // Cancel event propagation
                Utilities.stopPropagation(e, true);
              };
            }
          };
        }
      };
    }
  ]);
