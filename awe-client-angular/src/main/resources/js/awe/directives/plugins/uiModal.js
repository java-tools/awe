import { aweApplication } from "./../../awe";

// Modal plugin
aweApplication.directive('uiModal',
  ['AweUtilities',
    function (Utilities) {
      return {
        restrict: 'A',
        priority: 1,
        scope: {
          onOpen: '&',
          onClose: '&'
        },
        link: function (scope, elem, attrs) {
          var opened = false;

          scope.$on("modalChange", function (event, show) {
            if (show && show !== opened) {
              $(elem).modal('show');
              opened = true;
            } else {
              $(elem).modal('hide');
            }
          });

          // Call on finish showing
          $(elem).on('shown.bs.modal', function () {
            if (attrs.onOpen) {
              Utilities.timeout(function () {
                scope.onOpen();
              });
            }
          });

          // Call on finish hiding
          $(elem).on('hidden.bs.modal', function () {
            if (attrs.onClose) {
              Utilities.timeout(function () {
                scope.onClose();
              });
            }
            opened = false;
          });
        }
      };
    }
  ]);
