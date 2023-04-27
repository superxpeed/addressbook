const path = require('path');
const babelConfig = require('./babel.config');
const ESLintPlugin = require('eslint-webpack-plugin');

module.exports = {
    mode: 'development',
    devtool: 'source-map',
    entry: [
        './src/index.js'
    ],
    output: {
        path: path.resolve(__dirname, '../resources/static'),
        filename: 'bundle.js',
    },
    resolve: {
        modules: [
            path.resolve(__dirname, 'src'),
            'node_modules'
        ],
        extensions: ['.ts', '.tsx', '.js', '.jsx', '.json'],
    },
    module: {
        rules: [
            {
                test: /\.(js|ts)x?$/,
                include: [
                    path.resolve(__dirname, 'src'),
                    path.resolve(__dirname, 'node_modules/quill')
                ],
                use: [
                    {
                        loader: 'babel-loader',
                        options: babelConfig
                    },
                ],
            },
            {
                test: /\.less$/,
                use: [
                    'style-loader',
                    'css-loader',
                    'postcss-loader',
                    'less-loader',
                ],
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader',
                    'postcss-loader',
                ],
            },
            {
                test: /\.png$/,
                use: ['url-loader'],
            },
            {
                test: /\.(eot|svg|ttf|woff|woff2|gif)$/,
                use: ['url-loader'],
            },
        ],
    },
    devServer: {
        port: 7070,
        proxy: {
            '/bundle.js': {
                target: 'http://localhost:7070'
            },
            '**': 'http://localhost:10000',
        },
        client: {
            overlay: false,
        },
    },
    plugins: [new ESLintPlugin()]
};

