const path = require('path');
const resolve = require('path').resolve;
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const url = require('url');
const request = require("request");
const publicPath = '';

module.exports = (options = {}) => ({
    entry: {
        vendor: './src/vendor',
        index: './src/main.js'
    },
    output: {
        path: resolve(__dirname, '../contract-backend/src/main/resources/static'),
        filename: options.dev ? '[name].js' : '[name].js?[chunkhash]',
        chunkFilename: '[id].js?[chunkhash]',
        publicPath: options.dev ? '/assets/' : '/'
    },
    module: {
        loaders: [
            {
                test: /\.css$/,
                loader: 'style-loader!css-loader'
            }
        ],
        rules: [
            {
                test: /\.vue$/,
                use: ['vue-loader']
            },
            {
                test: /\.js$/,
                use: ['babel-loader'],

                //include: [resolve('src'), resolve('test'),resolve('/node_modules/element-ui/src'),resolve('/node_modules/element-ui/packages')],
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader', 'postcss-loader']
            },
            {
                test: /\.(png|jpg|jpeg|gif|eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
                use: [{
                    loader: 'url-loader',
                    options: {
                        limit: 10000
                    }
                }]
            }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            names: ['vendor', 'manifest']
        }),
        new HtmlWebpackPlugin({
            title: 'My App',
            template: 'src/index.html'
        })
    ],
    resolve: {
        alias: {
            '~': resolve(__dirname, 'src'),
            'vue': 'vue/dist/vue.js',
        }
    },
    devServer: {
        //host: 'stargate.along101corp.com',
        host: 'localhost',
        port: 8001,
        contentBase: [path.join(__dirname, "public"), path.join(__dirname, "/assets")],
        proxy: {
            '/web/': {
                target: 'http://localhost:7001',
                changeOrigin: true,
            },
            '/api/': {
                target: 'http://localhost:7001',
                changeOrigin: true,
            }
        },
        historyApiFallback: {
            index: url.parse(options.dev ? '/assets/' : publicPath).pathname
        }
    },
    devtool: options.dev ? '#eval-source-map' : '#source-map'
})
