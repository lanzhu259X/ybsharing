<template>
    <div style="margin-left: 10px; margin-right:10px;">
      <Row>
        <i-col span="8">
          <Row type="flex" justify="start">
            <h3>{{title}}</h3>
          </Row>
        </i-col>
        <i-col span="16">
          <Row type="flex" justify="end">
            <slot name="extra"></slot>
          </Row>
        </i-col>
      </Row>
        <hr size="1" style="width:100%; margin-top: 3px; margin-bottom: 5px;" />
    </div>
</template>

<script>
export default {
  name: "MyTitle",
  props: {
    title: String
  }
};
</script>

