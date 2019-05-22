import { aweApplication } from "./../../awe";
import { ClientActions } from "../../data/actions";
import "jquery-ui-sortable-npm";
import "PivotTable";

// Pivot table plugin
aweApplication.directive('uiPivotTable',
  ['AweSettings', 'AweUtilities',
    function ($settings, Utilities) {

      return {
        restrict: 'A',
        link: function (scope, elem) {

          scope.sorters = {};
          scope.rows = scope.controller.rows ? scope.controller.rows.split(',') : [];
          scope.cols = scope.controller.cols ? scope.controller.cols.split(',') : [];

          let initialized = false;
          let sortAs = $.pivotUtilities.sortAs;
          let tpl = $.pivotUtilities.aggregatorTemplates;
          let numOpt = $settings.get("numericOptions");
          let pivotOpt = $settings.get("pivotOptions");
          let aggregatorList = $.pivotUtilities.aggregators;

          // Watch for controller changes
          let initWatch = scope.$watch("datasource", initPlugin);

          // Custom number formats
          let customNumberFormat = $.pivotUtilities.numberFormat({
            digitsAfterDecimal: scope.controller.decimalNumbers || numOpt.mDec || 0,
            thousandsSep: scope.controller.thousandSeparator || numOpt.aSep,
            decimalSep: scope.controller.decimalSeparator || numOpt.aDec
          });

          // Add custom aggregators
          if (scope.controller.aggregator) {
            // Overwrite aggregators to apply number format
            aggregatorList["Custom Sum"] = {fn: tpl.sum(customNumberFormat), label: "Custom Sum"};
            aggregatorList["Custom Average"] = {fn: tpl.average(customNumberFormat), label: "Custom Average"};
            aggregatorList["Custom Minimum"] = {fn: tpl.min(customNumberFormat), label: "Custom Minimum"};
            aggregatorList["Custom Maximum"] = {fn: tpl.max(customNumberFormat), label: "Custom Maximum"};
            aggregatorList["Custom Sum over Sum"] = {
              fn: tpl.sumOverSum(customNumberFormat),
              label: "Custom Sum over Sum"
            };
            aggregatorList["Custom 80% Upper Bound"] = {
              fn: tpl.sumOverSumBound80(customNumberFormat),
              label: "Custom 80% Upper Bound"
            };
            aggregatorList["Custom 80% Lower Bound"] = {
              fn: tpl.sumOverSumBound80(customNumberFormat),
              label: "Custom 80% Lower Bound"
            };
          }

          // Init pivot configuration
          let config = {
            rendererOptions: {
              totalColumnPlacement: scope.controller.totalColumnPlacement || "right",
              totalRowPlacement: scope.controller.totalRowPlacement || "bottom"
            },
            unusedAttrsVertical: false,
            menuLimit: pivotOpt.numGroup,
            rendererName: scope.controller.renderer || null,
            aggregatorName: scope.controller.aggregator || null,
            vals: scope.controller.aggregationField ? [scope.controller.aggregationField] : [],
            sorters: function (attr) {
              if (scope.sorters && attr in scope.sorters) {
                return sortAs(scope.sorters[attr]);
              }
            },
            rows: scope.controller.rows ? scope.controller.rows.split(',') : [],
            cols: scope.controller.cols ? scope.controller.cols.split(',') : [],
            aggregators: aggregatorList,

            /**
             * On refresh component
             */
            onRefresh: function (newConfig) {
              if (scope.datasource.length > 0) {
                config.rows = newConfig.rows;
                config.cols = newConfig.cols;
              }
              config.vals = newConfig.vals;
              config.aggregatorName = newConfig.aggregatorName;
              config.aggregators = newConfig.aggregators;
              config.rendererName = newConfig.rendererName;
              scope.renderedData = newConfig.renderedData;
            }
          };

          /**
           * Plugin initialization
           */
          function initPlugin() {
            // Initialize element with options
            updatePlugin($settings.getLanguage(), true);

            // Unwatch initialization
            initWatch();
          }

          /**
           * Plugin initialization
           */
          function destroyPlugin() {
            // Destroy toc if created
            if (initialized) {
              initialized = false;
            }
          }

          /**
           * Plugin initialization
           * @param {String} language
           * @param {Boolean} update Full plugin update
           */
          function updatePlugin(language, update) {
            // Destroy toc if created
            destroyPlugin();

            // Initialize element with options
            elem.pivotUI(scope.datasource, config, update, language);

            // Set initialized as true
            initialized = true;
          }

          /**
           * Change current sorters
           * @param {object} parameters
           * @param {object} scope
           */
          scope.onSetSorters = function (parameters, scope) {
            if ("sorters" in parameters) {
              // Get points data from action
              let sorters = parameters.sorters;
              // Add points
              scope.sorters = sorters;
            }
          };

          /**
           * Set group colum names
           * @param {object} parameters
           * @param {object} scope
           */
          scope.onSetCols = function (parameters, scope) {
            if ("cols" in parameters) {
              // Get colum names
              let columns = parameters.cols ? parameters.cols.split(',') : [];
              // Add colums
              config.cols = columns;
            }
          };

          /**
           * Set group rows names
           * @param {object} parameters
           * @param {object} scope
           */
          scope.onSetRows = function (parameters, scope) {
            if ("rows" in parameters) {
              // Get rows names
              let rows = parameters.rows ? parameters.rows.split(',') : [];
              // Add rows
              config.rows = rows;
            }
          };

          /**
           * Update the model on model changed
           * @param {Object} changes
           */
          scope.onModelChanged = function (changes) {
            scope.datasource = changes.values;
            // Remove id column
            _.each(scope.datasource, function (data, key) {
              delete data.id;
            });
            updatePlugin($settings.getLanguage(), true);
          };

          // Define event listeners
          let listeners = {};

          // Action listener definition
          Utilities.defineActionListeners(listeners, ClientActions.pivot, scope, scope);

          // Action listener definition
          Utilities.defineModelChangeListeners(listeners, {
            scope: scope,
            check: ["values"],
            service: scope,
            method: "onModelChanged"
          });

          // Watch for language change
          listeners["languageChanged"] = scope.$on('languageChanged', function (event, language) {
            updatePlugin(language, true);
          });

          /**
           * Destroy plugin
           */
          function destroy() {
            // Destroy the plugin
            destroyPlugin();

            // Clear listeners
            Utilities.clearListeners(listeners);
          }

          // Observe destroy event
          elem.on("$destroy", destroy);
        }
      };
    }
  ]);
