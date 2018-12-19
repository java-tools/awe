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
      thousandsSep: " ",
      decimalSep: ","
    });
    frFmtInt = nf({
      digitsAfterDecimal: 0,
      thousandsSep: " ",
      decimalSep: ","
    });
    frFmtPct = nf({
      digitsAfterDecimal: 1,
      scaler: 100,
      suffix: "%",
      thousandsSep: " ",
      decimalSep: ","
    });
    return $.pivotUtilities.locales.fr = {
      localeStrings: {
        renderError: "Une erreur est survenue en dessinant le tableau crois&eacute;.",
        computeError: "Une erreur est survenue en calculant le tableau crois&eacute;.",
        uiRenderError: "Une erreur est survenue en dessinant l'interface du tableau crois&eacute; dynamique.",
        selectAll: "S&eacute;lectionner tout",
        selectNone: "S&eacute;lectionner rien",
        tooMany: "(trop de valeurs &agrave; afficher)",
        filterResults: "Filtrer les valeurs",
        totals: "Totaux",
        vs: "sur",
        by: "par"
      },
      aggregators: {
        "Count": {fn: tpl.count(frFmtInt), label: "Nombre"},
        "Count Unique Values": {fn: tpl.countUnique(frFmtInt), label: "Nombre de valeurs uniques"},
        "List Unique Values": {fn: tpl.listUnique(", "), label: "Liste de valeurs uniques"},
        "Sum": {fn: tpl.sum(frFmt), label: "Somme"},
        "Integer Sum": {fn: tpl.sum(frFmtInt), label: "Somme en entiers"},
        "Average": {fn: tpl.average(frFmt), label: "Moyenne"},
        "Minimum": {fn: tpl.min(frFmt), label: "Minimum"},
        "Maximum": {fn: tpl.max(frFmt), label: "Maximum"},
        "Sum over Sum": {fn: tpl.sumOverSum(frFmt), label: "Ratio de sommes"},
        "80% Upper Bound": {fn: tpl.sumOverSumBound80(true, frFmt), label: "Borne sup&eacute;rieure 80%"},
        "80% Lower Bound": {fn: tpl.sumOverSumBound80(false, frFmt), label: "Borne inf&eacute;rieure 80%"},
        "Sum as Fraction of Total": {fn: tpl.fractionOf(tpl.sum(), "total", frFmtPct), label: "Somme en proportion du totale"},
        "Sum as Fraction of Rows": {fn: tpl.fractionOf(tpl.sum(), "row", frFmtPct), label: "Somme en proportion de la ligne"},
        "Sum as Fraction of Columns": {fn: tpl.fractionOf(tpl.sum(), "col", frFmtPct), label: "Somme en proportion de la colonne"},
        "Count as Fraction of Total": {fn: tpl.fractionOf(tpl.count(), "total", frFmtPct), label: "Nombre en proportion du totale"},
        "Count as Fraction of Rows": {fn: tpl.fractionOf(tpl.count(), "row", frFmtPct), label: "Nombre en proportion de la ligne"},
        "Count as Fraction of Columns": {fn: tpl.fractionOf(tpl.count(), "col", frFmtPct), label: "Nombre en proportion de la colonne"}
      },
      renderers: {
        "Table": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Table"]), label: "Table"},
        "Table Barchart": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Table Barchart"]), label: "Table avec barres"},
        "Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Heatmap"]), label: "Carte de chaleur"},
        "Row Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Row Heatmap"]), label: "Carte de chaleur par ligne"},
        "Col Heatmap": {fn: new NRecoPivotTableExtensions({}).wrapTableRenderer($.pivotUtilities.renderers["Col Heatmap"]), label: "Carte de chaleur par colonne"}
      }
    };
  });

}).call(this);