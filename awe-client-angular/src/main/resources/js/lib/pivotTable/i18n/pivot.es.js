(function () {
  var callWithJQuery;

  callWithJQuery = function (pivotModule) {
    if (typeof exports === "object" && typeof module === "object") {
      return pivotModule(require("jquery"));
    } else if (typeof define === "function" && define.amd) {
      return define(["jquery"], pivotModule);
    } else {
      return pivotModule(jQuery);
    }
  };

  callWithJQuery(function ($) {
    var frFmt, frFmtInt, frFmtPct, nf, tpl;
    nf = $.pivotUtilities.numberFormat;
    tpl = $.pivotUtilities.aggregatorTemplates;
    frFmt = nf({
      thousandsSep: ".",
      decimalSep: ","
    });
    frFmtInt = nf({
      digitsAfterDecimal: 0,
      thousandsSep: ".",
      decimalSep: ","
    });
    frFmtPct = nf({
      digitsAfterDecimal: 1,
      scaler: 100,
      suffix: "%",
      thousandsSep: ".",
      decimalSep: ","
    });
    return $.pivotUtilities.locales.es = {
      localeStrings: {
        renderError: "Ocurri&oacute; un error durante la interpretaci&oacute;n de la tabla din&acute;mica.",
        computeError: "Ocurri&oacute; un error durante el c&acute;lculo de la tabla din&acute;mica.",
        uiRenderError: "Ocurri&oacute; un error durante el dibujado de la tabla din&acute;mica.",
        selectAll: "Seleccionar todo",
        selectNone: "Deseleccionar todo",
        tooMany: "(demasiados valores)",
        filterResults: "Filtrar resultados",
        totals: "Totales",
        vs: "vs",
        by: "por"
      },
      aggregators: {
        "Count": {fn: tpl.count(frFmtInt), label: "Cuenta"},
        "Count Unique Values": {fn: tpl.countUnique(frFmtInt), label: "Cuenta de valores &uacute;nicos"},
        "List Unique Values": {fn: tpl.listUnique(", "), label: "Lista de valores &uacute;nicos"},
        "Sum": {fn: tpl.sum(frFmt), label: "Suma"},
        "Integer Sum": {fn: tpl.sum(frFmtInt), label: "Suma de enteros"},
        "Average": {fn: tpl.average(frFmt), label: "Promedio"},
        "Minimum": {fn: tpl.min(frFmt), label: "Mínimo"},
        "Maximum": {fn: tpl.max(frFmt), label: "Máximo"},
        "Sum over Sum": {fn: tpl.sumOverSum(frFmt), label: "Suma de sumas"},
        "80% Upper Bound": {fn: tpl.sumOverSumBound80(true, frFmt), label: "Cota 80% superior"},
        "80% Lower Bound": {fn: tpl.sumOverSumBound80(false, frFmt), label: "Cota 80% inferior"},
        "Sum as Fraction of Total": {fn: tpl.fractionOf(tpl.sum(), "total", frFmtPct), label: "Proporci&oacute;n del total (suma)"},
        "Sum as Fraction of Rows": {fn: tpl.fractionOf(tpl.sum(), "row", frFmtPct), label: "Proporci&oacute;n de la fila (suma)"},
        "Sum as Fraction of Columns": {fn: tpl.fractionOf(tpl.sum(), "col", frFmtPct), label: "Proporci&oacute;n de la columna (suma)"},
        "Count as Fraction of Total": {fn: tpl.fractionOf(tpl.count(), "total", frFmtPct), label: "Proporci&oacute;n del total (cuenta)"},
        "Count as Fraction of Rows": {fn: tpl.fractionOf(tpl.count(), "row", frFmtPct), label: "Proporci&oacute;n de la fila (cuenta)"},
        "Count as Fraction of Columns": {fn: tpl.fractionOf(tpl.count(), "col", frFmtPct), label: "Proporci&oacute;n de la columna (cuenta)"}
      },
      renderers: {
        "Table": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Table"]), label: "Tabla"},
        "Table Barchart": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Table Barchart"]), label: "Tabla con barras"},
        "Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Heatmap"]), label: "Heatmap"},
        "Row Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Row Heatmap"]), label: "Heatmap por filas"},
        "Col Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Col Heatmap"]), label: "Heatmap por columnas"}
      }
    };
  });

}).call(this);