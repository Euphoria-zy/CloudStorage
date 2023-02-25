<template>
    <el-container class="full-container">
        <!--顶栏-->
        <el-header class="full-header">
            <TopBar></TopBar>
        </el-header>
        <!--body主体-->
        <el-main class="full-body">
            <el-row>
                <!--body左侧-->
                <el-main class="body-left">
                    <el-row>
                        <div class="sideBar">
                            <SideBar :pageIndex="pageIndex"></SideBar>
                        </div>
                        <div class="sideBar-menu">
                            <el-col>
                                <el-menu 
                                    class="el-menu-vertical-demo"
                                    @open="handleOpen"
                                    @close="handleClose"
                                    :default-active="$route.path" router>
                                    <el-sub-menu index="1">
                                        <template #title>
                                            <RouterLink to="/index/files">
                                                <span>&nbsp;&nbsp;&nbsp;&nbsp;全 部 文 件&nbsp;&nbsp;</span>
                                            </RouterLink>
                                        </template>
                                        <el-menu-item-group>
                                            <el-menu-item index="/index/document">文档</el-menu-item>
                                            <el-menu-item index="/index/video">视频</el-menu-item>
                                            <el-menu-item index="/index/music">音乐</el-menu-item>
                                            <el-menu-item index="/index/other">其他</el-menu-item>
                                            <el-menu-item index="/index/image">图片</el-menu-item>
                                        </el-menu-item-group>
                                    </el-sub-menu>
                                </el-menu>
                            </el-col>
                        </div>
                    </el-row>
                </el-main>
                <!--body右侧文件信息-->
                <el-main class="body-right">
                    <RouterView></RouterView>
                </el-main>
            </el-row>
        </el-main>
    </el-container>
    
</template>

<script>
import ViewLog from './ViewLog.vue';
import TopBar from '@/components/TopBar.vue';
import SideBar from '@/components/SideBar.vue';
import ElSubmenu from 'element-plus';
import FileList from '@/view/Files.vue';
import DocumentTable from '../components/DocumentTable.vue';
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
    }
    .full-body {
        width: 100%;
        height: 600px;
        margin-top: 5px;
    }
    .body-left {
        width: 18%;
        height: 100%;
    }
    .body-right {
        width: 82%;
        height: 622px;
    }
    .sideBar {
        width: 32%;
        height: 600px;
    }
    .sideBar-menu {
        width: 60%;
        height: 600px;
    }
    a {
        text-decoration: none;
        color: black;
    }
    .router-link-active {
        text-decoration: none;
        color: blue;
    }
</style>