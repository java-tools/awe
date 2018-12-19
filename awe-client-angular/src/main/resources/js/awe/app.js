import jquery from "jquery";
import moment from "moment";
import numeral from "numeral";

// Export variables to global context (to be accessed by local files)
self.jQuery = jquery;
self.moment = moment;
self.numeral = numeral;

import "angular";
import "angular-cookies";
import "angular-sanitize";
import "angular-animate";
import "angular-translate";
import "angular-loading-bar";

// Fix for jquery special events
function fixSpecialEvents(events) {
  events.forEach(name => {
    jQuery.event.special[name] = {
      setup: function (_, ns, handle) {
        if (ns.includes("noPreventDefault")) {
          this.addEventListener(name, handle, {passive: false});
        } else {
          this.addEventListener(name, handle, {passive: true});
        }
      }
    };
  });
}

// Add special events to fix here
fixSpecialEvents(["touchstart", "touchmove", "touchend"]);

// Imports
import "./awe";
import "./controllers/controllers";
//import "./directives/directives";
import "./filters/filters";

require("jquery-ui");
require("angular-ui-bootstrap");
require("angular-base64");
require("bootstrap");

require("./data/actions");
require("./data/options");

require("./services/control");
require("./services/load");
require("./services/loadingBar");
require("./services/locals");
require("./services/position");
require("./services/serverData");
require("./services/settings");
require("./services/utilities");

require("./services/component");
require("./services/criterion");
require("./services/column");
require("./services/action");
require("./services/dependency");

require("./services/connection");
require("./services/ajax");
require("./services/comet");

require("./services/storage");
require("./services/sessionStorage");
require("./services/tabStorage");
require("./services/windowStorage");

require("./services/validator");
require("./services/validationRules");

require("./singletons/actionController");
require("./singletons/dependencyController");

require("./directives/form");
require("./directives/loader");
require("./directives/pressEnter");
require("./directives/rightClick");
require("./directives/help");
require("./directives/tagList");
require("./directives/translateMultiple");
require("./directives/plugins/uiDependency");

require("./services/maximize");
require("./services/panel");
require("./directives/window");
require("./directives/resizable");

require("spin.js");
require("./directives/plugins/uiSpin");

require("./directives/info");
require("./directives/infoDropdown");

require("./services/button");
require("./directives/button");
require("./directives/infoButton");
require("./directives/column/button");

require("./directives/menu");
require("./directives/option");

require("./services/contextMenu");
require("./directives/contextMenu");
require("./directives/contextOption");

require("jquery-file-download");
require("./directives/downloader");

require("./services/text");
require("./directives/input/text");
require("./directives/input/hidden");
require("./directives/column/text");

require("./directives/input/textarea");
require("./directives/column/textarea");

require("./directives/input/textView");
require("./directives/column/textView");

require("./services/checkboxRadio");
require("./directives/input/checkbox");
require("./directives/input/buttonCheckbox");
require("./directives/input/radio");
require("./directives/input/buttonRadio");
require("./directives/column/checkbox");

require("./directives/column/progress");

require("ng-caps-lock");
require("./directives/input/password");
require("./directives/column/password");

require("Select2");
require("./services/selector");
require("./directives/plugins/uiSelect");
require("./directives/input/select");
require("./directives/input/selectMultiple");
require("./directives/input/suggest");
require("./directives/input/suggestMultiple");
require("./directives/column/select");
require("./directives/column/selectMultiple");
require("./directives/column/suggest");
require("./directives/column/suggestMultiple");

require("autonumeric");
require("bootstrap-slider");
require("./services/numeric");
require("./directives/plugins/uiNumeric");
require("./directives/plugins/uiSlider");
require("./directives/input/numeric");
require("./directives/column/numeric");

require("angular-bootstrap-colorpicker");
require("./directives/input/color");
require("./directives/column/color");

require("bootstrap-datepicker");
require("bootstrap-timepicker");
require("./services/dateTime");
require("./directives/plugins/uiDate");
require("./directives/plugins/uiTime");
require("./directives/input/date");
require("./directives/input/filteredCalendar");
require("./directives/input/time");
require("./directives/column/date");
require("./directives/column/filteredCalendar");
require("./directives/column/time");

require("./services/formattedText");
require("./directives/column/formattedText");


require("ng-file-upload");
require("./services/uploader");
require("./directives/input/upload");
require("./directives/column/upload");

require("bootstrap-markdown/js/bootstrap-markdown");
require("style-loader!css-loader!bootstrap-markdown/css/bootstrap-markdown.min");
require("./directives/input/markdownEditor");

require("./services/dialog");
require("./directives/plugins/uiModal");
require("./directives/confirm");
require("./directives/dialog");
require("./directives/column/dialog");

require("bootstrap-tabdrop");
require("bootstrap-tabdrop-less");
require("./directives/plugins/uiTabdrop");
require("./directives/input/tab");
require("./directives/tabcontainer");

require("./directives/input/wizard");
require("./directives/wizardpanel");

require("./directives/accordion");
require("./directives/accordionItem");

require("./directives/video");

require("angular-ui-grid");
require("./services/grid/base");
require("./services/grid/commons");
require("./services/grid/components");
require("./services/grid/editable");
require("./services/grid/events");
require("./services/grid/multioperation");
require("./services/grid/tree");
require("./directives/grid");
require("./directives/gridHeader");
require("./directives/treegrid");

require("jquery-ui-sortable-npm");
require("../lib/pivotTable/pivot");
require("../lib/pivotTable/nrecopivot");
require("./directives/plugins/uiPivotTable");
require("./directives/pivotTable");

require("highcharts/highcharts-more.src")(Highcharts);
require("highcharts/highcharts-3d.src")(Highcharts);
require("highcharts/modules/drilldown.src")(Highcharts);
require("highcharts/modules/boost.src")(Highcharts);
require("highcharts/modules/no-data-to-display.src")(Highcharts);
require("highcharts/modules/exporting.src")(Highcharts);
require("HighchartsLocale");
require("./directives/plugins/uiChart");
require("./services/chart/chart");
require("./services/chart/events");
require("./services/chart/sparkline");
require("./directives/chart");

require("../lib/highcharts/themes/themes");
require("../lib/highcharts/themes/blue");
require("../lib/highcharts/themes/dark-green");
require("../lib/highcharts/themes/dark-blue");
require("../lib/highcharts/themes/dark-unica");
require("../lib/highcharts/themes/gray");
require("../lib/highcharts/themes/green");
require("../lib/highcharts/themes/grid");

require("../lib/tocify/jquery.tocify");
require("./directives/plugins/uiToc");

require("./directives/map");

require("./directives/column/icon");
require("./directives/column/progress");
require("./directives/column/image");

require("./directives/viewers/fileViewer");
require("./directives/viewers/helpViewer");
require("./directives/viewers/pdfViewer");
require("./directives/viewers/logViewer");
