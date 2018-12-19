import { aweApplication } from "./../awe";

// Manage the screen requests
aweApplication.controller("ScreenController",
  ['$scope', '$location', 'AweSettings', 'AweUtilities', 'ActionController', 'DependencyController', 'Control', 'ServerData', 'Storage', '$state', 'Actions',
    /**
     * Control screen data
     *
     * @param {Service} $scope
     * @param {Service} $location
     * @param {Service} $settings
     * @param {Service} Utilities
     * @param {Service} ActionController
     * @param {Service} DependencyController
     * @param {Service} Control
     * @param {Service} ServerData
     * @param {Service} Storage Storage service
     * @param {Service} $state $state service
     * @param {Service} Actions Actions service
     */
    function ($scope, $location, $settings, Utilities, ActionController, DependencyController, Control, ServerData, Storage, $state, Actions) {
      // Action Controller methods

      /**
       * Whether to show actions or not
       * @returns {Boolean}
       */
      $scope.showActions = function () {
        return $settings.get("actionsStack") > 0;
      };

      /**
       * Manage keydown event
       * @param {Object} $event jQuery Event
       */
      $scope.onKeydown = function($event) {
        // On Alt + Shift + 0-9, toggle actions to 0-9 secs
        let DIGITS = [48, 49, 50, 51, 52, 53, 54, 55, 56, 57];
        if ($event.altKey && $event.shiftKey && DIGITS.includes($event.which)) {
          // Toggle stack
          $settings.update({actionsStack: DIGITS.indexOf($event.which) * 1000});
        }
      }

      // Sync stack lists
      $scope.syncStacks = ActionController.actionStackList;

      // Async stack
      $scope.asyncStack = ActionController.asyncStackList;

      var ScreenActions = {
        /**
         * Retrieve parameters and send them to the server
         * @param {Action} action Action received
         */
        screen: function (action) {

          // Retrieve action parameters
          var parameters = action.attr("parameters");
          var context = action.attr("context");
          var token = $settings.getToken();

          // If token has been received, update it
          if ("token" in parameters) {
            token = parameters.token;
            if (Utilities.isEmpty(token)) {
              $settings.clearToken();
            } else {
              $settings.setToken(token);
            }
          }

          // If language has been received, update it
          if ("language" in parameters) {
            $settings.changeLanguage(parameters.language);
          }

          // If theme has been received, update it
          if ("theme" in parameters) {
            $settings.update({theme: parameters.theme});
          }

          // Define target screen
          var target = context ? "/" + context + "/" : "";
          if ("screen" in parameters) {
            target += parameters.screen;
          } else if ("target" in parameters) {
            target += parameters.target;
          } else {
            target += action.attr("target");
          }

          // Location is not the same
          if (target !== $location.url()) {
            // Redirect to the screen
            var state = Utilities.getState(target);
            $state.go(state.to, state.parameters, {reload: false, inherit: true, notify: true, location: true});

            // Finish screen action
            action.accept();
          } else if ($settings.get("reloadCurrentScreen")) {
            // Location is the same: reload
            ScreenActions.reload(action);
          } else {
            // Finish action
            action.accept();
          }
        },
        /**
         * Reload the current state
         * @param {Action} action Action received
         */
        reload: function (action) {
          // Retrieve action parameters
          $state.go($state.current, {r: Math.random()}, {reload: false, inherit: true, notify: true, location: false});

          // Finish screen action
          action.accept();
        },
        /**
         * Return to the previous screen
         * @param {Action} action Action received
         */
        back: function (action) {
          // Finish screen action
          action.accept();

          // Go to the previous screen
          window.history.back();
        },
        /**
         * Change the language of the interface
         * @param {Action} action Action received
         */
        changeLanguage: function (action) {
          // Retrieve action parameters
          var target = action.attr("target");
          var view = action.attr("view");
          var model = Storage.get("model");
          var language;
          if (view in model && target in model[view]) {
            language = model[view][target].selected;
          }

          // If language has been received, update it
          if (target) {
            $settings.changeLanguage(language);
          }

          // Finish screen action
          action.accept();
        },
        /**
         * Wait x milliseconds
         * @param {Action} action Action received
         */
        wait: function (action) {
          // Retrieve action parameters
          var parameters = action.attr("parameters");
          var time = parameters.target || 1;

          Utilities.timeout(function() {
            // Finish action
            action.accept();
          }, time);
        },
        /**
         * Change the theme of the interface
         * @param {Action} action Action received
         */
        changeTheme: function (action) {

          // Retrieve action parameters
          var target = action.attr("target");
          var view = action.attr("view");
          var theme = $settings.get("theme");
          var model = Storage.get("model");
          if (view in model && target in model[view]) {
            theme = model[view][target].selected;
          }

          // If language has been received, update it
          if (target) {
            $settings.update({theme: theme});
          }

          // Finish screen action
          action.accept();
        },
        /**
         * Load screen data
         * @param {Action} action Action received
         */
        screenData: function (action) {
          // Get parameters
          var parameters = action.attr("parameters");

          // Store parameters in view scope
          Storage.get("screenData")[parameters.view] = parameters.screenData;

          // Send messages
          var actions = parameters.screenData.actions;
          if (actions.length > 0) {
            ActionController.addActionList(actions, false, {});
          }

          // Close action
          action.accept();
        },
        /**
         * Open screen dialog
         * @param {Action} action Action received
         */
        openDialog: function (action) {
          var address = action.attr("callbackTarget");

          // Change controller
          var attributes = {opened: true, openAction: action};
          Control.changeControllerAttribute(address, attributes);
        },
        /**
         * Close screen dialog
         * @param {Action} action Action received
         */
        closeDialog: function (action) {
          // Close action
          action.accept();

          // Close dialog
          Utilities.timeout(function () {
            Control.changeControllerAttribute(action.attr("callbackTarget"), {opened: false, accept: true});
          });
        },
        /**
         * Close screen dialog and cancel action
         * @param {Action} action Action received
         */
        closeDialogAndCancel: function (action) {
          // Close action
          action.accept();

          // Close dialog
          Utilities.timeout(function () {
            Control.changeControllerAttribute(action.attr("callbackTarget"), {opened: false, accept: false});
          });
        },
        /**
         * Finish loading
         * @param {Action} action Action received
         */
        endLoad: function (action) {
          // Retrieve action address
          var address = action.attr("callbackTarget");
          var api = Control.getAddressApi(address);
          if (api.endLoad) {
            api.endLoad();
          }

          // Close action
          action.accept();
        },
        /**
         * Get file from server
         * @param {Action} action get file from server
         */
        getFile: function (action) {
          // Variable definition
          let parameters = {
            ...action.attr("parameters"),
            [$settings.get("serverActionKey")]: "get-file",
            ...$settings.getTokenObject()
          };

          // Generate url parameter
          var fileData = ServerData.getFileData("download", parameters);
          fileData.action = action;

          // Download file
          Utilities.downloadFile(fileData);
        },
        /**
         * Enable dependencies
         * @param {Action} action Action received
         */
        enableDependencies: function (action) {
          ScreenActions.toggleDependencies(action, true);
        },
        /**
         * Disable dependencies
         * @param {Action} action Action received
         */
        disableDependencies: function (action) {
          ScreenActions.toggleDependencies(action, false);
        },
        /**
         * Enable/Disable dependencies
         * @param {Action} action Action received
         * @param {Boolean} enabled Enable/disable dependencies
         */
        toggleDependencies: function (action, enabled) {
          // Retrieve action address
          DependencyController.toggleDependencies(enabled);

          // Close action
          action.accept();
        },
        /**
         * Add Class
         * @param {Action} action Action received
         */
        addClass: function (action) {
          ScreenActions.toggleClass(action, true);
        },
        /**
         * Remove Class
         * @param {Action} action Action received
         */
        removeClass: function (action) {
          ScreenActions.toggleClass(action, false);
        },
        /**
         * Add/Remove a class to a tag
         * @param {Action} action Action received
         * @param {Action} add Add/Remove a class
         */
        toggleClass: function (action, add) {
          // Variable definition
          var tagSelector = action.attr("target");
          var parameters = action.attr("parameters");
          var targetClass = parameters[$settings.get("targetActionKey")];
          var method = add ? "addClass" : "removeClass";

          // Add/remove the class/classes
          $(tagSelector)[method](targetClass);

          // Close action
          action.accept();
        },
        /**
         * Print the current screen
         * @param {Action} action Action received
         */
        screenPrint: function (action) {
          window.print();

          // Close action
          action.accept();
        },
        /**
         * Close the current window
         * @param {Action} action Action received
         */
        closeWindow: function (action) {
          // Close action
          action.accept();

          // Call window close
          window.close();
        },
        /**
         * Print the current screen
         * @param {Action} action Action received
         */
        endDependency: function (action) {
          var dependency = action.attr("parameters").dependency;
          DependencyController.finishDependency(dependency, action);
        }
      };

      // Define listeners
      var listeners = {};
      _.each(Actions.screen, function (actionOptions, actionId) {
        listeners[actionId] = $scope.$on("/action/" + actionId, function (event, action) {
          return ScreenActions[actionOptions.method](action);
        });
      });

      // On model change launch dependency
      listeners['modelChanged'] = $scope.$on("modelChanged", function (event, launchers) {
        DependencyController.checkAndLaunch(launchers);
      });
      // On screen compiled
      listeners['compiled'] = $scope.$on('compiled', function (event, view) {
        DependencyController.start(view);
      });
      // On cell compiled
      listeners['initializeCell'] = $scope.$on('initialize-cell', function (event, address) {
        DependencyController.restart(address);
      });
      // Unload dependencies
      listeners["unload"] = $scope.$on("unload", function (event, view) {
        DependencyController.unregisterView(view);
      });

      // Destroy listeners
      listeners["destroy"] = $scope.$on("$destroy", function () {
        Utilities.clearListeners(listeners);
      });
    }]);