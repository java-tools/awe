import {aweApplication} from "./../awe";
import moment from "moment";

// Validation rules service
aweApplication.factory('ValidationRules',
  ['Control', 'AweSettings', '$translate', 'AweUtilities',
    function ($control, $settings, $translate, $utilities) {
      // Retrieve default $settings
      let patterns = {
        TEXT: /^[A-Za-z]+$/,
        TEXT_WHITESPACES: /^([a-zA-Z]+\s)+[a-zA-Z]+$/,
        NUMBER: /^-?(?:\d+|\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/,
        INTEGER: /^-?\d+$/,
        DIGITS: /^\d+$/,
        EMAIL: /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
        DATE: /^\d{1,2}\/\d{1,2}\/\d{4}$/,
        TIME: /^([0-1]\d|2[0-3]):([0-5]\d):([0-5]\d)$/
      };
      /**
       * Retrieve external parameter values
       * @param {type} rule Rule to retrieve the parameters
       * @param {type} view
       */
      let retrieveExternalParameters = function (rule, view) {
        let parameterValue = null;
        if ("value" in rule) {
          parameterValue = rule.value;
        } else if ("criterion" in rule) {
          let address = {component: rule.criterion, view: view};
          let model = $control.getAddressModel(address);
          parameterValue = model.selected;
        } else if ("setting" in rule) {
          parameterValue = $settings.get([rule["setting"]]);
        }
        return parameterValue;
      };
      /**
       * Retrieve a value or null if it is not valid
       * @param {Mixed} value
       * @param {Object} address
       * @return {Object} Group name
       */
      let getRequiredValue = function (value, address) {
        // Get controller
        let requiredValue = null;
        let controller = $control.getAddressController(address);
        // Retrieve group values (for radio buttons)
        if ("group" in controller) {
          let model = $control.getAddressModel({view: address.view, component: controller.group});
          if (model !== null) {
            requiredValue = model.selected;
          } else {
            requiredValue = value;
          }
          // Retrieve list values (for multiple)
        } else if (angular.isArray(value)) {
          if (value.length !== 0) {
            requiredValue = value;
          }
          // Retrieve single values (default)
        } else {
          requiredValue = value;
        }
        return requiredValue;
      };
      /**
       * Retrieves an array list of elements with group name
       * @param {String} address Group id
       * @return {Object} Group name
       */
      let getObjectsSelectedInGroup = function (address) {
        // Get controller
        let groupList = [];
        let controller = $control.getAddressViewController(address);

        _.each(controller, function (component, componentId) {
          if ("group" in component && component.group === address.group) {
            let model = $control.getAddressModel({view: address.view, component: componentId});
            if (model.selected) {
              groupList.push(componentId);
            }
          }
        });
        return groupList;
      };
      /**
       * Extract the parameters to compare
       * @param {type} ruleMethod rule method
       * @param {type} value Value to compare
       * @param {type} rule Rule to retrieve the parameters
       * @param {type} address
       */
      let extractParameters = function (ruleMethod, value, rule, address) {
        let parameters = {values: {value1: value}};
        if (typeof rule === "object") {
          _.merge(parameters, rule);
          if ("from" in rule && "to" in rule) {
            parameters.values.from = retrieveExternalParameters(rule["from"], address.view);
            parameters.values.to = retrieveExternalParameters(rule["to"], address.view);
          } else if ("value" in rule || "criterion" in rule || "setting" in rule) {
            parameters.values.value2 = retrieveExternalParameters(rule, address.view);
          }
        } else {
          parameters.values.value2 = rule;
        }
        // Format comparison values
        switch (parameters.type) {
          case "float":
            _.each(parameters.values, function (value, valueId) {
              parameters.values[valueId] = !$utilities.isEmpty(value) ? parseFloat(value) : null;
            });
            break;
          case "integer":
            _.each(parameters.values, function (value, valueId) {
              parameters.values[valueId] = !$utilities.isEmpty(value) ? parseInt(value, 10) : null;
            });
            break;
          case "date":
            _.each(parameters.values, function (value, valueId) {
              parameters.values[valueId] = !$utilities.isEmpty(value) ? moment(value, "DD/MM/YYYY") : null;
            });
            break;
          default:
            switch (ruleMethod) {
              case "required":
                parameters.values.value1 = getRequiredValue(value, address);
                break;
              case "checkAtLeast":
                parameters.values.value1 = getObjectsSelectedInGroup(address).length;
                parameters.values.value2 = !$utilities.isEmpty(parameters.values.value2) ? parseInt(parameters.values.value2, 10) : null;
                break;
              case "equallength":
              case "maxlength":
              case "minlength":
                parameters.values.value2 = !$utilities.isEmpty(parameters.values.value2) ? parseInt(parameters.values.value2, 10) : null;
                break;
              default:
            }
        }
        return parameters;
      };
      /**
       * Fornat a message with parameters
       * @param {type} message
       * @param {type} parameters
       * @returns {unresolved}
       */
      let formatMessage = function (message, parameters) {
        let messageFormatted = $translate.instant(message || "");
        _.each(parameters.values, function (value, key) {
          let formattedValue = value;
          if (!$utilities.isEmpty(value) && value._isAMomentObject) {
            formattedValue = formattedValue.format("DD/MM/YYYY");
          }
          messageFormatted = messageFormatted.replace("{" + key + "}", formattedValue);
        });
        return messageFormatted;
      };
      const $rules = {
        validate: function (ruleMethod, value, rule, address) {
          let parameters = extractParameters(ruleMethod, value, rule, address);
          return $rules[ruleMethod](parameters);
        },
        required: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_REQUIRED", parameters)
          };
          return $utilities.isEmpty(parameters.values.value1) && parameters.values.value2 ? error : null;
        },
        text: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_TEXT", parameters)
          };
          let passed = new RegExp(patterns.TEXT).test(String(parameters.values.value1));
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        textWithSpaces: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_TEXT_WHITESPACES", parameters)
          };
          let passed = new RegExp(patterns.TEXT_WHITESPACES).test(String(parameters.values.value1));
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        number: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_NUMBER", parameters)
          };
          let passed = new RegExp(patterns.NUMBER).test(String(parameters.values.value1));
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        integer: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_INTEGER", parameters)
          };
          let passed = new RegExp(patterns.INTEGER).test(String(parameters.values.value1));
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        digits: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_DIGITS", parameters)
          };
          let passed = new RegExp(patterns.DIGITS).test(String(parameters.values.value1));
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        email: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_EMAIL", parameters)
          };
          let passed = new RegExp(patterns.EMAIL).test(String(parameters.values.value1));
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        date: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_DATE", parameters)
          };
          let passed = new RegExp(patterns.DATE).test(String(parameters.values.value1));
          if (passed) {
            let d = moment(parameters.values.value1, 'DD/MM/YYYY');
            passed = d.isValid();
          }
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        time: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_TIME", parameters)
          };
          let passed = new RegExp(patterns.TIME).test(String(parameters.values.value1));
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        eq: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_EQUAL", parameters)
          };
          let passed = parameters.values.value1 === parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        ne: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_NOT_EQUAL", parameters)
          };
          let passed = parameters.values.value1 !== parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        lt: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_LESS_THAN", parameters)
          };
          let passed = parameters.values.value1 < parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        le: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_LESS_OR_EQUAL", parameters)
          };
          let passed = parameters.values.value1 <= parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        gt: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_GREATER_THAN", parameters)
          };
          let passed = parameters.values.value1 > parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        ge: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_GREATER_OR_EQUAL", parameters)
          };
          let passed = parameters.values.value1 >= parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        mod: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_DIVISIBLE_BY", parameters)
          };
          let passed = (parameters.values.value1 % parameters.values.value2) === 0;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        range: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_RANGE", parameters)
          };
          let passed = parameters.values.value1 >= parameters.values.from && parameters.values.value1 <= parameters.values.to;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.from) || $utilities.isEmpty(parameters.values.to) || passed ? null : error;
        },
        equallength: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_EQUAL_LENGTH", parameters)
          };
          let passed = String(parameters.values.value1).length === parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        maxlength: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_MAXLENGTH", parameters)
          };
          let passed = String(parameters.values.value1).length <= parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        minlength: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_MINLENGTH", parameters)
          };
          let passed = String(parameters.values.value1).length >= parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        pattern: function (parameters) {
          let pattern = parameters.values.value2 || $settings.get("passwordPattern");
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_PATTERN", parameters)
          };
          let passed = new RegExp(pattern).test(parameters.values.value1);
          return $utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        checkAtLeast: function (parameters) {
          let error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_CHECK_AT_LEAST", parameters)
          };
          let passed = parameters.values.value1 >= parameters.values.value2;
          return $utilities.isEmpty(parameters.values.value1) || $utilities.isEmpty(parameters.values.value2) || passed ? null : error;
        },
        invalid: function (parameters) {
          return {
            message: formatMessage(parameters.message, parameters)
          };
        }
      };
      return $rules;
    }
  ]);
