describe('awe-client-angular/src/test/js/controllers/view.js', function() {
  let scope, controller, $utilities, $actionController, $loadingBar, $storage;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(['$rootScope', '$controller', 'ServerData', 'Storage', 'ActionController', 'AweUtilities', 'Load', 'LoadingBar', '$log',
     function($rootScope, $controller, _ServerData_, _Storage_, _ActionController_, _AweUtilities_, _Load_, _LoadingBar_, _$log_){

      // Define injects
      scope = $rootScope.$new();
      $utilities = _AweUtilities_;
      $loadingBar = _LoadingBar_;
      $actionController = _ActionController_;
      $storage = _Storage_;

      // Define storage
      spyOn($storage, "get").and.returnValue({"base":{"components":[{"id":"cod_usr","controller":{"autoload":false,"checkEmpty":false,"checkInitial":true,"checkTarget":false,"checked":false,"component":"text","contextMenu":[],"dependencies":[],"icon":"user signin-form-icon","id":"cod_usr","loadAll":false,"optional":false,"placeholder":"SCREEN_TEXT_USER","printable":true,"readonly":false,"required":true,"showFutureDates":true,"showSlider":false,"showTodayButton":true,"showWeekends":true,"size":"lg","strict":true,"style":"no-label","validation":"required","visible":true},"model":{"selected":[],"defaultValues":[],"values":[],"page":1,"total":0,"records":0}},{"id":"pwd_usr","controller":{"autoload":false,"checkEmpty":false,"checkInitial":true,"checkTarget":false,"checked":false,"component":"password","contextMenu":[],"dependencies":[],"icon":"key signin-form-icon","id":"pwd_usr","loadAll":false,"optional":false,"placeholder":"SCREEN_TEXT_PASS","printable":true,"readonly":false,"required":true,"showFutureDates":true,"showSlider":false,"showTodayButton":true,"showWeekends":true,"size":"lg","strict":true,"style":"no-label","validation":"required","visible":true},"model":{"selected":[],"defaultValues":[],"values":[],"page":1,"total":0,"records":0}},{"id":"ButLogIn","controller":{"actions":[{"silent":false,"async":false,"type":"validate","parameters":{}},{"silent":false,"async":false,"type":"server","parameters":{"serverAction":"login"}}],"autoload":false,"buttonType":"submit","checkEmpty":false,"checkInitial":true,"checkTarget":false,"checked":false,"contextMenu":[],"dependencies":[],"icon":"sign-in","id":"ButLogIn","label":"BUTTON_LOGIN","loadAll":false,"optional":false,"printable":true,"readonly":false,"required":false,"showFutureDates":true,"showSlider":false,"showTodayButton":true,"showWeekends":true,"strict":true,"style":"no-class btn btn-primary signin-btn bg-primary","visible":true},"model":{"selected":[],"defaultValues":[],"values":[],"page":1,"total":0,"records":0}}],"messages":{},"actions":[],"screen":{"name":"signin","title":"SCREEN_TITLE_LOGIN","option":null}}});

      // Generate controller
      controller = $controller('ViewController', {
        '$scope': scope,
        'ServerData': _ServerData_,
        'Storage': $storage,
        'ActionController': $actionController,
        'AweUtilities': $utilities,
        'Load': _Load_,
        'LoadingBar': $loadingBar,
        '$log': _$log_,
        'screenData': "base",
        'context': ""
        });
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // A simple test to verify the controller exists
  it('should exist', function() {
    expect(controller).toBeDefined();
  });

  // Once initialized, launch tests
  describe('once initialized', function() {

    // Initialize
    it('should initialize again with empty data', function() {
      controller.onInit(null);
    });

    // Initialize
    it('should initialize again with bad data', function() {
      controller.onInit("dferg");
    });

    // Initialize
    it('should initialize again with bad data in good format', function() {
      controller.onInit({});
    });

    // Initialize
    it('should initialize again with components without screen', function() {
      controller.onInit({components:[]});
    });

    // Call unload
    it('should unload the view with ie', function() {
      spyOn($utilities, "getBrowser").and.returnValue("ie")
      scope.$emit("unload", "base");
      expect(scope.visible).toBe(false);
    });

    // Call unload
    it('should unload the view with chrome', function() {
      spyOn($utilities, "getBrowser").and.returnValue("chrome")
      scope.$emit("unload", "base");
    });
  });
});