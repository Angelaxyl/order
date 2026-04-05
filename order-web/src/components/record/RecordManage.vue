<template>
  <div>
    <div style="margin-bottom:5px;">
      <el-input v-model="name" placeholder="请输入设备名字" suffix-icon="el-icon-search" style="width: 200px"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
      <el-button type="success" style="margin-left: 5px" @click="resetParam">重置</el-button>

    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555'}"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
    >
      <el-table-column prop="deviceName" label="设备名字" width="180">
      </el-table-column>
      <el-table-column prop="adminname" label="操作人" width="180">
      </el-table-column>
      <el-table-column prop="username" label="申请人" width="180">
      </el-table-column>
      <el-table-column prop="count" label="数量" width="180">
      </el-table-column>
      <el-table-column prop="createTime" label="操作时间">
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20, 30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "RecordManage",
  data() {
    return {
      user : JSON.parse(sessionStorage.getItem('CurUser')),
      http:this.$httpUrl,
      tableData:[],
      pageSize:10,
      pageNum:1,
      total:0,
      name:'',
      model:'',
      centerDialogVisible:false,
      inDialogVisible:false,
      innerVisible:false,
      currentRow:{},
      tempUser:{},
      adminname:'',
      username:'',
      form:{
        id:'',
        deviceName:'',
        deviceModel:'',
        price:'',
        stock:'',
        introduction:'',
        urls:[],
      },
      fileList:[
      ]
    }
  },
  methods:{
    selectCurrentChange(val) {
      this.currentRow = val;
    },
    handleRemove(file, fileList) {
      this.form.urls = this.form.urls.filter(item => file.url.indexOf(item+'') === -1 )
    },
    resetForm(formName) {
      this.$refs.form.resetFields();
      this.fileList = []
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum=1
      this.pageSize=val
      this.loadPost()
    },
    //翻页
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val
      this.loadPost()
    },
    loadGet(){
      this.$axios.get(this.$httpUrl+'/user/list').then(res=>res.data).then(res=>{
      })
    },
    resetParam(){
      this.name=''
      this.model=''
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/record/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          deviceName:this.name,
          role:this.user.role+'',
          userId:this.user.id+'',
        }
      }).then(res=>res.data).then(res=>{
        if(res.code==200){
          this.tableData=res.data
          this.total=res.total
        }else{
          alert('获取数据失败')
        }
      })
    },
  },
  beforeMount() {
    // this.loadGet();
    this.loadPost()
  }
}
</script>

<style scoped>

</style>