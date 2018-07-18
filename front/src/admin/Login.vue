<style lang="less">
@import "../libs/common.less";
</style>

<template>
    <div class="login" :style="{ backgroundImage: 'url(' + bgImage + ')' }">
        <Row type="flex" justify="center" class="login-con">
            <Card style="width: 300px;">
                <p slot="title">Welcome to YIBAN-Sharing</p>
                <div class="form-con">
                    <i-form ref="loginForm" :model="form" :rules="rules">
                        <FormItem prop="username">
                            <i-input v-model="form.username" placeholder="请输入手机号">
                                <span slot="prepend">
                                    <Icon :size="16" type="iphone"></Icon>
                                </span>
                            </i-input>
                        </FormItem>
                        <FormItem prop="password">
                            <i-input type="password" v-model="form.password" placeholder="请输入密码">
                                <span slot="prepend">
                                    <Icon :size="14" type="locked"></Icon>
                                </span>
                            </i-input>
                        </FormItem>

                        <FormItem>
                            <Button @click="handleSubmit" type="primary" long :loading="loading">登录</Button>
                        </FormItem>
                        <Alert type="error" v-show="loginResponse" show-icon>{{ loginResponse }}</Alert>
                        <Row>
                            <i-col span="12">
                                <Checkbox v-model="form.remember">记住我</Checkbox>
                            </i-col>
                            <i-col span="12">
                                <a style="float:right" @click="toRegister">新用户注册</a>
                            </i-col>
                        </Row>
                    </i-form>
                </div>
            </Card>
        </Row>
    </div>
</template>

<script>
import Cookies from "js-cookie";
import util from "@/libs/util.js";
import Qs from "qs";

export default {
  name: "login",
  data() {
    return {
      bgImage:
        "https://cn.bing.com/az/hprichbg/rb/Nyala_ZH-CN13349334824_1920x1080.jpg",
      form: {
        username: Cookies.get("user"),
        password: "",
        remember: false
      },
      loading: false,
      loginResponse: "",
      rules: {
        username: [
          { required: true, message: "手机号不能为空", trigger: "blur" }
        ],
        password: [{ required: true, message: "密码不能为空", trigger: "blur" }]
      }
    };
  },
  methods: {
    handleSubmit() {
      var self = this;
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          var data = Qs.stringify(this.form);
          this.loading = true;
          util.ajax
            .post("/login", data, {
              headers: { "Content-Type": "application/x-www-form-urlencoded" }
            })
            .then(function(response) {
              console.log(response);
              self.loading = false;
              let result = response.data;
              let userDetail = result ? result.userDetail : "";
              let jwt = result ? result.jwt : "";
              if (userDetail && jwt) {
                self.$store.commit("setToken", jwt);
                self.$store.commit("setUserDetail", userDetail);
                Cookies.set("user", userDetail.phone);
                if (userDetail.avatarUrl) {
                  self.$store.commit(
                    "setAvator",
                    userDetail.avatarUrl + "?x-oss-process=style/resize200"
                  );
                } else {
                  //default avator
                  self.$store.commit(
                    "setAvator",
                    "https://yibanerp.oss-cn-shanghai.aliyuncs.com/6/21/20180619_9V5ffK3uK1o7gZcSV42A.jpg?x-oss-process=style/resize200"
                  );
                }
                self.$router.push({
                  name: "manager"
                });
              } else {
                self.loginResponse = "系统异常, 请联系技术人员";
              }
            })
            .catch(function(error) {
              console.log(error);
              self.loading = false;
              let result = error.response ? error.response.data : "";
              if (result && result.message) {
                console.log(result);
                self.loginResponse = result.message;
              } else {
                self.loginResponse = "登录异常";
              }
            });
        }
      });
    },
    toRegister() {
      this.$router.push("/register");
    }
  }
};
</script>

<style lang="less">
.login {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  text-align: center;
  &-con {
    padding-top: 50px;
    &-header {
      font-size: 16px;
      font-weight: 300;
      text-align: center;
      padding: 30px 0;
    }
    .form-con {
      padding: 10px 0 0;
    }
    .login-tip {
      font-size: 10px;
      text-align: center;
      color: #c3c3c3;
    }
  }
}
</style>
