<template>
    <div>
        <el-menu class="el-menu-vertical-demo leftMenu" :default-active="$route.path" router>
            <el-menu-item index="/index">
                <el-col>
                    <div class="menu-image" v-if="pageIndex == 1">
                        <img src="../../assets/sidebar/index-active.png" />
                    </div>
                    <div class="menu-image" v-else>
                        <img src="../../assets/sidebar/index.png" />
                    </div>
                    <div class="menu-title"><span>全部文件</span></div>
                </el-col>
            </el-menu-item>
            <el-menu-item index="/document">
                <el-col>
                    <div class="menu-image" v-if="pageIndex == 2">
                        <img src="../../assets/sidebar/sharing-active.png" />
                    </div>
                    <div class="menu-image" v-else>
                        <img src="../../assets/sidebar/sharing.png" />
                    </div>
                    <div class="menu-title"><span>文档</span></div>
                </el-col>
            </el-menu-item>
            <el-menu-item index="/video">
                <el-col>
                    <div class="menu-image" v-if="pageIndex == 3">
                        <img src="../../assets/sidebar/sharing-active.png" />
                    </div>
                    <div class="menu-image" v-else>
                        <img src="../../assets/sidebar/sharing.png" />
                    </div>
                    <div class="menu-title"><span>视频</span></div>
                </el-col>
            </el-menu-item>
            <el-menu-item index="/music">
                <el-col>
                    <div class="menu-image" v-if="pageIndex == 4">
                        <img src="../../assets/sidebar/sharing-active.png" />
                    </div>
                    <div class="menu-image" v-else>
                        <img src="../../assets/sidebar/sharing.png" />
                    </div>
                    <div class="menu-title"><span>音乐</span></div>
                </el-col>
            </el-menu-item>
            <el-menu-item index="/image">
                <el-col>
                    <div class="menu-image" v-if="pageIndex == 5">
                        <img src="../../assets/sidebar/sharing-active.png" />
                    </div>
                    <div class="menu-image" v-else>
                        <img src="../../assets/sidebar/sharing.png" />
                    </div>
                    <div class="menu-title"><span>图片</span></div>
                </el-col>
            </el-menu-item>
            <el-menu-item index="/other">
                <el-col>
                    <div class="menu-image" v-if="pageIndex == 6">
                        <img src="../../assets/sidebar/sharing-active.png" />
                    </div>
                    <div class="menu-image" v-else>
                        <img src="../../assets/sidebar/sharing.png" />
                    </div>
                    <div class="menu-title"><span>其他</span></div>
                </el-col>
            </el-menu-item>
            <el-menu-item index="/viewLog">
                <el-col>
                    <div class="menu-image" v-if="pageIndex == 7">
                        <img src="../../assets/sidebar/about-active.png" />
                    </div>
                    <div class="menu-image" v-else>
                        <img src="../../assets/sidebar/about.png" />
                    </div>
                    <div class="menu-title"><span>日志记录</span></div>
                </el-col>
            </el-menu-item>
        </el-menu>
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


.menu-image img {
    margin-top: 10px;
    width: 50px;
    height: 50px;
}

.menu-title {
    width: 50px;
    height: 20px;
    text-align: center;
}

.menu-title span {
    text-align: center;
}

</style>