<template>
    <div style="margin: 10px 10px;">
      <Row>
        <i-col span="8">
          <Row type="flex" justify="start">
            <h3 style="color: #2b85e4">{{title}}</h3>
          </Row>
        </i-col>
        <i-col span="16">
          <Row type="flex" justify="end">
            <slot name="extra"></slot>
          </Row>
        </i-col>
      </Row>
        <hr size="1" style="width:100%; margin-top: 3px; margin-bottom: 5px; height: 2px; border: none; background-color: #2b85e4" />
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

