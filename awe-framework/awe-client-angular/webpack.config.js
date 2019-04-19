let path = require("path");
let webpack = require("webpack");
let ExtractTextPlugin = require("extract-text-webpack-plugin");
let LessPluginAutoPrefix = require('less-plugin-autoprefix');

let dir = path.join(__dirname, "src", "main", "resources", "webpack");
let styleDir = path.resolve(__dirname, "src", "main", "resources", "less");

let autoprefixerBrowsers = ['last 2 versions', '> 1%', 'opera 12.1', 'bb 10', 'android 4', 'IE 10'];

module.exports = {
  devtool : "source-map",
  entry : {
    "locals-es" : path.join(dir, "locals-es.config.js"),
    "locals-en" : path.join(dir, "locals-en.config.js"),
    "locals-eu" : path.join(dir, "locals-eu.config.js"),
    "locals-fr" : path.join(dir, "locals-fr.config.js"),
    "bundle" : path.join(dir, "awe.config.js")
  },
  output: {
    filename: "js/[name].js",
    path: path.join(__dirname, 'target', 'classes', 'static'),
    publicPath: "../"
  },
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
    },
    {
      test : /\.css$/,
      include : [ styleDir ],
      use : ExtractTextPlugin.extract({
        fallback : "style-loader",
        use : "css-loader"
      })
    }, {
      test : /\.less$/,
      include : [ styleDir ],
      use : ExtractTextPlugin.extract({
        fallback : "style-loader",
        use : [ "css-loader", {
          loader: "less-loader",
          options: {
            lessPlugins: [
              new LessPluginAutoPrefix({ browsers: autoprefixerBrowsers })
            ],
            minimize: true,
            sourceMap: true
          }
        }]
      })
    }, {
      test : /\.(jpg|gif|png)$/,
      loader : 'url-loader?limit=100000&name=./images/[hash].[ext]'
    }, {
      test : /\.woff[2]*?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
      use : "url-loader?limit=10000&mimetype=application/font-woff&name=./fonts/[hash].[ext]"
    }, {
      test : /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
      use : "file-loader?name=./fonts/[hash].[ext]"
    } ]
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
  new ExtractTextPlugin("css/styles.css"),
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
  }),
  new webpack.optimize.CommonsChunkPlugin({
    name : 'commons',
    filename : 'js/commons.js',
    minChunks : 2
  }),
  new webpack.optimize.UglifyJsPlugin({ uglifyOptions: {compress: { warnings: true, drop_console: false}}, cache: true, parallel:true, sourceMap: true})
  //new BundleAnalyzerPlugin()
  ]
};