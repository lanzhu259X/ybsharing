<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: "App"
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
