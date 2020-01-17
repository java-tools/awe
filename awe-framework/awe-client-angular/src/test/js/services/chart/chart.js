describe('awe-framework/awe-client-angular/src/test/js/services/chart/chart.js', function () {
  let $injector, Chart, $rootScope;
  let originalTimeout;

  // Mock module
  beforeEach(function () {
    angular.mock.module('aweApplication');

    inject(["$injector", function (__$injector__) {
      $injector = __$injector__;
      $rootScope = $injector.get('$rootScope');
      Chart = $injector.get('Chart');
    }]);

    originalTimeout = jasmine.DEFAULT_TIMEOUT_INTERVAL;
    jasmine.DEFAULT_TIMEOUT_INTERVAL = 10000;
  });

  afterEach(function () {
    jasmine.DEFAULT_TIMEOUT_INTERVAL = originalTimeout;
  });

  // Fix column
  it('should check if parameters have changed', function () {
    // Mock
    let $scope = $rootScope.$new();
    let chart = new Chart($scope, "lala", {bind:() => null});
    chart.controller = {chartModel: {}};
    chart.model = {};
    chart.listeners = {};
    spyOn(chart, "asComponent").and.returnValue(true);
    chart.asChart();
    spyOn(chart, "changeZoom").and.returnValue(null);

    // Launch
    chart.checkParametersChanged ({chartOptions: {}});
    expect(chart.changeZoom).not.toHaveBeenCalled();

    chart.checkParametersChanged ({chartOptions: {zoom: 10}});
    expect(chart.changeZoom).toHaveBeenCalledWith(10);
  });
});