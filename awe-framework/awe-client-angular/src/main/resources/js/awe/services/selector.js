import { aweApplication } from "./../awe";
import "../directives/plugins/uiSelect";

// Selector template
export const templateSelector =
`<div ng-show="controller.visible" class="criterion {{criterionClass}}" ui-dependency="dependencies" ng-attr-criterion-id="{{::controller.id}}" ng-cloak>
  <awe-context-menu ng-cloak></awe-context-menu>
  <div ng-class="::groupClass" ng-cloak>
    <label ng-attr-for="{{::controller.id}}" ng-class="::labelClass" ng-style="::labelStyle" ng-cloak>
      <i ng-if="::controller.help" class="help-target fa fa-fw fa-question-circle"></i>
      {{controller.label| translateMultiple}}
    </label>
    <div class="validator input {{::validatorGroup}} focus-target" ng-class="{'input-group': controller.unit}">
      <span ng-if="::controller.icon" ng-class="::iconClass" ng-cloak></span>
      <input type="hidden" ui-select2="aweSelectOptions" class="form-control {{classes}}" initialized="initialized" autocomplete="off" ng-click="click($event)"
             ng-attr-id="{{::controller.id}}" ng-attr-name="{{::controller.id}}" ng-disabled="controller.readonly"/>
      <awe-loader class="loader" ng-if="controller.loading" icon-loader="{{::iconLoader}}" ng-cloak/>
      <span ng-if="controller.unit" class="input-group-addon add-on unit" translate-multiple="{{controller.unit}}" ng-cloak></span>
    </div>
  </div>
</div>`;

// Selector template for columns
export const templateSelectorColumn =
`<div ng-show="component.controller.visible" class="validator column-input criterion text-{{::component.controller.align}} no-animate {{component.model.values[0].style}}" ui-dependency="dependencies" ng-cloak>
  <span class="visible-value" ng-cloak>{{component.visibleValue}}</span>
  <span class="edition">
    <div class="input input-group-{{::size}} focus-target">
      <input type="hidden" ui-select2="aweSelectOptions" class="form-control col-xs-12 {{classes}}" value="{{component.model.selected}}"
             ng-disabled="component.controller.readonly" initialized="initialized" autocomplete="off"/>
    </div>
    <span ng-if="::component.controller.icon" ng-class="::iconClass" ng-cloak></span>
  </span>
  <awe-loader class="loader no-animate" ng-if="component.controller.loading" icon-loader="{{::iconLoader}}" ng-cloak/>
</div>`;

