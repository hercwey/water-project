package com.learnbind.ai.common.ziputil;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;

import net.lingala.zip4j.core.ZipFile;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.ziputil
 *
 * @Title: UnPackeUtil.java
 * @Description: 解压工具类
 *
 * @author Administrator
 * @date 2019年12月21日 上午9:49:25
 * @version V1.0 
 *
 */
public class UnPackeUtil {
 
	/**
	 * @Fields log：日志
	 */
	private static Log log = LogFactory.getLog(UnPackeUtil.class);
	
    /**
     * @Title: unPackZip
     * @Description: zip文件解压
     * @param destPath	解压文件路径
     * @param zipFile	压缩文件
     * @param password 	解压密码(如果有)
     */
    public static void unPackZip(String destPath, File zipFile, String password) {
        try {
            ZipFile zip = new ZipFile(zipFile);
            /*zip4j默认用GBK编码去解压,这里设置编码为GBK的*/
            zip.setFileNameCharset("GBK");
            log.info("begin unpack zip file....");
            zip.extractAll(destPath);
            // 如果解压需要密码
            if (zip.isEncrypted()) {
                zip.setPassword(password);
            }
        } catch (Exception e) {
            log.error("unPack zip file to " + destPath + " fail ....", e);
        }
    }
 
    /**
     * @Title: unPackRar
     * @Description: rar文件解压(不支持有密码的压缩包)
     * @param destPath	解压保存路径
     * @param rarFile 	rar压缩包
     */
    public static void unPackRar(String destPath, File rarFile) {
        try (Archive archive = new Archive(rarFile)) {
            if (null != archive) {
                FileHeader fileHeader = archive.nextFileHeader();
                File file = null;
                while (null != fileHeader) {
                    // 防止文件名中文乱码问题的处理
                    String fileName = fileHeader.getFileNameW().isEmpty() ? fileHeader.getFileNameString() : fileHeader.getFileNameW();
                    if (fileHeader.isDirectory()) {
                        //是文件夹
                        file = new File(destPath + File.separator + fileName);
                        file.mkdirs();
                    } else {
                        //不是文件夹
                        file = new File(destPath + File.separator + fileName.trim());
                        if (!file.exists()) {
                            if (!file.getParentFile().exists()) {
                                // 相对路径可能多级，可能需要创建父目录.
                                file.getParentFile().mkdirs();
                            }
                            file.createNewFile();
                        }
                        FileOutputStream os = new FileOutputStream(file);
                        archive.extractFile(fileHeader, os);
                        os.close();
                    }
                    fileHeader = archive.nextFileHeader();
                }
            }
        } catch (Exception e) {
            log.error("unpack rar file fail....", e);
        }
    }
}
