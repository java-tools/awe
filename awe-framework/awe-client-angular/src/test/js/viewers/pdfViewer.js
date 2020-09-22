import {DefaultSettings} from "../../../main/resources/js/awe/data/options";
import {launchScreenAction} from "../utils";
import PDFObject from "pdfobject";

describe('awe-framework/awe-client-angular/src/test/js/viewers/pdfViewer.js', function () {
  let $injector, $rootScope, $compile, $httpBackend, $actionController, $storage, $control, $utilities, $serverData, $connection, $window;
  let model = {page: 1, records: 0, selected: null, total: 0, values: []};
  let controller = {targetAction: "test", id: "viewerId"};

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$injector", "$rootScope", "$compile", "$httpBackend", "ActionController", "Storage", "Control", "AweUtilities", "ServerData", "Connection", "$window",
      function (__$injector__, _$rootScope_, _$compile_, _$httpBackend_, __$actionController__, __$storage__, __$control__, __$utilities__, __serverData__, __connection__, __window__) {
        $injector = __$injector__;
        $rootScope = _$rootScope_;
        $compile = _$compile_;
        $httpBackend = _$httpBackend_;
        $actionController = __$actionController__;
        $storage = __$storage__;
        $control = __$control__;
        $utilities = __$utilities__;
        $serverData = __serverData__;
        $connection = __connection__;
        $window = __window__;

        $rootScope.view = 'base';
        $rootScope.context = 'screen';

        // backend definition common for all tests
        $httpBackend.when('POST', 'settings').respond(200, DefaultSettings);
        $httpBackend.when('POST', 'http://server/file/stream/maintain/test').respond(200, "");

        spyOn($control, 'checkOnlyComponent').and.returnValue(true);
      }]);
  });

  it('replaces the element with the appropriate content', function() {
    // Compile a piece of HTML containing the directive
    let element = $compile("<awe-pdf-viewer pdf-viewer-id='viewerId'></awe-pdf-viewer>")($rootScope);
    // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
    $rootScope.$digest();

    expect(element.hasClass("pdf-viewer")).toBe(true);
  });

  it('initializes a pdf-viewer', function(done) {
    $rootScope.firstLoad = true;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);

    // Compile a piece of HTML containing the directive
    $compile("<awe-pdf-viewer pdf-viewer-id='viewerId'></awe-pdf-viewer>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
            id: "viewerId", controller: controller, model: model}], screen: {name: "TEST"}, messages: []}}}, () => {
      // Close all actions
      $actionController.closeAllActions();

      // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
      $rootScope.$digest();

      done();
    });
  });

  it('reloads pdf-viewer', function(done) {
    $rootScope.firstLoad = true;

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($serverData, "getFormValues").and.returnValue({});
    spyOn($utilities, "timeout").and.callFake(fn => setTimeout(fn, 0));
    spyOn($control, "getAddressController").and.returnValue({autoload: true, targetAction: "test"});
    spyOn($connection, "getFile").and.returnValue({then: (fn) => fn({data: ""})});
    $window.URL = {createObjectURL: () => null};
    spyOn(PDFObject, "embed").and.callFake(() => null);

    // Compile a piece of HTML containing the directive
    $compile("<awe-pdf-viewer pdf-viewer-id='viewerId'></awe-pdf-viewer>")($rootScope);

    launchScreenAction($injector, "screen-data", "screenData", {
      parameters: {
        view: "base", screenData: {
          actions: [{type: "reload"}], components: [{
            id: "viewerId", controller: {...controller, autoload: true}, model: model
          }], screen: {name: "TEST"}, messages: []
        }
      }
    }, () => {
      // Close all actions
      $actionController.closeAllActions();

      // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
      $rootScope.$digest();

      // Fire a digest after setTimeout
      setTimeout(() => {
        $rootScope.$digest();
        done();
      }, 100);
    });
  });
});