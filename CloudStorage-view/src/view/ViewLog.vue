<template>
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
</template>

<script>
import axios from 'axios';
export default {
    name: "ViewLog",
    data() {
        return {
            tableData: [
                {
                    formatTime: "2023.03.23.16.14",
                    operationType: "1",
                    operationName: "上传",
                    operationSuccess: "true",
                    file: "true",
                    fileFolderName: "test.txt",
                },
            ],
            search: ''
        }
    },
    created() {
        /*this.getAll();*/
    },
    methods: {
        getAll() {
            axios
                .post("/index/log-list")        //有时会无法向后端传值
                .then((res) => {
                    if (res.data.flag) {
                        this.$message({ message: res.data.msg, type: "success" });
                        this.tableData = res.data.data.logList;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
        },
        refresh() {
            axios
                .post("/index/log-list")        //有时会无法向后端传值
                .then((res) => {
                    if (res.data.flag) {
                        this.tableData = res.data.data.logList;
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
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
            var params = new URLSearchParams();
            params.append("logId", logId);
            axios
                .post("/deleteLog", params)
                .then((res) => {
                    if (res.data.flag) {
                        this.$message({ message: res.data.msg, type: "success" });
                        this.refresh();
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
        },
        clearLog() {
            axios
                .post("/clearLog")
                .then((res) => {
                    if (res.data.flag) {
                        this.$message({ message: res.data.msg, type: "success" });
                        this.refresh();
                    } else {
                        this.$message.error(res.data.msg);
                    }
                });
        }
    }
}
</script>
