var path = require("path");
var webpack = require("webpack");
let ExtractTextPlugin = require("extract-text-webpack-plugin");
let LessPluginAutoPrefix = require('less-plugin-autoprefix');

var dir = path.join(__dirname, "src", "main", "resources", "webpack");
let autoprefixerBrowsers = ['last 2 versions', '> 1%', 'opera 12.1', 'bb 10', 'android 4', 'IE 10'];
let styleDir = path.resolve(__dirname, "src", "main", "resources", "less");
let fontDir = path.resolve(__dirname, "target", "classes", "static", "fonts");

module.exports = {
  devtool : "source-map",
  entry : {
    "bundle-tools" : path.join(dir, "tools.config.js")
  },
  output : {
    filename : "js/[name].js",
    path: path.join(__dirname, 'target', 'classes', 'static'),
    publicPath : "./"
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
      use : [{
        loader: 'url-loader',
        options: {
          limit: 10000,
          mimetype: 'application/font-woff',
          name: '[hash].[ext]',
          publicPath: '../',
          outputPath: 'fonts/'
        }
      }]
    }, {
      test : /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
      use : [{
        loader: 'file-loader',
        options: {
          name: '[hash].[ext]',
          publicPath: '../',
          outputPath: 'fonts/'
        }
      }]
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
    new ExtractTextPlugin("css/file-manager-styles.css"),
    new webpack.optimize.UglifyJsPlugin({ uglifyOptions: {compress: { warnings: true, drop_console: false}}, cache: true, parallel:true, sourceMap: true}),
    new webpack.ProvidePlugin({
      "$" : "jquery",
      "jQuery" : "jquery",
      "window.jQuery" : "jquery"
    })
   ]
};
