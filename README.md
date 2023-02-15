# 加密极速云盘

### 项目简介

###### 加密极速云盘项目，以公有云存储技术为核心，采用公钥私钥的加密机制对数据进行加密存储，来保障用户的数据隐私安全。应用场景主要如下：

- 提供个人资料存储平台，保护个人隐私并支持个性化服务；
- 为大小型事业单位提供安全共享平台，存储公司内部机密文件、客户资料并支持安全搜索和访问；
- 可应用于军事领域，与强制访问控制方式 相结合，针对不同密级的文档。制定不同的访问策略，不同级别的人员搜索和访问授权内的文档；
- 实现政府部门机密档案安全管理和提供安全的在线办公平台，保证机密信息的安全传输、存储和共享，提高政府办公效率。

### 项目主要功能

用户的登录、注册、忘记密码（采用邮箱验证）；

文件的管理、上传、下载（支持限速下载）、删除、重命名、复制、移动、预览（TODO）；

文件夹得管理、创建、上传、删除、重命名、复制、移动；

用户操作日志管理；

文件的分享，支持链接或者二维码分享（TODO）；

文件数据的加密（TODO）

云盘客户端app（TODO）

### 所用技术
###### 前端

###### 后端
- SpringBoot+MyBatis-Plus
- Redis缓存
- COS 对象存储
- Java邮件服务

###### 部署
- 腾讯云轻量服务器
- Docker容器
- MySQL数据库

### 项目运行环境搭建

- 项目下载：在本地文件打开Git Bash，使用git clone https://github.com/Euphoria-zy/CloudStorage.git(仓库地址)，拉取项目到本地。使用IntelliJ IDEA打开项目，点击CloudStorage-server下的pom.xml，将项目添加为Maven工程，导入项目运行所需依赖。
- 数据库配置：在本地数据库中运行项目路径：CloudStorage-server/src/main/resources/templates/sql/下的.sql文件：cloud_db.sql，完成数据库表的创建。修改application.yml中连接数据库的用户名和密码。
- 项目运行：开启本地Redis服务端，运行CloudstorageApplication.java即将项目部署到Tomcat容器中。登录地址：http://localhost:8080/login.html。