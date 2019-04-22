import { aweApplication } from "./../../awe";

// Add requirements
aweApplication.requires.push("colorpicker.module");

// Column colorpicker directive
aweApplication.directive('aweColumnColor', [
  'ServerData', 'Criterion', 'Column',
  function (serverData, Criterion, Column) {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: function () {
        return serverData.getAngularTemplateUrl('column/color');
      },
      link: function (scope, elem, attrs) {
        // Create column, criterion and component
        var column = new Column(attrs);
        var component = new Criterion(scope, column.id, elem);

        // Initialize criterion and column
        if (column.init(component).asCriterion()) {

          // Update visible value on generation
          component.updateVisibleValue();
        }
      }
    };
  }
]);
