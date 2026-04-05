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
      <el-button type="primary" style="margin-left: 5px" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555'}"
              border
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
      <el-table-column prop="phone" label="电话" width="180">
      </el-table-column>
      <el-table-column prop="operate" label="操作">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 5px;"
          >
            <el-button slot="reference" size="small" type="danger">删除</el-button>
          </el-popconfirm>
          <el-button size="small" style="margin-left: 5px;" type="info" @click="modPsw(scope.row)">修改密码</el-button>
        </template>
      </el-table-column>
    </el-table>
<!--    分页-->
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 20, 30]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
    <!-- 新增和编辑弹窗-->
    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <!--此id不展示，form解决清空bug-->
        <el-form-item v-show="false" label="id" prop="id">
          <el-col :span="20">
            <el-input v-model="form.id"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="账号" prop="userName">
          <el-col :span="20">
            <el-input v-model="form.userName"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="名字" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item v-if="form.id==''" label="密码" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password" type="password" show-password></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-col :span="20">
            <el-input v-model="form.age"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="1">男</el-radio>
            <el-radio label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-col :span="20">
            <el-input v-model="form.phone"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="修改密码"
        :visible.sync="pswDialogVisible"
        width="30%"
        center>
      <el-form :model="ruleForm" status-icon :rules="rules1" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="原密码" prop="originPass">
          <el-col :span="20">
            <el-input type="password" v-model="ruleForm.originPass" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-col :span="20">
            <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-col :span="20">
            <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
              <el-button @click="pswDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="doUpdate">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "AdminManage",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入原密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass1 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
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
      pageSize:10,
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
      pswDialogVisible:false,
      centerDialogVisible:false,
      form:{
        id:'',
        userName:'',
        name:'',
        password:'',
        age: '',
        phone:'',
        gender:'0',
        role:'0'
      },
      ruleForm: {
        userId:'',
        originPass: '',
        pass: '',
        checkPass: '',
      },
      rules1: {
        originPass: [
          {validator: validatePass, trigger: 'blur'},
          { min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: 'blur' }
        ],
        pass: [
          {validator: validatePass1, trigger: 'blur'},
          { min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: 'blur' }
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'blur'},
          { min: 6, max: 18, message: '长度在 6 到 18 个字符', trigger: 'blur' }
        ],
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
    resetForm1(formName) {
      this.$refs.ruleForm.resetFields();
    },
    resetForm(formName) {
      this.$refs.form.resetFields();
    },
    doUpdate(){
      let element={
        userId:this.ruleForm.userId,
        originPass:this.ruleForm.originPass,
        pass:this.ruleForm.pass,
        checkPass:this.ruleForm.checkPass,
      }
      this.$axios.post(this.$httpUrl+'/user/updatePassword',element).then(res=>res.data).then(res=>{
        if(res.code==200) {
          this.$message({
            message: '操作成功！',
            type: 'success'
          });
          this.pswDialogVisible= false
          this.resetForm1()
        }else{
          this.$message({
            message: res.msg,
            type: 'error'
          });
        }
      })
    },
    modPsw(row){
      this.pswDialogVisible=true
      this.$nextTick(()=>{
        this.resetForm1();
      })
      this.ruleForm.userId=row.id
    },
    del(id){
      this.$axios.get(this.$httpUrl+'/user/del?id='+id).then(res=>res.data).then(res=>{
        if(res.code==200){
          this.$message({
            showClose: true,
            message: '操作成功！',
            type: 'success'
          });
          this.loadPost()
        }else{
          this.$message({
            showClose: true,
            message: '操作失败！',
            type: 'error'
          });
        }
      })
    },
    mod(row){
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        //  赋值到表单
        this.form.id=row.id
        this.form.userName=row.userName
        this.form.name=row.name
        this.form.password=''
        this.form.age=row.age+''
        this.form.gender=row.gender+''
        this.form.phone=row.phone
        this.form.role=row.role
      })
    },
    add(){
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        this.resetForm();
      })
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/user/save',this.form).then(res=>res.data).then(res=>{
        if(res.code==200){
          this.$message({
            showClose: true,
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible=false
          this.loadPost()
          this.resetForm()
        }else{
          this.$message({
            showClose: true,
            message: '操作失败！',
            type: 'error'
          });
        }
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/user/update',this.form).then(res=>res.data).then(res=>{
        if(res.code==200){
          this.$message({
            showClose: true,
            message: '操作成功！',
            type: 'success'
          });
          this.centerDialogVisible=false
          this.loadPost()
          this.resetForm()
        }else{
          this.$message({
            showClose: true,
            message: '操作失败！',
            type: 'error'
          });
        }
      })
    },
    save(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.form.id){
            this.doMod();
          }else{
            this.doSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
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
      this.gender=''
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/user/listPageC1',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          name:this.name,
          gender:this.gender,
          role:'0'
        }
      }).then(res=>res.data).then(res=>{
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