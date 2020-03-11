package com.space.water.iot.api.rocketmq;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.config.rocketmq
 *
 * @Title: RocketTopicConfig.java
 * @Description: 读取RocketMQ的topic配置文件
 *
 * @author SRD
 * @date 2020年2月20日 下午3:00:07
 * @version V1.0 
 *
 */
@Configuration
@PropertySource(value = {"classpath:topic.properties" })
public class RocketTopicConfig {

	private final Log log = LogFactory.getLog(getClass());//日志
	
    @Value("${topic.name}")
    private String topicName;//#topic的名称
    @Value("${tag.auto.report}")
    private String tagAutoReport;//设备自动上报
    @Value("${tag.config.parms.south}")
    private String tagConfigParmsSouth;//配置设备参数
    @Value("${tag.config.parms.north}")
    private String tagConfigParmsNorth;//配置设备参数返回	
    @Value("${tag.config.threshold.south}")
    private String tagConfigThresholdSouth;//设置阈值
    @Value("${tag.config.threshold.north}")
    private String tagConfigThresholdNorth;//设置阈值返回
    @Value("${tag.query.parms.south}")
    private String tagQueryParmsSouth;//查询设备参数
    @Value("${tag.query.parms.north}")
    private String tagQueryParmsNorth;//查询参数返回
    @Value("${tag.query.monthdata.south}")
    private String tagQueryMonthDataSouth;//查询月冻结数据
    @Value("${tag.query.monthdata.north}")
    private String tagQueryMonthdataNorth;//查询月冻结数据返回
    @Value("${tag.control.valve.south}")
    private String tagControlValveSouth;//控制设备（开/关阀等）
    @Value("${tag.control.valve.north}")
    private String tagControlValveNorth;//控制设备返回
    @Value("${tag.order.status}")
    private String tagOrderStatus;//命令执行状态
    @Value("${tag.device.register.south}")
    private String tagDeviceRegisterSouth;//注册设备请求	发布	订阅
    @Value("${tag.device.update.south}")
    private String tagDeviceUpdateSouth;//修改设备请求	发布	订阅
    @Value("${tag.device.delete.south}")
    private String tagDeviceDeleteSouth;//删除设备请求	发布	订阅	
    @Value("${tag.device.query.south}")
    private String tagDeviceQuerySouth;//查询设备请求	发布	订阅
    @Value("${tag.device.register.north}")
    private String tagDeviceRegisterNorth;//注册设备响应	发布	订阅
    @Value("${tag.device.update.north}")
    private String tagDeviceUpdateNorth;//修改设备响应	发布	订阅
    @Value("${tag.device.delete.north}")
    private String tagDeviceDeleteNorth;//删除设备响应	发布	订阅
    @Value("${tag.device.query.north}")
    private String tagDeviceQueryNorth;//查询设备响应	发布	订阅

    @Value("${tag.account.status.read.south}")
    private String tagAccountStatusReadSouth;//查询开户状态	发布	订阅
    @Value("${tag.account.status.read.north}")
    private String tagAccountStatusReadNorth;//查询开户状态	发布	订阅
    @Value("${tag.account.status.write.south}")
    private String tagAccountStatusWriteSouth;//设置开户状态	发布	订阅
    @Value("${tag.account.status.write.north}")
    private String tagAccountStatusWriteNorth;//设置开户状态	发布	订阅

