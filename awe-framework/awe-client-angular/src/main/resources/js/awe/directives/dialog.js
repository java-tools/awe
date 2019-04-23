import { aweApplication } from "./../awe";

// Dialog directive
aweApplication.directive('aweDialog',
  ['ServerData', 'Component', 'ActionController', 'AweUtilities',
    function (serverData, Component, ActionController, Utilities) {
      return {
        restrict: 'E',
        transclude: true,
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('dialog');
        },
        scope: {
          modalId: '@dialogId'
        },
        link: function (scope) {
          // Init as component
          var component = new Component(scope, scope.modalId);
          component.isOpened = false;
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          if (component.controller) {
            scope.isExpandible = component.controller.style ? component.controller.style.indexOf("expand") !== -1 : false;
          }
          /**
           * Open modal screen
           */
          component.openDialog = function () {
            if (!component.isOpened) {
              component.isOpened = true;

              // Broadcast model change
              Utilities.publishDelayedFromScope("modalChange", true, scope);

              // Add new modal action stack
              ActionController.addStack();

              // Load dialog model (if not loaded yet)
              var actions = [];
              // Launch a resize
              actions.push({type: 'resize', parameters: {delay: 200}});

              // Launch action list
              ActionController.addActionList(actions, true, {address: component.address, context: component.context});
            }
          };
          /**
           * Close modal screen
           */
          component.closeDialog = function () {
            if (component.isOpened) {
              // Broadcast model change
              Utilities.publishDelayedFromScope("modalChange", false, scope);

              // Launch close dialog delayed
              Utilities.timeout(function() {
                // Remove modal action stack
                ActionController.removeStack();
                // Close dialog action
                if (component.controller.accept) {
                  component.controller.openAction.accept();
                } else {
                  component.controller.openAction.reject();
                }
                // Set default action on accept
                component.controller.accept = component.controller.acceptOnClose;
                component.isOpened = false;
              }, 200);
            }
          };

          /**
           * Event listeners
           */
          component.listeners = component.listeners || {};
          // On model change launch dependency
          component.listeners["controllerChange"] = scope.$on("controllerChange", function (event, parameters) {
            if (_.isEqual(parameters.address, component.address) && "opened" in parameters.controller) {
              if (parameters.controller.opened) {
                component.openDialog();
              } else {
                component.closeDialog();
              }
            }
          });
        }
      };
    }
  ]);
