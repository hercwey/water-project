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
}
