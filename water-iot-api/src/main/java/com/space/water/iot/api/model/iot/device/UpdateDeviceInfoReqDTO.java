package com.space.water.iot.api.model.iot.device;

public class UpdateDeviceInfoReqDTO {
	private String name;
	private String endUser;

	private Enum mute;
	private String manufacturerId;
	private String manufacturerName;
	private String location;
	private String deviceType;
	private String model;
	private String protocolType;
	private DeviceConfigDTO deviceConfig;
	private String region;
	private String organization;
	private String timezone;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEndUser() {
		return endUser;
	}
	public void setEndUser(String endUser) {
		this.endUser = endUser;
	}
	public Enum getMute() {
		return mute;
	}
	public void setMute(Enum mute) {
		this.mute = mute;
	}
	public String getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
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
	public String getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
	public DeviceConfigDTO getDeviceConfig() {
		return deviceConfig;
	}
	public void setDeviceConfig(DeviceConfigDTO deviceConfig) {
		this.deviceConfig = deviceConfig;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
}
