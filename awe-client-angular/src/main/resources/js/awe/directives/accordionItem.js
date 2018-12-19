import { aweApplication } from "./../awe";

// Accordion item
aweApplication.directive('aweAccordionItem',
  ['ServerData', 'Component',
    function (serverData, Component) {
      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('accordionItem');
        },
        scope: {
          'itemId': '@accordionItemId'
        },
        link: function (scope, element) {
          // Init as component
          var component = new Component(scope, scope.itemId);
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }
          // Read component data from screen
          var parent = scope.$parent.$parent;

          // Get controller values and assign to scope
          scope.label = component.controller.label;
          scope.isSelected = parent.controller.itemsSelected.indexOf(scope.itemId) !== -1;
          scope.autocollapse = parent.controller.autocollapse;
          scope.accordionId = parent.accordionId;

          /*
           * Whenever Accordion's "itemsSelected" changes it means we have to update
           * this AccordionItem to see if it's still selected or not and apply
           * the adequate classes
           */
          scope.$watch("$parent.$parent.controller.itemsSelected", function(newValue){
              scope.isSelected = newValue.indexOf(scope.itemId) !== -1;

              /* Bootstrap applies "height: 0px" after clicking on an item to close it
               * This creates problems when opening due to a dependency, so we remove it
               * if this is the selected one
               */
              if(scope.isSelected && element.length !== 0 && element[0].getElementsByClassName("collapse").length !== 0){
                  element[0].getElementsByClassName("collapse")[0].style.height = "";
              }
          });

          // Click handler
          scope.onClick = function(itemId){
              /* Accordion has a $watch over it's "selected" property that handles
               * itemsSelected array correctly. Setting its value to this item's ID
               * fires it
              */
            scope.$parent.$parent.controller.selected = itemId;
          };
        }
      };
    }
  ]);
