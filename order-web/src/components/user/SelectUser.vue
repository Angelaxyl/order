<template>
  <div>
    <div style="margin-bottom:5px;">
      <el-input v-model="name" placeholder="请输入名字" suffix-icon="el-icon-search" style="width: 200px"
                @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="gender" filterable placeholder="请选择性别" style="margin-left: 5px">
        <el-option
            v-for="item in genders"
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
              highlight-current-row
              @current-change="selectCurrentChange"
    >
      <el-table-column prop="userName" label="账号" width="180">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="180">
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="80">
      </el-table-column>
      <el-table-column prop="gender" label="性别" width="80">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.gender === 1 ? 'primary' : 'danger'"
              disable-transitions>{{scope.row.gender === 1 ? '男' : '女'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="role" label="角色" width="120">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.role === 0? 'danger' : (scope.row.role === 1? 'primary' : 'success')"
              disable-transitions>{{scope.row.role === 0? '管理员' :
              (scope.row.role === 1? '用户' : '安装人员')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话">
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
  name: "SelectUser",
  data() {
    let checkAge = (rule, value, callback) => {
      if(value>150){
        callback(new Error('年龄输⼊过⼤'));
      }else{
        callback();
      }
    };
    let checkDuplicate =(rule,value,callback)=>{
      if(this.form.id){
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/user/findByUserName?userName="+this.form.userName).then(res=>res.data).then(res=>{
        if(res.code!=200){
          callback()
        }else{
          callback(new Error('账号已经存在'));
        }
      })
    };
    return {
      tableData:[],
      pageSize:5,
      pageNum:1,
      total:0,
      name:'',
      gender:'',
      genders:[
        {
          value: '1',
          label: '男'
        }, {
          value: '0',
          label: '女'
        }
      ],
      centerDialogVisible:false,
      form:{
        id:'',
        userName:'',
        name:'',
        password:'',
        age:'',
        phone:'',
        gender:'0',
        role:'1'
      },
      rules: {
        userName: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 8, message: '长度在 3 到 8 个字符', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入名字', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '请输⼊年龄', trigger: 'blur'},
          {min: 1, max: 3, message: '⻓度在 1 到 3 个位', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/,message: '年龄必须为正整数字',trigger: "blur"},
          {validator:checkAge,trigger: 'blur'}
        ],
        phone: [
          {required: true,message: "⼿机号不能为空",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输⼊正确的⼿机号码", trigger:
                "blur"}
        ]
      }
    }
  },
  methods:{
    resetForm(formName) {
      this.$refs.form.resetFields();
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
        console.log(res)
      })
    },
    resetParam(){
      this.name=''
      this.gender=''
    },
    selectCurrentChange(val) {
      this.$emit("doSelectUser",val)
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/user/listPageC1',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          gender:this.gender,
          role:'1'
        }
      }).then(res=>res.data).then(res=>{
        console.log(res)
        if(res.code==200){
          this.tableData=res.data
          this.total=res.total
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