package com.learnbind.ai.common.ziputil;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.ziputil
 *
 * @Title: FileTypeEnum.java
 * @Description: 压缩文件类型枚举类
 *
 * @author Administrator
 * @date 2019年12月21日 上午9:31:56
 * @version V1.0 
 *
 */
public enum FileTypeEnum {
	
    /**
     * @Fields FILE_TYPE_ZIP：zip
     */
    FILE_TYPE_ZIP("application/zip", ".zip"),
    /**
     * @Fields FILE_TYPE_RAR：rar
     */
    FILE_TYPE_RAR("application/octet-stream", ".rar");
	
    private FileTypeEnum(String type, String fileStufix) {
		this.type = type;
		this.fileStufix = fileStufix;
	}

	/**
	 * @Fields type：类型
	 */
	public String type;
    /**
     * @Fields fileStufix：扩展名
     */
    public String fileStufix;
 
    public static String getFileStufix(String type) {
        for (FileTypeEnum orderTypeEnum : FileTypeEnum.values()) {
            if (orderTypeEnum.type.equals(type)) {
                return orderTypeEnum.fileStufix;
            }
        }
        return null;
    }
}
