
<template>
    <div class="admin-head">
        <Row >
            <!-- <i-col span="8">
                <Row type="flex" justify="start">
                    <img class="logo-image" src="../image/logo.png" />
                </Row>
            </i-col> -->
            <i-col span="16" class="page-info">
              <Row type="flex" justify="start" align="middle" :gutter="10">
                <a href="javascript:void(0)" @click="gotoHome"><Icon type="home" size="50" class=""></Icon></a>
                <span class="page-info-title">{{title}}</span>
              </Row>
            </i-col>
            <i-col span="8">
                <Row type="flex" justify="end" align="middle" class="user-dropdown">
                    <Dropdown transfer trigger="click" @on-click="handleClickUserDropdown" class="user-drop">
                        <a href="javascript:void(0)" >
                            <Avatar  :src="avatorPath" class="user-avatar" />
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
    return {
      title: ""
    };
  },
  mounted() {
    this.init();
  },
  computed: {
    avatorPath() {
      console.log(localStorage.avator);
      return localStorage.avator;
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
    init() {
      console.log(this.$route.meta.title);
      this.title = this.$route.meta.title ? this.$route.meta.title : "";
    },
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
              self.$Message.success("成功退出登录");
            }
          })
          .catch(function(error) {
            util.errorProcessor(self, error);
          });
        this.$router.push({
          name: "login"
        });
      }
    },
    gotoHome() {
      this.$router.push({
        name: "home"
      });
    }
  }
};
</script>

<style lang="less">
.admin-head {
  height: 100%;
  background-color: #1c2438;
  width: 100%;
  .logo-image {
    height: 80px;
    margin: 5px 10px;
  }
  .page-info {
    height: 100%;
    margin-top: 20px;
    padding-left: 10px;
    .page-info-title {
      margin-left: 10px;
      font-size: 1.2em;
      font-weight: bolder;
      color: #fff;
    }
  }
  .user-dropdown {
    height: 90px;
    .user-drop {
      margin: 2px 10px;
    }
    .user-avatar {
      width: 60px;
      height: 60px;
      border: 3px solid;
      border-radius: 50%;
      border-color: #fff;
    }
  }
}
</style>


