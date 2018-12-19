import { aweApplication } from "./../awe";

// Action controller
aweApplication.service('ActionController',
  ['Storage', 'AweUtilities', 'Action',
    function (Storage, Utilities, Action) {

      // Action number
      var actionNumber = 0;

      // Action stack initialization
      this.actionStackList = [];
      this.asyncStackList = [];
      var currentStack = 0;
      this.actionStackList[currentStack] = [];
      var active = null;

      /**
       * Generate an action
       * @param actionData action  data
       * @return Action
       */
      this.generateAction = function(actionData, addressData, silent, async) {
        var view = addressData.address && addressData.address.view ? addressData.address.view : "base";
        var context = actionData.context || addressData.context || "";
        var actionTarget = addressData.address;
        if ("address" in actionData) {
          actionTarget = actionData.address;
        } else if ("target" in actionData) {
          actionTarget = {view: actionData.context || view, component: actionData.target};
        }

        // Update actionData with extra parameters
        var newAction = _.cloneDeep(actionData);
        newAction["actionId"] = "action-" + actionNumber++;
        newAction["callbackTarget"] = actionTarget;
        newAction["silent"] = actionData.silent || silent || false;
        newAction["async"] = actionData.async || async || false;
        newAction["context"] = context;
        newAction["view"] = view;
        return new Action(newAction);
      }

      /**
       * Launchs an action list
       * @param {Object} data Action list
       * @param {Boolean} addAtBottom Add the action at the bottom of the pile
       * @param {Object} addressData Launcher address and context
       * @public
       */
      this.addActionList = function (data, addAtBottom, addressData) {
        // Get self instance
        var _self = this;

        // Variable definition
        var controller = this;
        var stack = this.actionStackList[currentStack];
        var actionList = [];
        var firstAction = true;

        // If data is empty, exit
        if (Utilities.isNull(data)) {
          return;
        }

        // For each action,
        _.each(data, function (actionData) {
          var action = actionData;
          if (!(actionData instanceof Action)) {
            action = _self.generateAction(actionData, addressData, data.silent, data.async);
          }

          // Store action or launch it if it's asynchronous
          if (action.attr("async") && firstAction) {
            // Store action in async list
            controller.asyncStackList.push(action);
            // Run the action
            controller.runAction(action);
          } else {
            actionList.push(action);
            firstAction = false;
          }
        });

        if (!addAtBottom) {
          this.actionStackList[currentStack] = _.concat(actionList, stack);
        } else {
          this.actionStackList[currentStack] = _.concat(stack, actionList);
        }

        // Run first action
        this.runNext();

        // Return promise which tells if queue is empty
        active = active === null ? Utilities.q.defer() : active;
        return active.promise;
      };

      /**
       * Launches the last stack action and removes it from the stack
       * @public
       */
      this.runNext = function () {
        // Variable definition
        var stack = this.actionStackList[currentStack];
        if (stack.length === 0) {
          this.enableButtons();
        } else {
          // Retrieve and launch action
          var action = stack[0];
          if (!action.attr("running")) {
            this.runAction(action);
          }

          // If action is async, move the action to the other stack and call next action
          if (action.attr("async")) {
            // Store action in async list
            this.asyncStackList.push(action);
            // Remove from stack
            stack.splice(0, 1);
            // Call next action
            this.runNext();
          }
        }
      };

      /**
       * Finishes an action
       * @public
       * @param {String} actionId Action identifier
       */
      this.closeAction = function (actionId) {
        // Variable definition
        var searchStacks = [];
        var currentSyncStack = this.actionStackList[currentStack];

        // Add stacks to search stack
        searchStacks.push(this.asyncStackList);
        searchStacks.push(currentSyncStack);

        // Search in stacks
        _.each(searchStacks, function (stack) {

          // Search action in stack
          var toBeRemoved = _.remove(stack, function (action) {
            return action.attr("actionId") === actionId;
          });

          // Destroy found actions
          _.map(toBeRemoved, function(action) {
            action.destroy();
          });
        });

        if (currentSyncStack.length === 0) {
          // Enable all buttons
          this.enableButtons();
        }
      };

      /**
       * Relaunch last action and continue with stack
       * @public
       */
      this.relaunch = function () {
        this.runNext();
      };

      /**
       * Deletes the action stack removing all actions
       * @public
       */
      this.deleteStack = function () {
        var stack = this.actionStackList[currentStack];
        for (var i = 0, t = stack.length; i < t; i++) {
          stack[i].destroy();
          stack[i] = null;
        }
        this.actionStackList[currentStack] = [];
        this.enableButtons();
      };

      /**
       * Finishes all actions
       * @public
       */
      this.closeAllActions = function () {
        // Variable definition
        var searchStacks = [];
        var rejectStack = [];

        // Retrieve all actions to be rejected
        _.each(this.actionStackList, function(stack) {
         _.concat(rejectStack, stack);
          stack = [];
        });

        // Add also async actions
        _.concat(rejectStack, this.asyncStackList);

        // Empty stacks
        this.actionStackList[currentStack] = [];
        this.asyncStackList = [];

        // Search action in stack
        _.each(searchStacks, function (action) {
          // Reject action
          action.cancel();
        });

        // Enable all buttons
        this.enableButtons();
      };

      /**
       * Enables all buttons in screen if actions stack is empty
       * @public
       */
      this.enableButtons = function () {
        // Set actions running to false
        Utilities.timeout(function () {
          Storage.put("actions-running", false);
        });

        // Resolve active
        if (active !== null) {
          active.resolve();
          active = null;
        }
      };

      /**
       * Disables all buttons in screen if actions stack is not empty
       */
      this.disableButtons = function () {
        Storage.put("actions-running", true);
        Utilities.publish("disable-buttons");
      };

      /**
       * Launches the action
       * @param {Action} action Action to launch
       * @public
       */
      this.runAction = function (action) {
        // Get self instance
        var _self = this;

        // Disable all buttons
        if (!action.attr("silent")) {
          this.disableButtons();
        }

        // Run the action
        action.run()
          .then(function () {
            //Accept
            _self.closeAction(action.attr("actionId"));
            _self.runNext();
          }, function () {
            //Reject
            _self.deleteStack();
          });
      };

      /**
       * Adds a new stack to separate the stack context
       * @public
       */
      this.addStack = function () {
        // Variable definition
        currentStack++;

        // Generate stack
        this.actionStackList[currentStack] = [];

        // Run new stack next option (should be empty)
        this.runNext();
      };

      /**
       * Removes last stack
       * @public
       */
      this.removeStack = function () {
        // Retrieve pending actions
        var previousStack = currentStack;
        var pendingActions = this.actionStackList[previousStack];

        // Change stack number
        var nextStack = Math.max(0, currentStack - 1);

        // Set new stack number
        currentStack = nextStack;

        // Concat pending actions
        var currentActionList = this.actionStackList[currentStack];
        this.actionStackList[currentStack] = pendingActions.concat(currentActionList);

        // Delete previous stack
        this.actionStackList[previousStack] = [];
      };
      /**
       * Send a message
       * @param {Object} $scope Component scope
       * @param {String} type Message type
       * @param {String} title Message title
       * @param {String} content Message content
       */
      this.sendMessage = function ($scope, type, title, content) {
        // Send message
        var messageAction = {type: 'message', silent: false};
        // Add message to action
        messageAction.parameters = {
          type: type,
          title: title,
          message: content
        };
        // Send action send message
        this.addActionList([messageAction], false, {address: {view: $scope.view}, context: $scope.context});
      };
    }
  ]);
