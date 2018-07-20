<template>
    <Card @click.native="onClick" class="sider-tag">
        <Row style="width:100%" type="flex" justify="center">
                <Icon :type="icon" size="30" color="#19be6b"></Icon>
        </Row>
        <Row type="flex" style="width: 100%; margin-top: 5px;" align="middle" justify="center">
            <p class="content-title">{{title}}</p>
        </Row>
    </Card>
</template>

<script>
export default {
  name: "configSiderTag",
  props: {
    icon: {
      type: String,
      default: "android-locate"
    },
    title: {
      type: String,
      required: true
    }
  },
  methods: {
    onClick() {
      this.$emit("click");
    }
  }
};
</script>

<style lang="less">
.sider-tag {
  text-align: left;
  margin: 5px;
  background-color: #fff;
  cursor: pointer;
  .content-title {
    font-size: 12px;
    font-weight: bold;
    color: #19be6b;
  }
}
</style>
