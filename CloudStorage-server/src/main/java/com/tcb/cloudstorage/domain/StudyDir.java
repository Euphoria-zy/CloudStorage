package com.tcb.cloudstorage.domain;

import lombok.Data;

import java.io.File;

@Data
public class StudyDir
{
    private long dirSize = 0;

    private int fileCount = 0;

    private int dirCount = 0;

    public static void main(String[] args) {

        String path = "G:\\static\\layui";
        File file = new File(path);
        StudyDir studyDir = new StudyDir();
        //打印文档结构树
        studyDir.printDir(new File(path), 0);
        //统计路径下文件的大小、文件夹的个数、文件的个数
        studyDir.dirTest(file);
        System.out.println(studyDir.getDirSize());
        System.out.println(studyDir.getDirCount());
        System.out.println(studyDir.getFileCount());

    }

    /**
     * @param file
     * @return void
     * @author wwkjk
     * @date 2021-10-24 21:03:15
     * @Description 使用递归计算出文件夹的大小、文件的个数、文件夹的个数
     **/
    private void dirTest(File file) {

        //判断是否为文件
        if (file.isFile()) {
            this.dirSize += file.length();
            this.fileCount++;
        }
        //判断是否为文件夹
        if (file.isDirectory()) {
            this.dirCount++;
            File[] list = file.listFiles();
            for (File f : list) {
                dirTest(f);
            }
        }

    }

    /**
     * @param file,level
     * @return void
     * @author wwkjk
     * @date 2021-10-24 21:04:16
     * @Description 打印文件目录结构树
     **/
    private void printDir(File file, int level) {

        //打印层次划分
        for (int i = 0; i < level; i++) {
            System.out.print("-->");
        }

        //打印文件名
        System.out.println(file.getName());

        if (file.isDirectory()) {
            File[] list = file.listFiles();
            for (File f : list) {
                printDir(f, level + 1);
            }
        }

    }
}
