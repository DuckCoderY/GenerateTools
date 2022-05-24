module.exports = {
    devServer: {
        proxy: {
            '^/api': {
                target: 'http:/192.168.0.222:10086/back',
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    }
}
