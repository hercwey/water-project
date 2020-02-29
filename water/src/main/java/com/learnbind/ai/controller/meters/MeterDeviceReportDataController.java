package com.learnbind.ai.controller.meters;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.iot.protocol.util.ByteUtil;
import com.learnbind.ai.iot.protocol.util.HexStringUtils;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.TestMeterDataBaseBean;
import com.learnbind.ai.model.iot.TestMeterReportBean;
import com.learnbind.ai.model.iot.TestMeterStatusBean;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.service.iot.IMeterService;
import com.learnbind.ai.service.meters.MetersService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterDeviceReportDataController.java
 * @Description: 水表设备上报数据查询前端控制器
 *
 * @author SRD
 * @date 2020年3月1日 上午1:01:31
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-report")
public class MeterDeviceReportDataController {
	private static Log log = LogFactory.getLog(MeterDeviceReportDataController.class);
	private static final String TEMPLATE_PATH = "meters/meter_report/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private MetersService metersService;  //表计档案
	@Autowired
	IMeterService imeterService;
//	@Autowired
//	private WmDeviceService wmDeviceService;
	
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		
		return TEMPLATE_PATH + "starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "main";
	}

	/**
	 * @Title: searchMeters
	 * @Description: 查询上报数据列表
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/search-meters")
	public String searchMeters(Model model, Integer pageNum, Integer pageSize, Integer searchDataType,
			String searchCond) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<WmMeter> meterList = imeterService.searchList(searchDataType, searchCond);
		PageInfo<WmMeter> pageInfo = new PageInfo<>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)

		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for (WmMeter meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);// 实体转MAP

			Long deviceId = meter.getDeviceId();// 设备（水表）表主键ID

			Meters device = metersService.selectByPrimaryKey(deviceId);// 查询水表信息
			meterMap.put("verifyCode", device.getMeterNo());

			String meterData = meter.getMeterData();
			TestMeterReportBean meterReportBean = null;
			Integer dataType = null;

			TestMeterDataBaseBean meterDataBaseBean = new TestMeterDataBaseBean();// MeterDataBaseBean.fromJson(meterData);
			try {
				meterDataBaseBean = new TestMeterDataBaseBean();
				meterDataBaseBean.setType(meter.getMeterDataType());
				dataType = meterDataBaseBean.getType();// 赋值表计数据类型：0=未知类型数据；1=设备上报数据；2=设备配置信息数据；3=设备月冻结数据；
				meterDataBaseBean.setData(meter.getMeterData());
				meterDataBaseBean.setDataBasic(meter.getMeterDataBasic());
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (StringUtils.isNotBlank(meterData)) {
				if (meterData.startsWith("{") && meterData.endsWith("}")) {
					// TODO G11 meter_data字段,增加多种类型数据，根据type判断数据类型
					if (meterDataBaseBean.getType() == TestMeterDataBaseBean.METER_DATA_TYPE_REPORT
							|| meterDataBaseBean.getType() == TestMeterDataBaseBean.METER_DATA_TYPE_RSP_READ_CONFIG
							|| meterDataBaseBean.getType() == TestMeterDataBaseBean.METER_DATA_TYPE_RSP_WRITE_CONFIG) {
						meterReportBean = TestMeterReportBean.fromJson(meterDataBaseBean.getData());
					}
				} else {
					try {
						meterReportBean = TestMeterReportBean.fromHexData(meterData);
					} catch (Exception e) {
						e.printStackTrace();
						meterReportBean = null;
						System.out.println("----------解析16进制数据异常（MeterDataBean.fromHexData），原16进制数据：" + meterData);
					}
				}
			}

			String meterNumber = "";// 表号: 6字节数字型字符串
			String meterTime = "";// 表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
			BigDecimal totalVolumeBd = null;// 累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
			String sampleUnit = "";// 采样参数：单位M3
			Integer batteryVoltage = null;// 电池电压：单位V
			TestMeterStatusBean meterStatus = null;// 表状态字：2字节
			String signal = "";// 信号强度
			String pressure = "";// 压力值：xx.yyyy
			if (meterReportBean != null) {
				meterNumber = meterReportBean.getMeterNumber();// 表号: 6字节数字型字符串
				Date meterTimeD = meterReportBean.getMeterTime();// 表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
				int totalVolume = meterReportBean.getTotalVolume();// 累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
				meterTime = sdf.format(meterTimeD);// 表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
				totalVolume = meterReportBean.getTotalVolume();// 累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
				sampleUnit = meterReportBean.getSampleUnit();// 采样参数：单位M3
				batteryVoltage = meterReportBean.getBatteryVoltage();// 电池电压：单位V
				// meterStatus =
				// MeterStatusBean.toJsonString(meterReportBean.getMeterStatus());//表状态字：2字节
				meterStatus = meterReportBean.getMeterStatus();// 表状态字：2字节
				signal = meterReportBean.getSignal();// 信号强度
				pressure = meterReportBean.getPressure();// 压力值：xx.yyyy
				totalVolumeBd = BigDecimalUtils.multiply(new BigDecimal(totalVolume), new BigDecimal(sampleUnit));// 累计使用量整数,
																													// (用水量(M3)
			}

			meterMap.put("dataType", dataType);
			meterMap.put("meterNumber", meterNumber);// 表号: 6字节数字型字符串
			meterMap.put("meterTime", meterTime);// 表当前时间: 7字节数字字符串(YYMMWWDDhhmmss), 年、月、星期、日、时、分、秒
			meterMap.put("totalVolumeBd", totalVolumeBd);// 累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
			meterMap.put("sampleUnit", sampleUnit);// 采样参数：单位M3
			meterMap.put("batteryVoltage", batteryVoltage);// 电池电压：单位V
			meterMap.put("meterStatus", meterStatus);// 表状态字：2字节
			meterMap.put("signal", signal);// 信号强度
			meterMap.put("pressure", pressure);// 压力值：xx.yyyy

			// TODO G11 数据标识由short转为hexString，方便显示
			try {
				String dataDiHex = HexStringUtils
						.bytesToHexString(ByteUtil.getBytes(Short.parseShort(meter.getDataDi())));
				meterMap.put("dataDi", dataDiHex);
			} catch (Exception e) {
				// TODO: handle exception
			}

			meterMapList.add(meterMap);
		}

		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		return TEMPLATE_PATH + "table";

	}
	
	
}