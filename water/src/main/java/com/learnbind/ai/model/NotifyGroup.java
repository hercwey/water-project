package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learnbind.ai.common.enumclass.EnumGroupIncludeFlag;
import com.learnbind.ai.common.enumclass.EnumWaterNotifyGroupType;

@Table(name = "NOTIFY_GROUP")
public class NotifyGroup {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT NOTIFY_GROUP_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "INCLUDE_FLAG")
    private Integer includeFlag;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;
    
    @Column(name = "USE_LOCATION")
    private Integer useLocation;

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

    /**
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return TYPE
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }
    
    /**
     * @Title: getTypeStr
     * @Description: 获取水费通知单分组类型
     * @return 
     */
    public String getTypeStr() {
    	if(type!=null) {
    		return EnumWaterNotifyGroupType.getName(type);
    	}
    	return null;
    }

    /**
     * @return INCLUDE_FLAG
     */
    public Integer getIncludeFlag() {
        return includeFlag;
    }

    /**
     * @param includeFlag
     */
    public void setIncludeFlag(Integer includeFlag) {
        this.includeFlag = includeFlag;
    }
    
    /**
     * @Title: getIncludeFlagStr
     * @Description: 水费通知单分组包含关系字符串
     * @return 
     */
    public String getIncludeFlagStr() {
    	if(includeFlag!=null) {
    		return EnumGroupIncludeFlag.getName(includeFlag);
    	}
    	return null;
    }

    /**
     * @return CUSTOMER_ID
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getUseLocation() {
		return useLocation;
	}

	public void setUseLocation(Integer useLocation) {
		this.useLocation = useLocation;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", includeFlag=").append(includeFlag);
        sb.append(", customerId=").append(customerId);
        sb.append(", useLocation=").append(useLocation);
        sb.append("]");
        return sb.toString();
    }
}