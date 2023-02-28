<template>
    <el-container class="full-container">
        <!--顶栏-->
        <el-header class="full-header">
            <TopBar></TopBar>
        </el-header>
        <!--body主体-->
        <div class="full-body">
            <el-row>
                <!--body左侧-->
                <div class="body-left">
                    <el-row>
                        <div class="sideBar">
                            <SideBar :pageIndex="pageIndex"></SideBar>
                        </div>
                        <el-main class="sideBar-menu greyColor">
                            <el-menu class="el-menu-vertical-demo midMenu greyColor" @open="handleOpen" @close="handleClose"
                                :default-active="$route.path" router>
                                <el-sub-menu index="1">
                                    <template #title>
                                        <RouterLink to="/index/files" class="menuTitle">
                                            <span>&nbsp;&nbsp;&nbsp;&nbsp;全 部 文 件&nbsp;&nbsp;</span>
                                        </RouterLink>
                                    </template>
                                    <el-menu-item class="greyColor" index="/index/document">文档</el-menu-item>
                                    <el-menu-item class="greyColor" index="/index/video">视频</el-menu-item>
                                    <el-menu-item class="greyColor" index="/index/music">音乐</el-menu-item>
                                    <el-menu-item class="greyColor" index="/index/other">其他</el-menu-item>
                                    <el-menu-item class="greyColor" index="/index/image">图片</el-menu-item>
                                </el-sub-menu>
                            </el-menu>
                        </el-main>
                    </el-row>
                </div>
                <!--body右侧文件信息-->
                <el-main class="body-right">
                    <RouterView></RouterView>
                </el-main>
            </el-row>
        </div>
    </el-container>
</template>

<script>
import ViewLog from './ViewLog.vue';
import TopBar from '@/components/TopBar.vue';
import SideBar from '@/components/SideBar.vue';
import ElSubmenu from 'element-plus';
import FileList from '@/view/Files.vue';
import DocumentTable from '../components/DocumentTable.vue';
import bus from "../bus";
export default {
    components: {
        TopBar,
        SideBar,
        ElSubmenu,
        FileList,
        DocumentTable
    },
    data() {
        return {
            pageIndex: 1
        }
    },
    created(){
        bus.on("goto", url => {
            if (url === "/login") {
                localStorage.removeItem("access-user");
            }
            this.$router.push(url);
        });
        this.defaultActiveIndex = this.$route.path;
    },
    methods: {
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        }
    }
}
</script>
<style scoped>
.el-main {
    padding: 10px !important;
}

.full-container {
    width: 100%;
    height: 100%;
    position: fixed;
}

.full-header {
    width: 100%;
    height: fit-content;
    padding-left: 0px;
    padding-right: 0px;
}

.full-body {
    width: 100%;
    height: 1000px;
}

.body-left {
    width: 300px;
    height: 1000px;
}

.body-right {
    width: calc(100% - 300px);
    height: 1000px;
}

.sideBar {
    width: 100px;
    height: 1000px;
}

.sideBar-menu {
    padding-top: 10px;
    width: 180px;
    height: 1000px;
}

.greyColor {
    background-color: rgb(249, 250, 251);
}
.menuTitle{
    text-decoration: none;
}
.midMenu{
    width:190px;
}
</style>