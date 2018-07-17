<style lang="less">
@import "./404.less";
</style>

<template>
    <div class="error404">
        <div class="error404-body-con">
            <Card>
                <div class="error404-body-con-title">4<span><Icon type="ios-navigate-outline"></Icon></span>4</div>
                <p class="error404-body-con-message">业务已暂停，请关注其他业务！</p>
            </Card>
        </div>
    </div>
</template>

<script>
export default {
  name: "Error404",
  methods: {
    backPage() {
      this.$router.go(-1);
    },
    goHome() {
      this.$router.push({
        name: "home_index"
      });
    }
  }
};
</script>
