import {DefaultSettings} from "../../../main/resources/js/awe/data/options";

describe('awe-client-angular/src/test/js/services/settings.js', function() {
  let $injector, $utilities, $settings, $storage, $rootScope, $translate, $httpBackend, $log, $state, $serverData;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    inject(["$injector", function(__$injector__) {
      $injector = __$injector__;
      $rootScope = $injector.get('$rootScope');
      $utilities = $injector.get('AweUtilities');
      $settings = $injector.get('AweSettings');
      $storage = $injector.get('Storage');
      $translate = $injector.get('$translate');
      $httpBackend = $injector.get('$httpBackend');
      $log = $injector.get('$log');
      $state = $injector.get('$state');
      $serverData = $injector.get('ServerData');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  it('should init', function(done) {
    // backend definition common for all tests
    $httpBackend.when('POST', 'settings').respond(DefaultSettings);
    spyOn($settings, "settingsLoaded").and.callFake(done);
    $settings.init();
    $httpBackend.flush();
  });

  it('should init with errors', function(done) {
    // backend definition common for all tests
    $httpBackend.when('POST', 'settings').respond(404, { foo: 'bar' });
    spyOn($log, "error").and.callFake(done);
    $settings.init();
    $httpBackend.flush();
  });

  it('should init settings', function() {
    spyOn($state, "go");
    $settings.settingsLoaded(DefaultSettings);
    expect($state.go).toHaveBeenCalled();
  });

  it('should init settings reloading current screen', function() {
    spyOn($state, "go");
    $settings.settingsLoaded({...DefaultSettings, reloadCurrentScreen: true});
    expect($state.go).toHaveBeenCalled();
  });

  it('should get a token', function() {
    spyOn($storage, "hasSession").and.returnValue(true);
    spyOn($storage, "getSession");
    spyOn($storage, "get");
    $settings.getToken();
    expect($storage.getSession).toHaveBeenCalled();
    expect($storage.get).not.toHaveBeenCalled();
  });

  it('should set a token', function(done) {
    spyOn($settings, "get").and.returnValue("es");
    spyOn($storage, "putSession");
    spyOn($injector, "get").and.returnValue({init: fn => null});
    spyOn($storage, "putRoot").and.callFake(done);
    spyOn($storage, "getSession").and.returnValue("tutu");
    $settings.setToken("tutu");
    expect($storage.putSession).toHaveBeenCalled();
    expect($storage.putRoot).toHaveBeenCalled();
  });

  it('should clear a token', function() {
    spyOn($storage, "removeSession");
    $settings.clearToken();
    expect($storage.removeSession).toHaveBeenCalled();
  });

  it('should try to change the same language without forcing', function() {
    spyOn($settings, "get").and.returnValue("es");
    spyOn($settings, "update");
    $settings.changeLanguage("es", false);
    expect($settings.update).not.toHaveBeenCalled();
  });

  it('should try to change the same language forcing', function() {
    spyOn($settings, "get").and.returnValue("es");
    spyOn($settings, "update");
    $settings.changeLanguage("es", true);
    expect($settings.update).toHaveBeenCalled();
  });

  it('should try to change language to null', function() {
    spyOn($settings, "update");
    $settings.changeLanguage(null, false);
    expect($settings.update).not.toHaveBeenCalled();
  });

  it('should try to change language successfully', function() {
    spyOn($settings, "get").and.returnValue("es");
    spyOn($translate, "use").and.returnValue({then: fn => fn()});
    spyOn($settings, "update");
    spyOn($utilities, "publish");
    $settings.changeLanguage("en", true);
    expect($utilities.publish).toHaveBeenCalled();
  });

  it('should preload the templates', function() {
    spyOn($injector, "get").and.returnValue($serverData);
    spyOn($serverData, "preloadAngularTemplate");
    $settings.preloadTemplates();
    expect($serverData.preloadAngularTemplate).toHaveBeenCalledTimes(15);
  });

  it('should update undefined settings', function() {
    spyOn($storage, "putRoot").and.callThrough();
    $settings.update();
    expect($storage.putRoot).toHaveBeenCalled();
  });
});