<style lang="less">
@import "@/libs/common.less";
</style>

<template>
    <div class="login" :style="{ backgroundImage: 'url(' + bgImage + ')' }">
        <div class="login-con">
            <Card :bordered="false">
                <p slot="title">
                    <Icon type="log-in"></Icon>
                    欢迎登录
                </p>
                <div class="form-con">
                    <Form ref="loginForm" :model="form" :rules="rules">
                        <FormItem prop="username">
                            <i-input v-model="form.username" placeholder="请输入用户名/手机号">
                                <span slot="prepend">
                                    <Icon :size="16" type="person"></Icon>
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
                                <Checkbox-group v-model="form.remember">
                                    <Checkbox label="记住我"></Checkbox>
                                </Checkbox-group>
                            </i-col>
                            <i-col span="12">
                                <a style="float:right" @click="toRegister">新用户注册</a>
                            </i-col>
                        </Row>
                    </Form>

                </div>
            </Card>
        </div>
    </div>
</template>

<script>
import util from "@/libs/util.js";

export default {
  name: "login",
  data() {
    return {
      bgImage: ""
    };
  },
  methods: {
    loadBackground: function() {
      var self = this;
      util.ajax
        .get("/login/dynamic-bg", {
          params: {
            format: "js",
            idx: 1,
            n: 10
          }
        })
        .then(function(response) {
          if (response.status === 200) {
            var imageCount = response.data.images.length;
            var idx = Math.floor(Math.random() * imageCount);
            self.bgImage =
              "https://www.bing.com/" + response.data.images[idx].url;
          }
        })
        .catch(function(error) {
          self.bgImage =
            "https://www.bing.com/az/hprichbg/rb/OakTreeMaize_ZH-CN10523296117_1920x1080.jpg";
        });
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
  position: relative;
  &-con {
    position: absolute;
    right: 160px;
    top: 50%;
    transform: translateY(-60%);
    width: 300px;
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



