<template >
    <el-container>
        <el-main>
            <el-row>
                <!-- 左侧图片 -->
                <el-col :xl="6" :lg="5" :md="3" :sm="1" :xs="0"> </el-col>
                <el-col :xl="4" :lg="5" :md="7" :sm="9" :xs="11" class="colLeft">
                    <p class="titleLeft">加&nbsp;密&nbsp;极&nbsp;速&nbsp;云&nbsp;盘</p>
                    <img src="../assets/product_logo.jpg" class="logoLeft" alt="产品logo">
                </el-col>
                <!-- 右侧表单 -->
                <el-col :xl="2" :lg="2" :md="2" :sm="2" :xs="1"> </el-col>
                <el-col :xl="6" :lg="7" :md="8" :sm="10" :xs="12" class="colRight">
                    <p class="msgRight">欢&nbsp;迎&nbsp;使&nbsp;用&nbsp;，请&nbsp;登&nbsp;录</p>
                    <el-tabs type="border-card" class="tabRight" stretch="true">
                        <!-- 登录标签页 -->
                        <el-tab-pane label="登录">
                            <el-form class="tabForm" ref="LoginForm" :model="loginUser" :rules="rules">
                                <el-form-item class="gapBig" prop="username">
                                    <el-input placeholder="用户名：" v-model="loginUser.username" />
                                </el-form-item>
                                <el-form-item class="gapBig" prop="password">
                                    <el-input placeholder="密码：" type="password" v-model="loginUser.password" />
                                </el-form-item>
                                <el-form-item class="gapBig" prop="imageCode">
                                    <el-input placeholder="验证码：" class="codeInput1" type="text"
                                        v-model="loginUser.imageCode" />
                                    <div class="identifybox codeImg" @click="refreshCode">
                                        <ImageCode :identifyCode="identifyCode"></ImageCode>
                                    </div>
                                </el-form-item>
                                <el-form-item>
                                    <el-link :underline="false" type="primary">
                                        <a class="fgtPwdBtn" @click="forgetPwdDialog = true">忘记密码</a>
                                    </el-link>
                                    <el-button type="primary" class="loginBtn" @click="login"
                                        :loading="loading">登录</el-button>
                                </el-form-item>
                            </el-form>
                        </el-tab-pane>
                        <!-- 注册标签页 -->
                        <el-tab-pane label="注册">
                            <el-form class="tabForm" ref="RegForm" :model="registerUser" :rules="rules">
                                <el-form-item class="gapBig" prop="username">
                                    <el-input placeholder="用户名：" v-model="registerUser.username" />
                                </el-form-item>
                                <el-form-item class="gapBig" prop="password">
                                    <el-input placeholder="密码：" type="password" v-model="registerUser.password" />
                                </el-form-item>
                                <el-form-item class="gapBig" prop="confirmRegPassword">
                                    <el-input placeholder="确认密码：" type="password"
                                        v-model="registerUser.confirmRegPassword" />
                                </el-form-item>
                                <el-form-item class="gapBig" prop="email">
                                    <el-input placeholder="电子邮箱：" v-model="registerUser.email" />
                                </el-form-item>
                                <el-form-item class="gapSmall" prop="emailCheckCode">
                                    <el-input placeholder="验证码：" type="text" v-model="registerUser.emailCheckCode"
                                        class="codeInput2" />
                                    <el-button type="primary" class="codeButton" :disabled="!canClick"
                                        @click="getRegisterCode" plain>{{ getCode }}</el-button>
                                </el-form-item>
                                <el-form-item class="gapMini" prop="agree">
                                    <el-checkbox class="checkBox" v-model="registerUser.agree" label="同意用户使用准则"
                                        name="type" />
                                </el-form-item>
                                <el-button type="primary" class="commonBtn" @click="register"
                                    :loading="loading">注册</el-button>
                            </el-form>
                        </el-tab-pane>
                    </el-tabs>
                </el-col>
            </el-row>
            <!-- 忘记密码弹窗 -->
            <el-dialog title="忘记密码" v-model="forgetPwdDialog" width="400px" center>
                <el-form ref="ForgetPwdForm" :model="changePwd" :rules="rules">
                    <el-form-item class="gapBig" prop="username">
                        <el-input placeholder="用户名：" v-model="changePwd.username" />
                    </el-form-item>
                    <el-form-item class="gapBig" prop="emailCheckCode">
                        <el-input placeholder="验证码：" type="password" v-model="changePwd.emailCheckCode"
                            class="codeInput2" />
                        <el-button class="codeButton" type="primary" :disabled="!canClick" @click="getForgetPwdCode"
                            plain>{{ getCode }}</el-button>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button style="width:100px;" @click="forgetPwdDialog = false">取 消</el-button>
                    <el-button style="width:100px;margin-left: 150px;" type="primary"
                        @click="forgetPwdDialog = false;changePwdDialog = true">修改密码</el-button>
                </span>
            </el-dialog>
            <!-- 修改密码弹窗 -->
            <el-dialog title="修改密码" v-model="changePwdDialog" width="400px" center>
                <el-form ref="ForgetPwdForm" :model="changePwd" :rules="rules">
                    <el-form-item class="gapBig" prop="password">
                        <el-input placeholder="新密码：" type="password" v-model="changePwd.password" />
                    </el-form-item>
                    <el-form-item class="gapBig" prop="confirmPassword">
                        <el-input placeholder="确认密码：" type="password" v-model="changePwd.confirmPassword" />
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button style="width:100px;" @click="changePwdDialog = false">取 消</el-button>
                    <el-button style="width:100px;margin-left: 150px;" type="primary" @click="forgetPwd"
                        :loading="loading">修改密码</el-button>
                </span>
            </el-dialog>
            <p class="msgBottom">版权所有@20xx-20xx | 团队：The Coding Boys | 联系方式：********</p>
        </el-main>
    </el-container>
