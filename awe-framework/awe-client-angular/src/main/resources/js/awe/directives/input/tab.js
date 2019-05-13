import { aweApplication } from "./../../awe";
import "../plugins/uiTabdrop";
import "../tabcontainer";

// Tab directive
aweApplication.directive('aweInputTab',
  ['ServerData', 'Criterion', 'AweUtilities', 'Storage', 'Maximize', '$translate', 'AweSettings',
    function (serverData, Criterion, Utilities, Storage, Maximize, $translate, $settings) {

      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/tab');
        },
        scope: {
          'criterionId': '@inputTabId'
        },
        controller: ["$scope", function ($scope) {
            // Initialize criterion
            let controller = this;
            let component = new Criterion($scope, $scope.criterionId, $scope.element);
            if (!component.asCriterion()) {
              // If criterion is wrong, cancel initialization
              return false;
            }

            // Select first option in case of selected is null
            if (!component.model.selected && component.model.values.length > 0) {
              component.model.selected = component.model.values[0].value;
            }

            /******************************************************************************
             * COMPONENT METHODS
             *****************************************************************************/

            /**
             * Extra data function (To be overwritten on complex directives)
             * @returns {Object} Data from criteria
             */
            component.getPrintData = function () {
              // Initialize data
              var data = component.getData();
              if (component.controller.printable) {
                data[component.address.component + $settings.get("dataSuffix")] = {
                  text: component.getVisibleValue(),
                  all: component.model.values
                };
              }
              return data;
            };

            /**
             * Retrieves visible value for the selector
             * @returns {string} visible value
             */
            component.getVisibleValue = function () {
              return getSelectedLabel(component.model.selected, component.model.values);
            };

            /**
             * Get selected label
             * @param {String} selected Model
             * @param {Array} valueList Model
             * @return {String} label
             */
             function getSelectedLabel(selected, valueList) {
              let label = selected;
              _.each(valueList, function (value) {
                if (String(selected) === String(value.value)) {
                  label = $translate.instant(String(value.label));
                }
              });
              return label;
            }

            /******************************************************************************
             * CONTROLLER METHODS
             *****************************************************************************/

            /**
             * Check if tab pane is active
             * @param {String} identifier Panel identifier
             * @returns {boolean} Panel is active
             */
            this.isActive = function (identifier) {
              return identifier === $scope.component.model.selected;
            };

            /**
             * Event on click tab
             * @param {type} value
             */
            this.selectTab = function (value) {
              component.model.selected = value;
              component.modelChange();
              function resizeAction() {
                Utilities.publishFromScope("resize-action", {}, $scope);
              }
              Utilities.timeout(resizeAction);
              Utilities.timeout(resizeAction, 250);
            };

            /******************************************************************************
             * COMPONENT METHODS
             *****************************************************************************/

            // Disable reset and restore
            component.onReset = Utilities.noop;
            component.onRestore = Utilities.noop;
            // Controller variables
            component.scope.isExpandible = true;
            component.expandDirection = "vertical";

            /******************************************************************************
             * SCOPE METHODS
             *****************************************************************************/

            /**
             * Event on click tab
             * @param {type} value
             */
            $scope.clickTab = function (value) {
              if (!$scope.isDisabled()) {
                controller.selectTab(value);
              }
            };
            /**
             * Check if tab is disabled
             * @returns {boolean} tab is disabled
             */
            $scope.isDisabled = function () {
              return Storage.get("actions-running") || $scope.$root.loading ||
                (component.controller && component.controller.disabled);
            };
          }],
        link: function (scope, elem) {
          scope.element = elem;

          // Define maximize options
          if (scope.component.controller.maximize) {
            Maximize.initMaximize(scope, elem);
          }
        }
      };
    }
  ]);
