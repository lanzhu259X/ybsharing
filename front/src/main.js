import Vue from "vue";
import App from "./App";
import router from "./router";
import iView from "iview";
import Vuex from "vuex";
import store from "./store/index";
import "iview/dist/styles/iview.css";

Vue.use(iView);
Vue.use(Vuex);

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: "#app",
  router: router,
  stort: store,
  render: h => h(App)
});
