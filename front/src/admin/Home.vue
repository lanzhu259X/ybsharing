<template>
    <div>
        <my-title title="功能区"></my-title>
        <Row>
            <i-col :xs="8" :sm="6" :md="5" :lg="4">
                <my-menu @click="newSharing" icon="paper-airplane" title="新建分享登记"></my-menu>
            </i-col>
            <i-col :xs="8" :sm="6" :md="5" :lg="4">
                <my-menu @click="sharingList" icon="stats-bars" title="分享信息统计"></my-menu>
            </i-col>
            <i-col :xs="8" :sm="6" :md="5" :lg="4">
                <my-menu title="任务列表"></my-menu>
            </i-col>
            <i-col :xs="8" :sm="6" :md="5" :lg="4">
            </i-col>
            <i-col :xs="8" :sm="6" :md="5" :lg="4">
            </i-col>
            <i-col :xs="8" :sm="6" :md="5" :lg="4">
            </i-col>
        </Row>
    </div>
</template>

<script>
import MyTitle from "@/components/MyTitle.vue";
import MyMenu from "@/components/MyMenu.vue";

export default {
  name: "Home",
  components: {
    MyTitle,
    MyMenu
  },
  data() {
    return {};
  },
  mounted() {},
  methods: {
    newSharing() {
      this.$router.push({ name: "newsharing" });
    },
    sharingList() {
      this.$router.push({ name: "sharinglist" });
    }
  }
};
</script>