</template>

<script>
import API from "../api/api_user";
import ImageCode from "../components/ImageCode.vue";
export default {
    components: {
        ImageCode
    },
    data() {
        // 图片验证码自定义验证规则
        const validateImageCode = (rule, value, callback) => {
            if (value === "") {
                callback(new Error('请输入验证码'))
            } else if (value !== this.identifyCode) {
                console.log('验证码:', value);
                callback(new Error('验证码不正确!'))
            } else {
                callback()
            }
        };
        // 确认密码自定义验证规则
        const confirmRegPwd = (rule, value, callback) => {
            if (value === "") {
                callback(new Error('请再次输入密码'))
            } else if (value !== this.registerUser.password) {
                callback(new Error('两次输入不一致!'))
            } else {
                callback()
            }
        };
        // 修改密码确认自定义验证规则
        const confirmPwd = (rule, value, callback) => {
            if (value === "") {
                callback(new Error('请再次输入密码'))
            } else if (value !== this.changePwd.password) {
                callback(new Error('两次输入不一致!'))
            } else {
                callback()
            }
        };
        return {
            loading: false,
            getCode: "获取验证码",
            canClick: true,
            forgetPwdDialog: false,
            changePwdDialog: false,
            loginUser: {
                username: "",
                password: "",
                imageCode: ""
            },
            registerUser: {
                username: "",
                password: "",
                email: "",
                confirmRegPassword: "",
                emailCheckCode: "",
                agree: []
            },
            changePwd: {
                username: "",
                password: "",
                confirmPassword: "",
                emailCheckCode: ""
            },
            rules: {
                username: [
                    { required: true, message: "请输入账号", trigger: ["blur", "change"] },
                    { message: "电话号码格式不正确", pattern: /^[1][3,4,5,7,8][0-9]{9}$/, trigger: ["blur", "change"] }
                ],
                password: [
                    { required: true, message: "请输入密码", trigger: ["blur", "change"] },
                    { message: "密码至少为6位字母或数字的组合", pattern: /^[0-9a-zA-Z]{6,}$/, trigger: ["blur", "change"] }
                ],
                email: [
                    { required: true, message: "请输入邮箱", trigger: ["blur", "change"] },
                    { message: "电子邮箱格式不正确", type: 'email', trigger: ["blur", "change"] }
                ],
                imageCode: [
                    { required: true, trigger: ["blur", "change"], validator: validateImageCode }
                ],
                confirmRegPassword: [
                    { required: true, message: "请再次输入密码", trigger: ["blur", "change"] },
                    { required: true, trigger: ["blur", "change"], validator: confirmRegPwd }
                ],
                confirmPassword: [
                    { required: true, message: "请再次输入密码", trigger: ["blur", "change"] },
                    { required: true, trigger: ["blur", "change"], validator: confirmPwd }
                ],
                emailCheckCode: [
                    { required: true, message: "请输入验证码", trigger: "blur" },
                ],
                agree: [
                    { required: true, message: "请阅读用户使用准则", trigger: ["blur", "change"] }
                ]
            },
            identifyCode: "",
            identifyCodes: "1234567890"
        }
    },
    created() {
        this.refreshCode();
    },
    mounted() {
        // 验证码初始化
        this.identifyCode = '';
        this.makeCode(this.identifyCodes, 4);
    },
    methods: {
        login() {
            let that = this;
            this.$refs.LoginForm.validate(valid => {
                if (valid) {
                    this.loading = true;
                    let loginParams = this.loginUser;
                    API.login(loginParams).then(
                        function (res) {
                            that.loading = false;
                            console.log(res);
                            if (res.flag) {
                                window.localStorage.setItem("access-token", res.data.token);
                                window.localStorage.setItem("currentUser", JSON.stringify(res.data.currentUser));
                                that.$message({ message: res.msg, type: "success" });
                                that.$router.push({ path: "/index" });
                            } else {
                                that.$message.error(res.msg);
                            }
                        }
                    )
                }
            });
        },
        register() {
            let that = this;
            this.$refs.RegForm.validate(valid => {
                if (valid) {
                    this.loading = true;
                    var registerParams = new FormData();
                    registerParams.append("username", this.registerUser.username);
                    registerParams.append("password", this.registerUser.password);
                    registerParams.append("email", this.registerUser.email);
                    registerParams.append("checkCode", this.registerUser.emailCheckCode);
                    API.register(registerParams).then(
                        function (res) {
                            that.loading = false;
                            console.log(res);
                            if (res.flag) {
                                that.$message({ message: res.msg, type: "success" });
                                that.$router.push({ path: "/login" });
                            } else {
                                that.$message.error(res.msg);
                            }
                        }
                    )
                }
            });
        },
        forgetPwd() {
            let that = this;
            this.$refs.ForgetPwdForm.validate(valid => {
                if (valid) {
                    this.loading = true;
                    var forgetPwdParams = new FormData();
                    forgetPwdParams.append("username", this.changePwd.username);
                    forgetPwdParams.append("newPwd", this.changePwd.password);
                    forgetPwdParams.append("checkCode", this.changePwd.emailCheckCode);
                    API.forgetPwd(forgetPwdParams).then(
                        function (res) {
                            that.loading = false;
                            console.log(res);
                            if (res.flag) {
                                that.changePwdDialog = false;
                                that.$message({ message: res.msg, type: "success" });
                            } else {
                                that.$message.error(res.msg);
                            }
                        }
                    )
                }
            });
        },
        getRegisterCode() {
            let that = this;
            let validateList = [];
            this.$refs.RegForm.validateField(['username', 'password', 'confimPassword', 'email', 'agree'], (err) => {
                validateList.push(err);
            });
            if (validateList.every((item) => item == "")) {
                that.canClick = false;
                var time = 120;
                var timer = setInterval(function () {
                    if (time == -1) {
                        clearInterval(timer)
                        that.canClick = true;
                        that.getCode = '重新发送验证码';
                    } else {
                        that.getCode = time + '秒后重新获取';
                        time--;
                    }
                }, 1000);
                var getCodeParams = new FormData();
                getCodeParams.append("username", that.registerUser.username);
                getCodeParams.append("email", that.registerUser.email);
                API.getRegisterCode(getCodeParams).then(function (res) {
                    if (res.flag) {
                        clearInterval(timer);
                        that.canClick = true;
                        that.getCode = '重新发送验证码';
                        that.$message({ message: res.msg, type: "success" });
                    } else {
                        that.$message.error(res.msg);
                    }
                })
            }
        },
        getForgetPwdCode() {
            let that = this;
            let validateList = [];
            this.$refs.ForgetPwdForm.validateField('username', (err) => {
                validateList.push(err);
            });
            if (validateList.every((item) => item == "")) {
                that.canClick = false;
                var time = 120;
                var timer = setInterval(function () {
                    if (time == -1) {
                        clearInterval(timer)
                        that.canClick = true;
                        that.getCode = '重新发送验证码';
                    } else {
                        that.getCode = time + '秒后重新获取';
                        time--;
                    }
                }, 1000);
                var getCodeParams = new FormData();
                getCodeParams.append("username", that.changePwd.username);
                API.getForgetPwdCode(getCodeParams).then(function (res) {
                    if (res.flag) {
                        clearInterval(timer)
                        that.canClick = true;
                        that.getCode = '重新发送验证码';
                        that.$message({ message: res.msg, type: "success" });
                    } else {
                        that.$message.error(res.msg);
                    }
                })
            }
        },
        //验证码----start
        randomNum(min, max) {
            return Math.floor(Math.random() * (max - min) + min);
        },
        refreshCode() {
            this.identifyCode = "";
            this.makeCode(this.identifyCodes, 4);
        },
        makeCode(o, l) {
            for (let i = 0; i < l; i++) {
                this.identifyCode += this.identifyCodes[
                    this.randomNum(0, this.identifyCodes.length)
                ];
            }
        }
    }
};
</script>
  
