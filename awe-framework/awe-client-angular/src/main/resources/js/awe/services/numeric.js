import {aweApplication} from "./../awe";
import "../directives/plugins/uiNumeric";
import "../directives/plugins/uiSlider";

// Numeric template
export const templateNumeric =
`<div ng-show="controller.visible" class="criterion {{criterionClass}}" ui-dependency="dependencies" ng-attr-criterion-id="{{::controller.id}}" ng-cloak>
  <awe-context-menu ng-cloak></awe-context-menu>
  <div ng-class="::groupClass" ng-cloak>
    <label ng-attr-for="{{::controller.id}}" ng-class="::labelClass" ng-style="::labelStyle" ng-cloak>
      <i ng-if="::controller.help" class="help-target fa fa-fw fa-question-circle"></i>
      {{controller.label| translateMultiple}}
    </label>
    <div class="validator input  {{::validatorGroup}} focus-target" ng-class="{'input-group': controller.unit}">
      <span ng-if="::controller.icon" ng-class="::iconClass" ng-cloak></span>
      <input ui-numeric="aweNumericOptions" class="form-control text-right {{classes}}" autocomplete="off" ng-click="click($event)"
             ng-attr-id="{{::controller.id}}" ng-attr-name="{{::controller.id}}" ng-disabled="controller.readonly" ng-press-enter="submit($event)"
             placeholder="{{controller.placeholder| translateMultiple}}" ng-focus="focus()" ng-blur="blur()"/>
      <awe-loader class="loader" ng-if="controller.loading" icon-loader="{{::iconLoader}}" ng-cloak></awe-loader>
      <span ng-if="controller.unit" class="input-group-addon unit" translate-multiple="{{controller.unit}}" ng-cloak></span>
    </div>
  </div>
  <input ng-if="::controller.showSlider" ui-slider="aweNumericOptions" ng-cloak/>
</div>`;

// Numeric template for columns
export const templateNumericColumn =
`<div ng-show="component.controller.visible" class="validator column-input criterion text-{{::component.controller.align}} no-animate" ui-dependency="dependencies" ng-cloak>
  <span class="visible-value text-right" ng-cloak>{{component.visibleValue}}</span>
  <div class="edition input input-group-{{::size}} focus-target">
    <input ui-numeric="aweNumericOptions" class="form-control text-right col-xs-12 {{classes}} {{component.model.values[0].style}}" autocomplete="off"
           ng-disabled="component.controller.readonly" ng-focus="focus()" ng-blur="blur()"
           placeholder="{{::component.controller.placeholder| translateMultiple}}" ng-click="click($event)" ng-press-enter="saveRow($event)"/>
    <span ng-if="::component.controller.icon" ng-class="::iconClass" ng-cloak></span>
  </div>
  <awe-loader class="loader no-animate" ng-if="component.controller.loading" icon-loader="{{::iconLoader}}" ng-cloak/>
</div>`;

// Numeric service
aweApplication.factory('Numeric',
  ['Criterion', 'AweUtilities', 'AweSettings', 'Control',
    /**
     * Numeric generic methods
     * @param {object} Criterion
     * @param {object} Utilities
     * @param {object} $settings
     * @param {object} Control
     */
    function (Criterion, Utilities, $settings, Control) {
      /**
       * Numeric constructor
       * @param {Scope} scope Numeric scope
       * @param {String} id Numeric id
       * @param {String} element Numeric element
       */
      function Numeric(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Criterion(this.scope, this.id, this.element);
        var numeric = this;
        this.component.asNumeric = function () {
          return numeric.init();
        };
        return this.component;
      }
      Numeric.prototype = {
        /**
         * Initialize numeric criteria
         */
        init: function () {
          // Initialize criterion
          var component = this.component;
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          /**********************************************************************/
          /* PRIVATE METHODS                                                    */
          /**********************************************************************/

          // Update number format
          var updateNumberFormat = function () {
            var controller = Control.getAddressController(component.address);
            var numberFormat;
            switch (typeof controller.numberFormat) {
              case "string":
                numberFormat = Utilities.evalJSON(controller.numberFormat);
                break;
              case "object":
                numberFormat = _.cloneDeep(controller.numberFormat);
                break;
              default:
                numberFormat = {};
            }
            if (!_.isEqual(component.scope.aweNumericOptions, numberFormat)) {
              component.scope.aweNumericOptions = numberFormat;
              component.scope.$broadcast("updateNumberFormat", numberFormat);
            }
          };

          /**
           * Sanitize selected value
           * @param {object | array | number} Selected value
           * @return Sanitized value
           */
          var sanitizeModel = function (selectedValue) {
            var sanitizedValue = selectedValue;
            // Check array
            if (Array.isArray(selectedValue) && selectedValue.length > 0) {
              // Get first value
              sanitizedValue = selectedValue[0];
            }

            // Check object
            if (angular.isObject(sanitizedValue) && "value" in sanitizedValue) {
              sanitizedValue = sanitizedValue.value;
            }

            // Check object
            return sanitizedValue;
          };

          /**********************************************************************/
          /* COMPONENT METHODS                                                  */
          /**********************************************************************/

          /**
           * Extra data function (overwrite criterion getData function)
           * @returns {Object} Data from criteria
           */
          component.getData = function () {
            // Initialize data
            var data = {};
            if (component.model.selected === null) {
              data[component.address.component] = null;
            } else {
              data[component.address.component] = parseFloat(component.model.selected);
            }

            if (component.getExtraData) {
              data[component.address.component + $settings.get("dataSuffix")] = component.getExtraData();
            }
            return data;
          };

          /**
           * Call update model
           * @returns {undefined}
           */
          component.onModelChanged = function () {
            // 1.- Sanitize model
            component.model.selected = sanitizeModel(component.model.selected);

            // 2.- Update model value
            if (component.updateModel) {
              component.updateModel();
            }
          };

          /**
           * Retrieves visible value for the numeric
           * @returns {string} visible value
           */
          component.getVisibleValue = function () {
            var visibleValue = "";
            var model = Control.getAddressModel(component.address);
            // Refresh visible value if not generated yet
            if (component.updateModel) {
              component.updateModel();
            }
            if (model.values.length > 0 && model.values[0].label) {
              visibleValue = model.values[0].label;
            }
            return visibleValue;
          };


          /**********************************************************************/
          /* API METHODS                                                        */
          /**********************************************************************/
          /**
           * API link to update the model values
           * @param {object} data New model data attributes
           */
          component.api.updateModelValues = function (data) {
            var model = Control.getAddressModel(component.address);
            if (model) {
              _.merge(model, data);
              if (component.updateModel) {
                component.updateModel(model);
              }
            }
          };

          /******************************************************************************
           * EVENT LISTENERS
           *****************************************************************************/
          component.listeners = component.listeners || {};

          // Action listener definition
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, check: ["selected"], service: component, method: "onModelChanged"});

          // On number format change launch dependency
          component.listeners["controllerChange"] = component.scope.$on("controllerChange", function (event, parameters) {
            if (_.isEqual(parameters.address, component.scope.address) && "numberFormat" in parameters.controller) {
              updateNumberFormat();
            }
          });

          // Update number format at start
          updateNumberFormat();

          // Finish initialization
          return true;
        }
      };

      return Numeric;
    }
  ]);