import Vue from "vue";
import App from "./App";
import router from "./router";
import iView from "iview";
import store from "./store/index";
import FontIcon from "./components/FontIcon";
import "iview/dist/styles/iview.css";

Vue.use(iView);
Vue.component("FontIcon", FontIcon);

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: "#app",
  router: router,
  store: store,
  render: h => h(App)
});
