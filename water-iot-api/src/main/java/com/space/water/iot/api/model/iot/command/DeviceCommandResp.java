package com.space.water.iot.api.model.iot.command;

import com.alibaba.fastjson.JSONObject;

public class DeviceCommandResp {

	/**
	 * 	commandId	必选	String(1-64)	设备命令ID
	 *	appId	必选	String(1-64)	设备命令撤销任务所属的应用ID
	 *	deviceId	必选	String(1-64)	设备命令指定的设备ID
	 *	command	必选	CommandDTOV4	下发命令的信息，定义参见附录1.3.7 CommandDTOV4 结构体说明
	 *	callbackUrl	可选	String(1024)	命令状态变化通知地址，必须使用HTTPS信道回调地址 (说明：HTTP信道只可用于调测，不能用于商用环境)；
当命令状态变化时(执行失败，执行成功，超时，发送，已送达)会通知NA，平台会以POST方式发送HTTP消息给应用，请求Body为json字符串，格式形如：{"deviceId":"deviceId","commandId":"commandId","result":{"status":"SUCCESS","result":{…}}}
	 *	expireTime	可选	Integer(>=0)	下发命令有效的超期时间，单位为秒，表示设备命令在创建后expireTime秒内有效，超过这个时间范围后命令将不再下发，如果未设置则默认为48小时
	 *	status	必选	String	设备命令的状态，DEFAULT表示未下发，EXPIRED表示命令已经过期，SUCCESSFUL表示命令已经成功执行，FAILED表示命令执行失败，TIMEOUT表示命令下发执行超时，CANCELED表示命令已经被撤销执行
	 *	result	可选	ObjectNode	设备命令执行的详细结果，格式 为json字符串
	 *	creationTime	必选	String(20)	设备命令的创建时间
	 *	executeTime	可选	String(20)	设备命令执行的时间
	 *	platformIssuedTime	可选	String(20)	平台发送设备命令的时间
	 *	deliveredTime	可选	String(20)	平台将设备命令送达到设备的时间
	 *	issuedTimes	可选	Integer(>=0)	平台发送设备命令的次数
	 */
	
	private String commandId;
	private String appId;
	private String deviceId;
	private CommandDTOV4 command;
	private String callbackUrl;
	private int expireTime;
	private String status;
	private JSONObject result;
	private String creationTime;
	private String executeTime;
	private String platformIssuedTime;
	private String deliveredTime;
	private int issuedTimes;
	public String getCommandId() {
		return commandId;
	}
	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public CommandDTOV4 getCommand() {
		return command;
	}
	public void setCommand(CommandDTOV4 command) {
		this.command = command;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public int getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public JSONObject getResult() {
		return result;
	}
	public void setResult(JSONObject result) {
		this.result = result;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(String executeTime) {
		this.executeTime = executeTime;
	}
	public String getPlatformIssuedTime() {
		return platformIssuedTime;
	}
	public void setPlatformIssuedTime(String platformIssuedTime) {
		this.platformIssuedTime = platformIssuedTime;
	}
	public String getDeliveredTime() {
		return deliveredTime;
	}
	public void setDeliveredTime(String deliveredTime) {
		this.deliveredTime = deliveredTime;
	}
	public int getIssuedTimes() {
		return issuedTimes;
	}
	public void setIssuedTimes(int issuedTimes) {
		this.issuedTimes = issuedTimes;
	}
	
	
}
