import {aweApplication} from "./../awe";
import {DefaultSpin} from "./../data/options";

// Criterion service
aweApplication.factory('Criterion',
  ['Component', 'AweUtilities', 'Control', 'AweSettings', 'Validator',
    /**
     * Criterion generic methods
     * @constructor Generic Component constructor
     * @param {object} Component
     * @param {object} Utilities Awe Utilities
     * @param {object} Control Controller service
     * @param {object} $settings Awe $settings
     * @param {object} Validator Validator service
     */
    function (Component, Utilities, Control, $settings, Validator) {


      /**
       * @constructor Criterion constructor
       * @param {Scope} scope Criterion scope
       * @param {String} id Criterion id
       * @param {String} element Criterion element
       */
      function Criterion(scope, id, element) {
        if (!("criterion" in scope)) {
          scope.criterion = this;
        }
        this.scope = scope;
        this.id = id;
        // Define attribute methods
        this.attributeMethods = {
          /**
           * Retrieve text value
           * @param {object} component Component scope
           * @returns {mixed} Current text value
           */
          text: function (component) {
            return component.getVisibleValue();
          },
          /**
           * Retrieve value
           * @param {object} component Component scope
           * @returns {mixed} Current  value
           */
          value: function (component) {
            return component.model.selected;
          },
          /**
           * Retrieve value
           * @param {object} component Component scope
           * @returns {mixed} Current  value
           */
          label: function (component) {
            return component.controller.label || "";
          },
          /**
           * Retrieve value
           * @param {object} component Component scope
           * @returns {mixed} Current  value
           */
          unit: function (component) {
            return component.controller.unit || "";
          },
          /**
           * Retrieve value
           * @param {object} component Component scope
           * @returns {mixed} Current  value
           */
          editable: function (component) {
            return component.controller.readonly;
          },
          /**
           * Retrieve value
           * @param {object} component Component scope
           * @returns {mixed} Current  value
           */
          required: function (component) {
            return component.controller.required;
          },
          /**
           * Retrieve selected values
           * @param {object} component Component scope
           * @returns {integer} Selected values length
           */
          selectedValues: function (component) {
            return Utilities.asArray(component.model.selected).length;
          }
        };

        // Generate as component
        this.component = new Component(this.scope, this.id);
        this.component.element = element;
        let criterion = this;
        this.component.asCriterion = function () {
          return criterion.init();
        };

        // Add attribute methods
        _.merge(this.component.attributeMethods, this.attributeMethods);

        return this.component;
      }
      Criterion.prototype = {
        /**
         * Initialization method
         */
        init: function () {
          // Init criterion as component
          let criterion = this;
          let component = criterion.component;
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          let controller = component.controller;
          controller.criterion = true;

          // Spin options store
          component.scope.spinOptions = component.scope.spinOptions || DefaultSpin.medium;

          /******************************************************************************
           * SCOPE METHODS
           *****************************************************************************/

          /**
           * On focus event
           */
          component.scope.focus = function () {
            component.changeFocus(!controller.readonly);
          };
          /**
           * On blur event
           */
          component.scope.blur = function () {
            component.changeFocus(false);
          };

          // If not scope defined
          if (!component.scope.click) {
            /**
             * On click event
             * @param {Object} e Event
             */
            component.scope.click = function (e) {
              component.scope.focus();
              // Cancel event propagation
              Utilities.stopPropagation(e);
            };
          }
          /**
           * On submit catch event
           * @param {event} event Submit
           */
          component.scope.submit = function (event) {
            // Cancel event propagation
            Utilities.stopPropagation(event, true);
            // Search submit button
            let form = $("form").first();
            // Step 1: Retrieve closest submit button
            let submitButton = component.element.closest(":submit:visible");
            if (submitButton.length > 0) {
              submitButton = $(submitButton[0]);
            } else {
              submitButton = form.find(":submit:visible");
              if (submitButton.length > 0) {
                submitButton = $(submitButton[0]);
              } else {
                submitButton = null;
              }
            }

            // If submit button has been found, make click on it
            if (submitButton) {
              let submitScope = submitButton.scope();
              submitScope.onClick();
            }
          };

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * Change component focus
           * @param {boolean} focus
           */
          component.changeFocus = function (focus) {
            component.focused = focus;
            let target = component.element.is(".focus-target") ? component.element : component.element.find(".focus-target");
            if (focus) {
              target.addClass('focused');
            } else {
              target.removeClass('focused');
            }
          };

          /**
           * Reset criterion
           */
          if (!component.onReset) {
            component.onReset = function () {
              Control.resetModelAttribute(component.address, "selected");
            };
          }

          /**
           * Restore criterion
           */
          if (!component.onRestore) {
            component.onRestore = function () {
              Control.restoreModelAttribute(component.address, "selected");
            };
          }

          /**
           * Restore criterion with target
           */
          if (!component.onRestoreTarget) {
            component.onRestoreTarget = function () {
              Control.restoreInitialModelAttribute(component.address, "selected");
            };
          }

          /**
           * Extra data function (To be overwritten on complex directives)
           * @returns {Object} Data from criteria
           */
          component.getData = function () {
            // Initialize data
            let data = {};
            data[component.address.component] = component.model.selected;
            if (component.getExtraData) {
              data[component.address.component + $settings.get("dataSuffix")] = component.getExtraData();
            }
            return data;
          };
          /**
           * Extra data function (To be overwritten on complex directives)
           * @returns {Object} Data from criteria
           */
          component.getPrintData = function () {
            // Initialize data
            let data = component.getData();
            if (controller.printable) {
              data[component.address.component + $settings.get("dataSuffix")] = {
                text: component.getVisibleValue()
              };
            }
            return data;
          };

          /**
           * Update criterion classes
           */
          component.updateClasses = function () {
            component.scope.criterionClass = getCriterionClass();
            component.scope.classes = getClass();
          };

          /******************************************************************************
           * PRIVATE METHODS
           *****************************************************************************/

          /**
           * Retrieve input classes (validation and input size)
           * @returns {String} Input classes
           */
          function getClass() {
            let criterionClass = [];
            // Add required
            if (controller && controller.required) {
              criterionClass.push("required");
            }

            // Add size
            if (component.scope.size) {
              criterionClass.push("input-" + component.scope.size);
            }

            // Add special class if existent
            if (component.specialClass) {
              criterionClass.push(component.specialClass);
            }

            // Add column class if existent
            if (component.columnClass) {
              criterionClass.push(component.columnClass);
            }

            return criterionClass.join(" ");
          }

          /**
           * Retrieve full criterion classes
           * @returns {String} Criterion classes
           */
          function getCriterionClass() {
            let criterionClass = [];
            if (controller) {
              if (controller.style !== "") {
                criterionClass.push(controller.style);
              }
              if (controller.hidden) {
                criterionClass.push("hidden");
              }
              if (controller.invisible) {
                criterionClass.push("invisible");
              }
            }
            return criterionClass.join(" ");
          }

          /**
           * Update icon class
           */
          function updateIconClass() {
            let icon = "criterion-icon-" + component.scope.size;
            icon += " form-icon fa fa-" + controller.icon;
            icon += ("style" in controller && controller.style.indexOf('no-label') === -1) ? " w-label" : "";
            component.scope.iconClass = icon;
          }

          /**
           * Update group classes
           */
          function updateGroupClass() {
            let group = "form-group group-" + component.scope.size;
            group += controller.icon ? " w-icon" : "";
            component.scope.groupClass = group;
          }

          /**
           * Update label classes
           */
          function updateLabelClass() {
            let label = "control-label label-" + component.scope.size;
            // Add left label
            if ("leftLabel" in controller) {
              let charLength = parseInt(controller.leftLabel, 10);
              component.scope.labelStyle = {width: (charLength * component.scope.charSize) + "px"};
              component.scope.leftLabelInput = " expand";
              label += " label-left";
            } else {
              component.scope.leftLabelInput = '';
            }
            component.scope.validatorGroup = 'input-group-' + component.scope.size + component.scope.leftLabelInput;
            component.scope.labelClass = label;
          }

          /**
           * Initialize validation rules
           */
          function initHelp() {
            // Set context menu if existing
            if (controller && "help" in controller) {
              Utilities.timeout(function () {
                // Initialize help node
                component.scope.help = {
                  node: component.element.find(".help-target"),
                  text: controller.help,
                  image: controller.helpImage
                };
                component.initHelpNode(component.scope.help);
              });
            }
          }

          // Initially update classes
          component.updateClasses();
          // Initially update icon class
          updateIconClass();
          // Initially update group class
          updateGroupClass();
          // Initially update group class
          updateLabelClass();
          // Initialize help
          initHelp();

          /******************************************************************************
           * EVENT LISTENERS
           *****************************************************************************/
          component.listeners = component.listeners || {};
          // On model change launch dependency
          component.listeners["controllerChange"] = component.scope.$on("controllerChange", function (event, parameters) {
            if (_.isEqual(parameters.address, component.address)) {
              component.updateClasses();
            }
          });
          // Capture reset action
          component.listeners['resetScope'] = component.scope.$on('reset-scope', function (event, view) {
            if (view === component.address.view) {
              component.onReset();
            }
          });
          // Capture restore action
          component.listeners['restoreScope'] = component.scope.$on('restore-scope', function (event, view) {
            if (view === component.address.view) {
              component.onRestore();
            }
          });
          // Capture restore action
          component.listeners['restoreScopeTaget'] = component.scope.$on('restore-scope-target', function (event, view) {
            if (view === component.address.view) {
              component.onRestoreTarget();
            }
          });

          /**********************************************************************/
          /* API METHODS                                                        */
          /**********************************************************************/
          component.api = component.api || {};

          /**
           * Change validation rules
           * @param {String} rule Rule to change
           * @param {boolean} add Add or remove rule
           */
          component.api.changeValidation = function (rule, add) {
            let controller = Control.getAddressController(component.address);
            Validator.initController(controller, component.address);
            let rules = controller.validationRules;
            let newRule = Validator.parseRule(rule, component.address);
            if (add) {
              _.merge(rules, newRule);
            } else {
              _.each(newRule, function (ruleValue, ruleId) {
                delete rules[ruleId];
              });
            }
            component.updateClasses();
          };

          /**
           * Validate the current criterion
           * @param {type} baseNode
           * @param {type} errorsDetected
           * @return {boolean} Return if criterion is valid or not
           */
          component.api.validate = function (baseNode, errorsDetected) {
            // Check if baseNode contains the current node
            let validation = null;
            let $baseNode = baseNode ? $(baseNode) : $(component.element);
            if ($baseNode.is(component.element) || $baseNode.find(component.element).length > 0) {
              let controller = Control.getAddressController(component.address);
              let model = Control.getAddressModel(component.address);
              Validator.initController(controller, component.address);
              validation = Validator.validate(model.selected, controller.validationRules, component.element, component.address);
              if (validation !== null) {
                // Add validation error class
                component.scope.criterionClass += " has-error dark";
                if (!errorsDetected) {
                  // Select the tab of the element (if it's in a tab)
                  component.scope.$emit("select-current-tab");
                  component.scope.$emit("show-validation-error", validation);
                }
              } else {
                component.updateClasses();
              }
            }
            return validation;
          };

          // Initialization well done
          return true;
        }
      };
      return Criterion;
    }
  ]);
