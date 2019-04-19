import { aweApplication } from "./../awe";

// Confirm directive
aweApplication.directive('aweConfirm',
  ['ServerData', 'AweSettings',
    function (ServerData, $settings) {
      return {
        restrict: 'E',
        templateUrl: function () {
          return ServerData.getAngularTemplateUrl('confirm');
        },
        compile: function () {
          return {
            /**
             * Pregeneration function
             * @param {Object} scope
             */
            pre: function (scope) {
              // Define confirm button size
              scope.confirmSize = $settings.get("defaultComponentSize");
              var accepted = false;

              /**
               * Accept action in confirm modal
               */
              scope.acceptConfirm = function () {
                accepted = true;
              };

              /**
               * Cancel confirm modal
               */
              scope.closeConfirm = function () {
                // Cancel action
                if (scope.confirmAction) {
                  if (accepted) {
                    scope.confirmAction.accept();
                  } else {
                    scope.confirmAction.reject();
                  }
                }
                scope.confirmAction = null;
                accepted = false;
              };
            },
            /**
             * Postgeneration function
             * @param {Object} scope
             */
            post: function (scope) {
              // Watch for update model if checkbox is checked or not
              scope.$watch('confirmAction', function () {
                if (scope.confirmAction) {
                  // Open confirm modal
                  scope.$broadcast("modalChange", true);
                }
              });
            }
          };
        }
      };
    }
  ]);
