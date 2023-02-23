<template>
    <div class="flex-container">
        <el-col>
            <el-menu class="el-menu-vertical-demo">
                <el-menu-item index="1">
                    <el-col>
                        <div class="menu-image"><img src="../assets/sidebar/index.png" /></div>
                        <div class="menu-title"><span>首页</span></div>
                    </el-col>
                </el-menu-item>
                <el-menu-item index="2">
                    <el-col>
                        <div class="menu-image"><img src="../assets/sidebar/sharing.png" /></div>
                        <div class="menu-title"><span slot="title">分享</span></div>
                    </el-col>
                </el-menu-item>
                <el-menu-item index="3">
                    <el-col>
                        <div class="menu-image"><img src="../assets/sidebar/about.png" /></div>
                        <div class="menu-title"><span slot="title">关于我们</span></div>
                    </el-col>
                </el-menu-item>
                <el-menu-item index="4">
                    <el-col>
                        <div class="menu-image"><i class="el-icon-setting"></i></div>
                        <div class="menu-title"><span slot="title">云盘使用</span></div>
                    </el-col>
                </el-menu-item>
            </el-menu>
        </el-col>
    </div>
</template>
<script>
export default {
    name: "SideBar"
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