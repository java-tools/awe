import {DefaultSettings} from "../../../main/resources/js/awe/data/options";

describe('awe-framework/awe-client-angular/src/test/js/controllers/form.js', function() {
  let $scope, $compile, $utilities, $settings, $actionController, $control, $serverData, $validator, $httpBackend;
  let originalTimeout;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$rootScope", "$compile", "ServerData", "Validator", "AweUtilities", "AweSettings", "ActionController", "Control", "$httpBackend",
      function($rootScope, _$compile_, _ServerData_, _Validator_, _AweUtilities_, _AweSettings_, _ActionController_, _Control_, _$httpBackend_){
      $scope = $rootScope;
      $utilities = _AweUtilities_;
      $settings = _AweSettings_;
      $actionController = _ActionController_;
      $control = _Control_;
      $serverData = _ServerData_;
      $validator = _Validator_;
      $compile = _$compile_;
      $httpBackend = _$httpBackend_;
    }]);

    // backend definition common for all tests
    $httpBackend.when('POST', 'settings').respond(DefaultSettings);
    $httpBackend.when('POST', 'http://server/action/screen-data').respond({});

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function() {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  /**
   * Define form element for tests
   */
  function defineForm() {
    // Compile a piece of HTML containing the directive
    let element = $compile("<form awe-form=''></form>")($scope);

    // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
    $scope.$digest();

    // Retrieve element
    return element;
  }

  it('replaces the element with the appropriate content', function() {
    // Define form
    let element = defineForm();

    // Expect element is defined and directive is active
    expect(element[0].tagName).toBe("FORM");
  });

  // Once initialized, launch tests
  describe('once initialized', function() {

    /**
     * Launch a form action
     * @param {String} actionName Action name
     * @param {String} actionMethod Action method
     * @param {Object} parameters Parameters
     * @param {Function} done Launch when done
     */
    function launchFormAction(actionName, actionMethod, parameters, done = () => null) {
      // Launch action
      $actionController.closeAllActions();
      let action = $actionController.generateAction({type: actionName, ...parameters}, {}, true, true);

      // Spy screen action
      spyOn(action, "accept").and.callFake(done);
      spyOn(action, "launchAction").and.callThrough();

      // Call action
      action.run();

      // Flush timeout
      $utilities.timeout.flush();

      // Expect launch action to be called
      expect(action.launchAction).toHaveBeenCalled();
    }

    // Launch server-download action
    it('should launch a server-download action', function(done) {
      // Prepare
      defineForm();
      spyOn($serverData, "getFormValues");
      spyOn($serverData, "getFileData").and.returnValue({});
      spyOn($control, "getAddressApi").and.returnValue({getSpecificFields: done});

      // Run
      launchFormAction("server-download", "serverDownload", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{message:{message: "This field is required", id: "cod_usr", uid: "6a2bda7a-03dd-8106-d906-ecd029f5c6fa"}}});

      // Assert
      expect($serverData.getFormValues).toHaveBeenCalled();
      expect($serverData.getFileData).toHaveBeenCalled();
      expect($control.getAddressApi).toHaveBeenCalled();
    });

    // Launch server-download action
    it('should launch a server-download action without target', function() {
      // Prepare
      defineForm();
      spyOn($serverData, "getFormValues");
      spyOn($serverData, "getFileData").and.returnValue({});
      spyOn($utilities, "downloadFile");
      spyOn($control, "getAddressApi");

      // Run
      launchFormAction("server-download", "serverDownload", {parameters:{message:{message: "This field is required", id: "cod_usr", uid: "6a2bda7a-03dd-8106-d906-ecd029f5c6fa"}}});

      // Assert
      expect($serverData.getFormValues).toHaveBeenCalled();
      expect($serverData.getFileData).toHaveBeenCalled();
      expect($utilities.downloadFile).toHaveBeenCalled();
      expect($control.getAddressApi).not.toHaveBeenCalled();
    });

    // Launch server-download action
    it('should launch a server-download action without specific fields', function() {
      // Prepare
      defineForm();
      spyOn($serverData, "getFormValues");
      spyOn($serverData, "getFileData").and.returnValue({});
      spyOn($control, "getAddressApi").and.returnValue({});

      // Run
      launchFormAction("server-download", "serverDownload", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{message:{message: "This field is required", id: "cod_usr", uid: "6a2bda7a-03dd-8106-d906-ecd029f5c6fa"}}});

      // Assert
      expect($serverData.getFormValues).toHaveBeenCalled();
      expect($serverData.getFileData).toHaveBeenCalled();
      expect($control.getAddressApi).toHaveBeenCalled();
    });

    // Call update-controller action
    it('should call an update-controller action', function() {
      // Prepare
      defineForm();
      spyOn($control, "changeControllerAttribute").and.returnValue({});
      spyOn($actionController, "acceptAction").and.returnValue({});

      // Run
      launchFormAction("update-controller", "updateController", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{attribute: "lala", datalist: {rows:[{value: "tutu"}]}}});
      launchFormAction("update-controller", "updateController", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{attribute: "lele", value: "lolo"}});

      // Assert
      expect($control.changeControllerAttribute.calls.allArgs()).toEqual([
        [undefined, {"loading": true}],
        [{view: "base", component:"tutu"}, {"lala": "tutu"}],
        [{view: "base", component:"tutu"}, {"lele": "lolo"}]
      ]);
      expect($actionController.acceptAction.calls.count()).toBe(2);
    });

    // Call validate action
    it('should call a validate action', function() {
      // Prepare
      defineForm();
      spyOn($validator, "validateNode").and.returnValues([], ["error1", "error2"]);
      spyOn($actionController, "finishAction").and.returnValue({});

      // Run
      launchFormAction("validate", "validate", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{}});
      launchFormAction("validate", "validate", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{}});

      // Assert
      expect($validator.validateNode).toHaveBeenCalled();
      expect($actionController.finishAction).toHaveBeenCalledTimes(2);
    });

    // Call setValid action
    /*it('should call a set-valid action', function(done) {
      return callFormAction("set-valid", "setValid", {parameters:{}}, false, true, true, done);
    });

    // Call setInvalid action
    it('should call a set-invalid action', function(done) {
      return callFormAction("set-invalid", "setInvalid", {parameters:{}}, false, true, true, done);
    });

    // Call server action
    it('should call a server action', function(done) {
      return callFormAction("server", "server", {parameters:{}}, true, true, false, done);
    });

    // Call server-print action
    it('should call a server-print action', function(done) {
      return callFormAction("server-print", "serverPrint", {parameters:{}}, true, true, false, done);
    });

    // Call fill action
    it('should call a fill action', function(done) {
      return callFormAction("fill", "fill", {parameters:{}}, true, true, false, done);
    });

    // Call select action
    it('should call a select action', function(done) {
      return callFormAction("select", "select", {parameters:{}}, true, true, false, done);
    });

    // Call reset action
    it('should call a reset action', function(done) {
      return callFormAction("reset", "reset", {parameters:{}}, true, true, false, done);
    });

    // Call restore action
    it('should call a restore action', function(done) {
      return callFormAction("restore", "restore", {parameters:{}}, true, true, false, done);
    });

    // Call restore-target action
    it('should call a restore-target action', function(done) {
      return callFormAction("restore-target", "restoreTarget", {parameters:{}}, true, true, false, done);
    });

    // Call confirm-updated-data action
    it('should call a confirm-updated-data action', function(done) {
      return callFormAction("confirm-updated-data", "checkModelUpdated", {parameters:{}}, true, true, false, done);
    });

    // Call confirm-not-updated-data action
    it('should call a confirm-not-updated-data action', function(done) {
      return callFormAction("confirm-not-updated-data", "checkModelNoUpdated", {parameters:{}}, true, true, false, done);
    });

    // Call confirm-empty-data action
    it('should call a confirm-empty-data action', function(done) {
      return callFormAction("confirm-empty-data", "checkModelEmpty", {parameters:{}}, true, true, false, done);
    });

    // Call value action
    it('should call a value action', function(done) {
      return callFormAction("value", "value", {parameters:{}}, true, true, false, done);
    });

    // Call cancel action
    it('should call a cancel action', function(done) {
      return callFormAction("cancel", "cancel", {parameters:{}}, true, true, false, done);
    });

    // Call filter action
    it('should call a filter action', function(done) {
      return callFormAction("filter", "filter", {parameters:{}}, true, true, false, done);
    });

    // Call start-load action
    it('should call a start-load action', function(done) {
      return callFormAction("start-load", "startLoad", {parameters:{}}, true, true, false, done);
    });

    // Call end-load action
    it('should call a end-load action', function(done) {
      return callFormAction("end-load", "endLoad", {parameters:{}}, true, true, false, done);
    });*/

    /**
     * Launch a message action
     * @param {String} actionName Action name
     * @param {String} actionMethod Action method
     * @param {Object} parameters Parameters
     * @param {Function} done Launch when done
     */
    /*function launchFormAction(actionName, actionMethod, parameters, done = () => null) {
      // Spy screen action
      let acceptAction = $actionController.acceptAction.bind($actionController);
      spyOn($actionController, "acceptAction").and.callFake((action) => {
        acceptAction(action);
        done();
      });

      // Launch action
      $actionController.closeAllActions();
      let action = $actionController.generateAction({type: actionName, ...parameters}, {address: {view: "base"}}, true, true);
      controller.FormActions[actionMethod].call(this, action);
      return action;
    }*/

    // Launch message action
    /*it('should launch a message action', function(done) {
      spyOn(scope.alerts, "push").and.callFake((message) => {
        expect(message.type).toBe("success");
        expect(message.title).toBe("tutu");
        expect(message.msg).toBe("lala");
        done();
      });
      return launchFormAction("message", "message", {parameters:{type: "ok", title:"tutu", message: "lala"}});
    });

    // Launch message action
    it('should launch a message action without message', function(done) {
      spyOn(scope.alerts, "push").and.callFake((message) => {
        expect(message.type).toBe("danger");
        expect(message.title).not.toBeDefined();
        expect(message.msg).not.toBeDefined();
        done();
      });
      return launchFormAction("message", "message", {parameters:{type: "error"}});
    });

    // Launch message action with a target message
    it('should launch a message action with a target message', function(done) {
      spyOn(scope.alerts, "push").and.callFake((message) => {
        expect(message.type).toBe("warning");
        expect(message.title).toBe("lala");
        expect(message.msg).toBe("tutu");
        scope.alerts[0] = message;
        scope.closeAlert(0);
        done();
      });

      // Store screen messages
      $ngRedux.dispatch(updateMessages("base", {"testMessage": {id: "testMessage", title: "lala", message: "tutu"}}));

      // Launch message action
      launchFormAction("message", "message", {parameters:{view: "base", type: "warning", target: "testMessage"}});
    });

    // Launch target-message action
    it('should launch a target-message action', function(done) {
      let finished = false;
      spyOn($.fn, "popover").and.callFake(function() {return this});
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
      return launchFormAction("target-message", "targetMessage", {parameters:{type: "error", title:"tutu", message: "lala"}});
    });

    // Launch target-message action with address
    it('should launch a target-message action with a component address', function(done) {
      let finished = false;
      spyOn($.fn, "popover").and.callFake(function() {return this});
      spyOn($actionController, "isAlive").and.returnValue(true);
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
      return launchFormAction("target-message", "targetMessage", {address: {view:"base", component: "tutu"}, parameters:{type: "ok", title:"tutu", message: "lala"}});
    });

    // Launch target-message action with address
    it('should launch a target-message action with a grid cell address', function(done) {
      let finished = false;
      spyOn($.fn, "popover").and.callFake(function() {return this});
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
      return launchFormAction("target-message", "targetMessage", {address: {view:"base", component: "tutu", row: "1", column: "lala"}, parameters:{type: "warning", title:"tutu", message: "lala"}});
    });

    // Launch target-message twice
    it('should launch a target-message action with a previous message defined', function(done) {
      let finished = false;
      spyOn($.fn, "popover").and.callFake(function() {return this});
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
      return launchFormAction("target-message", "targetMessage", {parameters:{type: "error", title:"tutu", message: "lala"}});
    });

    // Launch confirm action
    it('should launch a confirm action', function() {
      let action = launchFormAction("confirm", "confirm", {parameters:{type: "error", title:"tutu", message: "lala"}});
      expect(scope.confirmTitle).toBe("tutu");
      expect(scope.confirmMessage).toBe("lala");
      expect(scope.confirmAction).toBe(action);
    });

    // Launch confirm action
    it('should launch a confirm action without message', function() {
      let action = launchFormAction("confirm", "confirm", {parameters:{type: "error"}});
      expect(scope.confirmTitle).not.toBeDefined();
      expect(scope.confirmMessage).not.toBeDefined();
      expect(scope.confirmAction).toBe(action);
    });*/

    // Launch validate
    /*it('should launch a validate action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, validationRules: {required: true}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("validate", "validate", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{values:["tutu"]}});
    });

    // Launch set-valid
    it('should launch a set-valid action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, validationRules: {required: true}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("set-valid", "setValid", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{values:["tutu"]}});
    });

    // Launch set-invalid
    it('should launch a set-invalid action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, validationRules: {required: true}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("set-invalid", "setInvalid", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{message:{message: "This field is required", id: "cod_usr", uid: "6a2bda7a-03dd-8106-d906-ecd029f5c6fa"}}});
    });

    // Launch server-print action
    it('should launch a server-print action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, validationRules: {required: true}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("server-print", "serverPrint", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{message:{message: "This field is required", id: "cod_usr", uid: "6a2bda7a-03dd-8106-d906-ecd029f5c6fa"}}});
    });

    // Launch server-download action
    it('should launch a server-download action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, validationRules: {required: true}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("server-download", "serverDownload", {target:"tutu", address:{view: "base", component:"tutu"}, parameters:{message:{message: "This field is required", id: "cod_usr", uid: "6a2bda7a-03dd-8106-d906-ecd029f5c6fa"}}});
    });

    // Launch fill
    it('should launch a fill action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("fill", "fill", {address:{view: "base", component:"tutu"}, parameters:{datalist:{records:1, total: 1, page: 1, rows:[{value: "en", label: "English"}]}}});
      expect($ngRedux.getState().components["tutu"].model.values[0].label).toBe("English");
    });

    // Launch update-controller
    it('should launch an update-controller action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("update-controller", "updateController", {address:{view: "base", component:"tutu"}, parameters:{attribute: "language", datalist:{records:1, total: 1, page: 1, rows:[{value: "en", label: "English"}]}}});
      expect($ngRedux.getState().components["tutu"].attributes.language).toBeDefined();
      expect($ngRedux.getState().components["tutu"].attributes.language).toBe("en");
    });

    // Launch select
    it('should launch a select action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("select", "select", {address:{view: "base", component:"tutu"}, parameters:{values:["tutu"]}});
      expect($ngRedux.getState().components["tutu"].model.values.length).toBe(1);
      expect($ngRedux.getState().components["tutu"].model.values[0].selected).toBe(true);
      expect($ngRedux.getState().components["tutu"].model.values[0].value).toBe("tutu");
    });

    // Launch reset
    it('should launch a reset action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, model: {defaultValues: [], values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("reset", "reset", {address:{view: "base", component:"tutu"}, parameters:{}});
      $control.resetModel({view: "base", component:"tutu"});
      expect($ngRedux.getState().components["tutu"].model.values.length).toBe(0);
    });

    // Launch restore
    it('should launch a restore action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, storedModel: { values: [{selected: false, value: "es", label: "Español"}]}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("restore", "restore", {address:{view: "base", component:"tutu"}, parameters:{}});
      $control.restoreModel({view: "base", component:"tutu"});
      expect($ngRedux.getState().components["tutu"].model.values[0].value).toBe("es");
    });

    // Launch restore-target
    it('should launch a restore-target action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, storedModel: { values: [{selected: false, value: "es", label: "Español"}]}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("restore-target", "restoreTarget", {address:{view: "base", component:"tutu"}, parameters:{}});
      $control.restoreModel({view: "base", component:"tutu"});
      expect($ngRedux.getState().components["tutu"].model.values[0].value).toBe("es");
    });

    // Launch logout
    it('should launch a logout action', function() {
      launchFormAction("logout", "logout", {address:{view: "base", component:"tutu"}, parameters:{}});
    });

    // Launch a confirm-updated-data action
    it('should launch a confirm-updated-data action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, storedModel: { values: [{selected: false, value: "es", label: "Español"}]}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("confirm-updated-data", "checkModelUpdated", {address:{view: "base", component:"tutu"}, parameters:{}});
    });

    // Launch a confirm-updated-data action with updated model
    it('should launch a confirm-updated-data action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, storedModel: { values: [{selected: false, value: "es", label: "Español"}]}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      $control.changeModel({component: "tutu", view: "base"}, {model: {values: [{selected: true, value: "es", label: "Español"}]}});
      launchFormAction("confirm-updated-data", "checkModelUpdated", {address:{view: "base", component:"tutu"}, parameters:{}});
    });

    // Launch a confirm-not-updated-data action
    it('should launch a confirm-not-updated-data action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, storedModel: { values: [{selected: false, value: "es", label: "Español"}]}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("confirm-not-updated-data", "checkModelNoUpdated", {address:{view: "base", component:"tutu"}, parameters:{}});
    });

    // Launch a confirm-not-updated-data action with updated model
    it('should launch a confirm-not-updated-data action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, storedModel: { values: [{selected: false, value: "es", label: "Español"}]}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      $control.changeModel({component: "tutu", view: "base"}, {model: {values: [{selected: true, value: "es", label: "Español"}]}});
      launchFormAction("confirm-not-updated-data", "checkModelNoUpdated", {address:{view: "base", component:"tutu"}, parameters:{}});
    });

    // Launch a confirm-empty-data action
    it('should launch a confirm-empty-data action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, storedModel: { values: [{selected: false, value: "es", label: "Español"}]}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("confirm-empty-data", "checkModelEmpty", {address:{view: "base", component:"tutu"}, parameters:{}});
    });

    // Launch a confirm-empty-data action with empty model
    it('should launch a confirm-empty-data action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, model: {values: []}});
      launchFormAction("confirm-empty-data", "checkModelEmpty", {address:{view: "base", component:"tutu"}, parameters:{}});
    });

    // Launch value
    it('should launch a value action', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("value", "value", {value: "tutu", address:{view: "base", component:"tutu"}, parameters:{}});
      expect($ngRedux.getState().components["tutu"].model.values.length).toBe(1);
      expect($ngRedux.getState().components["tutu"].model.values[0].selected).toBe(true);
      expect($ngRedux.getState().components["tutu"].model.values[0].value).toBe("tutu");
    });

    // Launch cancel stack
    it('should cancel the stack', function() {
      launchFormAction("cancel", "cancel", {parameters:{}});
    });

    // Launch filter
    it('should filter a component', function() {
      $control.changeComponent({component: "tutu", view: "base"}, {address:{view: "base", component:"tutu"}, attributes:{loading: false}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("filter", "filter", {address:{view: "base", component:"tutu"}, parameters:{}});
    });

    // Launch start-load
    it('should start load', function(data) {
      $control.changeComponent({component: "tutu", view: "base"}, {attributes:{loading: false}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("start-load", "startLoad", {address:{view: "base", component:"tutu"}, parameters:{}}, data);
      $control.flushDebouncedAttributes();
      expect($ngRedux.getState().components.tutu.attributes.loading).toBe(true);
    });

    // Launch end-load
    it('should end load', function(data) {
      $control.changeComponent({component: "tutu", view: "base"}, {attributes:{loading: true}, model: {values: [{selected: true, value: "fr", label: "Français"}]}});
      launchFormAction("end-load", "endLoad", {address:{view: "base", component:"tutu"}, parameters:{}}, data);
      $control.flushDebouncedAttributes();
      expect($ngRedux.getState().components.tutu.attributes.loading).toBe(false);
    });*/
  });
});