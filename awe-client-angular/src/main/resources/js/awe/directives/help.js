import { aweApplication } from "./../awe";

// Help directive
aweApplication.directive('aweHelp',
  ['ServerData', 'Position', 'AweUtilities',
    /**
     * Help directive
     * @param {object} ServerData Server call service
     * @param {object} Position Position service
     * @param {object} Utilities Awe utilities
     */
    function (ServerData, Position, Utilities) {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return ServerData.getAngularTemplateUrl('help');
        },
        /**
         * Link directive
         * @param {type} scope
         * @param {type} elem
         */
        link: function (scope, elem) {

          // Variable definition
          scope.direction = "bottom";
          scope.isShowing = false;
          scope.helpTimer = null;

          /**
           * Reposition the help node
           */
          scope.reposition = function () {
            if (scope.isShowing) {

              // Retrieve position
              var position = Position.checkPosition(elem, scope.node, $('body'), {
                position: {vertical: "bottom", horizontal: "center"},
                margin: {vertical: 0, horizontal: 0}
              });

              // Reposition element
              elem.css({
                top: position.coordinates.top,
                left: position.coordinates.left
              });

              // Change arrow direction
              scope.direction = position.direction;
            }
          };

          /**
           * Show the help
           * @param {Object} parameters
           * @returns {undefined}
           */
          scope.showHelp = function (parameters) {
            // Store help parameters
            scope.text = parameters.text;
            scope.image = parameters.image;
            scope.node = parameters.node;

            // Show help
            scope.isShowing = true;

            // Reposition help
            Utilities.timeout(function () {
              scope.reposition();
            }, 50);
          };

          /**
           * Hide the help
           */
          scope.hideHelp = function () {
            scope.isShowing = false;
            // Reposition element
            elem.css({
              top: -10000,
              left: -10000
            });
          };

          // Event listeners
          var listeners = {};
          listeners["resize"] = scope.$on("resize", function () {
            scope.reposition();
          });
          listeners["resize-action"] = scope.$on("resize-action", function () {
            scope.reposition();
          });

          // Show the help
          listeners["show"] = scope.$on('showHelp', function (event, parameters) {
            if (!scope.isShowing) {
              Utilities.timeout(function () {
                scope.showHelp(parameters);
              });
            }
          });

          // Hide the help
          listeners["hide"] = scope.$on('hideHelp', function () {
            if (scope.isShowing) {
              Utilities.timeout(function () {
                scope.hideHelp();
              });
            }
          });

          // Destroy listeners
          elem.on("$destroy", function () {
            scope.hideHelp();
            Utilities.clearListeners(listeners);
          });
        }
      };
    }
  ]);
