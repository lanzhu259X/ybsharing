<template>
  <div id="app">
    <keep-alive>
      <router-view></router-view>
    </keep-alive>
    <my-footer class="page-footer"></my-footer>
  </div>
</template>

<script>
import MyFooter from "./components/MyFooter.vue";

export default {
  name: "App",
  components: { MyFooter }
};
</script>

<style>
html,
body {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
  width: 100%;
  height: 100%;
  background: #f0f0f0;
  overflow: hidden;
}
#app {
  width: 100%;
  height: 100%;
}
.page-footer {
  position: absolute;
  width: 100%;
  bottom: 0em;
}
</style>
