package com.learnbind.ai.bean;

import java.util.List;

import com.learnbind.ai.common.enumclass.EnumGroupIncludeFlag;
import com.learnbind.ai.common.enumclass.EnumWaterNotifyGroupType;
import com.learnbind.ai.model.NotifyGroupMeter;

public class NotifyGroupBean {
    
    private Long id;

    private String name;

    private Integer type;

    private Integer includeFlag;

    private Long customerId;
    
    private List<NotifyGroupMeter> groupMeterList;

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

    public List<NotifyGroupMeter> getGroupMeterList() {
		return groupMeterList;
	}

	public void setGroupMeterList(List<NotifyGroupMeter> groupMeterList) {
		this.groupMeterList = groupMeterList;
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
        sb.append(", groupMeterList=").append(groupMeterList);
        sb.append("]");
        return sb.toString();
    }
}