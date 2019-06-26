import { DefaultSettings } from "./../../../main/resources/js/awe/data/options";
import { launchScreenAction } from "../services/screen";

describe('awe-framework/awe-client-angular/src/test/js/components/tab.js', function() {
  let $injector, $rootScope, $compile, $httpBackend, $actionController, $storage, $control, $utilities;
  let model = {page:1, records:3, selected: "3", total:1, values:[{label: "Step 1", value: "1"}, {label: "Step 2", value: "2"}, {label: "Step 3", value: "3"}]};
  let emptyModel = {page:1, records:0, selected: null, total:0, values:[]};
  let controller = {numberFormat: "{min: 0}", checkInitial: true, checkTarget:false, checked:false, component:"tab", contextMenu:[], dependencies:[], icon:"search", id:"tabId", loadAll:false, optional:false, placeholder:"SCREEN_TEXT_USER", printable:true, readonly:false, required:true, size:"lg", strict:true, style:"no-label", validation:"required", visible:true};

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
    }]);
  });

  it('replaces the element with the appropriate content', function() {
    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-tab input-tab-id='tabId'></awe-input-tab>")($rootScope);
    // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
    $rootScope.$digest();

    expect(element.find(".nav-tabs").length).toBe(1);
    expect(element.find(".tab-content").length).toBe(1);
  });

  it('initializes a tab', function(done) {
    $rootScope.firstLoad = true;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-tab input-tab-id='tabId'></awe-input-tab>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "tabId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
      $rootScope.$digest();

      done();
    });
  });

  it('initializes a tab with model defined', function(done) {
    $rootScope.firstLoad = true;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-tab input-tab-id='tabId'></awe-input-tab>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "tabId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
      $rootScope.$digest();

      done();
    });
  });

  it('resize component', function(done) {
    $rootScope.firstLoad = true;
    controller.maximize = true;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($control, "getAddressModel").and.returnValue(model);
    spyOn($control, "getAddressController").and.returnValue(controller);
    spyOn($utilities, "checkAddress").and.returnValue(true);
    spyOn($utilities, "timeout").and.callFake((fn) => fn());

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-tab input-tab-id='tabId'></awe-input-tab>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "tabId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      $rootScope.$digest();
      $rootScope.$broadcast("resize");
      $rootScope.$digest();
      $rootScope.$broadcast("resize");
      $rootScope.$digest();
      done()
    });
  });

  it('get print data', function() {
    $rootScope.firstLoad = true;
    model.selected = "1";

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($control, "getAddressModel").and.returnValue(model);
    spyOn($control, "getAddressController").and.returnValue(controller);
    spyOn($utilities, "checkAddress").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-tab input-tab-id='tabId'></awe-input-tab>")($rootScope.$new());
    $rootScope.$digest();

    let $scope = element.isolateScope() || element.scope();
    controller.printable = true;
    let data = $scope.component.getPrintData();
    /*for (let i=0, t=data["tabId.data"].all.length ; i<t; i++) {
      delete data["tabId.data"].all[i].$$hashKey;
    }*/
    expect(data).toEqual({ tabId: '1', "tabId.data": { text: 'Step 1', all: [{ label: 'Step 1', value: '1' }, { label: 'Step 2', value: '2' }, { label: 'Step 3', value: '3' }]}});

    // Check data if printable is false
    let printable = controller.printable;
    controller.printable = false;
    data = $scope.component.getPrintData();
    expect(data).toEqual({ tabId: '1'});
    controller.printable = printable;
  });

  it('check if panel is active', function() {
    $rootScope.firstLoad = true;
    model.selected = "3";

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($control, "getAddressModel").and.returnValue(model);
    spyOn($control, "getAddressController").and.returnValue(controller);
    spyOn($utilities, "checkAddress").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-tab input-tab-id='tabId'></awe-input-tab>")($rootScope.$new());
    $rootScope.$digest();

    let $ctrl = element.controller("aweInputTab");
    expect($ctrl.isActive("1")).toBe(false);
    expect($ctrl.isActive("3")).toBe(true);
  });

  it('click on tab', function() {
    $rootScope.firstLoad = true;
    model.selected = "1";
    model.selectedIndex = 0;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($control, "getAddressModel").and.returnValue(model);
    spyOn($control, "getAddressController").and.returnValue(controller);
    spyOn($utilities, "checkAddress").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-tab input-tab-id='tabId'></awe-input-tab>")($rootScope.$new());
    $rootScope.$digest();

    let $scope = element.isolateScope() || element.scope();
    $scope.isDisabled = () => false;
    $scope.clickTab("3");
    expect(model.selected).toEqual("3");
    $scope.clickTab("1");
    expect(model.selected).toEqual("1");
    $scope.isDisabled = () => true;
    $scope.clickTab("2");
    expect(model.selected).toEqual("1");
  });

  it('select tab', function() {
    $rootScope.firstLoad = true;
    model.selected = "1";
    model.selectedIndex = 0;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($control, "getAddressModel").and.returnValue(model);
    spyOn($control, "getAddressController").and.returnValue(controller);
    spyOn($utilities, "checkAddress").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-input-tab input-tab-id='tabId'></awe-input-tab>")($rootScope.$new());
    $rootScope.$digest();

    let $ctrl = element.controller("aweInputTab");
    let $scope = element.isolateScope() || element.scope();
    $scope.isDisabled = () => false;
    $ctrl.selectTab("3");
    expect(model.selected).toEqual("3");
    $ctrl.selectTab("1");
    expect(model.selected).toEqual("1");
    $scope.isDisabled = () => true;
    $ctrl.selectTab("2");
    expect(model.selected).toEqual("2");
  });
});