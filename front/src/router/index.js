import Vue from "vue";
import Router from "vue-router";
import iView from "iview";
import store from "../store";

Vue.use(Router);

export const loginRoute = {
  path: "/login",
  name: "login",
  meta: {
    title: "登录"
  },
  component: () => import("@/admin/Loin.vue")
};

export const registerRoute = {
  path: "/register",
  name: "register",
  meta: {
    title: "注册"
  },
  component: () => import("@/admin/Register.vue")
};

export const page404 = {
  path: "/*",
  name: "error-404",
  meta: {
    title: "找不到业务"
  },
  component: () => import("@/error-page/404.vue")
};

export const adminRoute = {
  path: "/manager",
  name: "manager",
  redirect: "/home",
  component: () => import("@/admin/Main.vue"),
  children: [
    {
      path: "/home",
      name: "home",
      meta: {
        title: "分享管理-主页"
      },
      component: () => import("@/admin/Home.vue")
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
        title: "发票贷信息登记"
      },
      component: () => import("@/sharing/LoanBill.vue")
    },
    {
      path: "/loan-ag",
      name: "LoanAG",
      meta: {
        title: "爱柜贷信息登记"
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
  Util.title(to.meta.title);

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
  Util.openNewPage(router.app, to.name, to.params, to.query);
  iView.LoadingBar.finish();
  window.scrollTo(0, 0);
});

export default router;
