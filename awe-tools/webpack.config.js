var path = require("path");
var webpack = require("webpack");

var dir = path.join(__dirname, "src", "main", "resources", "webpack");

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
    {
       test: /\.jsx?$/,
       loader: 'babel-loader',
       exclude: /node_modules/
    },
    // Hack to load angular synchronously
    {
      test : /[\/]angular\.js$/,
      loader : "exports-loader?angular"
    },
    {
      test : /\.woff[2]*?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
      use : "url-loader?limit=10000&mimetype=application/font-woff&name=./fonts/[hash].[ext]"
    }, {
      test : /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
      use : "file-loader?name=./fonts/[hash].[ext]"
    } ]
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
};
