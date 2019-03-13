describe('Download controller', function() {
  let scope, controller, $utilities, $actionController, $dependencyController, $screen;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$rootScope", "$controller", "AweUtilities", "ActionController",
      function($scope, $controller, _AweUtilities_, _ActionController_){
      scope = $scope.$new();
      $utilities = _AweUtilities_;
      $actionController = _ActionController_;
      controller = $controller('DownloadController', {
        '$scope': scope,
        'AweUtilities': $utilities,
        'ActionController': $actionController
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
    // Start a download
    it('should start a download', function() {
      controller.startDownload({});
      expect(controller.downloads.length).toBe(1);
    });

    // Finish a download
    it('should finish a download with an action', function() {
      spyOn($utilities, "timeout").and.callFake((fn) => fn());
      let file = {action: {accept: () => null}};
      controller.startDownload(file);
      expect(controller.downloads.length).toBe(1);
      controller.finishDownload(file);
      expect(controller.downloads.length).toBe(0);
    });

    // Fail a download
    it('should fail a download', function() {
      spyOn($utilities, "timeout").and.callFake((fn) => fn());
      spyOn($actionController, 'sendMessage');
      let file = {};
      controller.startDownload(file);
      expect(controller.downloads.length).toBe(1);
      controller.failDownload(file);
      expect(controller.downloads.length).toBe(0);
      expect($actionController.sendMessage).toHaveBeenCalled();
    });

    // Trigger scope events
    it('should trigger scope events', function() {
      scope.$emit("download-file", {});
    });
  });
});