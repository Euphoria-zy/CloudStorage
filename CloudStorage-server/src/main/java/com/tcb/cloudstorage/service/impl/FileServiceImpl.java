package com.tcb.cloudstorage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.model.ciModel.mediaInfo.MediaInfoResponse;
import com.tcb.cloudstorage.domain.FileStoreInfo;
import com.tcb.cloudstorage.domain.User;
import com.tcb.cloudstorage.domain.UserFile;
import com.tcb.cloudstorage.mapper.FileMapper;
import com.tcb.cloudstorage.service.FileService;
import com.tcb.cloudstorage.utils.COSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, UserFile> implements FileService
{
    @Autowired
    private FileMapper fileMapper;

    /**
     * @Description 添加文件
     * @param userFile
     * @return boolean
     */
    @Override
    public boolean addUserFile(UserFile userFile)
    {
        return fileMapper.insertUserFile(userFile);
    }

    @Override
    public boolean updateUserFileById(UserFile userFile)
    {
        return fileMapper.updateById(userFile)>0;
    }

    /**
     * @Description 根据文件夹id获取文件夹下的文件
     * @param folderId
     * @return
     */
    @Override
    public List<UserFile> getUserFileByParentFolderId(int folderId)
    {
        return fileMapper.getUserFileByFolderId(folderId);
    }

    /**
     * @Description 根据文件仓库id获取根目录下的文件
     * @param fileStoreId
     * @return
     */
    @Override
    public List<UserFile> getRootFileByFileStoreId(int fileStoreId)
    {
        return fileMapper.getRootFileByFileStoreId(fileStoreId);
    }


    /**
     * @Description 根据文件id获取文件
     * @param fileId
     * @return
     */
    @Override
    public UserFile getFileByFileId(int fileId)
    {
        return fileMapper.selectById(fileId);
    }

    @Override
    public boolean deleteFileById(int fileId)
    {
        return fileMapper.deleteById(fileId)>0;
    }

    /**
     * @Description 根据文件的后缀名获得对应的类型
     * @Author xw
     * @Date 23:20 2020/2/10
     * @Param [type]
     * @return int 1:文本类型   2:图像类型  3:视频类型  4:音乐类型  5:其他类型
     **/
    @Override
    public int getFileTypeByPostfix(String type){
        //1-doc
        if (".chm".equals(type)||".txt".equals(type)||".xmind".equals(type)||".xlsx".equals(type)||".md".equals(type)
                ||".doc".equals(type)||".docx".equals(type)||".pptx".equals(type)
                ||".wps".equals(type)||".word".equals(type)||".html".equals(type)||".htm".equals(type)||".pdf".equals(type)
                ||".xml".equals(type)||".xls".equals(type)){
            return  1;
        }
        //2-image
        else if (".bmp".equals(type)||".gif".equals(type)||".jpg".equals(type)||".ico".equals(type)||".vsd".equals(type)
                ||".pic".equals(type)||".png".equals(type)||".jepg".equals(type)||".jpeg".equals(type)||".webp".equals(type)
                ||".svg".equals(type)){
            return 2;
        }
        //3-video
        else if (".avi".equals(type)||".mov".equals(type)||".qt".equals(type)
                ||".asf".equals(type)||".rm".equals(type)||".navi".equals(type)||".wav".equals(type)
                ||".mp4".equals(type)||".mkv".equals(type)||".webm".equals(type)){
            return 3;
        }
        //4-music
        else if (".mp3".equals(type)||".wma".equals(type)){
            return 4;
        }
        //5-other
        else {
            return 5;
        }
    }
    /**
     * @Description 正则验证文件名是否合法 [汉字,字符,数字,下划线,英文句号,横线]
     * @Author xw
     * @Date 23:22 2020/2/10
     * @Param [target]
     * @return boolean
     **/
    public boolean checkFileName(String target) {
        final String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_.]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(target);
        return !matcher.find();
    }

    @Override
    public boolean isFileRepeat(int parentFolderId, String fileName, String postfix)
    {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new QueryWrapper<UserFile>().lambda();
        lambdaQueryWrapper.eq(UserFile::getParentFolderId, parentFolderId);
        lambdaQueryWrapper.eq(UserFile::getFileName, fileName);
        lambdaQueryWrapper.eq(UserFile::getPostfix, postfix);
        return fileMapper.selectCount(lambdaQueryWrapper)>0;
    }

    /**
     * @Description 获取文件图标url，图片文件返回图片地址，视频文件返回截帧图片地址
     * @param userFile
     * @return
     */
    @Override
    public String getFileImage(UserFile userFile)
    {
        int fileType = userFile.getFileType();
        String fileImage = null;
        switch (fileType){
            case 1:
                //文档类型
                break;
            case 2:
                //图片-获取图片的访问url
                URL viewObjectUrl = COSUtils.getViewObjectUrl(userFile.getFilePath() + userFile.getFileName() + userFile.getPostfix());
                fileImage = viewObjectUrl.toString();
                break;
            case 3:
                //视频类型，截帧保存图片
                fileImage = getVideoImage(userFile);
            case 4:
                //音乐类型

            case 5:
                //未知类型

        }

        return fileImage;
    }

    @Override
    public String getDocumentFileImage(String postfix)
    {
        return null;
    }

    /**
     * @Description 获取视频截帧图片url
     * @param userFile
     * @return
     */
    @Override
    public String getVideoImage(UserFile userFile)
    {
        String folderPath = userFile.getFilePath();
        String fileName = userFile.getFileName();
        String postfix = userFile.getPostfix();
        String key = folderPath + fileName + postfix;
        String saveImageKey = folderPath + fileName + "_image" + ".jpg";
        MediaInfoResponse videoInfo = COSUtils.getVideoInfo(key);
        double times = Double.parseDouble(videoInfo.getMediaInfo().getFormat().getDuration());
        int time  = (int)Math.floor(times);
        URL videoImageUrl = COSUtils.getVideoImage(key, time / 2, saveImageKey);
        return videoImageUrl.toString();
    }

    @Override
    public Map<String, Object> getFileSize(double fileSize)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        Map<String, Object> map = new HashMap<>();
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize);
            map.put("unit","B");
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024);
            map.put("unit","KB");
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576);
            map.put("unit","MB");
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824);
            map.put("unit","GB");
        }
        fileSize = Double.parseDouble(fileSizeString);
        map.put("fileSize", fileSize);
        return map;
    }

    @Override
    public List<UserFile> getFileByType(int fileStoreId, int fileType)
    {
        LambdaQueryWrapper<UserFile> lambdaQueryWrapper = new QueryWrapper<UserFile>().lambda();
        lambdaQueryWrapper.eq(UserFile::getFileStoreId, fileStoreId);
        lambdaQueryWrapper.eq(UserFile::getFileType, fileType);
        return fileMapper.selectList(lambdaQueryWrapper);
    }

}
