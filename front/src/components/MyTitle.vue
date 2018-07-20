<template>
    <div style="margin-left: 10px; margin-right:10px;">
        <h3>{{title}}</h3>
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

