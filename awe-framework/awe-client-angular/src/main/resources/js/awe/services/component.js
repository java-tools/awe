import {aweApplication} from "../awe";
import {ClientActions} from "../data/actions";

// Component service
aweApplication.factory('Component',
  ['Control', 'AweSettings', 'AweUtilities', 'ServerData', 'ActionController', '$log',
    /**
     * Component generic methods
     * @param {object} Control
     * @param {object} $settings Awe $settings
     * @param {object} Utilities Awe utilities
     * @param {object} ServerData Server data calls
     * @param {object} $actionController Action controller
     * @param {object} $log Log
     */
    function (Control, $settings, Utilities, ServerData, $actionController, $log) {

      /**
       * Destroys autorefresh timer
       * @param {object} component
       */
      function destroyTimers(component) {
        Utilities.timeout.cancel(component.helpTimer);
        Utilities.interval.cancel(component.autoRefreshTimer);
      }

      /**
       * Destroy component
       * @param {object} component
       */
      function destroy(component) {
        component.helpNode && component.helpNode.off();
        component.helpOver = false;
        component.alive = false;
        destroyTimers(component);
        // Clear listeners
        Utilities.clearListeners(component.listeners);
      }

      /**
       * Component constructor
       * @param {Scope} scope Component scope
       * @param {String} id Component id
       */
      function Component(scope, id) {
        if (!("component" in scope)) {
          scope.component = this;
        }
        this.scope = scope;
        this.id = id;
        this.initialized = false;
        this.dependencies = [];
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
           * @returns {string} Current  value
           */
          value: function (component) {
            return String(component.model.selected);
          },
          /**
           * Retrieve total values
           * @param {object} component Component scope
           * @returns {number} Total values length
           */
          totalValues: function (component) {
            return component.model.records || 0;
          },
          /**
           * Retrieve if component is visible or not
           * @param {object} component Component scope
           * @returns {boolean} Component visibility
           */
          visible: function (component) {
            return component.controller.visible && !component.controller.invisible;
          }
        };
        return this;
      }

      // Component methods
      Component.prototype = {
        /**
         * Initialization method
         */
        init: function () {
          var component = this;
          component.alive = true;
          // Check if identifier exists
          if (component.id) {
            // View
            var scope = component.scope;
            scope.view = scope.view || scope.$parent.view || scope.$parent.$parent.view;
            component.view = scope.view;

            // Context
            scope.context = scope.context || scope.$parent.context || scope.$parent.$parent.context;
            component.context = scope.context;

            // Id
            component.address = component.address || {
              view: component.view,
              component: component.id
            };
            component.scope.address = component.address;

            // Check if component can be initialized
            if (!Control.checkComponent(component.address)) {
              $log.warn("[COMPONENT INITIALIZATION] Component " + component.id + " can't be initialized", {address: component.address});
              return false;
            }

            // Model
            component.model = Control.getAddressModel(component.address);
            component.scope.model = component.model;

            // Controller
            component.controller = Control.getAddressController(component.address) || {};
            component.controller.id = component.id;
            component.scope.controller = component.controller;

            if (!component.scope.iconLoader) {
              component.scope.iconLoader = component.controller.iconLoading;
            }

            // Set loading as false
            component.controller.loading = false;

            // Set dependencies if existing
            if ("dependencies" in component.controller) {
              component.dependencies = component.controller.dependencies;
              component.scope.dependencies = component;
            }

            // Set dependencies if existing
            if ("contextMenu" in component.controller) {
              component.scope.contextMenuData = component.controller.contextMenu;
            } else {
              component.scope.contextMenuData = [];
            }

            // Check autoload attribute of controller
            component.checkAutoLoad();

            // Check autorefresh attribute of controller
            component.checkAutoRefresh();

            /******************************************************************************
             * EVENT LISTENERS
             *****************************************************************************/
            component.listeners = component.listeners || {};

            // Action listener definition
            $actionController.defineActionListeners(component.listeners, ClientActions.component, component.scope, component);

            // Clean objects on destroy | unload
            component.listeners['destroy'] = component.scope.$on("$destroy", () => destroy(component));
            component.listeners['unload'] = component.scope.$on("unload", function (event, view) {
              if (view === component.view) {
                destroy(component);
              }
            });

            /**
             * Generate component API
             */
            component.api = component.api || {};
            Control.setAddressApi(component.address, component.api);
            /**              * API link to specific getAttribute function;
             * @param {string} attribute Attribute to check
             * @param {column} column Column id
             * @param {row} row Row id
             * @returns {mixed} Directive attribute value
             */
            component.api.getAttribute = function (attribute, column, row) {
              return component.getAttribute(attribute, column, row);
            };

            /**
             * API link to specific getData function;
             * @returns {mixed} Retrieve current component data
             */
            component.api.getData = function () {
              return component.getData();
            };

            /**
             * API link to specific getData function;
             * @returns {mixed} Retrieve current component data
             */
            component.api.getPrintData = function () {
              return component.getPrintData();
            };

            /**
             * API link to specific getSpecificFields function;
             * @returns {mixed} Retrieve current component data
             */
            component.api.getSpecificFields = function () {
              return component.getSpecificFields();
            };

            /**
             * API link to specific getSpecificFields function;
             * @returns {mixed} Retrieve visible value
             */
            component.api.getVisibleValue = function () {
              return component.getVisibleValue();
            };

            /**
             * API link to update the model values
             * @param {object} data New model data attributes
             */
            var methodName = "updateModelValues";
            if (component.api.updateModelValues) {
              methodName = "updateComponentModelValues";
            }

            component.api[methodName] = function (data) {
              var model = Control.getAddressModel(component.address);
              if (model) {
                _.merge(model, data);
                model.values = _.uniqBy(model.values, 'value');

                // If selected in data, update selected values
                if ("selected" in data) {
                  // Store component model
                  model.selectedValues = Utilities.asArray(data.selected);

                  // Store value list
                  var valueList = [];
                  _.each(model.selectedValues, function (row) {
                    if (typeof row === "object" && "value" in row) {
                      valueList.push(row.value);
                    } else {
                      valueList.push(row);
                    }
                  });

                  // Store selected values
                  model.selected = Control.formatDataList(valueList);
                }
              }
            };

            /**
             * API link to toggle autorefresh
             * @param {integer} value Autorefresh seconds
             * @param {boolean} enable Enable autorefresh
             */
            component.api.toggleAutorefresh = function (value, enable) {
              var controller = Control.getAddressController(component.address);
              if (enable) {
                controller.autorefresh = value;
                component.checkAutoRefresh();
              } else {
                controller.autorefresh = 0;
                Utilities.interval.cancel(component.autoRefreshTimer);
              }
            };

            /**
             * API link to update the selected values
             * @param {type} selectedValues
             */
            component.api.updateSelectedValue = function (selectedValues) {
              var model = Control.getAddressModel(component.address);
              if (model) {
                // Store component model
                model.selectedValues = selectedValues || [];

                // Store value list
                var selectedList = [];
                var valueList = [];

                _.each(component.model.selectedValues, function (selected) {
                  if (selected !== null) {
                    if (typeof selected === "object" && "value" in selected) {
                      selectedList.push(selected.value);
                      valueList.push(selected);
                    } else {
                      selectedList.push(selected);
                      valueList.push({value: selected, label: selected, id: selected});
                    }
                  }
                });

                // Store values
                _.merge(model.values, valueList);

                // Store selected values
                model.selected = Control.formatDataList(selectedList);

                // Publish model change
                component.modelChange();
              }
            };

            /**
             * API link to finish loading
             */
            component.api.endLoad = function () {
              Control.changeControllerAttribute(component.address, {loading: false});
            };

            // Define sizes
            component.scope.size = component.getSize();
            // Define character size
            component.scope.charSize = component.getCharSize(component.scope.size);

            // Report to view that component has been initialized
            Utilities.emitFromScope("component-initialized-" + component.address.view,
              {
                address: _.cloneDeep(component.address),
                hasDependencies: component.dependencies.length > 0
              }, component.scope);
          }
          // Initialization has been right
          return true;
        },
        /**
         * Basic getAttribute function (To be overwritten on complex directives)
         * @param {type} attribute Attribute to check
         * @returns {undefined}
         */
        getAttribute: function (attribute) {
          return this.attributeMethods[attribute](this);
        },
        /**
         * Get max elements per page
         * @param {int|undefined} defaultValue [optional] default value
         * @returns {int} elements per page
         */
        getMax: function (defaultValue = $settings.get("recordsPerPage")) {
          return parseInt("max" in this.controller ? this.controller.max : defaultValue, 10);
        },
        /**
         * Basic getData function (To be overwritten on complex directives)
         * @returns {Object} Data from criteria
         */
        getData: function () {
          // Initialize data
          return {};
        },
        /**
         * Basic getPrintData function (To be overwritten on complex directives)
         * @returns {Object} Data from criteria
         */
        getPrintData: function () {
          return this.getData();
        },
        /**
         * Basic getSpecificFields function (To be overwritten on complex directives)
         * @returns {Object} Specific fields from component
         */
        getSpecificFields: function () {
          // Initialize data
          return {
            max: this.getMax()
          };
        },
        /**
         * Initialize a help node events
         * @param {Object} help Object
         */
        initHelpNode: function (help) {
          var component = this;
          component.helpOver = false;
          component.helpNode = $(help.node);
          var isDisabled = function () {
            return component.isDisabled ? component.isDisabled() : false;
          };
          var onEnter = function () {
            if (component.alive && !isDisabled()) {
              component.helpOver = true;
              Utilities.timeout.cancel(component.helpTimer);
              component.helpTimer = Utilities.timeout(showHelp, $settings.get("helpTimeout"));
            }
          };
          let showHelp = function () {
            if (component.helpOver && !isDisabled()) {
              Control.publish('showHelp', help);
            }
          };
          var onLeave = function () {
            component.helpOver = false;
            Utilities.timeout.cancel(component.helpTimer);
            Control.publish('hideHelp');
          };
          component.helpNode.on({mouseenter: onEnter, mousedown: onLeave, mouseleave: onLeave});
        },
        /**
         * Checks autoload
         */
        checkAutoLoad: function () {
          if (this.controller.autoload) {
            var component = this;
            Utilities.timeout(function () {
              component.reload();
            });
          }
        },
        /**
         * Checks autorefresh timeout
         */
        checkAutoRefresh: function () {
          let component = this;
          if (this.controller.autorefresh) {
            Utilities.interval.cancel(this.autoRefreshTimer);
            this.autoRefreshTimer = Utilities.interval(function () {
              component.reload({silent: true, async: true});
            }, (this.controller.autorefresh * 1000));
          }
        },
        /**
         * Reload data from element
         * @return Action server action
         */
        reload: function (parameters = {silent: this.reloadSilent, async: this.reloadAsync}) {
          // Retrieve silent and async if arguments eq 2
          let isSilent = parameters.silent;
          let isAsync = parameters.async;

          // Add action to actions stack
          let values = {};
          values.type = this.controller[$settings.get("serverActionKey")];
          values[$settings.get("targetActionKey")] = this.controller[$settings.get("targetActionKey")];

          // Generate server action
          let actionScope = {address: this.address, context: this.context};
          let serverAction = $actionController.generateAction(ServerData.getServerAction(this.address, values, isAsync, isSilent), actionScope, isSilent, isAsync);

          // Send action list
          $actionController.addActionList([serverAction], false, actionScope);

          // Return server action to cancel it
          return serverAction;
        },
        /**
         * Set component as loading
         */
        startLoad: function () {
          // Change loading attribute
          if (this.controller) {
            this.controller.loading = true;
          }
        },
        /**
         * Retrieves visible value for the component
         * @returns {string} visible value
         */
        getVisibleValue: function () {
          return Utilities.asArray(this.model.selected).join(", ");
        },
        /**
         * Updates visible value for the array
         * @returns {string} visible value
         */
        updateVisibleValue: function () {
          // Update visible value
          var value = this.getVisibleValue();
          if (value !== this.visibleValue) {
            this.visibleValue = value;
          }
        }, /**
         * Store in model the last event launched
         * @param {String} event Event launched
         */
        storeEvent: function (event) {
          // Publish event launched
          Control.publishModelChanged(this.address, {event: event});
        },
        modelChange: function () {
          this.model.selected = this.model.selected !== '' ? this.model.selected : null;
          var changed = this.model.previous !== this.model.selected;
          if (changed) {
            Control.changeModelAttribute(this.address, {selected: this.model.selected}, this.model.previous !== this.model.selected);
          }
        },
        /**
         * Retrieve input classes (validation and input size)
         * @returns {String} Input classes
         */
        getSize: function () {
          var componentSize;
          if (this.scope.componentSize && this.scope.componentSize !== "") {
            componentSize = this.scope.componentSize;
          } else if (this.controller && this.controller.size && this.controller.size !== "") {
            componentSize = this.controller.size;
          } else {
            componentSize = $settings.get("defaultComponentSize");
          }
          return componentSize;
        },
        /**
         * Retrieve input classes (validation and input size)
         * @param {String} size
         * @returns {String} Input classes
         */
        getCharSize: function (size) {
          var charSize = $settings.get("pixelsPerCharacter");
          switch (size) {
            case "md":
              charSize += 1;
              break;
            case "lg":
              charSize += 3;
              break;
          }
          return charSize;
        },
        /**
         * Retrieve attribute value
         * @param {String} attribute
         * @returns {String} attribute value
         */
        get: function (attribute) {
          return this[attribute];
        },
        /**
         * Show the context menu
         * @param {event} event
         */
        showContextMenu: function (event) {
          if (this.scope.contextMenu) {
            this.scope.contextMenu.show(event);
          }
        },
        /**
         * Hide the context menu
         */
        hideContextMenu: function () {
          if (this.scope.contextMenu) {
            this.scope.contextMenu.hide();
          }
        },
        /**
         * Initialize as component
         */
        asComponent: function () {
          return this.init();
        }
      };
      return Component;
    }
  ]);
