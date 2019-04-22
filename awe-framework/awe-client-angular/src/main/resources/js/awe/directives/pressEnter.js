import { aweApplication } from "./../awe";

// Press enter directive
aweApplication.directive('ngPressEnter',
  ['$parse', 'AweUtilities', function ($parse, Utilities) {
      return function (scope, element, attrs) {
        var fn = $parse(attrs.ngPressEnter);
        element.bind("keydown keypress", function (event) {
          if (event.which === 13) {
            element.change();
            element.blur();
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