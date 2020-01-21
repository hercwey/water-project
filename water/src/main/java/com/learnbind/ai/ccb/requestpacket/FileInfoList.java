package com.learnbind.ai.ccb.requestpacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 代收代付 FILELISTPACK 对象描述
 * 
 * @author lenovo
 *
 */
public class FileInfoList {

	private List<FileInfo> fileList;

	public FileInfoList() {
		this.fileList = new ArrayList<FileInfo>();
	}

	/**
	 * 增加文件
	 * 
	 * @param fileInfo
	 */
	public void addFileInfo(FileInfo fileInfo) {
		fileList.add(fileInfo);
	}
	
	/**
	 * 返回文件个数
	 * @return
	 */
	public int getFileNum() {
		return fileList.size();
	}

	public List<FileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileInfo> fileList) {
		this.fileList = fileList;
	}

}
