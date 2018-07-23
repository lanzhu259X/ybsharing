<template>
    <div>
        <Row type="flex" justify="center" v-if="isDown" >
            <h2 style="padding-top:1em; padding-bottom: 2em;">分享内容已下架或者获取分享内容信息失败！</h2>
        </Row>
        <div v-else class="content">
            <div>
                <img width="100%" height="200" v-if="sharingModal.headImage" class="head-image" :src="sharingModal.headImage" />
            </div>
            <Row type="flex" justify="center">
                <p class="sharing-title">{{sharingModal.sharingTitle}}</p>
            </Row>
            <Row v-if="sharingModal.sharingContent" type="flex" justify="center">
                <p class="sharing-content">{{sharingModal.sharingContent}}</p>
            </Row>
            <Row type="flex" justify="center" style="margin-top:10px;">
                <hr size="1" style="width: 50%; height: 2px; border: none; background-color: #bbbec4" />
            </Row>
            <div v-if="sharingModal.modalBodyList && sharingModal.modalBodyList.length > 0">
                <Row type="flex" justify="center">
                    <p class="record-title">登记信息</p>
                </Row>
                <Row type="flex" justify="center" v-for="(item, index) in sharingModal.modalBodyList" :key="index">
                    <div class="record-content">
                        <span style="color: #ed3f14;" >{{item.required ? "*" : `&nbsp;`}}</span>
                        <i-input style="width: 95%;" clearable v-if="!item.selector" v-model="item.value" :placeholder="item.name">
                        </i-input>
                        <Select style="width: 95%;" clearable v-else v-model="item.vaule" :placeholder="item.name">
                            <Option v-for="(optionItem, oIndex) in item.options" :key="oIndex" :value="optionItem">{{optionItem}}</Option>
                        </Select>
                    </div>
                </Row>
                <Row v-if="submitError" type="flex" justify="center" >
                    <Alert  type="warning" show-icon>
                        <p style="color: #ed3f14">{{submitError}}</p>
                    </Alert>
                </Row>
                <Row type="flex" justify="center">
                    <Button style="width: 200px;" type="success" icon="checkmark" :loading="submitLoading" @click="submitHandle">提交信息</Button>
                </Row>
            </div>
            <Spin size="large" fix v-if="spinShow"></Spin>
        </div>
        <div class="footer">
            <company-desc></company-desc>
        </div>
    </div>
</template>

<script>
import util from "@/libs/util.js";
import CompanyDesc from "@/components/CompanyDesc.vue";

export default {
  name: "DefaultSharing",
  components: { CompanyDesc },
  data() {
    return {
      isDown: false,
      spinShow: false,
      sharingNo: "",
      sharingModal: {},
      submitError: "",
      submitLoading: false
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
      console.log(this.sharingNo);
      if (!this.sharingNo) {
        this.$Message.error("分享内容获取失败");
        this.isDown = true;
        return;
      }
      // 获取分享模本信息
      this.loadSharingModal();
    },
    loadSharingModal() {
      this.isDown = false;
      this.spinShow = true;
      util.ajax
        .get("/sharing/out/sharingmodal/" + this.sharingNo)
        .then(response => {
          this.spinShow = false;
          this.sharingModal = response.data;
          if (this.sharingModal.sharingTitle) {
            window.document.title = this.sharingModal.sharingTitle;
          }
          if (!this.sharingModal || this.sharingModal.status !== "NORMAL") {
            this.isDown = true;
          }
        })
        .catch(error => {
          this.spinShow = false;
          this.isDown = true;
          util.errorProcessor(this, error);
        });
    },
    submitHandle() {
      this.submitError = "";
      let modalData = this.sharingModal.modalBodyList;
      if (!modalData || modalData.length <= 0) {
        this.submitError = "获取提交信息失败";
        return;
      }
      //validate required params
      for (let i = 0; i < modalData.length; i++) {
        let item = modalData[i];
        if (item.required && (!item.value || !item.value.trim())) {
          this.submitError = item.name + "为必输项!";
          return;
        }
      }
      let reqData = {
        recordTicket: this.sharingModal.recordTicket,
        modalId: this.sharingModal.id,
        sharingNo: this.sharingModal.sharingNo,
        modalBodyList: modalData
      };
      console.log(reqData);
      this.submitLoading = true;
      util.ajax
        .post("/sharing/out/" + this.sharingNo + "/submit", reqData)
        .then(response => {
          this.submitLoading = false;
          this.$Message.success("信息提交成功.");
        })
        .catch(error => {
          this.submitLoading = false;
          let data = error.response.data;
          if (data && data.errorMessage) {
            this.submitError = data.errorMessage;
          } else {
            this.submitError = "未知异常，请刷新页面重试.";
          }
        });
    }
  }
};
</script>

<style lang="less" scoped>
.head-image {
  width: 100%;
}

.sharing-title {
  font-size: 2.3em;
  font-weight: 580;
  margin-top: 0.5em;
  margin-bottom: 0.3em;
}

.sharing-content {
  padding-right: 25px;
  padding-left: 25px;
  font-size: 1.3em;
  font-weight: 450;
  color: #657180;
}

.record-title {
  font-size: 1.55em;
  font-weight: 540;
  margin-top: 1em;
  margin-bottom: 0.3em;
}

.record-content {
  margin-bottom: 1.2em;
  width: 320px;
}
</style>

