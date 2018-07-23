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
            <div>
                <img width="100%" height="200" v-if="sharingModal.headImage" class="head-image" :src="sharingModal.headImage" />
            </div>
            <Row type="flex" justify="start">
                <strong>分享模板编号:</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
                <p>{{sharingModal.sharingNo}}</p>
            </Row>
            <Row type="flex" justify="start">
                <strong>标题:</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
                <span style="color: #ed3f14">*</span><i-input style="width: 97%;" v-model="sharingModal.sharingTitle" placeholder="请输入分享标题" />
            </Row>
            <Row type="flex" justify="start">
                <strong>副标题:</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
              <span style="color: #ed3f14">*</span><i-input style="width: 97%;" v-model="sharingModal.sharingSubTitle" placeholder="请输入分享副标题" />
            </Row>

            <Row type="flex" justify="start">
                <strong>规则/描述:</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
              &nbsp;<i-input style="width: 97%;" type="textarea" :rows="3" v-model="sharingModal.sharingContent" placeholder="请输入分享规则或内容描述" />
            </Row>

            <Row type="flex" justify="end" class="modal-info" style="margin-right:6px;">
              <Button size="small" type="success" icon="checkmark" @click="updateModalHandle">保存修改</Button>
            </Row>

            <Row type="flex" justify="start">
                <strong>分享头图片:(宽度100%,高度200px)</strong>
            </Row>
            <Row type="flex" justify="start" class="modal-info">
              <image-upload :namePre="filePreName" @success="imageHeadUpload"></image-upload>
            </Row>

            <my-title title="登记记录" style="margin-top: 25px"></my-title>
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
            <Spin size="large" fix v-if="tongjiLoading"></Spin>
            <Row :gutter="10">
              <i-col :xs="24" :sm="12" :md="8" :lg="6" style="margin-bottom: 15px;">
                <info-card title="总访问量" icon="pinpoint" color="#2b85e4" unit="次"
                  :number="sharingCount.totalClick ? sharingCount.totalClick : 0">
                </info-card>
              </i-col>
              <i-col :xs="24" :sm="12" :md="8" :lg="6" style="margin-bottom: 15px;">
                <info-card title="总登记量" icon="disc" color="#19be6b" unit="次"
                  :number="sharingCount.totalRecord ? sharingCount.totalRecord : 0">
                </info-card>
              </i-col>
              <i-col :xs="24" :sm="12" :md="8" :lg="6" style="margin-bottom: 15px;">
                <info-card title="未处理量" icon="chatbubble-working" color="#ff9900" unit="条"
                  :number="sharingCount.totalUnprocess ? sharingCount.totalUnprocess : 0">
                </info-card>
              </i-col>
              <i-col :xs="24" :sm="12" :md="8" :lg="6" style="margin-bottom: 15px;">
              </i-col>
            </Row>
        </div>

        <Modal @on-visible-change="processModalChnage" v-model="processModalShow" :title="processTitle" :mask-closable="false" :footerHide="true" width="80">
          <div>
            <i-input type="textarea" v-model="processResult" :row="3" placeholder="请输入处理留言/结果"/>
            <Row type="flex" justify="end" style="margin-top: 5px;margin-bottom: 10px;">
              <ButtonGroup size="small">
                <Button type="error" :loading="processLoading" @click="processHandle('UNPROCESS')">未处理完成</Button>
                <Button type="warning" :loading="processLoading" @click="processHandle('PROCESSING')">处理中</Button>
                <Button type="success" :loading="processLoading" @click="processHandle('PROCESSED')">处理完成</Button>
              </ButtonGroup>
            </Row>

            <div v-for="(item, index) in processList" :key="index">
              <Row>
                <i-col span="18">
                  <Row type="flex" justify="start"><strong>{{item.showTitle}}</strong></Row>
                </i-col>
                <i-col span="6">
                  <Row type="flex" justify="end">
                    <strong>
                      {{item.handleStatus === "UNPROCESS" ? "未处理" : (item.handleStatus === "PROCESSING" ? "处理中" : (item.handleStatus === "PROCESSED" ? "处理完成" : ""))}}
                    </strong>
                  </Row>
                </i-col>
              </Row>
              <hr size="1" style="width: 99%; height: 1px; border: none; background-color: #bbbec4" />
              <Row type="flex" justify="start" style="margin-top:0.5em; margin-bottom: 1.2em;">
                <p>{{item.processResult}}</p>
              </Row>
            </div>
          </div>
        </Modal>
    </div>
</template>

<script>
import util from "@/libs/util.js";
import moment from "moment";
import MyTitle from "@/components/MyTitle.vue";
import ImageUpload from "@/components/ImageUpload.vue";
import InfoCard from "@/components/InfoCard.vue";

