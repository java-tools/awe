import { aweApplication } from "./../awe";
import numeral from "numeral";

// Translate multiple filter
aweApplication.filter('formatNumber', ['AweSettings', ($settings) => (number, options) => {
  numeral.locale($settings.getLanguage().toLowerCase());
  return numeral(number).format(options);
}]);