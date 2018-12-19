import { aweApplication } from "./../awe";

// Manage the view requests
aweApplication.controller("ViewController",
  ['$scope', 'ServerData', 'Storage', 'ActionController', 'AweUtilities', 'Load', 'LoadingBar', '$log', 'screenData', 'context',
    /**
     * Control screen data
     *
     * @param {service} $scope
     * @param {service} ServerData
     * @param {service} Storage
     * @param {service} ActionController
     * @param {service} Utilities
     * @param {service} Load
     * @param {service} loadingBar
     * @param {service} $log
     * @param {type} view
     * @param {type} context
     */
    function ($scope, ServerData, Storage, ActionController, Utilities, Load, loadingBar, $log, view, context) {

      // Initialize scope view
      loadingBar.end();
      var screenData = Storage.get("screenData");
      var data = screenData[view];
      var sendInitialization = true;
      // If there's no controller in data, probably the request has been cancelled
      if (data) {
        if (typeof data !== "object") {
          $log.error("[ERROR] Loading view (screen data is not an object)", {view: view, screenData: screenData, context: context});

          // Send error message
          ActionController.sendMessage({view:view, context:context}, 'error', 'ERROR_TITLE_SCREEN_GENERATION_ERROR', data);
        } else {
          if ('components' in data) {
            sendInitialization = false;
            // Start loading bar task
            loadingBar.startTask();
            $scope.view = view;
            $scope.context = context;

            // Store data
            ServerData.storeScreenData(data, view);

            // Check controller
            if(data.screen){
              $scope.$root.screen = data.screen;
            }

            // Clear data
            screenData[view] = null;

            // Start loading phase
            var load = new Load($scope, view, data.components);
            load.start();
          }
        }
      } else {
        $log.error("[ERROR] Loading view", {view: view, screenData: screenData, context: context});
      }

      // Check whether to send initialization if it's not delayed
      if (sendInitialization) {
        Utilities.publish('initialised');
      }

      // Hack to hide screen in IE before dirty change
      $scope.$on("unload", function ($event, viewName) {
        // Navigator is IE
        var browser = Utilities.getBrowser();
        var ie = browser === "ie";
        if (viewName === view && ie) {
          $scope.visible = false;
        }
      });
    }
  ]);