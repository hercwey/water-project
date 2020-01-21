package com.learnbind.ai.controller.meters;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CheckResultErecord;
import com.learnbind.ai.model.EngineeringDoc;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.meters.CheckResultDocService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.engineering
 *
 * @Title: EngineeringController.java
 * @Description: 工程单据控制器
 *
 * @author Administrator
 * @date 2019年8月4日 上午9:16:46
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/check-result-doc")
public class CheckResultDocController {
	
	private static Log log = LogFactory.getLog(CheckResultDocController.class);
	
	private static final String TEMPLATE_PATH = "meter_stock/meter_deduct/doc/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	@Autowired
	private CheckResultDocService checkResultDocService;//检测结果电子档
	

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model, Integer eRecordType) {
		model.addAttribute("eRecordType", eRecordType);
		return TEMPLATE_PATH + "doc_main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, Long checkResultId, String searchCond, Integer eRecordType) {

		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Example example = new Example(CheckResultErecord.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("checkResultId", checkResultId);
		criteria.andEqualTo("erecordType", eRecordType);
		if(StringUtils.isNotBlank(searchCond)) {
			criteria.andLike("description", "%"+searchCond+"%");
		}
		List<CheckResultErecord> docList = checkResultDocService.selectByExample(example);
		PageInfo<CheckResultErecord> pageInfo = new PageInfo<>(docList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("docList", docList);//列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "doc_table";
	}

	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, Long itemId) {
		
		CheckResultErecord record = null;
		if(itemId!=null) {
			record = checkResultDocService.selectByPrimaryKey(itemId);
		}
		model.addAttribute("currItem", record);
		
		return TEMPLATE_PATH + "doc_dialog_edit";
	}
	
	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param request
	 * @param engineeringDoc
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/insert")
	@ResponseBody
	public String insert(HttpServletRequest request, CheckResultErecord record, String fileType, String inputName) {
		
		//根据操作系统类型获取上传文件目录
		String path = this.getPath();
		
		//上传工程单据文件
		List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
		if(filePathList!=null && filePathList.size()>0) {
			String filePath = filePathList.get(0);
			System.out.println("上传图片路径："+filePath);
			filePath = FileUploadUtil.subImgPath(filePathList.get(0));
			System.out.println("截取后上传图片路径："+filePath);
			record.setFilePath(filePath);
		}
		
		int row = checkResultDocService.insertSelective(record);
		if (row > 0) {
			return JSON.toJSONString(RequestResultUtil.getResultAddSuccess());
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultAddWarn());
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param request
	 * @param engineeringDoc
	 * @param fileType
	 * @param inputName
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(HttpServletRequest request, CheckResultErecord record, String fileType, String inputName) throws Exception {
		
		//根据操作系统类型获取上传文件目录
		String path = this.getPath();
		
		//上传工程单据文件
		List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
		if(filePathList!=null && filePathList.size()>0) {
			String filePath = filePathList.get(0);
			System.out.println("上传图片路径："+filePath);
			filePath = FileUploadUtil.subImgPath(filePathList.get(0));
			System.out.println("截取后上传图片路径："+filePath);
			record.setFilePath(filePath);
		}
		
		checkResultDocService.updateByPrimaryKeySelective(record);
		return JSON.toJSONString(RequestResultUtil.getResultUpdateSuccess());
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				//System.out.println(id);
				checkResultDocService.deleteByPrimaryKey(id);
				//植入的BUG,用于测试
				//long x=1/0;  
			}
		}catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

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
	
}