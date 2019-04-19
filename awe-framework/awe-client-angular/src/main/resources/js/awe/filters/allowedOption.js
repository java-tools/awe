import { aweApplication } from "./../awe";

// Allowed option filter
aweApplication.filter('allowedOption',
  ['Control', 'AweUtilities',
    /**
     * Filter allowed options
     * @param {type} $control Control service
     * @param {type} $utilities Utilities service
     * @returns {Function}
     */
    function ($control, $utilities) {
      function filter(optionList) {
        var allowed = [];
        var moduleComponent = $control.getAddressModel({view: "base", component: "module"});
        var module = moduleComponent ? moduleComponent.selected : "";
        _.each(optionList, function (option) {
          var moduleValid = false;
          if (module === option.module || $utilities.isEmpty(option.module)) {
            moduleValid = true;
          }
          if (option.visible && !option.restricted && moduleValid) {
            allowed.push(option);
          }
        });
        return allowed;
      }
      filter.$stateful = true;
      return filter;
    }
  ]);