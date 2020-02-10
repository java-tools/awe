const path = require("path");
const webpack = require("webpack");
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const LessPluginAutoPrefix = require('less-plugin-autoprefix');

const dir = path.join(__dirname, "src", "main", "resources", "webpack");
const autoprefixerBrowsers = ['last 2 versions', '> 1%', 'opera 12.1', 'bb 10', 'android 4', 'IE 10'];
const styleDir = path.resolve(__dirname, "src", "main", "resources", "less");

module.exports = {
  mode: process.env.NODE_ENV,
  devtool : "source-map",
  entry : {
    "bundle-tools" : path.join(dir, "tools.config.js")
  },
  output : {
    filename : "js/[name].js",
    path: path.join(__dirname, 'target', 'classes', 'static'),
    publicPath : "./"
  },
  optimization: {
    splitChunks: {
      name: true,
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
      { test: /\.jsx?$/, loader: 'babel-loader', exclude: /node_modules/},
      // Hack to load angular synchronously
      { test : /[\/]angular\.js$/, loader : "exports-loader?angular"},
      { test : /\.css$/, include : [ styleDir ], use : [MiniCssExtractPlugin.loader, "css-loader"]},
      { test : /\.less$/, include : [ styleDir ], use : [MiniCssExtractPlugin.loader, "css-loader", {
          loader: "less-loader", options: { lessPlugins: [ new LessPluginAutoPrefix({browsers: autoprefixerBrowsers}) ],
            minimize: true, sourceMap: true}}]},
      { test : /\.(jpg|gif|png)$/, loader : 'url-loader?limit=100000&name=./images/[hash].[ext]'},
      { test : /\.woff[2]*?(\?v=[0-9]\.[0-9]\.[0-9])?$/, use : "url-loader?limit=10000&mimetype=application/font-woff&name=./fonts/[hash].[ext]"},
      { test : /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/, use : "file-loader?name=./fonts/[hash].[ext]"}
    ]
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
    new MiniCssExtractPlugin("css/file-manager-styles.css"),
    new webpack.ProvidePlugin({
      "$" : "jquery",
      "jQuery" : "jquery",
      "window.jQuery" : "jquery"
    })
   ]
};