// Selector service
aweApplication.factory('Selector',
  ['Control', 'Criterion', '$translate', 'AweUtilities', 'AweSettings', 'ActionController',
    /**
     * Criterion generic methods
     * @param {object} Control
     * @param {object} Criterion
     * @param {object} $translate
     * @param {object} Utilities Awe Utilities
     * @param {object} $settings Awe $settings
     * @param {object} $actionController Action controller
     */
    function (Control, Criterion, $translate, Utilities, $settings, $actionController) {
      /**
       * Format select data
       * @param {Array} values
       * @param {Array} term Term to filter
       * @return {Array} formatted data
       */
      function formatSelectData(values, term) {
        let data = [];
        _.each(values, function (element) {

          // Search for term if defined
          let search = String(term || "").toUpperCase();
          let value = String(element.value || "");
          let label = String(element.label || "");
          let translatedLabel = $translate.instant(label.toString());

          // Add element to select data
          if (value.toUpperCase().indexOf(search) !== -1 || translatedLabel.toUpperCase().indexOf(search) !== -1) {
            data.push({
              id: element.value,
              text: translatedLabel
            });
          }
        });
        return data;
      }

      /**
       * Format select data
       * @param {Array} values Model values
       * @param {Array} selected Model selected values
       * @return {Array} formatted data
       */
      function filterSelectData(values, selected) {
        let data = [];
        if (selected !== null) {
          let selectedList = Utilities.asArray(selected).map(String);
          _.each(values, function (element) {
            if ($.inArray(String(element.value), selectedList) > -1) {
              data.push({
                id: element.value,
                text: $translate.instant(String(element.label))
              });
            }
          });
        }
        return data;
      }

      /**
       * Format selected values (store in values only the selected data)
       * @param {Array} values Model values
       * @param {Array} selected Model selected values
       * @param {boolean} multiple Select multiple values
       * @return {Array} formatted data
       */
      function filterSelectedValues(values, selected, multiple) {
        let filtered = [];
        if (selected !== null) {
          let selectedList = Utilities.asArray(selected).map(String);
          _.each(values, function (element) {
            if ($.inArray(String(element.value), selectedList) > -1) {
              filtered.push(String(element.value));
            }
          });
        }
        return multiple ? filtered : filtered.length > 0 ? filtered[0] : null;
      }
      /**
       * Format select data
       * @param {Array} model
       * @return {Array} formatted data
       */
      function filterSuggestModel(model) {
        let data = [];
        let stored = [];
        if (model.selected !== null) {
          let selected = Utilities.asArray(model.selected).map(String);
          _.each([model.storedValues, model.values], function (values) {
            _.each(values, function (element) {
              let stringValue = String(element.value);
              if ($.inArray(stringValue, selected) > -1 && $.inArray(stringValue, stored) === -1) {
                data.push(element);
                stored.push(stringValue);
              }
            });
          });
        }
        model.storedValues = _.cloneDeep(data);
        model.values = data;
      }

      /**
       * Fill a callback object (in select2 format) with a model value list
       * @param {Array} values
       * @param {Object} callback Callback function
       * @param {String} term Search term
       */
      function fillCallback(values, callback, term) {
        let results = formatSelectData(values, term);
        let data = {
          results: results
        };
        callback(data);
      }

      /**
       * Fill a callback object (in select2 format) with a model value list
       * filtered by selected data
       * @param {Array} values All values
       * @param {Array} selected Selected values
       * @param {Object} callback Callback function
       * @return {Object} Data retrieved
       */
      function filterSelectedCallback(values, selected, callback) {
        let data = filterSelectData(values, selected);
        callback(data.length === 1 ? data[0] : data);
        return data;
      }

      /**
       * Check if selected is in value list
       * @param {object} component
       * @return {boolean} selected is in value list
       */
      function checkSelectedValues(component) {
        let check = false;
        let model = Control.getAddressModel(component.address);
        _.each(model.values, function (value) {
          if (String(model.selected) === String(value.value)) {
            check = true;
          }
        });
        return check;
      }

      /**
       * Select data in select2 format
       * @param {Array} selected Model
       * @param {Array} valueList Model
       * @return {Array} label list
       */
      function getSelectedLabel(selected, valueList) {
        let selectedList = Utilities.asArray(selected);
        let labelList = [];
        _.each(selectedList, function (selectedValue) {
          _.each(valueList, function (value) {
            if (String(selectedValue) === String(value.value)) {
              labelList.push($translate.instant(String(value.label)));
            }
          });
        });
        return labelList;
      }

      /**
       * Process change event
       *
       * @param {Object} component
       * @param {Object} item item to add/remove
       */
      function processChangeEvent(component, item) {
        // Check value length (of string / array)
        let model = Control.getAddressModel(component.address);
        if (item.val && item.val.length) {
          model.selected = item.val;
        } else {
          model.selected = null;
        }
        component.modelChange();
      }

      /**
       * Check if selected is in value list
       * @param {Object} component
       * @param {boolean} multiple
       */
      function fixRequiredValue(component, multiple) {
        // If not optional set first value as selected
        let model = Control.getAddressModel(component.address);
        if (!multiple && !component.controller.optional && model.values.length) {
          if (model.selected === null || !checkSelectedValues(component)) {
            model.selected = model.values[0].value;
            model["initial-selected"] = _.cloneDeep(model.selected);
            Utilities.timeout(function () {
              component.modelChange();
            });
          }
        }
      }

      /**
       * Selector constructor
       * @param {Scope} scope Selector scope
       * @param {String} id Selector id
       * @param {String} element Selector element
       */
      function Selector(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Criterion(this.scope, this.id, this.element);
        this.initialized = false;
        let selector = this;
        this.component.asSelect = function () {
          selector.multiple = false;
          return selector.asSelect();
        };
        this.component.asSuggest = function (initializable) {
          selector.multiple = false;
          selector.initializable = initializable;
          return selector.asSuggest();
        };
        this.component.asSelectMultiple = function () {
          selector.multiple = true;
          return selector.asSelect();
        };
        this.component.asSuggestMultiple = function (initializable) {
          selector.multiple = true;
          selector.initializable = initializable;
          return selector.asSuggest();
        };
        return this.component;
      }

      Selector.prototype = {
        /**
         * Initialize selector
         */
        asSelect: function () {
          // Initialize criterion
          let selector = this;
          let component = this.component;
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          /*
           * Set select2 options
           */
          let options = {
            allowClear: component.controller && component.controller.optional,
            minimumResultsForSearch: 5,
            multiple: selector.multiple,
            query: function (query) {
              let model = Control.getAddressModel(component.address);
              fillCallback(model.values, query.callback, query.term);
            },
            initSelection: function (node, callback) {
              let model = Control.getAddressModel(component.address);
              let data = filterSelectedCallback(model.values, model.selected, callback);
              if (data.length === 0) {
                model.selected = null;
              }
            }
          };
          /**
           * Retrieves visible value for the selector
           * @returns {string} visible value
           */
          component.getVisibleValue = function () {
            let model = Control.getAddressModel(component.address);
            return getSelectedLabel(model.selected, model.values).join(", ");
          };
          /**
           * Update the model on model changed
           */
          component.onModelChanged = function () {
            // Fix required value
            fixRequiredValue(component, selector.multiple);
            // Fill data
            selector.updateSelectValues(component);
          };
          /**
           * On plugin initialization
           */
          component.onPluginInit = function () {
            // Get plugin
            selector.initialized = true;
            // Fix required value
            component.onStart();
          };
          /**
           * On plugin initialization
           */
          component.onStart = function () {
            // Fix required value
            fixRequiredValue(component, selector.multiple);
            selector.updateSelectValues(component);
          };
          /**
           * On plugin change
           * @param {object} item
           */
          component.onPluginChange = function (item) {
            processChangeEvent(component, item);
          };
          /**********************************************************************/
          /* API METHODS                                                        */
          /**********************************************************************/
          /**
           * Update selected values
           * @param {type} data
           */
          component.api.updateModelValues = function (data) {
            let model = Control.getAddressModel(component.address);
            if (model) {
              _.merge(model, data);
              // If selected in data, update selected values
              if ("selected" in data) {
                // Store component model
                model.selectedValues = Utilities.asArray(data.selected);
                // Store value list
                let valueList = [];
                _.each(model.selectedValues, function (selected) {
                  if (selected !== null) {
                    if (typeof selected === "object" && "value" in selected) {
                      valueList.push(selected.value);
                    } else {
                      valueList.push(selected);
                    }
                  }
                });
                // Filter selected values
                model.selected = filterSelectedValues(model.values, valueList, selector.multiple);
                selector.selectData(model.selected);
              }
            }
          };
          /******************************************************************************
           * EVENT LISTENERS
           *****************************************************************************/
          component.listeners = component.listeners || {};
          // Action listener definition
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, service: component, method: "onModelChanged"});
          // Watch for model values change
          component.listeners['editingCell'] = component.scope.$on("editing-cell", function (event, address) {
            if (_.isEqual(address, component.address)) {
              component.onStart();
            }
          });
          // Watch for language change
          component.listeners['languageChanged'] = component.scope.$on('languageChanged', function () {
            selector.updateSelectValues(component);
            // Update visible value on language change (if defined)
            if (component.updateVisibleValue) {
              component.updateVisibleValue();
            }
          });
          // Placeholder
          component.controller.placeholder = component.controller.placeholder || 'SELECT2_SELECT_VALUE';
          options.placeholder = component.controller.placeholder;
          // Define first option as placeholder place
          options.placeholderOption = 'first';
          // Store options
          selector.scope.aweSelectOptions = options;
          // Finish initialization
          return true;
        },
        /**
         * Initialize suggest
         */
        asSuggest: function () {
          // Define searchQuery for model changes
          let searchCallback = null, initCallback = null;
          let timer = null;
          // Initialize criterion
          let selector = this;
          let component = this.component;
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          // Retrieve attributes from component
          selector.term = component.model.selected || "";
          // Generate target and initial target
          selector.target = component.controller[$settings.get("targetActionKey")];
          selector.initialTarget = component.controller.checkTarget || selector.target;
          /*
           * Set select2 options
           */
          let options = {
            allowClear: true,
            multiple: selector.multiple,
            minimumInputLength: 1,
            query: function (query) {
              // Remove timer
              searchCallback = null;
              Utilities.timeout.cancel(timer);
              // Abort last action if alive
              if (selector.lastAction) {
                $actionController.abortAction(selector.lastAction);
              }
              timer = Utilities.timeout(function () {
                searchCallback = query.callback;
                selector.term = Utilities.trim(query.term);
                component.suggest(selector.target);
              }, $settings.get("suggestTimeout"));
            },
            initSelection: function (node, callback) {
              let model = Control.getAddressModel(component.address);
              let data = filterSelectedCallback(model.values, model.selected, callback);
              if (data.length === 0) {
                model.selected = null;
              }
            }
          };
          // Fix the selected value so that it always returns an array
          if (selector.multiple) {
            component.model.selected = Utilities.asArray(component.model.selected);
          }
          // Strict value method
          if (component.controller && !component.controller.strict) {
            options.createSearchChoice = function (term) {
              let trimmedTerm = $.trim(term);
              let output = null;
              if (trimmedTerm.length > 0) {
                let newValue = {value: trimmedTerm, label: trimmedTerm};
                component.model.values.push(newValue);
                component.model.values = _.uniqWith(component.model.values, _.isEqual);
                output = {id: trimmedTerm, text: trimmedTerm};
              }
              return output;
            };
            options.createSearchChoicePosition = "bottom";
          }

          /**
           * Launch a suggest
           * @param {type} target
           * @returns {undefined}
           */
          component.suggest = function (target) {
            component.controller[$settings.get("targetActionKey")] = target;
            selector.lastAction = component.reload();
          };
          /**
           * Retrieves visible value for the selector
           * @returns {string} visible value
           */
          component.getVisibleValue = function () {
            let model = Control.getAddressModel(component.address);
            return getSelectedLabel(model.selected, model.values).join(", ");
          };
          /**
           * Basic getSpecificFields function (To be overwritten on complex directives)
           * @returns {Object} Specific fields from component
           */
          component.getSpecificFields = function () {
            // Initialize data
            return {
              max: component.getMax(0),
              suggest: Control.formatDataList(selector.term)
            };
          };
          /**
           * Update the model when model and selected have changed
           */
          component.onModelChangedValuesSelected = function () {
            // Fill data
            let model = Control.getAddressModel(component.address);
            selector.selectData(Utilities.asArray(model.selected));
            filterSuggestModel(model);
          };
          /**
           * Update the model on model changed
           */
          component.onModelChangedValues = function () {
            // Fill suggest
            let model = Control.getAddressModel(component.address);
            if (searchCallback !== null) {
              fillCallback(model.values, searchCallback, selector.term);
              searchCallback = null;
              // Fill data
            } else if (initCallback !== null) {
              let data = filterSelectedCallback(model.values, model.selected, initCallback);
              if (data.length === 0) {
                model.selected = null;
              }
              initCallback = null;
            } else {
              filterSuggestModel(model);

              // Add option if it's not strict
              let term = model.selected;
              if (!component.controller.strict && !Utilities.isEmpty(term) && model.values.length === 0) {
                model.values.push({value: term, label: term});
              }

              selector.selectData(Utilities.asArray(model.selected));
            }
          };
          /**
           * Check if selected value has a value list to fill the selector
           * @param {object} model Model
           * @returns {Boolean}
           */
          function checkSelectedValue(model) {
            let check = false;
            if (model.values.length === 0 && !Utilities.isEmpty(model.selected)) {
              selector.term = model.selected;
              component.suggest(selector.initialTarget);
              check = true;
            }
            return check;
          }

          /**
           * Update the model on selected changed
           */
          component.onModelChangedSelected = function () {
            let model = Control.getAddressModel(component.address);
            // Important: Filter first to avoid non useful values, and after that,
            // select the data to launch callback if needed
            if (!checkSelectedValue(model)) {
              filterSuggestModel(model);
              selector.selectData(Utilities.asArray(model.selected));
            }
          };
          /**
           * On plugin initialization
           */
          component.onPluginInit = function () {
            // Get plugin
            selector.initialized = true;
            // Start
            component.onStart();
          };
          /**
           * On plugin start
           */
          component.onStart = function () {
            // Fill data
            let model = Control.getAddressModel(component.address);
            selector.selectData(Utilities.asArray(model.selected));
            filterSuggestModel(model);
          };
          /**
           * On plugin change
           * @param {object} item
           */
          component.onPluginChange = function (item) {
            processChangeEvent(component, item);
          };

          // Initialization (for suggest on columns)
          let defaultModel = Control.getAddressModel(component.address);
          if (checkSelectedValue(defaultModel)) {
            // Store options (on
            let initialModelChanged = component.scope.$on("modelChanged", function (event, launchers) {
              let changes = Utilities.modelChanged(component, launchers);
              if (changes) {
                if (selector.initializable) {
                  component.scope.initialized = true;
                }
                // Plugin initialization (retarded)
                initialModelChanged();
              }
            });
          } else {
            if (selector.multiple) {
              filterSuggestModel(defaultModel);
            }
            if (selector.initializable) {
              component.scope.initialized = true;
            }
          }

          /******************************************************************************
           * EVENT LISTENERS
           *****************************************************************************/
          component.listeners = component.listeners || {};
          // Model changed listeners
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, check: ["values", "selected"], service: component, method: "onModelChangedValuesSelected"});
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, check: ["values"], service: component, method: "onModelChangedValues"});
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, check: ["selected"], service: component, method: "onModelChangedSelected"});
          // Placeholder
          component.controller.placeholder = component.controller.placeholder || 'SELECT2_SEARCH_VALUE';
          options.placeholder = component.controller.placeholder;
          // Store options
          selector.scope.aweSelectOptions = options;
          // Finish initialization
          return true;
        },
        /**
         * Update select values
         * @param {type} component
         */
        updateSelectValues: function (component) {
          // Update select
          let model = Control.getAddressModel(component.address);
          this.fillData(model.values);
          this.selectData(model.selected);
        },
        /**
         * Fill a select2 element (in select2 format) with a model value list
         * @param {Array} values Model
         */
        fillData: function (values) {
          if (this.initialized) {
            let data = formatSelectData(values, null);
            this.component.fill(data);
          }
        },
        /**
         * Select data in select2 format
         * @param {Array} selected Model
         */
        selectData: function (selected) {
          if (this.initialized) {
            this.component.select(selected);
          }
        }
      };
      return Selector;
    }
  ]);
