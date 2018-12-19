import { aweApplication } from "./../../awe";

// Wizard directive
aweApplication.directive('aweInputWizard',
  ['ServerData', 'Criterion', 'AweUtilities', 'Storage', 'Actions', '$translate', 'AweSettings',
    function (serverData, Criterion, Utilities, Storage, Actions, $translate, $settings) {

      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/wizard');
        },
        scope: {
          'wizardId': '@inputWizardId'
        },
        controller: ["$scope", function ($scope) {
            // Initialize criterion
            let component = new Criterion($scope, $scope.wizardId, $scope.element);
            if (!component.asCriterion()) {
              // If criterion is wrong, cancel initialization
              return false;
            }

            // Select first option in case of selected is null
            component.model.selected = component.model.values[0].value;
            component.model.selectedIndex = 0;
            let controller = this;

            // Animation variables (retrieve from $settings)
            let animationTime = 300;
            let useCSS3Animation = true;
            let minStepWidth = 200;

            // Define variables for resize
            let steps, stepContainer, prevWidth, stepWidth;

            /******************************************************************************
             * COMPONENT METHODS
             *****************************************************************************/

            /**
             * Extra data function (To be overwritten on complex directives)
             * @returns {Object} Data from criteria
             */
            component.getPrintData = function () {
              // Initialize data
              let data = component.getData();
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
              return identifier === component.model.selected;
            };

            /**
             * Event on click tab
             * @param {type} value
             */
            this.selectTab = function (value) {
              // Retrieve the selected tab
              component.model.selected = value;
              component.model.selectedIndex = findIndex(value);
              component.modelChange();
              // If move steps, move the current step viewer
              if (component.moveSteps) {
                component.moveStepsTo(component.model.selectedIndex);
              }
              // Broadcast a resize
              Utilities.publishDelayedFromScope("resize-action", {}, $scope);
            };


            /******************************************************************************
             * SCOPE METHODS
             *****************************************************************************/

            /**
             * Event on click tab
             * @param {type} value
             */
            $scope.clickTab = function (value) {
              let index = findIndex(value);
              if (index < component.model.selectedIndex) {
                controller.selectTab(value);
              }
            };
            /**
             * Check if wizard tab is disabled
             * @returns {boolean} Wizard tab is disabled
             */
            $scope.isDisabled = function () {
              return Storage.get("actions-running") || $scope.$root.loading ||
                (component.controller && component.controller.disabled);
            };


            /******************************************************************************
             * PRIVATE METHODS
             *****************************************************************************/

            /**
             * Find the index of the selected value
             * @param {type} value
             * @returns {number} index
             */
            function findIndex(value) {
              let index = 0;
              _.each(component.model.values, function (valueObject, valueIndex) {
                if (valueObject.value === value) {
                  index = valueIndex;
                }
              });
              return index;
            }

            /**
             * Resize wizard steps
             */
            function initSteps() {
              // Retrieve step container (once)
              if (!stepContainer) {
                stepContainer = $scope.element.find('.wizard-steps');
              }
              // Retrieve steps (once)
              if (!steps) {
                steps = stepContainer.children();
              }
            }

            /**
             * Resize wizard steps
             */
            function resize() {
              // Retrieve wizard width
              let width = $scope.element.width();

              // Check if width has changed
              if (width !== prevWidth) {
                // Initialize steps
                initSteps();

                // Calculate step width
                stepWidth = steps[0].offsetWidth;

                // Retrieve step width
                if (stepWidth <= minStepWidth) {
                  component.moveSteps = true;
                  component.moveStepsTo(component.model.selectedIndex);
                } else {
                  component.moveSteps = false;
                  stepContainer.animate({left: 0});
                }

                // Store previous width
                prevWidth = width;
              }
            }

            /******************************************************************************
             * COMPONENT METHODS
             *****************************************************************************/

            // Disable reset and restore
            component.onReset = Utilities.noop;
            component.onRestore = Utilities.noop;

            /**
             * Go to next tab
             */
            component.next = function () {
              let nextTab = component.model.selectedIndex + 1;
              if (nextTab < component.model.values.length) {
                controller.selectTab(component.model.values[nextTab].value);
              }
            };
            /**
             * Go to previous tab
             */
            component.prev = function () {
              let nextTab = component.model.selectedIndex - 1;
              if (nextTab >= 0) {
                controller.selectTab(component.model.values[nextTab].value);
              }
            };
            /**
             * Move steps to the selected index
             * @param {int} position Move the steps to show the position
             */
            component.moveStepsTo = function (position) {
              // Initialize steps
              initSteps();

              // Retrieve wizard width
              let width = $scope.element.width();

              // Retrieve step width
              let containerWidth = stepWidth * steps.length;
              let containerLeft = width / 2 - ((stepWidth * position) + (stepWidth / 2));
              containerLeft = Math.min(containerLeft, 0);
              containerLeft = Math.max(width - containerWidth, containerLeft);
              let animation = {left: containerLeft};
              if (useCSS3Animation) {
                Utilities.animateCSS(stepContainer, animation, animationTime);
              } else {
                Utilities.animateJavascript(stepContainer, animation, animationTime);
              }
            };

            /******************************************************************************
             * EVENT LISTENERS
             *****************************************************************************/
            component.listeners = component.listeners || {};
            // Capture event for element resize
            component.listeners['resize'] = $scope.$on("resize", resize);
            component.listeners['resize-action'] = $scope.$on("resize-action", resize);
            // Action listener definition
            Utilities.defineActionListeners(component.listeners, Actions.wizard, $scope, component);
          }],
        link: function (scope, elem) {
          scope.element = elem;
        }
      };
    }
  ]);
