package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learnbind.ai.common.enumclass.EnumReadType;

@Table(name = "SYS_METER_READ_CONFIG")
public class SysMeterReadConfig {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT READ_CONFIG_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "NUM")
    private Long num;

    @Column(name = "REMARK")
    private String remark;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    
    
    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/**
     * @Title: getTypeStr
     * @Description: 抄表类型字符串
     * @return 
     */
    public String getTypeStr() {
    	return EnumReadType.getName(type);
    }

    /**
     * @return NUM
     */
    public Long getNum() {
        return num;
    }

    /**
     * @param num
     */
    public void setNum(Long num) {
        this.num = num;
    }

    /**
     * @return REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", num=").append(num);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}