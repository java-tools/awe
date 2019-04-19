import { aweApplication } from "./../../awe";

// Datepicker directive
aweApplication.directive('aweInputDate',
  ['ServerData', 'DateTime',
    function (serverData, DateTime) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/date');
        },
        scope: {
          'criterionId': '@inputDateId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          scope.initialized = new DateTime(scope, scope.criterionId, elem).asDate();
        }
      };
    }
  ]);
