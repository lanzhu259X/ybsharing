<template>
    <div>
        <my-title title="分享页列表"></my-title>
        <Row style="margin-bottom: 10px;">
          <i-col span="16">
            <Row type="flex" justify="start" style="padding-left: 7px;">
              <i-input v-model="searchValue" placeholder="分享标题/分享编号" style="width: 200px;"/>
            </Row>
          </i-col>
          <i-col span="8">
            <Row type="flex" justify="end">
              <Button type="primary" icon="search" @click="reloadListData" :loading="dataLoading">查询</Button>
            </Row>
          </i-col>
        </Row>
        <Table highlight-row :columns="tabColumns" :data="tabData"
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
      tabColumns: [
        {
          title: "分享编号",
          width: 145,
          key: "sharingNo"
        },
        {
          title: "标题",
          minWidth: 150,
          key: "sharingTitle"
        },
        {
          title: "状态",
          width: 130,
          key: "status",
          render: (h, params) => {
            let status = params.row.status;
            let label = "";
            let color = "";
            if (status === "NORMAL") {
              label = "分享中";
              color = "#19be6b";
            } else if (status === "DOWN") {
              label = "下架";
              color = "#ed3f14";
            }
            return h(
              "Tag",
              {
                props: {
                  type: "dot",
                  color: color
                }
              },
              label
            );
          }
        },
        {
          title: "操作",
          key: "action",
          width: 160,
          render: (h, params) => {
            let self = this;
            return h("ButtonGroup", { props: { size: "small" } }, [
              h(
                "Button",
                {
                  props: {
                    type: "success",
                    icon: "eye"
                  },
                  on: {
                    click: () => {
                      self.$router.push({
                        name: "sharingdetail",
                        params: { sharingNo: params.row.sharingNo }
                      });
                    }
                  }
                },
                "详情"
              ),
              h(
                "Button",
                {
                  props: {
                    type: "error",
                    icon: "arrow-down-a",
                    disabled: params.row.status === "DOWN"
                  },
                  on: {
                    click: () => {
                      self.setToDown(params.row.id);
                    }
                  }
                },
                "下架"
              )
            ]);
          }
        }
      ]
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
    },
    setToDown(id) {
      console.log(id);
      if (!id || id <= 0) {
        return;
      }
      let self = this;
      this.$Modal.confirm({
        title: "下架确认",
        content: "是否确认下架，下架后不可再上架！",
        width: 280,
        onOk: () => {
          util.ajax
            .delete("/sharing/admin/down/" + id)
            .then(repsonse => {
              self.$Message.success("下架成功");
              self.reloadListData();
            })
            .catch(error => {
              util.errorProcessor(self, error);
            });
        }
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
