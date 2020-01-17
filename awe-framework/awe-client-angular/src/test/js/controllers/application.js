import $ from "jquery";
import _ from "lodash";
import {DefaultSettings} from "../../../main/resources/js/awe/data/options";

describe('awe-framework/awe-client-angular/src/test/js/controllers/application.js', function() {
  var scope, controller, $utilities, $loadingBar, $settings, $storage, $serverData, $log, $window;
  var currentStatus = 0;
  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');
    inject(["$rootScope", "$controller", "$log", "AweUtilities", "LoadingBar", "AweSettings", "Storage", "ServerData", "$window", "$httpBackend",
    function($rootScope, $controller, _$log_, _AweUtilities_, _LoadingBar_, _AweSettings_, _Storage_, _ServerData_, _$window_, $httpBackend){
      scope = $rootScope.$new();
      $utilities = _AweUtilities_;
      $loadingBar = _LoadingBar_;
      $settings = _AweSettings_;
      $storage = _Storage_;
      $serverData = _ServerData_;
      $log = _$log_;
      $window = _$window_;

      controller = $controller('AppController', {
        '$scope': scope,
        '$log': $log,
        'LoadingBar': $loadingBar,
        'ServerData': $serverData,
        'Storage': $storage,
        'AweUtilities': $utilities,
        'AweSettings': $settings
      });

      // backend definition common for all tests
      $httpBackend.when('POST', 'settings').respond(DefaultSettings);
    }]);
  });

  // A simple test to verify the controller exists
  it('should exist', function() {
    expect(controller).toBeDefined();
  });

  // Once initialized, launch tests
  describe('once initialized', function() {

    // Check browser
    it('checks if browser is internet explorer', function() {
      let browser = $utilities.getBrowser();
      expect(controller.isIE()).toEqual(browser.includes("ie") ? browser : `not-ie ${browser}`);
    });

    // Check keypress event
    it('checks event keypress with a Alt+Shift+1 key', function(done) {
      spyOn($settings, "update").and.callFake((changes) => {
        expect(changes).toEqual({actionsStack: 1000});
        done();
      });
      controller.onKeydown({
        shiftKey: true,
        altKey: true,
        which: 49
      });
    });

    // Check keypress event
    it('checks event keypress with a Alt+Shift+0 key', function(done) {
      spyOn($settings, "update").and.callFake((changes) => {
        expect(changes).toEqual({actionsStack: 0});
        done();
      });
      controller.onKeydown({
        shiftKey: true,
        altKey: true,
        which: 48
      });
    });

    // Check keypress event
    it('checks event keypress with other key', function() {
      controller.onKeydown({
        shiftKey: false,
        altKey: true,
        which: 33
      });
    });

    // Check state change start
    it('should launch a state change', function() {
      spyOn(_, "merge").and.returnValue({base: {}, report: {}});
      scope.$emit("$stateChangeStart", {}, {}, {}, {});
    });

    // Check state change start
    it('should launch a state change with connection', function() {
      spyOn($storage, "getRoot").and.returnValue({});
      spyOn($storage, "get").and.returnValue({base: {screen: "ee"}, report: {screen: "sasasd", onunload: "asdasd"}});
      scope.$emit("$stateChangeStart", {views: {base: {}, report: {}}}, {}, {views: {base: {}}}, {});
    });

    // Check state change success
    it('should launch a state change success', function() {
      scope.$emit("$stateChangeSuccess", {}, {}, {}, {});
      expect(scope.resizing).toBe(false);
    });

    // Check state change error
    it('should launch a state change error', function() {
      spyOn($log, "warn");
      scope.$emit("$stateChangeError", {}, {}, {}, {});
      expect(scope.loading).toBe(false);
      expect(scope.resizing).toBe(false);
      expect($log.warn).toHaveBeenCalled();
    });

    // Check state change error
    it('should launch a state not found error', function() {
      spyOn($log, "warn");
      scope.$emit("$stateNotFound", {}, {}, {}, {});
      expect(scope.loading).toBe(false);
      expect($log.warn).toHaveBeenCalled();
    });

    // Init load state
    it('should init load', function() {
      scope.$emit("cfpLoadingBar:started");
      expect(scope.loading).toBe(true);
    });

    // Complete load state
    it('should end load', function() {
      scope.$emit("cfpLoadingBar:completed");
      expect(scope.loading).toBe(false);
    });

    // Call window resize
    it('should call window resize', function() {
      spyOn($utilities, "publish");
      $($window).trigger("resize");
      expect($utilities.publish).toHaveBeenCalled();
    });

    // Call resize action
    it('should call resize action', function() {
      let action = {attr: () => ({delay: 500}), accept: () => null};
      spyOn($utilities, "timeout").and.callFake((fn) => fn());
      spyOn(action, "accept");
      scope.$emit("/action/resize", action);
      expect(action.accept).toHaveBeenCalled();
    });

    // Call resize action without delay
    it('should call resize action without delay', function() {
      let action = {attr: () => ({}), accept: () => null};
      spyOn($utilities, "timeout").and.callFake((fn) => fn());
      spyOn(action, "accept");
      scope.$emit("/action/resize", action);
      expect(action.accept).toHaveBeenCalled();
    });

    // Initialize application
    it('should initialize application', function() {
      spyOn($utilities, "timeout").and.callFake((fn) => fn());
      spyOn($loadingBar, "endTask");
      scope.$emit("initialised");
      expect($loadingBar.endTask).toHaveBeenCalled();
    });

    // Before unload call
    it('should call on before unload', function() {
      spyOn($storage, "get").and.returnValue({base: {screen: "ee"}, report: {screen: "sasasd", onunload: "asdasd"}});
      spyOn($serverData, "send");
      controller.beforeUnload();
      expect($serverData.send).toHaveBeenCalled();
    });
  });
});