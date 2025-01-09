const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  chainWebpack: config => {
    config.module
      .rule('svg')
      .type('asset/resource')
      .set('generator', {
        filename: 'img/[name].[hash:8][ext]'
      });
  },
  devServer: {
    port: 3000,
    historyApiFallback: {
      rewrites: [
        { from: /./, to: '/index.html' }
      ]
    },
    proxy: {
      '/mg': {
        target: 'http://localhost:18888',
        changeOrigin: true,
        pathRewrite: {
          '^/mg': '/mg'
        },
        logLevel: 'debug'
      }
    }
  },
  css: {
    loaderOptions: {
      scss: {
        additionalData: `
          @import "@/assets/styles/variables.scss";
          @import "@/assets/styles/mixins.scss";
        `
      }
    }
  }
});