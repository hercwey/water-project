package com.learnbind.ai.bean;

import java.util.List;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: LocationDataBean.java
 * @Description: 地理位置数据Bean
 *
 * @author Administrator
 * @date 2019年6月20日 下午12:01:13
 * @version V1.0 
 *
 */
public class LocationDataBean {

	private String nodeType;//节点类型
	private String name;//名称
	private String code;//编码
	private String longCode;//长编码
	
	private List<LocationDataBean> children;//子节点

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLongCode() {
		return longCode;
	}

	public void setLongCode(String longCode) {
		this.longCode = longCode;
	}

	public List<LocationDataBean> getChildren() {
		return children;
	}

	public void setChildren(List<LocationDataBean> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
