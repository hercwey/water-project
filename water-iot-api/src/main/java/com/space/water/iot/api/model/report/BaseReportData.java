package com.space.water.iot.api.model.report;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.space.water.iot.api.model.command.QueryMonthDataResponse;
import com.space.water.iot.api.model.common.DeviceParams;
import com.space.water.iot.api.model.common.MonthData;
import com.space.water.iot.api.model.common.ReportDataType;
import com.space.water.iot.api.model.iot.report.ServiceBean;
import com.space.water.iot.api.model.iot.report.UploadMessageBean;
import com.space.water.iot.api.protocol.PacketCodec;
import com.space.water.iot.api.protocol.PacketFrame;
import com.space.water.iot.api.protocol.bean.MeterBase;
import com.space.water.iot.api.protocol.bean.MeterConfig;
import com.space.water.iot.api.protocol.bean.MeterConfigReadResp;
import com.space.water.iot.api.protocol.bean.MeterConfigWriteResp;
import com.space.water.iot.api.protocol.bean.MeterReadWaterResp;
import com.space.water.iot.api.protocol.bean.MeterReport;
import com.space.water.iot.api.protocol.bean.MeterValveControlResp;
import com.space.water.iot.api.protocol.bean.MeterVolumeThresholdResp;
import com.space.water.iot.api.protocol.util.HexStringUtils;
import com.space.water.iot.api.util.StringUtil;

public class BaseReportData {

    private Long id;
    private String deviceId;
    private String gatewayId;
    private String requestId;
    private String serviceId;
    private String serviceType;
    private Date eventTime;

    private Integer meterType;
    private String meterAddr;
    private String factoryCode;
    private String ctrlCode;
    private short dataDI;
    private Integer sequence;
    private Integer dataType;
    private String data;
    private String dataBasic;
    private Integer checksum;
    private String jsonData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public Integer getMeterType() {
        return meterType;
    }

    public void setMeterType(Integer meterType) {
        this.meterType = meterType;
    }

