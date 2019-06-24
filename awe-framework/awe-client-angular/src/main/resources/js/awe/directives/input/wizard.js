import {aweApplication} from "./../../awe";
import {ClientActions} from "../../data/actions";
import "../wizardpanel";
import "../../services/panelable";

// Template
const template =
`<div class="wizard wizard-{{::size}} expand expandible-vertical" ui-dependency="dependencies" ng-attr-criterion-id="{{::controller.id}}" ng-cloak>
 <awe-context-menu ng-cloak></awe-context-menu>
 <div class="{{::criterionClass}} expand expandible-vertical">
  <div class="wizard-wrapper">
    <ul class="wizard-steps" ng-class="{disabled:isDisabled()}">
      <li ng-class="{'active':model.selectedIndex === $index, 'completed':model.selectedIndex > $index}" ng-repeat="wizard in model.values track by wizard.value" ng-click="clickTab(wizard.value)">
        <span class="wizard-step-number">{{$index + 1}}</span>
        <span class="wizard-step-caption">
          <span translate-multiple="{{::controller.label}}"></span> {{$index + 1}}
          <span class="wizard-step-description" translate-multiple="{{::wizard.label}}"></span>
        </span>
        </li>
      </ul>
    </div>
    <div ng-transclude class="wizard-content wizard-panel wizard-panel-{{::size}} expand expandible-vertical"></div>
  </div>
</div>`;

// Wizard directive
aweApplication
  .controller('WizardController', ["$scope", "$element", "Panelable", "AweUtilities",
    function ($scope, $element, Panelable, $utilities) {
      let $ctrl = this;

      // Initialize criterion
      let component = new Panelable($scope, $scope.wizardId, $element);
      if (!component.asPanelable()) return false;

      // Select first option in case of selected is null
      component.model.selectedIndex = component.model.selected ? component.findIndex(component.model.selected) : 0;

      // Animation variables (retrieve from $settings)
      let animationTime = 300;
      let useCSS3Animation = true;
      let minStepWidth = 200;

      // Define variables for resize
      let steps, stepContainer, prevWidth, stepWidth;

      /******************************************************************************
       * CONTROLLER METHODS
       *****************************************************************************/

      /**
       * Event on click tab
       * @param {type} value
       */
      $ctrl.selectTab = function (value) {
        // Retrieve the selected tab
        component.model.selected = value;
        component.model.selectedIndex = component.findIndex(value);
        component.modelChange();
        // If move steps, move the current step viewer
        if (component.moveSteps) {
          moveStepsTo(component.model.selectedIndex);
        }
        // Broadcast a resize
        $utilities.publishDelayedFromScope("resize-action", {}, $scope);
      };

      /**
       * Check if wizard panel is active
       * @param id panel identifier
       * @returns {*|boolean}
       */
      $ctrl.isActive = (id) => component.isActive(id);

      /******************************************************************************
       * SCOPE METHODS
       *****************************************************************************/

      /**
       * Event on click tab
       * @param {type} value
       */
      $scope.clickTab = function (value) {
        let index = component.findIndex(value);
        if (index < component.model.selectedIndex) {
          $ctrl.selectTab(value);
        }
      };

      /******************************************************************************
       * PRIVATE METHODS
       *****************************************************************************/

      /**
       * Resize wizard steps
       */
      function initSteps() {
        // Retrieve step container (once)
        if (!stepContainer) {
          stepContainer = $element.find('.wizard-steps');
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
        let width = $element.width();

        // Check if width has changed
        if (width !== prevWidth) {
          // Initialize steps
          initSteps();

          // Calculate step width
          stepWidth = steps[0].offsetWidth;

          // Retrieve step width
          if (stepWidth <= minStepWidth) {
            component.moveSteps = true;
            moveStepsTo(component.model.selectedIndex);
          } else {
            component.moveSteps = false;
            stepContainer.animate({left: 0});
          }

          // Store previous width
          prevWidth = width;
        }
      }

      /**
       * Move steps to the selected index
       * @param {int} position Move the steps to show the position
       */
      function moveStepsTo(position) {
        // Initialize steps
        initSteps();

        // Retrieve wizard width
        let width = $element.width();

        // Retrieve step width
        let containerWidth = stepWidth * steps.length;
        let containerLeft = width / 2 - ((stepWidth * position) + (stepWidth / 2));
        containerLeft = Math.min(containerLeft, 0);
        containerLeft = Math.max(width - containerWidth, containerLeft);
        let animation = {left: containerLeft};
        $utilities.animate(useCSS3Animation, stepContainer, animation, animationTime);
      }

      /******************************************************************************
       * COMPONENT METHODS
       *****************************************************************************/

      /**
       * Go to next tab
       */
      component.next = function () {
        let nextTab = component.model.selectedIndex + 1;
        if (nextTab < component.model.values.length) {
          $ctrl.selectTab(component.model.values[nextTab].value);
        }
      };
      /**
       * Go to previous tab
       */
      component.prev = function () {
        let nextTab = component.model.selectedIndex - 1;
        if (nextTab >= 0) {
          $ctrl.selectTab(component.model.values[nextTab].value);
        }
      };

      /**
       * Move steps to the first tab
       */
      component.first = function () {
        let values = component.model.values;
        if (values.length > 0) {
          $ctrl.selectTab(component.model.values[0].value);
        }
      };

      /**
       * Move steps to the last tab
       */
      component.last = function () {
        let values = component.model.values;
        if (values.length > 0) {
          $ctrl.selectTab(values[values.length - 1].value);
        }
      };

      /**
       * Move steps to the nth tab
       * @param parameters Action parameters
       */
      component.nth = function (parameters) {
        $ctrl.selectTab(parameters.value);
      };

      /******************************************************************************
       * EVENT LISTENERS
       *****************************************************************************/
      component.listeners = component.listeners || {};
      // Capture event for element resize
      component.listeners['resize'] = $scope.$on("resize", resize);
      component.listeners['resize-action'] = $scope.$on("resize-action", resize);
      // Action listener definition
      $utilities.defineActionListeners(component.listeners, ClientActions.wizard, $scope, component);
    }])
  .directive('aweInputWizard',
    function () {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        template: template,
        scope: {
          'wizardId': '@inputWizardId'
        },
        controller: 'WizardController'
      };
    }
  );
