<template>
    <div id="app">
        <el-container>
            <el-header>
                <el-row>
                    <el-col :span="3">
                        <el-upload
                                class="upload-demo"
                                action="/uploadFile"
                                multiple
                                :data="{'nowFolderId': nowFolderId}"
                                :on-success="uploadSuccess"
                                :limit="3">
                            <el-dropdown @command="handleCommand">
                                <el-button type="success" class="el-dropdown-link">
                                    <i class="el-icon-upload"></i>
                                    <span>上传</span>
                                </el-button>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item command="1" style="z-index: 1;position: relative">
                                        <span>上传文件</span>
                                        <input type="file" title="选择文件" name="file" id="uploadFile" @change="uploadFile" accept="*/*" multiple hidden style="z-index:-1;position:absolute;top:0;left:0;width:100%;height:100%;opacity: 0; cursor: pointer">
                                    </el-dropdown-item>
                                    <el-dropdown-item command="2" style="z-index: 1;position: relative">
                                        <span>上传文件夹</span>
                                        <input type="file" title="选择文件夹" id="uploadFolder" name="folder" @change="uploadFolder" accept="*/*" multiple hidden webkitdirectory style="z-index:-1;position:absolute;top:0;left:0;width:100%;height:100%;opacity: 0; cursor:pointer">
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </el-upload>
                    </el-col>
                    <el-col :span="3">
                        <el-button type="primary" @click="addFolderFormVisible = true">
                            <i class="el-icon-folder-add"></i>
                            <span>新建文件夹</span>
                        </el-button>
                    </el-col>
                </el-row>
            </el-header>
            <el-divider></el-divider>
            <el-main>
                <el-breadcrumb separator-class="el-icon-arrow-right">
                    <transition-group>
                        <el-breadcrumb-item :key="0">
                            <a @click.prevent="handleLink(0)">根目录</a>
                        </el-breadcrumb-item>
                        <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.folderId">
                            <a @click.prevent="handleLink(item.folderId)">{{ item.folderName }}</a>
                        </el-breadcrumb-item>
                        <el-breadcrumb-item v-if="nowFolder.folderId != 0" :key="nowFolder.folderId">
                            <a @click.prevent="handleLink(nowFolder.folderId)">{{ nowFolder.folderName }}</a>
                        </el-breadcrumb-item>
                    </transition-group>
                    <el-breadcrumb-item v-if="nowFolder.folderId != 0">
                        <a @click.prevent="handleLink(nowFolder.parentFolderId)">&nbsp;&nbsp;|&nbsp;返回上一级</a>
                    </el-breadcrumb-item>
                </el-breadcrumb>

                <el-divider></el-divider>

                <el-table
                        id="folder-table"
                        ref="multipleTable"
                        :data="datalist"
                        stripe
                        tooltip-effect="dark"
                        style="width: 100%"
                        @selection-change="handleSelectionChange"
                        @row-dblclick="handleFolder">
                    <el-table-column
                            type="selection"
                            width="55">
                    </el-table-column>
                    <el-table-column
                            width="50">
                        <template slot-scope="scope">
                            <div class="demo-image__preview" v-if="scope.row.fileType == 2">
                                <el-image
                                        style="width: 32px; height: 32px"
                                        :src="scope.row.fileImage"
                                        :preview-src-list="srcList"
                                        @click="viewImage(scope.row.fileImage)">
                                </el-image>
                            </div>
                            <div class="demo-image__preview" v-else-if="scope.row.fileType == null">
                                <el-image
                                        style="width: 32px; height: 32px"
                                        :src="initUrl">
                                </el-image>
                            </div>
                            <div class="demo-image__preview" v-else>
                                <el-image
                                        style="width: 32px; height: 32px"
                                        :src="scope.row.fileImage">
                                </el-image>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="名称"
                            width="120">
                        <template slot-scope="scope">{{scope.row.folderName?scope.row.folderName:scope.row.fileName}}</template>
                    </el-table-column>
                    <el-table-column
                            label="类型"
                            width="120">
                        <template slot-scope="scope">
                            <i v-if="scope.row.fileType == 1">文档</i>
                            <i v-else-if="scope.row.fileType == 2">图片</i>
                            <i v-else-if="scope.row.fileType == 3">视频</i>
                            <i v-else-if="scope.row.fileType == 4">音乐</i>
                            <i v-else-if="scope.row.fileType == 5">其它</i>
                            <i v-else>文件夹</i>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="大小"
                            width="120">
                        <template slot-scope="scope">{{scope.row.fileSize?scope.row.fileSize+scope.row.unit:scope.row.fileSize==0?0:'--'}}</template>
                    </el-table-column>
                    <el-table-column
                            label="下载次数"
                            width="120">
                        <template slot-scope="scope">{{scope.row.downloadCount?scope.row.downloadCount:scope.row.downloadCount == 0 ?0:'--'}}</template>
                    </el-table-column>
                    <el-table-column
                        label="添加时间"
                        width="150"
                        :formatter="formatDate">
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            width="300">
                        <template slot-scope="scope">
                            <el-row>
                                <el-tooltip class="item" effect="light" content="下载" placement="bottom-start">
                                    <el-button type="success" size="mini" icon="el-icon-download" circle @click="handleDownLoad(scope.row)"></el-button>
                                </el-tooltip>
                                <el-tooltip class="item" effect="light" content="分享" placement="bottom-start">
                                    <el-button type="info" size="mini" icon="el-icon-share" circle @click="handleShare(scope.row)"></el-button>
                                </el-tooltip>
                                <el-tooltip class="item" effect="light" content="删除" placement="bottom-start">
                                    <el-button type="danger" size="mini" icon="el-icon-delete" circle @click="handleDelete(scope.row)"></el-button>
                                </el-tooltip>
                                <el-tooltip class="item" effect="light" content="重命名" placement="bottom-start">
                                    <el-button type="primary" size="mini" icon="el-icon-edit" circle @click="handleEdit(scope.row)"></el-button>
                                </el-tooltip>
                                <el-tooltip class="item" effect="light" content="复制" placement="bottom-start">
                                    <el-button type="text" size="mini" icon="el-icon-document-copy" circle @click="handleCopy(scope.row)"></el-button>
                                </el-tooltip>
                                <el-tooltip class="item" effect="light" content="移动" placement="bottom-start">
                                    <el-button type="warning" size="mini" icon="el-icon-rank" circle @click="handleMove(scope.row)"></el-button>
                                </el-tooltip>
                            </el-row>
                        </template>
                    </el-table-column>
                </el-table>

                <span id="nowF" style="display: none" v-text="nowFolder.folderId"></span>
                <span id="preF" style="display: none" v-text="nowFolder.parentFolderId"></span>

                <!--创建文件夹dialog-->
                <div id="addFolderForm">
                    <el-dialog title="新建文件夹" :visible.sync="addFolderFormVisible" width="400px">
                        <el-form>
                            <el-form-item>
                                <el-input name="newFolderName" v-model="newFolderName" placeholder="请输入文件名" prefix-icon="el-icon-folder-add"></el-input>
                            </el-form-item>
                        </el-form>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="addFolderFormVisible = false" round>取 消</el-button>
                            <el-button type="primary" @click="addFolder()" round>确 定</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!--复制文件或文件夹dialog-->
                <div id="copyFolderOrFileDialog">
                    <el-dialog title="复制到" :visible.sync="copyDialogTableVisible">
                        <!--导航面包屑-->
                        <el-breadcrumb separator-class="el-icon-arrow-right">
                            <transition-group>
                                <el-breadcrumb-item :key="0">
                                    <a @click.prevent="enterFolder(0)">根目录</a>
                                </el-breadcrumb-item>
                                <el-breadcrumb-item v-for="(item,index) in folderLevel" :key="item.folderId">
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
                                    <i class="el-icon-folder"></i>
                                </template>
                            </el-table-column>
                            <el-table-column property="folderName" label="文件名" width="80"></el-table-column>
                        </el-table>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="copyDialogTableVisible = false" round>取 消</el-button>
                            <el-button type="primary" @click="copyFileOrFolder(nowCopyFolder.folderId)" round>复制到此处</el-button>
                        </div>
                    </el-dialog>
                </div>

                <!--移动文件或文件夹dialog-->
                <div id="moveFolderOrFileDialog">
                    <el-dialog title="移动到" :visible.sync="moveDialogTableVisible">
                        <!--导航面包屑-->
                        <el-breadcrumb separator-class="el-icon-arrow-right">
                            <transition-group>
                                <el-breadcrumb-item :key="0">
                                    <a @click.prevent="enterFolder(0)">根目录</a>
                                </el-breadcrumb-item>
                                <el-breadcrumb-item v-for="(item,index) in folderLevel" :key="item.folderId">
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
                                    <i class="el-icon-folder"></i>
                                </template>
                            </el-table-column>
                            <el-table-column property="folderName" label="文件名" width="80"></el-table-column>
                        </el-table>
                        <div slot="footer" class="dialog-footer">
                            <el-button @click="moveDialogTableVisible = false" round>取 消</el-button>
                            <el-button type="primary" @click="moveFileOrFolder(nowCopyFolder.folderId)" round>移动到此处</el-button>
                        </div>
                    </el-dialog>
                </div>
            </el-main>
        </el-container>
    </div>  
