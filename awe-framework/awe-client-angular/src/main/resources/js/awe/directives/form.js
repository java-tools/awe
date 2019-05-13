import { aweApplication } from "./../awe";
import { ClientActions } from "../data/actions";
import _ from "lodash";

// Form directive
aweApplication.directive('aweForm',
  ['ServerData', 'Control', 'ActionController', 'AweSettings', 'AweUtilities', 'Validator',
    /**
     * Form directive
     * @param {Service} ServerData Server data
     * @param {Service} Control Control service
     * @param {Service} Control Control service
     * @param {Service} ActionController ActionController service
     * @param {Service} $settings AWE $settings
     * @param {Service} Utilities AWE Utilities
     * @param {Service} Validator Validator service
     */
    function (ServerData, Control, ActionController, $settings, Utilities, Validator) {


      /**
       * Retrieve reseteable scopes
       * @param {type} target
       * @param {type} scope
       * @returns {Array}
       */
      var getReseteableScopes = function (target, scope) {
        var reseteableScopes = [];
        if (target) {
          // Reset target model
          var reseteableComponents = [".criterion", ".grid", ".chart"];
          var targetId = "#" + target;
          var reseteableTarget = "";
          _.each(reseteableComponents, function (reseteableComponent) {
            reseteableTarget += reseteableTarget === "" ? "" : ",";
            reseteableTarget += targetId + " " + reseteableComponent;
          });
          var $target = $(reseteableTarget);
          if ($target.length) {
            // If target has children, reset all children
            _.each($target, function (reseteable) {
              var $reseteable = $(reseteable);
              if ($reseteable.children().length > 0) {
                _.each($reseteable.children(), function(child) {
                  if($(child).scope) {
                    reseteableScopes.push($(child).scope());
                  }
                });
                // Else reset only the target
              } else if ($reseteable.scope()) {
                reseteableScopes.push($reseteable.scope());
              }
            });
          } else {
            // No children. Try to reset scope
            $target = $(targetId);
            if ($target.children().length > 0 && $target.children().scope()) {
              reseteableScopes.push($target.children().scope());
              // Else reset only the target
            } else if ($target.scope()) {
              reseteableScopes.push($target.scope());
            }
          }
        } else {
          // Reset view model
          reseteableScopes.push(scope);
        }
        return reseteableScopes;
      };

      var FormActions = {
        /**
         * Validate the form
         * @param {Action} action Action received
         * @param {Object} scope Scope
         */
        validate: function (action, scope) {

          // Reset validator errors
          scope.showValidation = false;

          // Check if action is for a specific target
          var target = action.attr("target");
          if (target) {
            // Validate an element and children
            var $target = $("#" + target);
            var $base;
            if ($target.is(".form-control")) {
              $base = $target.closest(".criterion");
            } else {
              $base = $target;
            }
          } else {
            $base = $(document.body);
          }

          // Launch validation
          var errorList = Validator.validateNode($base);

          // Check if validation has been sucessful
          if (errorList.length === 0) {
            // If is ok, accept the action
            action.accept();
            // If validation is not ok, reject the action
          } else {
            action.reject();
          }
        },
        /**
         * Show validation error
         * @param {Object} scope Scope
         * @param {Object} error Error to show
         */
        showValidationError: function (scope, error) {
          Validator.showValidationError(scope, error);
        },
        /**
         * Set a criterion as valid
         * @param {Action} action Action received
         */
        setValid: function (action) {
          var target = action.attr("callbackTarget");
          Control.launchApiMethod(target, "changeValidation", ["invalid", false]);
          action.accept();
        },
        /**
         * Set a criterion as invalid
         * @param {Action} action Action received
         */
        setInvalid: function (action) {
          var parameters = action.attr("parameters");
          var target = action.attr("callbackTarget");
          Control.launchApiMethod(target, "changeValidation", [{
              invalid: {
                message: parameters.message
              }
            }, true]);
          Control.launchApiMethod(target, "validate", []);
          // Accept the current action
          action.accept();
        },
        /**
         * Retrieve parameters and send them to the server
         * @param {Action} action Action received
         */
        server: function (action) {
          // Launch server action with form values
          ServerData.launchServerAction(action, ServerData.getFormValues(), false);
        },
        /**
         * Retrieve parameters and send them to the server for printing actions (send images and text)
         * @param {Action} action Action received
         */
        serverPrint: function (action) {
          // Launch server action for printing
          ServerData.launchServerAction(action, ServerData.getFormValuesForPrinting(), true);
        },
        /**
         * Retrieve parameters and send them to the server for printing actions (send images and text)
         * @param {Action} action Action received
         * @param {Object} scope Scope
         */
        serverDownload: function (action, scope) {
          // Launch server action for printing
          var parameters = {};
          var target = action.attr("callbackTarget");

          // Store parameters
          _.merge(parameters, action.attr("parameters"), ServerData.getFormValues());
          var targetAction = parameters[$settings.get("targetActionKey")];

          // Retrieve target specific attributes for the server call
          if (target) {
            let api = Control.getAddressApi(target);
            if (api && api.getSpecificFields) {
              // Add form values
              _.merge(parameters, api.getSpecificFields());
            }
          }

          // Generate url parameter
          var fileData = ServerData.getFileData("download/maintain/" + targetAction, parameters);
          fileData.action = action;

          // Download file
          Utilities.downloadFile(fileData);
        },
        /**
         * Update model with action values
         * @param {Action} action Action received
         */
        fill: function (action) {
          // Retrieve parameters
          var parameters = _.cloneDeep(action.attr("parameters"));
          var data = parameters.datalist;
          var address = action.attr("callbackTarget");

          // Generate model
          var model = data;
          model.values = model.rows;
          delete model.rows;

          // Publish model change
          Control.changeModelAttribute(address, model, true);

          // Finish action
          action.accept();
        },
        /**
         * Update controller with action values
         * @param {Action} action Action received
         */
        updateController: function (action) {
          // Retrieve parameters
          let parameters = _.cloneDeep(action.attr("parameters"));
          let data = parameters.datalist;
          let values = data.rows;
          let address = action.attr("callbackTarget");
          let attribute = action.attr("parameters").attribute;
          delete data.rows;

          // Change controller
          let attributes = {};
          attributes[attribute] = values[0].value;
          Control.changeControllerAttribute(address, attributes);

          // Finish action
          action.accept();
        },
        /**
         * Update model with action values
         * @param {Action} action Action received
         */
        select: function (action) {
          // Retrieve parameters
          var parameters = _.cloneDeep(action.attr("parameters"));
          var values = parameters.values;
          var address = action.attr("callbackTarget");

          // Call the method update seleted value from API
          Control.changeModelAttribute(address, {selected: values}, true);

          // Finish action
          action.accept();
        },
        /**
         * Reset view selected values
         * @param {Action} action
         * @param {Object} scope
         */
        reset: function (action, scope) {
          // Get parameters
          var view = action.attr("view");

          // Check reset target
          var reseteableScopes = getReseteableScopes(action.attr("target"), scope);
          _.each(reseteableScopes, function (reseteableScope) {
            reseteableScope.$broadcast("reset-scope", view);
          });

          // Finish action
          action.accept();
        },
        /**
         * Restore view selected values
         * @param {Service} action
         * @param {Object} scope
         */
        restore: function (action, scope) {
          // Get parameters
          var view = action.attr("view");

          // Check restore target
          var reseteableScopes = getReseteableScopes(action.attr("target"), scope);
          _.each(reseteableScopes, function (reseteableScope) {
            reseteableScope.$broadcast("restore-scope", view);
          });

          // Finish action
          action.accept();
        },
        /**
         * Restore view selected values with target
         * @param {Service} action
         * @param {Object} scope
         */
        restoreTarget: function (action, scope) {
          // Get parameters
          var view = action.attr("view");

          // Check restore target
          var reseteableScopes = getReseteableScopes(action.attr("target"), scope);
          _.each(reseteableScopes, function (reseteableScope) {
            reseteableScope.$broadcast("restore-scope-target", view);
          });

          // Finish action
          action.accept();
        },
        /**
         * Destroy all views
         * @param {Action} action
         */
        logout: function (action) {
          // Close following actions
          ActionController.deleteStack();

          // Zombie action (to accept server actions)
          action.attr("alive", true);

          // Launch a logout server action
          var parameters = {};
          parameters[$settings.get("serverActionKey")] = "logout";
          action.attr("parameters", parameters);
          FormActions.server(action);

          // Destroy all views
          Control.destroyAllViews();
        },
        /**
         * Check if model has been modified
         * @param {Action} action
         */
        checkModelUpdated: function (action) {
          // Get target
          var target = action.attr("callbackTarget");
          // Get view
          var view = action.attr("view");
          var context = action.attr("context");
          // Check if model is different than initial model
          var changes = Control.checkModelChanged(view);

          if (changes) {
            // Create message to show in confirm action
            var targetMessage = 'CONFIRM_UPDATE_DATA';
            var message = {
              title: 'CONFIRM_TITLE_UPDATED_DATA',
              message: 'CONFIRM_MESSAGE_UPDATED_DATA'
            };
            // Add targetAction message to scope
            Control.addMessageToScope(view, targetMessage, message);

            // Create confirm action
            var confirmAction = {type: 'confirm'};

            // Add parameters
            confirmAction.parameters = {'target': targetMessage};

            // Send action confirm
            ActionController.addActionList([confirmAction], false, {address: target, context: context});
          }

          // Accept action
          action.accept();
        },
        /**
         * Check if model hasn't been modified
         * @param {Action} action
         */
        checkModelNoUpdated: function (action) {
          // Get target
          var target = action.attr("callbackTarget");
          // Get view
          var view = action.attr("view");
          var context = action.attr("context");
          // Check if model is equal than initial model
          var changes = Control.checkModelChanged(view);

          if (!changes) {
            // Create message to show in confirm action
            var targetMessage = 'CONFIRM_NOT_UPDATE_DATA';
            var message = {
              title: 'CONFIRM_TITLE_NOT_UPDATED_DATA',
              message: 'CONFIRM_MESSAGE_NOT_UPDATED_DATA'
            };
            // Add targetAction message to scope
            Control.addMessageToScope(view, targetMessage, message);

            // Create confirm action
            var confirmAction = {type: 'confirm'};

            // Add parameters
            confirmAction.parameters = {'target': targetMessage};

            // Send action confirm
            ActionController.addActionList([confirmAction], false, {address: target, context: context});
          }

          // Accept action
          action.accept();
        },
        /**
         * Check if model has empty data
         * @param {Action} action
         */
        checkModelEmpty: function (action) {
          // Get target
          var target = action.attr("callbackTarget");
          // Get view
          var view = action.attr("view");
          var context = action.attr("context");
          // Check if model is different than initial model
          var empty = Control.checkModelEmpty(view);

          if (empty) {
            // Create message to show in confirm action
            var targetMessage = 'CONFIRM_EMPTY_DATA';
            var message = {
              title: 'CONFIRM_TITLE_EMPTY_DATA',
              message: 'CONFIRM_MESSAGE_EMPTY_DATA'
            };
            // Add targetAction message to scope
            Control.addMessageToScope(view, targetMessage, message);

            // Create confirm action
            var confirmAction = {type: 'confirm'};

            // Add parameters
            confirmAction.parameters = {'target': targetMessage};

            // Send action confirm
            ActionController.addActionList([confirmAction], false, {address: target, context: context});
          }
          // Accept action
          action.accept();
        },
        /**
         * Set a static value for an element
         * @param {Action} action
         */
        value: function (action) {
          // Retrieve parameters
          action.attr("parameters").values = [action.attributes.value];
          FormActions.select(action);
        },
        /**
         * Cancel all actions of the current stack
         */
        cancel: function () {
          ActionController.deleteStack();
        }
      };
      const Form = {
        restrict: 'A',
        link: function (scope, elem) {
          // Store element in scope
          scope.element = elem;

          // Define listeners
          var listeners = {};
          _.each(ClientActions.form, function (actionOptions, actionId) {
            listeners[actionId] = scope.$on("/action/" + actionId, function (event, action) {
              return FormActions[actionOptions.method](action, scope);
            });
          });

          // Show validation error
          listeners["showValidationError"] = scope.$on("show-validation-error", function (event, error) {
            FormActions.showValidationError(scope, error);
          });

          // Destroy listeners
          listeners["destroy"] = scope.$on("$destroy", function () {
            Utilities.clearListeners(listeners);
          });
        }
      };
      return Form;
    }
  ]);
