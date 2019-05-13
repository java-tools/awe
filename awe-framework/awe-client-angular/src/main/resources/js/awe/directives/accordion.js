import { aweApplication } from "./../awe";
import "./accordionItem";

// Accordion directive
aweApplication.directive('aweAccordion', [
	'ServerData',
	'Component',
	'AweUtilities',
	function(serverData, Component, utils) {
	  return {
		restrict : 'E',
		replace : true,
		transclude : true,
		templateUrl : function() {
		  return serverData.getAngularTemplateUrl('accordion');
		},
		scope : {
		  'accordionId' : '@'
		},
		link : function(scope) {
		  // Init as component
		  var component = new Component(scope, scope.accordionId);
		  if (!component.asComponent()) {
			// If component initialization is wrong, cancel initialization
			return false;
		  }
		  // Read component data from screen
		  scope.$watch("controller.selected", function(newValue) {
			if (utils.isNull(newValue)) {
			  return;
			}
			if (component.controller.itemsSelected.indexOf(newValue) !== -1) {
			  // Duplicate array removing the item clicked
			  component.controller.itemsSelected = component.controller.itemsSelected.filter(function(item) {
				return item !== newValue;
			  });
			} else {
			  // Add to array. If autocollapse then only one is allowed;
			  // otherwise, several can be
			  if (component.controller.autocollapse) {
				component.controller.itemsSelected = [ newValue ];
			  } else {
				component.controller.itemsSelected = component.controller.itemsSelected.concat(newValue);
			  }
			}
			component.controller.selected = null;
		  });
		}
	  };
	} ]);
