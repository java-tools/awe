import { aweApplication } from "./../awe";

// Validator service
aweApplication.factory('Validator',
  ['Storage', 'AweUtilities', 'AweSettings', 'ValidationRules', '$log',
    /**
     *
     * @param {object} Storage
     * @param {object} $utilities
     * @param {object} $settings
     * @param {object} ValidationRules
     * @param {object} $log
     */
    function (Storage, $utilities, $settings, ValidationRules, $log) {

      // CONSTANTS
      const ERROR_CONTAINER = '.error-container';
      const FORM_GROUP = '.form-group';

      let Validator = {
        /**
         * Validate a component
         * @param {Object} controller
         * @param {Object} address
         */
        initController: function (controller, address) {
          if (!("validationRules" in controller)) {
            // Parse validation rules
            controller.validationRules = Validator.parseValidationRules(controller.validation, address);
          }
        },
        /**
         * Validate a component
         * @param {Object} component
         * @param {Array} errorList
         * @param {Object} baseNode
         */
        validateComponent: function (component, errorList, baseNode) {
          let method = "validate";
          // Get data for component
          if (method in component) {
            let validationData = component[method](baseNode, errorList.length > 0);
            if (validationData !== null) {
              errorList.push(validationData);
            }
          }
        },
        /**
         * Retrieve the validation nodes
         * @param {Object} baseNode base node
         */
        validateNode: function (baseNode) {
          // Retrieve action data
          let errorList = [];
          let api = Storage.get("api");

          // Retrieve components from each view
          _.each(api, function (view) {
            _.each(view, function (component) {
              Validator.validateComponent(component, errorList, baseNode);
              if ("cells" in component) {
                _.each(component.cells, function (cell) {
                  Validator.validateComponent(cell, errorList, baseNode);
                });
              }
            });
          });
          // Return data
          return errorList;
        },
        /**
         * Validate a value with
         * @param {Mixed} value
         * @param {object} rules
         * @param {object} element
         * @param {object} address
         * @returns {Array}
         */
        validate: function (value, rules, element, address) {
          let errorMessage = null;
          _.each(rules, function (rule, ruleMethod) {
            if (errorMessage === null) {
              errorMessage = ValidationRules.validate(ruleMethod, value, rule, address);
              if (angular.isObject(errorMessage)) {
                errorMessage.element = element;
                errorMessage.id = $utilities.getAddressId(address);
              }
            }
          });
          return errorMessage;
        },
        /**
         * Retrieve the validation nodes
         * @param {type} scope
         * @param {type} error
         * @returns {undefined}
         */
        showValidationError: function (scope, error) {
          let $errorContainer = $(ERROR_CONTAINER);
          let $element = $(error.element);

          // Retrieve the element data
          $utilities.timeout.cancel(scope.errorTimer);
          $utilities.timeout(function () {
            let target = $element;
            let classTarget = $element.find(FORM_GROUP);
            let targetPosition = target.offset();
            targetPosition.top += target.height();

            // Show validation message
            $errorContainer.css(targetPosition);
            scope.validationStyle = classTarget.length > 0 ? classTarget.attr("class") : "";
            scope.validationMessage = error.message;
            $log.info("[VALIDATION] '" + error.id + "' - " + scope.validationMessage);
            scope.showValidation = true;

            // Scroll to element
            let scrollable = window;
            let scrollableNodes = $element.parentsUntil(".scrollable");
            if (scrollableNodes.length > 0) {
              scrollable = scrollableNodes[0];
            }
            scrollable.scrollTo(targetPosition.left, targetPosition.to - target.height());

            // Hide validation after 2 seconds
            scope.errorTimer = $utilities.timeout(function () {
              scope.showValidation = false;
            }, $settings.get("messageTimeout").validate);
          });
        },
        /**
         * Retrieve the validation nodes
         * @param {type} validationRules
         * @param {type} address
         * @returns {undefined}
         */
        parseValidationRules: function (validationRules, address) {
          let rulesParsed = {};
          if (validationRules) {
            if (angular.isObject(validationRules)) {
              rulesParsed = validationRules;
            } else {
              let rulesList = validationRules.indexOf("{") > -1 ? [validationRules] : validationRules.split(" ");
              rulesParsed = rulesList.reduce((parsed, rule) => ({...parsed, ...Validator.parseRule(rule, address)}), {});
            }
          }
          return rulesParsed;
        },
        /**
         * Retrieve the validation nodes
         * @param {string|object} rule
         * @param {object} address
         * @returns {object} Rule as object
         */
        parseRule: function (rule, address) {
          let newRule;
          if (angular.isObject(rule)) {
            newRule = rule;
          } else if (rule.indexOf("{") > -1) {
            try {
              newRule = $utilities.evalJSON(rule);
            } catch (exc) {
              $log.error("[ERROR] Parsing validation rule to JSON", {rule: rule, address: address, exception: exc});
            }
          } else {
            newRule = {[rule]: true};
          }
          return {...newRule};
        }
      };
      return Validator;
    }
  ]);
