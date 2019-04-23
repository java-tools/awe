let path = require("path");
let webpack = require("webpack");
let testDir = path.join(__dirname, "src", "test", "js");
let resourcesDir = path.join(__dirname, "src", "main", "resources");
let tests = path.join(testDir, "tests.js");
let dir = path.join(__dirname, "src", "main", "resources", "webpack");

// Fix webpack for karma
module.exports = (config) => {
  config.set({
    basePath: path.join(__dirname, "target", "reports", "karma"),
    frameworks: ['detectBrowsers', 'jasmine'],
    reporters: ['spec', 'sonarqubeUnit', 'coverage-istanbul'],
    //concurrency: 1,
    browserConsoleLogOptions: { level: 'info', format: '%b %T: %m', terminal: true},
    reportSlowerThan: 500,
    singleRun: true,
    files: [ tests ],
    preprocessors: {
      [tests]: ['webpack', 'sourcemap']
    },
    // configuration
    detectBrowsers: {
      enabled: true,
      usePhantomJS: false,
      preferHeadless: true,
      // Remove edge
      postDetection: function(availableBrowsers) {
        var result = availableBrowsers;
        var edgeIndex = availableBrowsers.indexOf('Edge');
        console.info("LAUNCH EDGE? " + process.env["launch.edge"]);
        if (process.env["launch.edge"] !== "true") {
          if (edgeIndex > -1) {
            result.splice(edgeIndex, 1);
          }
        }

        // Replace safari to safariNative if found
        var safariIndex = availableBrowsers.indexOf('Safari');
        if (safariIndex > -1) {
          result[safariIndex] = 'SafariNative';
        }
        return result;
      }
    },
    webpack: {
      devtool: 'inline-source-map',
      module : {
        rules : [
          {
            test: /\.jsx?$/,
            loader: 'babel-loader',
            exclude: /node_modules|webpack/
          }]
      },
      resolve : {
        extensions : [ ".js", ".css", ".less", "*" ],
        alias : {
          "aweApplication": path.resolve(__dirname, "src", "main", "resources", "js", "awe", "awe"),
          "jquery": path.resolve(__dirname, "node_modules", "jquery", "dist", "jquery"),
          "stompjs" : path.resolve(__dirname, "node_modules", "stompjs", "lib", "stomp"),
          "moment" : path.resolve(__dirname, "node_modules", "moment"),
          "numeral" : path.resolve(__dirname, "node_modules", "numeral"),
          "ng-caps-lock" : path.resolve(__dirname, "src", "main", "resources", "js", "lib", "ngCapsLock", "ng-caps-lock"),
          "bootstrap-tabdrop" : path.resolve(__dirname, "src", "main", "resources", "js", "lib", "bootstrap-tabdrop", "src", "js", "bootstrap-tabdrop"),
          "HighchartsLocale" : path.resolve(__dirname, "src", "main", "resources", "js", "lib", "highcharts", "i18n", "highcharts-lang")
        }
      },
      plugins : [
        new webpack.ProvidePlugin({
          "jQuery": "jquery",
          "$": "jquery",
          "window.jQuery": "jquery",
          "window.$": "jquery",
          "aweApplication": "aweApplication",
          "SockJS" : "sockjs-client",
          "Highcharts" : "highcharts/highstock.src",
          "HighchartsLocale" : "HighchartsLocale",
          "numeral" : "numeral",
          "moment" : "moment",
          "window.constructor" : "constructor",
          "Spinner" : "spin.js",
          "Slider" : "bootstrap-slider"
        })
      ]
    },
    specReporter: {
      suppressErrorSummary: false, // do not print error summary
      suppressFailed: false,       // do not print information about failed tests
      suppressPassed: false,       // do not print information about passed tests
      suppressSkipped: true,       // do not print information about skipped tests
      showSpecTiming: true,        // print the time elapsed for each spec
      failFast: false              // test would finish with error when a first fail occurs.
    },
    coverageIstanbulReporter: {
      dir: path.join(__dirname, "target", "reports", "karma", "coverage"),
      // reports can be any that are listed here: https://github.com/istanbuljs/istanbuljs/tree/aae256fb8b9a3d19414dcf069c592e88712c32c6/packages/istanbul-reports/lib
      reports: ['html', 'lcovonly', 'text-summary'],

      // Combines coverage information from multiple browsers into one report rather than outputting a report
      // for each browser.
      combineBrowserReports: true,

      // if using webpack and pre-loaders, work around webpack breaking the source path
      fixWebpackSourcePaths: true,

      // Omit files with no statements, no functions and no branches from the report
      skipFilesWithNoCoverage: true,

      // Most reporters accept additional config options. You can pass these through the `report-config` option
      'report-config': {
        // all options available at: https://github.com/istanbuljs/istanbuljs/blob/aae256fb8b9a3d19414dcf069c592e88712c32c6/packages/istanbul-reports/lib/html/index.js#L135-L137
        html: {
          // outputs the report in ./coverage/html
          subdir: 'html'
        }
      },
      verbose: true // output config used by istanbul for debugging
    },
    sonarQubeUnitReporter: {
      sonarQubeVersion: 'LATEST',
      outputFile: path.join("junit", "javascriptUnitTests.xml"),
      overrideTestDescription: false,
      testFilePattern: '.js',
      useBrowserName: false
    },
  });
};