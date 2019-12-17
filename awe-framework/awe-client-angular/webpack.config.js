let path = require("path");
let webpack = require("webpack");
let ExtractTextPlugin = require("extract-text-webpack-plugin");
let LessPluginAutoPrefix = require('less-plugin-autoprefix');

const dir = path.join(__dirname, "src", "main", "resources", "webpack");
const styleDir = path.resolve(__dirname, "src", "main", "resources", "less");
const libPath = path.resolve(__dirname, "src", "main", "resources", "js", "lib");

let autoprefixerBrowsers = ['last 2 versions', '> 1%', 'opera 12.1', 'bb 10', 'android 4', 'IE 10'];

module.exports = {
  devtool: "source-map",
  entry: {
    "locals-es": path.join(dir, "locals-es.config.js"),
    "locals-en": path.join(dir, "locals-en.config.js"),
    "locals-eu": path.join(dir, "locals-eu.config.js"),
    "locals-fr": path.join(dir, "locals-fr.config.js"),
    "bundle": path.join(dir, "awe.config.js")
  },
  output: {
    filename: "js/[name].js",
    path: path.join(__dirname, 'target', 'classes', 'static'),
    publicPath: "../"
  },
  module: {
    rules: [
      // Hack to load angular synchronously
      {
        test: /[\/]angular\.js$/,
        loader: "exports-loader?angular"
      },
      {
        test: /\.jsx?$/,
        loader: 'babel-loader',
        exclude: /node_modules/
      },
      {
        test: /\.css$/,
        include: [styleDir],
        use: ExtractTextPlugin.extract({
          fallback: "style-loader",
          use: "css-loader"
        })
      }, {
        test: /\.less$/,
        include: [styleDir],
        use: ExtractTextPlugin.extract({
          fallback: "style-loader",
          use: [{
            loader: "css-loader"
          }, {
            loader: "less-loader",
            options: {
              lessPlugins: [
                new LessPluginAutoPrefix({browsers: autoprefixerBrowsers})
              ],
              //compress: true,
              minimize: true,
              sourceMap: true,
              //output: {comments: /^\[\+/}
            },
          }]
        })
      }, {
        test: /\.(jpg|gif|png)$/,
        use: [{
          loader: "url-loader",
          options: {
            limit: 100000,
            name: "./images/[hash].[ext]"
          }
        }]
      }, {
        test: /\.woff[2]*?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        use: [{
          loader: "url-loader",
          options: {
            limit: 10000,
            mimetype: "application/font-woff",
            name: "./fonts/[name].[ext]"
          }
        }]
      }, {
        test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        use: [{
          loader: "file-loader",
          options: {
            name: "./fonts/[name].[ext]"
          }
        }]
      }]
  },
  resolve: {
    extensions: [".js", ".css", ".less", "*"],
    alias: {
      "jquery": path.resolve(__dirname, "node_modules", "jquery", "dist", "jquery"),
      "ng-caps-lock": path.resolve(libPath, "ngCapsLock", "ng-caps-lock"),
      "bootstrap-tabdrop": path.resolve(libPath, "bootstrap-tabdrop", "src", "js", "bootstrap-tabdrop"),
      "HighchartsLocale": path.resolve(libPath, "highcharts", "i18n", "highcharts-lang"),
      "HighchartsThemes": path.resolve(libPath, "highcharts", "themes", "all"),
      "Tocify": path.resolve(libPath, "tocify", "jquery.tocify"),
      "PivotTable": path.resolve(libPath, "pivotTable", "pivotTable")
    },
    modules: [
      path.join(__dirname, "src"),
      "node_modules"
    ]
  },
  plugins: [
    new ExtractTextPlugin({
      filename: "css/styles.css",
      disable: false,
      allChunks: true
    }),
    new webpack.ProvidePlugin({
      "jQuery": "jquery",
      "$": "jquery",
      "window.jQuery": "jquery",
      "window.$": "jquery",
      "Highcharts": "highcharts/highstock",
      "HighchartsLocale": "HighchartsLocale",
      "window.constructor": "constructor"
    }),
    new webpack.optimize.CommonsChunkPlugin({
      name: 'commons',
      filename: 'js/commons.js',
      minChunks: 2
    }),
    new webpack.optimize.UglifyJsPlugin({
      uglifyOptions: {compress: {warnings: true, drop_console: false}},
      cache: true,
      parallel: true,
      sourceMap: true
    })
    //new BundleAnalyzerPlugin()
  ]
};