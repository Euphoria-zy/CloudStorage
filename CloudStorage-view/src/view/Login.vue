<template >
    <el-container>
        <el-main>
            <el-row>
                <!-- 左侧图片 -->
                <el-col :xl="6" :lg="5" :md="4" :sm="3" :xs="2"> </el-col>
                <el-col :xl="4" :lg="5" :md="6" :sm="7" :xs="8">
                    <p class="titleLeft">加&nbsp;密&nbsp;极&nbsp;速&nbsp;云&nbsp;盘</p>
                    <img src="../assets/product_logo.jpg" class="logoLeft" alt="产品logo">
                </el-col>
                <!-- 右侧表单 -->
                <el-col :xl="2" :lg="2" :md="2" :sm="1" :xs="1"> </el-col>
                <el-col :xl="6" :lg="7" :md="8" :sm="9" :xs="10">
                    <p class="msgRight">欢&nbsp;迎&nbsp;使&nbsp;用&nbsp;，请&nbsp;登&nbsp;录</p>
                    <el-tabs type="border-card" class="tabRight" stretch="true">
                        <!-- 登录标签页 -->
                        <el-tab-pane label="登录">
                            <el-form class="loginForm">
                                <el-form-item>
                                    <el-input placeholder="用户名：" v-model="Email" />
                                </el-form-item>
                                <el-form-item>
                                    <el-input placeholder="密码：" type="password" v-model="password" />
                                </el-form-item>
                                <el-form-item>
                                    <el-input placeholder="验证码：" v-model="Email" />
                                </el-form-item>
                                <el-form-item>
                                    <el-button text>
                                        <a class="floatR" @click="isChangePassword = true">忘记密码</a>
                                    </el-button>
                                </el-form-item>
                                <el-button type="primary" class="loginBtn" @click="login">
                                    登录
                                </el-button>
                            </el-form>
                        </el-tab-pane>
                        <!-- 注册标签页 -->
                        <el-tab-pane label="注册">
                            <el-form class="loginForm">
                                <el-form-item>
                                    <el-input placeholder="用户名：" v-model="Email" />
                                </el-form-item>
                                <el-form-item>
                                    <el-input placeholder="密码：" type="password" v-model="rPassword" />
                                </el-form-item>
                                <el-form-item>
                                    <el-input placeholder="确认密码：" type="password" v-model="confirmPassword"
                                        @blur="confirmFunc" />
                                </el-form-item>
                                <el-form-item>
                                    <el-input placeholder="电子邮箱：" v-model="Email" />
                                </el-form-item>
                                <el-form-item class="closerItem">
                                    <el-input placeholder="验证码：" type="password" v-model="identifyCode" class="codeInput" />
                                    <el-button type="primary" @click="getIdentifyCode" class="codeButton" plain>
                                        获取验证码
                                    </el-button>
                                </el-form-item>
                                <el-form-item class="closerItem">
                                    <el-checkbox class="checkBox" v-model="rAgree" label="同意用户使用准则" name="type" />
                                </el-form-item>
                                <el-button type="primary" class="loginBtn" @click="register">
                                    注册
                                </el-button>
                            </el-form>
                        </el-tab-pane>
                    </el-tabs>
                </el-col>
                <!-- 忘记密码弹窗 -->
                <teleport to="body">
                    <el-dialog v-model="isChangePassword" title="修改密码" width="40%">
                        <el-form label-position="right" label-width="100px" style="max-width: 460px" class="loginForm">
                            <el-form-item label="邮箱：">
                                <el-input v-model="rEmail" />
                            </el-form-item>
                            <el-form-item label="密码：">
                                <el-input type="password" v-model="rPassword" />
                            </el-form-item>
                            <el-form-item label="确认密码：">
                                <el-input type="password" v-model="confirmPassword" @blur="confirmFunc" />
                            </el-form-item>
                            <el-form-item label="验证码：">
                                <el-row>
                                    <el-input type="password" v-model="identifyCode" class="inpWidth" />
                                    <el-button type="primary" @click="getIdentifyCode" plain>
                                        获取验证码
                                    </el-button>
                                </el-row>
                            </el-form-item>
                            <el-row>
                                <el-checkbox class="checkBox" v-model="rAgree" label="同意用户使用准则" name="type" />
                            </el-row>
                            <el-button type="primary" class="loginBtn" @click="changePassword">
                                修改密码
                            </el-button>
                            <el-button v-if="rAgree" type="primary" class="loginBtn" @click="isChangePassword = false">
                                关闭页面
                            </el-button>
                        </el-form>
                    </el-dialog>
                </teleport>
            </el-row>
            <p class="msgBottom">版权所有@20xx-20xx | 团队：The Coding Boys | 联系方式：********</p>
        </el-main>
    </el-container>
</template>

<script>
import { reactive, toRefs, ref } from "@vue/reactivity";
import { ElMessage } from "element-plus";
export default {
    setup() {
        const form = reactive({
            Email: "",
            password: "",
            isAgree: 0,
        });
        const registerForm = reactive({
            rEmail: "",
            rPassword: "",
            confirmPassword: "",
            identifyCode: "",
            rAgree: 0,
        });

        // 方法
        // 登录 将账号密码写入后台,获取用户数据,后登录
        // 需要修改共享数据
        function login() {
            console.log(form);
        }
        // 注册 -- 将账号密码写入后台, 自动登录
        // 需要修改共享数据
        function register() {
            console.log("注册", registerForm);
        }
        // 获取验证码
        function getIdentifyCode() {
            console.log("获取验证码");
        }
        // 确认密码
        const confirmFunc = () => {
            if (registerForm.confirmPassword !== registerForm.rPassword)
                ElMessage.error("密码与确认密码不一致.");
        };
        // 修改密码
        let isChangePassword = ref(false);
        // 用的是注册参数
        function changePassword() { }

        return {
            isChangePassword,
            ...toRefs(form),
            ...toRefs(registerForm),
            login,
            register,
            getIdentifyCode,
            confirmFunc,
            changePassword,
        };
    },
};
</script>
  
<style scoped> .el-container {
     width: 100%;
     height: 100%;
     position: fixed;
     background-color: lightskyblue;
 }

 .el-main {
     margin-top: 155px;
     /*往大整就完了*/
     height: 1000px;
 }

 .titleLeft {
    /*this.height=83px*/
     font-size: 25px;
     /*this.paddingTop=logoLeft.paddingTop-this.height+titleLeft.height*/
     padding-top: 18px;
     text-align: center;
 }

 .logoLeft {
     width: 100%;
     /*this.paddingTop=45px*/
     /*this.height=(tabRight.height+2)
        -(2*this.paddingTop)*/
     height: 288px;
 }

 .msgRight {
    /*this.height=56px*/
     font-size: 17px;
     text-align: center;
 }

 .tabRight {
     width: 100%;
     /*开f12调整，以下边框无冗余为准*/
     height: 377px;
 }

 .msgBottom{
     font-size: 10px;
     padding-top: 250px;
     text-align: center;
 }

 .loginBtn {
     width: 100px;
 }

 .loginForm {
     text-align: center;
 }

 .checkBox {
     margin-left: 7px;
 }

 .codeInput {
     width: calc(100% - 107px);
 }

 .codeButton {
     margin-left: 5px;
 }

 .floatR {
     font-size: 10px;
     margin-left: 9px;
     color: blue;
 }
 .closerItem{
    margin-bottom: 6px;
 }</style>