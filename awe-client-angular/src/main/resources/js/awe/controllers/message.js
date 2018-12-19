import { aweApplication } from "./../awe";

// Manage the message calls
aweApplication.controller("MessageController",
  ['$scope', 'AweSettings', 'AweUtilities', 'Control', 'Position', 'Actions',
    /**
     * Control screen data
     * @param {object} $scope
     * @param {object} $settings Awe $settings
     * @param {object} Utilities
     * @param {object} Control Control service
     * @param {object} Position Position service
     * @param {object} Actions Actions service
     */
    function ($scope, $settings, Utilities, Control, Position, Actions) {
      // Define scope alerts
      $scope.alerts = [];

      /**
       * On alert close
       * @param {type} index
       * @returns {undefined}
       */
      $scope.closeAlert = function (index) {
        let alert = $scope.alerts.splice(index, 1)[0];
        if (alert && "action" in alert) {
          if ("action" in alert) {
            alert.action.accept();
          }
          if ("timer" in alert) {
            Utilities.timeout.cancel(alert.timer);
          }
        }
      };

      /**
       * Retrieve message text from action parameters
       * @param {String} view Action view
       * @param {Object} parameters Action parameters
       * @returns {Object} message text
       */
      function getMessageText(view, parameters) {
        let messageText = {};
        if (parameters) {
          // Retrieve title and parameters
          if ("target" in parameters) {
            // If message target in parameters, retrieve from message target
            messageText = Control.getMessageFromScope(view, parameters.target);
          } else {
            if ("title" in parameters) {
              messageText.title = parameters.title;
            }
            if ("message" in parameters) {
              messageText.message = parameters.message;
            }
          }
        }
        // Retrieve message text;
        return messageText;
      }

      var MessageActions = {
        /**
         * Show a message in the screen
         * @param {Action} action Action received
         */
        message: function (action) {
          var parameters = action.attr("parameters");
          var type, timeout;
          // Translate class name
          switch (parameters.type) {
            case "error":
            case "wrong":
              type = "danger";
              break;
            case "ok":
              type = "success";
              break;
            default:
              type = parameters.type;
          }

          // Retrieve timeout
          timeout = $settings.get("messageTimeout")[parameters.type];

          // Generate message
          var messageContent = {
            type: type,
            action: action
          };

          // Retrieve message text
          var messageText = getMessageText(action.attr("view"), parameters);
          if ("title" in messageText && !Utilities.isEmpty(messageText.title)) {
            messageContent.title = messageText.title;
          }
          if ("message" in messageText) {
            messageContent.msg = messageText.message;
          }

          // Publish timeout when needed
          Utilities.timeout(function () {
            if (timeout > 0) {
              messageContent.timer = Utilities.timeout(function () {
                $scope.closeAlert($scope.alerts.length - 1);
              }, timeout);
            }
            // Push message
            $scope.alerts.push(messageContent);
          });
        },
        /**
         * Show a message over a target in the screen
         * @param {Action} action Action received
         */
        targetMessage: function (action) {
          let parameters = action.attr("parameters");
          let type, timeout;
          // Translate class name
          switch (parameters.type) {
            case "error":
            case "wrong":
              type = "danger";
              break;
            case "ok":
              type = "success";
              break;
            default:
              type = parameters.type;
          }

          // Retrieve timeout
          timeout = $settings.get("messageTimeout")[parameters.type];

          // Retrieve message text
          let messageText = getMessageText(action.attr("view"), parameters);

          // Get message target
          let address = action.attr("callbackTarget");
          let target, popoverTimer;
          if ("view" in address && "component" in address) {
            if ("column" in address && "row" in address) {
              target = $("#" + address.component + " [row-id='" + address.row + "'] [column-id='" + address.column + "']");
            } else {
              target = $("#" + address.component);
            }
          } else {
            target = $("body");
          }

          /**
           * Hide the popover
           */
          function hidePopover() {
            if (target.popover) {
              Utilities.timeout.cancel(popoverTimer);
              target.popover('hide');
            }
          }

          /**
           * Hide the popover
           */
          let isVisible = false;
          function startPopover() {
            isVisible = true;
          }

          // Create message
          let fixedBackground = $("<div class='target-message component-mask popover-dark popover-" + type + "'></div>");
          $('body').append(fixedBackground);

          /**
           * Hide the popover
           */
          function destroyPopover() {
            if (target.popover && isVisible) {
              fixedBackground.remove();
              target.popover('destroy');
              // Finish action if alive
              if (action.isAlive()) {
                action.accept();
              }
              isVisible = false;
            }
          }

        // Delete previous popover
        if (target.popover) {
          $('body > .target-message').remove();
          target.popover('destroy');
        }

        target.popover({
          container: 'body',
          title: "title" in messageText ? messageText.title : "",
          content: "message" in messageText ? messageText.message : "",
          placement: "auto bottom", //getPopoverPlacement,
          trigger: "manual"
        }).on('shown.bs.popover', startPopover).on('hidden.bs.popover', destroyPopover).popover('show');

          // Hide the popover on click
          fixedBackground.on("click", hidePopover);

          // Hide the popover on timeout
          if (timeout > 0) {
            popoverTimer = Utilities.timeout(hidePopover, timeout);
          }

          // Destroy on unload & destroy
          $scope.$on('resize', destroyPopover);
          $scope.$on('unload', destroyPopover);
          $scope.$on('$destroy', destroyPopover);
        },
        /**
         * Show modal confirm
         *
         * @param {type} action
         */
        confirm: function (action) {
          let parameters = action.attr("parameters");

          // Retrieve message text
          let messageText = getMessageText(action.attr("view"), parameters);
          if ("title" in messageText) {
            $scope.confirmTitle = messageText.title;
          }
          if ("message" in messageText) {
            $scope.confirmMessage = messageText.message;
          }

          // Set action to scope
          $scope.confirmAction = action;
        }
      };

      // Define listeners
      let listeners = {};
      _.each(Actions.message, function (actionOptions, actionId) {
        listeners[actionId] = $scope.$on("/action/" + actionId, function (event, action) {
          return MessageActions[actionOptions.method](action);
        });
      });

      // Destroy listeners
      listeners["destroy"] = $scope.$on("$destroy", function () {
        Utilities.clearListeners(listeners);
      });
    }
  ]);
