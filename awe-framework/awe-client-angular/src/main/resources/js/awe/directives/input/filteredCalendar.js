import { aweApplication } from "./../../awe";

// Filtered calendar directive
aweApplication.directive('aweInputFilteredCalendar',
  ['ServerData', 'DateTime',
    function (serverData, DateTime) {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('input/date');
        },
        scope: {
          'criterionId': '@inputFilteredCalendarId'
        },
        link: function (scope, elem, attrs) {
          // Initialize criterion
          scope.initialized = new DateTime(scope, scope.criterionId, elem).asFilteredDate();
        }
      };
    }
  ]);
