package com.learnbind.ai.model;

import javax.persistence.*;

@Table(name = "SYS_INTERFACE_CONFIG")
public class SysInterfaceConfig {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "INTERFACE_TYPE")
    private String interfaceType;

    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;

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

    /**
     * @return INTERFACE_TYPE
     */
    public String getInterfaceType() {
        return interfaceType;
    }

    /**
     * @param interfaceType
     */
    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType == null ? null : interfaceType.trim();
    }

    /**
     * @return KEY
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }

    /**
     * @return VALUE
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
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
        sb.append(", interfaceType=").append(interfaceType);
        sb.append(", key=").append(key);
        sb.append(", value=").append(value);
        sb.append(", remark=").append(remark);
        sb.append("]");
        return sb.toString();
    }
}