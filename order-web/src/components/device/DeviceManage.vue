<template>
  <div>
    <div style="margin-bottom:5px;">
      <el-input v-model="name" placeholder="请输入设备名字" suffix-icon="el-icon-search" style="width: 200px"
                @keyup.enter.native="loadPost"></el-input>
      <el-input v-model="model" placeholder="请输入设备型号" suffix-icon="el-icon-search" style="width: 200px ; margin-left: 5px"
                @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="loadPost">查询</el-button>
      <el-button type="success" style="margin-left: 5px" @click="resetParam">重置</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="add" v-if="user.role==0">新增</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="inDevices" v-if="user.role==0">入库</el-button>
      <el-button type="primary" style="margin-left: 5px" @click="outDevices" v-if="user.role==0">出库</el-button>
    </div>
    <el-table :data="tableData"
              :header-cell-style="{background:'#f3f6fd',color:'#555'}"
              border
              highlight-current-row
              @current-change="selectCurrentChange"
    >
      <el-table-column prop="device" label="设备图片" width="80">
        <template slot-scope="scope">
          <el-image
            style="width: 50px; height: 50px"
            :src="http+scope.row.urls[0]"
            fit="fill">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="deviceName" label="设备名字" width="180">
      </el-table-column>
      <el-table-column prop="deviceModel" label="设备型号" width="80">
      </el-table-column>
      <el-table-column prop="price" label="单价" width="80">
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="80">
      </el-table-column>
      <el-table-column prop="introduction" label="介绍" width="500">
      </el-table-column>
      <el-table-column prop="operate" label="操作" v-if="user.role==0">
        <template slot-scope="scope">
          <el-button size="small" type="success" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
              title="确定删除吗？"
              @confirm="del(scope.row.id)"
              style="margin-left: 5px;"
          >
            <el-button slot="reference" size="small" type="danger">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[4, 6, 8, 10]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
    <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
      <el-form ref="form" :rules="rules" :model="form" label-width="80px">
        <el-form-item v-show="false" label="id" prop="id">
          <el-col :span="20">
            <el-input v-model="form.id"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="设备名字" prop="deviceName">
          <el-col :span="20">
            <el-input v-model="form.deviceName"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="设备型号" prop="deviceModel">
          <el-col :span="20">
            <el-input v-model="form.deviceModel"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-col :span="20">
            <el-input v-model="form.price"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-col :span="20">
            <el-input v-model="form.stock"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="介绍" prop="introduction">
          <el-col :span="20">
            <el-input v-model="form.introduction"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="图片" prop="urls">
          <el-upload
              class="upload-demo"
              ref="upload"
              :action= "this.$httpUrl+'/device/upload'"
              :on-success="handleImageSuccess"
              :on-remove="handleRemove"
              multiple
              :file-list="fileList"
              list-type="picture">
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">只能上传jpg/png图片</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
        title="出入库"
        :visible.sync="inDialogVisible"
        width="30%"
        center>
      <el-dialog
          width="60%"
          title="用户选择"
          :visible.sync="innerVisible"
          append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span slot="footer" class="dialog-footer">
          <el-button @click="innerVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmUser">确 定</el-button>
        </span>
      </el-dialog>
      <el-form ref="form1" :rules="rules1" :model="form1" label-width="80px">
        <el-form-item v-show="false" label="id" prop="id">
          <el-col :span="20">
            <el-input v-model="form1.id"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="设备名字">
          <el-col :span="20">
            <el-input v-model="form1.deviceName" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="设备型号">
          <el-col :span="20">
            <el-input v-model="form1.deviceModel" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人">
          <el-col :span="20">
            <el-input v-model="form1.userName" readonly @click.native="selectUser"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form1.count"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="inDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doInDevices">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import SelectUser from "@/components/user/SelectUser";
