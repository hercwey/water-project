package com.learnbind.ai.controller.meters;

import java.util.List;

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
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.model.MeterERecord;
import com.learnbind.ai.service.meters.MeterERecordService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterERcordController.java
 * @Description: 表计-电子档案
 *
 * @author Administrator
 * @date 2019年8月10日 上午9:32:22
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-erecord")
public class MeterERcordController {
	
	private static Log log = LogFactory.getLog(MeterERcordController.class);
	private static final String TEMPLATE_PATH = "meters/electronic_record/";//页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private MeterERecordService meterERecordService;//表计-电子档案服务
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息

	
	/*------------------------------	表计-电子档案	------------------------------*/
	
	/**
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param model
	 * @param functionModule
	 * @return 
	 */
	@RequestMapping(value = "/erecord-main")
	public String main(Model model) {
		return TEMPLATE_PATH + "erecord_main";
	}
	
	/**
	 * @Title: loadERecordItem
	 * @Description: 加载电子档案列表
	 * @param model
	 * @param functionModule
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/load-erecord-item")
	public String loadERecordItem(Model model, Long meterId) {
		
		MeterERecord record = new MeterERecord();
		record.setMeterId(meterId);
		List<MeterERecord> erecordList = meterERecordService.select(record);
		
		model.addAttribute("erecordList", erecordList);
		
		return TEMPLATE_PATH + "erecord_item_table";
	}
	
	/**
	 * @Title: loadERecordDialog
	 * @Description: 加载电子档案上传对话框
	 * @param model
	 * @param functionModule
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-erecord-dialog")
	public String loadERecordDialog(Model model, String functionModule, Long customerId) {
		
		return TEMPLATE_PATH + "erecord_dialog_edit";
	}

	/**
	 * @Title: upload
	 * @Description: 上传
	 * @param request
	 * @param model
	 * @param fileDir
	 * @param inputName
	 * @param erecord
	 * @return 
	 */
	@RequestMapping(value = "/erecord-upload")
	@ResponseBody
	public Object upload(HttpServletRequest request, Model model, String fileDir, String inputName, MeterERecord erecord) {
		int rows = 0;
		try {
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			
			//上传协议文件
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileDir, inputName);
			if(filePathList!=null && filePathList.size()>0) {
				for(String filePath : filePathList) {
					System.out.println("上传图片路径："+filePath);
					filePath = FileUploadUtil.subImgPath(filePath);
					System.out.println("截取后上传图片路径："+filePath);
					//设置主键为空
					erecord.setId(null);
					erecord.setErecordPath(filePath);
					rows = meterERecordService.insertSelective(erecord);
				}
				
			}
			
			
			if(rows>0) {
				return JSON.toJSONString(RequestResultUtil.getResultUploadSuccess());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultUploadWarn());
	}
	
	/**
	 * @Title: deleteERecord
	 * @Description: 删除表计-电子档案关系
	 * @param request
	 * @param model
	 * @param id
	 * @param erecordPath
	 * @return 
	 */
	@RequestMapping(value = "/delete-erecord")
	@ResponseBody
	public Object deleteERecord(HttpServletRequest request, Model model, Long id, String erecordPath) {
		
		try {
			//TODO 删除电子档案文件
			//删除客户-电子档案关系
			int rows = meterERecordService.deleteByPrimaryKey(id);
			if(rows>0) {
				return RequestResultUtil.getResultDeleteSuccess();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultDeleteWarn();
	}
	
	
	//------------------------------	上传文件通用方法	------------------------------
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
	
	public static void main(String[] args) {
		String path = "d:/upload/img/20190701/abc.jpg";
		//String path = "/home/upload/img/20190701/abc.jpg";
		path = path.substring(path.indexOf("upload"));
		System.out.println("windows:"+path);
		path = "/home/upload/img/20190701/abc.jpg";
		path = path.substring(path.indexOf("upload"));
		System.out.println("linux:"+path);
	}
	
}