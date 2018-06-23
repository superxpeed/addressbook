// var path = require('path')
var webpack = require('webpack')

module.exports = {
    devtool: 'source-map',
    entry: [
        './src/index.js'
    ],
    output: {
        // path: path.join(__dirname, '../webapp/dist'),
        publicPath: '/webapp',
        // filename: 'bundle.js', // если указать в отдельной опции папку в отдельной файл то live reload не работает
        filename: './dist/bundle.js', // если указать в отдельной опции папку в отдельной файл то live reload не работает
    },
    /*externals: {
        'react': 'React',
        'react-dom': 'ReactDOM'
    },*/
    module: {
        loaders: [
            {
                test: /\.jsx?$/,
                include: /(src)/,
                loader: ['babel-loader', 'eslint-loader'],
                exclude: /disposables/
                // loader: ['babel']
            },
            {
                test: /\.less$/,
                include: /(src)/,
                loader: "style-loader!css-loader!autoprefixer-loader!less-loader"
            },
            {
                test: /\.css$/,
                include: /(src)/,
                loader: "style-loader!css-loader"
            },
            {
                test: /\.png$/,
                // loader: 'url?limit=10000&name=build/img/[name].[ext]'
                loader: "url-loader"
            },
            {
                test: /\.(eot|svg|ttf|woff|woff2|gif)$/,
                loader: "url-loader"
                //webpackfdsf loader: 'file?name=build/fonts/[name].[ext]'
            }
        ]
    },
    devServer: {
        port: 7070,
        proxy: {
            "**" : "http://localhost:8080"
        },
    },
    plugins: []
};

