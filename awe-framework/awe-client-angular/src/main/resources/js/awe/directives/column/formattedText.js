import { aweApplication } from "./../../awe";

// Column formatted text directive
aweApplication.directive('aweColumnFormattedText',
['ServerData', 'Column', 'FormattedText',
 function (serverData, Column, FormattedText) {
   return {
     restrict: 'E',
     replace: true,
     templateUrl: function () {
       return serverData.getAngularTemplateUrl('column/formattedText');
     },
     link: function (scope, elem, attrs) {
       // Create column, criterion and component
       var column = new Column(attrs);
       var component = new FormattedText(scope, column.id, elem);

       // Initialize criterion and column
       if (column.init(component).asText()) {
         // Update visible value on generation
         component.updateVisibleValue();
       }
     }
   };
 }
]);
