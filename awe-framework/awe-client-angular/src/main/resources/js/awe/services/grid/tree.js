import { aweApplication } from "./../../awe";
import "./commons";

// Tree grid service
aweApplication.factory('GridTree',
  ['AweUtilities', 'Control', 'Component', 'GridEvents', 'GridCommons', '$log', 'ActionController', 'ServerData', 'AweSettings', 'uiGridConstants',
    function (Utilities, Control, Component, GridEvents, GridCommons, $log, ActionController, ServerData, $settings, uiGridConstants) {

      /**
       * Fix identifier
       * @param text
       */
      function sortTreeValues(tree) {
        let sortedTree = [];
        _.each(tree, node => {sortedTree = sortedTree.concat([node, ...sortTreeValues(node.$$children)]);});

        return sortedTree;
      }

      /**
       * Retrieve next index
       * @param {type} index
       * @param {type} level
       * @param {type} data
       * @returns {integer} next index
       */
      function getNextIndex(index, level, data) {
        let nextIndex = data.length;
        for (var i = index + 1, t = data.length; i < t; i++) {
          if (data[i].$$treeLevel <= level) {
            return i;
          }
        }
        return nextIndex;
      }

      /**
       * Remove row and children
       * @param {type} row
       */
      function removeRow(component, row) {
        var child = row.$$children.pop();
        while (child) {
          removeRow(component, child);
          child = row.$$children.pop();
        }

        // Remove from data list
        var position = component.model.values.indexOf(row);
        component.model.values.splice(position, 1);
      }

      /**
       * Grid constructor
       * @param {Scope} scope Criterion scope
       * @param {String} id Criterion id
       * @param {String} element Criterion element
       */
      let GridTree = function (scope, id, element) {
        this.scope = scope;
        this.id = id;
        this.element = element;
        this.component = new Component(this.scope, this.id);
        this.component.element = element;
        this.component.currentPage = 1;
        this.component.grid = this;
        this.component.isTree = true;
        this.sorting = [];
        this.component.asTree = function () {
          return this.grid.init();
        };

        return this.component;
      };

      GridTree.prototype = {
        /**
         * Initialize base grid
         */
        init: function () {
          // Get component
          var grid = this;
          var component = this.component;
          if (!component.asComponent()) {
            return false;
          }

          // Define 'CONSTANTS'
          component.constants = {
            SMALL_GRID: 750,
            ROW_IDENTIFIER: "_ID",
            ROW_CLASS_FIELD: "_style_",
            CELL_TITLE: "title",
            CELL_LABEL: "label",
            CELL_VALUE: "value",
            CELL_STYLE: "cell-style",
            CELL_ICON: "icon",
            CELL_IMAGE: "image"
          };

          // Initialize common methods
          this.commons = new GridCommons(this.component);

          /******************************************************************************
           * COMPONENT METHODS
           *****************************************************************************/

          /**
           * On tree header click
           * ng-class="{'fa-minus': grid.treeBase.numberLevels > 0 && grid.treeBase.expandAll, 'fa-plus': grid.treeBase.numberLevels > 0 && !grid.treeBase.expandAll}"
           * @param {grid} uiGrid
           * @param {event} $event
           * @returns {undefined}
           */
          component.treeHeaderButtonClick = function (uiGrid, $event) {
            Utilities.stopPropagation($event, true);
            if (uiGrid.treeBase.expandAll) {
              grid.api.treeBase.collapseAllRows();
            } else {
              grid.api.treeBase.expandAllRows();
            }
            // Change icon
            component.treeHeaderIcon = uiGrid.treeBase.numberLevels > 0 ? uiGrid.treeBase.expandAll ? grid.scope.gridOptions.icons.collapse : grid.scope.gridOptions.icons.expand : '';
          };

          /**
           * ng-class="{'fa-minus': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'fa-plus': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed'}"
           * @param {type} row
           * @param {type} $event
           * @returns {undefined}
           */
          component.treeButtonClick = function (row, $event) {
            Utilities.stopPropagation($event, true);
            if (!row.entity.$$isLeaf) {
              if (row.entity.$$loaded) {
                row.entity.$$treeIcon = row.entity.$$expanded ? component.scope.gridOptions.icons.expand + " icon-expand" : component.scope.gridOptions.icons.collapse + " icon-collapse";
                Utilities.timeout(function () {
                  grid.api.treeBase.toggleRowTreeState(row);
                }, 150);
              } else {
                // Load node
                row.entity.$$treeIcon = grid.scope.gridOptions.icons.loading;
                row.entity.$$isLoading = true;

                // Store the loading branch
                component.loadingBranch = row;
                component.isLoadingBranch = true;

                // Launch the load of the children of the branch
                var values = {};
                values.type = "tree-branch";
                values[$settings.get("targetActionKey")] = component.controller[$settings.get("targetActionKey")];
                values["expandingBranch"] = row.entity[component.constants.ROW_IDENTIFIER];

                // Generate server action
                var serverAction = ServerData.getServerAction(component.address, values, true, true);

                // Send action list
                ActionController.addActionList([serverAction], false, {address: component.address, context: component.context});
              }
            }
          };

          /**
           * Grid initialization
           */
          component.initGrid = function () {
            // Get row identity
            component.scope.gridOptions.getRowIdentity = function (row) {
              return row.entity[component.constants.ROW_IDENTIFIER];
            };
            // Broadcast rows rendered
            grid.api.core.on.rowsRendered(grid.scope, function () {
              Utilities.timeout.cancel(grid.rowsRenderTimeout);
              grid.rowsRenderTimeout = Utilities.timeout(function () {
                Utilities.publishFromScope("rows-rendered", {grid: grid.id}, grid.scope);
              });
            });
            // Manage on cell select events
            grid.api.selection.on.rowSelectionChanged(grid.scope, onSelectRow);
            grid.api.selection.on.rowSelectionChangedBatch(grid.scope, onSelectRow);
            grid.api.colMovable.on.columnPositionChanged(grid.scope, onChangeColumns);
            grid.api.colResizable.on.columnSizeChanged(grid.scope, onChangeColumns);
            grid.api.core.on.filterChanged(grid.scope, launchFilter);
            grid.api.core.on.rowsVisibleChanged(grid.scope, onFilter);
            // Manage scroll events
            grid.api.core.on.scrollBegin(grid.scope, scrollStart);
            grid.api.core.on.scrollEnd(grid.scope, scrollEnd);
            // Manage sort
            grid.api.core.on.sortChanged(grid.scope, onSort);
            // Manage tree events
            grid.api.treeBase.on.rowCollapsed(grid.scope, rowCollapsed);
            grid.api.treeBase.on.rowExpanded(grid.scope, rowExpanded);

            // Initialization
            component.treeHeaderIcon = component.scope.gridOptions.icons.expand;

            // End initialization
            Utilities.timeout(function () {
              // Initialize grid layers
              component.initLayers();
              // Initialize grid measures
              initMeasures();
              // Report changed columns
              component.reportChangedColumns();
              // Initialize grid
              component.updateModel();
              // Filter selection on initialization
              component.currentSelection = component.model.selected || [];
            });
          };

          /**
           * Add initial columns
           */
          component.addInitialColumns = function () {
          };
          /**
           * Toggle the tree row
           * @param {object} row
           * @param {boolean} expanded
           */
          component.toggleTreeRow = function (row, expanded) {
            row.$$expanded = expanded;
            if (component.controller.loadAll) {
              if (expanded) {
                grid.api.treeBase.expandRow(row);
              } else {
                grid.api.treeBase.collapseRow(row);
              }
            } else {
              setNodeIcon(row);
            }
            deferRowsRendered().then(function () {
              component.updateGridScrollBars();
            });
          };

          /**
           * Update the model (specific)
           */
          component.updateModelSpecific = function () {
            var deferred = Utilities.q.defer();
            // Update model
            component.defineTreeStructure(component.model.values);
            component.model.values = sortTreeValues(component.model.values.filter(node => node.$$treeLevel === 0));
            component.scope.gridOptions.data = component.model.values;
            // On rows rendered, publish
            deferRowsRendered().then(function () {
              // Publish grid data changed
              onUpdatedGridData();
              if (!component.initialized) {
                component.initialized = true;
              }
              if (component.controller.loadAll) {
                // Expand initial status
                component.expandInitialStatus();
              }
              // Resize
              component.resize();
              // Resolve promise
              deferred.resolve();
            });
            // Retrieve new id
            return deferred.promise;
          };

          /**
           * Parse a list of values into a tree list
           * @param {Array} data Data list
           * @returns {Array} data list as tree
           */
          component.defineTreeStructure = function (data) {
            let primaryIdName = component.controller.treeId;
            let parentIdName = component.controller.treeParent;
            let treeLeaf = component.controller.treeLeaf;
            var expandLevel = parseInt(component.controller.initialLevel || "0", 10);
            if (!data || data.length === 0 || !primaryIdName || !parentIdName) {
              return [];
            }

            let tree = {$$children: []}, rootIds = [], item, primaryKey, treeObjs = {},
              parentId, parent, len = data.length, i = 0;

            while (i < len) {
              item = data[i++];
              primaryKey = item[primaryIdName];
              item[component.constants.ROW_IDENTIFIER] = primaryKey;
              item.$$loaded = component.controller.loadAll;
              item.$$isLeaf = treeLeaf in item ? Utilities.parseBoolean(item[treeLeaf]) : item.$$loaded;
              item.$$isLoading = false;
              item.$$children = [];
              item.$$parent = tree;
              treeObjs[primaryKey] = item;
              // Static identifier
              parentId = item[parentIdName];

              if (parentId && parentId in treeObjs) {
                parent = treeObjs[parentId];
                parent.$$children.push(item);
                item.$$parent = parent;
                parent.$$isLeaf = false;
                parent.$$loaded = true;
              } else {
                rootIds.push(primaryKey);
              }
            }

            for (let j = 0; j < rootIds.length; j++) {
              tree.$$children.push(treeObjs[rootIds[j]]);
            }

            function setLevels(base, level) {
              _.each(base, function (node) {
                node.$$treeLevel = level;
                if ("expanded" in node) {
                  node.$$expanded = Utilities.parseBoolean(node.expanded);
                } else {
                  node.$$expanded = node.$$loaded && (level + 1) < expandLevel;
                }
                setLevels(node.$$children, level + 1);

                //'fa-minus': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'expanded', 'fa-plus': ( ( grid.options.showTreeExpandNoChildren && row.treeLevel > -1 ) || ( row.treeNode.children && row.treeNode.children.length > 0 ) ) && row.treeNode.state === 'collapsed'}"
                setNodeIcon(node);
              });
            }
            setLevels(tree.$$children, 0);
            component.treeData = tree;
            return data;
          };

          /**
           * Expand data as defined by initial status
           */
          component.expandInitialStatus = function () {
            _.each(component.model.values, function (node) {
              if (node.$$expanded) {
                var row = grid.api.grid.getRow(node);
                grid.api.treeBase.expandRow(row);
              }
            });
          };

          /**
           * Event launched when opening/closing a branch
           * @param {object} data Retreived data
           */
          component.onBranchExpand = function (data) {
            var branch = component.loadingBranch;

            // Finish loading parent
            branch.entity.$$isLoading = false;
            branch.entity.$$loaded = true;
            branch.entity.$$expanded = true;

            // Add loaded children
            _.each(data.rows, function (rowData) {
              component.addChildRow(rowData, branch.entity);
            });

            // Finish expanding
            component.loadingBranch = null;
            Utilities.timeout(function () {
              component.isLoadingBranch = false;
              grid.api.treeBase.expandRow(branch);
            }, 500);
          };

          /**
           * Add a new row
           * @param {object} row
           * @param {object} parent
           */
          component.addChildRow = function (row, parent) {
            // Restore row values
            component.addChildRowSpecific(row, parent);
          };
          /**
           * Select the rows
           */
          component.resetSelection = function () {
            if (grid.api) {
              grid.api.selection.clearSelectedRows();
            }
          };
          /**
           * Get the selected rows
           */
          component.getSelection = function () {
            var selectedRows;
            if (grid.api) {
              selectedRows = grid.api.selection.getSelectedRows();
            } else {
              selectedRows = [];
            }
            return selectedRows;
          };
          /**
           * Select the row
           * @param {Object} row Row to select
           */
          component.selectRow = function (row) {
            // Reset selection
            if (grid.api) {
              grid.api.selection.selectRow(row);
            }
          };
          /**
           * Generate a sorting algorithm
           * @param {type} a
           * @param {type} b
           * @returns {function}
           */
          component.sortingAlgorithm = function (a, b) {
            var nulls = grid.api.core.sortHandleNulls(a, b);
            if (nulls !== null) {
              return nulls;
            } else {
              var valA, valB;
              if (typeof a === 'object' && typeof b === 'object') {
                valA = a.value;
                valB = b.value;
              } else {
                valA = a;
                valB = b;
              }

              if (valA < valB) {
                return -1;
              } else if (valA > valB) {
                return 1;
              } else {
                return 0;
              }
            }
          };
          /**
           * Show the column
           * @param {string} columnId Column to show
           */
          component.showColumn = function (columnId) {
            // Check if column is already shown
            var column = grid.api.grid.getColumn(columnId);
            if (column.colDef.hidden) {
            	column.colDef.hidden = false;
            	column.colDef.width = column.colDef.initialWidth;
            	column.width = column.colDef.initialWidth;
            	column.drawnWidth = column.colDef.initialWidth;
            	finishPendingActions();
              Utilities.timeout(onChangeColumns);
            }
          };
          /**
           * Hide the column
           * @param {string} columnId Column to hide
           */
          component.hideColumn = function (columnId) {
            // Check if column is already hidden
            var column = grid.api.grid.getColumn(columnId);
            if (!column.colDef.hidden) {
            	column.colDef.hidden = true;
            	column.colDef.width = 0;
            	column.width = 0;
            	column.drawnWidth = 0;
              finishPendingActions();
              Utilities.timeout(onChangeColumns);
            }
          };
          /**
           * Change the column label
           * @param {string} columnId Column to change the label to
           * @param {string} label New label
           */
          component.changeColumnLabel = function (columnId, label) {
            // Retrieve the column index
            var column = grid.api.grid.getColumn(columnId);
            if (column.displayName !== label) {
              column.displayName = label;
              finishPendingActions();
            }
          };
          /**
           * On grid resize
           */
          component.resize = function () {
            // Resize grid
            if (component.initialized) {
              Utilities.timeout.cancel(component.resizeTimer);
              component.resizeTimer = Utilities.timeout(resize);
            }
          };
          /**
           * Retrieve grid columns;
           */
          component.getColumns = function () {
            var columns = component.controller.columnModel;
            // Resize grid
            if (grid.api) {
              columns = grid.api.grid.columns;
            }
            return columns;
          };
          /**
           * Basic getSpecificFields function (To be overwritten on complex directives)
           * @returns {Object} Specific fields from component
           */
          component.getSpecificFields = function () {
            // Retrieve specific query data
            return {
              max: component.controller.loadAll ? 0 : component.getMax(),
              page: component.currentPage,
              sort: grid.sorting
            };
          };
          /**
           * Adds a new row
           * @param {string} row Selected row
           * @param {integer} position
           * @param {object} data initial data
           * @return {object} Promise
           */
          component.addRowSpecific = function (row, position, data) {
            // Get selected row
            let newId;
            let scrollTime = 0;
            var rowData = data || {};
            let selectedRow = null;
            let parentRow;
            let childIndex;
              rowData.$$treeLevel = 0;
            newId = data && data.id ? data.id : "new-row-" + component.addedRows;
            component.addedRows++;
            rowData[component.constants.ROW_IDENTIFIER] = newId;
            rowData.$$isLeaf = true;
            rowData.$$loaded = true;
            rowData.$$children = [];
            rowData.$$parent = component.treeData;
            rowData.$$expanded = false;
            setNodeIcon(rowData);

            // Remove previous selected rows
            component.unselectRows();

            // Generate new row
            var rowIndex = component.model.values.length;
            if (row) {
              rowIndex = Control.getRowIndex(component.model.values, row, component.constants.ROW_IDENTIFIER);
              selectedRow = component.model.values[rowIndex];

              // Calculate rowIndex and treeLevel
              switch (position) {
                case "first":
                  rowIndex = 0;
                  break;
                case "before":
                  parentRow = selectedRow.$$parent;
                  childIndex = parentRow.$$children.indexOf(selectedRow);
                  rowData.$$treeLevel = selectedRow.$$treeLevel;
                  rowData.$$parent = parentRow;
                  parentRow.$$children.splice(childIndex, 0, rowData);
                  break;
                case "after":
                  parentRow = selectedRow.$$parent;
                  childIndex = parentRow.$$children.indexOf(selectedRow);
                  rowData.$$treeLevel = selectedRow.$$treeLevel;
                  rowData.$$parent = parentRow;
                  parentRow.$$children.splice(childIndex + 1, 0, rowData);
                  $log.info("Selected row index for AFTER: " + rowIndex);
                  rowIndex = getNextIndex(rowIndex, selectedRow.$$treeLevel, component.model.values);
                  $log.info("New row index for AFTER: " + rowIndex);
                  break;
                case "child":
                  // Finish loading parent
                  scrollTime = 150;
                  selectedRow.$$loaded = true;
                  selectedRow.$$children.push(rowData);
                  rowData.$$parent = selectedRow;
                  rowData.$$treeLevel = selectedRow.$$treeLevel + 1;
                  $log.info("Selected row index for CHILD: " + rowIndex);
                  rowIndex = getNextIndex(rowIndex, selectedRow.$$treeLevel, component.model.values);
                  $log.info("New row index for CHILD: " + rowIndex);
                  if (selectedRow.$$isLeaf || !selectedRow.$$expanded) {
                    selectedRow.$$isLeaf = false;
                    selectedRow.$$expanded = true;
                    selectedRow.$$treeIcon = component.scope.gridOptions.icons.collapse + " icon-expand";
                    Utilities.timeout(function () {
                      let selRow = grid.api.grid.getRow(selectedRow);
                      grid.api.treeBase.expandRow(selRow);
                    }, scrollTime);
                  }
                  break;
              }
            }

            // Add data to the model
            component.model.values.splice(rowIndex, 0, rowData);
            component.scope.gridOptions.data = component.model.values;
            grid.api.core.notifyDataChange(uiGridConstants.dataChange.ROW);
            // Retrieve new id
            return deferRowsRendered(function (deferred) {
              // Publish grid data changed
              onUpdatedGridData();
              // Resolve promise
              deferred.resolve(newId);
              // Show new row
              Utilities.timeout(function () {
                component.repositionSaveButton();
                if (grid.api) {
                  var gridRow = grid.api.grid.getRow(rowData);
                  grid.api.core.scrollToIfNecessary(gridRow, null);
                }
              }, scrollTime);
            });
          };
          /**
           * Add a new child row
           * @param {object} row
           * @param {object} parent
           * @return {String} rowId
           */
          component.addChildRowSpecific = function (row, parent) {
            var treeLeaf = component.controller.treeLeaf;

            // Add extra information to row
            row.$$isLeaf = treeLeaf in row ? Utilities.parseBoolean(row[treeLeaf]) : true;
            row.$$loaded = row.$$isLeaf;
            row.$$children = [];
            row.$$parent = parent;
            row.$$expanded = false;
            row.$$treeLevel = parent.$$treeLevel + 1;
            setNodeIcon(row);
            row[component.constants.ROW_IDENTIFIER] = row[component.controller.treeId];

            // Get last child
            var nodeBefore = parent;
            if (parent.$$children && parent.$$children.length > 0) {
              nodeBefore = parent.$$children[parent.$$children.length - 1];
            } else {
              parent.$$children = [];
            }
            var nodeIndex = component.model.values.indexOf(nodeBefore);

            // Add row to grid
            component.model.values.splice(nodeIndex + 1, 0, row);
            parent.$$children.push(row);
            component.scope.gridOptions.data = component.model.values;
            // Retrieve row identifier
            return deferRowsRendered(function (deferred) {
              // Publish grid data changed
              onUpdatedGridData();
              // Resolve promise
              deferred.resolve(row[component.constants.ROW_IDENTIFIER]);
              // Show new row
              Utilities.timeout(function () {
                component.repositionSaveButton();
                if (grid.api) {
                  var gridRow = grid.api.grid.getRow(row);
                  grid.api.core.scrollToIfNecessary(gridRow, null);

                }
              });
            });
          };
          /**
           * Removes the selected row
           * @param {string} rowId Selected row
           */
          component.deleteRowSpecific = function (rowId) {
            // If selectedRow is not null, remove row
            if (rowId) {
              // Calculate rowIndex
              var rowIndex = Control.getRowIndex(component.model.values, rowId, component.constants.ROW_IDENTIFIER);
              if (rowIndex > -1) {
                // Remove data from the model
                var rowToDelete = component.model.values[rowIndex];
                removeRow(component, rowToDelete);
                // Remove from parent
                var parentIndex = rowToDelete.$$parent.$$children.indexOf(rowToDelete);
                rowToDelete.$$parent.$$children.splice(parentIndex, 1);
                // Recalculate parent icon
                if (rowToDelete.$$treeLevel > 0) {
                  rowToDelete.$$parent.$$isLeaf = rowToDelete.$$parent.$$children.length === 0;
                  setNodeIcon(rowToDelete.$$parent);
                }
                component.scope.gridOptions.data = component.model.values;
              }
            }
            return deferRowsRendered().then(onUpdatedGridData);
          };
          /**
           * Check if grid size has changed & update new measures if changed
           * return {boolean} size has changed
           */
          component.gridSizeChanged = function () {
            var size = getGridSize();
            var changed = size.width > 0 && size.height > 0 && (
                grid.measures.height !== size.height || grid.measures.width !== size.width ||
                grid.measures.modelLength !== component.model.values.length);
            if (changed) {
              grid.measures = {
                ...size,
                modelLength: component.model.values.length
              };
            }
            return changed;
          };
          /**
           * Update grid scroll bars if changed visibility
           */
          component.updateGridScrollBars = function () {
            if (grid.api) {
              var contentSize = {width: 0, height: 0};
              $(component.layers["content"]).each(function () {
                contentSize.width += $(this).width();
                contentSize.height += $(this).height();
              });
              component.scope.gridOptions.enableHorizontalScrollbar = contentSize.width > component.layers["contentWrapper"].width() ? 1 : 0;
              component.scope.gridOptions.enableVerticalScrollbar = contentSize.height > component.layers["contentWrapper"].height() - component.layers["header"].height() ? 1 : 0;
              grid.api.core.handleWindowResize();
              grid.api.core.queueGridRefresh();
            }
          };
          /**
           * Check if grid is visible
           */
          component.isGridVisible = function () {
            return Utilities.isVisible(grid.element[0]);
          };
          /******************************************************************************
           * PRIVATE METHODS
           *****************************************************************************/

          /**
           * Publish grid data changed
           */
          function onUpdatedGridData() {
            // Publish model changed
            Control.publishModelChanged(component.address, {values: component.model.values});
            // Store event
            component.storeEvent('change');
            // Show/hide save button
            component.repositionSaveButton();
            // Resize the grid (to show or not scrollbars)
            component.resize();
          }
          /**
           * On scroll start
           */
          function scrollStart() {
            // Hide context menu if showing
            component.hideContextMenu();
            // Bind clickout scroll
            //component.layers["clickout"].on("scroll", onChangeLayout);
          }
          /**
           * On scroll end
           */
          function scrollEnd() {
            // Reposition save button if showing
            component.repositionSaveButton();
            // Unbind clickout scroll
            //component.layers["clickout"].off("scroll", onChangeLayout);
          }
          /**
           * On row collapsed
           * @param {type} row
           * @returns {undefined}
           */
          function rowCollapsed(row) {
            // Toggle row collapsed
            component.toggleTreeRow(row.entity, false);
          }
          /**
           * On row expanded
           * @param {type} row
           * @returns {undefined}
           */
          function rowExpanded(row) {
            // Toggle row expanded
            component.toggleTreeRow(row.entity, true);
          }
          /**
           * Link to scope function (onSelectRow)
           */
          function onSelectRow() {
            // Update selected rows
            if (component.initialized) {
              updateSelectedRows();
            }
          }
          /**
           * Link to scope function (onChangeColumns)
           */
          function onChangeColumns() {
            // Update selected rows
            if (component.initialized) {
              component.reportChangedColumns();
              // Reposition save buttons
              component.repositionSaveButton();
            }
          }
          /**
           * Refresh model
           */
          function refreshModel() {
            // If loadAll sort the model values
            if (component.controller.loadAll) {
              if (component.currentPage && component.currentPage !== component.model.page) {
                component.model.page = component.currentPage;
              }
            }
            component.updateModel();
          }
          /**
           * Manage on sort event
           * @param {Grid} gridScope
           * @param {Array} sortColumns
           */
          function onSort(gridScope, sortColumns) {
            // Fix sort parameters
            grid.sorting = [];
            _.each(sortColumns, function (column) {
              grid.sorting.push({id: column.colDef.sortfield, direction: column.sort.direction});
            });
            // Update the page
            pageUpdated();
          }

          /**
           * Manage on filter event
           */
          function isFiltering() {
            var filtering = false;
            var columns = component.getColumns();
            _.each(columns, function (column) {
              if (!Utilities.isEmpty(column.filters[0].term)) {
                filtering = true;
              }
            });
            return filtering;
          }

          /**
           * Manage on filter event
           */
          function launchFilter() {
            component.filterChanged = true;
            component.visibleRows = grid.api.core.getVisibleRows().length;
            if (isFiltering()) {
              if (!component.wasFiltering) {
                component.defaultPageSize = component.scope.gridOptions.paginationPageSize;
                component.previousPage = component.model.page;
                component.wasFiltering = true;
                component.scope.gridOptions.paginationPageSize = component.model.values.length;
                component.model.page = 1;
              }
            } else {
              component.wasFiltering = false;
              component.scope.gridOptions.paginationPageSize = component.defaultPageSize;
              component.model.page = component.previousPage;
            }
          }


          /**
           * Manage on filter event
           */
          function onFilter() {
            if (component.filterChanged && component.visibleRows !== grid.api.core.getVisibleRows().length) {
              var visibleRows = grid.api.core.getVisibleRows();
              // Update row number column size
              if (visibleRows.length > 0) {
                var columns = component.getColumns();
                grid.api.core.scrollToIfNecessary(visibleRows[0], columns[0]);
              }
              component.filterChanged = false;
            }
          }

          /**
           * Set node icon
           * @param node
           */
          function setNodeIcon(node) {
            // Define the row tree icon
            if (node.$$isLeaf) {
              node.$$treeIcon = component.scope.gridOptions.icons.leaf;
            } else {
              node.$$treeIcon = node.$$expanded ? component.scope.gridOptions.icons.collapse + " icon-collapse" : component.scope.gridOptions.icons.expand + " icon-expand";
            }
          }

          /**
           * Initialize grid measures
           */
          function initMeasures() {
            grid.measures = {
              width: undefined,
              height: undefined
            };
          }

          /**
           * Calculate grid sizes
           * @returns {object} Grid width and height in px
           */
          function getGridSize() {
            return {
              width: grid.element[0].clientWidth,
              height: grid.element[0].clientHeight
            };
          }

          /**
           * Resize grid
           */
          function resize() {
            // Check whether is visible or not
            if (component.isGridVisible()) {
              if (component.gridSizeChanged()) {
                // Check whether to show the scrollbar
                component.updateGridScrollBars();
              }
              // Reposition save button if showing
              component.repositionSaveButton();
            }
          }
          /**
           * Launch a promise on rows rendered
           * @param {type} onDefer
           */
          function deferRowsRendered(onDefer) {
            var deferred = Utilities.q.defer();
            var startTime = new Date();
            var listener = component.scope.$on("rows-rendered", function (event, parameters) {
              if (parameters.grid === grid.id) {
                // Remove listener
                listener();
                // Resolve promise
                if (onDefer) {
                  onDefer(deferred);
                } else {
                  deferred.resolve();
                }

                var dateDiff = (new Date() - startTime) / 1000;
                $log.debug("[GRID ROWS] Grid rows have been COMPILED", {grid: grid.id, compilationTime: dateDiff + "s"});
              }
            });
            // Retrieve new id
            return deferred.promise;
          }
          /**
           * Finish the pending actions
           */
          function finishPendingActions() {
            if (grid.api) {
              grid.api.grid.refresh();
            }
          }

          /**
           * Change the page to the stored one
           */
          function pageUpdated() {
            if (component.controller.loadAll) {
              // Retrieve new id
              component.updateModel();
              if (component.api && component.api.endLoad) {
                var listener = component.scope.$on("rows-rendered", function (event, parameters) {
                  if (parameters.grid === grid.id) {
                    // Remove listener
                    listener();
                    component.api.endLoad();
                  }
                });
              }
            } else {
              component.reload();
            }
          }
          /**
           * Updates selected rows in grid
           */
          function updateSelectedRows() {
            var selectedList = [];
            if (grid.api) {
              var selectedRows = grid.api.selection.getSelectedRows();
              _.each(selectedRows, function (row) {
                selectedList.push(row[component.constants.ROW_IDENTIFIER]);
              });
            }
            component.onSelectRows(selectedList);
          }

          /**********************************************************************/
          /* API                                                                */
          /**********************************************************************/
          /**
           * Update model values
           * @param {type} data
           */
          component.api.updateModelValues = function (data) {
            if (component.model) {
              _.merge(component.model, data);
              refreshModel();
            }
          };

          // Initialize common functions
          if (!this.commons.init()) {
            return false;
          }

          /**********************************************************************/
          /* EVENTS                                                             */
          /**********************************************************************/

          // Map grid actions
          GridEvents.mapTreeActions(component);

          // Initialized ok
          return true;
        }
      };
      return GridTree;
    }
  ]);
