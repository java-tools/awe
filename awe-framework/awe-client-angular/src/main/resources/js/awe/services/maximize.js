import { aweApplication } from "./../awe";

// Maximize service
aweApplication.factory('Maximize',
  ['Position', 'AweUtilities',
    function (position, Utilities) {
      var Maximize = {
        /**
         * Initialize dialog
         * @param {scope} scope Dialog scope
         * @param {object} elem dialog node
         */
        initMaximize: function (scope, elem) {
          // Panel text
          var panelText = {
            MAXIMIZE: 'SCREEN_TEXT_MAXIMIZE',
            RESTORE: 'SCREEN_TEXT_RESTORE'
          };

          // Variable initialization
          var animationTime = 300;
          var useCSS3Animation = true;
          var maximizeTarget;
          var minimize = {
            targets: [],
            parents: []
          };

          scope.maximized = false;
          scope.iconMaximized = false;
          scope.togglePanelText = panelText.MAXIMIZE;
          scope.panelResizing = false;
          scope.maximizing = false;

          // Controller variables
          if (scope.controller) {
            scope.maximize = scope.controller.maximize;
          } else {
            scope.maximize = false;
          }

          // Store heading and content panel
          var $body = $('body');
          /**
           * Remove animation node
           * @param {object} $node Animation node
           */
          var removeAnimationNode = function ($node) {
            Utilities.timeout(function () {
              // Remove aanimation
              $body.removeClass("animationContainer");
              $node.remove();
              // End resizing
              scope.$root.resizing = false;
              scope.panelResizing = false;
              scope.maximizing = false;
            });
          };
          /**
           * Launches maximize/restore animations
           * @param {object} node Resize node
           * @param {object} finalSize Final resize size
           * @param {function} onEndAnimation On end animation function (optional)
           * @returns {undefined}
           */
          var launchAnimation = function (node, finalSize, onEndAnimation) {
            var $node = $(node);
            /**
             * Launch end animation methods
             */
            var endAnimation = function () {
              // Launch on end animation event (if defined)
              if (onEndAnimation) {
                onEndAnimation();
              }

              // Remove animation node
              removeAnimationNode($node);
            };
            if (useCSS3Animation) {
              Utilities.animateCSS($node, finalSize, animationTime, endAnimation);
            } else {
              Utilities.animateJavascript($node, finalSize, animationTime, endAnimation);
            }
          };
          /**
           * Generate layer cloned for animation
           */
          var generateAnimationClone = function () {
            // Get initial size
            var initialSize = position.getOuterDimensions(elem);
            // Add animate clone
            var resizing = elem.clone();
            resizing.css(initialSize);
            resizing.removeClass("expand");
            resizing.addClass("resizeAnimation");

            // Empty content layer
            resizing.find(".maximize-content").addClass("panel-awe").empty();


            $body.addClass("animationContainer");
            $body.append(resizing);
            // Return clone layer
            return resizing;
          };

          /**
           * Minimize parents and siblings
           * @param {type} maximizeTarget
           * @returns {undefined}
           */
          var updateElementsToMinimize = function (maximizeTarget) {
            minimize.targets = [].concat(elem.siblings(":visible").toArray());
            minimize.parents = [];
            _.each(elem.parentsUntil(maximizeTarget), function (parent) {
              minimize.targets = minimize.targets.concat($(parent).siblings(":visible").toArray());
              minimize.parents.push(parent);
            });
          };

          /**
           * Minimize parents and siblings
           * @returns {undefined}
           */
          var minimizeParentsAndSiblings = function () {
            $(minimize.parents).addClass("maximizeParent");
            $(minimize.targets).addClass("minimized");
            elem.removeAttr('style');
          };

          /**
           * Calculate maximized size depending on maximize target
           * @returns {object} Resizing size
           */
          scope.maximizeTargetLayer = function () {
            var finalSize = position.getInnerDimensions(maximizeTarget);
            // Get margins
            var margins = {
              width: elem.outerWidth(true) - elem.outerWidth(false),
              height: elem.outerHeight(true) - elem.outerHeight(false)
            };
            finalSize.width -= margins.width;
            finalSize.height -= margins.height;
            // Apply maximize dimensions to target layer
            var elemSize = _.cloneDeep(finalSize);
            var offset = elem.offsetParent().offset();
            elemSize.top -= offset.top;
            elemSize.left -= offset.left;
            // Calculate final size for animation
            finalSize.top += parseInt(elem.css('margin-top'));
            finalSize.left += parseInt(elem.css('margin-left'));
            return {final: finalSize, element: elemSize};
          };
          /**
           * On resize screen
           *
           */
          scope.onResize = function () {
            if (scope.maximized) {
              var maximizeSizes = scope.maximizeTargetLayer();
              elem.css(maximizeSizes.element);
            }
          };
          /**
           * Maximize the panel
           */
          scope.maximizePanel = function () {
            // Generate animation clone
            var resizing = generateAnimationClone();
            // Set resizing and maximized
            scope.$root.resizing = true;
            scope.panelResizing = true;
            scope.maximizing = true;
            // Restore all maximized parents
            scope.$emit("restore");
            // Find maximize target
            if (!maximizeTarget) {
              maximizeTarget = elem.parents(".maximize-target:first");
            }

            // Launch animation
            Utilities.timeout(function () {
              // Launch animation
              var maximizeSizes = scope.maximizeTargetLayer();
              updateElementsToMinimize(maximizeTarget);
              $(minimize.targets).fadeOut(animationTime);
              launchAnimation(resizing, maximizeSizes.final, function () {
                scope.maximized = true;
                scope.iconMaximized = true;
                minimizeParentsAndSiblings();
                scope.$broadcast("resize");
              });
            }, 200);
          };
          /**
           * Restore the window size
           */
          scope.restorePanel = function () {
            // Generate animation clone
            var resizing = generateAnimationClone();
            // Remove minimize class
            $(minimize.targets).removeClass("minimized").fadeIn(animationTime + 150);
            $(minimize.parents).removeClass("maximizeParent").fadeIn(animationTime + 150);
            // Remove minimized class
            elem.removeAttr('style');
            // Set resizing
            scope.$root.resizing = true;
            scope.panelResizing = true;
            scope.maximized = false;
            Utilities.timeout(function () {
              // Launch animation
              var finalSize = position.getOuterDimensions(elem);
              launchAnimation(resizing, finalSize, function () {
                scope.iconMaximized = false;
                scope.$broadcast("resize");
              });
            }, 150);
          };

          /**
           * Restore the window size
           */
          scope.togglePanel = function () {
            if (scope.maximized) {
              scope.restorePanel();
              scope.togglePanelText = panelText.RESTORE;
            } else {
              scope.maximizePanel();
              scope.togglePanelText = panelText.MAXIMIZE;
            }
          };

          /**
           * Event listeners
           */
          var listeners = {};
          // Capture event for element resize
          listeners['resize'] = scope.$on("resize", function (event, initialScope) {
            if (scope !== initialScope && scope.onResize) {
              scope.onResize();
            }
          });
          // Capture event for children maximizes
          listeners['restore'] = scope.$on("restore", function () {
            if (scope.maximized) {
              scope.restorePanel();
            }
          });
          // Remove all listeners on unload
          scope.$on("$destroy", function () {
            // Clear listeners
            Utilities.clearListeners(listeners);
          });
        }
      };
      return Maximize;
    }
  ]);