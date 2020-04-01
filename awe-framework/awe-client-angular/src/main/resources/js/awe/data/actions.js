// Client actions
export const ClientActions = {
  // Available screen actions
  screen: {
    "screen": {method: "screen"},
    "reload": {method: "reload"},
    "back": {method: "back"},
    "wait": {method: "wait"},
    "screen-data": {method: "screenData"},
    "dialog": {method: "openDialog"},
    "close": {method: "closeDialog"},
    "close-cancel": {method: "closeDialogAndCancel"},
    "end-load": {method: "endLoad"},
    "change-language": {method: "changeLanguage"},
    "change-theme": {method: "changeTheme"},
    "get-file": {method: "getFile"},
    "disable-dependencies": {method: "disableDependencies"},
    "enable-dependencies": {method: "enableDependencies"},
    "add-class": {method: "addClass"},
    "remove-class": {method: "removeClass"},
    "print": {method: "screenPrint"},
    "redirect": {method: "redirect"},
    "redirect-screen": {method: "redirectScreen"},
    "close-window": {method: "closeWindow"},
    "end-dependency": {method: "endDependency"}
  },
  // Available message
  message: {
    "message": {method: "message"},
    "target-message": {method: "targetMessage"},
    "confirm": {method: "confirm"}
  },
  // Available form actions
  form: {
    "reset": {method: "reset"},
    "restore": {method: "restore"},
    "restore-target": {method: "restoreTarget"},
    "validate": {method: "validate"},
    "set-valid": {method: "setValid"},
    "set-invalid": {method: "setInvalid"},
    "server": {method: "server"},
    "server-print": {method: "serverPrint"},
    "server-download": {method: "serverDownload"},
    "fill": {method: "fill"},
    "update-controller": {method: "updateController"},
    "select": {method: "select"},
    "logout": {method: "logout"},
    "cancel": {method: "cancel"},
    "confirm-updated-data": {method: "checkModelUpdated"},
    "confirm-not-updated-data": {method: "checkModelNoUpdated"},
    "confirm-empty-data": {method: "checkModelEmpty"},
    "value": {method: "value"}
  },
  // Available component actions
  component: {
    "filter": {method: "reload"},
    "start-load": {method: "startLoad"}
  },
  // Available component actions
  menu: {
    "toggle-menu": {method: "toggleMenu"},
    "toggle-navbar": {method: "toggleNavbar"},
    "change-menu": {method: "changeMenu"}
  },
  // Available chart actions
  chart: {
    "add-points": {method: "onAddPoints", check: ["component", "view"]},
    "add-chart-series": {method: "onAddSeries", check: ["component", "view"]},
    "remove-chart-series": {method: "onRemoveSeries"}, check: ["component", "view"],
    "replace-chart-series": {method: "onReplaceSeries", check: ["component", "view"]},
    "reset": {method: "onReset", check: ["component", "view"]},
    "restore": {method: "onRestore", check: ["component", "view"]}
  },
  // Available wizard actions
  wizard: {
    "next-step": {method: "next"},
    "prev-step": {method: "prev"},
    "first-step": {method: "first"},
    "last-step": {method: "last"},
    "nth-step": {method: "nth"}
  },
  // Available pivot table actions
  pivot: {
    "set-pivot-sorters": {method: "onSetSorters"},
    "set-pivot-group-rows": {method: "onSetRows"},
    "set-pivot-group-cols": {method: "onSetCols"}
  },
  // Available uploader actions
  uploader: {
    "file-status": {method: "onFileStatus"},
    "file-uploaded": {method: "onFileUploaded"},
    "clear-file": {method: "onReset"}
  },
  // Available taglist actions
  taglist: {
    "taglist-data": {method: "onData"},
  },
  // Available grid actions
  grid: {
    // Shared actions
    commons: {
      "reset": {method: "onReset"},
      "restore": {method: "onRestore"},
      "select-first-row": {method: "onSelectFirstRow"},
      "select-last-row": {method: "onSelectLastRow"},
      "select-all-rows": {method: "onSelectAllRows"},
      "unselect-all-rows": {method: "onUnselectAllRows"},
      "check-one-selected": {method: "onCheckOneRowSelected"},
      "check-some-selected": {method: "onCheckSomeRowSelected"},
      "check-records-generated": {method: "onCheckRecordsGenerated"},
      "delete-row": {method: "onDeleteRow"},
      "after-delete-row": {method: "onAfterDeleteRow"},
      "add-row": {method: "onAddRow"},
      "add-row-top": {method: "onAddRowTop"},
      "add-row-down": {method: "onAddRowAfterSelected"},
      "add-row-up": {method: "onAddRowBeforeSelected"},
      "after-add-row": {method: "onAfterAddRow"},
      "update-row": {method: "onUpdateRow"},
      "copy-row": {method: "onCopyRow"},
      "copy-row-top": {method: "onCopyRowTop"},
      "copy-row-down": {method: "onCopyRowAfterSelected"},
      "copy-row-up": {method: "onCopyRowBeforeSelected"},
      "copy-selected-rows-clipboard": {method: "onCopySelectedRowsToClipboard"},
      "add-columns": {method: "onAddColumns"},
      "replace-columns": {method: "onReplaceColumns"},
      "show-columns": {method: "onShowColumns"},
      "hide-columns": {method: "onHideColumns"},
      "update-cell": {method: "onUpdateCell", check: ["component", "view"]}
    },
    // Treegrid actions
    tree: {
      "tree-branch": {method: "onBranchExpand"}
    },
    // Editable grid actions
    editable: {
      "save-row": {method: "onSaveRow"},
      "after-save-row": {method: "onAfterSaveRow"},
      "cancel-row": {method: "onCancelRow"},
      "after-cancel-row": {method: "onAfterCancelRow"},
      "check-records-saved": {method: "onCheckRecordSaved"},
      "validate-selected-row": {method: "validateSelectedRow"}
    }
  }
};