import { aweApplication } from "./../awe";
import _ from "lodash";

// Server data service
aweApplication.factory('ServerData',
  ['Connection', '$log', 'Storage', 'AweSettings', 'ActionController', 'Control', '$templateCache',
    /**
     *
     * @param {type} Connection
     * @param {type} $log
     * @param {type} Storage
     * @param {type} $settings
     * @param {type} ActionController
     * @param {type} Control
     * @param {type} $templateCache
     */
    function (Connection, $log, Storage, $settings, ActionController, Control, $templateCache) {
      let ServerData = {
        /**
         * Retrieve a screen template code
         * @param {String} screen Screen name
         * @param {String} view Screen view
         * @returns {String} Screen template
         */
        getScreenData: function (screen, view) {
          var parameters = ServerData.getFormValues();
          parameters[$settings.get("serverActionKey")] = "screen-data";
          parameters["view"] = view;
          if (screen !== null) {
            parameters[$settings.get("optionKey")] = screen;
            $log.info("Retrieving screen data for " + screen);
          } else {
            $log.info("Retrieving screen data for initial screen");
          }

          // Generate server action
          var serverAction = {type: 'server', parameters: parameters};
          var actionList = [serverAction];

          // Add action to actions stack
          return ActionController.addActionList(actionList, false, {});
        },
        /**
         * Store the loaded screen data
         * @param {Object} data Screen data
         * @param {String} view Screen view
         */
        storeScreenData: function (data, view) {
          var controller = Storage.get("controller");
          var messages = Storage.get("messages");
          var api = Storage.get("api");
          var screen = Storage.get("screen");

          // Store data
          if (data !== null) {
            // Generate screen data
            screen[view] = data.screen;

            // Store screen messages in scope
            messages[view] = data.messages;

            // Generate component api
            api[view] = {};

            // Store screen model in scope
            var viewModel = {};
            controller[view] = {};
            _.each(data.components, function (component) {
              // Define model architecture
              var modelArchitecture = {values: [], selected: null, defaultValues: null};
              // Add model values
              if (component.model !== null) {
                _.each(component.model, function (value, attribute) {
                  modelArchitecture[attribute] = value;
                });

                // Get selected values as list or single value
                if ("selected" in component.model) {
                  modelArchitecture.selected = Control.formatDataList(component.model.selected);
                  modelArchitecture.previous = _.cloneDeep(modelArchitecture.selected);
                }

                // Get selected values as list or single value
                if ("defaultValues" in component.model) {
                  modelArchitecture.defaultValues = Control.formatDataList(component.model.defaultValues);
                }
              }

              // Store controller in view
              component.controller.optionId = screen[view].option;
              controller[view][component.id] = component.controller;

              // Store model in view
              viewModel[component.id] = modelArchitecture;

              // Pregenerate api for the component
              api[view][component.id] = {};
            });

            // Change model
            Control.changeViewModel(view, viewModel, false);
          }
        },
        /**
         * Retrieve a screen template code
         * @param {String} screen Screen name
         * @param {String} view Screen view
         * @returns {String} Screen template
         */
        getTemplateUrl: function (screen, view) {
          var template = "";
          // Add option
          if (screen !== null) {
            template = "/" + view + "/" + screen;
          }

          // Retrieve url
          return Connection.getRawUrl() + "/template/screen" + template;
        },
        /**
         * Retrieve a screen template code
         * @param {String} screen Screen name
         * @param {String} view Screen view
         * @returns {String} Screen template
         */
        getTaglistUrl: function (option, taglist) {
          var template = "";
          // Add option
          if (option !== null) {
            template += "/" + option;
          }

          // Add taglist
          if (taglist !== null) {
            template += "/" + taglist;
          }

          // Retrieve url
          return Connection.getRawUrl() + "/taglist" + template;
        },
        /**
         * Retrieve the help url for a screen
         * @param {String} option Option identifier
         * @returns {String} Help template url
         */
        getHelpUrl: function (option) {
          // Add action
          var template = "";
          // Add option
          if (option !== null) {
            template = "/" + option;
          }

          // Retrieve url
          return Connection.getRawUrl() + "/template/help" + template;
        },
        /**
         * Retrieves a generic file url
         * @param {String} action Action to launch
         * @param {String} target Action target
         * @returns {String} Help template url
         */
        getGenericFileUrl: function (action, target) {
          // Add action
          var template = "";
          // Add option
          if (action !== null) {
            template += "/" + action;
            // Add target
            if (target !== null) {
              template += "/" + target;
            }
          }

          // Retrieve url
          return Connection.getRawUrl() + template;
        },
        /**
         * Compose url and parameters to retrieve a file from server
         * @param {String} action Action name
         * @param {Object} parameters Parameter list
         * @returns {Object} File data
         */
        getFileData: function (action, parameters) {
          // Retrieve url
          return {url: ServerData.getFileUrl(action), data: parameters};
        },
        /**
         * Retrieve URL for one server file
         * @param {String} action
         * @returns {String} Server file URL
         */
        getFileUrl: function (action) {
          let call = "/file";
          if (action) {
            call += "/" + action;
          }
          return Connection.getRawUrl() + call;
        },
        /**
         * Retrieve an angular template code
         * @param {String} template Template to load
         * @returns {String} Angular template url
         */
        getAngularTemplateUrl: function (template) {
          if (template !== null) {
            return Connection.getRawUrl() + "/template/angular/" + template;
          }
          return "";
        },
        /**
         * Preload an angular template code
         * @param {Object} template Template to load
         * @param {Function} onLoad on loaded method
         */
        preloadAngularTemplate: function (template, onLoad) {
          var templateUrl = ServerData.getAngularTemplateUrl(template.path);
          ServerData.get(templateUrl).success(function (data, status) {
            if (data && status === 200) {
              $templateCache.put(template.name || template.path, data);
              if (onLoad) {
                onLoad(data);
              }
            }
          });
        },
        /**
         * Retrieve the updated model
         * @param {Object} component Component to check
         * @param {Object} model model to update
         * @param {String} method Method to call
         * @param {String} args Method arguments
         * @returns {Object} model updated
         */
        getComponentData: function (component, model, method, args) {
          // Get data for component
          if (method in component) {
            var componentData = component[method](args);
            // Check and warn if value is overwritten
            _.each(componentData, function (parameter, parameterId) {
              if (parameterId in model) {
                $log.warn("[WARNING] Overwriting '" + parameterId + "' duplicated parameter", {'old': model[parameterId], 'new': parameter});
              }
            });
            // Update model sent
            _.merge(model, componentData);
          }

          // Return data
          return model;
        },
        /**
         * Retrieve the updated model
         */
        getFormValues: function () {
          // Retrieve action data
          var model = {};
          var api = Storage.get("api");

          // Retrieve components from each view
          _.each(api, function (view) {
            _.each(view, function (component) {
              model = ServerData.getComponentData(component, model, "getData");
            });
          });

          // Return data
          return model;
        },
        /**
         * Retrieve the updated model for printing
         */
        getFormValuesForPrinting: function () {
          // Retrieve action data
          var model = {};
          var api = Storage.get("api");
          var fullModel = Storage.get("model");
          var orientationModel = fullModel["report"]["reportOrientation"];
          var orientation = orientationModel.selected || "PORTRAIT";

          // Retrieve components from each view
          _.each(api, function (view) {
            _.each(view, function (component) {
              model = ServerData.getComponentData(component, model, "getPrintData", orientation);
            });
          });

          // Return data
          return model;
        },
        /**
         * Retrieve parameters and send them to the server
         * @param {Action} action Action received
         * @param {Object} parameters Server action parameters
         */
        launchServerAction: function (action, parameters) {
          // Retrieve form values
          var message = {
            action: action,
            target: action.attr("callbackTarget"),
            values: action.attr("parameters") || {}
          };

          // Set callback target as loading
          Control.changeControllerAttribute(message.target, {loading: true});

          // Add form values
          _.merge(message.values, parameters);

          // Retrieve target specific attributes for the server call
          if (message.target) {
            var target = Control.getAddressApi(message.target);
            if (target && target.getSpecificFields) {
              // Add form values
              _.merge(message.values, target.getSpecificFields());
            }
          }

          // Launch server action
          var request = Connection.sendMessage(message);
          action.onCancel = function () {
            if (request && request.reject) {
              request.reject();
            }
          };
        },
        /**
         * Generate a server action
         * @param {String} address Target address
         * @param {Object} custom Custom values
         * @param {Boolean} async Async action
         * @param {Boolean} silent Silent action
         */
        getServerAction: function (address, custom, async, silent) {
          var screenData = Storage.get("screen")[address.view];

          // Generate action
          var action = {
            type: 'server',
            async: async,
            silent: silent,
            parameters: {
              screen: screenData.name,
              address: address,
              component: address.component
            }
          };

          // Add form and custom values
          _.merge(action.parameters, custom);

          // Add server action
          action.parameters[$settings.get("serverActionKey")] = custom.type || "data";

          // Return server action
          return action;
        },
        /**
         * Send a query to the server
         * @param {Object} address Target address
         * @param {Object} custom Custom values
         * @param {Boolean} async Async action
         * @param {Boolean} silent Silent action
         */
        sendQuery: function (address, custom, async, silent) {
          // Generate server action
          var action = ServerData.getServerAction(address, custom, async, silent);
          // Add action to actions stack
          return ActionController.addActionList([action], true, {address: address, context: ""});
        },
        /**
         * Send a query to the server
         * @param {Object} custom Custom values
         * @param {Boolean} async Async action
         * @param {Boolean} silent Silent action
         */
        sendMaintain: function (custom, async, silent) {
          // Generate server action
          // Generate action
          var action = {
            type: 'server',
            async: async,
            silent: silent,
            parameters: {}
          };

          // Add server action
          action.parameters[$settings.get("serverActionKey")] = custom.type;
          action.parameters[$settings.get("targetActionKey")] = custom.maintain;

          // Add action to actions stack
          return ActionController.addActionList([action], true, {address: {}, context: ""});
        },
        /**
         * Send a message to the server
         * @param {Object} message Message to send
         * @return {Object} Connection promise
         */
        send: function (message) {
          return Connection.send(message);
        },
        /**
         * Launch a get message to the server
         * @param {Object} url Message to send
         * @return {Object} Connection promise
         */
        get: function (url) {
          return Connection.get(url);
        }
      };
      return ServerData;
    }
  ]);
