import { aweApplication } from "./../awe";

// Dependency controller
aweApplication.service('DependencyController',
  ['$log', 'AweUtilities', 'Dependency',
    /**
     * Dependency generic methods
     * @param {Service} $log
     * @param {Service} Utilities
     * @param {Service} Dependency
     */
    function ($log, Utilities, Dependency) {

      // Dependencies are active
      this.activeDependencies = true;
      this.dependencies = [];
      this.queue = [];
      this.checkTimeout = null;
      this.pendingInitialization = [];

      /**
       * Queue dependency
       * @param {Array} queue Dependency queue
       * @param {Object} dependency Dependency
       * @param {Object} condition Condition object
       */
      var queueDependency = function (queue, dependency, condition) {
        queue.push({dependency: dependency, condition: condition});
      };

      /**
       * Register dependencies
       * @param {Object} dependencies Dependency list
       * @param {Object} component Dependency component
       */
      this.register = function (dependencies, component) {
        var controller = this;
        _.each(dependencies, function (dependencyValues, index) {
          dependencyValues.index = index;
          controller.dependencies.push(new Dependency(dependencyValues, component));
        });

        // Check for pending
        var pending = _.remove(this.pendingInitialization, function (address) {
          return _.isEqual(component.address, address);
        });

        // If there were pending, restart them after register
        if (pending.length > 0) {
          this.restart(component.address);
        }
      };

      /**
       * Unregister dependencies
       * @param {Object} component Dependency component
       */
      this.unregister = function (component) {
        // Filter out dependencies
        var removed = _.remove(this.dependencies, function (dependency) {
          return dependency.belongsTo(component.address, component.controller.optionId);
        });

        // Destroy removed dependencies
        this.remove(removed);
      };

      /**
       * Unregister dependencies from a view
       * @param {String} view View unloaded
       */
      this.unregisterView = function (view) {
        // Filter out dependencies
        var removed = _.remove(this.dependencies, function (dependency) {
          return dependency.component.address.view === view;
        });

        if (removed.length > 0) {
          $log.debug("[Dependencies] Removing " + removed.length + " dependencies of view", view);

          // Destroy removed dependencies
          this.remove(removed);
        }
      };

      /**
       * Remove dependencies
       * @param {Array} dependencies Dependencies to remove
       */
      this.remove = function (dependencies) {
        // Destroy removed dependencies
        _.each(dependencies, function (dependency) {
          dependency.destroy();
        });
      };

      /* *****************************
       *  Phase 1: Check
       * **************************** */

      /**
       * Initialize dependency values
       */
      this.initializeValues = function (dependencies) {
        _.each(dependencies, function (dependency) {
          dependency.init();
        });
      };
      /**
       * Check and launch dependencies
       * @param {Object} launchers Launchers object
       */
      this.checkAndLaunch = function (launchers) {
        this.check(this.dependencies, launchers, null)
      };
      /**
       * Check and launch dependency
       * @param {Object} dependencies Dependencies to check
       * @param {Object} launchers Launchers object
       */
      this.check = function (dependencies, launchers, onCheck) {
        var controller = this;

        // For each dependency, check if it must be launched
        _.each(dependencies, function (dependency) {
          if (dependency.isAlive()) {
            // Check if one of the dependency launchers is the current component
            var check = dependency.check(launchers);
            var queued = false;

            // Check dependencies if dependencies are active
            if (check.launch && controller.activeDependencies) {

              // Dependency launcher belongs to the component. Check dependency condition
              var condition = dependency.evaluate(check.elements, Utilities.getAddressId(dependency.component.address));
              if (condition.valid) {

                // Dependency must be launched, enqueue it
                if (onCheck !== null) {
                  dependency.addCheck(onCheck);
                }
                queued = true;
                queueDependency(controller.queue, dependency, condition);
              }
            }

            if (onCheck !== null && !queued) {
              onCheck.apply(dependency);
            }
          }
        });
        // Launch queued dependencies
        this.runQueue();
      };
      /**
       * Initialize and check values
       */
      this.initAndCheck = function (dependencies, onCheck) {
        this.initializeValues(dependencies);
        this.check(dependencies, null, onCheck);
      };
      /**
       * Initialize the dependencies of the component if not done yet
       * @param {type} view
       */
      this.start = function (view) {
        // Filter out dependencies
        var controller = this;
        var dependencies = _.filter(this.dependencies, function (dependency) {
          return dependency.component.address.view === view;
        });

        if (dependencies.length > 0) {
          $log.debug("[Dependencies] Initializing " + dependencies.length + " dependencies of view", view);
          this.initAndCheck(dependencies, function () {
            controller.initialize(this.component);
          });
        }
      };

      /**
       * Initialize the dependencies of the component
       * @param {type} address grid address
       */
      this.restart = function (address) {
        // Filter out dependencies
        var dependencies = _.filter(this.dependencies, function (dependency) {
          return dependency.belongsTo(address, dependency.component.controller.optionId);
        });

        // If there are dependencies, reinitialize them
        if (dependencies.length > 0) {
          $log.debug("[Dependencies] Initializing dependencies of component", {address: address});
          this.initAndCheck(dependencies, null);
        } else {
          // Store address for reinitialization
          this.pendingInitialization.push(address);
        }
      };

      /**
       * Initialize component
       * @param {Object} component component
       */
      this.initialize = function (component) {
        Utilities.publish("dependencies-initialized-" + component.address.view, component.address);
      };

      /* *****************************
       *  Phase 2: Execution
       * **************************** */

      /**
       * Run dependency queue
       */
      this.runQueue = function () {
        var controller = this;
        // Check if queue has elements
        if (this.queue.length > 0) {
          // $log.debug("Queue running - Length: " + queue.length)
          var next = this.queue.shift();
          // Run the dependency
          next.dependency.execute(next.condition)
            .finally(function () {
              // Accept
              controller.runQueue();
            });
        }
      };

      /**
       * Enable / disable dependencies
       * @param {boolean} active Dependencies are active
       */
      this.toggleDependencies = function (active) {
        this.activeDependencies = active;
      };

      /**
       * Finish dependency
       * @param {Object} dependency Dependency
       * @param {Object} action Finish action
       */
      this.finishDependency = function (dependency, action) {
        // Finish dependency
        dependency.finish();

        // Close action
        action.accept();
      };
    }
  ]);
