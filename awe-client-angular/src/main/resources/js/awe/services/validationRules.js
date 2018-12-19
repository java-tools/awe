import { aweApplication } from "./../awe";

// Validation rules service
aweApplication.factory('ValidationRules',
  ['Control', 'AweSettings', '$translate', 'AweUtilities',
    function (Control, $settings, $translate, Utilities) {
      // Retrieve default $settings
      var patterns = {
        TEXT: /^[A-Za-z]+$/,
        TEXT_WHITESPACES: /^([a-zA-Z]+\s)*[a-zA-Z]+$/,
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
      var retrieveExternalParameters = function (rule, view) {
        var parameterValue = null;
        if ("value" in rule) {
          parameterValue = rule.value;
        } else if ("criterion" in rule) {
          var address = {component: rule.criterion, view: view};
          var model = Control.getAddressModel(address);
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
      var getRequiredValue = function (value, address) {
        // Get controller
        var requiredValue = null;
        var controller = Control.getAddressController(address);
        // Retrieve group values (for radio buttons)
        if ("group" in controller) {
          var model = Control.getAddressModel({view: address.view, component: controller.group});
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
      var getObjectsSelectedInGroup = function (address) {
        // Get controller
        var groupList = [];
        var controller = Control.getAddressViewController(address);

        _.each(controller, function (component, componentId) {
          if ("group" in component && component.group === address.group) {
            var model = Control.getAddressModel({view: address.view, component: componentId});
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
      var extractParameters = function (ruleMethod, value, rule, address) {
        var parameters = {values: {value1: value}};
        switch (typeof rule) {
          case "object":
            _.merge(parameters, rule);
            if ("from" in rule && "to" in rule) {
              parameters.values.from = retrieveExternalParameters(rule["from"], address.view);
              parameters.values.to = retrieveExternalParameters(rule["to"], address.view);
            } else if ("value" in rule || "criterion" in rule || "setting" in rule) {
              parameters.values.value2 = retrieveExternalParameters(rule, address.view);
            }
            break;
          default:
            parameters.values.value2 = rule;
        }
        // Format comparison values
        switch (parameters.type) {
          case "float":
            _.each(parameters.values, function (value, valueId) {
              parameters.values[valueId] = !Utilities.isEmpty(value) ? parseFloat(value) : null;
            });
            break;
          case "integer":
            _.each(parameters.values, function (value, valueId) {
              parameters.values[valueId] = !Utilities.isEmpty(value) ? parseInt(value, 10) : null;
            });
            break;
          case "date":
            _.each(parameters.values, function (value, valueId) {
              parameters.values[valueId] = !Utilities.isEmpty(value) ? moment(value, "DD/MM/YYYY") : null;
            });
            break;
          default:
            switch (ruleMethod) {
              case "required":
                parameters.values.value1 = getRequiredValue(value, address);
                break;
              case "checkAtLeast":
                parameters.values.value1 = getObjectsSelectedInGroup(address).length;
                break;
              case "maxlength":
              case "minlength":
                parameters.values.value2 = !Utilities.isEmpty(parameters.values.value2) ? parseInt(parameters.values.value2, 10) : null;
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
      var formatMessage = function (message, parameters) {
        var messageFormatted = $translate.instant(message);
        _.each(parameters.values, function (value, key) {
          var formattedValue = value;
          if (!Utilities.isEmpty(value) && value._isAMomentObject) {
            formattedValue = formattedValue.format("DD/MM/YYYY");
          }
          messageFormatted = messageFormatted.replace("{" + key + "}", formattedValue);
        });
        return messageFormatted;
      };
      var ValidationRules = {
        validate: function (ruleMethod, value, rule, address) {
          var parameters = extractParameters(ruleMethod, value, rule, address);
          return ValidationRules[ruleMethod](parameters);
        },
        required: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_REQUIRED", {})
          };
          return Utilities.isEmpty(parameters.values.value1) && parameters.values.value2 ? error : null;
        },
        text: function (parameters) {
            var error = {
              message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_TEXT", {})
            };
            var passed = new RegExp(patterns.TEXT).test(String(parameters.values.value1));
            return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        textWithSpaces: function (parameters) {
            var error = {
              message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_TEXT_WHITESPACES", {})
            };
            var passed = new RegExp(patterns.TEXT_WHITESPACES).test(String(parameters.values.value1));
            return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        number: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_NUMBER", {})
          };
          var passed = new RegExp(patterns.NUMBER).test(String(parameters.values.value1));
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        integer: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_INTEGER", {})
          };
          var passed = new RegExp(patterns.INTEGER).test(String(parameters.values.value1));
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        digits: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_DIGITS", {})
          };
          var passed = new RegExp(patterns.DIGITS).test(String(parameters.values.value1));
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        email: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_EMAIL", {})
          };
          var passed = new RegExp(patterns.EMAIL).test(String(parameters.values.value1));
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        date: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_DATE", {})
          };
          var passed = new RegExp(patterns.DATE).test(String(parameters.values.value1));
          if (passed) {
            var d = moment(parameters.values.value1, 'DD/MM/YYYY');
            passed = d.isValid();
          }
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        time: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_TIME", {})
          };
          var passed = new RegExp(patterns.TIME).test(String(parameters.values.value1));
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        eq: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_EQUAL", parameters)
          };
          var passed = parameters.values.value1 === parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        ne: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_NOT_EQUAL", parameters)
          };
          var passed = parameters.values.value1 !== parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        lt: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_LESS_THAN", parameters)
          };
          var passed = parameters.values.value1 < parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        le: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_LESS_OR_EQUAL", parameters)
          };
          var passed = parameters.values.value1 <= parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        gt: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_GREATER_THAN", parameters)
          };
          var passed = parameters.values.value1 > parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        ge: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_GREATER_OR_EQUAL", parameters)
          };
          var passed = parameters.values.value1 >= parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        mod: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_DIVISIBLE_BY", parameters)
          };
          var passed = parameters.values.value1 % parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        range: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_RANGE", parameters)
          };
          var passed = parameters.values.value1 >= parameters.values.from && parameters.values.value1 <= parameters.values.to;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        equallength: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_EQUAL_LENGTH", parameters)
          };
          var passed = String(parameters.values.value1).length = parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        maxlength: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_MAXLENGTH", parameters)
          };
          var passed = String(parameters.values.value1).length <= parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        minlength: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_MINLENGTH", parameters)
          };
          var passed = String(parameters.values.value1).length >= parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        pattern: function (parameters) {
          var pattern = parameters.value2 || $settings.get("passwordPattern");
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_PATTERN", {})
          };
          var passed = new RegExp(pattern).test(parameters.values.value1);
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        checkAtLeast: function (parameters) {
          var error = {
            message: formatMessage(parameters.message || "VALIDATOR_MESSAGE_CHECK_AT_LEAST", parameters)
          };
          var passed = parameters.values.value1 >= parameters.values.value2;
          return Utilities.isEmpty(parameters.values.value1) || passed ? null : error;
        },
        invalid: function (parameters) {
          return parameters;
        }
      };
      return ValidationRules;
    }
  ]);
