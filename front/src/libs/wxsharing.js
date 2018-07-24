import wx from "weixin-js-sdk";
import util from "./util";

/* param: {title: "分享标题", desc: "描述内容", link:"分享连接", imgUrl: "分享图标URL", localUrl: "分享连接做签名的URL"} */
export default {
  sharing: (vm, param) => {
    let sharingUrl = param.localUrl;
    util.ajax
      .get("/wecat/config", { params: { sharingUrl: sharingUrl } })
      .then(response => {
        let config = response.data;
        console.log(config);
        wx.config({
          debug: false,
          appId: config.appId,
          timestamp: config.timestamp,
          nonceStr: config.nonceStr,
          signature: config.signature,
          jsApiList: ["onMenuShareAppMessage", "onMenuShareTimeline"]
        });
        //签名验证失败
        wx.error(function(response) {
          console.log("微信签名验证失败:");
          console.log(response);
          vm.$Message.error("微信签名验证失败.");
        });
        //签名验证成功，会执行ready方法
        wx.ready(function() {
          // 分享到朋友圈
          wx.onMenuShareTimeline({
            title: param.title, //分享标题
            link: param.link, // 分享链接
            imgUrl: param.imgUrl, //分享图标
            success: function(res) {
              console.log("分享到朋友圈成功返回的信息：", res);
              vm.$Message.success("分享到朋友圈成功");
            },
            cancel: function(res) {
              //用户取消分享后执行的回调函数
              console.log("取消分享到朋友圈返回的信息:", res);
            }
          });

          //分享给朋友
          wx.onMenuShareAppMessage({
            title: param.title, // 标题
            desc: param.desc, // 描述内容
            link: param.link,
            imgUrl: param.imgUrl,
            type: "link",
            success: function(res) {
              console.log("分享到朋友圈成功返回的信息：", res);
              vm.$Message.success("分享到朋友圈成功");
            },
            cancel: function(res) {
              //用户取消分享后执行的回调函数
              console.log("取消分享到朋友圈返回的信息:", res);
            }
          });
        });
      })
      .catch(error => {
        console.log(error);
        util.errorProcessor(error);
      });
  }
};
