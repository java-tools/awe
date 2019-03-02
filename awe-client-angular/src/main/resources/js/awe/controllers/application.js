import { aweApplication } from "./../awe";

// Application controller
aweApplication.controller('AppController',
  ['$scope', '$log', 'LoadingBar', 'ServerData', 'Storage', 'AweUtilities',
    /**
     * Control the base application behaviour
     * @param {type} $scope
     * @param {type} $log
     * @param {type} loadingBar
     * @param {type} ServerData
     * @param {type} Storage
     * @param {type} Utilities
     */
    function ($scope, $log, loadingBar, ServerData, Storage, Utilities) {
      // Define root scope
      var $root = $scope.$root;

      // Initialize controller, model, messages and api
      Storage.put("controller", {});
      Storage.put("model", {});
      Storage.put("messages", {});
      Storage.put("screen", {});
      Storage.put("screenData", {});
      Storage.put("api", {});
      Storage.put("status", {});

      // Initialize rootScope attributes
      $root.stackPosition = window.history.length - 1;
      $root.activeDependencies = true;

      var $window = $(window);
      var ON_UNLOAD = "onunload";

      // Show loading message
      $root.loading = true;

      // View is resizing
      $scope.resizing = true;

      // Navigator is IE
      var browser = Utilities.getBrowser();
      $scope.isIE = browser === "ie" ? browser : "not-ie " + browser;

      /**
       * Broadcasts a resize event
       */
      var resize = function () {
        $scope.$apply(function () {
          Utilities.publish('resize');
        });
      };

      /**
       * Broadcasts a resize action
       */
      var resizeAction = function () {
        Utilities.publish('resize-action');
      };

      /**
       * Initialise loading
       */
      var initLoading = function () {
        $root.loading = true;
      };

      /**
       * Finish loading
       */
      var endLoading = function () {
        $root.loading = false;
      };

      /**
       * Check on unload
       * @param {type} screenParameters
       * @param {type} closingTab
       */
      var checkUnload = function (screenParameters, closingTab) {
        var onunload = null;
        if (ON_UNLOAD in screenParameters) {
          onunload = screenParameters[ON_UNLOAD];
        }
        if (onunload) {
          if (closingTab) {
            var message = {};
            message[$root.settings.serverActionKey] = 'maintain-async';
            message[$root.settings.targetActionKey] = onunload;

            ServerData.send(message);

            // Time for browser to send ajax
            for (var i = 0; i < 10000000; i++) {
            }
          } else {
            ServerData.sendMaintain({type: "maintain-silent", maintain: onunload}, false, true);
          }
        }
      };

      // Route change start (hide loading message)
      $scope.$on('$viewContentLoading', function (/*event, viewConfig*/) {
        //$log.debug("$viewContentLoading", {evento: event, config: viewConfig})
      });

      // Route change start (hide loading message)
      $scope.$on('$viewContentLoaded', function (/*event*/) {
        //Launch a resize on view content loaded
        //$log.debug("$viewContentLoaded", {evento: event})
      });

      // Route change start (show loading message)
      $scope.$on('$stateChangeStart', function (event, toState, toParams, fromState/*, fromParams*/) {
        // Prevent start if settings are still not defined
        if (!Storage.getRoot("connection")) {
          return event.preventDefault();
        }

        var views = _.merge({}, fromState.views, toState.views);
        _.each(views, function (view, viewName) {
          $scope.$broadcast("unload", viewName);
        });
        //$log.debug("$stateChangeStart", {evento: event, to: {state: toState, params: toParams}, from: {state: fromState, params: fromParams}})
        $scope.resizing = true;
        loadingBar.startTask();

        var views = {};
        // Retrieve view from target state
        for (var view in toState.views) {
          views[view] = true;
        }
        // Retrieve view from source state
        for (var view in fromState.views) {
          views[view] = true;
        }

        // For each model retrieve unload and launch
        var screen = Storage.get("screen");
        _.each(screen, function (screenView, viewId) {
          if (viewId in views) {
            checkUnload(screenView, false);
          }
        });
      });

      // Route change start (hide loading message)
      $scope.$on('$stateChangeSuccess', function (/*event, toState, toParams, fromState, fromParams*/) {
        // $log.debug("$stateChangeSuccess", {evento: event, to: {state: toState,params: toParams}, from: {state: fromState,params: fromParams}})
        $scope.resizing = false;
      });

      // Route change start (hide loading message)
      $scope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams, error) {
        $root.loading = false;
        $scope.resizing = false;
        loadingBar.end();
        $log.warn("State '" + toState.name + "' rejected: " + error);
      });

      // Route state not found
      $scope.$on('$stateNotFound', function (event, current/*, previous, reject*/) {
        //do you work here
        $root.loading = false;
        loadingBar.end();
        $log.warn("State not found: " + current);
      });

      // Window resize event
      $window.on('resize', resize);
      // Launch message action
      $scope.$on('/action/resize', function (event, action) {
        var parameters = action.attr("parameters");
        var delay = parameters ? parameters.delay || 0 : 0;
        Utilities.timeout(function () {
          resizeAction();
          // Finish screen action
          action.accept();
        }, delay);
      });

      // Manage init loading and end loading
      $scope.$on('cfpLoadingBar:started', initLoading);
      $scope.$on('cfpLoadingBar:completed', endLoading);

      // On loaded screen
      $scope.$on('initialised', function () {
        Utilities.timeout(function () {
          loadingBar.endTask();
        }, 100);
      });

      // Remove splash screen & friends
      $("#splash").fadeOut(250, () => $(this).remove());

      /**
       * Check for unload maintain actions in opened views
       */
      function onbeforeunload() {
        var screen = Storage.get("screen");
        _.each(screen, function (screenView) {
          checkUnload(screenView, true);
        });
      }

      window.onbeforeunload = onbeforeunload;
    }
  ]);