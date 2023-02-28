<template>
    <el-container>
        <!--table head-->
        <el-header>
            <el-upload class="upload-demo" action="${api.baseurl}/uploadFile" multiple :data="{ 'nowFolderId': nowFolderId }"
                :on-success="uploadSuccess" :limit="3">
                <el-dropdown @command="handleCommand">
                    <el-button type="success" class="el-dropdown-link">
                        <el-icon>
                            <Upload />
                        </el-icon>
                        <span>上传</span>
                    </el-button>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="1" style="z-index: 1;position: relative">
                                <span>上传文件</span>
                                <input type="file" title="选择文件" name="file" id="uploadFile" @change="uploadFile"
                                    accept="*/*" multiple hidden
                                    style="z-index:-1;position:absolute;top:0;left:0;width:100%;height:100%;opacity: 0; cursor: pointer">
                            </el-dropdown-item>
                            <el-dropdown-item command="2" style="z-index: 1;position: relative">
                                <span>上传文件夹</span>
                                <input type="file" title="选择文件夹" id="uploadFolder" name="folder" @change="uploadFolder"
                                    accept="*/*" multiple hidden webkitdirectory
                                    style="z-index:-1;position:absolute;top:0;left:0;width:100%;height:100%;opacity: 0; cursor:pointer">
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-upload>
            <el-divider />
        </el-header>
        <!--table body-->
        <el-main>
            <h3 style="margin-top: 0px;margin-bottom: 0px;">全部音频</h3>
            <el-table id="folder-table" ref="multipleTable" :data="datalist" stripe tooltip-effect="dark"
                style="width: 100%" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <el-table-column width="50">
                    <template #default="scope">
                        <div class="demo-image__preview">
                            <el-image style="width: 32px; height: 32px" :src="scope.row.fileImage">
                            </el-image>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="名称" width="120">
                    <template #default="scope">{{ scope.row.fileName }}</template>
                </el-table-column>
                <el-table-column label="类型" width="120">
                    <template #default="scope">
                        音频
                    </template>
                </el-table-column>
                <el-table-column label="大小" width="120">
                    <template #default="scope">{{ scope.row.fileSize + scope.row.unit }}</template>
                </el-table-column>
                <el-table-column label="下载次数" width="120">
                    <template #default="scope">{{ scope.row.downloadCount }}</template>
                </el-table-column>
                <el-table-column label="添加时间" width="150" :formatter="formatDate">
                </el-table-column>
                <el-table-column label="操作" width="300">
                    <template #default="scope">
                        <el-row>
                            <el-tooltip class="item" effect="light" content="下载" placement="bottom-start">
                                <el-button type="success" size="small" :icon="Download" circle
                                    @click="handleDownLoad(scope.row)">
                                    <el-icon>
                                        <Download />
                                    </el-icon>
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="light" content="分享" placement="bottom-start">
                                <el-button type="info" size="small" :icon="Share" circle @click="handleShare(scope.row)">
                                    <el-icon>
                                        <Share />
                                    </el-icon>
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="light" content="删除" placement="bottom-start">
                                <el-button type="danger" size="small" circle @click="handleDelete(scope.row)">
                                    <el-icon>
                                        <Delete />
                                    </el-icon>
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="light" content="重命名" placement="bottom-start">
                                <el-button type="primary" size="small" circle @click="handleEdit(scope.row)">
                                    <el-icon>
                                        <Edit />
                                    </el-icon>
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="light" content="复制" placement="bottom-start">
                                <el-button type="text" size="small" circle @click="handleCopy(scope.row)">
                                    <el-icon>
                                        <CopyDocument />
                                    </el-icon>
                                </el-button>
                            </el-tooltip>
                            <el-tooltip class="item" effect="light" content="移动" placement="bottom-start">
                                <el-button type="warning" size="small" circle @click="handleMove(scope.row)">
                                    <el-icon>
                                        <Rank />
                                    </el-icon>
                                </el-button>
                            </el-tooltip>
                        </el-row>
                    </template>
                </el-table-column>
            </el-table>

            <!--复制文件或文件夹dialog-->
            <div id="copyFolderOrFileDialog">
                <el-dialog title="复制到" v-model="copyDialogTableVisible">
                    <!--导航面包屑-->
                    <el-breadcrumb separator-class="el-icon-arrow-right">
                        <transition-group>
                            <el-breadcrumb-item :key="0">
                                <a @click.prevent="enterFolder(0)">根目录</a>
                            </el-breadcrumb-item>
                            <el-breadcrumb-item v-for="(item, index) in folderLevel" :key="item.folderId">
                                <a @click.prevent="enterFolder(item.folderId)">{{ item.folderName }}</a>
                            </el-breadcrumb-item>
                            <el-breadcrumb-item v-if="nowCopyFolder.folderId != 0" :key="nowCopyFolder.folderId">
                                <a @click.prevent="enterFolder(nowCopyFolder.folderId)">{{ nowCopyFolder.folderName }}</a>
                            </el-breadcrumb-item>
                        </transition-group>
                        <el-breadcrumb-item v-if="nowCopyFolder.folderId != 0">
                            <a @click.prevent="enterFolder(nowCopyFolder.parentFolderId)">&nbsp;&nbsp;|&nbsp;返回上一级</a>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                    <el-divider></el-divider>
                    <el-table :data="folderData" :show-header="false" @row-dblclick="handleDblClick">
                        <el-table-column label="图标" width="50">
                            <template>
                                <el-image style="width: 32px; height: 32px" :src="initUrl">
                                </el-image>
                            </template>
                        </el-table-column>
                        <el-table-column property="folderName" label="文件名" width="400"></el-table-column>
                    </el-table>
                    <el-divider />
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="copyDialogTableVisible = false" round>取 消</el-button>
                        <el-button type="primary" @click="copyFileOrFolder(nowCopyFolder.folderId)" round>复制到此处</el-button>
                    </div>
                </el-dialog>
            </div>

            <!--移动文件或文件夹dialog-->
            <div id="moveFolderOrFileDialog">
                <el-dialog title="移动到" v-model="moveDialogTableVisible">
                    <!--导航面包屑-->
                    <el-breadcrumb separator-class="el-icon-arrow-right">
                        <transition-group>
                            <el-breadcrumb-item :key="0">
                                <a @click.prevent="enterFolder(0)">根目录</a>
                            </el-breadcrumb-item>
                            <el-breadcrumb-item v-for="(item, index) in folderLevel" :key="item.folderId">
                                <a @click.prevent="enterFolder(item.folderId)">{{ item.folderName }}</a>
                            </el-breadcrumb-item>
                            <el-breadcrumb-item v-if="nowCopyFolder.folderId != 0" :key="nowCopyFolder.folderId">
                                <a @click.prevent="enterFolder(nowCopyFolder.folderId)">{{ nowCopyFolder.folderName }}</a>
                            </el-breadcrumb-item>
                        </transition-group>
                        <el-breadcrumb-item v-if="nowCopyFolder.folderId != 0">
                            <a @click.prevent="enterFolder(nowCopyFolder.parentFolderId)">&nbsp;&nbsp;|&nbsp;返回上一级</a>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                    <el-divider></el-divider>
                    <el-table :data="folderData" :show-header="false" @row-dblclick="handleDblClick">
                        <el-table-column label="图标" width="80">
                            <template>
                                <el-image style="width: 32px; height: 32px" :src="initUrl">
                                </el-image>
                            </template>
                        </el-table-column>
                        <el-table-column property="folderName" label="文件名" width="400"></el-table-column>
                    </el-table>
                    <el-divider />
                    <div slot="footer" class="dialog-footer">
                        <el-button @click="moveDialogTableVisible = false" round>取 消</el-button>
                        <el-button type="primary" @click="moveFileOrFolder(nowCopyFolder.folderId)" round>移动到此处</el-button>
                    </div>
                </el-dialog>
            </div>
        </el-main>
    </el-container>
