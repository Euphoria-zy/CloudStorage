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
                <div class="body-left">
                    <div class="sideBar">
                        <SideBar :pageIndex="pageIndex"></SideBar>
                    </div>
                </div>
                <!--body右侧文件信息-->
                <div class="body-right">
                    <DocumentTable></DocumentTable>
                </div>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
import TopBar from '@/components/bar/TopBar.vue';
import SideBar from '@/components/bar/SideBar.vue';
import DocumentTable from '@/components/table/DocumentTable.vue';
import ElSubmenu from 'element-plus';
import bus from "../bus";
export default {
    components: {
        TopBar,
        SideBar,
        ElSubmenu,
        DocumentTable
    },
    data() {
        return {
            pageIndex: 2
        }
    },
    created() {
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
    width: 90px;
    height: 1000px;
}

.body-right {
    width: calc(100% - 90px);
    height: 1000px;
}

.menuTitle {
    text-decoration: none;
}
</style>