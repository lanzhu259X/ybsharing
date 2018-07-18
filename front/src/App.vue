<template>
  <div id="app">
    <router-view/>
    <footer></footer>
  </div>
</template>

<script>
import Footer from "@/components/Footer.vue";

export default {
  name: "App",
  components: { Footer }
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
</style>
