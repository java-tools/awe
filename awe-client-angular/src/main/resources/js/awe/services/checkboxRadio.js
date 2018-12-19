import { aweApplication } from "./../awe";

// Checkbox Radio service
aweApplication.factory('CheckboxRadio',
  ['Criterion', 'Control', 'AweUtilities', 'Options',
    /**
     * Criterion generic methods
     * @param {Service} Criterion
     * @param {Service} Control
     * @param {Service} Utilities
     * @param {Service} Options
     */
    function (Criterion, Control, Utilities, Options) {

      /**
       * Button constructor
       * @param {Scope} scope Button scope
       * @param {String} id Button id
       * @param {String} element Button element
       */
      function CheckboxRadio(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Criterion(this.scope, this.id, this.element);

        // Link to initialization methods
        var checkboxradio = this;
        this.component.asCheckbox = function () {
          return checkboxradio.asCheckbox();
        };
        this.component.asRadio = function () {
          return checkboxradio.asRadio();
        };
        return this.component;
      }

      CheckboxRadio.prototype = {
        /**
         * Initialize as checkbox
         */
        asCheckbox: function () {
          // Initialize criterion
          var component = this.component;
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          // Define spin options
          component.scope.spinOptions = component.scope.spinOptions || Options.spin.small;

          // Add a default value if values has no data
          var checkedValue = component.model.defaultValues || 1;

          // Flag to checked or not
          component.scope.checked = String(component.model.selected) === String(checkedValue);
          component.model.selected = component.scope.checked ? checkedValue : 0;

          // Add group to address
          component.address.group = component.controller.group;

          /******************************************************************************
           * SCOPE METHODS
           *****************************************************************************/

          /**
           * Update checkbox selected value
           * @param {boolean} checked Checkbox is checked
           */
          component.scope.updateSelected = function (checked) {
            component.model.selected = checked ? checkedValue : 0;
            component.model.values = [{value:component.model.selected, label:component.model.selected}];
            component.modelChange();
          };

          /**********************************************************************/
          /* API METHODS                                                        */
          /**********************************************************************/

          /**
           * API link to update the model values
           * @param {type} data
           */
          component.api.updateModelValues = function (data) {
            var model = component.model;
            if (model && "selected" in data) {
              // Store selected values
              model.selected = Control.formatDataList(data.selected);
              component.scope.checked = String(model.selected) === String(checkedValue);
            }
          };

          // Return initialization
          return true;
        },
        /**
         * Initialize as radio
         */
        asRadio: function () {
          // Initialize criterion
          var component = this.component;

          // Define spin options
          component.scope.spinOptions = component.scope.spinOptions || Options.spin.small;

          // Initialize component as criterion
          if (!component.asCriterion()) {
            // If criterion is wrong, cancel initialization
            return false;
          }

          // Generate group address
          var group = component.controller.group;
          var groupAddress = _.cloneDeep(component.address);
          groupAddress.component = group;

          // Set model value
          component.value = component.model.values[0].value;

          // Change model with group attribute
          var viewModels = Control.getAddressViewModel(component.address);
          if (!(group in viewModels)) {
            viewModels[group] = {
              selected: null,
              defaultValues: component.model.selected
            };
          }

          // Change group selected model
          var selectedList = Utilities.asArray(component.model.selected);
          if (selectedList.length > 0) {
            viewModels[group].selected = selectedList[0];
            viewModels[group].defaultValues = selectedList[0];
          }

          // Change model to group model
          component.model = viewModels[group];

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * On model change
           */
          component.modelChange = function () {
            component.model.selected = component.model.selected !== '' ? component.model.selected : null;
            Control.publishModelChanged(groupAddress, {selected: component.model.selected});
          };

          /**
           * Restore criterion
           */
          component.onRestore = function () {
            Control.restoreModelAttribute(groupAddress, "selected");
          };

          /**********************************************************************/
          /* API METHODS                                                        */
          /**********************************************************************/
          // Update API for single radio
          var retrieveEmptyObject = function () {
            return {};
          };
          component.api.getData = retrieveEmptyObject;
          component.api.getPrintData = retrieveEmptyObject;

          /**********************************************************************/
          /* GROUP API METHODS                                                  */
          /**********************************************************************/

          var viewApis = Control.getAddressViewApi(component.address);
          viewApis[group] = {};

          // Add API for group
          /**
           * Basic getData function
           * @returns {Object} Data from criteria
           */
          viewApis[group].getData = function () {
            // Initialize data
            var data = {};
            data[group] = component.model.selected;
            return data;
          };

          /**
           * Printable data function (To be overwritten on complex directives)
           * @returns {Object} Data from criteria
           */
          viewApis[group].getPrintData = function () {
            // Initialize data
            return viewApis[group].getData();
          };

          /**
           * Basic getAttribute function (To be overwritten on complex directives)
           * @param {type} attribute Attribute to check
           * @returns {undefined}
           */
          viewApis[group].getAttribute = function (attribute) {
            return component.getAttribute(attribute);
          };

          /**
           * API link to update the selected values
           * @param {type} data
           */
          viewApis[group].updateModelValues = function (data) {
            if (component.model && "selected" in data) {
              // Store selected values
              component.model.selected = Control.formatDataList(data.selected);
            }
          };

          // Return initialization
          return true;
        }
      };
      return CheckboxRadio;
    }
  ]);