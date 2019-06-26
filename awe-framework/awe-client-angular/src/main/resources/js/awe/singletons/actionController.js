import {aweApplication} from "./../awe";
import _ from "lodash";

// Action controller
aweApplication.service('ActionController',
  ['Storage', 'AweUtilities', 'Action',
    function (Storage, $utilities, Action) {

      // Define controller
      let $ctrl = this;

      // Action number
      $ctrl.actionNumber = 0;

      // Action stack initialization
      $ctrl.actionStackList = [];
      $ctrl.asyncStackList = [];
      $ctrl.currentStack = 0;
      $ctrl.actionStackList[$ctrl.currentStack] = [];
      $ctrl.active = null;

      /**
       * Generate an action
       * @param actionData action  data
       * @return Action
       */
      $ctrl.generateAction = function (actionData, addressData, silent, async) {
        let view = addressData.address && addressData.address.view ? addressData.address.view : "base";
        let actionTarget = addressData.address;
        if ("address" in actionData) {
          actionTarget = actionData.address;
        } else if ("target" in actionData) {
          actionTarget = {view: actionData.context || view, component: actionData.target};
        }

        // Update actionData with extra parameters
        return new Action({
          ...actionData,
          actionId: "action-" + $ctrl.actionNumber++,
          callbackTarget: actionTarget,
          silent: actionData.silent || silent || false,
          async: actionData.async || async || false,
          context: actionData.context || addressData.context || "",
          view: view
        });
      };

      /**
       * Launchs an action list
       * @param {Object} data Action list
       * @param {Boolean} addAtBottom Add the action at the bottom of the pile
       * @param {Object} addressData Launcher address and context
       * @public
       */
      $ctrl.addActionList = function (data, addAtBottom, addressData) {

        // Variable definition
        let stack = $ctrl.actionStackList[$ctrl.currentStack];
        let actionList = [];
        let firstAction = true;

        // If data is empty, exit
        if ($utilities.isNull(data)) {
          return;
        }

        // For each action,
        _.each(data, function (actionData) {
          let action = actionData;
          if (!(actionData instanceof Action)) {
            action = $ctrl.generateAction(actionData, addressData, data.silent, data.async);
          }

          // Store action or launch it if it's asynchronous
          if (action.attr("async") && firstAction) {
            // Store action in async list
            $ctrl.asyncStackList.push(action);
            // Run the action
            $ctrl.runAction(action);
          } else {
            actionList.push(action);
            firstAction = false;
          }
        });

        if (!addAtBottom) {
          $ctrl.actionStackList[$ctrl.currentStack] = [...actionList, ...stack];
        } else {
          $ctrl.actionStackList[$ctrl.currentStack] = [...stack, ...actionList];
        }

        // Run first action
        $ctrl.runNext();

        // Return promise which tells if queue is empty
        $ctrl.active = $ctrl.active === null ? $utilities.q.defer() : $ctrl.active;
        return $ctrl.active.promise;
      };

      /**
       * Launches the last stack action and removes it from the stack
       * @public
       */
      $ctrl.runNext = function () {
        // Variable definition
        var stack = $ctrl.actionStackList[$ctrl.currentStack];
        if (stack.length === 0) {
          $ctrl.enableButtons();
        } else {
          // Retrieve and launch action
          var action = stack[0];
          if (!action.attr("running")) {
            $ctrl.runAction(action);
          }

          // If action is async, move the action to the other stack and call next action
          if (action.attr("async")) {
            // Store action in async list
            $ctrl.asyncStackList.push(action);
            // Remove from stack
            stack.splice(0, 1);
            // Call next action
            $ctrl.runNext();
          }
        }
      };

      /**
       * Finishes an action
       * @public
       * @param {String} actionId Action identifier
       */
      $ctrl.closeAction = function (actionId) {
        // Variable definition
        var searchStacks = [];
        var currentSyncStack = $ctrl.actionStackList[$ctrl.currentStack];

        // Add stacks to search stack
        searchStacks.push($ctrl.asyncStackList);
        searchStacks.push(currentSyncStack);

        // Search in stacks
        _.each(searchStacks, function (stack) {

          // Search action in stack
          var toBeRemoved = _.remove(stack, function (action) {
            return action.attr("actionId") === actionId;
          });

          // Destroy found actions
          _.map(toBeRemoved, function (action) {
            action.destroy();
          });
        });

        if (currentSyncStack.length === 0) {
          // Enable all buttons
          $ctrl.enableButtons();
        }
      };

      /**
       * Relaunch last action and continue with stack
       * @public
       */
      $ctrl.relaunch = function () {
        $ctrl.runNext();
      };

      /**
       * Deletes the action stack removing all actions
       * @public
       */
      $ctrl.deleteStack = function () {
        var stack = $ctrl.actionStackList[$ctrl.currentStack];
        for (var i = 0, t = stack.length; i < t; i++) {
          stack[i].destroy();
          stack[i] = null;
        }
        $ctrl.actionStackList[$ctrl.currentStack] = [];
        $ctrl.enableButtons();
      };

      /**
       * Finishes all actions
       * @public
       */
      $ctrl.closeAllActions = function () {
        // Variable definition
        let searchStacks = [];
        let rejectStack = [];

        // Retrieve all actions to be rejected
        _.each($ctrl.actionStackList, function (stack) {
          rejectStack = [...rejectStack, ...stack];
        });

        // Add also async actions
        rejectStack = [...rejectStack, ...$ctrl.asyncStackList];

        // Empty stacks
        $ctrl.actionStackList[$ctrl.currentStack] = [];
        $ctrl.asyncStackList = [];

        // Search action in stack
        _.each(searchStacks, function (action) {
          // Reject action
          action.cancel();
        });

        // Enable all buttons
        $ctrl.enableButtons();
      };

      /**
       * Enables all buttons in screen if actions stack is empty
       * @public
       */
      $ctrl.enableButtons = function () {
        // Set actions running to false
        $utilities.timeout(function () {
          Storage.put("actions-running", false);
        });

        // Resolve active
        if ($ctrl.active !== null) {
          $ctrl.active.resolve();
          $ctrl.active = null;
        }
      };

      /**
       * Disables all buttons in screen if actions stack is not empty
       */
      $ctrl.disableButtons = function () {
        Storage.put("actions-running", true);
        $utilities.publish("disable-buttons");
      };

      /**
       * Launches the action
       * @param {Action} action Action to launch
       * @public
       */
      $ctrl.runAction = function (action) {
        // Disable all buttons
        if (!action.attr("silent")) {
          $ctrl.disableButtons();
        }

        // Run the action
        action.run()
          .then(function () {
            //Accept
            $ctrl.closeAction(action.attr("actionId"));
            $ctrl.runNext();
          }, function () {
            //Reject
            $ctrl.deleteStack();
          });
      };

      /**
       * Adds a new stack to separate the stack context
       * @public
       */
      $ctrl.addStack = function () {
        // Variable definition
        $ctrl.currentStack++;

        // Generate stack
        $ctrl.actionStackList[$ctrl.currentStack] = [];

        // Run new stack next option (should be empty)
        $ctrl.runNext();
      };

      /**
       * Removes last stack
       * @public
       */
      $ctrl.removeStack = function () {
        // Retrieve pending actions
        var previousStack = $ctrl.currentStack;
        var pendingActions = $ctrl.actionStackList[previousStack];

        // Change stack number
        var nextStack = Math.max(0, $ctrl.currentStack - 1);

        // Set new stack number
        $ctrl.currentStack = nextStack;

        // Concat pending actions
        var currentActionList = $ctrl.actionStackList[$ctrl.currentStack];
        $ctrl.actionStackList[$ctrl.currentStack] = pendingActions.concat(currentActionList);

        // Delete previous stack
        $ctrl.actionStackList[previousStack] = [];
      };
      /**
       * Send a message
       * @param {Object} $scope Component scope
       * @param {String} type Message type
       * @param {String} title Message title
       * @param {String} content Message content
       */
      $ctrl.sendMessage = function ($scope, type, title, content) {
        // Send message
        let messageAction = {type: 'message', silent: false};
        // Add message to action
        messageAction.parameters = {
          type: type,
          title: title,
          message: content
        };
        // Send action send message
        $ctrl.addActionList([messageAction], false, {address: {view: $scope.view}, context: $scope.context});
      };

      /**
       * Finish an action
       * @param {Object} action Action
       * @param {boolean} accept Accept action or not
       */
      $ctrl.finishAction = function (action, accept) {
        if (accept) {
          action.accept();
        } else {
          action.reject();
        }
      };

      /**
       * Accept an action
       * @param {Object} action Action
       */
      $ctrl.acceptAction = function (action) {
        action.accept();
      };

      /**
       * Reject an action
       * @param {Object} action Action
       */
      $ctrl.rejectAction = function (action) {
        action.reject();
      };

      /**
       * Abort an action
       * @param {Object} action Action
       */
      $ctrl.abortAction = function (action) {
        action.abort();
      };

      /**
       * Define a list of action listeners
       * @param {type} listeners
       * @param {type} actions
       * @param {type} scope
       * @param {type} executor
       */
      $ctrl.defineActionListeners = function (listeners, actions, scope, executor) {
        _.each(actions, function (actionOptions, actionId) {
          listeners[actionId] = scope.$on("/action/" + actionId, function (event, action) {
            actionOptions["service"] = executor;
            actionOptions["scope"] = scope;
            $ctrl.resolveAction(action, actionOptions);
          });
        });
      };

      /**
       * Resolve the action if matches
       * @param {type} action
       * @param {object} parameters
       */
      $ctrl.resolveAction = function (action, parameters) {
        let scope = parameters.scope || {};
        let component = scope.component || {};
        let address = component.address || {};
        let check = parameters.check || false;

        // Launch action
        if ($utilities.checkAddress(action.attr("callbackTarget"), address, check)) {
          // Launch method
          parameters.service[parameters.method](action.attr("parameters"), scope, action.attr("callbackTarget"));

          // Finish action
          $ctrl.acceptAction(action);
        }
      };
    }
  ]);
