describe('awe-client-angular/src/test/js/controllers/message.js', function() {
  let scope, controller, $utilities, $settings, $actionController, $control, $injector, $storage;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$rootScope", "$controller", "AweUtilities", "AweSettings", "ActionController", "Control", "$injector", "Storage",
      function($rootScope, $controller, _AweUtilities_, _AweSettings_, _ActionController_, _Control_, _$injector_, _Storage_){
      scope = $rootScope.$new();
      $utilities = _AweUtilities_;
      $settings = _AweSettings_;
      $actionController = _ActionController_;
      $control = _Control_;
      $injector = _$injector_;
      $storage = _Storage_;
      controller = $controller('MessageController', {
        '$scope': scope,
        'AweSettings': $settings,
        'AweUtilities': $utilities,
        'Control': $control
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

    /**
     * Call a message action
     * @param {String} actionName Action name
     * @param {String} actionMethod Action method
     * @param {Object} parameters Parameters
     * @param {Boolean} async Async execution
     * @param {Boolean} silent Silent execution
     * @param {Boolean} top Add on top action
     * @param {Function} done Launch when done
     */
    function callMessageAction(actionName, actionMethod, parameters, async, silent, top, done) {
      // Spy screen action
      spyOn(controller.MessageActions, actionMethod).and.callFake(done);
      spyOn($utilities, "timeout").and.callFake((fn) => fn());

      // Launch action
      $actionController.closeAllActions();
      let action = $actionController.generateAction({type: actionName, context: "screen", ...parameters}, {view: "base"}, async, silent);
      $actionController.addActionList([action], top, {});
    }

    // Call message action
    it('should call a message action', function(done) {
      return callMessageAction("message", "message", {parameters:{}}, true, true, true, done);
    });

    // Call target-message action
    it('should call a target-message action', function(done) {
      return callMessageAction("target-message", "targetMessage", {parameters:{}}, false, true, true, done);
    });

    // Call confirm action
    it('should call a confirm action', function(done) {
      return callMessageAction("confirm", "confirm", {parameters:{}}, true, true, false, done);
    });

    /**
     * Launch a message action
     * @param {String} actionName Action name
     * @param {String} actionMethod Action method
     * @param {Object} parameters Parameters
     * @param {Function} done Launch when done
     */
    function launchMessageAction(actionName, actionMethod, parameters, done = () => null) {
      let $actionController = $injector.get('ActionController');
      spyOn($utilities, "timeout").and.callFake((fn) => fn());

      // Launch action
      $actionController.closeAllActions();
      let action = $actionController.generateAction({type: actionName, ...parameters}, {address: {view: "base"}}, true, true);

      // Spy screen action
      spyOn(action, "accept").and.callFake(done);
      spyOn(action, "isAlive").and.returnValue(true);

      // Call action
      controller.MessageActions[actionMethod].call(this, action);

      // Return action
      return action;
    }

    // Launch message action
    it('should launch a message action', function(done) {
      spyOn(controller.alerts, "push").and.callFake((message) => {
        expect(message.type).toBe("success");
        expect(message.title).toBe("tutu");
        expect(message.msg).toBe("lala");
        done();
      });
      return launchMessageAction("message", "message", {parameters:{type: "ok", title:"tutu", message: "lala"}});
    });

    // Launch message action
    it('should launch a message action without message', function(done) {
      spyOn(controller.alerts, "push").and.callFake((message) => {
        expect(message.type).toBe("danger");
        expect(message.title).not.toBeDefined();
        expect(message.msg).not.toBeDefined();
        done();
      });
      return launchMessageAction("message", "message", {parameters:{type: "error"}});
    });

    // Launch message action with a target message
    it('should launch a message action with a target message', function(done) {
      spyOn($storage, "get").and.returnValue({base: {testMessage: {title: "lala", message: "tutu"}}});
      spyOn(controller.alerts, "push").and.callFake((message) => {
        expect(message.type).toBe("warning");
        expect(message.title).toBe("lala");
        expect(message.msg).toBe("tutu");
        controller.alerts[0] = message;
        controller.closeAlert(0);
        done();
      });

      // Launch message action
      launchMessageAction("message", "message", {parameters:{view: "base", type: "warning", target: "testMessage"}});
    });

    // Launch target-message action
    it('should launch a target-message action', function(done) {
      let finished = false;
      spyOn($.fn, "popover").and.callFake(function() {return this;});
      spyOn(scope, "$on").and.callFake((event, func) => {
        if (!finished) {
          finished = true;
          expect(controller.popover).toBeDefined();
          controller.startPopover(controller.popover);
          expect(controller.popover.visible).toBe(true);
          controller.popover.background.trigger("click");
          expect(controller.popover.visible).toBe(false);
          controller.destroyPopover(controller.popover);
          expect(controller.popover).toBe(null);
          done();
        }
      });
      return launchMessageAction("target-message", "targetMessage", {parameters:{type: "error", title:"tutu", message: "lala"}});
    });

    // Launch target-message action with address
    it('should launch a target-message action with a component address', function(done) {
      let finished = false;
      spyOn($.fn, "popover").and.callFake(function() {return this;});
      spyOn(scope, "$on").and.callFake((event, func) => {
        if (!finished) {
          finished = true;
          expect(controller.popover).toBeDefined();
          controller.startPopover(controller.popover);
          expect(controller.popover.visible).toBe(true);
          controller.popover.background.trigger("click");
          expect(controller.popover.visible).toBe(false);
          controller.destroyPopover(controller.popover);
          expect(controller.popover).toBe(null);
          done();
        }
      });
      return launchMessageAction("target-message", "targetMessage", {address: {view:"base", component: "tutu"}, parameters:{type: "ok", title:"tutu", message: "lala"}});
    });

    // Launch target-message action with address
    it('should launch a target-message action with a grid cell address', function(done) {
      let finished = false;
      spyOn($.fn, "popover").and.callFake(function() {return this;});
      spyOn(scope, "$on").and.callFake((event, func) => {
        if (!finished) {
          finished = true;
          expect(controller.popover).toBeDefined();
          controller.startPopover(controller.popover);
          expect(controller.popover.visible).toBe(true);
          controller.popover.background.trigger("click");
          expect(controller.popover.visible).toBe(false);
          controller.destroyPopover(controller.popover);
          expect(controller.popover).toBe(null);
          done();
        }
      });
      return launchMessageAction("target-message", "targetMessage", {address: {view:"base", component: "tutu", row: "1", column: "lala"}, parameters:{type: "warning", title:"tutu", message: "lala"}});
    });

    // Launch target-message twice
    it('should launch a target-message action with a previous message defined', function(done) {
      let finished = false;
      spyOn($.fn, "popover").and.callFake(function() {return this;});
      controller.popover = {target: null};
      spyOn($settings, "get").and.returnValue({"error": 1000});
      spyOn(scope, "$on").and.callFake((event, func) => {
        if (!finished) {
          finished = true;
          expect(controller.popover).toBeDefined();
          controller.startPopover(controller.popover);
          expect(controller.popover.visible).toBe(true);
          controller.popover.background.trigger("click");
          expect(controller.popover.visible).toBe(false);
          controller.destroyPopover(controller.popover);
          expect(controller.popover).toBe(null);
          done();
        }
      });
      return launchMessageAction("target-message", "targetMessage", {parameters:{type: "error", title:"tutu", message: "lala"}});
    });

    // Launch confirm action
    it('should launch a confirm action', function() {
      let action = launchMessageAction("confirm", "confirm", {parameters:{type: "error", title:"tutu", message: "lala"}});
      expect(scope.confirmTitle).toBe("tutu");
      expect(scope.confirmMessage).toBe("lala");
      expect(scope.confirmAction).toBe(action);
    });

    // Launch confirm action
    it('should launch a confirm action without message', function() {
      let action = launchMessageAction("confirm", "confirm", {parameters:{type: "error"}});
      expect(scope.confirmTitle).not.toBeDefined();
      expect(scope.confirmMessage).not.toBeDefined();
      expect(scope.confirmAction).toBe(action);
    });
  });
});