import { aweApplication } from "./../awe";

// Context menu service
aweApplication.factory('ContextMenu',
  ['AweUtilities',
    /**
     * Context menu generic methods
     * @param {service} Utilities
     */
    function (Utilities) {
      /**
       * Context menu creator
       * @param {type} scope
       * @param {type} elem
       * @param {type} data
       */
      function ContextMenu(scope, elem, data) {
        this.scope = scope;
        this.node = elem;
        this.visible = false;
        this.data = data;
      }
      ContextMenu.prototype = {
        /**
         * Initialize ContextMenu
         */
        init: function () {
          // Add hide context menu event
          let _self = this;
          let hide = function () {
            _self.hide();
          };
          this.scope.$on("hideContextMenu", hide);
          this.scope.$on("$destroy", hide);
        },
        /**
         * Show the context menu
         * @param {event} event
         */
        show: function (event) {
          // Test element
          if (!this.element && !this.node.is(".context-menu")) {
            this.element = $(this.node.siblings(".context-menu"));
          }

          // Change menu position
          let contextMenu = this;
          let parentOffset = this.element.parent().offset();
          this.element.css({
            top: event.clientY - parentOffset.top - 16,
            left: event.clientX - parentOffset.left - 16
          });

          // Create a mask panel to hide the context menu
          this.mask = $("<div class='component-mask'></div>");
          $("body").append(this.mask);
          this.mask.on("click contextmenu mouseup", function () {
            // Cancel default event
            event.preventDefault();
            Utilities.timeout(function () {
              contextMenu.hide();
            });
            return false;
          });

          // Show the context menu
          contextMenu.visible = true;
        },
        /**
         * Hide the context menu
         */
        hide: function () {
          if (this.visible) {
            // Hide the context menu
            this.visible = false;

            // Remove mask
            this.mask.remove();
          }
        },
        /**
         * Retrieve the visibility of the context menu
         * @return {boolean} Context menu is visible or not
         */
        isVisible: function () {
          return this.visible;
        }
      };
      return ContextMenu;
    }]);
