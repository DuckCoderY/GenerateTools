<template>
  <div class="home">
    <div class="head">
      <div class="title"><i class="title-one">简单好用 又好吃 (0_0)!</i>
        <div class="title-two"><i>——— l_y</i></div>
      </div>
    </div>
    <div class="main">
      <div class="side">
        <Side @tablesConfig="tablesConfig"/>
      </div>
      <div class="context">
        <Context v-if="this.flush" :config="this.config"/>
      </div>
    </div>
  </div>
</template>

<script>
import Side from "@/components/page/side/Side";
import Context from "@/components/page/context/Context";

export default {
  name: 'home',
  components: {
    Side,
    Context
  },
  data() {
    return ({
      flush: true,
      config: {
        ip: "",
        port: "",
        storeName: "",
        userName: "",
        pwd: "",
        address: "",
        tableNames: [],
        codePackage: "",
        dbType: "",
        modelName: "",
      }
    })
  },
  methods: {
    tablesConfig(data) {
      this.config = data
    }
  }
}
</script>

<style lang="less">
@import './Home.less';
</style>
