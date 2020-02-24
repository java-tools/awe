import $ from "jquery";
import * as angular from "angular";
import uiRouter from "angular-ui-router";
import ngCookies from "angular-cookies";
import ngSanitize from "angular-sanitize";
import ngAnimate from "angular-animate";
import ngTranslate from "angular-translate";
import ngLoadingBar from "angular-loading-bar";
import uiBootstrap from "angular-ui-bootstrap";

import "jquery-ui";
import "bootstrap";

// Route methods
import { states } from './data/routes';

// Fix for jquery special events
function fixSpecialEvents(events) {
  events.forEach(name => {
    $.event.special[name] = {
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

// Fix async load issue
$.ajaxPrefilter(options => options.async = true);

// AWE Application
export const aweApplication = angular.module("aweApplication", [
  ngLoadingBar,
  uiRouter,
  uiBootstrap,
  ngAnimate,
  ngCookies,
  ngSanitize,
  ngTranslate])
// Config state router
.config(["$stateProvider", ($stateProvider) => states.forEach((state) => $stateProvider.state(state))])
// Filter animate provider
.config(["$animateProvider", $animate => $animate.classNameFilter(/^((?!no-animate).)*$/)])
// For any unmatched url, redirect to /
.config(["$urlRouterProvider", $urlRouter => $urlRouter.otherwise("/")])
// Activate HTML5 Mode (doing this we remove the hash (#) in the url
.config(["$locationProvider", $location => $location.html5Mode(true)])
// Apply async
.config(["$httpProvider", $http => $http.useApplyAsync(true)])
// Config translate
.config(["$translateProvider", $translate => {
  $translate.useLoader('AweLocals');
  $translate.useSanitizeValueStrategy(null);
}])
// Init settings and preload templates
.run(["AweSettings", $settings => $settings.init().then(() => $settings.preloadTemplates())]);

// Export jquery
export const jQuery = $;

// Put aweApplication on self to be accessed by the external tools, as fileManager
self.aweApplication = aweApplication;
