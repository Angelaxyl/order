<template>
  <div>
    <div style="margin-bottom:5px;">
      <el-input v-model="orderId" placeholder="请输入订单ID" suffix-icon="el-icon-search" style="width: 200px"
                @keyup.enter.native="loadPost"></el-input>
      <el-input v-model="billId" placeholder="请输入账单ID" suffix-icon="el-icon-search" style="width: 200px;margin-left: 5px"
                @keyup.enter.native="loadPost"></el-input>
      <el-select clearable v-model="userId" filterable placeholder="请选择创建人" style="margin-left: 5px">
        <el-option
            v-for="(item,index) in options1"
            :key="index"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select clearable v-model="installerId" filterable placeholder="请选择安装人员" style="margin-left: 5px">
        <el-option
            v-for="(item,index) in options3"
            :key="index"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select clearable v-model="orderState" filterable placeholder="请选择订单状态" style="margin-left: 5px">
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
      <el-table-column prop="id" label="订单ID" width="180">
      </el-table-column>
      <el-table-column prop="billId" label="账单ID" width="180">
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180">
      </el-table-column>
      <el-table-column prop="userName" label="创建人" width="80">
      </el-table-column>
      <el-table-column prop="installerName" label="安装人员" width="100">
      </el-table-column>
      <el-table-column prop="total" label="订单金额" width="80">
      </el-table-column>
      <el-table-column prop="orderStateStr" label="订单状态" width="80">
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button slot="reference" :disabled="scope.row.insuranceUrl == null" size="small" style="margin-left: 5px" type="primary" @click="onCheckInsurance(scope.row)">查看电子保单</el-button>
          <el-button slot="reference" :disabled="scope.row.orderState == '3'? false : true" size="small" style="margin-left: 5px" type="success" @click="onOutDevices(scope.row)">出库</el-button>
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
  name: "OrderManage",
  data() {
    return {
      http:this.$httpUrl,
      tableData:[],
      pageSize:10,
      pageNum:1,
      total:0,
      billId:'',
      orderId:'',
      userId:'',
      installerId:'',
      orderState:'',
      options1:[],
      options3:[],
      options2: [{
        value: '0',
        label: '未支付'
      }, {
        value: '1',
        label: '已支付'
      }, {
        value: '2',
        label: '已上传'
      }, {
        value: '3',
        label: '已审核'
      }, {
        value: '4',
        label: '已出库'
      }, {
        value: '5',
        label: '已结束'
      }],
    }
  },
  mounted() {
    this.axios.get(this.$httpUrl+'/user/getUsers').then((res) => {
      if(res.data.code == 200) {
        this.options1 = res.data.data
      }
    }),
    this.axios.get(this.$httpUrl+'/user/getAllInstallers').then((res) => {
       if(res.data.code == 200) {
          this.options3 = res.data.data
       }
    })
  },
  methods:{
    onCheckInsurance(row){
      window.open(this.$httpUrl+row.insuranceUrl)
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
    resetParam(){
      this.orderId=''
      this.billId=''
      this.userId=''
      this.installerId=''
      this.orderState=''
    },
    onOutDevices(row){
      let orderId=row.id
      let adminId = JSON.parse(sessionStorage.getItem('CurUser')).id
      this.axios.get(this.$httpUrl+'/order/outDevices?orderId='+orderId+'&adminId='+adminId)
          .then((res) =>{
        if(res.data.code == 200){
          this.loadPost()
          this.$axios.get(this.$httpUrl+'/order/showPoint1').then(res => {
            this.$store.commit('setPoint1',res.data.data)
          })
          this.$axios.get(this.$httpUrl+'/order/showPoint2').then(res => {
            this.$store.commit('setPoint2',res.data.data)
          })
        }else {
          alert('出库失败！请检查订单状态')
        }
      })
    },
    getOrderState(state){
      if(state == 0){
        return'未支付'
      }else if(state == 1){
        return'已支付'
      }else if (state == 2){
        return'已上传'
      }else if (state == 3){
        return'已审核'
      }else if(state == 4){
        return'已出库'
      }else if(state == 5){
        return'已结束'
      }
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/order/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          orderId:this.orderId,
          billId:this.billId,
          userId:this.userId,
          installerId: this.installerId,
          orderState:this.orderState,
        }
      }).then(res=>res.data).then(res=>{
        if(res.code==200){
          this.tableData=res.data
          this.total=res.total
          this.tableData.forEach(order=>{
            order.orderStateStr = this.getOrderState(order.orderState)
          })
        }else{
          alert('获取数据失败')
        }
      })
    }
  },
  beforeMount() {
    this.loadPost()
  }
}
</script>

<style scoped>

</style>