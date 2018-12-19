import { aweApplication } from "./../awe";

// Pivot table directive
aweApplication.directive('awePivotTable',
  ['ServerData', 'Component', 'Options',
    function (serverData, Component, Options) {

      return {
        restrict: 'E',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('pivotTable');
        },
        scope: {
          'pivotTableId': '@'
        },
        compile: function () {
          return {
            /**
             * Pregeneration function
             * @param {Object} scope
             */
            pre: function (scope) {
              // Set spin options
              scope.spinOptions = Options.spin.big;

              // Init as component
              var component = new Component(scope, scope.pivotTableId);
              if (!component.asComponent()) {
                // If component initialization is wrong, cancel initialization
                return false;
              }

              // Define datasource
              scope.datasource = component.model.values;

              // Sort by absolute value
              var absoluteSort = function (as, bs) {
                var a = parseFloat(as);
                var b = parseFloat(bs);
                if (isNaN(a) && isNaN(b)) {
                  return 0;
                }
                if (isNaN(a)) {
                  return -1;
                }
                if (isNaN(b)) {
                  return 1;
                }
                return Math.abs(a) - Math.abs(b);
              };

              // Define sort function
              switch (component.controller.sortMethod) {
                case "absolute":
                  window.pivotSortFunction = absoluteSort;
                  break;
                case "natural":
                default:
                  window.pivotSortFunction = $.pivotUtilities.naturalSort;
              }
            }
          };
        }
      };
    }
  ]);