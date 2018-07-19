
<template>
    <div class="admin-head">
        <Row >
            <i-col span="10">
                <Row type="flex" justify="start">
                    <img class="logo-image" src="../image/logo.png" />
                </Row>
            </i-col>
            <i-col span="14">
                <Row type="flex" justify="end" align="middle" class="user-dropdown">
                    <Dropdown transfer trigger="click" @on-click="handleClickUserDropdown">
                        <a href="javascript:void(0)">
                            <Avatar :src="avatorPath" style="margin-left:10px;"></Avatar>
                            <p>{{userName}}</p>
                        </a>
                        <DropdownMenu slot="list">
                            <DropdownItem name="ownSpace">个人中心</DropdownItem>
                            <DropdownItem name="loginout" divided>退出登录</DropdownItem>
                        </DropdownMenu>
                    </Dropdown>
                </Row>
            </i-col>
        </Row>
    </div>
</template>

<script>
import util from "@/libs/util.js";

export default {
  name: "AdminHead",
  data() {
    return {};
  },
  computed: {
    avatorPath() {
      return this.$store.state.app.avator;
    },
    userName() {
      let userDetail = this.$store.state.user.userDetail;
      if (userDetail && userDetail.userName) {
        return userDetail.userName;
      } else {
        let phone = userDetail ? userDetail.phone : "";
        if (phone) {
          return "**" + phone.substring(phone.length - 4);
        }
        return "Amdin";
      }
    }
  },
  methods: {
    handleClickUserDropdown(name) {
      if (name === "ownSpace") {
        console.log("go to user center setting page.");
        this.$router.push({
          name: "ownspace"
        });
      } else if (name === "loginout") {
        // 退出登录
        this.$store.commit("logout", this);
        var self = this;
        util.ajax
          .get("/logoff")
          .then(function(response) {
            if (response.status === 200) {
              self.$Notice.info({ title: "成功退出登录", duration: 2 });
            }
          })
          .catch(function(error) {
            util.errorProcessor(self, error);
          });
        this.$router.push({
          name: "login"
        });
      }
    }
  }
};
</script>

<style lang="less">
.admin-head {
  height: 250px;
  background-color: #1c2438;
  width: 100%;
  .logo-image {
    height: 100%;
  }
  .user-dropdown {
    height: 100%;
    width: 250px;
  }
}
</style>


