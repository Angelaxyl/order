<template>
  <div>
    <div style="margin-bottom:5px;">
      <el-input v-model="billId" placeholder="请输入账单ID" suffix-icon="el-icon-search" style="width: 200px"
                @keyup.enter.native="loadPost"></el-input>
      <el-input v-model="orderId" placeholder="请输入订单ID" suffix-icon="el-icon-search" style="width: 200px;margin-left: 5px"
                @keyup.enter.native="loadPost"></el-input>
      <el-select clearable v-model="userId" filterable placeholder="请选择创建人" style="margin-left: 5px">
        <el-option
            v-for="(item,index) in options1"
            :key="index"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select clearable v-model="billState" filterable placeholder="请选择账单状态" style="margin-left: 5px">
        <el-option
            v-for="item in options2"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
      <el-button type="success" style="margin-left: 5px" @click="resetParam">重置</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555'}"
              border
    >
      <el-table-column label="账单图片" width="150">
        <template slot-scope="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="http+scope.row.billUrl"
              :preview-src-list="[http+scope.row.billUrl]">
            <div slot="error" class="image-slot">
              <span>账单图片未上传！</span>
            </div>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="账单ID" width="180">
      </el-table-column>
      <el-table-column prop="orderId" label="订单ID" width="180">
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180">
      </el-table-column>
      <el-table-column prop="userName" label="创建人" width="80">
      </el-table-column>
      <el-table-column prop="total" label="账单金额" width="80">
      </el-table-column>
      <el-table-column prop="billStateStr" label="账单状态" width="80">
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
            <el-button slot="reference" :disabled="scope.row.billState == '1' ? false : true" size="small" style="margin-left: 5px" type="success" @click="checked(scope.row)">核验通过</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 4, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: "BillManage",
  data() {
    return {
      http:this.$httpUrl,
      tableData:[],
      pageSize:4,
      pageNum:1,
      total:0,
      billId:'',
      orderId:'',
      userId:'',
      billState:'',
      options1:[],
      options2: [{
        value: '0',
        label: '未支付'
      }, {
        value: '1',
        label: '已支付'
      }, {
        value: '2',
        label: '审核通过'
      }],
    }
  },
  mounted() {
    this.axios.get(this.$httpUrl+'/user/getUsers').then((res) => {
      if(res.data.code == 200) {
        this.options1 = res.data.data
      }
    })
  },
  methods:{
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
    resetParam(){
      this.billId=''
      this.orderId=''
      this.userId=''
      this.billState=''
    },
    checked(row){
      let billId=row.id
      this.axios.get(this.$httpUrl+'/bill/checked?billId='+billId).then((res) =>{
          if(res.data.code == 200){
            this.loadPost()
            this.$axios.get(this.$httpUrl+'/order/showPoint1').then(res => {
              this.$store.commit('setPoint1',res.data.data)
            })
            this.$axios.get(this.$httpUrl+'/order/showPoint2').then(res => {
              this.$store.commit('setPoint2',res.data.data)
            })
          }else {
            alert('审核失败！请检查您是否上传图片')
          }
        })
    },
    getBillState(state){
      if(state == 0){
        return'未支付'
      }else if(state == 1){
        return'已支付'
      }else if (state == 2){
        return'审核通过'
      }
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/bill/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          billId:this.billId,
          orderId:this.orderId,
          userId:this.userId,
          billState:this.billState,
        }
      }).then(res=>res.data).then(res=>{
        if(res.code==200){
          this.tableData=res.data
          this.total=res.total
          this.tableData.forEach(bill=>{
            bill.billStateStr = this.getBillState(bill.billState)
          })
        }else{
          alert('获取数据失败')
        }
      })
    }
  },
  beforeMount() {
    // this.loadGet();
    this.loadPost()
  }
}
</script>

<style scoped>

</style>