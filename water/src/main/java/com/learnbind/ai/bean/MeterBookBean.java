package com.learnbind.ai.bean;

/**
 * 表册实体
 * added by jch 2019/06/11
 */
public class MeterBookBean {
	
    /**
     * @Fields name：表册名称 格式：小区名称-楼号-单元
     */
    private String name;//表册名称 小区名称-10-1
    /**
     * @Fields equipType：表册所对应的设备类型名称  取值包括JC-积成 SC:三川  EG-艺高
     */
    private String equipType;//表册所对应的设备类型名称  取值包括JC-积成 SC:三川  EG-艺高
    /**
     * @Fields meterBookId：表册ID
     */
    private Long meterBookId;//表册ID
    
    /**
     * @Fields typeCode：分类编码
     */
    private String  typeCode;//分类编码
    /**
     * @Fields typeName：分类名称
     */
    private String typeName;//分类名称
    
    /**
     * @Fields readMode：抄表方式
     */
    private String readMode;	//抄表方式
    
    /**
     * @Fields bookUserType：表册用户类型
     */
    private Integer bookUserType;

    
    /**
     * @Title: getName
     * @Description: 获取表册名称 格式：小区名称-楼号-单元
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * @Title: setName
     * @Description: 设置表册名称 格式：小区名称-楼号-单元
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @Title: getEquipType
     * @Description: 获取表册所对应的设备类型名称  取值包括JC-积成 SC:三川  EG-艺高
     * @return 
     */
    public String getEquipType() {
        return equipType;
    }

    /**
     * @Title: setEquipType
     * @Description: 设置表册所对应的设备类型名称  取值包括JC-积成 SC:三川  EG-艺高
     * @param equipType 
     */
    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    /**
     * @Title: getMeterBookId
     * @Description: 获取表册ID
     * @return 
     */
    public Long getMeterBookId() {
        return meterBookId;
    }

    /**
     * @Title: setMeterBookId
     * @Description: 设置表册ID
     * @param meterBookId 
     */
    public void setMeterBookId(Long meterBookId) {
        this.meterBookId = meterBookId;
    }

	/**
	 * @Title: getTypeCode
	 * @Description: 获取分类编码
	 * @return 
	 */
	public String getTypeCode() {
		return typeCode;
	}

	/**
	 * @Title: setTypeCode
	 * @Description: 设置分类编码
	 * @param typeCode 
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	/**
	 * @Title: getTypeName
	 * @Description: 获取分类名称
	 * @return 
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @Title: setTypeName
	 * @Description: 设置分类名称
	 * @param typeName 
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @Title: getReadMode
	 * @Description: 获取抄表方式
	 * @return 
	 */
	public String getReadMode() {
		return readMode;
	}

	/**
	 * @Title: setReadMode
	 * @Description: 设置抄表方式
	 * @param readMode 
	 */
	public void setReadMode(String readMode) {
		this.readMode = readMode;
	}

	/**
	 * @Title: getBookUserType
	 * @Description: 获取表册用户类型
	 * @return 
	 */
	public Integer getBookUserType() {
		return bookUserType;
	}

	/**
	 * @Title: setBookUserType
	 * @Description: 设置表册类型
	 * @param bookUserType 
	 */
	public void setBookUserType(Integer bookUserType) {
		this.bookUserType = bookUserType;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: toString
	 * @Description: toString()方法
	 * @return 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MeterBookBean [name=" + name + ", equipType=" + equipType + ", meterBookId=" + meterBookId
				+ ", typeCode=" + typeCode + ", typeName=" + typeName + ", readMode=" + readMode + ", bookUserType="
				+ bookUserType + "]";
	}

}
