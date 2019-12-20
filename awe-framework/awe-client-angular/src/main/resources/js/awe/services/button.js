import {aweApplication} from "./../awe";

// Button service
aweApplication.factory('Button',
  ['Component', 'Storage', 'ActionController', 'AweUtilities',
    /**
     * Button generic methods
     * @param {Object} Component component
     * @param {Object} Storage service
     * @param {Object} ActionController service
     * @param {Object} Utilities service
     */
    function (Component, Storage, ActionController, Utilities) {

      /**
       * Button constructor
       * @param {Scope} scope Button scope
       * @param {String} id Button id
       * @param {String} element Button element
       */
      function Button(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Component(scope, id);
        // Store mouse down
        this.component.pendingActions = false;
        const button = this;

        /**
         * Initialize as button
         */
        this.component.asButton = function () {
          return button.init();
        };
        return this.component;
      }

      Button.prototype = {

        /**
         * Initialization method
         */
        init: function () {
          // Init as component
          const button = this;
          const component = this.component;
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          // Generate button class
          if (component.controller) {
            component.scope.buttonClass = component.controller.buttonType === "submit" ? "primary" : null;
            // Add reset action if type is reset
            if (component.controller.actions && component.controller.actions.length === 0 && component.controller.buttonType === "reset") {
              var resetAction = {type: "restore"};
              component.controller.actions.push(resetAction);
            }

            // Generate buttonType
            component.buttonType = component.controller.buttonType === "reset" ? "button" : component.controller.buttonType;
          }

          //*****************************************************************************
          // SCOPE METHODS
          //****************************************************************************/

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
           */
          component.scope.onClick = function () {
            component.pendingActions = false;
            ActionController.addActionList(component.controller.actions, true, {
              address: component.address,
              context: component.context
            });
            component.storeEvent('click');
          };

          /**
           * Check if button is disabled
           * @returns {boolean} Button is disabled
           */
          component.scope.isDisabled = function () {
            return Storage.get("actions-running") || component.scope.$root.loading ||
              (component.controller && component.controller.disabled);
          };

          /**
           * Mousedown button function
           * Store if button was disabled on mouse down to see if there are
           * pending actions that have not been launched due to button disablery
           */
          component.scope.onMouseDown = function () {
            component.pendingActions = !component.scope.isDisabled();
          };

          //*****************************************************************************
          // PRIVATE METHODS
          //****************************************************************************/

          /**
           * Retrieve button class
           * @return {String} button class
           */
          const getClass = function () {
            let buttonClass = [];
            let buttonStyle = component.controller && component.controller.style ? component.controller.style : "";
            let buttonSize = "btn-" + component.scope.size;
            buttonClass.push(buttonStyle);
            if (buttonStyle === "" || buttonStyle.indexOf('no-class') === -1) {
              buttonClass.push("btn");
              buttonClass.push("btn-awe");
              buttonClass.push(buttonSize);
              if (component.scope.buttonClass) {
                buttonClass.push("btn-" + component.scope.buttonClass);
              }
            }

            if (component.controller && "help" in component.controller) {
              buttonClass.push("help-target");
            }

            return buttonClass.join(" ");
          };

          /**
           * Retrieve button group class
           * @return {String} button group class
           */
          const getGroupClass = function () {
            let buttonClass = [];
            let buttonStyle = component.controller.style || "";
            if (buttonStyle === "" || buttonStyle.indexOf('no-class') === -1) {
              buttonClass.push("btn-group");
            }
            if (component.controller.hidden) {
              buttonClass.push("hidden");
            }
            if (component.controller.invisible) {
              buttonClass.push("invisible");
            }
            return buttonClass.join(" ");
          };

          /**
           * Check if there are pending actions, and if true, launch them
           */
          const checkPendingActions = function () {
            if (component.pendingActions) {
              component.scope.onClick();
            }
          };

          /**
           * Initialize help rules
           */
          const initHelp = function () {
            // Set context menu if existing
            if (component.controller && "help" in component.controller) {
              Utilities.timeout(function () {
                // Initialize help node
                component.scope.help = {
                  node: button.element.find(".help-target"),
                  text: component.controller.help,
                  image: component.controller.helpImage
                };
                component.initHelpNode(component.scope.help);
              }, 100);
            }
          };

          // Initialize help
          initHelp();

          //****************************************************************************
          //  COMPONENT METHODS
          //****************************************************************************

          /**
           * Store button classes
           */
          component.updateClasses = function () {
            component.scope.groupClass = getGroupClass();
            component.scope.classes = getClass();
          };

          // Initially update classes
          component.updateClasses();

          /**
           * Basic getSpecificFields function (To be overwritten on complex directives)
           * @returns {Object} Specific fields from component
           */
          component.getSpecificFields = function () {
            return {buttonValue: component.model.selected, buttonAddress: component.address};
          };

          //****************************************************************************
          // EVENT LISTENERS
          //****************************************************************************
          component.listeners = component.listeners || {};
          // On model change launch dependency
          component.listeners["controllerChange"] = component.scope.$on("controllerChange", function (event, parameters) {
            if (_.isEqual(parameters.address, component.address)) {
              component.updateClasses();
            }
          });
          // On disable button check for pending actions
          component.listeners["disableButtons"] = component.scope.$on("disable-buttons", checkPendingActions);

          // Return initialization flag
          return true;
        }
      };
      return Button;
    }
  ]);