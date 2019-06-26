import {aweApplication} from "./../awe";
import {ClientActions} from "../data/actions";

// Manage the message calls
aweApplication.controller("MessageController",
  ['$scope', 'AweSettings', 'AweUtilities', 'Control', 'ActionController',
    /**
     * Control screen data
     * @param {object} $scope
     * @param {object} $settings Awe Settings
     * @param {object} $utilities
     * @param {object} $control Control service
     */
    function ($scope, $settings, $utilities, $control, $actionController) {
      let $ctrl = this;

      // Define scope alerts
      $ctrl.alerts = [];
      $ctrl.popover = null;

      /**
       * On alert close
       * @param {type} index
       * @returns {undefined}
       */
      $ctrl.closeAlert = function (index) {
        var alert = $ctrl.alerts.splice(index, 1)[0];
        if (alert && "action" in alert) {
          $actionController.acceptAction(alert.action);
          if ("timer" in alert) {
            $utilities.timeout.cancel(alert.timer);
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
        var messageText = {};
        if (parameters) {
          // Retrieve title and parameters
          if ("target" in parameters) {
            // If message target in parameters, retrieve from message target
            messageText = $control.getMessageFromScope(view, parameters.target);
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

      /**
       * Translate incoming type
       * @param {String} incomingType Incoming type
       * @return {String} Translated type
       */
      function getMessageType(incomingType) {
        // Translate class name
        switch (incomingType) {
          case "error":
          case "wrong":
            return "danger";
          case "ok":
            return "success";
          default:
            return incomingType;
        }
      }

      /**
       * Start the popover message
       * @param {Object} message Popover message
       */
      $ctrl.startPopover = function (message = $ctrl.popover) {
        message.visible = true;
      };

      /**
       * Hide the popover
       * @param {Object} message Popover message
       */
      $ctrl.hidePopover = function (message = $ctrl.popover) {
        if ((message.target || {}).popover) {
          message.visible = false;
          $utilities.timeout.cancel(message.timer);
          message.target.popover('hide');
        }
      };

      /**
       * Destroy the popover
       * @param {Object} message Popover message
       */
      $ctrl.destroyPopover = function (message = $ctrl.popover) {
        if ((message.target || {}).popover) {
          message.background.remove();
          message.target.popover('destroy');
          // Finish action if alive
          if (message.action.isAlive()) {
            $actionController.acceptAction(message.action);
          }
        }
        $ctrl.popover = null;
      };

      $ctrl.MessageActions = {
        /**
         * Show a message in the screen
         * @param {Action} action Action received
         */
        message: function (action) {
          let parameters = action.attr("parameters");
          let timeout;
          let type = getMessageType(parameters.type);

          // Retrieve timeout
          timeout = $settings.get('messageTimeout')[parameters.type];

          // Generate message
          var messageContent = {
            type: type,
            action: action
          };

          // Retrieve message text
          var messageText = getMessageText(action.attr("view"), parameters);
          if ("title" in messageText && !$utilities.isEmpty(messageText.title)) {
            messageContent.title = messageText.title;
          }
          if ("message" in messageText) {
            messageContent.msg = messageText.message;
          }

          // Publish timeout when needed
          if (timeout > 0) {
            messageContent.timer = $utilities.timeout(function () {
              $ctrl.closeAlert($ctrl.alerts.length - 1);
            }, timeout);
          }
          // Push message
          $ctrl.alerts.push(messageContent);
        },
        /**
         * Show a message over a target in the screen
         * @param {Action} action Action received
         */
        targetMessage: function (action) {
          let parameters = action.attr("parameters");

          // Delete previous popover
          if ($ctrl.popover != null) {
            $ctrl.destroyPopover($ctrl.popover);
          }

          // Retrieve message data
          $ctrl.popover = {
            action: action,
            type: getMessageType(parameters.type),
            text: getMessageText(action.attr("view"), parameters),
            timeout: $settings.get('messageTimeout')[parameters.type]
          };

          // Clean up function
          let cleanUp = () => $ctrl.destroyPopover($ctrl.popover);

          // Get message target
          let address = action.attr("callbackTarget") || {};
          if ("view" in address && "component" in address) {
            if ("column" in address && "row" in address) {
              $ctrl.popover.target = $(`#${address.component} [row-id='${address.row}'] [column-id='${address.column}']`);
            } else {
              $ctrl.popover.target = $(`#${address.component}`);
            }
            $ctrl.popover.view = $(`[ui-view='${address.view}']`);
          } else {
            $ctrl.popover.target = $("body");
            $ctrl.popover.view = $("[ui-view='base']");
          }

          // Create message
          $ctrl.popover.background = $(`<div class="target-message component-mask popover-dark popover-${$ctrl.popover.type}"></div>`);
          $('body').append($ctrl.popover.background);
          $ctrl.popover.target.popover({
            container: 'body',
            title: "title" in $ctrl.popover.text ? $ctrl.popover.text.title : "",
            content: "message" in $ctrl.popover.text ? $ctrl.popover.text.message : "",
            placement: "auto bottom",
            trigger: "manual"
          })
            .on('shown.bs.popover', () => $ctrl.startPopover($ctrl.popover))
            .on('hidden.bs.popover', cleanUp)
            .popover('show');

          // Hide the popover on click
          let hide = () => $ctrl.hidePopover($ctrl.popover);
          $ctrl.popover.background.on("click", hide);

          // Hide the popover on timeout
          if ($ctrl.popover.timeout > 0) {
            $ctrl.popover.timer = $utilities.timeout(hide, $ctrl.popover.timeout);
          }

          // Destroy on resize, unload & destroy
          ['resize', 'unload', 'destroy'].forEach((event) => $scope.$on(event, cleanUp));
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
      _.each(ClientActions.message, function (actionOptions, actionId) {
        $scope.$on("/action/" + actionId, function (event, action) {
          return $ctrl.MessageActions[actionOptions.method](action);
        });
      });
    }
  ]);
