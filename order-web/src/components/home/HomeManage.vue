<template>
  <div class="box" style="text-align: center;background-color: #f1f1f3;height:95%;padding: 0px;margin: 0px;">
    <h1 style="font-size: 50px ; padding-top: 30px">{{'欢迎你！'+user.name}}</h1>
    <el-button type="primary" style="float: right" class="mod-button-info" @click="mod(user)">修改我的信息</el-button>
    <el-button type="primary" style="float: right" class="mod-button-psw" @click="modPsw()">修改我的密码</el-button>
    <el-descriptions style="margin-top: 50px" title="个人中心" :column="2" size="40" border>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-s-custom"></i>
          账号
        </template>
        {{user.userName}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-mobile-phone"></i>
          电话
        </template>
        {{user.phone}}
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-location-outline"></i>
          性别
        </template>
        <el-tag
            :type="user.gender === 1 ? 'primary' : 'danger'"
            disable-transitions><i :class="user.gender==1?'el-icon-male':'el-icon-female'"></i>{{user.gender==1?"男":"女"}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item>
        <template slot="label">
          <i class="el-icon-tickets"></i>
          角色
        </template>
        <el-tag
            :type="user.role === 0 ? 'danger' : 'primary'"
            disable-transitions>{{user.role=== 0 ?"管理员":"用户"}}</el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <div class="date-cell">
      <DateUtils></DateUtils>
    </div>
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
        <el-button type="primary" @click="doMod">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
        title="修改我的密码"
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
import DateUtils from "../DateUtils";
export default {
  name: "HomeManage",
  components: {DateUtils},
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
      if (value > 150) {
        callback(new Error('年龄输⼊过⼤'));
      } else {
        callback();
      }
    };
    let checkDuplicate = (rule, value, callback) => {
      if (this.form.id) {
        return callback();
      }
      this.$axios.get(this.$httpUrl + "/user/findByUserName?userName=" + this.form.userName).then(res => res.data).then(res => {
        if (res.code != 200) {
          callback()
        } else {
          callback(new Error('账号已经存在'));
        }
      })
    };
    return {
      user: {},
      centerDialogVisible: false,
      pswDialogVisible: false,
      form: {
        id: '',
        userName: '',
        name: '',
        password: '',
        age: '',
        phone: '',
        gender: '0',
        role: '0'
      },
      ruleForm: {
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
          {validator: checkDuplicate, trigger: 'blur'}
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
          {pattern: /^([1-9][0-9]*){1,3}$/, message: '年龄必须为正整数字', trigger: "blur"},
          {validator: checkAge, trigger: 'blur'}
        ],
        phone: [
          {required: true, message: "⼿机号不能为空", trigger: "blur"},
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输⼊正确的⼿机号码", trigger:
                "blur"
          }
        ]
      }
    }
  },
  computed: {},
  methods: {
    // submitForm(formName) {
    //   this.$refs.ruleForm.validate((valid) => {
    //     if (valid) {
    //       alert('submit!');
    //     } else {
    //       console.log('error submit!!');
    //       return false;
    //     }
    //   });
    // },
    resetForm1(formName) {
      this.$refs.ruleForm.resetFields();
    },
    resetForm(formName) {
      this.$refs.form.resetFields();
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/user/update',this.form).then(res=>res.data).then(res=>{
        if(res.code==200){
          this.$message({
            showClose: true,
            message: '操作成功！',
            type: 'success'
          });
          sessionStorage.setItem("CurUser",JSON.stringify(this.form))
          this.user = this.form
          this.centerDialogVisible=false
        }else{
          this.$message({
            showClose: true,
            message: '操作失败！',
            type: 'error'
          });
        }
      })
    },
    doUpdate(){
      let element={
        userId:this.user.id,
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
    modPsw(){
      this.pswDialogVisible=true
      this.$nextTick(()=>{
        this.resetForm1();
      })
    },
    mod(user){
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        //  赋值到表单
        this.form.id=user.id
        this.form.userName=user.userName
        this.form.name=user.name
        this.form.password=''
        this.form.age=user.age+''
        this.form.gender=user.gender+''
        this.form.phone=user.phone
        this.form.role=user.role
      })
    },
    init(){
      this.user = JSON.parse(sessionStorage.getItem('CurUser'))
    }
  },
  created(){
    this.init()
  }
}
</script>

<style scoped>
.el-descriptions{
  width:90%;
  margin: 0 auto;
  text-align: center;
}
.box{
  background-color: #f1f1f3;
}
.mod-button-info{
  margin-top: 50px;
  margin-right:  80px;
}
.mod-button-psw{
  margin-top: 50px;
  margin-right:  5px;
}
.date-cell{
  margin-top: 50px;
  text-align: center;
}
</style>