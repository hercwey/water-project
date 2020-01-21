package com.learnbind.ai.controller.importexcel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumLocalNodeType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.excelutil.Common;
import com.learnbind.ai.common.util.excelutil.ReadAppMeterRecordExcel;
import com.learnbind.ai.common.util.excelutil.ReadCustomerExcel;
import com.learnbind.ai.common.util.excelutil.ReadLocationExcel;
import com.learnbind.ai.common.util.excelutil.ReadMeterExcel;
import com.learnbind.ai.common.util.excelutil.ReadMultiMeterExcel;
import com.learnbind.ai.common.util.excelutil.ReadPastOweDataExcel;
import com.learnbind.ai.common.util.excelutil.ReadUpdateDataExcel;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.MeterRecordTemp;
import com.learnbind.ai.service.importexcel.ImportExcelService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.importexcel
 *
 * @Title: ImportExcelController.java
 * @Description: 导入Excel控制器
 *
 * @author Administrator
 * @date 2019年6月28日 上午9:36:21
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/import-excel")
public class ImportExcelController {
	private static Log log = LogFactory.getLog(ImportExcelController.class);
	private static final String TEMPLATE_PATH = "import_excel/"; // 页面目
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private ImportExcelService importExcelService;//导入Excel服务
	@Autowired
	private MeterRecordTempService meterRecordTempService;//APP抄表记录
	@Autowired
	private MeterRecordService meterRecordService;//抄表记录
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "import_excel_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		//查询地理位置-小区
		Location location = new Location();
		location.setLocalNodeType(EnumLocalNodeType.LOCAL_NODE_TYPE_BLOCK.getCode());
		location.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<Location> locationList = locationService.select(location);
		
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "import_excel_main";
	}

	//------------------------------	导入地理位置信息	------------------------------
	/**
	 * @Title: importExcelLocation
	 * @Description: 导入地理位置excel
	 * @param request
	 * @param model
	 * @param locationId
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-excel-location")
	@ResponseBody
	public Object importExcelLocation(HttpServletRequest request, Model model, Long locationId, String fileType, String inputName) {
		
		try {
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put(Common.EXCEL_PATH, filePath);
				Map<String, Object> readResult = ReadLocationExcel.readExcel(reqMap);
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<String> locationStrList = JSON.parseArray(resultDataJSON, String.class);
					int rows = importExcelService.parseLocationExcelDataToDb(locationId, locationStrList);
					if(rows>0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("导入地理位置数据成功！"));
					}
				}else {
					System.out.println("result code fail");
					return JSON.toJSONString(readResult);
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传Excel文件失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultFail("导入地理位置数据失败！请检查Excel件后重新导入！"));
	}
	
	//------------------------------	导入表计Excel信息	------------------------------
	/**
	 * @Title: importExcelMeter
	 * @Description: 导入表计Excel
	 * @param request
	 * @param model
	 * @param locationBlockCode
	 * @param meterId
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-excel-meter")
	@ResponseBody
	public Object importExcelMeter(HttpServletRequest request, Model model, String locationBlockCode, Long meterId, String fileType, String inputName) {
		
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put(Common.EXCEL_PATH, filePath);
				Map<String, Object> readResult = ReadMeterExcel.readExcel(reqMap);
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<Map> meterList = JSON.parseArray(resultDataJSON, Map.class);
					int rows = importExcelService.parseMeterExcelDataToDb(locationBlockCode, meterId, meterList);
					if(rows>0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("导入表计数据成功！"));
					}
				}else {
					System.out.println("result code fail");
					return JSON.toJSONString(readResult);
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传Excel文件失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultFail("导入表计数据失败！请检查Excel件后重新导入！"));
	}
	
	//------------------------------	导入客户Excel信息	------------------------------
	/**
	 * @Title: importExcelMeter
	 * @Description: 导入客户Excel
	 * @param request
	 * @param model
	 * @param locationBlockCode
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-excel-customer")
	@ResponseBody
	public Object importExcelCustomer(HttpServletRequest request, Model model, String locationBlockCode, String fileType, String inputName) {
		
		try {
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put(Common.EXCEL_PATH, filePath);
				Map<String, Object> readResult = ReadCustomerExcel.readExcel(reqMap);
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<Map> customerList = JSON.parseArray(resultDataJSON, Map.class);
					int rows = importExcelService.parseCustomerExcelDataToDb(locationBlockCode, customerList);
					if(rows>0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("导入客户数据成功！"));
					}
				}else {
					System.out.println("result code fail");
					return JSON.toJSONString(readResult);
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传Excel文件失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultFail("导入客户数据失败！请检查Excel件后重新导入！"));
	}
	
	//------------------------------	导入更新表计读数Excel信息	------------------------------
	/**
	 * @Title: importExcelMeter
	 * @Description: 导入更新表计表底Excel
	 * @param request
	 * @param model
	 * @param locationBlockCode
	 * @param meterId
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-excel-update-data")
	@ResponseBody
	public Object importExcelMeterRecord(HttpServletRequest request, Model model, String traceIds, String fileType, String inputName) {
		
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put(Common.EXCEL_PATH, filePath);
				Map<String, Object> readResult = ReadUpdateDataExcel.readExcel(reqMap);
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<Map> updateDataList = JSON.parseArray(resultDataJSON, Map.class);
					int rows = importExcelService.parseUpdateDataExcelDataToDb(traceIds, updateDataList);
					if(rows>0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("导入更新数据成功！"));
					}
				}else {
					System.out.println("result code fail");
					return JSON.toJSONString(readResult);
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传Excel文件失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultFail("导入更新数据失败！请检查Excel件后重新导入！"));
	}
	
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getLinuxUploadFolder();
		if(this.isWindowsOS()) {
			path = uploadFileConfig.getWindowsUploadFolder();
		}
		System.out.println("----------"+path);
		return path;
	}
	
	/**
	 * @Title: isWindowsOS
	 * @Description: 判断操作系统是否是Windows
	 * @return 
	 * 		true=windows;false=其他
	 */
	private boolean isWindowsOS() {
		String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
    	if(os.toUpperCase().startsWith("WIN")){  
    		System.out.println("==============================操作系统："+os);
    		return true;
    	}else {
    		System.out.println("==============================操作系统："+os);
    		return false;
    	}
	}
	
	//---------------------导入一户多表表计信息-------------------------------------------------------------------------------------------------------------
	
	@RequestMapping(value = "/import-excle-multi-meter")
	@ResponseBody
	public Object importExcelMultiMeter(HttpServletRequest request, Model model, String locationBlockCode, Long meterId, String fileType, String inputName) {
		
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put(Common.EXCEL_PATH, filePath);
				Map<String, Object> readResult = ReadMultiMeterExcel.readExcel(reqMap);
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<Map> meterList = JSON.parseArray(resultDataJSON, Map.class);
					int rows = importExcelService.parseMultiMeterExcelDataToDb(locationBlockCode, meterId, meterList);
					if(rows>0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("导入一表多户表计数据成功！"));
					}
				}else {
					System.out.println("result code fail");
					return JSON.toJSONString(readResult);
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传Excel文件失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultFail("导入一表多户表计数据失败！请检查Excel件后重新导入！"));
	}
	
	//=================测试部分==========================================================================
	
	/**
	 * @Title: importAppMeterRecord
	 * @Description: 导入APP抄表记录
	 * @param request
	 * @param model
	 * @param traceIds
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-app-meter-record")
	@ResponseBody
	public Object importAppMeterRecord(HttpServletRequest request, Model model, String traceIds, String fileType, String inputName) {
		
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put(Common.EXCEL_PATH, filePath);
				Map<String, Object> readResult = ReadAppMeterRecordExcel.readExcel(reqMap);
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<Map> updateDataList = JSON.parseArray(resultDataJSON, Map.class);
					int rows = importExcelService.parseAppMeterRecordToDb(traceIds, updateDataList);
					if(rows>0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("导入更新数据成功！"));
					}
				}else {
					System.out.println("result code fail");
					return JSON.toJSONString(readResult);
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传Excel文件失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultFail("导入更新数据失败！请检查Excel件后重新导入！"));
	}
	
	/**
	 * @Title: importPastOweData
	 * @Description: 导入往期欠费
	 * @param request
	 * @param model
	 * @param traceIds
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-past-owe-data")
	@ResponseBody
	public Object importPastOweData(HttpServletRequest request, Model model, String traceIds, String fileType, String inputName) {
		
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put(Common.EXCEL_PATH, filePath);
				Map<String, Object> readResult = ReadPastOweDataExcel.readExcel(reqMap);//导入往期欠费
				//Map<String, Object> readResult = ReadPrepaymentDataExcel.readExcel(reqMap);//导入预付金额
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<Map> dataMapList = JSON.parseArray(resultDataJSON, Map.class);
					if(dataMapList==null || dataMapList.size()<=0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("未读取到需要导入的数据！"));
					}
					int rows = importExcelService.parsePastOweDataToDb(traceIds, dataMapList);//导入往期欠费
					//int rows = importExcelService.parsePrepaymentDataToDb(traceIds, dataMapList);//导入预付金额
					if(rows>0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("导入往期欠费数据成功！"));
					}
				}else {
					System.out.println("result code fail");
					return JSON.toJSONString(readResult);
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传Excel文件失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultFail("导入往期欠费数据失败！请检查Excel件后重新导入！"));
	}
	
	/**
	 * @Title: importUpdateCustomer
	 * @Description: 导入更新客户信息
	 * @param request
	 * @param model
	 * @param traceIds
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-update-customer")
	@ResponseBody
	public Object importUpdateCustomer(HttpServletRequest request, Model model, String traceIds, String fileType, String inputName) {
		
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put(Common.EXCEL_PATH, filePath);
				Map<String, Object> readResult = ReadAppMeterRecordExcel.readExcel(reqMap);
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<Map> updateDataList = JSON.parseArray(resultDataJSON, Map.class);
					int rows = importExcelService.parseAppMeterRecordToDb(traceIds, updateDataList);
					if(rows>0) {
						return JSON.toJSONString(RequestResultUtil.getResultSuccess("导入更新数据成功！"));
					}
				}else {
					System.out.println("result code fail");
					return JSON.toJSONString(readResult);
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传Excel文件失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultFail("导入更新数据失败！请检查Excel件后重新导入！"));
	}
	
	
	//-----------------更新APP抄表记录上期表底，更新后删除--------------------------------------------------------------------------------------------
	
	@RequestMapping(value = "/update-app-record")
	@ResponseBody
	public Object appMeterRecord(HttpServletRequest request, Model model) {
		
		try {
			
			List<MeterRecordTemp> recrodTempList = meterRecordTempService.selectAll();
			for(MeterRecordTemp recordTemp : recrodTempList) {
				Long appRecordId = recordTemp.getId();
				Long meterId = recordTemp.getMeterId();
				String currRead = recordTemp.getCurrRead();
				
				if(StringUtils.isBlank(currRead)) {
					continue;
				}
				
				//上期记录
				MeterRecord searchObj = new MeterRecord();
				searchObj.setMeterId(meterId);
				MeterRecord result = meterRecordService.selectOne(searchObj);
				
				String preRead = result.getCurrRead();
				BigDecimal currAmount = BigDecimalUtils.subtract(new BigDecimal(currRead), new BigDecimal(preRead));
				
				MeterRecordTemp updateObj = new MeterRecordTemp();
				updateObj.setId(appRecordId);
				updateObj.setPreRead(preRead);
				updateObj.setCurrAmount(currAmount);
				meterRecordTempService.updateByPrimaryKeySelective(updateObj);
				
			}
			
			return RequestResultUtil.getResultSuccess("更新成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("更新失败！");
	}

}