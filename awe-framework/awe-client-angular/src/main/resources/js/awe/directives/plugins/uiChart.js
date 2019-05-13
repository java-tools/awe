import { aweApplication } from "./../../awe";

// HIGHCHARTS
import Highcharts from "highcharts/highstock";
require("highcharts/highcharts-more.src")(Highcharts);
require("highcharts/highcharts-3d.src")(Highcharts);
require("highcharts/modules/drilldown.src")(Highcharts);
require("highcharts/modules/boost.src")(Highcharts);
require("highcharts/modules/no-data-to-display.src")(Highcharts);
require("highcharts/modules/exporting.src")(Highcharts);

import "HighchartsLocale";
import "HighchartsThemes";

// Highcharts plugin
aweApplication.directive('uiChart', ['AweSettings', 'AweUtilities',
  /**
   * Chart generic methods
   * @param {Service} $settings
   * @param {Service} Utilities
   */
  function ($settings, Utilities) {
    // Get default $settings

    // Set global options
    Highcharts.setOptions({
      global: {
        useUTC: false
      },
      lang: global.HighchartsLocale[$settings.get("language")]
    });
    return {
      restrict: 'A',
      require: ['ngModel'],
      link: function (scope, elem, attrs, modelController) {

        // Flag chart all ready initialized
        var chartDimensions = {
          width: 0,
          height: 0
        };
        // Add global options
        var highchartOptions = {};
        var chartOptions = {};
        /**
         * Plugin initialization
         * @param {object} newValue plugin parameters
         * @param {object} oldValue plugin parameters
         */
        var initPlugin = function (newValue, oldValue) {
          if (newValue && (!scope.component.initialized || newValue !== oldValue)) {
            chartOptions = newValue;
            // Initialize chart with options
            scope.component.initializeModel(newValue, modelController);
            // Check flag stockChart
            var isStockChart = newValue.stockChart;
            // Add specific options
            _.merge(highchartOptions, newValue);
            // Disable export button
            highchartOptions.exporting = {
              enabled: false
            };
            // Translate labels
            scope.component.translateLabels(highchartOptions);
            // Get theme
            var themeChart = chartOptions.theme;
            // Mix options with chart theme
            if (themeChart !== undefined && themeChart !== "" && Highcharts.theme[themeChart]) {
              // Mix general options
              _.merge(highchartOptions, Highcharts.theme[themeChart]);
              // Mix theme options for multiple axis
              _.each(highchartOptions.xAxis, function (xAxis) {
                _.merge(xAxis, Highcharts.theme[themeChart].xAxis);
              });
              _.each(highchartOptions.yAxis, function (yAxis) {
                _.merge(yAxis, Highcharts.theme[themeChart].yAxis);
              });
            }

            // Add events to highcharts options
            var events = {
              chart: {
                renderTo: elem[0],
                events: {
                  selection: scope.component.onZoom,
                  redraw: scope.component.onRedraw,
                  load: scope.component.onRedraw
                }
              },
              plotOptions: {
                series: {
                  cursor: 'pointer',
                  point: {
                    events: {
                      click: scope.component.onClick
                    }
                  }
                }
              }
            };
            // Add events
            _.merge(highchartOptions, events);
            // Build chart
            if (isStockChart) {
              // Create stock chart
              scope.component.chart = Highcharts.stockChart(highchartOptions);
            } else {
              // Create normal chart
              scope.component.chart = Highcharts.chart(highchartOptions);
            }

            if (Utilities.isVisible(elem[0])) {
              chartDimensions = {
                width: elem[0].clientWidth,
                height: elem[0].clientHeight
              };
            }
            scope.component.initialized = true;
          } else {
            if (scope.component.chart) {
              // Redraw chart
              scope.component.chart.redraw();
            }
          }
        };
        /**
         * Update global options
         */
        var updateGlobalOptions = function () {
          // Update element as date with options
          if (scope.component.initialized) {
             scope.component.chart.destroy();
          }
          scope.component.initialized = false;
          // Get lenguage
          var language = global.HighchartsLocale[$settings.get("language")];
          // Update global options
          Highcharts.setOptions({
            lang: language
          });
          initPlugin(chartOptions);
        };
        /**
         * Check if dimensions have changed
         * @returns {Boolean}
         */
        var changedDimensions = function () {
          var changed = false;
          var currentDimensions = {
            width: elem[0].clientWidth,
            height: elem[0].clientHeight
          };
          changed = currentDimensions.width !== chartDimensions.width || currentDimensions.height !== chartDimensions.height;
          chartDimensions = currentDimensions;
          return changed;
        };
        /**********************************************************************/
        /* WATCHES                                                            */
        /**********************************************************************/

        // Watch for controller changes
        scope.$watch(attrs.uiChart, initPlugin);
        /**
         * Redraw on resize
         */
        var onResize = function () {
          if (scope.component.initialized && Utilities.isVisible(elem[0])) {
            if (scope.component.chart && changedDimensions()) {
              scope.component.chart.reflow();
            }
          }
        };
        // Watch for screen initialization
        scope.$on("initialised", onResize);
        // Watch for element resize
        scope.$on("resize", onResize);
        // Watch for element resize
        scope.$on("resize-action", onResize);
        // Watch for language change
        scope.$on('languageChanged', updateGlobalOptions);
      }
    };
  }]);