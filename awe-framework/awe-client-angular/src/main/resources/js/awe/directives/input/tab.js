import {aweApplication} from "./../../awe";
import "../plugins/uiTabdrop";
import "../tabcontainer";
import "../../services/panelable";

// Template
const template =
`<div class="tab-container tab-container-{{::size}} expand expandible-vertical" ng-class="{'maximized': maximized, 'maximizing': maximizing, 'resizing resizeTarget': panelResizing, 'expand': isExpandible || maximized}" ui-dependency="dependencies" ng-attr-criterion-id="{{::controller.id}}" ng-cloak>
    <awe-context-menu ng-cloak></awe-context-menu>
    <div class="{{::criterionClass}} expand expandible-vertical">
      <ul class="nav nav-tabs" ui-tabdrop ng-class="{disabled:isDisabled()}">
          <li ng-class="{'active':$parent.model.selected === tab.value}" ng-repeat="tab in model.values track by tab.value"
              ng-attr-id="tab-{{::tab.value}}">
          <a ng-click="clickTab(tab.value)">
            <i ng-if="::tab.icon" class="panel-title-icon fa fa-{{::tab.icon}}"></i>
            <span translate-multiple="{{::tab.label}}"></span>
          </a>
        </li>
        <li ng-if="::maximize" class="maximize-handler pull-right active">
          <a>
            <button role="button" type="button" class="maximize-button" aria-hidden="true" ng-click="togglePanel()" title="{{togglePanelText| translate}}">
              <i class="fa {{iconMaximized ? 'fa-compress' : 'fa-expand'}}"></i>
            </button>
          </a>
        </li>
      </ul>
      <div ng-transclude class="tab-content maximize-content expand expandible-vertical"></div>
    </div>
  </div>`;

// Tab directive
aweApplication
  .controller('TabController', ["$scope", "$element", "Panelable", "AweUtilities", "Maximize",
    function ($scope, $element, Panelable, $utilities, Maximize) {
      // Initialize criterion
      let $ctrl = this;

      // Initialize criterion
      let component = new Panelable($scope, $scope.tabId, $element);
      if (!component.asPanelable()) return false;

      /******************************************************************************
      * CONTROLLER METHODS
      *****************************************************************************/

      /**
      * Event on click tab
      * @param {type} value
      */
      $ctrl.selectTab = function (value) {
        component.model.selected = value;
        component.modelChange();

        function resizeAction() {
          $utilities.publishFromScope("resize-action", {}, $scope);
        }

        $utilities.timeout(resizeAction);
        $utilities.timeout(resizeAction, 250);
      };

      /**
      * Check if tab is active
      * @param id Tab identifier
      * @returns {*|boolean}
      */
      $ctrl.isActive = (id) => component.isActive(id);

      /******************************************************************************
      * COMPONENT METHODS
      *****************************************************************************/

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
          $ctrl.selectTab(value);
        }
      };

      /******************************************************************************
      * INIT MAXIMIZE
      *****************************************************************************/

      if (component.controller.maximize) {
        Maximize.initMaximize($scope, $element);
      }
    }])
  .directive('aweInputTab',
    function () {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        template: template,
        scope: {
          'tabId': '@inputTabId'
        },
        controller: 'TabController'
      };
    });
