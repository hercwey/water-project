package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumMeterCheckResult;
import com.learnbind.ai.common.enumclass.EnumMeterStatus;

@Table(name = "SYS_CHECK_METER_RESULT")
public class SysCheckMeterResult {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CHECK_M_RESULT_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "METER_ID")
    private Long meterId;

    @Column(name = "METER_NO")
    private String meterNo;

    @Column(name = "FACTORY")
    private String factory;

    @Column(name = "STEEL_SEAL_NO")
    private String steelSealNo;

    @Column(name = "METER_BOTTOM")
    private String meterBottom;

    @Column(name = "CHECK_RESULT")
    private String checkResult;

    @Column(name = "CHECK_FACTORY")
    private String checkFactory;

    @Column(name = "CHECK_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    @Column(name = "CHECK_PEOPLE")
    private String checkPeople;
    
    @Column(name = "METER_PLACE")
    private String meterPlace;
    
    @Column(name = "CHECK_TYPE")
    private Integer checkType;
    
    @Column(name = "CHECK_RESULT_TYPE")
    private Integer checkResultType;

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
     * @return METER_ID
     */
    public Long getMeterId() {
        return meterId;
    }

    /**
     * @param meterId
     */
    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    /**
     * @return METER_NO
     */
    public String getMeterNo() {
        return meterNo;
    }

    /**
     * @param meterNo
     */
    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo == null ? null : meterNo.trim();
    }

    /**
     * @return FACTORY
     */
    public String getFactory() {
        return factory;
    }

    /**
     * @param factory
     */
    public void setFactory(String factory) {
        this.factory = factory == null ? null : factory.trim();
    }

    /**
     * @return STEEL_SEAL_NO
     */
    public String getSteelSealNo() {
        return steelSealNo;
    }

    /**
     * @param steelSealNo
     */
    public void setSteelSealNo(String steelSealNo) {
        this.steelSealNo = steelSealNo == null ? null : steelSealNo.trim();
    }

    /**
     * @return METER_BOTTOM
     */
    public String getMeterBottom() {
        return meterBottom;
    }

    /**
     * @param meterBottom
     */
    public void setMeterBottom(String meterBottom) {
        this.meterBottom = meterBottom == null ? null : meterBottom.trim();
    }

    /**
     * @return CHECK_RESULT
     */
    public String getCheckResult() {
        return checkResult;
    }

    /**
     * @param checkResult
     */
    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult == null ? null : checkResult.trim();
    }

    /**
     * @return CHECK_FACTORY
     */
    public String getCheckFactory() {
        return checkFactory;
    }

    /**
     * @param checkFactory
     */
    public void setCheckFactory(String checkFactory) {
        this.checkFactory = checkFactory == null ? null : checkFactory.trim();
    }

    /**
     * @return CHECK_TIME
     */
    public Date getCheckTime() {
        return checkTime;
    }

    /**
     * @param checkTime
     */
    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
    
    /**
     * @Title: getCheckTimeStr
     * @Description: 获取检测时间字符串
     * @return 
     */
    public String getCheckTimeStr() {
    	if(checkTime!=null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(checkTime);
    	}
    	return null;
    }

    /**
     * @return CHECK_PEOPLE
     */
    public String getCheckPeople() {
        return checkPeople;
    }

    /**
     * @param checkPeople
     */
    public void setCheckPeople(String checkPeople) {
        this.checkPeople = checkPeople == null ? null : checkPeople.trim();
    }
    
    

    public String getMeterPlace() {
		return meterPlace;
	}

	public void setMeterPlace(String meterPlace) {
		this.meterPlace = meterPlace;
	}
	
	

	public Integer getCheckType() {
		return checkType;
	}

	public void setCheckType(Integer checkType) {
		this.checkType = checkType;
	}

	public Integer getCheckResultType() {
		return checkResultType;
	}

	public void setCheckResultType(Integer checkResultType) {
		this.checkResultType = checkResultType;
	}
	
	public String getCheckResultTypeStr() {
    	if(checkResultType!=null) {
    		return EnumMeterCheckResult.getName(checkResultType);
    	}
    	return null;
    }

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", meterId=").append(meterId);
        sb.append(", meterNo=").append(meterNo);
        sb.append(", factory=").append(factory);
        sb.append(", steelSealNo=").append(steelSealNo);
        sb.append(", meterBottom=").append(meterBottom);
        sb.append(", checkResult=").append(checkResult);
        sb.append(", checkFactory=").append(checkFactory);
        sb.append(", checkTime=").append(checkTime);
        sb.append(", checkPeople=").append(checkPeople);
        sb.append(", meterPlace=").append(meterPlace);
        sb.append(", checkType=").append(checkType);
        sb.append(", checkResultType=").append(checkResultType);
        sb.append("]");
        return sb.toString();
    }
}