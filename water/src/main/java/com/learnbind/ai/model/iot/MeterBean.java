package com.learnbind.ai.model.iot;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.learnbind.ai.iot.protocol.PacketCodec;
import com.learnbind.ai.iot.protocol.PacketFrame;
import com.learnbind.ai.iot.protocol.bean.MeterBase;
import com.learnbind.ai.iot.protocol.bean.MeterConfig;
import com.learnbind.ai.iot.protocol.bean.MeterReadWaterResp;
import com.learnbind.ai.iot.protocol.bean.MeterReport;
import com.learnbind.ai.iot.protocol.util.ByteUtil;
import com.learnbind.ai.iot.protocol.util.HexStringUtils;
import com.learnbind.ai.iot.util.StringUtil;

import javassist.expr.NewArray;

public class MeterBean {

    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("gatewayId")
    private String gatewayId;
    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("serviceType")
    private String serviceType;
    @JsonProperty("eventTime")
    private Date eventTime;

    @JsonProperty("meterType")
    private Integer meterType;
    @JsonProperty("meterAddr")
    private String meterAddr;
    @JsonProperty("factoryCode")
    private String factoryCode;
    @JsonProperty("ctrlCode")
    private String ctrlCode;
    @JsonProperty("dataDI")
    private short dataDI;
    @JsonProperty("sequence")
    private Integer sequence;
    @JsonProperty("dataType")
    private Integer dataType;
    @JsonProperty("data")
    private String data;
    @JsonProperty("dataBasic")
    private String dataBasic;
    @JsonProperty("checksum")
    private Integer checksum;
    @JsonProperty("jsonData")
    private String jsonData;
    @JsonProperty("createtime")
    private Date createtime;
    @JsonProperty("updatetime")
    private Date updatetime;

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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
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

	public static MeterBean fromUploadDataJson(String data) {
        MeterBean meterBean = new MeterBean();
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
        	dataBean.setType(MeterDataBaseBean.METER_DATA_TYPE_START_CONNECT);
        	dataBean.setData(serviceBean.getData().getJRprotocolXY());
//        	dataBean.setDataBasic(serviceBean.getData().getJRprotocolXY());
		} else if (serviceBean.getData().getJRprotocolXY().equalsIgnoreCase("aa1234bb")) {
			//TODO G11 设备即将断开与电信平台的连接
        	dataBean.setType(MeterDataBaseBean.METER_DATA_TYPE_START_DISCONNECT);
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
	            	dataBean.setType(MeterDataBaseBean.METER_DATA_TYPE_REPORT);
	            	
	                MeterReport meterReport = (MeterReport) meterBase;
	                MeterReportBean meterDataBean = MeterReportBean.fromMeterReport(meterReport);
	                dataBean.setData(MeterReportBean.toJsonString(meterDataBean));
	                
				} else if (meterBase instanceof MeterConfig) {
	            	dataBean.setType(MeterDataBaseBean.METER_DATA_TYPE_CONFIG);
	            	
	                MeterConfig meterConfig = (MeterConfig) meterBase;
	                MeterConfigBean meterConfigBean = MeterConfigBean.fromMeterConfig(meterConfig);
	                dataBean.setData(MeterConfigBean.toJsonString(meterConfigBean));
	                
				} else if (meterBase instanceof MeterReadWaterResp) {
	            	dataBean.setType(MeterDataBaseBean.METER_DATA_TYPE_MONTH_FREEZE);
	            	
					MeterReadWaterResp meterReadWaterResp = (MeterReadWaterResp) meterBase;
					MeterMonthFreezeBean meterMonthFreezeBean = MeterMonthFreezeBean.fromMeterTeadWaterResp(meterReadWaterResp);
					dataBean.setData(MeterMonthFreezeBean.toJsonString(meterMonthFreezeBean));
					
				} else {
	            	dataBean.setType(MeterDataBaseBean.METER_DATA_TYPE_UNKNOWN);
	            	dataBean.setData(JSON.toJSONString(meterBase));
				}
			} catch (Exception e) {
				if (serviceBean != null && serviceBean.getData() != null) {
	            	dataBean.setType(MeterDataBaseBean.METER_DATA_TYPE_UNKNOWN);
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
		MeterBean meterBean = MeterBean.fromUploadDataJson(data);
		System.out.println(JSON.toJSON(meterBean.getData()));
		System.out.println(JSON.toJSON(meterBean));
		
		System.out.println(meterBean.getEventTime().toString());
		System.out.println(MeterReportBean.fromJson(meterBean.getData()).getMeterTime().toString());
	}
}
