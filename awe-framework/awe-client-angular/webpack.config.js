const path = require("path");
const webpack = require("webpack");
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const LessPluginAutoPrefix = require('less-plugin-autoprefix');
const LodashModuleReplacementPlugin = require('lodash-webpack-plugin');

const dir = path.join(__dirname, "src", "main", "resources", "webpack");
const styleDir = path.resolve(__dirname, "src", "main", "resources", "less");
const srcPath = path.resolve(__dirname, "src", "main", "resources", "js", "awe");
const libPath = path.resolve(__dirname, "src", "main", "resources", "js", "lib");

const autoprefixerBrowsers = ['last 2 versions', '> 1%', 'opera 12.1', 'bb 10', 'android 4', 'IE 10'];

module.exports = {
  mode: process.env.NODE_ENV,
  devtool: "source-map",
  entry: {
    "bundle": path.join(dir, "awe.config.js"),
    "locals-es": path.join(dir, "locals-es.config.js"),
    "locals-en": path.join(dir, "locals-en.config.js"),
    "locals-eu": path.join(dir, "locals-eu.config.js"),
    "locals-fr": path.join(dir, "locals-fr.config.js")
  },
  output: {
    filename: "js/[name].js",
    path: path.join(__dirname, 'target', 'classes', 'static'),
    publicPath: "../"
  },
  optimization: {
    splitChunks: {
      name: "bundle",
      cacheGroups: {
        commons: {
          test: /[\\/]node_modules[\\/]/,
          name: 'commons',
          chunks: 'all'
        }
      }
    }
  },
  module : {
    rules : [
      { test: require.resolve('jquery'), use: [{loader: 'expose-loader', options: 'jQuery'},{loader: 'expose-loader', options: '$'}]},
      { test: /\.jsx?$/, loader: 'babel-loader', exclude: /node_modules/},
      // Hack to load angular synchronously
      { test : /[\/]angular\.js$/, loader : "exports?angular"},
      { test : /\.css$/, include : [ styleDir ], use : [MiniCssExtractPlugin.loader, "css-loader"]},
      { test : /\.less$/, include : [ styleDir ], use : [MiniCssExtractPlugin.loader, "css-loader", {
          loader: "less-loader", options: { lessPlugins: [ new LessPluginAutoPrefix({browsers: autoprefixerBrowsers}) ],
            minimize: true, sourceMap: true}}]},
      { test : /\.(jpg|gif|png)$/, loader : 'url-loader?limit=100000&name=./images/[hash].[ext]'},
      { test : /\.woff[2]*?(\?v=[0-9]\.[0-9]\.[0-9])?$/, use : "url-loader?limit=10000&mimetype=application/font-woff&name=./fonts/[hash].[ext]"},
      { test : /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/, use : "file-loader?name=./fonts/[hash].[ext]"}
    ]
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
  },
  plugins: [
    new MiniCssExtractPlugin({
      filename: "css/styles.css",
      disable: false,
      allChunks: true
    }),
    new LodashModuleReplacementPlugin,
    new webpack.ProvidePlugin({
      "Highcharts": "highcharts/highstock",
      "HighchartsLocale": "HighchartsLocale",
      "window.constructor": "constructor"
    })
  ]
};