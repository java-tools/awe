import { DefaultSettings } from "./../../../main/resources/js/awe/data/options";
import { launchScreenAction } from "../utils";

describe('Numeric component', function() {
  var $injector, $rootScope, $compile, $httpBackend, $actionController, $control, $storage;

  // Mock module
  beforeEach(function() {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$injector", "$rootScope", "$compile", "$httpBackend", "ActionController", "Control", "Storage",
      function(__$injector__, _$rootScope_, _$compile_, _$httpBackend_, _ActionController_, _Control_, __Storage__){
      $injector = __$injector__;
      $rootScope = _$rootScope_;
      $compile = _$compile_;
      $httpBackend = _$httpBackend_;
      $actionController = _ActionController_;
      $control = _Control_;
      $storage = __Storage__;

      $rootScope.view = 'base';
      $rootScope.context = 'screen';

      // backend definition common for all tests
      $httpBackend.when('POST', 'settings').respond(DefaultSettings);

      // Spy on storage
      spyOn($storage, "get").and.returnValue({'base': {}});
    }]);
  });

  it('replaces the element with the appropriate content', function() {
    // Compile a piece of HTML containing the directive
    var element = $compile("<awe-input-numeric input-numeric-id='RefreshTime'></awe-input-numeric>")($rootScope);

    // fire all the watches, so the scope expression {{1 + 1}} will be evaluated
    $rootScope.$digest();

    expect(element.find("awe-context-menu").length).toBe(1);
    expect(element.find(".criterion.ng-hide").attr("criterion-id")).toBe("RefreshTime");
    /*expect(element.find(".validator.input > span").length).toBe(0);
    expect(element.find(".validator.input > input[name='RefreshTime'].form-control.text-right").length).toBe(1);
    expect(element.find("[ui-slider]").length).toBe(0);*/
  });

  it('initializes a component', function() {
    $rootScope.firstLoad = true;

    spyOn($control, "getAddressModel").and.returnValue({defaultValues:[], page:1, records:0, selected:[], total:0, values:[]});
    spyOn($control, "getAddressController").and.returnValue({numberFormat: "{min: 0}", checkInitial: true, checkTarget:false, checked:false, component:"numeric", contextMenu:[], dependencies:[], icon:"search", id:"RefreshTime", loadAll:false, optional:false, placeholder:"SCREEN_TEXT_USER", printable:true, readonly:false, required:true, size:"lg", strict:true, style:"no-label", validation:"required", visible:true});

    // Compile a piece of HTML containing the directive
    var element = $compile("<awe-input-numeric input-numeric-id='RefreshTime'></awe-input-numeric>")($rootScope);

    // fire all the watches
    $rootScope.$digest();

    //expect(element.find("awe-context-menu").length).toBe(1);
    expect(element.find(".criterion.no-label").attr("criterion-id")).toBe("RefreshTime");
    /*expect(element.find(".form-group.group-lg.w-icon").length).toBe(1);
    expect(element.find(".validator.input > span.criterion-icon-lg.form-icon.fa.fa-search").length).toBe(1);
    expect(element.find(".validator.input > input[name='RefreshTime'].form-control.text-right.input-lg").length).toBe(1);
    expect(element.find("[ui-slider]").length).toBe(0);*/
  });

  it('initializes a component with a slider', function(done) {
    $rootScope.firstLoad = true;

    // Compile a piece of HTML containing the directive
    var element = $compile("<awe-input-numeric input-numeric-id='NumSlidReq'></awe-input-numeric>")($rootScope);
    launchScreenAction($injector, "screen-data", "screenData", {parameters:{view: "base", screenData:{actions: [{type: "reload"}], components: [{
          id: "NumSlidReq",
          controller: {size: "sm", checkInitial: true, checkTarget: false, checked: false, component: "numeric", contextMenu:[], dependencies:[], id: "NumSlidReq", label: "PARAMETER_NUMERIC", loadAll: false, numberFormat: "{min: -1000, max: 1000, step: 10, precision: 2, aSign:' $', pSign:'s', aPad:true, ticks: [-1000, -500, 0, 500, 1000], ticks_labels: ['-$1000', '-$500', '$0', '$500', '$1000']}", optional: false, printable: true, readonly: false, required: true, showSlider: true, strict: true, style: "col-xs-12 col-sm-6 col-lg-4", validation: "required", visible: true},
          validationRules: {required: true},
          model: {defaultValues:[], page:1, records:0, selected:[], total:0, values:[]}
        }
      ], screen: {name: "TEST"}, messages: []}}}, () => {
      $actionController.closeAllActions();

      // fire all the watches
      $rootScope.$digest();
      console.info(element[0].innerHTML);
      expect(element.find("awe-context-menu").length).toBe(1);
      expect(element.find(".criterion.col-xs-12.col-sm-6.col-lg-4").attr("criterion-id")).toBe("NumSlidReq");
      expect(element.find(".form-group.group-sm > label[for='NumSlidReq']").length).toBe(1);
      expect(element.find(".validator.input > span.form-icon").length).toBe(0);
      expect(element.find(".validator.input > input[name='NumSlidReq'].form-control.text-right.input-sm").length).toBe(1);
      expect(element.find(".slider.slider-horizontal").length).toBe(1);

      // Change attribute
      $control.changeAttribute({view: "base", component: "NumSlidReq"}, {readonly: true});

      // fire all the watches
      $rootScope.$digest();

      done();
    });
  });
});