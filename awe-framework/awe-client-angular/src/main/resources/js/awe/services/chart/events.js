import { aweApplication } from "./../../awe";
import { ClientActions } from "../../data/actions";

// Chart events service
aweApplication.factory('ChartEvents', ['AweUtilities', 'ActionController',
  function ($utilities, $actionController) {

    /*********
     * EVENTS
     *********/
    var ChartEvents = {
      mapActions: function (component) {
        /**
         * Event listeners
         */
        $actionController.defineActionListeners(component.listeners, ClientActions.chart, component.scope, ChartEvents);

        // Capture reset scope action
        component.listeners['resetScope'] = component.scope.$on('reset-scope', function (event, view) {
          if (view === component.address.view) {
            component.resetChart();
          }
        });

        // Capture restore scope
        component.listeners['restoreScope'] = component.scope.$on('restore-scope', function (event, view) {
          if (view === component.address.view) {
            component.restoreChart();
          }
        });

        // Add Event click for context menu
        component.chart.element.bind('mousedown', function (event) {
          switch (event.which) {
            case 1:
              // left click
              component.hideContextMenu();
              break;
            case 3:
              // Right click
              // Cancel event propagation
              $utilities.stopPropagation(event, true);
              component.showContextMenu(event);
              break;
            default:
          }
          return false;
        });
      },
      /**
       * On add points to series of chart
       * @param {parameters} parameters
       * @param {scope} scope
       */
      onAddPoints: function (parameters, scope) {
        if ("data" in parameters && "rows" in parameters.data) {
          // Get points data from action
          var points = parameters.data.rows;
          // Add points
          scope.component.addPoints(points);
        }
      },
      /**
       * On add series to chart
       * @param {parameters} parameters
       * @param {scope} scope
       */
      onAddSeries: function (parameters, scope) {
        if ("series" in parameters) {
          // Add series
          scope.component.addSeries(parameters.series);
        }
      },
      /**
       * On remove series of chart
       * @param {parameters} parameters
       * @param {scope} scope
       */
      onRemoveSeries: function (parameters, scope) {
        if ("series" in parameters) {
          // Remove series
          scope.component.removeSeries(parameters.series);
        }
      },
      /**
       * On replace series to chart
       * @param {parameters} parameters
       * @param {scope} scope
       */
      onReplaceSeries: function (parameters, scope) {
        if ("series" in parameters) {
          // Add series
          scope.component.replaceSeries(parameters.series);
        }
      },
      /**
       * On reset
       * Restore chart
       * @param {parameters} parameters
       * @param {object} scope
       */
      onReset: function (parameters, scope) {
        // Reset chart
        scope.component.resetChart();
      },
      /**
       * On reset
       * Restore chart
       * @param {parameters} parameters
       * @param {object} scope
       */
      onRestore: function (parameters, scope) {
        // Reset chart
        scope.component.restoreChart();
      }
    };

    return ChartEvents;
  }]);