import { aweApplication } from "./../awe";

// Control service
aweApplication.factory('Control',
  ['AweUtilities', 'Storage', '$log',
    /**
     * General control methods
     * @param {Service} Utilities
     * @param {Service} Storage
     * @param {Service} $log Log service
     */
    function (Utilities, Storage, $log) {

      // Storage constants
      var INITIAL = "initial-";
      var MODEL = "model";
      var CONTROLLER = "controller";
      var API = "api";

      // Model constants
      var SELECTED = "selected";
      var PREVIOUS = "previous";
      var VALUES = "values";

      // Address constants
      var VIEW = "view";
      var COMPONENT = "component";
      var COLUMN = "column";
      var ROW = "row";

      var Control = {
        /**
         * Retrieve an address target
         * @param {Object} address
         * @return {Object} Address type
         */
        getAddressType: function (address) {
          var addressType;
          if (address && VIEW in address && COMPONENT in address && COLUMN in address && ROW in address) {
            addressType = "cell";
          } else if (address && VIEW in address && COMPONENT in address) {
            addressType = "viewAndComponent";
          } else if (address && COMPONENT in address) {
            addressType = "component";
          } else {
            addressType = "invalid";
          }
          return addressType;
        },
        /**
         * Retrieve an address target
         * @param {Object} address
         * @param {string} action Action to operate
         * @return {Object} controller/model
         */
        getTarget: function (address, action) {
          var target = null;
          // Check if address, action, view and component exists in both checks
          var view, component;
          if (address && VIEW in address && COMPONENT in address) {
            view = address[VIEW];
            component = address[COMPONENT];
          }
          switch (Control.getAddressType(address)) {
            case "cell":
              if (Storage.has(action)) {
                var storedAction = Storage.get(action);
                // Retrieve cell id
                var cellId = Utilities.getCellId(address);
                var cells = storedAction[view][component].cells;
                // Initialize attribute
                if (cellId in cells) {
                  target = cells[cellId];
                }
              }
              break;
            case "viewAndComponent":
              // Normal component
              if (Storage.has(action)) {
                var storedAction = Storage.get(action);
                if (view in storedAction && component in storedAction[view]) {
                  target = storedAction[view][component];
                }
              }
              break;
            case "component":
              if (Storage.has(action)) {
                var storedAction = Storage.get(action);
                // Normal component (no view)
                _.each(storedAction, function (actionView) {
                  if (component in actionView) {
                    target = actionView[component];
                  }
                });
              }
              break;
            default:
              break;
          }
          return target;
        },
        /**
         * Store an address target
         * @param {Object} address
         * @param {string} action Action to operate
         * @param {string} value Value to set
         * @return {Object} controller/model
         */
        setTarget: function (address, action, value) {
          var target = null;
          var view, component;
          if (address && VIEW in address && COMPONENT in address) {
            view = address[VIEW];
            component = address[COMPONENT];
          }
          // Check if address, action, view and component exists in both checks
          switch (Control.getAddressType(address)) {
            case "cell":
              if (Storage.has(action)) {
                var storedAction = Storage.get(action);
                // Retrieve cell id
                var cellId = Utilities.getCellId(address);
                var cells = storedAction[view][component].cells;
                cells[cellId] = value;
                target = cells[cellId];
              }
              break;
            case "viewAndComponent":
              // Normal component
              var storedAction = Storage.get(action);
              storedAction[view][component] = value;
              target = storedAction[view][component];
              break;
            default:
              break;
          }
          return target;
        },
        /**
         * Check if model has changed compare to the initial model
         * @param {String} view
         * @return {boolean} true if have changed | false
         */
        checkModelChanged: function (view) {
          var changes = false;
          var model = Storage.get(MODEL);
          var initialValue = INITIAL + SELECTED;
          // Get model of view
          if (view in model) {
            var modelView = model[view];
            // Compare each selected values of element
            _.each(modelView, function (modelValue) {
              if (initialValue in modelValue && !_.isEqual(modelValue[SELECTED], modelValue[initialValue])) {
                changes = true;
              }
            });
          }
          return changes;
        },
        /**
         * Check if selected values in model are null
         * @param {String} view
         * @return {boolean} true if all null | false
         */
        checkModelEmpty: function (view) {
          var empty = true;
          var model = Storage.get(MODEL);
          var controller = Storage.get(CONTROLLER);

          // Get model of view
          if (view in model) {
            var modelView = model[view];
            var controllerView = controller[view];
            // Compare each selected values of element
            _.each(modelView, function (modelValue, componentId) {
              if (componentId in controller &&
                controllerView[componentId].criterion &&
                !angular.equals(modelView[componentId].selected, null)) {
                empty = false;
              }
            });
          }
          return empty;
        },
        /**
         * Add message to scope
         * @param {string} view
         * @param {string} messageId (Message Id)
         * @param {object} message Message to add
         */
        addMessageToScope: function (view, messageId, message) {
          var messages = Storage.get("messages");
          messages[view][messageId] = message;
        },
        /**
         * Get message from scope
         * @param {string} view
         * @param {string} messageId (Message Id)
         */
        getMessageFromScope: function (view, messageId) {
          var messages = Storage.get("messages");
          return messages[view][messageId];
        },
        /**
         * Publish model changed for the scope
         * @param {Object} address Target address
         * @param {Object} changes
         */
        publishModelChanged: function (address, changes) {
          var launchers = {};
          var launcherId = Utilities.getAddressId(address);
          launchers[launcherId] = changes;
          Control.publish("modelChanged", launchers);
        },
        /**
         * Publish model changed for the scope
         * @param {Object} address Target address
         * @param {Object} changes
         */
        publishControllerChanged: function (address, changes) {
          Control.publish("controllerChange", {address: address, controller: changes});
        },
        /**
         * Check if component has definition
         * @param {Object} address
         * @return {boolean} Component has definition
         */
        checkComponent: function (address) {
          return Control.getTarget(address, CONTROLLER) !== null;
        },
        /**
         * Fix the selected attribute so that it allways return an array
         * @param {Object} scope
         */
        fixMultipleSelectedValue: function (scope) {
          scope.model.selected = Utilities.asArray(scope.model.selected);
        },
        /**
         * Retrieve an address controller
         * @param {Object} address
         * @return {Object} controller
         */
        getAddressController: function (address) {
          return Control.getTarget(address, CONTROLLER) || {actions: []};
        },
        /**
         * Store an address controller
         * @param {Object} address
         * @param {Object} value
         */
        setAddressController: function (address, value) {
          return Control.setTarget(address, CONTROLLER, value);
        },
        /**
         * Retrieve an address controller
         * @param {Object} address
         * @return {Object} controller
         */
        getAddressViewController: function (address) {
          var controller = Storage.get(CONTROLLER);
          return controller[address[VIEW]];
        },
        /**
         * Retrieve an address model
         * @param {Object} address
         * @return {Object} model
         */
        getAddressViewModel: function (address) {
          var model = Storage.get(MODEL);
          return model[address[VIEW]];
        },
        /**
         * Retrieve an address api
         * @param {Object} address
         * @return {Object} api
         */
        getAddressViewApi: function (address) {
          var api = Storage.get(API);
          return api[address[VIEW]];
        },
        /**
         * Retrieve an address for api
         * @param {Object} address
         * @return {Object} api
         */
        getAddressApi: function (address) {
          return Control.getTarget(address, API) || {};
        },
        /**
         * Store an address controller
         * @param {Object} address
         * @param {Object} value
         */
        setAddressApi: function (address, value) {
          return Control.setTarget(address, API, value);
        },
        /**
         * Retrieve an address model
         * @param {Object} address
         * @return {Object} model
         */
        getAddressModel: function (address) {
          return Control.getTarget(address, MODEL) || {selected: null, values: []};
        },
        /**
         * Store an address model
         * @param {Object} address
         * @param {Object} value
         * @return {Object} model
         */
        setAddressModel: function (address, value) {
          return Control.setTarget(address, MODEL, value);
        },
        /**
         * Retrieve row index
         * @param {string} model Grid model
         * @param {string} rowId Row identifier
         * @param {string} identifier Row identifier field
         * @returns {integer} Selected row index in values array
         */
        getRowIndex: function (model, rowId, identifier) {
          for (var index = 0, total = model.length; index < total; index++) {
            if (String(model[index][identifier]) === String(rowId)) {
              return index;
            }
          }
          return -1;
        },
        /**
         * Format an array of data into readable data for the server
         * @param {object} data Data array
         * @return {mixed} formatted data
         */
        formatDataList: function (data) {
          var formattedData;
          var dataList = Utilities.asArray(data);
          switch (dataList.length) {
            case 0:
              formattedData = null;
              break;
            case 1:
              formattedData = dataList[0] === "" ? null : dataList[0];
              break;
            default:
              formattedData = dataList;
          }
          return formattedData;
        },
        /**
         * Change a controller attribute
         * @param {Object} address Component address
         * @param {Object} attributes Attributes to set
         */
        changeControllerAttribute: function (address, attributes) {
          var controller = Control.getAddressController(address);
          if (controller) {
            _.each(attributes, function (attribute, attributeId) {
              var initialAttribute = INITIAL + attributeId;
              if (!(initialAttribute in controller)) {
                controller[initialAttribute] = _.cloneDeep(controller[attributeId]);
              }
              controller[attributeId] = attribute;
            });

            // Publish controller change
            Control.publishControllerChanged(address, attributes);
          }
        },
        /**
         * Restore a controller attribute
         * @param {Object} address Component address
         * @param {String} attribute Attribute to restore
         */
        restoreControllerAttribute: function (address, attribute) {
          var controller = Control.getAddressController(address);
          var initialAttribute = INITIAL + attribute;
          if (controller) {
            if (initialAttribute in controller) {
              controller[attribute] = _.cloneDeep(controller[initialAttribute]);
            }
            // Publish controller change
            var changes = {};
            changes[attribute] = controller[attribute];
            Control.publishControllerChanged(address, changes);
          }
        },
        /**
         * Change model value
         * @param {Object} address Component address
         * @param {Object} attributes Model attributes
         * @param {Boolean} publish Publish model changed
         */
        changeModelAttribute: function (address, attributes, publish) {
          var model = Control.getAddressModel(address);
          if (model) {
            _.each(attributes, function (attribute, attributeId) {
              var initialAttribute = INITIAL + attributeId;
              if (!(initialAttribute in model) && Storage.get("status")[address.view] === "loaded") {
                if (attributeId === SELECTED) {
                  model[initialAttribute] = _.cloneDeep(model[PREVIOUS]);
                } else {
                  model[initialAttribute] = _.cloneDeep(attribute);
                }
              }
            });

            // Copy attributes to model
            Control.launchApiMethod(address, "updateModelValues", [attributes]);

            // Publish model change
            if (publish) {
              Control.publishModelChanged(address, attributes);
            }
          }
        },
        /**
         * Restore model value
         * @param {Object} address Component address
         * @param {String} attribute Model attribute
         */
        restoreModelAttribute: function (address, attribute) {
          var model = Control.getAddressModel(address);
          // Publish model change
          if (model) {
            var attributes = {};
            attributes[attribute] = model.defaultValues;
            Control.changeModelAttribute(address, attributes, true);
          }
        },
        /**
         * Restore radio model value
         * @param {Object} address Component address
         * @param {String} attribute Model attribute
         */
        restoreInitialModelAttribute: function (address, attribute) {
          var model = Control.getAddressModel(address);
          var initialAttribute = INITIAL + attribute;
          if (model) {
            if (initialAttribute in model) {
              model[attribute] = _.cloneDeep(model[initialAttribute]);
            }
            // Publish controller change
            var changes = {};
            changes[attribute] = model[attribute];
            Control.changeModelAttribute(address, changes, true);
          }
        },
        /**
         * Reset model attribute
         * @param {Object} address Component address
         * @param {String} attribute Model attribute
         */
        resetModelAttribute: function (address, attribute) {
          var attributes = {};
          attributes[attribute] = null;
          Control.changeModelAttribute(address, attributes, true);
        },
        /**
         * Change view model
         * @param {String} view Scope view
         * @param {object} modelView New model
         * @param {object} publish Publish the change
         */
        changeViewModel: function (view, modelView, publish) {
          var model = Storage.get(MODEL);
          model[view] = modelView;
          if (publish) {
            Control.publish("modelChanged", model[view]);
          }
        },
        /**
         * Destroy all views
         */
        destroyAllViews: function () {
          var controls = [MODEL, CONTROLLER, API];
          _.each(controls, function (control) {
            Storage.put(control, {});
          });
        },
        /**
         * Retrieve a controller attribute value
         * @param {Object} address Component address
         * @param {String} attribute Attribute to get
         */
        getControllerAttribute: function (address, attribute) {
          var controller = Control.getAddressController(address);
          var value = null;
          if (controller) {
            value = controller[attribute];
          }
          return value;
        },
        /**
         * Retrieve a model attribute value
         * @param {Object} address Component address
         * @param {String} attribute Attribute to get
         */
        getModelAttribute: function (address, attribute) {
          var model = Control.getAddressModel(address);
          var value = null;
          if (model) {
            value = model[attribute];
          }
          return value;
        },
        /**
         * Launch a method in api
         * @param {Object} address Component address
         * @param {String} method Method to call
         * @param {Array} parameters Method parameters (array)
         */
        launchApiMethod: function (address, method, parameters) {
          var api = Control.getAddressApi(address);
          if (api && method in api) {
            api[method].apply(api, parameters);
          } else {
            $log.warn("[WARNING] Method '" + method + "' not found in api", {'address': address});
          }
        },
        /**
         * Broadcast a change
         * @param {Object} channel Channel to publish
         * @param {Object} parameters Parameters to send
         */
        publish: function (channel, parameters) {
          Utilities.publish(channel, parameters);
        }
      };
      return Control;
    }
  ]);