import { aweApplication } from "./../awe";

// Formatted text service
aweApplication.factory('FormattedText', [ 'Text', 'AweUtilities', '$sce',
/**
 * Criterion generic methods
 * 
 * @param {Service} Criterion
 * @param {Service} Utilities Utilities service
 */
function(Text, Utilities) {
  /**
   * Text constructor
   * 
   * @param {Scope} scope Numeric scope
   * @param {String} id Numeric id
   * @param {String} element Numeric element
   */
  function FormattedText(scope, id, element) {
    this.scope = scope;
    this.id = id;
    this.element = element;
    this.component = new Text(this.scope, this.id, this.element);
    var formattedText = this;
    this.component.asText = function() {
      return formattedText.init();
    };

    return this.component;
  }
  FormattedText.prototype = {
    /**
     * Initialize text
     */
    init : function() {
      // Initialize criterion
      var component = this.component;
      if (!component.asCriterion()) {
        // If criterion is wrong, cancel initialization
        return false;
      }

      /*************************************************************************
       * PRIVATE METHODS
       ************************************************************************/

      /**
       * Retrieves visible value for the selector
       * 
       * @returns {string} visible value
       */
      function fixModel() {
        var selected = component.model.selected;
        var values = component.model.values;
        if (values.length === 0 || (String(values[0].value) !== String(selected))) {
          var value = {
            value : selected,
            label : selected || ""
          };
          values[0] = value;
        }
      }

      /*************************************************************************
       * SCOPE METHODS
       ************************************************************************/

      /**
       * Launch click event
       */
      component.scope.onClick = function() {
        Utilities.timeout(function() {
          component.storeEvent('click');
        });
      };

      /*************************************************************************
       * COMPONENT METHODS
       ************************************************************************/

      /**
       * Retrieves visible value for the selector
       * 
       * @returns {string} visible value
       */
      component.getVisibleValue = function() {
        var visibleValue = "";
        if (component.model.values.length > 0) {
          visibleValue = component.model.values[0].label;
        } else {
          visibleValue = component.model.selected;
        }
        return visibleValue;
      };

      /**
       * Update the model on model changed
       */
      component.onModelChanged = function() {
        // Fill data
        fixModel();

        // Fill data
        component.updateVisibleValue();
      };

      // Fix model on load
      component.onModelChanged();

      /*************************************************************************
       * EVENTS
       ************************************************************************/

      // Action listener definition
      Utilities.defineModelChangeListeners(component.listeners, {
        scope : component.scope,
        service : component,
        method : "onModelChanged"
      });

      // Initialization ok
      return true;
    }
  };
  return FormattedText;
} ]);
