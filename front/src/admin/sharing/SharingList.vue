<template>
    <div>
        <my-title title="分享页列表"></my-title>
        <Row type="flex" justify="start" :gutter="20" class="margin-left-10" >
            <i-input v-model="searchValue" placeholder="分享标题/分享编号" style="width: 200px;"/>
            <Button type="primary" icon="search" @click="reloadListData" :loading="dataLoading">查询</Button>
        </Row>
        <Table border highlight-row :columns="tabColumns" :data="tabData"
                :loading="dataLoading"
                ref="table" size="small">
        </Table>
        <Row type="flex" justify="end" class="margin-top-10">
            <Page size="small" :total="totalCount" :current="currentPage" :page-size="pageSize" show-total
                @on-change="pageChange">
            </Page>
        </Row>
    </div>
</template>

<script>
import MyTitle from "@/components/MyTitle.vue";
import util from "@/libs/util.js";

export default {
  name: "SharingList",
  components: { MyTitle },
  data() {
    return {
      dataLoading: false,
      searchValue: "",
      totalCount: 0,
      currentPage: 1,
      pageSize: 20,
      tabData: [],
      tabColumns: []
    };
  },
  mounted() {
    this.reloadListData();
  },
  methods: {
    pageChange(data) {
      this.currentPage = data;
    },
    reloadListData() {
      let requestData = {
        search: this.searchValue,
        page: this.currentPage,
        pageSize: this.pageSize
      };
      this.dataLoading = true;
      let self = this;
      util.ajax
        .post("/sharing/admin/list", requestData)
        .then(response => {
          self.dataLoading = false;
          self.tabData = response.data.data;
          self.totalCount = response.data.totalCount;
        })
        .catch(error => {
          self.dataLoading = false;
          util.errorProcessor(self, error);
        });
    }
  }
};
</script>

<style lang="less">
.margin-left-10 {
  margin-left: 10px;
}
</style>
