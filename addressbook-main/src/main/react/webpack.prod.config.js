const webpack = require('webpack');

const webpackConfig = require('./webpack.config.js');
const TerserPlugin = require('terser-webpack-plugin');

module.exports = Object.assign({}, webpackConfig, {
    mode: 'production',
    devtool: 'source-map',
    plugins: webpackConfig.plugins.concat([
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: JSON.stringify('production')
            }
        })
    ]),
    optimization: {
        minimizer: [(compiler) => {
            new TerserPlugin({
                terserOptions: {
                    compress: true,
                    sourceMap: true,
                    keep_fnames: true
                }
            }).apply(compiler);
        },],
    },
});
