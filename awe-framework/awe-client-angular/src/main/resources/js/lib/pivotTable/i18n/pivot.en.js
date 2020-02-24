(function ($) {
  var callWithJQuery;
  callWithJQuery = function (pivotModule) {
    return pivotModule($);
  };
  callWithJQuery(function ($) {
    var usFmt, usFmtInt, usFmtPct, nf, tpl;
    nf = $.pivotUtilities.numberFormat;
    tpl = $.pivotUtilities.aggregatorTemplates;
    usFmt = nf();
    usFmtInt = nf({
      digitsAfterDecimal: 0
    });
    usFmtPct = nf({
      digitsAfterDecimal: 1,
      scaler: 100,
      suffix: "%"
    });
    return $.pivotUtilities.locales.en = {
      localeStrings: {
        renderError: "An error occurred rendering the PivotTable results.",
        computeError: "An error occurred computing the PivotTable results.",
        uiRenderError: "An error occurred rendering the PivotTable UI.",
        selectAll: "Select All",
        selectNone: "Select None",
        tooMany: "(too many to list)",
        filterResults: "Filter results",
        totals: "Totals",
        vs: "vs",
        by: "by"
      },
      aggregators: {
        "Count": {fn: tpl.count(usFmtInt), label: "Count"},
        "Count Unique Values": {fn: tpl.countUnique(usFmtInt), label: "Count Unique Values"},
        "List Unique Values": {fn: tpl.listUnique(", "), label: "List Unique Values"},
        "Sum": {fn: tpl.sum(usFmt), label: "Sum"},
        "Integer Sum": {fn: tpl.sum(usFmtInt), label: "Integer Sum"},
        "Average": {fn: tpl.average(usFmt), label: "Average"},
        "Minimum": {fn: tpl.min(usFmt), label: "Minimum"},
        "Maximum": {fn: tpl.max(usFmt), label: "Maximum"},
        "Sum over Sum": {fn: tpl.sumOverSum(usFmt), label: "Sum over Sum"},
        "80% Upper Bound": {fn: tpl.sumOverSumBound80(true, usFmt), label: "80% Upper Bound"},
        "80% Lower Bound": {fn: tpl.sumOverSumBound80(false, usFmt), label: "80% Lower Bound"},
        "Sum as Fraction of Total": {fn: tpl.fractionOf(tpl.sum(), "total", usFmtPct), label: "Sum as Fraction of Total"},
        "Sum as Fraction of Rows": {fn: tpl.fractionOf(tpl.sum(), "row", usFmtPct), label: "Sum as Fraction of Rows"},
        "Sum as Fraction of Columns": {fn: tpl.fractionOf(tpl.sum(), "col", usFmtPct), label: "Sum as Fraction of Columns"},
        "Count as Fraction of Total": {fn: tpl.fractionOf(tpl.count(), "total", usFmtPct), label: "Count as Fraction of Total"},
        "Count as Fraction of Rows": {fn: tpl.fractionOf(tpl.count(), "row", usFmtPct), label: "Count as Fraction of Rows"},
        "Count as Fraction of Columns": {fn: tpl.fractionOf(tpl.count(), "col", usFmtPct), label: "Count as Fraction of Columns"}
      },
      renderers: {
        "Table": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Table"]), label: "Table"},
        "Table Barchart": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Table Barchart"]), label: "Table Barchart"},
        "Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Heatmap"]), label: "Heatmap"},
        "Row Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Row Heatmap"]), label: "Row Heatmap"},
        "Col Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Col Heatmap"]), label: "Col Heatmap"}
      }
    };
  });
})(jQuery);