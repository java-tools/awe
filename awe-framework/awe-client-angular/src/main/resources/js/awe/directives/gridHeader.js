import { aweApplication } from "./../awe";

// Grid header directive
aweApplication.directive('aweGridHeader',
  ['ServerData', 'AweUtilities',
    function (serverData, Utilities) {

      return {
        restrict: 'A',
        replace: true,
        transclude: true,
        templateUrl: function () {
          return serverData.getAngularTemplateUrl('grid/gridHeader');
        },
        scope: {
          'aweGridHeader': '='
        },
        link: function (scope, element) {
          // setup listener for scroll events to sync categories with table
          var viewPort;
          var headerContainer;

          /**
           * Reload headers based on column movements
           * @param {Object} event
           * @param {Object} data
           */
          function refreshHeaders(event, data) {
            var columns = data.columns;
            var headers = data.headers;
            scope.categories = [];
            var lastHeader = null;
            var totalWidth = 0;
            _.each(columns, function (column) {
              if (column.colDef.hidden) {
                return;
              }
              totalWidth += column.width;
              var header = null;
              if (column.name in headers) {
                header = headers[column.name];
              }
              if (header !== lastHeader) {
                scope.categories.push({
                  displayName: lastHeader === null ? "\u00A0" : lastHeader.label,
                  width: totalWidth - column.drawnWidth
                });
                totalWidth = column.width;
                lastHeader = header;
              }
            });
            if (totalWidth > 0) {
              scope.categories.push({
                displayName: lastHeader === null ? "\u00A0" : lastHeader.label,
                width: totalWidth
              });
            }

            // Update scroll
            if (data.columns.length !== 0 && data.columns[0].grid.id) {
              var grid = $(".grid" + data.columns[0].grid.id);
              var viewport = $(".ui-grid-viewport", grid);
              updateScroll({currentTarget: viewport});
            }
          }

          /**
           * Adapt scroll to viewport
           */
          function updateScroll(data) {
            // copy total width to compensate scrollbar width
            $(element).find(".ui-grid-header-canvas").width($(headerContainer).find(".ui-grid-header-canvas").width());
            $(element).find(".ui-grid-header-viewport").scrollLeft($(data.currentTarget).scrollLeft());
          }

          /**
           * Adapt scroll to viewport
           */
          function resize() {
            refreshHeaders(null, scope.aweGridHeader.columnHeaders);
          }

          // create cols as soon as $gridscope is avavilable
          // grids in tabs with lazy loading come later, so we need to
          // setup a watcher
          var unwatch = scope.$watch('aweGridHeader', function (gridOptions) {
            if (gridOptions && "layers" in gridOptions && "clickout" in gridOptions.layers) {
              // Bind grid layers
              viewPort = gridOptions.layers.clickout;
              headerContainer = gridOptions.layers.header;

              // Bind scroll once
              angular.element(viewPort).bind("scroll", updateScroll);

              // Check if gridOptions have columnHeaders
              if ("columnHeaders" in gridOptions) {
                refreshHeaders(null, gridOptions.columnHeaders);
              }

              /******************************************************************************
               * EVENT LISTENERS
               *****************************************************************************/
              var listeners = {};
              // Capture event for element resize
              listeners['columns-changed'] = scope.$on('columns-changed', refreshHeaders);
              listeners['resize'] = scope.$on("resize", resize);
              listeners['resize-action'] = scope.$on("resize-action", resize);
              listeners['unload'] = scope.$on("unload", function (event, view) {
                if (view === scope.$parent.view) {
                  Utilities.clearListeners(listeners);
                }
              });

              // Unwatch initialization
              unwatch();
            }
          });
        }
      };
    }
  ]);
