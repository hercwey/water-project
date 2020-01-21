package com.learnbind.ai.controller.location;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.location
 *
 * @Title: LocationMeterController.java
 * @Description: 地理位置-表计前端控制器
 *
 * @author Administrator
 * @date 2019年5月21日 下午12:14:20
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/location-meter")
public class LocationMeterController {
	
	private static Log log = LogFactory.getLog(LocationMeterController.class);
	
	private static final String TEMPLATE_PATH = "location/location_meter/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private DataDictService dataDictService;//数据字典服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private MetersService metersService;  //客户档案
	@Autowired
	private LocationMeterService locationMeterService;//地理位置与表计关系服务

	/**
	 * @Title: starter
	 * @Description: 起始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "location_meter_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		//查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("locaNodeType", dictList);
		//加载数据字典-水表口径、水表类型、水用途、抄表方式
		List<DataDict> waterCaliberList = dataDictService.getListByTypeCode(DataDictType.WARTER_CALIBER);
		List<DataDict> meterTypeList = dataDictService.getListByTypeCode(DataDictType.METER_TYPE);
		List<DataDict> meterUseList = dataDictService.getListByTypeCode(DataDictType.METER_USE);
		List<DataDict> readMoedeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		//List<DataDict> meterStatusList = dataDictService.getListByTypeCode(DataDictType.METER_STATUS);
		model.addAttribute("waterCaliberList", waterCaliberList);
		model.addAttribute("meterTypeList", meterTypeList);
		model.addAttribute("meterUseList", meterUseList);
		model.addAttribute("readMoedeList", readMoedeList);
		
//		Example example = new Example(Location.class);
//		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
//		example.setOrderByClause("location_level ASC,sort_value ASC,pid ASC,id ASC");
//		List<Location> locationList = locationService.selectByExample(example);
//		
//		//String locationListJson = JSON.toJSONString(locationList);
//		model.addAttribute("locationListJson", locationList);
		
		return TEMPLATE_PATH + "location_meter_main";
	}
	
	/**
	 * @Title: locationTable
	 * @Description: 加载地理位置
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/location-table")
	public String locationTable(Model model) {

		//查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("dictList", dictList);
		
//		Example example = new Example(Location.class);
//		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
//		example.setOrderByClause(" LOCATION_LEVEL ASC,SORT_VALUE ASC,PID ASC,CODE ASC, NAME ASC, ID ASC");
//		List<Location> locationList = locationService.selectByExample(example);
//		
//		//String locationListJson = JSON.toJSONString(locationList);
//		model.addAttribute("locationListJson", locationList);
//		
//		List<Location> resultList = new ArrayList<>();//保存排序结果
//		resultList.clear();
//		for(int i=0; i<locationList.size(); i++){
//			Location location = locationList.get(i);
//			if(location.getPid()==0){
//				resultList.add(location);
//				locationListSort(resultList, locationList,location.getId());
//			}
//		}
//		model.addAttribute("locationList", resultList);
		
		return TEMPLATE_PATH + "location_table";
	}
	
	/**
	 * @Title: metersMain
	 * @Description: 表计主页
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/meter-main")
	public String metersMain(Model model) {
		
		//查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("locaNodeType", dictList);
		//加载数据字典-水表口径、水表类型、水用途、抄表方式
		List<DataDict> waterCaliberList = dataDictService.getListByTypeCode(DataDictType.WARTER_CALIBER);
		List<DataDict> meterTypeList = dataDictService.getListByTypeCode(DataDictType.METER_TYPE);
		List<DataDict> meterUseList = dataDictService.getListByTypeCode(DataDictType.METER_USE);
		List<DataDict> readMoedeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		//List<DataDict> meterStatusList = dataDictService.getListByTypeCode(DataDictType.METER_STATUS);
		model.addAttribute("waterCaliberList", waterCaliberList);
		model.addAttribute("meterTypeList", meterTypeList);
		model.addAttribute("meterUseList", meterUseList);
		model.addAttribute("readMoedeList", readMoedeList);
		
		return TEMPLATE_PATH + "meters_main";
	}
	
	/** 
	*	@Title: metersTable 
	*	@Description: 加载用户列表(列表页面)
	*	@param @param model ModelView中传递数据的对象
	*	@param @param pageNum 页号
	*	@param @param pageSize 页大小
	*	@param @param searchCond 查询条件
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/meter-table")
	public String metersTable(Model model) {

//		Example example = new Example(Meters.class);
//		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
//		example.setOrderByClause("pid ASC,id ASC");
//		List<Meters> meterList = metersService.selectByExample(example);
//		
//		//String meterListJson = JSON.toJSONString(meterList);
//		model.addAttribute("meterListJson", meterList);
//		
//		List<Meters> resultList = new ArrayList<>();//保存排序结果
//		resultList.clear();
//		for(int i=0; i<meterList.size(); i++){
//			Meters meter = meterList.get(i);
//			if(meter.getPid()==0){
//				resultList.add(meter);
//				meterListSort(resultList, meterList,meter.getId());
//			}
//		}
//		model.addAttribute("meterList", resultList);
		
		return TEMPLATE_PATH + "meters_table";
	}
	
	/**
	 * @Title: loadLocationDetailDialog
	 * @Description: 加载地理位置详情对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-location-detail-dialog")
	public String loadLocationDetailDialog(Model model) {
		
		//查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("dictList", dictList);
		
		Example example = new Example(Location.class);
		example.setOrderByClause("location_level ASC,sort_value ASC,pid ASC,id ASC");
		List<Location> locationList = locationService.selectByExample(example);
		
		//String locationListJson = JSON.toJSONString(locationList);
		//model.addAttribute("locationListJson", locationList);
		
		List<Location> resultList = new ArrayList<>();//保存排序结果
		resultList.clear();
		for(int i=0; i<locationList.size(); i++){
			Location location = locationList.get(i);
			if(location.getPid()==0){
				resultList.add(location);
				locationListSort(resultList, locationList,location.getId());
			}
		}
		model.addAttribute("locationList", resultList);
		
		return TEMPLATE_PATH + "location_dialog_edit";
	}
	
	/**
	 * @Title: loadMeterDetailDialog
	 * @Description: 加载表计详情对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-meter-detail-dialog")
	public String loadMeterDetailDialog(Model model) {
		//加载数据字典-水表口径、水表类型、水用途、抄表方式
		List<DataDict> waterCaliberList = dataDictService.getListByTypeCode(DataDictType.WARTER_CALIBER);
		List<DataDict> meterTypeList = dataDictService.getListByTypeCode(DataDictType.METER_TYPE);
		List<DataDict> meterUseList = dataDictService.getListByTypeCode(DataDictType.METER_USE);
		List<DataDict> readMoedeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		
						
		model.addAttribute("waterCaliberList", waterCaliberList);
		model.addAttribute("meterTypeList", meterTypeList);
		model.addAttribute("meterUseList", meterUseList);
		model.addAttribute("readMoedeList", readMoedeList);
		return TEMPLATE_PATH + "meters_dialog_edit";
	}

	/**
	 * @Title: locationListSort
	 * @Description: 对地理位置集合排序
	 * @param resultList
	 * @param locationList
	 * @param id 
	 */
	private void locationListSort(List<Location> resultList, List<Location> locationList,Long id) {
		
		for(int i=0; i<locationList.size(); i++){
			Location right = locationList.get(i);
        	if(right.getPid().equals(id)){
                resultList.add(right);
                locationListSort(resultList, locationList,right.getId());
            }
        }
    }
	