export default {
  name: "SharingDetail",
  components: {
    MyTitle,
    ImageUpload,
    InfoCard
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
      pageSize: 20,
      processModalShow: false,
      processTitle: "",
      processList: [],
      processRecordId: "",
      processLoading: false,
      processResult: "",
      filePreName: "",
      tongjiLoading: false,
      sharingCount: {}
    };
  },
  mounted() {
    this.init();
  },
  watch: {
    $route: "init"
  },
  methods: {
    init() {
      this.sharingNo = this.$route.params.sharingNo;
      if (!this.sharingNo) {
        console.log("sharingNo get fail.");
        return;
      }
      this.filePreName = this.sharingNo + "-head";
      let self = this;
      util.ajax
        .get("/sharing/admin/detail/" + this.sharingNo + "/nocache")
        .then(response => {
          this.sharingModal = response.data;
          this.setModalTableColumn();
          this.loadSharingRecords();
          this.refreshTongji();
        })
        .catch(error => {
          util.errorProcessor(self, error);
        });
    },

    updateModalHandle() {
      if (!this.sharingModal) {
        return;
      }
      if (
        !this.sharingModal.sharingTitle ||
        !this.sharingModal.sharingTitle.trim()
      ) {
        this.$Message.info("模板标题不能为空");
        return;
      }
      if (
        !this.sharingModal.sharingSubTitle ||
        !this.sharingModal.sharingSubTitle.trim()
      ) {
        this.$Message.info("模板付标题不能为空");
        return;
      }
      util.ajax
        .post("/sharing/admin/modal/update", this.sharingModal)
        .then(response => {
          this.$Message.success("模板数据修改成功");
        })
        .catch(error => {
          util.errorProcessor(this, error);
        });
    },

    imageHeadUpload(data) {
      console.log(data);
      if (data && data.url && data.fileKey) {
        //修改当前模板的信息
        this.sharingModal.headImage = data.url;
        this.sharingModal.headFileKey = data.fileKey;
        util.ajax
          .post("/sharing/admin/modal/update", this.sharingModal)
          .then(response => {
            this.$Message.success("头图片更新成功");
          })
          .catch(error => {
            util.errorProcessor(this, error);
          });
      }
    },

    setModalTableColumn() {
      if (
        !this.sharingModal ||
        !this.sharingModal.modalBodyList ||
        this.sharingModal.modalBodyList.length <= 0
      ) {
        return;
      }
      let bodyList = this.sharingModal.modalBodyList;
      let recordColumns = [
        {
          title: "序号",
          type: "index",
          width: 60
        },
        {
          title: "处理状态",
          key: "handleStatus",
          width: 120,
          render: (h, params) => {
            let handleStatus = params.row.handleStatus;
            let label = "";
            let color = "";
            if (handleStatus === "UNPROCESS") {
              label = "未处理";
              color = "#ed3f14";
            } else if (handleStatus === "PROCESSING") {
              label = "处理中";
              color = "#ff9900";
            } else if (handleStatus === "PROCESSED") {
              label = "已处理";
              color = "#19be6b";
            }
            return h("Tag", { props: { color: color } }, label);
          }
        },
        {
          title: "登记时间",
          key: "createdTime",
          width: 150,
          render: (h, params) => {
            let createdTimeStr = params.row.createdTime
              ? moment(params.row.createdTime).format("YYYY-MM-DD HH:mm")
              : "";
            return h("span", createdTimeStr);
          }
        }
      ];
      for (let i = 0; i < bodyList.length; i++) {
        let body = bodyList[i];
        let item = {
          title: body.name,
          key: body.name,
          minWidth: 120,
          render: (h, params) => {
            let modalArr = params.row.modalBodyList;
            let label = "";
            if (modalArr && modalArr.length > 0) {
              for (let i = 0; i < modalArr.length; i++) {
                if (modalArr[i].name === body.name) {
                  label = modalArr[i].value;
                  break;
                }
              }
            }
            return h("span", label);
          }
        };
        recordColumns.push(item);
      }

      recordColumns.push({
        title: "操作",
        key: "action",
        width: 120,
        render: (h, params) => {
          return h("ButtonGroup", { props: { size: "small" } }, [
            h(
              "Button",
              {
                props: { type: "success" },
                on: {
                  click: () => {
                    this.openProcessModal(params.row);
                  }
                }
              },
              "处理"
            )
          ]);
        }
      });
      this.recordColumns = recordColumns;
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
      self.recordLoading = true;
      util.ajax
        .post("/sharing/admin/" + this.sharingNo + "/records", requestData)
        .then(response => {
          console.log(response.data);
          self.recordLoading = false;
          self.recordData = response.data.data;
          self.totalCount = response.data.totalCount;
        })
        .catch(error => {
          self.recordLoading = false;
          util.errorProcessor(self, error);
        });
    },

    openProcessModal(record) {
      this.processTitle = record.sharingNo;
      this.processModalShow = true;
      this.processRecordId = record.id;
      this.reloadProcessList(record.id);
    },

    processModalChnage(data) {
      if (!data) {
        // 关闭时刷新列表数据
        this.loadSharingRecords();
      }
    },

    reloadProcessList(recordId) {
      let self = this;
      self.processLoading = true;
      util.ajax
        .get("/sharing/admin/record/process/" + recordId)
        .then(response => {
          self.processLoading = false;
          self.processList = response.data;
        })
        .catch(error => {
          self.processLoading = false;
          util.errorProcessor(self, error);
        });
    },

    processHandle(handleStatus) {
      if (this.processRecordId === null || !handleStatus) {
        return;
      }
      let requestData = {
        recordId: this.processRecordId,
        handleStatus: handleStatus,
        processResult: this.processResult
      };
      this.processLoading = true;
      util.ajax
        .post("/sharing/admin/record/process/add", requestData)
        .then(response => {
          this.processLoading = false;
          this.processList = response.data;
        })
        .catch(error => {
          this.processLoading = false;
          util.errorProcessor(this, error);
        });
    },

    sharingHandle() {
      console.log("click sharing handle");
      this.$router.push({
        name: "defaultsharing",
        params: { sharingNo: this.sharingNo }
      });
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
      this.tongjiLoading = true;
      util.ajax
        .get("/sharing/admin/count/" + this.sharingNo)
        .then(response => {
          this.tongjiLoading = false;
          this.sharingCount = response.data;
        })
        .catch(error => {
          this.tongjiLoading = false;
          util.errorProcessor(this, error);
        });
    }
  }
};
</script>

<style lang="less">
.sharing-modal {
  padding-left: 5px;
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