</template>
<script>
    new Vue({
        el: '#app',
        data: function() {
            return {
                visible: false,
                datalist: [],
                levelList: [],
                nowFolder: {
                    folderId: 0,
                    folderName: '',
                    parentFolderId: 0,
                    fileStoreId: 0,
                    folderPath: '',
                    createTime: null
                },
                addFolderFormVisible: false,
                copyDialogTableVisible: false,
                moveDialogTableVisible: false,
                newFolderName: '',
                nowFolderId: null,
                folderData: [],
                folderLevel: [],
                nowCopyFolder: {},
                copyAndMoveId: null,
                operateType: null,
                initUrl: "https://img.51miz.com/Element/00/38/10/50/ed73a715_E381050_ba39adab.png",
                srcList: [
                    'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
                ]
            }
        },
        created(){
            this.getAll(0);
        },
        methods: {
            getAll(folderId){
                //发送异步post请求
                var params = new URLSearchParams();
                params.append("folderId",folderId);
                axios.post("/index/file-list",params).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        this.datalist = res.data.data.datalist;
                        this.nowFolder = res.data.data.nowFolder;
                        this.nowFolderId = this.nowFolder.folderId;
                        this.levelList = res.data.data.location;
                    }else{
                        this.$message.error(res.data.msg);
                    }
                });
            },
            //返回上一级
            handleLink(folderId){
                this.getAll(folderId);
            },
            uploadSuccess(res){
                if (res.flag){
                    this.$message({message: res.data.msg, type:"success"});
                    this.getAll(this.nowFolderId);
                }else {
                    this.$message.error(res.data.msg);
                }
            },
            //控制文件上传
            handleCommand(command){
                //上传文件
                if (command == 1){
                    $("#uploadFile").click();
                }//上传文件夹
                else {
                    $("#uploadFolder").click();
                }
            },
            uploadFile(event){
                console.log("当前文件前端上传时间: "+new Date());
                var file = event.target.files[0];
                var formData = new FormData();
                var nowFolderId = parseInt($('#nowF').html());
                formData.append("file",file);
                formData.append("nowFolderId",nowFolderId);
                axios.post("/uploadFile",formData).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        console.log(res.data.msg);
                        this.getAll(nowFolderId);
                        $("#uploadFile").val('');
                    }else{
                        this.$message.error(res.data.msg);
                    }
                });
            },
            uploadFolder(event){
                var files = event.target.files;
                var formData = new FormData();
                var nowFolderId = parseInt($('#nowF').html());
                for (var i = 0; i < files.length; i++) {
                    // "file"是后台接口的参数名
                    formData.append("file", files[i]);
                }
                formData.append("nowFolderId",nowFolderId);
                axios.post("/uploadFolder",formData).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        console.log(res.data.msg);
                        this.getAll(nowFolderId);
                        $("#uploadFolder").val('');
                    }else{
                        this.$message.error(res.data.msg);
                    }
                });
            },
            addFolder(){
                var nowFolderId = parseInt($('#nowF').html());
                var b = this.checkAddFolder(this.newFolderName);
                if (b){
                    this.addFolderFormVisible = false;
                    //发送异步post请求
                    var params = new URLSearchParams();
                    params.append("parentFolderId",nowFolderId);
                    params.append("folderName",this.newFolderName);
                    axios.post("/addFolder",params).then((res)=>{
                        if (res.data.flag){
                            this.$message({message: res.data.msg, type:"success"});
                            this.getAll(nowFolderId);
                        }else{
                            this.$message.error(res.data.msg);
                        }

                    });
                }
            },
            checkAddFolder(folderName){
                var nameReg = /^[\u4E00-\u9FA5A-Za-z0-9]{1,20}$/;
                if (!nameReg.test(folderName)) {
                    this.$message.error("文件夹格式错误！【汉字、字母、数字】");
                    return false;
                }else
                    return true;
            },
            handleFolder(row){
                if (row.fileType == null){
                    this.getAll(row.folderId);
                }else
                {
                    console.log("双击预览文件");
                }
            },
            handleDblClick(row){
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
            formatDate(row){
                // 获取单元格数据
                var data = null;
                if (row.createTime != null)
                    data = row.createTime;
                if (row.uploadTime != null)
                    data = row.uploadTime;
                console.log(data);
                if(data == null) {
                    return null;
                }
                let dt = new Date(data)
                console.log(dt);
                var year = dt.getFullYear();
                var mouth = add0((dt.getMonth() + 1));
                var day = add0(dt.getDate());
                var hour = add0(dt.getHours());
                var minutes = add0(dt.getMinutes());
                return year + '-' + mouth + '-' + day + ' ' + hour + ':' + minutes;
            },
            viewImage(fileImage) {
                this.srcList = [];
                this.srcList.push(fileImage);
            },
            handleDownLoad(row){
                if (row.fileType != null){
                    console.log(row.fileId);
                    //发送异步post请求
                    var params = new URLSearchParams();
                    params.append("fileId",row.fileId);
                    axios.post("/downloadFile",params).then((res)=>{
                        if (res.data.flag){
                            let link = document.createElement("a"); //创建a标签
                            link.style.display = "none"; //使其隐藏
                            link.href = res.data.data.filePath; //赋予文件下载地址
                            link.setAttribute("download", res.data.data.fileName); //设置下载属性 以及文件名
                            document.body.appendChild(link); //a标签插至页面中
                            link.click(); //强制触发a标签事件
                            this.$message({message: res.data.msg, type:"success"});
                        }else {
                            this.$message.error(res.data.msg);
                        }
                    });
                    var nowFolderId = parseInt($('#nowF').html());
                    this.getAll(nowFolderId);
                }else {
                    this.$message({message: "下载内容包含文件夹，请使用网盘客户端下载", type:"warning"});
                }
            },
            handleEdit(row){
                var hint,inputErrorMsg, inputRule, inputValue;
                var that = this;
                if (row.fileType != null){
                    hint = "请输入文件名: ";
                    inputErrorMsg = "文件名称格式不对!";
                    inputRule = /[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$/;//文件名格式判断
                    inputValue = row.fileName+row.postfix;//文本框默认值
                }else {
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
                    inputValue: inputValue,
                    callback: function (action, instance) {
                        if (action == 'confirm'){
                            var nowFolderId = parseInt($('#nowF').html());
                            if (row.fileType != null){
                                that.editFileName(row.fileId, instance.inputValue, nowFolderId);
                            }else {
                                that.editFolderName(row.folderId, instance.inputValue, nowFolderId);
                            }
                        }
                    }
                }).then(({ value }) => {
                    console.log(value);
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消输入'
                    });
                });
            },
            editFileName(fileId, name, nowFolderId) {
                //发送异步post请求
                var params = new URLSearchParams();
                params.append("fileId", fileId);
                params.append("newFileName", name);
                params.append("parentFolderId", nowFolderId)
                axios.post("/renameFile",params).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        this.getAll(nowFolderId);
                    }else {
                        this.$message.error(res.data.msg);
                    }

                });
            },
            editFolderName(folderId, name, nowFolderId) {
                //发送异步post请求
                var params = new URLSearchParams();
                params.append("folderId", folderId);
                params.append("newFolderName", name);
                params.append("parentFolderId", nowFolderId)
                axios.post("/renameFolder",params).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        this.getAll(nowFolderId);
                    }else {
                        this.$message.error(res.data.msg);
                    }
                });
            },
            //删除文件
            handleDelete(row){
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '删除',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then((action) => {
                    if (action == 'confirm'){
                        var nowFolderId = parseInt($('#nowF').html());
                        if (row.fileType != null){
                            this.deleteFile(row.fileId, nowFolderId);
                        }else{
                            this.deleteFolder(row.folderId, nowFolderId);
                        }
                    }
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            },
            deleteFile(fileId, nowFolderId){
                var params = new URLSearchParams();
                params.append("fileId",fileId);
                axios.post("/deleteFile",params).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        this.getAll(nowFolderId);
                    }else {
                        this.$message.error(res.data.msg);
                    }
                });
            },
            deleteFolder(folderId, nowFolderId){
                var params = new URLSearchParams();
                params.append("folderId",folderId);
                axios.post("/deleteFolder",params).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        this.getAll(nowFolderId);
                    }else {
                        this.$message.error(res.data.msg);
                    }
                });
            },
            //复制文件或文件夹
            handleCopy(row){
                this.copyDialogTableVisible = true;
                if (row.fileType == null){
                    //移动文件夹
                    this.copyAndMoveId = row.folderId;
                    this.operateType = 1;
                }else {
                    //移动文件
                    this.copyAndMoveId = row.fileId;
                    this.operateType = 2;
                }
                this.enterFolder(0);
            },
            enterFolder(nowFolderId){
                //发送异步post请求
                var params = new URLSearchParams();
                params.append("nowFolderId",nowFolderId);
                axios.post("/getFolder",params).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        this.folderData = res.data.data.folderData;
                        this.nowCopyFolder = res.data.data.nowFolder;
                        this.folderLevel = res.data.data.location;
                    }else{
                        this.$message.error(res.data.msg);
                    }
                });
            },
            copyFileOrFolder(parentFolderId){
                //发送异步post请求
                this.copyDialogTableVisible = false;
                var params = new URLSearchParams();
                params.append("operateType",this.operateType);
                params.append("operateId", this.copyAndMoveId);
                params.append("parentFolderId", parentFolderId);
                axios.post("/copyFileOrFolder",params).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        this.getAll(parentFolderId);
                    }else{
                        this.$message.error(res.data.msg);
                    }
                });
            },
            //移动文件或文件夹
            handleMove(row){
                this.moveDialogTableVisible = true;
                if (row.fileType == null){
                    //移动文件夹
                    this.copyAndMoveId = row.folderId;
                    this.operateType = 1;
                }else {
                    //移动文件
                    this.copyAndMoveId = row.fileId;
                    this.operateType = 2;
                }
                this.enterFolder(0);
            },
            moveFileOrFolder(parentFolderId){
                //发送异步post请求
                this.moveDialogTableVisible = false;
                var params = new URLSearchParams();
                params.append("operateType",this.operateType);
                params.append("operateId", this.copyAndMoveId);
                params.append("parentFolderId", parentFolderId);
                axios.post("/moveFileOrFolder",params).then((res)=>{
                    if (res.data.flag){
                        this.$message({message: res.data.msg, type:"success"});
                        this.getAll(parentFolderId);
                    }else{
                        this.$message.error(res.data.msg);
                    }
                });
            }
        }
    });
</script>
<script type="text/javascript">
    function add0(date){
        return date<10? '0'+date:date;
    };
</script>