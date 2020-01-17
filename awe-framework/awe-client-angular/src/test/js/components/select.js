import { DefaultSettings } from "./../../../main/resources/js/awe/data/options";
import { launchScreenAction } from "../utils";

describe('Select component', function() {
  var $injector, $rootScope, $compile, $httpBackend, $actionController, $screen, $control, $storage;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$injector", "$rootScope", "$compile", "$httpBackend", "ActionController", "Screen", "Control", "Storage",
      function(__$injector__, _$rootScope_, _$compile_, _$httpBackend_, _ActionController_, _Screen_, _Control_, __Storage__){
      $injector = __$injector__;
      $rootScope = _$rootScope_;
      $compile = _$compile_;
      $httpBackend = _$httpBackend_;
      $actionController = _ActionController_;
      $screen = _Screen_;
      $control = _Control_;
      $storage = __Storage__;

      $rootScope.view = 'base';
      $rootScope.context = 'screen';

      // backend definition common for all tests
      $httpBackend.when('POST', './settings').respond(DefaultSettings);

      // Spy on storage
      spyOn($storage, "get").and.returnValue({'base': {}});
    }]);
  });

  it('replaces the element with the appropriate content', function() {
    // Compile a piece of HTML containing the directive
    var element = $compile("<awe-input-select input-select-id='RefreshTime'></awe-input-select>")($rootScope);
    // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
    $rootScope.$digest();

    expect(element.find("awe-context-menu").length).toBe(1);
    expect(element.find(".criterion.ng-hide").attr("criterion-id")).toBe("RefreshTime");
    expect(element.find(".validator.input > span").length).toBe(0);
    expect(element.find(".validator.input > select[id='RefreshTime']").length).toBe(1);
  });

  it('initializes a select', function(done) {
    $rootScope.firstLoad = true;

    // Compile a piece of HTML containing the directive
    var element = $compile("<awe-input-select input-select-id='RefreshTime'></awe-input-select>")($rootScope);
    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
          id: "RefreshTime",
          controller: {numberFormat: "{min: 0}", checkInitial: true, checkTarget:false, checked:false, component:"select", contextMenu:[], dependencies:[], icon:"search", id:"RefreshTime", loadAll:false, optional:false, placeholder:"SCREEN_TEXT_USER", printable:true, readonly:false, required:true, size:"lg", strict:true, style:"no-label", validation:"required", visible:true},
          model: {defaultValues:[], page:1, records:0, selected:[], total:0, values:[]}
        }
      ], screen: {name: "TEST"}, messages: []}}}, () => {
      $actionController.closeAllActions();

      // fire all the watches
      $rootScope.$digest();

      expect(element.find("awe-context-menu").length).toBe(1);
      expect(element.find(".criterion.no-label").attr("criterion-id")).toBe("RefreshTime");
      expect(element.find(".form-group.group-lg.w-icon").length).toBe(1);
      expect(element.find(".validator.input > span.criterion-icon-lg.form-icon.fa.fa-search").length).toBe(1);
      expect(element.find(".validator.input > select[id='RefreshTime']").length).toBe(1);
      done();
    });
  });

  it('initializes a suggest', function(done) {
    $rootScope.firstLoad = true;

    // Compile a piece of HTML containing the directive
    var element = $compile("<awe-input-suggest input-suggest-id='RefreshTime'></awe-input-suggest>")($rootScope);
    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
          id: "RefreshTime",
          controller: {numberFormat: "{min: 0}", checkInitial: true, checkTarget:false, checked:false, component:"select", contextMenu:[], dependencies:[], icon:"search", id:"RefreshTime", loadAll:false, optional:false, placeholder:"SCREEN_TEXT_USER", printable:true, readonly:false, required:true, size:"lg", strict:true, style:"no-label", validation:"required", visible:true},
          model: {defaultValues:[], page:1, records:0, selected:[], total:0, values:[]}
        }
      ], screen: {name: "TEST"}, messages: []}}}, () => {
      $actionController.closeAllActions();

      // fire all the watches
      $rootScope.$digest();

      expect(element.find("awe-context-menu").length).toBe(1);
      expect(element.find(".criterion.no-label").attr("criterion-id")).toBe("RefreshTime");
      expect(element.find(".form-group.group-lg.w-icon").length).toBe(1);
      expect(element.find(".validator.input > span.criterion-icon-lg.form-icon.fa.fa-search").length).toBe(1);
      expect(element.find(".validator.input > select[id='RefreshTime'][on-refresh]").length).toBe(1);
      done();
    });
  });

  it('initializes a select multiple', function(done) {
    $rootScope.firstLoad = true;

    // Compile a piece of HTML containing the directive
    var element = $compile("<awe-input-select-multiple input-select-multiple-id='RefreshTime'></awe-input-select-multiple>")($rootScope);
    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
          id: "RefreshTime",
          controller: {numberFormat: "{min: 0}", checkInitial: true, checkTarget:false, checked:false, component:"select", contextMenu:[], dependencies:[], icon:"search", id:"RefreshTime", loadAll:false, optional:false, placeholder:"SCREEN_TEXT_USER", printable:true, readonly:false, required:true, size:"lg", strict:true, style:"no-label", validation:"required", visible:true},
          model: {defaultValues:[], page:1, records:0, selected:[], total:0, values:[]}
        }
      ], screen: {name: "TEST"}, messages: []}}}, () => {
      $actionController.closeAllActions();

      // fire all the watches
      $rootScope.$digest();

      expect(element.find("awe-context-menu").length).toBe(1);
      expect(element.find(".criterion.no-label").attr("criterion-id")).toBe("RefreshTime");
      expect(element.find(".form-group.group-lg.w-icon").length).toBe(1);
      expect(element.find(".validator.input > span.criterion-icon-lg.form-icon.fa.fa-search").length).toBe(1);
      expect(element.find(".validator.input > select[id='RefreshTime'][multiple]").length).toBe(1);
      done();
    });
  });

  it('initializes a suggest multiple', function(done) {
    $rootScope.firstLoad = true;

    // Compile a piece of HTML containing the directive
    var element = $compile("<awe-input-suggest-multiple input-suggest-multiple-id='RefreshTime'></awe-input-suggest-multiple>")($rootScope);
    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
          id: "RefreshTime",
          controller: {numberFormat: "{min: 0}", checkInitial: true, checkTarget:false, checked:false, component:"select", contextMenu:[], dependencies:[], icon:"search", id:"RefreshTime", loadAll:false, optional:false, placeholder:"SCREEN_TEXT_USER", printable:true, readonly:false, required:true, size:"lg", strict:true, style:"no-label", validation:"required", visible:true},
          model: {defaultValues:[], page:1, records:0, selected:[], total:0, values:[]}
        }
      ], screen: {name: "TEST"}, messages: []}}}, () => {
      $actionController.closeAllActions();

      // fire all the watches
      $rootScope.$digest();

      expect(element.find("awe-context-menu").length).toBe(1);
      expect(element.find(".criterion.no-label").attr("criterion-id")).toBe("RefreshTime");
      expect(element.find(".form-group.group-lg.w-icon").length).toBe(1);
      expect(element.find(".validator.input > span.criterion-icon-lg.form-icon.fa.fa-search").length).toBe(1);
      expect(element.find(".validator.input > select[id='RefreshTime'][multiple][on-refresh]").length).toBe(1);
      done();
    });
  });
});