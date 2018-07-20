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
        title: "首页"
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
    },
    {
      path: "new-sharing",
      name: "newsharing",
      meta: {
        title: "创建分享信息"
      },
      component: () => import("@/admin/sharing/NewSharing.vue")
    },
    {
      path: "sharing-list",
      name: "sharinglist",
      meta: {
        title: "分享信息列表"
      },
      component: () => import("@/admin/sharing/SharingList.vue")
    },
    {
      path: "/sharing-detail/:sharingNo",
      name: "sharingdetail",
      meta: {
        title: "分享详情"
      },
      component: () => import("@/admin/sharing/SharingDetail.vue")
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

export const routes = [
  loginRoute,
  registerRoute,
  adminRoute,
  sharingRoute,
  page404
];
