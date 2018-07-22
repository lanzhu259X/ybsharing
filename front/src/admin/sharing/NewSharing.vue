<template>
    <div>
        <my-title title="分享信息"></my-title>
        <Form ref="form" :model="formData" :label-width="0" :rules="formRules">
            <Row >
                <FormItem prop="sharingTitle" >
                    <span style="color: #ed3f14">*</span><i-input style="width: 97%;" v-model="formData.sharingTitle" placeholder="请输入分享标题" />
                </FormItem>
            </Row>
            <Row>
                <FormItem prop="sharingSubTitle" >
                    <span style="color: #ed3f14">*</span><i-input style="width: 97%;" v-model="formData.sharingSubTitle" placeholder="请输入分享副标题" />
                </FormItem>
            </Row>
            <Row >
                <FormItem prop="sharingContent" >
                    &nbsp;<i-input style="width: 97%;" type="textarea" :rows="3" v-model="formData.sharingContent" placeholder="请输入分享规则或内容描述" />
                </FormItem>
            </Row>
            <my-title title="登记信息"></my-title>
            <div v-for="(item, index) in dataList" :key="index" class="data-list-row">
              <Row type="flex" justify="start" :gutter="15">
                  <i-col span="15">
                      <i-input v-model="item.name" placeholder="请输入字段描述,eg:姓名" />
                  </i-col>
                  <i-col span="7">
                      <Checkbox v-model="item.required">是否必输</Checkbox>
                  </i-col>
                  <i-col span="2">
                      <a href="javascript:void(0)" @click="removeItem(index)">
                          <Icon color="#ed3f14" size="25" type="minus-circled"></Icon>
                      </a>
                  </i-col>
              </Row>
              <Row v-if="item.selector" type="flex" justify="start" style="margin-top:5px;">
                <i-input v-model="item.selectValues" placeholder="下拉选择框的值, 多个值之间使用';'号分割, eg: 6个月;12个月;24个月"/>
              </Row>
            </div>
        </Form>
        <BottonGroup>
          <Button type="dashed" icon="plus" style="margin-left: 10px; background-color: #fff;" @click="addTextItem">文本输入框</Button>
          <Button type="dashed" icon="plus" style="margin-left: 10px; background-color: #fff;" @click="addSelectItem">下拉选择框</Button>
        </BottonGroup>

        <hr size="2" style="margin-top: 10px; margin-bottom:15px; margin-left:5px; width:97%;" />
        <ButtonGroup style="margin-left: 10px;">
            <Button type="success" icon="checkmark" @click="submitHandle" :loading="submitLoading">确认提交</Button>
            <Button type="primary" icon="loop" @click="resetData">重置</Button>
            <Button type="ghost" icon="reply" @click="gotoHome">返回首页</Button>
        </ButtonGroup>
    </div>
</template>

<script>
import MyTitle from "@/components/MyTitle.vue";
import util from "@/libs/util.js";

export default {
  name: "NewSharing",
  components: {
    MyTitle
  },
  data() {
    return {
      formData: {
        sharingTitle: "",
        sharingSubTitle: "",
        sharingContent: ""
      },
      formRules: {
        sharingTitle: [
          { required: true, message: "分享标题不能为空", trigger: "blur" }
        ],
        sharingSubTitle: [
          { required: true, message: "分享副标题不能为空", trigger: "blur" }
        ]
      },
      dataList: [
        {
          name: "",
          value: "",
          required: false,
          selector: false,
          selectValues: "",
          options: []
        }
      ],
      submitLoading: false
    };
  },
  methods: {
    addTextItem() {
      let item = {
        name: "",
        value: "",
        required: false,
        selector: false,
        selectValues: "",
        options: []
      };
      this.dataList.push(item);
    },
    addSelectItem() {
      let item = {
        name: "",
        value: "",
        required: false,
        selector: true,
        selectValues: "",
        options: []
      };
      this.dataList.push(item);
    },
    removeItem(index) {
      this.dataList.splice(index, 1);
    },
    gotoHome() {
      this.resetData();
      this.$router.push({ name: "home" });
    },
    resetData() {
      this.dataList = [
        {
          name: "",
          value: "",
          required: false,
          selector: true,
          selectValues: "",
          options: []
        }
      ];
      this.formData = {
        sharingTitle: "",
        sharingSubTitle: "",
        sharingContent: ""
      };
      this.$refs.form.resetFields();
    },
    submitHandle() {
      let self = this;
      this.$refs.form.validate(valid => {
        console.log(valid);
        if (!valid) {
          return;
        }
        // validate dataList name is must not empty;
        for (let i = 0; i < self.dataList.length; i++) {
          if (!self.dataList[i].name || !self.dataList[i].name.trim()) {
            self.$Message.info("登记信息的字段描述不能为空!");
            return;
          }
        }
        // submit
        let requestData = {
          sharingTitle: self.formData.sharingTitle,
          sharingSubTitle: self.formData.sharingSubTitle,
          sharingContent: self.formData.sharingContent,
          modalBodyList: self.dataList
        };
        console.log(requestData);
        self.submitLoading = true;
        util.ajax
          .post("/sharing/admin/add", requestData)
          .then(response => {
            self.submitLoading = false;
            self.$Message.success("创建成功.");
            self.resetData();
          })
          .catch(error => {
            console.log(error);
            self.submitLoading = false;
            util.errorProcessor(self, error);
          });
      });
    }
  }
};
</script>

<style lang="less">
.data-list-row {
  margin-bottom: 15px;
  padding-left: 10px;
  padding-right: 10px;
}
</style>


