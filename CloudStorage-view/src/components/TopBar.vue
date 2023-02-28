<template>
    <header>
        <div class="top-flex-container">
            <div class="header-left">
                <img src="../assets/product_logo.jpg" alt="产品logo" height="30">
                <p>极速云盘</p>
            </div>
            <!-- 将网盘 logo 和用户信息隔开 -->
            <div class="header-center"></div>
            <div class="header-right">
                <el-dropdown @command="handleImage">
                    <el-avatar :src="headImage"></el-avatar>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="1" style="z-index: 1;position: relative">
                                <span>修改头像</span>
                                <input type="file" title="选择文件" name="file" id="uploadImage" @change="uploadHeadImg"
                                    accept="*/*" multiple hidden
                                    style="z-index:-1;position:absolute;top:0;left:0;width:100%;height:100%;opacity: 0; cursor: pointer">
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
                <p>{{ userName }}</p>
                <el-button link @click="loginOut">退出登录</el-button>
            </div>
        </div>
    </header>
</template>
<script>
import API from "../api/api_user";
import headImage from "@/assets/1.jpg";
import $ from "jquery";
export default {
    name: "TopBar",
    data() {
        return {
            userName: "zhou",
            headImage
        }
    },
    created() {
        var user = JSON.parse(window.localStorage.getItem("currentUser"));
        this.userName = user.username;
        if (user.imagePath != null) {
            this.headImage = user.imagePath;
        }
    },
    methods: {
        loginOut(){
            let that = this;
            API.loginOut().then(
                function(res){
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        localStorage.removeItem("access-token");
                        localStorage.removeItem("currentUser");
                        that.$router.push({ path: "/login" });
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        //控制文件上传
        handleImage(command) {
            //上传文件
            if (command == 1) {
                $("#uploadImage").click();
            }
        },
        uploadHeadImg(event){
            let that = this;
            var file = event.target.files[0];
            var formData = new FormData();
            formData.append("headImage", file);
            API.uploadHeadImage(formData).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        window.localStorage.removeItem("currentUser");
                        window.localStorage.setItem("currentUser", JSON.stringify(res.data.currentUser))
                        that.headImage = res.data.headImage;
                        $("#uploadFile").val('');
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        }
    }
}
</script>

<style scoped>
header {
    height: 55px;
    box-shadow: 0 3px 10px 0 rgb(0 0 0 / 6%);
    padding: 0 24px;
}

.top-flex-container,
.header-left,
.header-right {
    display: flex;
    align-items: center;
}

.header-left {
    justify-content: space-between;
    min-width: 110px;
}

.header-right {
    justify-content: space-between;
    min-width: 156px;
}

.header-center {
    flex: 1 100px;
    min-width: 100px;
}
</style>