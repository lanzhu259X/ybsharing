<template>
    <Upload ref="upload"
            :show-upload-list="false"
            :default-file-list="defaultList"
            :on-success="handleSuccess"
            :format="['jpg','jpeg','png', 'gif']"
            :max-size="10240"
            :on-format-error="handleFormatError"
            :on-exceeded-size="handleMaxSize"
            type="drag"
            action=""
            :before-upload="handleUpload"
            style="display: inline-block;width:58px;">
        <div style="width: 58px;height:58px;line-height: 58px;">
            <Icon type="image" size="20"></Icon>
        </div>
    </Upload>
</template>

<script>
import util from "@/libs/util.js";

export default {
  name: "ImageUpload",
  props: {
    namePre: String
  },
  data() {
    return {
      defaultList: []
    };
  },
  methods: {
    handleUpload(file) {
      var self = this;
      var formData = new FormData();
      formData.append("keyPre", this.namePre);
      formData.append("file", file);
      util.ajax
        .post("/file/upload", formData, {
          headers: { "Content-Type": "multipart/form-data" }
        })
        .then(response => {
          if (response.status === 200) {
            self.url = response.data.url;
            self.fileKey = response.data.key;
            let repsEvent = {
              url: self.url,
              fileKey: self.fileKey
            };
            self.$emit("success", repsEvent);
          }
        })
        .catch(error => {
          util.errorProcessor(self, error);
        });
      return false;
    },
    handleSuccess(res, file) {},
    handleFormatError(file) {
      this.$Notice.warning({
        title: "文件格式错误",
        desc: "文件格式必须是jpg或者png"
      });
    },
    handleMaxSize(file) {
      this.$Notice.warning({
        title: "文件大小问题",
        desc: "文件 " + file.name + " 太大,请勿超过10M."
      });
    }
  }
};
</script>