    public String getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr;
    }

    public String getFactoryCode() {
        return factoryCode;
    }

    public void setFactoryCode(String factoryCode) {
        this.factoryCode = factoryCode;
    }

    public String getCtrlCode() {
        return ctrlCode;
    }

    public void setCtrlCode(String ctrlCode) {
        this.ctrlCode = ctrlCode;
    }

    public short getDataDI() {
        return dataDI;
    }

    public void setDataDI(short dataDI) {
        this.dataDI = dataDI;
        //setDataDIStr(HexStringUtils.bytesToHexString(ByteUtil.getBytes(dataDI)));
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getDataBasic() {
		return dataBasic;
	}

	public void setDataBasic(String dataBasic) {
		this.dataBasic = dataBasic;
	}

	public static String toJsonString(BaseReportData bean) {
		return JSON.toJSONString(bean);
	}
	
	public static BaseReportData fromUploadDataJson(String data) {
        BaseReportData meterBean = new BaseReportData();
        UploadMessageBean uploadMessageBean = UploadMessageBean.parseJson(data);

        meterBean.setDeviceId(uploadMessageBean.getDeviceId());
        meterBean.setGatewayId(uploadMessageBean.getGatewayId());
        meterBean.setRequestId(uploadMessageBean.getRequestId());
        meterBean.setJsonData(data);

        ServiceBean serviceBean = uploadMessageBean.getService();
        if (serviceBean == null) {
            JSONArray services= uploadMessageBean.getServices();
            if (services.size() > 0) {
                serviceBean = ServiceBean.parseJson(services.getJSONObject(0).toJSONString());
            }
        }

        meterBean.setServiceId(serviceBean.getServiceId());
        meterBean.setServiceType(serviceBean.getServiceType());
        meterBean.setEventTime(StringUtil.timeZoneTrans(serviceBean.getEventTime()));
        
    	MeterDataBaseBean dataBean = new MeterDataBaseBean();
    	dataBean.setDataBasic(serviceBean.getData().getJRprotocolXY());
    	
    	if (serviceBean.getData().getJRprotocolXY().equalsIgnoreCase("9912364557")) {
			//TODO G11 设备开始与电信平台建立连接
        	dataBean.setType(ReportDataType.METER_DATA_TYPE_START_CONNECT);
        	dataBean.setData(serviceBean.getData().getJRprotocolXY());
//        	dataBean.setDataBasic(serviceBean.getData().getJRprotocolXY());
		} else if (serviceBean.getData().getJRprotocolXY().equalsIgnoreCase("aa1234bb")) {
			//TODO G11 设备即将断开与电信平台的连接
        	dataBean.setType(ReportDataType.METER_DATA_TYPE_START_DISCONNECT);
        	dataBean.setData(serviceBean.getData().getJRprotocolXY());
//        	dataBean.setDataBasic(serviceBean.getData().getJRprotocolXY());
		} else {
			try {
	        	PacketFrame packetFrame = PacketCodec.decodeFrame(HexStringUtils.hexStringToBytes(serviceBean.getData().getJRprotocolXY()));

	            meterBean.setMeterType(packetFrame.getMeterType());
	            meterBean.setMeterAddr(packetFrame.getMeterAddr());
	            meterBean.setFactoryCode(packetFrame.getFactoryCode());
	            meterBean.setCtrlCode(packetFrame.getCtrlCodeStr());
	            meterBean.setDataDI(packetFrame.getDataDI());	            
	            meterBean.setSequence(packetFrame.getSequence());
	            //meterBean.setData(HexStringUtils.bytesToHexString(packetFrame.getData()));
	            meterBean.setChecksum(Integer.valueOf(packetFrame.getChecksum()));
	            
	            MeterBase meterBase = PacketCodec.decodeData(packetFrame);
//	            dataBean.setDataBasic(HexStringUtils.bytesToHexString(packetFrame.getData()));
	            
	            //FIXME G11 针对数据上报的其他类型数据，进行解析（后续根据需求对数据进行分类，优化处理逻辑）
	            if (meterBase instanceof MeterReport) {
	            	dataBean.setType(ReportDataType.METER_DATA_TYPE_REPORT);
	            	
	                MeterReport meterReport = (MeterReport) meterBase;
	                MeterReportBean meterDataBean = MeterReportBean.fromMeterReport(meterReport);
	                dataBean.setData(MeterReportBean.toJsonString(meterDataBean));
	                
				} else if (meterBase instanceof MeterConfigReadResp) {
	            	dataBean.setType(ReportDataType.METER_DATA_TYPE_RSP_READ_CONFIG);
	            	
	                MeterConfig meterConfig = (MeterConfig) meterBase;
	                DeviceParams meterConfigBean = DeviceParams.fromMeterConfig(meterConfig);
	                dataBean.setData(DeviceParams.toJsonString(meterConfigBean));
	                
				} else if (meterBase instanceof MeterReadWaterResp) {
	            	dataBean.setType(ReportDataType.METER_DATA_TYPE_MONTH_FREEZE);
	            	
					MeterReadWaterResp meterReadWaterResp = (MeterReadWaterResp) meterBase;
					MonthData meterMonthFreezeBean = MonthData.fromMeterTeadWaterResp(meterReadWaterResp);
					dataBean.setData(MonthData.toJsonString(meterMonthFreezeBean));
					
				} else if (meterBase instanceof MeterConfigWriteResp) {

	            	dataBean.setType(ReportDataType.METER_DATA_TYPE_RSP_WRITE_CONFIG);
	            	MeterConfig meterConfig = (MeterConfig) meterBase;
	            	DeviceParams meterConfigBean = DeviceParams.fromMeterConfig(meterConfig);
	                dataBean.setData(DeviceParams.toJsonString(meterConfigBean));
	                
				} else if (meterBase instanceof MeterValveControlResp) {

	            	dataBean.setType(ReportDataType.METER_DATA_TYPE_RSP_SWITCH_VALVE);
	                dataBean.setData(JSON.toJSONString(meterBase));
	                
				} else if (meterBase instanceof MeterVolumeThresholdResp) {

	            	dataBean.setType(ReportDataType.METER_DATA_TYPE_RSP_SET_THRESHOLD);
	                dataBean.setData(JSON.toJSONString(meterBase));
	                
				} else {
	            	dataBean.setType(ReportDataType.METER_DATA_TYPE_UNKNOWN);
	            	dataBean.setData(JSON.toJSONString(meterBase));
				}
			} catch (Exception e) {
				if (serviceBean != null && serviceBean.getData() != null) {
	            	dataBean.setType(ReportDataType.METER_DATA_TYPE_UNKNOWN);
	            	dataBean.setData(serviceBean.getData().getJRprotocolXY());
				}
			}
		}
    	
        meterBean.setDataType(dataBean.getType());
        meterBean.setData(dataBean.getData());
        meterBean.setDataBasic(dataBean.getDataBasic());
        return meterBean;
    }	
	
	public static void main(String[] args) {
		String data = "{\"notifyType\":\"deviceDatasChanged\",\"requestId\":null,\"deviceId\":\"20a1a5a3-7705-4850-92fd-9deb88988c24\",\"gatewayId\":\"20a1a5a3-7705-4850-92fd-9deb88988c24\",\"services\":[{\"serviceId\":\"JRprotocol\",\"serviceType\":\"JRprotocol\",\"data\":{\"JRprotocolXY\":\"681002390745404358811d1f90a702390745404350172309010220a6090000024836090015000000db16\"},\"eventTime\":\"20200206T050809Z\"}]}";
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(data);
		System.out.println(JSON.toJSON(meterBean.getData()));
		System.out.println(JSON.toJSON(meterBean));
		
		System.out.println(meterBean.getEventTime().toString());
		System.out.println(MeterReportBean.fromJson(meterBean.getData()).getMeterTime().toString());
	}
}
