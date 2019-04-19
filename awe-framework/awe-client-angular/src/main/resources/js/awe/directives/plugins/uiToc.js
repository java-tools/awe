import { aweApplication } from "./../../awe";

// Toc plugin
aweApplication.directive('uiToc',
  ['AweUtilities',
    /**
     * @param {Service} Utilities
     */
    function (Utilities) {
      return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
          let initialized = false;

          // Watch for controller changes
          let initWatch = scope.$watch(attrs.uiToc, initPlugin);

          /**
           * Plugin initialization
           */
          function initPlugin() {
            // Initialize element with options
            updatePlugin();

            // Unwatch initialization
            initWatch();
          }

          /**
           * Plugin destruction
           */
          function destroyPlugin() {
            // Destroy toc if created
            if (initialized) {
              initialized = false;
            }
          }

          /**
           * Plugin update
           */
          function updatePlugin() {
            // Destroy toc if created
            destroyPlugin();

            // Initialize element with options
            elem.tocify({
              context: $("#" + attrs.uiToc),
              scrollContext: $("#" + attrs.scroll),
              highlightOffset: 0
            });
            initialized = true;
          }

          let listeners = {};
          // Watch for language change
          listeners["languageChanged"] = scope.$on('languageChanged', updatePlugin);

          /**
           * Destroy plugin
           */
          function destroy() {
            // Destroy the plugin
            destroyPlugin();

            // Clear listeners
            Utilities.clearListeners(listeners);
          }

          // Observe destroy event
          elem.on("$destroy", destroy);
        }
      };
    }
  ]);
