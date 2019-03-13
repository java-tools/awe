import { updateScreen, setScreenView, clearScreenView } from "./../../../main/resources/js/awe/actions/screen";

describe('View controller', function() {
  var scope, controller, $utilities, $ngRedux, $settings, $actionController, $control, $loadingBar;
  var currentStatus = 0;
  var originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$rootScope", "$controller", "AweUtilities", "$ngRedux", "LoadingBar",
     function($rootScope, $controller, _AweUtilities_, _$ngRedux_, _LoadingBar_){
      scope = $rootScope.$new();
      $utilities = _AweUtilities_;
      $ngRedux = _$ngRedux_;
      $loadingBar = _LoadingBar_;
      controller = $controller('ViewController', {
        '$scope': scope,
        '$ngRedux': $ngRedux,
        'LoadingBar': $loadingBar,
        'AweUtilities': $utilities
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
    it('should start', function() {
      controller.view = "base";
      spyOn($ngRedux, "connect").and.callFake(() => {
        controller.disconnect = () => null;
        return (fn) => fn({screen: {name: "TEST", visible: false, loaded: true}});
      });
      controller.$onInit();
    });

    // Initialize not already loaded
    it('should not start if not loaded yet', function() {
      controller.view = "base";
      spyOn($ngRedux, "connect").and.callFake(() => {
        controller.disconnect = () => null;
        return (fn) => fn({screen: {name: "TEST", visible: false, loaded: false}});
      });
      controller.$onInit();
    });

    // Avoid second initialization
    it('should not start if previously started', function() {
      controller.view = "base";
      controller.disconnect = () => null;
      controller.started = true;
      spyOn($ngRedux, "connect").and.callFake(() => {
        controller.disconnect = () => null;
        return (fn) => fn({screen: {name: "TEST", visible: false, loaded: true}});
      });
      controller.$onInit();
    });

    // Avoid second initialization
    it('should disconnect on destroy', function() {
      controller.view = "base";
      controller.disconnect = () => null;
      scope.$emit("$destroy");
      $ngRedux.dispatch(clearScreenView(controller.view));
    });
  });
});