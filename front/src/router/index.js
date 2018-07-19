import Vue from "vue";
import Router from "vue-router";
import iView from "iview";
import store from "../store";

Vue.use(Router);

// 页面刷新时，重新赋值token
if (window.localStorage.getItem("token")) {
  store.commit("setToken", window.localStorage.getItem("token"));
}
if (window.localStorage.getItem("userDetail")) {
  store.commit("setUserDetail", window.localStorage.getItem("userDetail"));
}

export const loginRoute = {
  path: "/login",
  name: "login",
  meta: {
    title: "登录",
    noAuth: true
  },
  component: () => import("@/admin/Login.vue")
};

export const registerRoute = {
  path: "/register",
  name: "register",
  meta: { title: "注册", noAuth: true },
  component: () => import("@/admin/Register.vue")
};

export const page404 = {
  path: "/*",
  name: "error-404",
  meta: {
    title: "找不到业务",
    noAuth: true
  },
  component: () => import("@/error-page/404.vue")
};

export const adminRoute = {
  path: "/",
  name: "manager",
  redirect: "/home",
  component: () => import("@/admin/Main.vue"),
  children: [
    {
      path: "home",
      name: "home",
      meta: {
        title: "分享管理-主页"
      },
      component: () => import("@/admin/Home.vue")
    },
    {
      path: "ownspace",
      name: "ownspace",
      meta: {
        title: "账号信息"
      },
      component: () => import("@/admin/MyOwnSpace.vue")
    }
  ]
};

export const sharingRoute = {
  path: "/sharing",
  name: "sharing",
  children: [
    {
      path: "/loan-bill",
      name: "LoginBill",
      meta: {
        title: "发票贷信息登记",
        noAuth: true
      },
      component: () => import("@/sharing/LoanBill.vue")
    },
    {
      path: "/loan-ag",
      name: "LoanAG",
      meta: {
        title: "爱柜贷信息登记",
        noAuth: true
      },
      component: () => import("@/sharing/LoanAG.vue")
    }
  ]
};

export const router = new Router({
  routes: [loginRoute, registerRoute, page404, adminRoute, sharingRoute]
});

router.beforeEach((to, from, next) => {
  iView.LoadingBar.start();
  let title = to.meta.title ? "医伴金服" : to.meta.title;
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
