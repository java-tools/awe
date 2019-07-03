import { aweApplication } from "./../../awe";
import "../../directives/plugins/uiChart";
import "./events";
import "./sparkLine";

// Chart service
aweApplication.factory('Chart',
  ['Component', '$translate', 'ChartEvents', 'Storage', 'Control', 'AweUtilities', 'AweSettings',
    /**
     * Chart generic methods
     * @param {Object} Component
     * @param {Object} $translate
     * @param {Object} ChartEvents
     * @param {Object} Storage
     * @param {Object} Control
     * @param {Object} Utilities
     * @param {Object} $settings Awe $settings
     */
    function (Component, $translate, ChartEvents, Storage, Control, Utilities, $settings) {
      // Get awe $settings

      // Axis formatter API
      var formatterList = {
        /**
         * Format currency magnitude
         * @returns {String} formatted value
         */
        formatCurrencyMagnitude: function () {
          var value = this.value;
          var symbol = null;
          var magnitudes = [{
              exp: 6,
              symbol: "M"
            }, {
              exp: 3,
              symbol: "K"
            }, {
              exp: 0,
              symbol: ""
            }];
          // Search for magnitudes and pick the biggest one
          _.each(magnitudes, function (magnitude) {
            var factor = Math.pow(10, magnitude.exp);
            if (Math.abs(value) >= factor && symbol === null) {
              symbol = magnitude.symbol;
              value = Math.round(value * 100 / factor) / 100;
            }
          });
          return value + symbol;
        }
      };
      /**
       * Process chart series model
       * @param serie chart serie
       * @param model chart model
       */
      var getSerieData = function (serie, model) {
        var serieData = [];
        // Retrieve fieldlist
        var fieldList = [serie.xValue, serie.yValue];
        if (serie.zValue) {
          fieldList.push(serie.zValue);
        }
        _.each(model, function (row) {
          if ((fieldList[0] in row) && (fieldList[1] in row)) {
            if ("drilldown" in serie) {
              var objectPointSerie = {};
              objectPointSerie.name = row[fieldList[0]];
              objectPointSerie.y = row[fieldList[1]];
              objectPointSerie.drilldown = serie.drilldown;
              serieData.push(objectPointSerie);
            } else {
              var pointSerie = [];
              _.each(fieldList, function (field) {
                if (field in row) {
                  pointSerie.push(row[field]);
                }
              });
              serieData.push(pointSerie);
            }

          }
        });
        return serieData;
      };

      /**
       * Chart constructor
       * @param {Scope} scope Chart scope
       * @param {String} id Chart id
       * @param {String} element Chart element
       */
      function Chart(scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Component(this.scope, this.id);
        this.component.chart = this;
        this.sorting = [];
        this.addedRows = 0;

        // Define attribute methods
        this.attributeMethods = {
          /**
           * Retrieve x value
           * @param {object} component Component scope
           * @returns {mixed} Current value
           */
          "x": function (component) {
            return component.model.selected && "x" in component.model.selected ? component.model.selected.x : null;
          },
          /**
           * Retrieve y value
           * @param {object} component Component scope
           * @returns {mixed} Current value
           */
          "y": function (component) {
            return component.model.selected && "y" in component.model.selected ? component.model.selected.y : null;
          },
          /**
           * Retrieve min x value on zoom
           * @param {object} component Component scope
           * @returns {mixed} Current text value
           */
          "xMin": function (component) {
            return component.model.zoom && "x" in component.model.zoom ? component.model.zoom.x.min : null;
          },
          /**
           * Retrieve max x value on zoom
           * @param {object} component Component scope
           * @returns {mixed} Current text value
           */
          "xMax": function (component) {
            return component.model.zoom && "x" in component.model.zoom ? component.model.zoom.x.max : null;
          },
          /**
           * Retrieve min y value on zoom
           * @param {object} component Component scope
           * @returns {mixed} Current text value
           */
          "yMin": function (component) {
            return component.model.zoom && "y" in component.model.zoom ? component.model.zoom.y.min : null;
          },
          /**
           * Retrieve max y value on zoom
           * @param {object} component Component scope
           * @returns {mixed} Current text value
           */
          "yMax": function (component) {
            return component.model.zoom && "y" in component.model.zoom ? component.model.zoom.y.max : null;
          }
        };

        // Add attribute methods
        _.merge(this.component.attributeMethods, this.attributeMethods);
        this.component.asChart = function () {
          return this.chart.init();
        };
        return this.component;
      }

      Chart.prototype = {
        init: function () {
          // Init as component
          var component = this.component;
          if (!component.asComponent()) {
            // If component initialization is wrong, cancel initialization
            return false;
          }
          var chartOptions = component.controller.chartModel;

          /**
           * Process chart series model
           * @param controller chart controller
           * @param modelController chart model
           */
          component.initializeModel = function (controller) {
            // Get chart series controller
            var seriesModelList = _.get(controller, "series", []);
            // Get chart drilldown series controller
            var drillSeriesModelList = _.get(controller, "drilldown.series", []);
            // Chart model data
            var model = component.model.values;
            // Build array data for each serie of chart
            if (seriesModelList) {
              _.each(seriesModelList, function (serie) {
                serie.data = getSerieData(serie, model);
              });
            }
            // Build array data for each drilldown serie of chart
            if (drillSeriesModelList && drillSeriesModelList.length > 0) {
              _.each(drillSeriesModelList, function (serie) {
                serie.data = getSerieData(serie, model);
              });
              // Disabled allow point selection in Pies
              if (controller.plotOptions.pie) {
                controller.plotOptions.pie.allowPointSelect = false;
              }
            }
            // Define model selection
            component.model.selected = {};
            component.model.zoom = {};
          };
          /**
           * Process chart series model
           * @param model chart model
           */
          component.updateModel = function (model) {
            // Store model
            component.model.values = model;
            // Get number size limit serie
            var limitPointSerie = $settings.get("chartOptions").limitPointsSerie;

            if (component.initialized) {
              // Get chart object
              var chart = component.chart;
              // Flag check limit serie size
              var sizeSerieLimit = false;
              // Get chart series controller
              var seriesModelList = _.get(chartOptions, "series", []);
              // Get chart series controller
              var drillDownSeriesModelList = _.get(chartOptions, "drilldown.series", []);
              // Build array datas for each serie of chart
              if (seriesModelList) {
                _.each(seriesModelList, function (serie) {
                  var chartSerie = chart.get(serie.id);
                  var serieData = getSerieData(serie, model);
                  sizeSerieLimit = !sizeSerieLimit && serieData.length > limitPointSerie;
                  chartSerie.setData(serieData, false, true);
                });
              }

              // Build array datas for each drilldown serie of chart
              if (drillDownSeriesModelList !== null) {
                _.each(drillDownSeriesModelList, function (serie) {
                  var chartSerie = chart.get(serie.id);
                  var serieData = getSerieData(serie, model);
                  sizeSerieLimit = !sizeSerieLimit && serieData.length > limitPointSerie;
                  chartSerie.setData(serieData, false, true);
                });
                // Disabled allow point selection in Pies
                if (chartOptions.plotOptions.pie) {
                  chartOptions.plotOptions.pie.allowPointSelect = false;
                }
              }

              // Check limit serie size flag
              if (sizeSerieLimit) {
                // Reset chart and show message
                component.resetChart();
                if (!chart.hasData()) {
                  chart.hideNoData();
                  chart.showNoData(chart.options.lang.tooMuchData);
                }
              } else {
                // Redraw chart
                chart.redraw();
              }
            }
          };
          /**
           * Translate literals of chart
           * @param highchartOptions chart options
           * @returns highchartOptions with labels translated
           */
          component.translateLabels = function (highchartOptions) {
            // Chart title
            if ("title" in highchartOptions) {
              highchartOptions.title.text = $translate.instant(highchartOptions.title.text);
            }

            // Chart subtitle
            if ("subtitle" in highchartOptions) {
              highchartOptions.subtitle.text = $translate.instant(highchartOptions.subtitle.text);
            }

            // Chart legend
            if ("legend" in highchartOptions && "title" in highchartOptions.legend) {
              highchartOptions.legend.title.text = $translate.instant(highchartOptions.legend.title.text);
            }

            // Chart xAxis
            if ("xAxis" in highchartOptions) {
              _.each(highchartOptions.xAxis, function (xAxis) {
                if ("title" in xAxis) {
                  xAxis.title.text = $translate.instant(xAxis.title.text);
                }
                if ("labels" in xAxis && "formatter" in xAxis.labels) {
                  var formatterFunction = xAxis.labels.formatter;
                  xAxis.labels.formatter = formatterList[formatterFunction];
                }
              });
            }

            // Chart yAxis
            if ("yAxis" in highchartOptions) {
              _.each(highchartOptions.yAxis, function (yAxis) {
                if ("title" in yAxis) {
                  yAxis.title.text = $translate.instant(yAxis.title.text);
                }
                if ("labels" in yAxis && "formatter" in yAxis.labels) {
                  var formatterFunction = yAxis.labels.formatter;
                  yAxis.labels.formatter = formatterList[formatterFunction];
                }
              });
            }

            // Chart series
            if ("series" in highchartOptions) {
              _.each(highchartOptions.series, function (serie) {
                if ("name" in serie) {
                  serie.name = $translate.instant(serie.name);
                }
              });
            }

            // Drilldown Chart series
            if ("drilldown" in highchartOptions && "series" in highchartOptions.drilldown) {
              _.each(highchartOptions.drilldown.series, function (drilldownSerie) {
                if ("name" in drilldownSerie) {
                  drilldownSerie.name = $translate.instant(drilldownSerie.name);
                }
              });
            }
          };
          /**
           * On add points event
           * Add points to chart series
           * @param {object} points chart points
           */
          component.addPoints = function (points) {
            // Add points to chart series
            if (points) {
              // Get attribute id for each serie
              component.model.values.push(points[0]);
              _.each(chartOptions.series, function (serie) {
                if ("id" in serie) {
                  var fieldList = [serie.xValue, serie.yValue];
                  if ("zValue" in serie) {
                    fieldList.push(serie.zValue);
                  }
                  // Get point for serie
                  var point = [];
                  _.each(fieldList, function (field) {
                    point.push(points[0][field]);
                  });
                  // Add point
                  var swift = component.model.values.length >= component.getMax();
                  component.chart.get(serie.id).addPoint(point, false, swift);
                }
              });
              component.chart.redraw();
            }
          };

          /**
           * On add series event
           * Add series to chart
           * @param {object} series to add chart
           */
          component.addSeries = function (series) {
            // Get chart
            var chart = component.chart;

            _.each(series, function (serie) {
              // Add serie with
              chart.addSeries(serie, false, true);
            });
            // Redraw chart
            chart.redraw();
          };

          /**
           * On replace series event
           * Replace series of chart
           * @param {object} series to replace chart
           */
          component.replaceSeries = function (series) {
            // Get chart
            var chart = component.chart;

            // Remove old series
            var seriesLength = chart.series.length;
            for (var i = seriesLength - 1; i > -1; i--) {
              chart.series[i].remove(false);
            }
            // Add new series
            _.each(series, function (serie) {
              chart.addSeries(serie, false, true);
            });
            // Redraw chart
            chart.redraw();
          };

          /**
           * On remove series event
           * Remove series of chart
           * @param {object} series to remove chart
           */
          component.removeSeries = function (series) {
            // Get chart
            var chart = component.chart;

            // Remove series by id
            _.each(series, function (serie) {
              if (chart.get(serie.id) !== null) {
                chart.get(serie.id).remove(false);
              }
            });

            // Redraw chart
            chart.redraw();
          };

          /**
           * On reset action event
           * Remove series of chart
           */
          component.resetChart = function () {
            // Get chart Object
            var chart = component.chart;

            // Get chart series controller
            var seriesModelList = _.get(chartOptions, "series", []);

            // Build array datas for each serie of chart
            if (seriesModelList) {
              _.each(seriesModelList, function (serie) {
                // Get serie identifier with id or name
                var serieID = "id" in serie ? serie.id : serie.name;
                var chartSerie = chart.get(serieID);
                chartSerie.setData([], false, true);
              });
            }

            // Redraw chart
            chart.redraw();
          };
          /**
           * On restore action event
           * Restore chart with initial model
           */
          component.restoreChart = function () {
            Control.restoreInitialModelAttribute(component.address, "values");
            //component.updateModel(model.values);
          };
          /**
           * Basic getAttribute function (To be overwritten on complex directives)
           * @param {type} attribute Attribute to check
           * @returns {undefined}
           */
          component.getAttribute = function (attribute) {
            return component.attributeMethods[attribute](component);
          };
          var processParameters = function (parameters) {
            var options;
            switch (typeof parameters) {
              case "string":
                options = Utilities.evalJSON(parameters);
                break;
              case "object":
                options = parameters;
                break;
              default:
                options = {};
            }
            return options;
          };
          /**
           * Check what parameters have changed and apply the changes to the chart
           * @param {object} parameters
           */
          component.checkParametersChanged = function (parameters) {
            var options = processParameters(parameters.chartOptions);
            if ("zoom" in options) {
              component.changeZoom(options.zoom);
            }
          };
          /**
           * Change the chart zoom
           * @param {object} zoom
           */
          component.changeZoom = function (zoom) {
            var axisList = ["x", "y"];
            _.each(axisList, function (axis) {
              var selectedMin = axis in component.model.zoom ? component.model.zoom[axis]["min"] : null;
              var selectedMax = axis in component.model.zoom ? component.model.zoom[axis]["max"] : null;
              if (axis in zoom) {
                if (selectedMin !== zoom[axis].min || selectedMax !== zoom[axis].max) {
                  // Set zoom extremes
                  if (component.chart[axis + "Axis"][0].setExtremes) {
                    component.chart[axis + "Axis"][0].setExtremes(zoom[axis].min || null, zoom[axis].max || null);
                  }
                  // Update zoom model
                  component.model.zoom[axis] = {min: zoom[axis].min || null, max: zoom[axis].max || null};
                  // Reset zoom if both values are null
                  if (zoom[axis].min === null && zoom[axis].max === null) {
                    component.chart.zoomOut();
                  }
                }
              }
            });
          };
          /**********************************************************************/
          /* EVENT HANDLERS                                                     */
          /**********************************************************************/

          /**
           * Store point clicked on chart model
           * @param {object} data
           */
          component.storeClickData = function (data) {
            component.model.selected = {x: data.x, y: data.y};
          };

          /**
           * On each redraw add dblclick and contextmenu events
           */
          component.onRedraw = function () {
            var chart = this;
            var eventTimer;
            for (var j in chart.series) {
              var series = chart.series[j];
              for (var i in series.data) {
                (function (index) {
                  var point = series.data[index];
                  if (point.graphic) {
                    point.graphic.on('dblclick', function (event) {
                      event.preventDefault();
                      Utilities.timeout.cancel(eventTimer);
                      eventTimer = Utilities.timeout(function () {
                        component.onDblClick(event, point);
                      });

                      return false;
                    });
                    point.graphic.on('mousedown', function (event) {
                      if (event.which === 3) {
                        // Cancel event propagation
                        Utilities.stopPropagation(event, true);
                        return false;
                      }
                    });
                    var onContextMenuPoint = function (event) {
                      if (event.which === 3) {
                        // Right click
                        // Cancel event propagation
                        Utilities.stopPropagation(event, true);
                        Utilities.timeout.cancel(eventTimer);
                        eventTimer = Utilities.timeout(function () {
                          component.onContextMenu(event, point);
                        });
                        return false;
                      }
                    };
                    point.graphic.on('contextmenu', onContextMenuPoint);
                    point.graphic.on('click', onContextMenuPoint);
                    point.graphic.on('mouseup', onContextMenuPoint);
                  }
                })(i);
              }
            }
          };

          /**
           * Click event
           */
          component.onClick = function () {
            // Store click data
            component.storeClickData(this);
            // Store click event
            component.storeEvent("click");
          };

          /**
           * Double Click event
           * @param {object} event
           * @param {object} point
           */
          component.onDblClick = function (event, point) {
            // Store click data
            component.storeClickData(point);
            // Store click event
            component.storeEvent("double-click");
          };

          /**
           * Context menu event
           * @param {object} event
           * @param {object} point
           */
          component.onContextMenu = function (event, point) {
            // Store click data
            component.storeClickData(point);
            // Store click event
            component.storeEvent("context-menu");
            // Show context menu
            component.showContextMenu(event);
          };
          /**
           * Selection (zoom) event
           * @param {object} event
           */
          component.onZoom = function (event) {
            var zoom = {};
            // Set yAxis zoom
            if (event.yAxis && event.yAxis.length > 0) {
              zoom = {min: event.yAxis[0].min, max: event.yAxis[0].max};
            } else {
              zoom = {min: null, max: null};
            }
            component.model.zoom.y = zoom;
            // Set xAxis zoom
            if (event.xAxis && event.xAxis.length > 0) {
              zoom = {min: event.xAxis[0].min, max: event.xAxis[0].max};
            } else {
              zoom = {min: null, max: null};
            }
            component.model.zoom.x = zoom;
            // Store zoom event
            component.storeEvent("zoom");
          };

          /**
           * Update the model on model changed
           * @param {Object} changes
           */
          component.onModelChanged = function (changes) {
            component.updateModel(changes.values);
          };

          /**********************************************************************/
          /* API                                                                */
          /**********************************************************************/

          /**
           * Get chart return data function;
           * @returns Object data of chart
           */
          var api = Control.getAddressApi(component.address);
          /**
           * Retrieve the data of the component that should be sent to the server
           * @return {Object} Data to send to the server
           */
          api.getData = function () {
            var data = {};
            var model = Control.getAddressModel(component.address);
            if (model.selected) {
              if ("x" in model.selected) {
                data[component.address.component + ".x"] = model.selected.x;
              }
              if ("y" in model.selected) {
                data[component.address.component + ".y"] = model.selected.y;
              }
            }
            return data;
          };
          /**
           * Retrieve the data of the component that should be sent to the server for printing actions
           * @param {String} Orientation
           * @return {Object} Data to send to the server
           */
          api.getPrintData = function (orientation) {
            var data = {};
            var chartSize;
            switch (orientation) {
              case "LANDSCAPE":
                chartSize = {width:1167, height:360};
                break;
              default:
              case "PORTRAIT":
                chartSize = {width:796, height:540};
                break;
            }
            data[component.address.component] = {
              // Set chart image
              image: component.chart.getSVG({chart:chartSize})
            };
            return data;
          };
          /**********************************************************************/
          /* EVENTS                                                             */
          /**********************************************************************/

          // Map grid actions
          ChartEvents.mapActions(component);
          /**
           * Event listeners
           */
          component.listeners = component.listeners || {};

          // Action listener definition
          Utilities.defineModelChangeListeners(component.listeners, {scope: component.scope, check: ["values"], service: component, method: "onModelChanged"});

          // On model change launch dependency
          component.listeners["controllerChange"] = component.scope.$on("controllerChange", function (event, parameters) {
            if (_.isEqual(parameters.address, component.address)) {
              component.checkParametersChanged(parameters.controller);
            }
          });

          // Return initialization well done
          return true;
        }
      };
      return Chart;
    }
  ]);