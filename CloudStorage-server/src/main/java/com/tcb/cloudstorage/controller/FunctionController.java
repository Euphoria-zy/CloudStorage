package com.tcb.cloudstorage.controller;

import com.tcb.cloudstorage.domain.FileStore;
import com.tcb.cloudstorage.domain.Folder;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.domain.UserFile;
import com.tcb.cloudstorage.service.FileService;
import com.tcb.cloudstorage.service.FileStoreService;
import com.tcb.cloudstorage.service.FolderService;
import com.tcb.cloudstorage.service.impl.FolderServiceImpl;
import com.tcb.cloudstorage.utils.COSUtils;
import com.tcb.cloudstorage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
* 系统功能控制器
* */
@RestController
public class FunctionController extends BaseController
{
    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderService folderService;

    public static boolean DELETE_FOLDER_FLAG = true;
    public static String DELETE_FOLDER_MSG = "";

    /**
     * @Description 上传文件,并将文件元数据信息更新到数据库
     * @param file
     * @param nowFolderId
     * @return
     */
    @RequestMapping("/uploadFile")
    public R uploadFile(@RequestParam MultipartFile file, @RequestParam int nowFolderId)
    {
        System.out.println(nowFolderId);
        FileStore store = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        String name = file.getOriginalFilename();
        //判断是否有同名文件
        List<UserFile> userFiles = null;
        //以用户名开头
        String folderPath = null;
        Folder nowFolder;
        if (nowFolderId == 0){
            //当前目录为根目录
            userFiles = fileService.getRootFileByFileStoreId(loginUser.getFileStoreId());
            folderPath = loginUser.getUsername() +"/";
        }else {
            //当前目录为其他目录
            userFiles = fileService.getUserFileByParentFolderId(nowFolderId);
            nowFolder = folderService.getFolderById(nowFolderId);
            folderPath = nowFolder.getFolderPath()+nowFolder.getFolderName()+"/";
        }
        for (int i = 0; i < userFiles.size(); i++) {
            if ((userFiles.get(i).getFileName()+userFiles.get(i).getPostfix()).equals(name)){
                return new R(false, "当前文件已存在!上传失败");
            }
        }
        //上传时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateStr = null;
        try
        {
            dateStr = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        if (!fileService.checkFileName(name)){
            return new R(false, "上传失败!文件名不符合规范");
        }
        Integer sizeInt = Math.toIntExact(file.getSize() / 1024);
        //是否仓库放不下该文件
        if(store.getCurrentSize()+sizeInt > store.getMaxSize()){
            return new R(false,"仓库容量不够!");
        }
        //处理文件大小(以KB为单位)
        String fileSize = String.valueOf(file.getSize()/1024.0);
        int indexDot = fileSize.lastIndexOf(".");
        fileSize = fileSize.substring(0,indexDot);
        //处理文件名
        int index = name.lastIndexOf(".");
        String tempName = name;
        //文件后缀，判断文件类型
        String postfix = "";
        String key = folderPath;
        int type = 5;
        if (index!=-1){
            tempName = name.substring(index);
            //处理文件名
            name = name.substring(0,index);
            //获得文件类型
            type = fileService.getFileTypeByPostfix(tempName.toLowerCase());
            postfix = tempName.toLowerCase();
            key += (name + postfix);
        }
        //文件上传到COS服务器
        boolean b = COSUtils.uploadFile(key, file);
        if (b){
            //关闭连接
            COSUtils.shutdownTransferManager();
            //上传成功,向数据库文件表写入数据
            fileService.addUserFile(
                    UserFile.builder()
                            .fileName(name).fileStoreId(loginUser.getFileStoreId()).filePath(folderPath)
                            .downloadCount(0).uploadTime(dateStr).parentFolderId(nowFolderId).
                            fileSize(Integer.valueOf(fileSize)).fileType(type).postfix(postfix).build());
            //更新仓库表的当前大小
            fileStoreService.addFileStoreSize(store.getFileStoreId(),store.getCurrentSize()+Integer.valueOf(fileSize));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("文件: "+file.getOriginalFilename()+"上传失败!");
            return new R(false, "文件: "+file.getOriginalFilename()+"上传失败!");
        }
        return new R(true, "文件: "+file.getOriginalFilename()+"上传成功!", nowFolderId);
    }

    /**
     * @Description 上传文件夹，解析上传的文件路径，创建文件夹，并把文件上传到COS
     * @param file
     * @param nowFolderId
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping("/uploadFolder")
    public R uploadFolders(@RequestParam("file") MultipartFile[] file, @RequestParam int nowFolderId) throws IOException, ParseException
    {
        FileStore store = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        List<Folder> folders = new ArrayList<>();
        String folderPath = null;
        if (nowFolderId == 0){
            //向用户根目录添加文件夹
            folders = folderService.getRootFolderByFileStoreId(loginUser.getFileStoreId());
            folderPath = loginUser.getUsername() +"/";
        }else{
            //向用户的其他目录添加文件夹
            folders = folderService.getFolderByParentFolderId(nowFolderId);
            Folder parentFolder = folderService.getFolderById(nowFolderId);
            folderPath = parentFolder.getFolderPath() +parentFolder.getFolderName() +"/";
        }
        //判断当前选中文件夹是否已经存在
        String firstFolderName = null;
        if (file.length>0){
            MultipartFile firstFile = file[0];
            String path = firstFile.getOriginalFilename();
            int nameIndex = path.lastIndexOf("/");
            String filePath = path.substring(0,nameIndex);
            String[] fileFolder = filePath.split("/");
            firstFolderName = fileFolder[0];
            if (folderService.getFolderByPIdAndName(nowFolderId, firstFolderName) != null){
                return new R(false, "上传文件夹失败! 文件夹: "+firstFolderName+"已存在!");
            }
        }
        for (MultipartFile f: file)
        {
            //判断容量大小,以KB为单位
            Integer sizeInt = Math.toIntExact(f.getSize() / 1024);
            //是否仓库放不下该文件
            if(store.getCurrentSize()+sizeInt > store.getMaxSize()){
                //关闭连接
                COSUtils.shutdownTransferManager();
                return new R(false,"仓库容量不够!");
            }

            String path = f.getOriginalFilename();
            int nameIndex = path.lastIndexOf("/");
            String filePath = path.substring(0,nameIndex);
            String[] fileFolder = filePath.split("/");

            //处理文件名
            String fileName = path.substring(nameIndex+1);
            int postfixIndex = fileName.lastIndexOf(".");
            String postfix = fileName.substring(postfixIndex).toLowerCase();
            String name = fileName.substring(0,postfixIndex);
            int type = fileService.getFileTypeByPostfix(postfix);
            //处理文件大小(以KB为单位)
            String fileSize = String.valueOf(f.getSize()/1024.0);
            int indexDot = fileSize.lastIndexOf(".");
            fileSize = fileSize.substring(0,indexDot);
            String key = folderPath + filePath+"/"+name +postfix;

            //文件上传到COS服务器
            boolean b = COSUtils.uploadFile(key, f);
            if (b){
                //上传成功,向数据库写入文件夹、文件等数据
                int parentFolderId = nowFolderId;
                String currentFolderPath = folderPath;
                //上传时间
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date dateStr = dateFormat.parse(dateFormat.format(new Date()));
                for (int i = 0; i < fileFolder.length; i++)
                {
                    Folder currentFolder = folderService.getFolderByPIdAndName(parentFolderId, fileFolder[i]);
                    if (currentFolder == null){
                        Folder folder = Folder.builder().folderName(fileFolder[i])
                                .folderPath(currentFolderPath)
                                .parentFolderId(parentFolderId)
                                .fileStoreId(store.getFileStoreId())
                                .createTime(dateStr)
                                .build();
                        int b1 = folderService.addFolderReturnFolderId(folder);
                        if (b1>0){
                            parentFolderId = folder.getFolderId();
                            currentFolderPath = currentFolderPath + folder.getFolderName() +"/";
                            dateStr = dateFormat.parse(dateFormat.format(new Date()));
                        }else {
                            //关闭连接
                            COSUtils.shutdownTransferManager();
                            return new R(false, "文件夹添加失败!");
                        }
                    }else{
                        parentFolderId = currentFolder.getFolderId();
                        currentFolderPath = currentFolder.getFolderPath() + currentFolder.getFolderName() +"/";
                        dateStr = dateFormat.parse(dateFormat.format(new Date()));
                    }
                }
                fileService.addUserFile(
                        UserFile.builder()
                                .fileName(name).fileStoreId(loginUser.getFileStoreId()).filePath(currentFolderPath)
                                .downloadCount(0).uploadTime(dateStr).parentFolderId(parentFolderId).
                                fileSize(Integer.valueOf(fileSize)).fileType(type).postfix(postfix).build());
                //更新仓库表的当前大小
                fileStoreService.addFileStoreSize(store.getFileStoreId(), store.getCurrentSize()+Integer.valueOf(fileSize));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("添加文件信息:"+f.getOriginalFilename()+"失败!");
                    e.printStackTrace();
                }
            }else{
                System.out.println("文件: "+f.getOriginalFilename()+"上传失败!");
            }
        }
        //关闭连接
        COSUtils.shutdownTransferManager();
        return new R(true, "上传文件夹:"+firstFolderName+"成功!");
    }

    /**
     * @Description 文件下载，生成预签名url，返回给前端
     * @param fileId
     * @return
     */
    @RequestMapping("/downloadFile")
    public R downloadFile(@RequestParam int fileId)
    {
        UserFile userFile = fileService.getFileByFileId(fileId);
        String name = userFile.getFileName();
        String postfix = userFile.getPostfix();
        String key = userFile.getFilePath() + name + postfix;
        URL url = null;
        //获取文件下载的url
        url = COSUtils.downLoadFileWithURL(key, name + postfix);
        //关闭连接
        COSUtils.shutdownTransferManager();
        //更新文件被下载的次数
        userFile.setDownloadCount(userFile.getDownloadCount()+1);
        boolean b = fileService.updateUserFileById(userFile);
        if (b){
            Map<String, Object> data = new HashMap<>();
            data.put("filePath", url);
            data.put("fileName", name+postfix);
            return new R(true, "文件下载成功!", data);
        }else {
            return new R(false, "数据库文件信息更新失败");
        }
    }

    /**
     * @Description 创建文件夹
     * @param folderName
     * @param parentFolderId
     * @return
     */
    @RequestMapping("/addFolder")
    public R addFolder(String folderName, int parentFolderId)
    {
        //创建时间
        Date dateStr = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateStr = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String folderPath = null;
        if (parentFolderId == 0){
            //向用户根目录添加文件夹
            folderPath = loginUser.getUsername() +"/";
        }else{
            //向用户的其他目录添加文件夹
            Folder parentFolder = folderService.getFolderById(parentFolderId);
            folderPath = parentFolder.getFolderPath() +parentFolder.getFolderName() +"/";
        }
        if (folderService.getFolderByPIdAndName(parentFolderId, folderName) != null){
            return new R(false, "添加文件夹失败!文件夹已存在!");
        }
        Folder newFolder = Folder.builder()
                .folderName(folderName)
                .parentFolderId(parentFolderId)
                .fileStoreId(loginUser.getFileStoreId())
                .folderPath(folderPath)
                .createTime(dateStr).build();
        //向数据库写入数据
        int b = folderService.addFolder(newFolder);
        if (b > 0){
            return new R(true, "文件夹："+folderName+"上传成功!");
        }else
            return new R(false,"文件夹："+folderName+"上传失败!");
    }

    /**
     * @Description 重命名文件夹,先复制源文件到新路径，再删除源文件
     * @param fileId
     * @param newFileName
     * @param parentFolderId
     * @return
     */
    @RequestMapping("/renameFile")
    public R RenameFile(@RequestParam int fileId, @RequestParam String newFileName, @RequestParam int parentFolderId)
    {
        int index = newFileName.lastIndexOf(".");
        String name = newFileName.substring(0,index);
        String postfix = newFileName.substring(index);
        //判断文件名是否冲突
        boolean b1 = fileService.isFileRepeat(parentFolderId, name, postfix);
        if (b1){
            return new R(false, "重命名失败，当前文件: "+newFileName+"已存在!");
        }
        UserFile userFile = fileService.getFileByFileId(fileId);
        if ((userFile.getFileName()+userFile.getPostfix()).equals(newFileName))
            return new R(true, "新旧文件名一致，无需重命名!");
        String sourceKey = userFile.getFilePath() + userFile.getFileName() +userFile.getPostfix();
        String destinationKey = userFile.getFilePath()+newFileName;
        boolean b2 = COSUtils.copyFile(sourceKey, destinationKey);
        if (b2) {
            boolean b3 = COSUtils.deleteFile(sourceKey);
            if (b3) {
                userFile.setFileName(name);
                userFile.setPostfix(postfix);
                boolean b4 = fileService.updateUserFileById(userFile);
                if (b4)
                    return new R(true, "文件重命名成功");
                else
                    return new R(false, "重命名信息写入数据库出错!");
            }else {
                return new R(false, "删除源文件对象出错!");
            }
        }else {
            return new R(false, "复制源文件出错!");
        }
    }

    /**
     * @Description 重命名文件夹
     * @param folderId
     * @param newFolderName
     * @return
     */
    @RequestMapping("/renameFolder")
    public R RenameFolder(@RequestParam int folderId, @RequestParam String newFolderName, @RequestParam int parentFolderId)
    {
        Folder folder = folderService.getFolderById(folderId);
        Folder folderByPIdAndName = folderService.getFolderByPIdAndName(parentFolderId, newFolderName);
        if (folderByPIdAndName != null){
            return new R(false, "重命名失败，当前文件夹: "+newFolderName+"已存在!");
        }else {
            folder.setFolderName(newFolderName);
            boolean b1 = folderService.updateFolder(folder);
            if (b1){
                return new R(true, "文件夹重命名成功!");
            }else
                return new R(false, "文件夹重命名失败!");
        }
    }

    /**
     * @Description 根据文件id删除文件
     * @param fileId
     * @return
     */
    @RequestMapping("/deleteFile")
    public R deleteFile(@RequestParam int fileId)
    {
        FileStore store = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        UserFile userFile = fileService.getFileByFileId(fileId);
        String name = userFile.getFileName();
        String postfix = userFile.getPostfix();
        String key = userFile.getFilePath() + name + postfix;
        boolean b = COSUtils.deleteFile(key);
        if (b){
            //关闭连接
            COSUtils.shutdownTransferManager();
            boolean b1 = fileService.deleteFileById(fileId);
            if (b1){
                fileStoreService.subFileStoreSize(store.getFileStoreId(), store.getCurrentSize()-userFile.getFileSize());
                return new R(true, "文件: "+ name+postfix +"删除成功!");
            }else
                return new R(false, "文件: "+ name+postfix +"删除失败!");
        }else{
            return new R(false, "文件: "+ name+postfix +"删除失败!");
        }
    }

    /**
     * @Description 删除文件夹内，则需要用代码遍历实现删除文件夹内的对象
     * @param folderId
     * @return
     */
    @RequestMapping("/deleteFolder")
    public R deleteFolder(@RequestParam int folderId)
    {
        Folder folder = folderService.getFolderById(folderId);
        deepDeleteFolder(folder);
        //关闭连接
        COSUtils.shutdownTransferManager();
        boolean deleteResult = DELETE_FOLDER_FLAG;
        String deleteMsg = DELETE_FOLDER_MSG;
        if (deleteResult)
            deleteMsg = "文件夹："+ folder.getFolderName() +"删除成功!";
        return new R(deleteResult,deleteMsg);
    }
    /**
     * @Description 递归删除文件夹下面的所有数据（文件夹和文件）
     * @param folder
     * @return
     */
    public void deepDeleteFolder(Folder folder)
    {
        FileStore store = fileStoreService.getFileStoreById(loginUser.getFileStoreId());
        //删除目录下的所有文件夹
        List<Folder> folders = folderService.getFolderByParentFolderId(folder.getFolderId());
        //删除文件夹下的所有文件
        List<UserFile> files = fileService.getUserFileByParentFolderId(folder.getFolderId());
        boolean b1, b2;
        b1 = b2 = true;
        for (UserFile file: files){
            String filePath = file.getFilePath();
            String name = file.getFileName();
            String postfix = file.getPostfix();
            b1 = COSUtils.deleteFile(filePath + name + postfix);
            if (b1){
                b2 = fileService.deleteFileById(file.getFileId());
                if (b2){
                    fileStoreService.subFileStoreSize(store.getFileStoreId(), store.getCurrentSize()- file.getFileSize());
                }else {
                    DELETE_FOLDER_FLAG = false;
                    DELETE_FOLDER_MSG = "文件: "+name+postfix+"删除失败";
                    System.out.println("数据库文件信息删除失败!");
                }
            }else {
                DELETE_FOLDER_FLAG = false;
                DELETE_FOLDER_MSG = "文件: "+name+postfix+"删除失败";
                System.out.println("COS文件信息删除失败");
            }
        }
        for (Folder childFolder: folders){
            deepDeleteFolder(childFolder);
        }
        //在成功删除当前文件夹中的文件的情况下，再删除文件夹
        if (b1)
            folderService.deleteFolder(folder.getFolderId());
    }

}