    @Value("${tag.auto.report.cache}")
    private String tagAutoReportCache;//设备自动上报数据缓存
    
    
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getTagAutoReport() {
		return tagAutoReport;
	}
	public void setTagAutoReport(String tagAutoReport) {
		this.tagAutoReport = tagAutoReport;
	}
	public String getTagConfigParmsSouth() {
		return tagConfigParmsSouth;
	}
	public void setTagConfigParmsSouth(String tagConfigParmsSouth) {
		this.tagConfigParmsSouth = tagConfigParmsSouth;
	}
	public String getTagConfigParmsNorth() {
		return tagConfigParmsNorth;
	}
	public void setTagConfigParmsNorth(String tagConfigParmsNorth) {
		this.tagConfigParmsNorth = tagConfigParmsNorth;
	}
	public String getTagConfigThresholdSouth() {
		return tagConfigThresholdSouth;
	}
	public void setTagConfigThresholdSouth(String tagConfigThresholdSouth) {
		this.tagConfigThresholdSouth = tagConfigThresholdSouth;
	}
	public String getTagConfigThresholdNorth() {
		return tagConfigThresholdNorth;
	}
	public void setTagConfigThresholdNorth(String tagConfigThresholdNorth) {
		this.tagConfigThresholdNorth = tagConfigThresholdNorth;
	}
	public String getTagQueryParmsSouth() {
		return tagQueryParmsSouth;
	}
	public void setTagQueryParmsSouth(String tagQueryParmsSouth) {
		this.tagQueryParmsSouth = tagQueryParmsSouth;
	}
	public String getTagQueryParmsNorth() {
		return tagQueryParmsNorth;
	}
	public void setTagQueryParmsNorth(String tagQueryParmsNorth) {
		this.tagQueryParmsNorth = tagQueryParmsNorth;
	}
	public String getTagQueryMonthDataSouth() {
		return tagQueryMonthDataSouth;
	}
	public void setTagQueryMonthDataSouth(String tagQueryMonthDataSouth) {
		this.tagQueryMonthDataSouth = tagQueryMonthDataSouth;
	}
	public String getTagQueryMonthdataNorth() {
		return tagQueryMonthdataNorth;
	}
	public void setTagQueryMonthdataNorth(String tagQueryMonthdataNorth) {
		this.tagQueryMonthdataNorth = tagQueryMonthdataNorth;
	}
	public String getTagControlValveSouth() {
		return tagControlValveSouth;
	}
	public void setTagControlValveSouth(String tagControlValveSouth) {
		this.tagControlValveSouth = tagControlValveSouth;
	}
	public String getTagControlValveNorth() {
		return tagControlValveNorth;
	}
	public void setTagControlValveNorth(String tagControlValveNorth) {
		this.tagControlValveNorth = tagControlValveNorth;
	}
	public String getTagOrderStatus() {
		return tagOrderStatus;
	}
	public void setTagOrderStatus(String tagOrderStatus) {
		this.tagOrderStatus = tagOrderStatus;
	}
	public String getTagDeviceRegisterSouth() {
		return tagDeviceRegisterSouth;
	}
	public void setTagDeviceRegisterSouth(String tagDeviceRegisterSouth) {
		this.tagDeviceRegisterSouth = tagDeviceRegisterSouth;
	}
	public String getTagDeviceUpdateSouth() {
		return tagDeviceUpdateSouth;
	}
	public void setTagDeviceUpdateSouth(String tagDeviceUpdateSouth) {
		this.tagDeviceUpdateSouth = tagDeviceUpdateSouth;
	}
	public String getTagDeviceDeleteSouth() {
		return tagDeviceDeleteSouth;
	}
	public void setTagDeviceDeleteSouth(String tagDeviceDeleteSouth) {
		this.tagDeviceDeleteSouth = tagDeviceDeleteSouth;
	}
	public String getTagDeviceQuerySouth() {
		return tagDeviceQuerySouth;
	}
	public void setTagDeviceQuerySouth(String tagDeviceQuerySouth) {
		this.tagDeviceQuerySouth = tagDeviceQuerySouth;
	}
	public String getTagDeviceRegisterNorth() {
		return tagDeviceRegisterNorth;
	}
	public void setTagDeviceRegisterNorth(String tagDeviceRegisterNorth) {
		this.tagDeviceRegisterNorth = tagDeviceRegisterNorth;
	}
	public String getTagDeviceUpdateNorth() {
		return tagDeviceUpdateNorth;
	}
	public void setTagDeviceUpdateNorth(String tagDeviceUpdateNorth) {
		this.tagDeviceUpdateNorth = tagDeviceUpdateNorth;
	}
	public String getTagDeviceDeleteNorth() {
		return tagDeviceDeleteNorth;
	}
	public void setTagDeviceDeleteNorth(String tagDeviceDeleteNorth) {
		this.tagDeviceDeleteNorth = tagDeviceDeleteNorth;
	}
	public String getTagDeviceQueryNorth() {
		return tagDeviceQueryNorth;
	}
	public void setTagDeviceQueryNorth(String tagDeviceQueryNorth) {
		this.tagDeviceQueryNorth = tagDeviceQueryNorth;
	}
	
	public String getTagAccountStatusReadSouth() {
		return tagAccountStatusReadSouth;
	}
	public void setTagAccountStatusReadSouth(String tagAccountStatusReadSouth) {
		this.tagAccountStatusReadSouth = tagAccountStatusReadSouth;
	}
	public String getTagAccountStatusWriteSouth() {
		return tagAccountStatusWriteSouth;
	}
	public void setTagAccountStatusWriteSouth(String tagAccountStatusWriteSouth) {
		this.tagAccountStatusWriteSouth = tagAccountStatusWriteSouth;
	}
	public String getTagAutoReportCache() {
		return tagAutoReportCache;
	}
	public void setTagAutoReportCache(String tagAutoReportCache) {
		this.tagAutoReportCache = tagAutoReportCache;
	}
	public String getTagAccountStatusReadNorth() {
		return tagAccountStatusReadNorth;
	}
	public void setTagAccountStatusReadNorth(String tagAccountStatusReadNorth) {
		this.tagAccountStatusReadNorth = tagAccountStatusReadNorth;
	}
	public String getTagAccountStatusWriteNorth() {
		return tagAccountStatusWriteNorth;
	}
	public void setTagAccountStatusWriteNorth(String tagAccountStatusWriteNorth) {
		this.tagAccountStatusWriteNorth = tagAccountStatusWriteNorth;
	}
	@Override
	public String toString() {
		return "RocketTopicConfig [topicName=" + topicName + ", tagAutoReport=" + tagAutoReport
				+ ", tagConfigParmsSouth=" + tagConfigParmsSouth + ", tagConfigParmsNorth=" + tagConfigParmsNorth
				+ ", tagConfigThresholdSouth=" + tagConfigThresholdSouth + ", tagConfigThresholdNorth="
				+ tagConfigThresholdNorth + ", tagQueryParmsSouth=" + tagQueryParmsSouth + ", tagQueryParmsNorth="
				+ tagQueryParmsNorth + ", tagQueryMonthDataSouth=" + tagQueryMonthDataSouth
				+ ", tagQueryMonthdataNorth=" + tagQueryMonthdataNorth + ", tagControlValveSouth="
				+ tagControlValveSouth + ", tagControlValveNorth=" + tagControlValveNorth + ", tagOrderStatus="
				+ tagOrderStatus + ", tagDeviceRegisterSouth=" + tagDeviceRegisterSouth + ", tagDeviceUpdateSouth="
				+ tagDeviceUpdateSouth + ", tagDeviceDeleteSouth=" + tagDeviceDeleteSouth + ", tagDeviceQuerySouth="
				+ tagDeviceQuerySouth + ", tagDeviceRegisterNorth=" + tagDeviceRegisterNorth + ", tagDeviceUpdateNorth="
				+ tagDeviceUpdateNorth + ", tagDeviceDeleteNorth=" + tagDeviceDeleteNorth + ", tagDeviceQueryNorth="
				+ tagDeviceQueryNorth + ", tagAutoReportCache=" + tagAutoReportCache
				+ "]";
	}
    
}
