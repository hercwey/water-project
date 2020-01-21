package com.learnbind.ai.controller.importexcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.learnbind.ai.common.util.excelutil.Common;
import com.learnbind.ai.common.util.excelutil.ReadBigMeterExcel;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.service.importexcel.ImportBigMeterExcelService;
import com.learnbind.ai.service.importexcel.ImportExcelService;
import com.learnbind.ai.service.location.LocationService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.importexcel
 *
 * @Title: ImportBigMeterExcelController.java
 * @Description: 导入大表信息
 *
 * @author Administrator
 * @date 2019年9月11日 下午5:41:57
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/import-bigmeter-excel")
public class ImportBigMeterExcelController {
	private static Log log = LogFactory.getLog(ImportBigMeterExcelController.class);
	private static final String TEMPLATE_PATH = "import_bigmeter_excel/"; // 页面目
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private ImportExcelService importExcelService;//导入Excel服务
	@Autowired
	private ImportBigMeterExcelService importBigMeterExcelService;//导入大表Excel服务
	
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

	//------------------------------	导入大表数据	------------------------------
	/**
	 * @Title: importExcelMeter
	 * @Description: 导入大表数据
	 * @param request
	 * @param model
	 * @param traceIds
	 * @param meterId
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-excel")
	@ResponseBody
	public Object importBigMeterExcelMeter(HttpServletRequest request, Model model, String traceIds, Long meterId, String fileType, String inputName) {
		
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
				Map<String, Object> readResult = ReadBigMeterExcel.readExcel(reqMap);
				String result_code = readResult.get("result_code").toString();
				if(result_code.equalsIgnoreCase("success")) {
					String resultDataJSON = readResult.get("result_data").toString();
					List<Map> meterList = JSON.parseArray(resultDataJSON, Map.class);
					int rows = importBigMeterExcelService.parseBigMeterExcelDataToDb(traceIds, meterList);
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

}