import axios from "axios";
import store from "../store";
import env from "../../build/curr.env";
import router from "../router";

const ajaxUrl =
  env === "development"
    ? "http://localhost:8089/sharingserver"
    : "http://sharing.yibanjf.com/sharingserver";

const util = {};
util.baseUrl = ajaxUrl;

util.ajax = axios.create({
  baseURL: ajaxUrl,
  timeout: 30000
});

util.ajax.defaults.headers["Content-Type"] = "application/json";

util.ajax.interceptors.request.use(
  config => {
    //is exist token then pre http header add token
    if (store.state.user.token) {
      config.headers.Authorization = `YBS ${store.state.user.token}`;
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  }
);

util.ajax.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 返回 401 清除token信息并跳转到登录页面
          store.commit("logout", this);

          router.replace({
            path: "/login",
            query: { redirect: router.currentRoute.fullPath }
          });
      }
    }
    return Promise.reject(error); // 返回接口返回的错误信息
  }
);

function showErrorMessage(vm, data) {
  if (!data) {
    vm.$Notice.error({
      title: "系统异常",
      desc: "未收到返回数据, 请联系客服"
    });
  }
  let display = data.errorDisplay ? data.errorDisplay : "NOTICE";
  let code = data.errorCode ? data.errorCode : 9999;
  let message = data.errorMessage ? data.errorMessage : "服务器连接中断";
  switch (display) {
    case "MESSAGE":
      vm.$Message.error(message);
      break;
    case "NOTICE":
      vm.$Notice.error({
        title: "错误码: " + code,
        desc: message
      });
      break;
    case "MODAL":
      vm.$Modal.error({
        title: "错误码: " + code,
        content: message
      });
      break;
    default:
      vm.$Notice.error({
        title: "系统异常",
        desc: "系统数据错误, 请联系运营人员"
      });
      break;
  }
}

util.errorProcessor = function(vm, error, callback) {
  let response = error.response;
  if (response) {
    let httpCode = response.status;
    let data = response.data;
    if (httpCode === 401) {
      vm.$Notice.warning("登录超时, 请重新登录");
    } else if (httpCode === 403) {
      vm.$Notice.error({
        title: "系统异常",
        desc: "获取系统资源路径失败, 请联系技术人员"
      });
    } else if (httpCode === 404) {
      vm.$Notice.error({
        title: "系统异常",
        desc: "获取系统资源路径失败, 请联系技术人员"
      });
    } else {
      if (callback) {
        callback(data);
      } else {
        showErrorMessage(vm, data);
      }
    }
  } else {
    showErrorMessage(vm, "服务器连接中断");
  }
};

export default util;
