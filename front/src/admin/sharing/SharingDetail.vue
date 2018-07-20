<template>
    <div>
        <my-title title="分享详情">
            <ButtonGroup slot="extra" size="small">
                <Button type="success" icon="paper-airplane" @click="sharingHandle">分享</Button>
                <Button type="primary" icon="refresh" @click="init">刷新</Button>
                <Button type="info" icon="reply" @click="gotoList">列表</Button>
                <Button type="ghost" icon="home" @click="gotoHome">首页</Button>
            </ButtonGroup>
        </my-title>
        <div class="sharing-modal">
            <Row type="flex" justify="start">
                <strong>分享编号:</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
                {{sharingModal.sharingNo}}
            </Row>

            <Row type="flex" justify="start">
                <strong>标题:</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
                {{sharingModal.sharingTitle}}
            </Row>

            <Row type="flex" justify="start">
                <strong>副标题:</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
                {{sharingModal.sharingSubTitle}}
            </Row>

            <Row type="flex" justify="start">
                <strong>规则/描述:</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
                {{sharingModal.sharingContent}}
            </Row>

            <Table highlight-row :columns="recordColumns" :data="recordData"
                :loading="recordLoading"
                ref="table" size="small">
            </Table>
            <Row type="flex" justify="end" class="margin-top-10">
                <Page size="small" :total="totalCount" :current="currentPage" :page-size="pageSize" show-total
                    @on-change="pageChange">
                </Page>
            </Row>
        </div>

        <my-title title="统计信息" style="margin-top: 25px">
            <a slot="extra" href="javascript:void(0)" @click="refreshTongji">
                <Icon type="refresh" size="20"></Icon>
            </a>
        </my-title>
        <div class="tongji">
            <h2>统计信息 </h2>
        </div>
    </div>
</template>

<script>
import util from "@/libs/util.js";
import MyTitle from "@/components/MyTitle.vue";

export default {
  name: "SharingDetail",
  components: {
    MyTitle
  },
  data() {
    return {
      sharingNo: "",
      sharingModal: {},
      recordColumns: [],
      recordData: [],
      recordLoading: false,
      totalCount: 0,
      currentPage: 1,
      pageSize: 20
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.sharingNo = this.$route.params.sharingNo;
      if (!this.sharingNo) {
        console.log("sharingNo get fail.");
        return;
      }
      let self = this;
      util.ajax
        .get("/sharing/admin/detail/" + this.sharingNo)
        .then(response => {
          this.sharingModal = response.data;
          this.loadSharingRecords();
        })
        .catch(error => {
          util.errorProcessor(self, error);
        });
    },
    pageChange(data) {
      this.currentPage = data;
      this.loadSharingRecords();
    },
    loadSharingRecords() {
      if (!this.sharingNo) {
        return;
      }
      let requestData = {
        page: this.currentPage,
        pageSize: this.pageSize
      };
      let self = this;
      util.ajax
        .post("/sharing/admin/" + this.sharingNo + "/records", requestData)
        .then(response => {
          console.log(response.data);
        })
        .catch(error => {
          util.errorProcessor(self, error);
        });
    },
    sharingHandle() {
      console.log("click sharing handle");
    },
    gotoList() {
      this.$router.push({ name: "sharinglist" });
    },
    gotoHome() {
      this.$router.push({ name: "home" });
    },
    refreshTongji() {
      if (!this.sharingNo) {
        return;
      }
      console.log("get tongji data.");
    }
  }
};
</script>

<style lang="less">
.sharing-modal {
  padding-left: 10px;
  padding-right: 5px;
  .modal-info {
    margin-top: 0.2em;
    margin-bottom: 0.65em;
  }
}
.tongji {
  padding-left: 10px;
  padding-right: 5px;
}
</style>
