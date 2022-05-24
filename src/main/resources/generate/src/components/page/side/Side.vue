<template>
  <div class="main">
    <el-form class="form-less" ref="form" :model="form" label-width="80px">
      <el-form-item label="DB类型：">
        <el-select size="mini" class="input" v-model="form.dbType" placeholder="请选择数据库类型">
          <el-option label="MYSQL" value="MYSQL"></el-option>
          <el-option label="ORACLE" value="ORACLE"></el-option>
          <el-option label="PGSQL" value="PGSQL"></el-option>
          <el-option label="SQLSERVER" value="SQLSERVER"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="IP地址：">
        <el-input size="mini" class="input" v-model="form.ip"></el-input>
      </el-form-item>
      <el-form-item label="端口：">
        <el-input size="mini" class="input" v-model="form.port"></el-input>
      </el-form-item>
      <el-form-item label="DB名称：">
        <el-input size="mini" class="input" v-model="form.storeName"></el-input>
      </el-form-item>
      <el-form-item label="用户名：">
        <el-input size="mini" class="input" v-model="form.userName"></el-input>
      </el-form-item>
      <el-form-item label="密码：">
        <el-input size="mini" class="input" v-model="form.pwd"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button class="button" size="mini" @click="onSubmit">查询表数据</el-button>
        <el-button class="button" size="mini" @click="reset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

export default {
  name: "Side",
  data() {
    return ({
      form: {
        ip: "192.168.0.0",
        port: "3306",
        storeName: "database",
        userName: "root",
        pwd: "root",
        address: "",
        tableNames: [],
        codePackage: "",
        dbType: "MYSQL",
        modelName: "",
      }
    })
  },

  methods: {
    onSubmit() {
      this.form.tableNames = [];
      // eslint-disable-next-line no-undef
      this.$request.post("/queryList", this.form).then(response => {
        if (response.data.length <= 0) {
          this.$message.error('未查询到任何数据信息！！！');
          return
        }
        this.form.tableNames = response.data
        this.transferHistogramData(this.form)
      })
    },
    reset() {
      this.form = {}
    },
    transferHistogramData(tablesMsg) {
      this.$emit('tablesConfig', tablesMsg);
    }

  }
}
</script>

<style lang="less" scoped>
@import "./Side.less";
</style>
