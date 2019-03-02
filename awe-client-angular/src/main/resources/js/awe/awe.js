import $ from "jquery";
import * as angular from "angular";
import "angular-ui-router";

// Route methods
import { states } from './data/routes';

// Fix async load issue
$.ajaxPrefilter(options => options.async = true);

// AWE Application
export const aweApplication = angular.module("aweApplication", [
  'cfp.loadingBar',
  'ui.router',
  'ui.bootstrap',
  'ngAnimate',
  'ngCookies',
  'ngSanitize',
  'pascalprecht.translate'])
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

// Put aweApplication on self to be accessed by the external tools, as fileManager
self.aweApplication = aweApplication;
