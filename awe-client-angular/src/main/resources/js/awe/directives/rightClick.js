import { aweApplication } from "./../awe";

// Right click directive
aweApplication.directive('ngRightClick',
  ['$parse', 'AweUtilities', function ($parse, Utilities) {
      return function (scope, element, attrs) {
        var fn = $parse(attrs.ngRightClick);
        element.bind('contextmenu', function (event) {
          if (Utilities.isVisible(event.target)) {
            scope.$apply(function () {
              // Cancel event propagation
              Utilities.stopPropagation(event, true);
              fn(scope, {$event: event});
            });
          }
        });
      };
    }
  ]);