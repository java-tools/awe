import {DefaultSettings} from "../../../main/resources/js/awe/data/options";

describe('awe-framework/awe-client-angular/src/test/js/plugins/uiSelect.js', function () {
  let $rootScope, $compile, $httpBackend, $storage, $control, $utilities, $translate;
  let options = "{}";
  let controller = {
    numberFormat: "{min: 0}",
    checkInitial: true,
    checkTarget: false,
    checked: false,
    component: "wizard",
    contextMenu: [],
    dependencies: [],
    icon: "search",
    id: "wizardId",
    loadAll: false,
    optional: false,
    placeholder: "SCREEN_TEXT_USER",
    printable: true,
    readonly: false,
    required: true,
    size: "lg",
    strict: true,
    style: "no-label",
    validation: "required",
    visible: true
  };
  let model = {page: 1, records: 3, selected: "3", total: 1, values: [{label: "Step 1", value: "1"}, {label: "Step 2", value: "2"}, {label: "Step 3", value: "3"}]};
  let component = {controller: controller, model: model, api: {}, onPluginInit: () => null};

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    // Inject controller
    inject(["$rootScope", "$compile", "$httpBackend", "$translate", "Storage", "Control", "AweUtilities",
      function (_$rootScope_, _$compile_, _$httpBackend_, _$translate_, __$storage__, __$control__, __$utilities__) {
        $rootScope = _$rootScope_;
        $compile = _$compile_;
        $httpBackend = _$httpBackend_;
        $translate = _$translate_;
        $storage = __$storage__;
        $control = __$control__;
        $utilities = __$utilities__;

        $rootScope.view = 'base';
        $rootScope.context = 'screen';

        // backend definition common for all tests
        $httpBackend.when('POST', 'settings').respond(DefaultSettings);
      }]);
  });

  it('replaces the element with the appropriate content', function () {
    // Compile a piece of HTML containing the directive
    $rootScope.component = component;
    $compile("<input ui-select2='" + options + "' initialized='true'/>")($rootScope);
    // fire all the watches
    $rootScope.$digest();
  });

  it('initializes a select2 plugin', function () {
    $rootScope.firstLoad = true;
    $rootScope.component = component;
    $rootScope["aweSelectOptions"] = {placeholder: "tutu"};

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($.fn, "select2");

    // Compile a piece of HTML containing the directive
    let element = $compile("<input ui-select2='aweSelectOptions' initialized='true'/>")($rootScope);

    // fire all the watches
    $rootScope.$digest();
  });

  it('updates a select2 plugin without placeholder', function () {
    $rootScope.firstLoad = true;
    $rootScope.component = component;
    $rootScope["aweSelectOptions"] = {};
    let select2Content = {opts: {}};

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($translate, "instant").and.returnValue("lala");
    spyOn($.fn, "select2").and.returnValue({});

    // Compile a piece of HTML containing the directive
    let element = angular.element("<input ui-select2='aweSelectOptions' initialized='true'/>");
    $compile(element)($rootScope);

    // fire all the watches
    $rootScope.$digest();

    // Update language
    $rootScope.$broadcast("languageChanged");

    // fire all the watches
    $rootScope.$digest();

    // Check translate instant called on language changed
    expect($translate.instant).not.toHaveBeenCalled();
  });

  it('updates a select2 plugin with placeholder', function () {
    $rootScope.firstLoad = true;
    $rootScope.component = component;
    $rootScope["aweSelectOptions"] = {placeholder: "tutu"};
    let select2Content = {opts: {}};

    // Spy on storage
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($translate, "instant").and.returnValue("lala");
    spyOn($.fn, "select2").and.returnValue({});

    // Compile a piece of HTML containing the directive
    let element = angular.element("<input ui-select2='aweSelectOptions' initialized='true'/>");
    $compile(element)($rootScope);

    // fire all the watches
    $rootScope.$digest();

    // Update language
    spyOn($.fn, "data").and.returnValue(select2Content);
    $rootScope.$broadcast("languageChanged");

    // fire all the watches
    $rootScope.$digest();

    // Check translate instant called on language changed
    expect($translate.instant).toHaveBeenCalled();
  });

  it('updates a select2 plugin with placeholder and setPlaceholder method', function () {
    $rootScope.firstLoad = true;
    $rootScope.component = component;
    $rootScope["aweSelectOptions"] = {placeholder: "tutu"};
    let select2Content = {opts: {}, setPlaceholder: () => null};

    // Spies
    spyOn($storage, "get").and.returnValue({'base': {}});
    spyOn($control, "checkComponent").and.returnValue(true);
    spyOn($translate, "instant").and.returnValue("lala");
    spyOn($.fn, "select2").and.returnValue({});
    spyOn(select2Content, "setPlaceholder");

    // Compile a piece of HTML containing the directive
    let element = angular.element("<input ui-select2='aweSelectOptions' initialized='true'/>");
    $compile(element)($rootScope);

    // fire all the watches
    $rootScope.$digest();

    // Update language
    spyOn($.fn, "data").and.returnValue(select2Content);
    $rootScope.$broadcast("languageChanged");

    // fire all the watches
    $rootScope.$digest();

    // Check set placeholder to have been called
    expect(select2Content.setPlaceholder).toHaveBeenCalled();
  });
});