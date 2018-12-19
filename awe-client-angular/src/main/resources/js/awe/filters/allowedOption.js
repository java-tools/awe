import { aweApplication } from "./../awe";

// Allowed option filter
aweApplication.filter('allowedOption',
  ['Control', 'AweUtilities',
    /**
     * Filter allowed options
     * @param {type} Control Control service
     * @param {type} Utilities Utilities service
     * @returns {Function}
     */
    function (Control, Utilities) {
      function filter(optionList) {
        var allowed = [];
        var moduleComponent = Control.getAddressModel({view: "base", component: "module"});
        var module = moduleComponent ? moduleComponent.selected : "";
        _.each(optionList, function (option) {
          var moduleValid = false;
          if (module === option.module || Utilities.isEmpty(option.module)) {
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