package com.learnbind.ai.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.learnbind.ai.common.enumclass.EnumIdType;
import com.learnbind.ai.common.enumclass.EnumReplyStatus;
import com.learnbind.ai.common.enumclass.EnumSatisfiedStatus;

@Table(name = "CUSTOMER_INFO_COLLECT")
public class CustomerInfoCollect {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT CUS_INFO_C_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_PHONE")
    private String customerPhone;

    @Column(name = "QUESTION")
    private String question;

    @Column(name = "SOLUTION")
    private String solution;

    @Column(name = "REPLY_STATUS")
    private Integer replyStatus;

    @Column(name = "SATISFIED_STATUS")
    private Integer satisfiedStatus;

    @Column(name = "OPERATION_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "REPLY_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;

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

   

    public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
     * @return CUSTOMER_PHONE
     */
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @param customerPhone
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone == null ? null : customerPhone.trim();
    }

    /**
     * @return QUESTION
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question
     */
    public void setQuestion(String question) {
        this.question = question == null ? null : question.trim();
    }

    /**
     * @return SOLUTION
     */
    public String getSolution() {
        return solution;
    }

    /**
     * @param solution
     */
    public void setSolution(String solution) {
        this.solution = solution == null ? null : solution.trim();
    }

    /**
     * @return REPLY_STATUS
     */
    public Integer getReplyStatus() {
        return replyStatus;
    }

    /**
     * @param replyStatus
     */
    public void setReplyStatus(Integer replyStatus) {
        this.replyStatus = replyStatus;
    }
    
    /**
     * @Title: getReplyStatusStr
     * @Description: 回复状态字符串
     * @return 
     */
    public String getReplyStatusStr() {
    	return EnumReplyStatus.getName(replyStatus);
    }

    /**
     * @return SATISFIED_STATUS
     */
    public Integer getSatisfiedStatus() {
        return satisfiedStatus;
    }

    /**
     * @param satisfiedStatus
     */
    public void setSatisfiedStatus(Integer satisfiedStatus) {
        this.satisfiedStatus = satisfiedStatus;
    }
    
    /**
     * @Title: getReplyStatusStr
     * @Description: 满意程度字符串
     * @return 
     */
    public String getSatisfiedStatusStr() {
    	return EnumSatisfiedStatus.getName(satisfiedStatus);
    }

    /**
     * @return OPERATION_TIME
     */
    public Date getOperationTime() {
        return operationTime;
    }

    /**
     * @param operationTime
     */
    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }
    
    /**
     * @Title: getOperationTimeStr
     * @Description: 获取操作时间字符串
     * @return 
     */
    public String getOperationTimeStr() {
    	if(operationTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(operationTime);
    	}
    	return null;
    }

    /**
     * @return OPERATOR
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @param operator
     */
    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    /**
     * @return REPLY_TIME
     */
    public Date getReplyTime() {
        return replyTime;
    }

    /**
     * @param replyTime
     */
    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }
    
    /**
     * @Title: getOperationTimeStr
     * @Description: 获取回复时间字符串
     * @return 
     */
    public String getReplyTimeStr() {
    	if(replyTime != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        	return sdf.format(replyTime);
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
        sb.append(", customerName=").append(customerName);
        sb.append(", customerPhone=").append(customerPhone);
        sb.append(", question=").append(question);
        sb.append(", solution=").append(solution);
        sb.append(", replyStatus=").append(replyStatus);
        sb.append(", satisfiedStatus=").append(satisfiedStatus);
        sb.append(", operationTime=").append(operationTime);
        sb.append(", operator=").append(operator);
        sb.append(", replyTime=").append(replyTime);
        sb.append("]");
        return sb.toString();
    }
}