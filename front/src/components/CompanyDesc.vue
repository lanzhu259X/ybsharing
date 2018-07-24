<template>
    <div class="company-body">
        <Row type="flex" justify="center" style="margin-bottom: 0.8em;">
            <h3 class="company-title">— 医伴金服 —</h3>
        </Row>
        <Row type="flex" justify="center">
            <p class="company-desc">
                医伴金服于深圳前海注册成立，旗下拥有保理、金融服务、投资、征信和健康管理等五家子公司，是国内首家
                专注于医疗全产业链的金融科技服务平台。
            </p>
            <p class="company-desc" style="margin-top: 0.3em;">
                公司致力于为医院、医疗厂家、代理商和医疗消费者提供全方位金融服务，同时通过大数据征信以及风控科技模型为
                银行、信托等金融机构提供优质医疗信贷资产等。全力打造一流的医疗金融科技服务平台，做行业细分领域的金融领导者。
            </p>
        </Row>

        <Row type="flex" justify="center" style="margin-top: 2em;">
            <img src="https://mp.weixin.qq.com/mp/qrcode?scene=10000004&size=102&__biz=MzI2MjY0MTAyNA==&mid=100000058&idx=1&sn=216fd75c700b684119dd2911a5612fb2" height="100" width="100">
        </Row>

        <Row type="flex" justify="center" style="margin-top: 2em;">
            <img width="146" height="54"  src="https://yibansharing.oss-cn-shanghai.aliyuncs.com/image/default-1807241609.png?Expires=1847779767&OSSAccessKeyId=LTAIULFYwI6R0YNJ&Signature=mO6jUtefPc4CSBTPqBm%2B6TD%2BV8M%3D"/>
        </Row>
    </div>
</template>

<script>
export default {
  name: "CompanyDesc"
};
</script>

<style lang="less">
.company-body {
  padding-left: 25px;
  padding-right: 25px;
  margin-top: 3em;
}
.company-title {
  font-size: 2.5em;
  font-weight: 600;
  color: #464c5b;
}
.company-desc {
  color: #9ea7b4;
  font-size: 1.2em;
  font-weight: 400;
}
</style>


