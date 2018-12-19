import { aweApplication } from "./../awe";

// Panel service
aweApplication.factory('Panel',
  ['Component',
    /**
     * Numeric generic methods
     * @param {Service} Component
     */
    function (Component) {
      var Panel = {
        /**
         * Initialize numeric criteria
         * @param {Object} controller Panel controller
         * @param {Scope} scope Panel scope
         * @param {string} identifier Panel id
         */
        init: function (controller, scope, identifier) {
          // Init as component
          var component = new Component(scope, identifier);
          if (!component.init()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }

          /**
           * Click button function
           */
          scope.isActive = function () {
            return controller.isActive(identifier);
          };

          /**
           * Event listeners
           */
          component.listeners = component.listeners || {};

          // Select current tab
          component.listeners['selectCurrentTab'] = scope.$on("selectCurrentTab", function () {
            controller.selectTab(identifier);
          });
        }
      };

      return Panel;
    }
  ]);