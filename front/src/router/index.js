import Vue from "vue";
import Router from "vue-router";
import iView from "iview";
import store from "../store";
import { routes } from "./router";

Vue.use(Router);

// 页面刷新时，重新赋值token
if (window.localStorage.getItem("token")) {
  store.commit("setToken", window.localStorage.getItem("token"));
}
if (window.localStorage.getItem("userDetail")) {
  store.commit("setUserDetail", window.localStorage.getItem("userDetail"));
}

// 路由配置
const RouterConfig = {
  routes: routes
};
export const router = new Router(RouterConfig);

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start();
  let title = to.meta.title ? to.meta.title : "医伴金服";
  window.document.title = title;
  if (!to.meta.noAuth) {
    // 判断该路由是否需要登录权限
    if (store.state.user && store.state.user.token) {
      // 通过vuex state获取当前的token是否存在
      next();
    } else {
      next({
        name: "login"
      });
    }
  } else {
    next();
  }
});

router.afterEach(to => {
  iView.LoadingBar.finish();
  window.scrollTo(0, 0);
});

export default router;