	/**
	 * @Title: meterListSort
	 * @Description: 对表计集合排序
	 * @param resultList
	 * @param meterList
	 * @param id 
	 */
	private void meterListSort(List<Meters> resultList, List<Meters> meterList,Long id) {
		
		for(int i=0; i<meterList.size(); i++){
			Meters meter = meterList.get(i);
        	if(meter.getPid().equals(id)){
                resultList.add(meter);
                meterListSort(resultList, meterList,meter.getId());
            }
        }
    }
	
	/**
	 * @Title: bind
	 * @Description: 建立地理位置与表计的绑定关系
	 * @param request
	 * @param locationId
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/bind", produces = "application/json")
	@ResponseBody
	public Object bind(HttpServletRequest request, Long locationId, Long meterId){
		
		Location location = locationService.selectByPrimaryKey(locationId);
		Meters meter = metersService.selectByPrimaryKey(meterId);

		boolean  isBinding= locationMeterService.isBinding(locationId, meterId);
		if(isBinding) {
			//return RequestResultUtil.getResultSaveWarn("【"+location.getName()+"】与【"+meter.getSteelSealNo()+"】已绑定！");
			return RequestResultUtil.getResultSaveWarn("【"+meter.getSteelSealNo()+"】已绑定！");
		}
		
		int row = locationMeterService.bind(locationId, location.getTraceIds(), meterId);
		if (row>0){
			return RequestResultUtil.getResultSaveSuccess("【"+location.getName()+"】与【"+meter.getSteelSealNo()+"】绑定成功！");
		}else{
			return RequestResultUtil.getResultSaveWarn("【"+location.getName()+"】与【"+meter.getSteelSealNo()+"】绑定失败！请重新操作！");
		}
	}
	/**
	 * @Title: unbind
	 * @Description: 解除地理位置与表计的绑定关系
	 * @param request
	 * @param locationId
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/unbind", produces = "application/json")
	@ResponseBody
	public Object unbind(HttpServletRequest request, Long locationId, Long meterId){
		
		Location location = locationService.selectByPrimaryKey(locationId);
		Meters meter = metersService.selectByPrimaryKey(meterId);
		
		boolean  isBinding= locationMeterService.isBinding(locationId, meterId);
		if(!isBinding) {
			return RequestResultUtil.getResultSaveWarn("【"+location.getName()+"】与【"+meter.getSteelSealNo()+"】未绑定！");
		}
		
		int row = locationMeterService.unbind(locationId, meterId);
		if (row>0){
			return RequestResultUtil.getResultSaveSuccess("【"+location.getName()+"】与【"+meter.getSteelSealNo()+"】解绑成功！");
		}else{
			return RequestResultUtil.getResultSaveWarn("【"+location.getName()+"】与【"+meter.getSteelSealNo()+"】解绑失败！请重新操作！");
		}
	}

}