</template>
<script>
import API from '../../api/api_file';
import api from '../../api/index.js';
import { Delete, Edit, Download, Share, Rank, CopyDocument, } from '@element-plus/icons-vue';
import $ from "jquery";
export default {
    name: "MusicTable",
    components: {
        Delete, Edit, Download, Share, Rank, CopyDocument
    },
    data() {
        return {
            visible: false,
            datalist: [],
            copyDialogTableVisible: false,
            moveDialogTableVisible: false,
            nowFolderId: null,
            folderData: [],
            folderLevel: [],
            nowCopyFolder: {},
            copyAndMoveId: null,
            operateType: null,
            initUrl: "https://img.51miz.com/Element/00/38/10/50/ed73a715_E381050_ba39adab.png",
        }
    },
    created() {
        this.getAll();
    },
    methods: {
        getAll() {
            //发送异步post请求
            let that = this;
            var params = new URLSearchParams();
            params.append("fileType", 4);
            API.getFileByType(params).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.datalist = res.data.datalist;
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        uploadSuccess(res) {
            if (res.flag) {
                this.$message({ message: res.msg, type: "success" });
                this.getAll();
            } else {
                this.$message.error(res.msg);
            }
        },
        //控制文件上传
        handleCommand(command) {
            //上传文件
            if (command == 1) {
                $("#uploadFile").click();
            }//上传文件夹
            else {
                $("#uploadFolder").click();
            }
        },
        uploadFile(event) {
            let that = this;
            var file = event.target.files[0];
            var formData = new FormData();
            formData.append("file", file);
            formData.append("nowFolderId", 0);
            API.uploadFile(formData).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.getAll();
                        $("#uploadFile").val('');
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        uploadFolder(event) {
            let that = this;
            var files = event.target.files;
            var formData = new FormData();
            for (var i = 0; i < files.length; i++) {
                // "file"是后台接口的参数名
                formData.append("file", files[i]);
            }
            formData.append("nowFolderId", 0);
            API.uploadFolder(formData).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        console.log(res.msg);
                        that.getAll();
                        $("#uploadFolder").val('');
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        handleDblClick(row) {
            this.enterFolder(row.folderId);
        },
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        formatDate(row) {
            // 获取单元格数据
            var data = null;
            if (row.createTime != null)
                data = row.createTime;
            if (row.uploadTime != null)
                data = row.uploadTime;
            console.log(data);
            if (data == null) {
                return null;
            }
            let dt = new Date(data)
            console.log(dt);
            var year = dt.getFullYear();
            var mouth = this.add0((dt.getMonth() + 1));
            var day = this.add0(dt.getDate());
            var hour = this.add0(dt.getHours());
            var minutes = this.add0(dt.getMinutes());
            return year + '-' + mouth + '-' + day + ' ' + hour + ':' + minutes;
        },
        handleDownLoad(row) {
            let that = this;
            console.log(row.fileId);
            //发送异步post请求
            var params = new URLSearchParams();
            params.append("fileId", row.fileId);
            API.downloadFile(params).then(
                function (res) {
                    if (res.flag) {
                        let link = document.createElement("a"); //创建a标签
                        link.style.display = "none"; //使其隐藏
                        link.href = res.data.filePath; //赋予文件下载地址
                        link.setAttribute("download", res.data.fileName); //设置下载属性 以及文件名
                        document.body.appendChild(link); //a标签插至页面中
                        link.click(); //强制触发a标签事件
                        that.$message({ message: res.msg, type: "success" });
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
            this.getAll();
        },
        handleEdit(row) {
            var hint, inputErrorMsg, inputRule, inputValue;
            var that = this;
            if (row.fileType != null) {
                hint = "请输入文件名: ";
                inputErrorMsg = "文件名称格式不对!";
                inputRule = /[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$/;//文件名格式判断
                inputValue = row.fileName + row.postfix;//文本框默认值
            } else {
                hint = "请输入文件夹名: ";
                inputErrorMsg = "文件夹名称格式不对!"
                inputRule = /^[\u4E00-\u9FA5A-Za-z0-9]{1,20}$/;
                inputValue = row.folderName;
            }
            this.$prompt(hint, '重命名', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                inputPattern: inputRule,
                inputErrorMessage: inputErrorMsg,
                inputValue: inputValue
            }).then(({ value }) => {
                console.log(value);
                this.editFileName(row.fileId, instance.inputValue, row.folderId);
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '取消输入'
                });
            });
        },
        editFileName(fileId, name, nowFolderId) {
            let that = this;
            //发送异步post请求
            var params = new URLSearchParams();
            params.append("fileId", fileId);
            params.append("newFileName", name);
            params.append("parentFolderId", nowFolderId)
            API.renameFile(params).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.getAll();
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        //删除文件
        handleDelete(row) {
            this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                confirmButtonText: '删除',
                cancelButtonText: '取消',
                type: 'warning'
            }).then((action) => {
                if (action == 'confirm') {
                    this.deleteFile(row.fileId, row.folderId);
                }
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });

        },
        deleteFile(fileId, nowFolderId) {
            let that = this;
            var params = new URLSearchParams();
            params.append("fileId", fileId);
            API.deleteFile(params).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.getAll();
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        //复制文件或文件夹
        handleCopy(row) {
            this.copyDialogTableVisible = true;
            if (row.fileType == null) {
                //移动文件夹
                this.copyAndMoveId = row.folderId;
                this.operateType = 1;
            } else {
                //移动文件
                this.copyAndMoveId = row.fileId;
                this.operateType = 2;
            }
            this.enterFolder(0);
        },
        enterFolder(nowFolderId) {
            //发送异步post请求
            let that = this;
            var params = new URLSearchParams();
            params.append("nowFolderId", nowFolderId);
            API.enterFolder(params).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.folderData = res.data.folderData;
                        that.nowCopyFolder = res.data.nowFolder;
                        that.folderLevel = res.data.location;
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        copyFileOrFolder(parentFolderId) {
            //发送异步post请求
            let that = this;
            this.copyDialogTableVisible = false;
            var params = new URLSearchParams();
            params.append("operateType", this.operateType);
            params.append("operateId", this.copyAndMoveId);
            params.append("parentFolderId", parentFolderId);
            API.copyFileOrFolder(params).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.getAll();
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        //移动文件或文件夹
        handleMove(row) {
            this.moveDialogTableVisible = true;
            if (row.fileType == null) {
                //移动文件夹
                this.copyAndMoveId = row.folderId;
                this.operateType = 1;
            } else {
                //移动文件
                this.copyAndMoveId = row.fileId;
                this.operateType = 2;
            }
            this.enterFolder(0);
        },
        moveFileOrFolder(parentFolderId) {
            //发送异步post请求
            let that = this;
            this.moveDialogTableVisible = false;
            var params = new URLSearchParams();
            params.append("operateType", this.operateType);
            params.append("operateId", this.copyAndMoveId);
            params.append("parentFolderId", parentFolderId);
            API.moveFileOrFolder(params).then(
                function (res) {
                    if (res.flag) {
                        that.$message({ message: res.msg, type: "success" });
                        that.getAll();
                    } else {
                        that.$message.error(res.msg);
                    }
                }
            )
        },
        add0(date) {
            return date < 10 ? '0' + date : date;
        }
    }
}     
</script>
<style scoped>
.el-header {
    margin-top: 20px;
}
</style>