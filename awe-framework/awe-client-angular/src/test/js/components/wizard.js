import { DefaultSettings } from "./../../../main/resources/js/awe/data/options";
import { launchScreenAction } from "../services/screen";

describe('awe-framework/awe-client-angular/src/test/js/components/wizard.js', function() {
  let $injector, $rootScope, $compile, $httpBackend, $actionController, $storage, $control, $utilities;
  let model = {page:1, records:3, selected: "3", total:1, values:[{label: "Step 1", value: "1"}, {label: "Step 2", value: "2"}, {label: "Step 3", value: "3"}]};
  let emptyModel = {page:1, records:0, selected: null, total:0, values:[]};
  let controller = {numberFormat: "{min: 0}", checkInitial: true, checkTarget:false, checked:false, component:"wizard", contextMenu:[], dependencies:[], icon:"search", id:"wizardId", loadAll:false, optional:false, placeholder:"SCREEN_TEXT_USER", printable:true, readonly:false, required:true, size:"lg", strict:true, style:"no-label", validation:"required", visible:true};

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$injector", "$rootScope", "$compile", "$httpBackend", "ActionController", "Storage", "Control", "AweUtilities",
      function(__$injector__, _$rootScope_, _$compile_, _$httpBackend_, __$actionController__, __$storage__, __$control__, __$utilities__){
      $injector = __$injector__;
      $rootScope = _$rootScope_;
      $compile = _$compile_;
      $httpBackend = _$httpBackend_;
      $actionController = __$actionController__;
      $storage = __$storage__;
      $control = __$control__;
      $utilities = __$utilities__;

      $rootScope.view = 'base';
      $rootScope.context = 'screen';

      // backend definition common for all tests
      $httpBackend.when('POST', 'settings').respond(DefaultSettings);
      spyOn($control, 'checkOnlyComponent').and.returnValue(true);
    }]);
  });

  it('replaces the element with the appropriate content', function() {
    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);
    // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
    $rootScope.$digest();

    expect(element.find(".wizard-wrapper").length).toBe(1);
    expect(element.find(".wizard-steps").length).toBe(1);
  });

  it('initializes a wizard', function(done) {
    $rootScope.firstLoad = true;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
      $rootScope.$digest();

      done();
    });
  });

  it('initializes a wizard with model defined', function(done) {
    $rootScope.firstLoad = true;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
      $rootScope.$digest();

      done();
    });
  });

  /**
   * Generate an action
   * @param done
   * @returns {Spy}
   */
  function generateAction(done) {
    let action = jasmine.createSpy('spy');
    action.attr = (what) => {
      switch (what) {
        case "id": return "wizardId";
        case "parameters": return {value: "2"};
        case "async":
        case "silent":
          return true;
      }
    };
    action.accept = done;
    return action;
  }

  /**
   * Spy controls
   */
  function spyControls() {
    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($control, "getAddressModel").and.returnValue(model);
    spyOn($control, "getAddressController").and.returnValue(controller);
    spyOn($utilities, "checkAddress").and.returnValue(true);
  }

  it('selects next panel', function(done) {
    $rootScope.firstLoad = true;
    model.selected = null;
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/next-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("2");
    });
  });

  it('selects prev panel', function(done) {
    $rootScope.firstLoad = true;
    model.selected = "3";
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/prev-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("2");
    });
  });

  it('selects first panel', function(done) {
    $rootScope.firstLoad = true;
    model.selected = "3";
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/first-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("1");
    });
  });

  it('selects last panel', function(done) {
    $rootScope.firstLoad = true;
    model.selected = null;
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/last-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("3");
    });
  });

  it('selects nth panel', function(done) {
    $rootScope.firstLoad = true;
    model.selected = "3";
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/nth-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("2");
    });
  });

  it('selects next panel without values', function(done) {
    $rootScope.firstLoad = true;
    model.selected = null;
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: emptyModel}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/next-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("2");
    });
  });

  it('selects prev panel without values', function(done) {
    $rootScope.firstLoad = true;
    model.selected = null;
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: emptyModel}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/prev-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("1");
    });
  });

  it('selects first panel without values', function(done) {
    $rootScope.firstLoad = true;
    model.selected = null;
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: emptyModel}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/first-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("1");
    });
  });

  it('selects last panel without values', function(done) {
    $rootScope.firstLoad = true;
    model.selected = null;
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: emptyModel}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/last-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("3");
    });
  });

  it('selects nth panel without values', function(done) {
    $rootScope.firstLoad = true;
    model.selected = null;
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: emptyModel}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("/action/nth-step", action);
      $rootScope.$digest();

      expect(model.selected).toEqual("2");
    });
  });

  it('resize component', function(done) {
    $rootScope.firstLoad = true;
    let action = generateAction(done);

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "wizardId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("resize");
      $rootScope.$digest();
      $rootScope.$broadcast("resize");
      $rootScope.$digest();
      $rootScope.$broadcast("/action/next-step", action);
    });
  });

  it('get print data', function() {
    $rootScope.firstLoad = true;
    model.selected = "1";

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope.$new());
    $rootScope.$digest();

    let $scope = element.isolateScope() || element.scope();
    let data = $scope.component.getPrintData();
    expect(data).toEqual({ wizardId: '1', "wizardId.data": { text: 'Step 1', all: [{ label: 'Step 1', value: '1' }, { label: 'Step 2', value: '2' }, { label: 'Step 3', value: '3' }]}});

    // Check data if printable is false
    let printable = controller.printable;
    controller.printable = false
    data = $scope.component.getPrintData();
    expect(data).toEqual({ wizardId: '1'});
    controller.printable = printable;
  });

  it('check if panel is active', function() {
    $rootScope.firstLoad = true;
    model.selected = "3";

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope.$new());
    $rootScope.$digest();

    let $ctrl = element.controller("aweInputWizard");
    expect($ctrl.isActive("1")).toBe(false);
    expect($ctrl.isActive("3")).toBe(true);
  });

  it('click on tab', function() {
    $rootScope.firstLoad = true;
    model.selected = "1";
    model.selectedIndex = 0;

    // Spies
    spyControls();

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-wizard input-wizard-id='wizardId'></awe-input-wizard>")($rootScope.$new());
    $rootScope.$digest();

    let $scope = element.isolateScope() || element.scope();
    $scope.clickTab("3");
    expect(model.selected).toEqual("1");
    expect(model.selectedIndex).toEqual(0);
    model.selected = "3";
    model.selectedIndex = 2;
    $scope.clickTab("1");
    expect(model.selected).toEqual("1");
    expect(model.selectedIndex).toEqual(0);
  });
});