<style scoped> .el-container {
     width: 100%;
     height: 100%;
     position: fixed;
     background-color: lightskyblue;
 }

 .el-row {
     margin-top: 150px;
 }

 .colLeft {
     max-width: 320px;
 }

 .colRight {
     max-width: 480px;
 }

 .titleLeft {
     /*this.height=83px*/
     font-size: 25px;
     /*this.paddingTop=logoLeft.paddingTop-this.height+titleLeft.height*/
     padding-top: 43px;
     text-align: center;
 }

 .logoLeft {
     width: 100%;
     /*this.paddingTop=80px*/
     /*this.height=(tabRight.height+2)
        -(2*this.paddingTop)*/
     height: 295px;
 }

 .msgRight {
     /*this.height=56px*/
     font-size: 17px;
     text-align: center;
 }

 .tabRight {
     width: 100%;
     /*开f12调整，以下边框无冗余为准*/
     height: 433px;
 }

 .tabForm {
     /*default:15px*/
     padding-top: 11px;
     padding-bottom: 11px;
 }

 .msgBottom {
     font-size: 12px;
     padding-top: 220px;
     text-align: center;
 }

 .codeInput1 {
     width: calc(100% - 120px);
 }

 .codeImg {
     height: 32px;
     width: 120px;
     margin-top: 0px;
 }

 .fgtPwdBtn {
     margin-left: 5px;
 }

 .loginBtn {
     margin-left: calc(50% - 50px - 56px - 5px);
     width: 100px;
 }

 .checkBox {
     margin-left: 2px;
 }

 .codeInput2 {
     width: calc(100% - 165px);
 }

 .codeButton {
     width: 160px;
     margin-left: 5px;
 }

 .gapBig {
     /*default:18*/
     margin-bottom: 24px;
 }

 .gapSmall {
     /*default:18*/
     margin-bottom: 16px;
 }

 .gapMini {
     /*default:18*/
     margin-bottom: 6px;
 }

 .commonBtn {
     margin-left: calc(50% - 50px);
     width: 100px;
 }
</style>