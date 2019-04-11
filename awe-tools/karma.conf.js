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
    reporters: ['spec', 'junit', 'coverage'],
    //concurrency: 1,
    browserConsoleLogOptions: { level: 'info', format: '%b %T: %m', terminal: true},
    reportSlowerThan: 500,
    singleRun: true,
    files: [ tests ],
    preprocessors: {
      [tests]: [ 'webpack', 'sourcemap' ],
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
        if (process.env["launch.edge"] != "true") {
          if (edgeIndex > -1) {
            result.splice(edgeIndex, 1);
          }
        }
        return result;
      }
    },
    specReporter: {
      suppressErrorSummary: false, // do not print error summary
      suppressFailed: false,       // do not print information about failed tests
      suppressPassed: false,       // do not print information about passed tests
      suppressSkipped: true,       // do not print information about skipped tests
      showSpecTiming: true,        // print the time elapsed for each spec
      failFast: false              // test would finish with error when a first fail occurs.
    },
    coverageReporter: {
      dir: 'coverage/',
      reporters: [
        { type: 'html', subdir: (browser) => browser.toLowerCase().split(/[ /-]/)[0] },
        { type: 'lcovonly', subdir: '.', file: 'lcov.info' }
      ],
      instrumenterOptions: {
        istanbul: {
          noCompact: true
        }
      }
    },
    junitReporter: {
      outputDir: 'junit/'
    },
    webpack: {
      devtool : "inline-source-map",
      module : {
        rules : [
          // Hack to load angular synchronously
          {
            test : /[\/]angular\.js$/,
            loader : "exports-loader?angular"
          },
          {
            test: /\.jsx?$/,
            loader: 'babel-loader',
            exclude: /node_modules/
          }]
      },
      externals: {
        aweApplication: 'aweApplication'
      },
      resolve : {
        extensions : [ ".js", ".css", ".less", "*" ],
        alias : {
          "angular-filemanager" : "angular-filemanager-fkoester"
        }
      },
      plugins : [
        new webpack.optimize.UglifyJsPlugin({ uglifyOptions: {compress: { warnings: true, drop_console: false}}, cache: true, parallel:true, sourceMap: true}),
        new webpack.ProvidePlugin({
          "$" : "jquery",
          "jQuery" : "jquery",
          "window.jQuery" : "jquery"
        })
      ]
    }
  })
};