import { aweApplication } from "./../awe";
import { ClientActions } from "../data/actions";
import "./../services/screen";

// Manage the screen requests
aweApplication.controller("ScreenController",
  ['$scope', 'AweSettings', 'ActionController', 'DependencyController', 'Screen',
    /**
     * Control screen data
     *
     * @param {object} $scope
     * @param {object} $settings
     * @param {object} $actionController
     * @param {object} $dependencyController
     * @param {object} $screen
     */
    function ($scope, $settings, $actionController, $dependencyController, $screen) {
      // Controller definition
      let $ctrl = this;

      /**
       * Whether to show actions or not
       * @returns {Boolean}
       */
      $ctrl.showActions = () => $settings.get("actionsStack") > 0;

      /**
       * Show action information
       * @param {object} action
       */
      $ctrl.showInfo = (action) => console.info(action);

      // Sync stack lists
      $ctrl.syncStacks = $actionController.actionStackList;

      // Async stack
      $ctrl.asyncStack = $actionController.asyncStackList;

      // Define listeners
      _.each(ClientActions.screen, function (actionOptions, actionId) {
        $scope.$on("/action/" + actionId, (event, action) => $screen[actionOptions.method](action));
      });
      // On model change launch dependency
      $scope.$on("modelChanged", (event, launchers) => $dependencyController.checkAndLaunch(launchers));

      // On screen compiled
      $scope.$on('compiled', (event, view) => $dependencyController.start(view));

      // On cell compiled
      $scope.$on('initialize-cell', (event, address) => $dependencyController.restart(address));

      // Unload dependencies
      $scope.$on("unload", (event, view) => $dependencyController.unregisterView(view));

    }]);