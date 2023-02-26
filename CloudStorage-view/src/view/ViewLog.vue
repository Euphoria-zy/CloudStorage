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
                    <div class="sideBar">
                        <SideBar :pageIndex="pageIndex"></SideBar>
                    </div>
                </el-main>
                <!--body右侧文件信息-->
                <el-main class="body-right">
    <div id="logTable">
        <el-table
            :data="tableData.filter(data => !search || data.fileFolderName.toLowerCase().includes(search.toLowerCase()))"
            style="width: 100%">
            <el-table-column prop="formatTime" label="时间" sortable width="210">
            </el-table-column>
            <el-table-column prop="operationName" label="操作类型" width="160" :filters="[
                { text: '上传', value: '上传' },
                { text: '下载', value: '下载' },
                { text: '新建文件夹', value: '新建文件夹' },
                { text: '移动', value: '移动' },
                { text: '复制', value: '复制' },
                { text: '重命名', value: '重命名' },
                { text: '删除', value: '删除' }]" :filter-method="chooseType" filter-placement="bottom-end">
                <template #default="scope">
                    <el-tag style="width:82px;text-align:center" type="
                            scope.row.operationType === 1 ? 'success' :
                                scope.row.operationType === 2 ? 'primary' :
                                    scope.row.operationType === 3 ? 'success' :
                                        scope.row.operationType === 4 ? 'success' :
                                            scope.row.operationType === 5 ? 'success' :
                                                scope.row.operationType === 6 ? 'primary' :
                                                    scope.row.operationType === 7 ? 'warning' :
                                                        'info'" disable-transitions>
                        {{ scope.row.operationName }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column width="150" label="操作结果">
                <template #default="scope">
                    <el-tag type="
                            scope.row.operationSuccess ? 'info' :
                                'warning'" disable-transitions>
                        <p v-if="scope.row.operationSuccess">操作成功</p>
                        <p v-else>操作失败</p>
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="类型" width="120">
                <template #default="scope">
                    <template v-if="scope.row.file"><i class="el-icon-folder"></i>&nbsp;文件</template>
                    <template v-else><i class="el-icon-folder-opened"></i>&nbsp;文件夹</template>
                </template>
            </el-table-column>
            <el-table-column prop="fileFolderName" label="文件名">
            </el-table-column>
            <el-table-column width="240" align="right">
                <template #header>
                    <el-input v-model="search" size="mini" placeholder="根据文件名搜索" />
                </template>
            </el-table-column>
            <el-table-column width="180" align="center">
                <template #header>
                    <el-button type="danger" size="small" @click="handleClear()">清空记录</el-button>
                </template>
                <template #default="scope">
                    <el-button type="danger" size="small" round plain @click="handleDelete(scope.row)">删除记录</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
                </el-main>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
import TopBar from '@/components/TopBar.vue';
import SideBar from '@/components/SideBar.vue';
import API from '../api/api_log'; 
export default {
    name: "ViewLog",
    components: {
        TopBar,
        SideBar
    },
    data() {
        return {
            tableData: [
                /*{
                    formatTime: "2023.03.23.16.14",
                    operationType: "1",
                    operationName: "上传",
                    operationSuccess: "true",
                    file: "true",
                    fileFolderName: "test.txt",
                },
                {
                    formatTime: "2023.03.24.16.14",
                    operationType: "2",
                    operationName: "下载",
                    operationSuccess: "false",
                    file: "true",
                    fileFolderName: "abc.txt",
                },*/
            ],
            search: ''
        }
    },
    created() {
        this.getAll();
    },
    methods: {
        getAll() {
            let that = this;
            API.getAllLog().then(
                function(res){
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.tableData = res.data.logList;
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        refresh() {
            this.getAll();
        },
        chooseType(value, row) {
            return row.operationName === value;
        },
        handleDelete(row) {
            this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then((action) => {
                if (action == 'confirm') {
                    this.deleteLog(row.logId);
                }
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        handleClear() {
            this.$confirm('此操作将永久删除全部记录, 是否继续?', '提示', {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then((action) => {
                if (action == 'confirm') {
                    this.clearLog();
                }
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        deleteLog(logId) {
            let that = this;
            var params = new URLSearchParams();
            params.append("logId", logId);
            API.deleteLog(params).then(
                function(res){
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.refresh();
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        clearLog() {
            let that = this;
            API.clearLog().then(
                function(res){
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.refresh();
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
        width: 8%;
        height: 100%;
    }
    .body-right {
        width: 92%;
        height: 622px;
    }
    .sideBar {
        width: 100%;
        height: 600px;
    }
</style>