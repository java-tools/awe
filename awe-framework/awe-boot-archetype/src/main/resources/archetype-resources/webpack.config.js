var path = require("path");
var webpack = require("webpack");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var dir = path.join(__dirname, "src", "main", "resources", "webpack");

module.exports = {
  devtool : "source-map",
  entry : {
    "specific" : path.join(dir, "app.config.js")
  },
  output : {
    filename : "js/[name].js",
    path: path.join(__dirname, 'target', 'classes', 'static'),
    publicPath : "../"
  },
  module : {
    rules : [ {
      test : /\.css$/,
      include : [ path.resolve(__dirname, "src", "main", "resources", "css") ],
      use : ExtractTextPlugin.extract({
        fallback : "style-loader",
        use : "css-loader"
      })
    }, {
      test : /\.less$/,
      include : [ path.resolve(__dirname, "src", "main", "resources", "less") ],
      use : ExtractTextPlugin.extract({
        fallback : "style-loader",
        use : [ "css-loader", "less-loader" ]
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
    extensions : [ ".js", ".css", ".less", "*" ]
  },
  plugins : [ new ExtractTextPlugin("css/specific.css"),
  /*
   * new webpack.optimize.UglifyJsPlugin({ compress: { warnings: true,
   * drop_console: false, } }),
   */
  ]
};