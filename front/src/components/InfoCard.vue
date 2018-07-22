<template>
    <Card :padding="0">
        <Row style="heigth:100%;">
            <i-col span="8" :style="{backgroundColor: color, color: 'white', padding: '5px 0px'}">
                <Row type="flex" justify="center" align="middle">
                    <Icon :type="icon" size="20"></Icon>
                </Row>
                <Row type="flex" justify="center" align="middle">
                    <span class="title-text">{{ title }}</span>
                </Row>
            </i-col>
            <i-col span="16" :style="{backgroundColor: 'white', color: color, padding: '5px 0px'}">
                <Row type="flex" justify="center" align="middle">
                    <span class="number-text">{{ number }}</span><span class="unit-text">{{ unit }}</span>
                </Row>
            </i-col>
        </Row>
    </Card>
</template>

<script>
export default {
  name: "InfoCard",
  props: {
    title: {
      type: String,
      required: true
    },
    number: {
      type: Number,
      default: 0
    },
    unit: {
      type: String,
      default: ""
    },
    icon: {
      type: String,
      default: "stats-bars"
    },
    color: {
      type: String,
      default: "#19be6b"
    }
  }
};
</script>


<style lang="less" scoped>
.title-text {
  font-size: 12px;
  padding-top: 5px;
  color: #fff;
}

.number-text {
  padding-top: 5px;
  font-size: 25px;
}

.unit-text {
  font-size: 12px;
  padding-top: 10px;
}
</style>

