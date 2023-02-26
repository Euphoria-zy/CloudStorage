<template>
    <div class="flex-container">
        <el-col>
            <el-menu class="el-menu-vertical-demo" :default-active="$route.path" router>
                <el-menu-item index="/index">
                    <el-col>
                            <div class="menu-image" v-if="pageIndex == 1">
                                <img src="../assets/sidebar/index-active.png" />
                            </div>
                            <div class="menu-image" v-else>
                                <img src="../assets/sidebar/index.png" />
                            </div>
                            <div class="menu-title"><span>首页</span></div>
                    </el-col>
                </el-menu-item>
                <el-menu-item index="2">
                    <el-col>
                        <div class="menu-image" v-if="pageIndex == 2">
                            <img src="../assets/sidebar/sharing-active.png" />
                        </div>
                        <div class="menu-image" v-else>
                            <img src="../assets/sidebar/sharing.png" />
                        </div>
                        <div class="menu-title"><span>分享</span></div>
                    </el-col>
                </el-menu-item>
                <el-menu-item index="3">
                    <el-col>
                        <div class="menu-image" v-if="pageIndex == 3">
                            <img src="../assets/sidebar/about-active.png" />
                        </div>
                        <div class="menu-image" v-else>
                            <img src="../assets/sidebar/about.png" />
                        </div>
                        <div class="menu-title"><span>关于我们</span></div>
                    </el-col>
                </el-menu-item>
                <el-menu-item index="/viewLog">
                    <el-col>
                            <div class="menu-image" v-if="pageIndex == 4">
                            </div>
                            <div class="menu-image" v-else>
                            </div>
                            <div class="menu-title"><span>日志记录</span></div>
                    </el-col>
                </el-menu-item>
            </el-menu>
        </el-col>
    </div>
</template>
<script>
import { RouterLink } from 'vue-router';
export default {
    name: "SideBar",
    props: {
        pageIndex: Number,
        required: true
    }
}
</script>

<style scoped>
    .el-col {
        width: 100%;
        height: 100%;
    }
    .el-menu {
        width: 100%;
        height: max-content;
    }
    .el-menu-item {
        width: 80px;
        height: 80px;
        align-items: center;
        padding-left: 15px !important;
        line-height: 20px;
    }
    .menu-image {
        width: 50px;
        height: 50px;
    }
    .menu-image img{
        width: 50px;
        height: 50px;
    }
    .menu-title{
        width: 50px;
        height: 20px;
        text-align: center;
    }
    .menu-title span{
        text-align: center;
    }
</style>