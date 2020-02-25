package com.space.water.iot.api.model.iot.device;

public class DeviceInfo {
	/**
	 * nodeId	必选	String	设备唯一标识
		name	可选	String	设备名称
		description	可选	String	设备描述
		manufactureId	必选	String	供应商标识
		manufacturerName	可选	String	供应商名字
		mac	可选	String	物理地址
		location	可选	String	设备位置
		deviceType	必选	String	设备种类：MultiSensor，ContactSensor，Camera，Gateway
		model	必选	String	设备型号. 如果是Z-Wave协议，型号就是ProductType + ProductId（用零填充，如果需要） 格式的十六进制数,例如   001A-0A12.
如果是其他协议，型号需要另一种格式表示
		swVersion	可选	String	使用Z-Wave协议时的软件版本号。 其格式是 major version number.minor version number, 例如, 1.1.
		fwVersion	可选	String	固件版本号
		hwVersion	可选	String	硬件版本号
		protocolType	必选	String	协议类型 (Z-Wave).
		signalStrength	可选	String	信号强度
		bridgeId	可选	String	网桥标识
		supportedSecurity	可选	String	是否支持安全模式，其取值为TRUE或FALSE
		isSecurity	可选	String	设备是否安全，在安全模式为TRUE的情况下有效，其取值为TRUE或FALSE
		sigVersion	可选	String	信令版本
		runningStatus	可选	String	设备运行状态：NORMAL：正常  ABNORMAL：异常  FAULT：故障
		status	可选	String	设备状态. ONLINE: 在线  OFFLINE: 下线  INBOX: 停用.
		statusDetail	可选	String	设备状态的详细情况
		mute	可选	String	设备是否进行屏蔽  TRUE: 被屏蔽.  FALSE: 没被屏蔽.
	 */
	
	String nodeId;
	String name;
	String description;
	String manufactureId;
	String manufacturerName;
	String mac;
	String location;
	String deviceType;
	String model;
	String swVersion;
	String fwVersion;
	String hwVersion;
	String protocolType;
	String signalStrength;
	String bridgeId;
	String supportedSecurity;
	String isSecurity;
	String sigVersion;
	String runningStatus;
	String status;
	String statusDetail;
	String mute;
	
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getManufactureId() {
		return manufactureId;
	}
	public void setManufactureId(String manufactureId) {
		this.manufactureId = manufactureId;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSwVersion() {
		return swVersion;
	}
	public void setSwVersion(String swVersion) {
		this.swVersion = swVersion;
	}
	public String getFwVersion() {
		return fwVersion;
	}
	public void setFwVersion(String fwVersion) {
		this.fwVersion = fwVersion;
	}
	public String getHwVersion() {
		return hwVersion;
	}
	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}
	public String getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
	public String getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(String signalStrength) {
		this.signalStrength = signalStrength;
	}
	public String getBridgeId() {
		return bridgeId;
	}
	public void setBridgeId(String bridgeId) {
		this.bridgeId = bridgeId;
	}
	public String getSupportedSecurity() {
		return supportedSecurity;
	}
	public void setSupportedSecurity(String supportedSecurity) {
		this.supportedSecurity = supportedSecurity;
	}
	public String getIsSecurity() {
		return isSecurity;
	}
	public void setIsSecurity(String isSecurity) {
		this.isSecurity = isSecurity;
	}
	public String getSigVersion() {
		return sigVersion;
	}
	public void setSigVersion(String sigVersion) {
		this.sigVersion = sigVersion;
	}
	public String getRunningStatus() {
		return runningStatus;
	}
	public void setRunningStatus(String runningStatus) {
		this.runningStatus = runningStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDetail() {
		return statusDetail;
	}
	public void setStatusDetail(String statusDetail) {
		this.statusDetail = statusDetail;
	}
	public String getMute() {
		return mute;
	}
	public void setMute(String mute) {
		this.mute = mute;
	}
	
	
	
}
