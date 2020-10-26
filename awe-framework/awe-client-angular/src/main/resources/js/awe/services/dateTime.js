import {aweApplication} from "./../awe";
import "../directives/plugins/uiDate";
import "../directives/plugins/uiTime";
import moment from "moment";

// Date and time service
aweApplication.factory('DateTime',
  ['Criterion', 'AweUtilities', 'AweSettings',
    /**
     * @constructor
     * @param {object} Criterion
     * @param {object} Utilities Utilities service
     * @param {object} $settings $settings service
     */
    function (Criterion, Utilities, $settings) {

      /**
       * @constructor
       * @param {Scope} scope DateTime scope
       * @param {String} id DateTime id
       * @param {String} element DateTime element
       */
      function DateTime(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.validDates = {};
        this.validMonths = {};
        this.validYears = {};
        this.component = new Criterion(this.scope, this.id, this.element);
        let datetime = this;
        this.component.asDate = function () {
          return datetime.asDate();
        };
        this.component.asFilteredDate = function () {
          return datetime.asFilteredDate();
        };
        this.component.asTime = function () {
          return datetime.asTime();
        };
        return this.component;
      }
      DateTime.prototype = {
        /**
         * Initialize date criteria
         */
        asDate: function () {
          // Set date options
          let dateOptions = {
            container: "body",
            format: "dd/mm/yyyy",
            todayHighlight: true,
            todayBtn: 'linked',
            autoclose: true,
            enableOnReadonly: false,
            maxViewMode: 2,
            language: $settings.getLanguage()
          };

          // Define type as text
          let component = this.component;

          // Initialize criterion
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          // Update date options
          updateDateOptions(dateOptions, component);

          /**
           * Update the model on model changed
           */
          component.onSelectedChanged = function () {
            if (component.pluginInitialized) {
              component.updateModelSelected();
            }
          };

          /**
           * Event listeners
           */
          component.listeners = component.listeners || {};

          // Action listener definition
          Utilities.defineModelChangeListeners(component.listeners, {scope: this.scope, check: ["selected"], service: component, method: "onSelectedChanged"});

          // Store options
          this.scope.aweDateOptions = dateOptions;

          // Return initialization flag
          return true;
        },
        /**
         * Initialize filtered date criteria
         */
        asFilteredDate: function () {
          let component = this.component;
          let dateTime = this;
          // Set filtered date options
          let filteredDateOptions = {
            container: "body",
            format: "dd/mm/yyyy",
            todayHighlight: true,
            todayBtn: true,
            autoclose: true,
            enableOnReadonly: false,
            maxViewMode: 2,
            language: $settings.getLanguage()
          };

          // Define type as text
          component.pluginInitialized = false;

          // Initialize criterion
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          // Initialize valid dates
          let filterValidDates = function () {
            dateTime.validDates = {};
            dateTime.validMonths = {};
            dateTime.validYears = {};
            _.each(component.model.values, function (date) {
              let momentDate = moment(date.value, "DD/MM/YYYY");
              dateTime.validDates[date.value] = true;
              dateTime.validMonths[momentDate.format('MMYYYY')] = true;
              dateTime.validYears[momentDate.format('YYYY')] = true;
            });
          };

          /**
           * Check if dates are valid
           * @param {string} date
           * @param {string} format date format
           * @param {object} filteredSet Set to check
           * @returns {boolean}
           */
          let filterDates = function (date, format, filteredSet) {
            return moment(date).format(format) in filteredSet;
          };

          // Set filtered dates in dates with a day value
          filteredDateOptions.beforeShowDay = (date) =>  filterDates(date, 'DD/MM/YYYY', dateTime.validDates);
          filteredDateOptions.beforeShowMonth = (date) =>  filterDates(date, 'MMYYYY', dateTime.validMonths);
          filteredDateOptions.beforeShowYear = (date) =>  filterDates(date, 'YYYY', dateTime.validYears);

          // Update date options
          updateDateOptions(filteredDateOptions, component);

          /**
           * Update the model on model changed
           */
          component.onModelChanged = function () {
            filterValidDates();

            if (component.pluginInitialized) {
              component.updateModelValues();
            }
          };

          /**
           * Update the model on model changed
           */
          component.onSelectedChanged = function () {
            if (component.pluginInitialized) {
              component.updateModelSelected();
            }
          };

          // Update model at initial load
          filterValidDates();

          /**
           * Event listeners
           */
          component.listeners = component.listeners || {};

          // Action listener definition
          Utilities.defineModelChangeListeners(component.listeners, {scope: this.scope, check: ["values"], service: component, method: "onModelChanged"});
          Utilities.defineModelChangeListeners(component.listeners, {scope: this.scope, check: ["selected"], service: component, method: "onSelectedChanged"});

          // Store options
          this.scope.aweDateOptions = filteredDateOptions;

          // Return initialization flag
          return true;
        },
        /**
         * Initialize time criteria
         */
        asTime: function () {
          let component = this.component;
          // Set time options
          let timeOptions = {
            minuteStep: 1,
            showSeconds: true,
            secondStep: 1,
            showInputs: false,
            showMeridian: false,
            defaultTime: false
          };

          // Initialize criterion
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          // Update date options
          updateDateOptions(timeOptions, component);

          // Store options
          component.scope.aweTimeOptions = timeOptions;

          // Return initialization flag
          return true;
        }
      };

      /**
       * Update date options
       *
       * @param {Object} dateOptions Date options
       * @param {Object} component of Criteria
       */
      function updateDateOptions(dateOptions, component) {
        // Check flag showWeekends
        if ('showWeekends' in component.controller && !component.controller['showWeekends']) {
          dateOptions.daysOfWeekDisabled = [0, 6];
        }
        // Check flag showFutureDates
        if ('showFutureDates' in component.controller && !component.controller['showFutureDates']) {
          // Get selected value to disable next future dates
          var targetDate = component.model.selected;
          if (targetDate) {
            dateOptions.endDate = targetDate;
          }
        }

        // Check date format
        if ('dateFormat' in component.controller && component.controller['dateFormat'] !== null) {
          dateOptions.format = component.controller['dateFormat'];
        }

        // Check flag of Today button
        if ('showTodayButton' in component.controller && component.controller['showTodayButton'] !== null) {
          dateOptions.todayBtn = component.controller['showTodayButton'] ? "linked" : false;
        }

        // Check view mode
        if ('dateViewMode' in component.controller && component.controller['dateViewMode'] !== null) {
          dateOptions.minViewMode = component.controller['dateViewMode'];
        }
      }

      return DateTime;
    }
  ]);