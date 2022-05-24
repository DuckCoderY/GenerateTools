<template>
  <div>
    <div class="main_table main_style">
      <el-table class="table" :data="conf.tableNames" max-height="570px"
                ref="multipleTable"
                tooltip-effect="dark" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="155"></el-table-column>
        <el-table-column prop="tableName" label="数据库表名" width="490"></el-table-column>
        <el-table-column prop="tableComment" label="数据库表注释" width="550"></el-table-column>
      </el-table>
    </div>
    <div class="table-foot">
      <el-row>
        <el-col :span="3">
          <span style=" margin-left: 60px">导出的包名：</span>
        </el-col>
        <el-col :span="3">
          <el-input size="mini" placeholder="请输入导出的包名" v-model="conf.codePackage" clearable></el-input>
        </el-col>
        <el-col :span="3">
          <span style=" margin-left: 60px">项目的模块名：</span>
        </el-col>
        <el-col :span="3">
          <el-input size="mini" placeholder="请输入项目的模块名" v-model="conf.modelName" clearable></el-input>
        </el-col>
        <el-col :span="3">
          <span style=" margin-left: 60px">表前缀字段：</span>
        </el-col>
        <el-col :span="3">
          <el-input size="mini" placeholder="表不需要前缀" v-model="conf.tablePrefix" clearable></el-input>
        </el-col>
        <el-col :span="3">
          <el-button size="mini" @click="exportAll">导出</el-button>
        </el-col>
      </el-row>
    </div>
  </div>

</template>

<script>

export default {
  name: "Context",
  methods: {
    handleSelectionChange(val) {
      this.checkTableNames = val;
    },
    convert(data) {
      this.conf.port = data.port,
          this.conf.ip = data.ip,
          this.conf.storeName = data.storeName,
          this.conf.userName = data.userName,
          this.conf.pwd = data.pwd,
          this.conf.address = data.address,
          this.conf.tableNames = data.tableNames,
          this.conf.dbType = data.dbType
    },
    exportAll() {
      if (this.checkTableNames.length <= 0) {
        this.$message({
          message: '请选择导出数据的表！！！',
          type: 'warning'
        });
      }
      this.conf.tableNames = []
      this.conf.tableNames = this.checkTableNames
      // eslint-disable-next-line no-undef
      this.$request.postForm("/generate", this.conf, {responseType: "blob"}).then(respone => {
        let blob = respone.data
        let reader = new FileReader()
        reader.readAsDataURL(blob)
        reader.onload = (e) => {
          let a = document.createElement('a')
          a.download = this.conf.storeName + ".zip"
          a.href = e.target.result
          document.body.appendChild(a)
          a.click()
          document.body.removeChild(a)

        }
        this.$message({
          message: '导出成功！',
          type: 'success'
        });
      })

    }
  }
  ,
  data() {
    return {
      conf: {
        ip: "",
        port: "",
        storeName: "",
        userName: "",
        pwd: "",
        address: "",
        tablePrefix: "",
        tableNames: [
          {
            tableName: "",
            tableComment: ""
          }
        ],
        codePackage: "com.rich",
        dbType: "",
        modelName: "model",
      },
      checkTableNames: [],
    }
  }
  ,
  props: {
    config: {
      type: Object
    }
  }
  ,
  watch: {
    config: {
      immediate: true,
      deep: true,
      handler: function () {
        if (this.config !== null) {
          this.convert(this.config);
        }
      }
    }
  }
  ,
}
</script>

<style scoped></style>

<style lang="less">
@import "Context";
</style>