export default {
  name: "DeviceManage",
  components: {SelectUser},
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
      user : JSON.parse(sessionStorage.getItem('CurUser')),
      http:this.$httpUrl,
      tableData:[],
      pageSize:4,
      pageNum:1,
      total:0,
      name:'',
      model:'',
      centerDialogVisible:false,
      inDialogVisible:false,
      innerVisible:false,
      currentRow:{},
      tempUser:{},
      form:{
        // id:'',
        deviceName:'',
        deviceModel:'',
        price:'',
        stock:'',
        introduction:'',
        urls:[],
      },
      form1:{
        id:'',
        deviceId:'',
        deviceName:'',
        deviceModel:'',
        count:'',
        adminId:'',
        userId:'',
        userName:'',
        action:'1',
      },
      fileList:[],
      rules1: {},
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
    doSelectUser(val){
      this.tempUser = val
    },
    confirmUser(){
      this.form1.userName = this.tempUser.name
      this.form1.userId = this.tempUser.id
      this.innerVisible = false
    },
    selectCurrentChange(val) {
      this.currentRow = val;
    },
    handleRemove(file, fileList) {
      this.form.urls = this.form.urls.filter(item => file.url.indexOf(item+'') === -1 )
    },
    handleImageSuccess(response, file, fileList){
      if(response.code !== 400){
        this.form.urls.push(response.data)
      }else {
        this.$message({
          showClose: true,
          message: '操作失败，文件类型不符，请上传图片',
          type: 'error'
        });
      }
    },
    resetForm(formName) {
      this.$refs.form.resetFields();
      this.fileList = []
    },
    resetInForm(){
      this.$refs.form1.resetFields();
    },
    del(id){
      this.$axios.get(this.$httpUrl+'/device/del?id='+id).then(res=>res.data).then(res=>{
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
      this.fileList = []
      for (let i = 0; i < row.urls.length; i++) {
        let data = {
          name:'image'+i,
          url:this.$httpUrl+row.urls[i]
        }
        this.fileList.push(data)
      }
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        //  赋值到表单
        this.form.id=row.id+''
        this.form.deviceName=row.deviceName
        this.form.deviceModel=row.deviceModel
        this.form.price=row.price+''
        this.form.stock=row.stock+''
        this.form.introduction=row.introduction
        this.form.urls=row.urls
      })
    },
    add(){
      this.centerDialogVisible=true
      this.$nextTick(()=>{
        this.resetForm();
      })
    },
    inDevices() {
      if(!this.currentRow.id){
        alert('请选择记录');
        return;
      }
      this.inDialogVisible = true
      this.$nextTick(() => {
        this.resetInForm();
      })
      this.form1.deviceId=this.currentRow.id
      this.form1.deviceName=this.currentRow.deviceName
      this.form1.deviceModel=this.currentRow.deviceModel
      this.form1.adminId=this.user.id
      this.form1.action='1'
    },
    outDevices() {
      if(!this.currentRow.id){
        alert('请选择记录');
        return;
      }
      this.inDialogVisible = true
      this.$nextTick(() => {
        this.resetInForm();

      })
      this.form1.deviceId=this.currentRow.id
      this.form1.deviceName=this.currentRow.deviceName
      this.form1.deviceModel=this.currentRow.deviceModel
      this.form1.adminId=this.user.id
      this.form1.action='2'
    },
    selectUser(){
      this.innerVisible = true;
    },
    doSave(){
      let data={
        deviceName:this.form.deviceName,
        deviceModel:this.form.deviceModel,
        price:this.form.price,
        stock:this.form.stock,
        introduction:this.form.introduction,
        urls:this.form.urls
      }
      this.$axios.post(this.$httpUrl+'/device/save',data).then(res=>res.data).then(res=>{
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
      let data={
        id:this.form.id,
        deviceName:this.form.deviceName,
        deviceModel:this.form.deviceModel,
        price:this.form.price,
        stock:this.form.stock,
        introduction:this.form.introduction,
        urls:this.form.urls
      }
      this.$axios.post(this.$httpUrl+'/device/update',data).then(res=>res.data).then(res=>{
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
    doInDevices() {
      this.$axios.post(this.$httpUrl + '/record/save', this.form1).then(res => res.data).then(res => {
        if (res.code == 200) {
          this.$message({
            showClose: true,
            message: '操作成功！',
            type: 'success'
          });
          this.inDialogVisible = false
          this.loadPost()
          this.resetInForm()
        } else {
          this.$message({
            showClose: true,
            message: '操作失败！',
            type: 'error'
          });
        }
      })
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
      this.$axios.post(this.$httpUrl+'/device/listPage',{
        pageSize:this.pageSize,
        pageNum:this.pageNum,
        param:{
          deviceName:this.name,
          deviceModel:this.model
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