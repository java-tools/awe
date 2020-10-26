import {aweApplication} from "./../awe";

// Panel service
aweApplication.factory('Panelable',
  ['Criterion', 'AweUtilities', 'Storage', '$translate', 'AweSettings',
    /**
     * @constructor Panelable generic methods
     * @param {object} Criterion
     * @param {object} $utilities
     * @param {object} $storage
     * @param {object} $translate
     */
    function (Criterion, $utilities, $storage, $translate, $settings) {

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

      /**
       * Panelable element
       * @param scope
       * @param id
       * @param element
       * @constructor
       */
      function Panelable (scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Criterion(this.scope, this.id, this.element);
        this.initialized = false;
        let panelable = this;
        this.component.asPanelable = () => this.asPanelable();

        /******************************************************************************
         * SCOPE METHODS
         *****************************************************************************/

        /**
         * Check if wizard tab is disabled
         * @returns {boolean} Wizard tab is disabled
         */
        this.scope.isDisabled = function () {
          return $storage.get("actions-running") || panelable.scope.$root.loading ||
            (panelable.component.controller && panelable.component.controller.disabled);
        };

        return this.component;
      }

      Panelable.prototype = {
        asPanelable: function () {
          let panelable = this;
          if (!this.component.asCriterion()) return false;

          // Select first option in case of selected is null
          if (!this.component.model.selected && this.component.model.values.length > 0) {
            this.component.model.selected = this.component.model.values[0].value;
          }

          // Set component methods
          this.component.getPrintData = () => panelable.getPrintData(panelable.component);
          this.component.getVisibleValue = () => panelable.getVisibleValue(panelable.component);
          this.component.isActive = (ide) => panelable.isActive(ide);
          this.component.findIndex = (ide) => panelable.findIndex(ide);

          // Disable reset and restore
          this.component.onReset = $utilities.noop;
          this.component.onRestore = $utilities.noop;

          return true;
        },
        /**
         * Find the index of the selected value
         * @param {type} value
         * @returns {number} index
         */
        findIndex: function (value) {
          let index = 0;
          _.each(this.component.model.values, function (valueObject, valueIndex) {
            if (valueObject.value === value) {
              index = valueIndex;
            }
          });
          return index;
        },
        /**
         * Extra data function (To be overwritten on complex directives)
         * @returns {Object} Data from criteria
         */
        getPrintData: function (component) {
          // Initialize data
          let data = component.getData();
          if (component.controller.printable) {
            data[component.address.component + $settings.get("dataSuffix")] = {
              text: component.getVisibleValue(),
              all: component.model.values
            };
          }
          return data;
        },
        /**
         * Retrieves visible value for the selector
         * @returns {string} visible value
         */
        getVisibleValue: function (component) {
          return getSelectedLabel(component.model.selected, component.model.values);
        },
        /**
         * Check if tab pane is active
         * @param {String} identifier Panel identifier
         * @returns {boolean} Panel is active
         */
        isActive: function (identifier) {
          return identifier === this.component.model.selected;
        }
      };

      return Panelable;
    }
  ]);