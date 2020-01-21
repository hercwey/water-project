package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "STAT_OWE_COMPANY_DETAIL")
public class StatOweCompanyDetail {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT STAT_O_C_DETAIL_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STAT_JSON")
    private String statJson;

    @Column(name = "CREATE_TIME")
    private Date createTime;

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
     * @return PERIOD
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period
     */
    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
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
     * @return STAT_JSON
     */
    public String getStatJson() {
        return statJson;
    }

    /**
     * @param statJson
     */
    public void setStatJson(String statJson) {
        this.statJson = statJson == null ? null : statJson.trim();
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", period=").append(period);
        sb.append(", name=").append(name);
        sb.append(", statJson=").append(statJson);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}