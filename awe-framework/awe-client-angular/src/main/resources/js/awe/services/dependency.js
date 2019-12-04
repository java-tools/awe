import { aweApplication } from "./../awe";

// Dependency object
aweApplication.factory('Dependency',
  ['$log', 'ActionController', 'AweUtilities', 'AweSettings', 'ServerData', 'Control', '$translate', 'Storage',
    /**
     * Dependency generic methods
     * @param {Service} $log
     * @param {Service} ActionController
     * @param {Service} Utilities
     * @param {Service} $settings
     * @param {Service} ServerData
     * @param {Service} Control
     * @param {Service} $translate
     * @param {Service} Storage
     */
    function ($log, ActionController, Utilities, $settings, ServerData, Control, $translate, Storage) {

      // Globals
      var VALUE_DEFERRED = "[[ DEFERRED ]]";
      var VALUE_NONE = "[[ NONE ]]";

      /**
       * Test a dependency value
       * @param {object} value1 First value
       * @param {object} value2 Second value
       * @param {string} condition Condition
       * @returns {object} test results
       */
      const testValue = function (value1, value2, condition) {
        var output = {};
        var strValue1 = Utilities.isEmpty(value1) ? "" : String(value1);
        var strValue2 = Utilities.isEmpty(value2) ? "" : String(value2);
        output.string = "'" + strValue1 + "' " + condition + " '" + strValue2 + "'";
        switch (condition) {
          case "eq":
            output.value = Utilities.compareEqualValues(value1, value2);
            break;
          case "ne":
            output.value = !Utilities.compareEqualValues(value1, value2);
            break;
          case "contains":
            output.value = Utilities.compareContainValues(value1, value2);
            break;
          case "not contains":
            output.value = !Utilities.compareContainValues(value1, value2);
            break;
          case "ge":
            output.value = value1 >= value2;
            break;
          case "le":
            output.value = value1 <= value2;
            break;
          case "gt":
            output.value = value1 > value2;
            break;
          case "lt":
            output.value = value1 < value2;
            break;
          case "in":
            output.value = Utilities.compareInValues(value1, value2);
            break;
          case "not in":
            output.value = !Utilities.compareInValues(value1, value2);
            break;
          case "is empty":
            output.value = Utilities.isEmpty(value1);
            output.string = "'" + strValue1 + "' " + condition;
            break;
          case "is not empty":
          default:
            output.value = !Utilities.isEmpty(value1);
            output.string = "'" + strValue1 + "' " + condition;
            break;
        }
        // Retrieve test output
        return output;
      };

      /**
       * Check a depedency condition
       * @param {Object} dependency Dependency to check
       * @param {Object} element Element to check
       * @returns {Object} Check element object
       */
      var checkElement = function (dependency, element) {
        var checkResults = {
          abort: {value: false, string: "false"},
          changed: {value: false, string: "false"},
          update: true,
          valid: true
        };
        // Define row
        var row = element.row1 || dependency.component.address.row || null;
        // Element 1
        var comp1 = element.id;
        var view1 = element.view1 || dependency.component.address.view;
        var attr1 = element.attribute1 || "value";
        var col1 = element.column1 || null;
        // Condition
        var condition = element.condition || "is not empty";
        // Comparation value
        var value = element.value || null;
        var comp2 = element.id2 || null;
        var view2 = element.view2 || dependency.component.address.view;
        var attr2 = element.attribute2 || "value";
        var col2 = element.column2 || null;
        var event = element.event || null;
        // Modifiers
        var cancel = element.cancel ? true : false;
        var optional = element.optional ? true : false;
        var checkChanges = element.checkChanges;
        var initial = dependency.initial && !dependency.alreadyLaunched ? true : false;
        var force = element.force ? true : false;
        var alias = element.alias || comp1;
        if (comp1 in Storage.get("model")[view1]) {
          // Input value
          if (comp2 !== null) {
            value = Utilities.getAttribute({view: view2, component: comp2, column: col2, row: row}, attr2);
          }

          // Event
          if (event !== null) {
            // Test value
            checkResults.condition = {
              value: element.eventLaunched || optional,
              string: "((" + event + " launched: " + element.eventLaunched + ") || " + optional + ")"
            };
            checkResults.changed.value = true;
            checkResults.changed.string = "true";
            // Set abort value
            checkResults.abort.value = (Utilities.parseBoolean(cancel) && !element.eventLaunched);
            checkResults.abort.string = "(" + cancel + " && !(" + element.eventLaunched + "))";

            // Store value
            dependency.values[alias] = element.eventLaunched;
            element.eventLaunched = false;
          } else {
            // Get component value
            var componentValue = Utilities.getAttribute({view: view1, component: comp1, column: col1, row: row}, attr1);
            // Check forced
            if (force) {
              dependency.values[alias] = null;
            }

            // Check existence
            if (!(alias in dependency.values)) {
              dependency.values[alias] = componentValue;
            }

            // Check changed
            if (((componentValue !== dependency.values[alias]) && checkChanges) || initial || event) {
              checkResults.changed.value = true;
              checkResults.changed.string = "true";
            }

            // Test value
            checkResults.condition = testValue(componentValue, value, condition);
            // Set condition
            checkResults.condition.value = checkResults.condition.value || optional;
            checkResults.condition.string = "((" + checkResults.condition.string + ") || " + optional + ")";
            // Set abort value
            checkResults.abort.value = (Utilities.parseBoolean(cancel) && !checkResults.condition.value);
            checkResults.abort.string = "(" + cancel + " && !(" + checkResults.condition.string + "))";
            // Store value
            dependency.values[alias] = componentValue;
          }
        } else {
          checkResults.update = false;
          checkResults.valid = false;
          $log.warn("[Dependency] WARNING! " + comp1 + " is not defined!");
        }

        // Retrieve condition
        return checkResults;
      };

      /**
       * Retrieve element launcher
       * @param {Object} dependency Dependency
       * @param {Object} element element
       * @param {Object} launchers Launcher list
       * @return {String} Element launcher identifier
       */
      var checkLauncher = function (dependency, element, launchers) {
        var launcherAddress = {component: element.id};
        var sameRow = true;
        if ("column1" in element && "attribute1" in element) {
          // Store column address
          launcherAddress["column"] = element.column1;
          // Retrieve launcher row depending on attribute to check
          var view = element.view1 ? element.view1 : dependency.component.address.view;
          var apiView = Storage.get("api")[view];
          if (element.id in apiView && apiView[element.id].getAttribute) {
            switch (element.attribute1) {
              case "selectedRowValue":
                launcherAddress["row"] = apiView[element.id].getAttribute("selectedRow", element.column1);
                sameRow = "row" in dependency.component.address ? String(dependency.component.address.row) === String(launcherAddress["row"]) : true;
                break;
              case "currentRowValue":
                launcherAddress["row"] = apiView[element.id].getAttribute("currentRow", element.column1, element.row1 || dependency.component.address.row);
                break;
            }
          }
        }
        var elementLauncher = Utilities.getAddressId(launcherAddress);
        return (launchers === null || elementLauncher in launchers) && sameRow ? elementLauncher : null;
      };

      /**
       * Dependency constructor
       * @param {type} dependency
       * @param {type} component
       * @returns {Dependencies}
       */
      function Dependency(dependency, component) {
        _.assign(this, dependency);
        this.component = component;
        this.deferred = Utilities.q.defer();
        this.values = {};
        this.alreadyLaunched = false;
        this.alive = false;
        this.onCheck = null;
        return this;
      }

      // Component methods
      Dependency.prototype = {
        /* *****************************
         *  Phase 0: Launch
         * **************************** */

        /**
         * Initialize dependency values
         */
        init: function () {
          var dependency = this;
          // Store last values if not defined
          this.values = this.values || {};
          this.alreadyLaunched = false;
          this.alive = true;
          _.each(this.elements, function (element) {
            checkElement(dependency, element);
          });
        },

        /* *****************************
         *  Phase 1: Check
         * **************************** */

        /**
         * Check all elements of a dependency
         * @param {Object} launchers Dependency launchers
         * @returns {Array} Check results array
         */
        check: function (launchers) {
          var check = [];
          var abort = false;
          var dependency = this;
          // Check if dependency has launchers or not
          var launch = this.elements.length === 0;
          // Search for events (if event has not been fired, abort check)
          _.each(this.elements, function (element) {
            // First check if any of the launchers has been modified
            var elementLauncher = checkLauncher(dependency, element, launchers);
            if (elementLauncher !== null) {
              launch = true;
              // Check if a element is an event and has not been triggered
              if (element.event) {
                if (launchers === null || element.event !== launchers[elementLauncher].event) {
                  abort = true;
                  element.eventLaunched = false;
                } else if (launchers !== null && element.event === launchers[elementLauncher].event) {
                  element.eventLaunched = true;
                }
              }
            }
          });
          // Update launch with abort
          launch = launch && !abort;
          // Check launchers (if not abort)
          if (launch) {
            _.each(this.elements, function (element) {
              var checkedElement = checkElement(dependency, element);
              if (checkedElement.valid) {
                check.push(checkedElement);
              }
            });
          }

          // Retrieve elements and launch condition
          return {elements: check, launch: launch};
        },

        /**
         * Evaluate if a condition has been achieved
         * @param {Array} condition Condition to check
         * @param {String} launcherAddress Launcher address
         * @returns {Boolean} Condition result
         */
        evaluate: function (condition, launcherAddress) {
          // Generate test object
          var test = {
            changed: {value: false, string: "false"},
            abort: {value: false, string: "false"},
            condition: {value: true, string: "true", initial: true},
            update: true,
            valid: false
          };

          // Evaluate condition
          for (var i = 0, t = condition.length; i < t; i++) {
            var testElement = condition[i];
            // Append condition to other conditions
            if (test.condition.initial) {
              test.condition.value = testElement.condition.value;
              test.condition.string = testElement.condition.string;
              test.condition.initial = false;
            } else {
              if (this.type.toLowerCase() === "or") {
                test.condition.value = test.condition.value || testElement.condition.value;
                test.condition.string += " || " + testElement.condition.string;
              } else {
                test.condition.value = test.condition.value && testElement.condition.value;
                test.condition.string += " && " + testElement.condition.string;
              }
            }

            // Set abort value
            test.abort.value = test.abort.value || testElement.abort.value;
            test.abort.string += " || " + testElement.abort.string;
            // Set changed value
            test.changed.value = test.changed.value || testElement.changed.value;
            test.changed.string += " || " + testElement.changed.string;
            // Set update value
            test.update = test.update && testElement.update;
          }

          // Get changed condition
          test.changed.value = test.changed.value || (condition.length === 0);
          test.changed.string += " || (" + condition.length + " === 0)";
          // Check if test is valid
          test.valid = !test.abort.value && test.changed.value;
          // If initial, remove initial flag
          if (this.initial) {
            this.alreadyLaunched = true;
          }

          // Check conditions if update is not false
          if (test.update) {
            test.update = test.condition.value;
          }

          // Check if invert
          if (this.invert) {
            test.update = !test.update;
          }

          // Store update
          this.update = test.update;
          // Log launch dependency
          if (test.valid) {
            $log.debug("[Dependency '" + launcherAddress + " (" + this.index + ")'] Check dependency", {
              condition: test.condition.value + ": [" + test.condition.string + "]",
              result: test.update,
              abort: test.abort.value + ": [" + test.abort.string + "]",
              changed: test.changed.value + ": [" + test.changed.string + "]",
              dependency: this
            });
          }

          // Return test results
          return test;
        },

        /* *****************************
         *  Phase 2: Execution
         * **************************** */

        /**
         * Execute dependency
         * @param {Object} condition Condition object
         */
        execute: function (condition) {
          var target = this.target || "none";
          var values = _.cloneDeep(this.values || {});
          var force;
          // Init dependency promise
          this.deferred = Utilities.q.defer();
          /* Check force by target */
          switch (target) {
            /* Do not force update */
            case "label":
            case "columnLabel":
            case "unit":
            case "specific":
            case "input":
              force = false;
              break;
              /* Force update */
            default:
              force = true;
              break;
          }

          // Retrieve dependency source
          var value = this.retrieveSource(values, condition.update, force);
          // Set target dependency if value has been defined
          switch (value) {
            case VALUE_DEFERRED:
              // on deferred wait till dependency is closed by action
              break;
            case VALUE_NONE:
              // no update, close dependency
              this.finish();
              break;
            default:
              this.applyTarget(value, condition.update);
          }

          // Return promise
          return this.deferred.promise;
        },
        /**
         * Retrieve dependency source
         * @param {Object} dependency Dependency
         * @param {String}  values Dependency values
         * @param {Boolean} update Condition updated
         * @param {Boolean} force Force check
         */
        retrieveSource: function (values, update, force) {
          var source = this.source || "none";
          var target = this.target || "none";
          var value = VALUE_NONE;
          // Search for source
          switch (source) {
            // Update model with query output
            case "query":
              if (update) {
                switch (target) {
                  case "label":
                    values.controllerAttribute = "label";
                    values.type = "update-controller";
                    break;
                  case "unit":
                    values.controllerAttribute = "unit";
                    values.type = "update-controller";
                    break;
                  case "format-number":
                    values.controllerAttribute = "numberFormat";
                    values.type = "update-controller";
                    break;
                  case "validate":
                    values.controllerAttribute = "validation";
                    values.type = "update-controller";
                    break;
                  case "input":
                  default:
                    values.type = this[$settings.get("serverActionKey")];
                }

                // Add component identifier and target action
                values.componentId = this.component.id;
                values[$settings.get("targetActionKey")] = this[$settings.get("targetActionKey")];
                // Launch end dependency
                var endDependency = {type: 'end-dependency',
                  parameters: {
                    dependency: this
                  }
                };
                // Generate server action
                var serverAction = ServerData.getServerAction(this.component.address, values, this.async, this.silent);
                // Launch action list
                ActionController.addActionList([serverAction, endDependency], true, {address: this.component.address, context: this.component.context});
                value = VALUE_DEFERRED;
              } else {
                switch (target) {
                  case "format-number":
                    // Restore numberFormat
                    Control.restoreControllerAttribute(this.component.address, "numberFormat");
                    break;
                  case "validate":
                    // Restore validation
                    Control.restoreControllerAttribute(this.component.address, "validation");
                    break;
                  default:
                }
              }
              break;
              // Update value with criteria value
            case "criteria-value":
            case "criteria-text":
            case "launcher":
              if (update || force) {
                value = this.values[this.query];
              }
              break;
              // Update value with plain text
            case "value":
              if (update || force) {
                value = this.value;
              }
              break;
              // Update value with label text
            case "label":
              if (update || force) {
                value = $translate.instant(this.label);
              }
              break;
              // Update value with formule
            case "formule":
              if (update || force) {
                value = Utilities.formule(this.formule, this.values);
              }
              break;
              // Reset value
            case "reset":
              if (update || force) {
                value = null;
              }
              break;
              // Update value without values
            default:
              value = update;
              break;
          }

          return value;
        },
        /**
         * Apply target type
         * @param {String}  value Target value
         * @param {Boolean} update Condition updated
         */
        applyTarget: function (value, update) {
          var target = this.target || "none";
          switch (target) {
            case "label":
              if (update) {
                Control.changeControllerAttribute(this.component.address, {label: value});
              }
              break;
            case "unit":
              if (update) {
                Control.changeControllerAttribute(this.component.address, {unit: value});
              }
              break;
            case "icon":
              if (update) {
                Control.changeControllerAttribute(this.component.address, {icon: value});
              }
              break;
            case "format-number":
              if (update) {
                Control.changeControllerAttribute(this.component.address, {numberFormat: value});
              } else {
                Control.restoreControllerAttribute(this.component.address, "numberFormat");
              }
              break;
            case "chart-options":
              if (update) {
                Control.changeControllerAttribute(this.component.address, {chartOptions: value});
              }
              break;
            case "validate":
              Control.launchApiMethod(this.component.address, "changeValidation", [value, update]);
              break;
            case "set-required":
              Control.changeControllerAttribute(this.component.address, {required: update});
              Control.launchApiMethod(this.component.address, "changeValidation", ["required", update]);
              break;
            case "set-optional":
              Control.changeControllerAttribute(this.component.address, {required: !update});
              Control.launchApiMethod(this.component.address, "changeValidation", ["required", !update]);
              break;
            case "show":
              Control.changeControllerAttribute(this.component.address, {visible: update});
              break;
            case "hide":
              Control.changeControllerAttribute(this.component.address, {visible: !update});
              break;
            case "set-invisible":
              Control.changeControllerAttribute(this.component.address, {invisible: update});
              break;
            case "set-visible":
              Control.changeControllerAttribute(this.component.address, {invisible: !update});
              break;
            case "disable":
              Control.changeControllerAttribute(this.component.address, {disabled: update});
              break;
            case "enable":
              Control.changeControllerAttribute(this.component.address, {disabled: !update});
              break;
            case "set-readonly":
              Control.changeControllerAttribute(this.component.address, {readonly: update});
              break;
            case "set-editable":
              Control.changeControllerAttribute(this.component.address, {readonly: !update});
              break;
            case "enable-autorefresh":
              Control.launchApiMethod(this.component.address, "toggleAutorefresh", [value, update]);
              break;
            case "disable-autorefresh":
              Control.launchApiMethod(this.component.address, "toggleAutorefresh", [value, !update]);
              break;
            case "attribute":
              if (update) {
                var change = {};
                change[this.query] = value;
                Control.changeControllerAttribute(this.component.address, change);
              }
              break;
            case "input":
              if (update) {
                Control.changeModelAttribute(this.component.address, {selected: value}, true);
              }
              break;
            case "none":
              break;
            default:
              Control.launchApiMethod(this.component.address, "applyDependency", [this, value, update]);
              break;
          }

          // Launch action list if update
          if (this.actions.length > 0 && update) {
            ActionController.addActionList(this.actions, true, {address: this.component.address, context: this.component.context});
          }

          // Finish dependency
          this.finish();
        },
        /**
         * Add check on finish
         * @param {object} address Address to check
         */
        addCheck: function (checkFunction) {
          this.onCheck = checkFunction;
        },
        /**
         * Check if dependency is alive
         */
        isAlive: function () {
          return this.alive;
        },
        /**
         * Check dependency component address
         * @param {object} address Address to check
         */
        belongsTo: function (address, option) {
          return _.isEqual(this.component.address, address) && this.component.controller.optionId === option;
        },
        /**
         * Finish dependency
         */
        finish: function () {
          if (this.onCheck !== null) {
            this.onCheck.apply(this);
            this.onCheck = null;
          }
          this.deferred.resolve();
        },
        /**
         * Remove dependency
         */
        destroy: function () {
          this.finish();
          this.alive = false;
        }
      };
      return Dependency;
    }
  ]);
