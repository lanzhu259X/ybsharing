<template>
    <div class="admin-main">
        <div class="head">
            <admin-head></admin-head>
        </div>
        <div class="content">
            <keep-alive>
                <router-view></router-view>
            </keep-alive>
        </div>
    </div>
</template>

<script>
import AdminHead from "@/components/AdminHead.vue";

export default {
  name: "Main",
  components: {
    AdminHead
  }
};
</script>

<style lang="less">
.admin-main {
  width: 100%;
  height: 100%;
  .head {
    position: absolute;
    top: 0em;
  }
  .content {
    margin-top: 1em;
  }
}
</style>

