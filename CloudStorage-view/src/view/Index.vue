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
                            <SideBar></SideBar>
                        </div>
                        <div class="sideBar-menu">
                            <el-col>
                                <el-menu 
                                    class="el-menu-vertical-demo"
                                    @open="handleOpen"
                                    @close="handleClose">
                                    <el-submenu index="1">
                                        <template slot="title">
                                            <i class="el-icon-location"></i>
                                            <span>全部文件</span>
                                        </template>
                                        <el-menu-item-group>
                                            <el-menu-item index="1-1">文档</el-menu-item>
                                            <el-menu-item index="1-2">图片</el-menu-item>
                                        </el-menu-item-group>
                                    </el-submenu>
                                </el-menu>
                            </el-col>
                        </div>
                    </el-row>
                </el-main>
                <!--body右侧文件信息-->
                <el-main class="body-right">
                    <ViewLog></ViewLog>
                </el-main>
            </el-row>
        </el-main>
    </el-container>
    
</template>

<script>
import ViewLog from './ViewLog.vue';
import TopBar from '@/components/TopBar.vue';
import SideBar from '../components/SideBar.vue';
export default {
    components: {
        TopBar,
        SideBar,
        ViewLog
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
<style type="scope">
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
        border: 1px solid;
    }
</style>