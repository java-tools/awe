import { aweApplication } from "./../awe";
import { ClientActions } from "../data/actions";
import "../directives/plugins/uiModal";

// Dialog service
aweApplication.factory('Dialog',
  ['ActionController', 'AweUtilities', 'Control', 'AweSettings', 'Button',
    function (ActionController, Utilities, Control, $settings, Button) {
      /**
       * Numeric constructor
       * @param {Scope} scope Numeric scope
       * @param {String} id Numeric id
       * @param {String} element Numeric element
       */
      function Dialog(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Button(this.scope, this.id, this.element);
        var dialog = this;
        this.component.asDialog = function () {
          return dialog.init();
        };
        return this.component;
      }
      Dialog.prototype = {
        /**
         * Initialize dialog
         */
        init: function () {

          // Init as component
          var component = this.component;
          if (!component.asButton()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          // Define icon
          var icon = null;
          if (component.model && component.model.values && component.model.values.length > 0 && component.model.values[0].icon) {
            icon = component.model.values[0].icon;
          }
          if (!icon && component.controller.icon) {
            icon = "fa-" + component.controller.icon;
          }
          component.icon = icon;

          /******************************************************************************
           * SCOPE METHODS
           *****************************************************************************/

          /**
           * On click out event
           * @param {Object} e Event
           */
          component.scope.click = function (e) {
            // Cancel event propagation
            Utilities.stopPropagation(e, true);
          };

          /**
           * Click button function
           * @param {Object} event Event
           */
          component.scope.onClick = function (event) {
            // Cancel event propagation
            Utilities.stopPropagation(event, true);
            component.pendingActions = false;
            // Load dialog model (if not loaded yet)
            var actions = [];
            // Reset the dialog
            actions.push({type: 'reset', target: component.controller.dialog});
            if (component.controller[$settings.get("targetActionKey")] && !component.model.selected &&
              !(component.model.values.length > 0 && "dialog" in component.model.values[0])) {
              // Launch target to load dialog model
              actions.push({type: 'filter'});
            } else {
              // Put the model
              actions.push({type: 'put-model'});
            }
            // Open the dialog
            actions.push({type: 'dialog', target: component.controller.dialog});
            // Retrieve the dialog model
            actions.push({type: 'get-model'});

            // Launch action list
            ActionController.addActionList(actions, true, {address: component.address, context: component.context});
            component.storeEvent('click');
          };

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * Copy the model into the dialog
           */
          component.putModel = function () {
            // Retrieve model
            var view = component.address.view;
            if (component.model.values.length > 0) {
              var model = component.model.values[0].dialog;

              // For each component, retrieve each model
              _.each(model, function (componentModel, componentId) {
                if (componentModel !== null) {
                  var componentAddress = {component: componentId, view: view};
                  Control.changeModelAttribute(componentAddress, componentModel, true);
                }
              });

              // Retrieve dialog
              var dialog = component.controller.dialog;

              // Retrieve dialog components model
              var dialogAddress = {component: dialog, view: view};
              var dialogApi = Control.getAddressApi(dialogAddress);

              // Report children updated
              if (dialogApi.reportGridUpdated) {
                dialogApi.reportGridUpdated();
              }
            }
          };

          /**
           * Retrieve the model from the dialog
           */
          component.getModel = function () {
            var model = {};
            var selected = {};

            // Retrieve dialog
            var dialog = component.controller.dialog;

            // Retrieve dialog components model
            var dialogAddress = {component: dialog, view: component.address.view};
            var dialogController = Control.getAddressController(dialogAddress);

            // For each component, retrieve each model
            var dialogComponents = dialogController.children;
            _.each(dialogComponents, function (component) {
              var componentAddress = {component: component, view: component.address.view};
              var componentModel = Control.getAddressModel(componentAddress);
              var componentApi = Control.getAddressApi(componentAddress);
              model[component] = componentModel;
              if (componentApi && "getData" in componentApi) {
                _.merge(selected, componentApi.getData());
              }
            });

            // Store model
            component.model.selected = {value: _.cloneDeep(selected)};
            if (component.model.values.length === 0) {
              component.model.values.push({});
            }
            component.model.values[0].dialog = _.cloneDeep(model);
          };

          /******************************************************************************
           * EVENT LISTENERS
           *****************************************************************************/
          component.listeners = component.listeners || {};

          // Action listener definition
          Utilities.defineActionListeners(component.listeners, ClientActions.dialog, component.scope, component);

          // Return initialization
          return true;
        }
      };
      return Dialog;
    }
  ]);