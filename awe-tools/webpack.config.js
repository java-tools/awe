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
    // {
    // test : /\.css$/,
    // include : [ path.resolve(__dirname, "src", "main", "resources", "less")
    // ],
    // use : ExtractTextPlugin.extract({
    // fallback : "style-loader",
    // use : "css-loader"
    // })
    // }, {
    // test : /\.less$/,
    // include : [ path.resolve(__dirname, "src", "main", "resources", "less")
    // ],
    // use : ExtractTextPlugin.extract({
    // fallback : "style-loader",
    // use : [ "css-loader", "less-loader" ]
    // })
    // }, {
    // test : /\.(jpg|gif|png)$/,
    // loader : 'url-loader?limit=100000&name=./images/[hash].[ext]'
    // },
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