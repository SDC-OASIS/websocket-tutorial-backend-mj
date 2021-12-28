// const httpProxyMiddleware = require('http-proxy-middleware');

// module.exports = {
// // ...
//   devServer: {
//     proxy: {
//       '/': {
//         target: 'http://localhost:8081',
//         ws: true,
//         changeOrigin: true
//       }
//     }
//   }
// }
module.exports = {
  outputDir: "../src/main/resources/static",
  indexPath: "../static/index.html",
  devServer: {
    proxy: "http://localhost:8080"
  },
  chainWebpack: config => {
    const svgRule = config.module.rule("svg");
    svgRule.uses.clear();
    svgRule.use("vue-svg-loader").loader("vue-svg-loader");
  }
};