package com.learnbind.ai.controller.engineering;

import java.io.File;
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
import com.learnbind.ai.model.EngineeringDoc;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.engineering.EngineeringDocService;

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
@RequestMapping(value = "/engineering-doc")
public class EngineeringDocController {
	
	private static Log log = LogFactory.getLog(EngineeringDocController.class);
	
	private static final String TEMPLATE_PATH = "engineering/doc/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	@Autowired
	private EngineeringDocService engineeringDocService;//工程单据
	

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
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
	public String table(Model model, Integer pageNum, Integer pageSize, Long engineeringId, String searchCond) {

		Long operatorId = this.getOperatorId();//操作员ID
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Example example = new Example(EngineeringDoc.class);
		Criteria criteria = example.createCriteria();
//		if(operatorId!=null) {
//			criteria.andEqualTo("operatorId", operatorId);
//		}
		criteria.andEqualTo("engineeringId", engineeringId);
		if(StringUtils.isNotBlank(searchCond)) {
			criteria.andLike("docComment", "%"+searchCond+"%");
		}
		List<EngineeringDoc> engineeringDocList = engineeringDocService.selectByExample(example);
		PageInfo<EngineeringDoc> pageInfo = new PageInfo<>(engineeringDocList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("engineeringDocList", engineeringDocList);//列表数据
		
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
		
		EngineeringDoc engineeringDoc = null;
		if(itemId!=null) {
			engineeringDoc = engineeringDocService.selectByPrimaryKey(itemId);
		}
		model.addAttribute("currItem", engineeringDoc);
		
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
	public Object insert(HttpServletRequest request, EngineeringDoc engineeringDoc, String fileType, String inputName) {
		
		//根据操作系统类型获取上传文件目录
		String path = this.getPath();
		
		//上传工程单据文件
		List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
		if(filePathList!=null && filePathList.size()>0) {
			String filePath = filePathList.get(0);
			System.out.println("上传图片路径："+filePath);
			filePath = FileUploadUtil.subImgPath(filePathList.get(0));
			System.out.println("截取后上传图片路径："+filePath);
			engineeringDoc.setDocPath(filePath);
		}
		
		int row = engineeringDocService.insertSelective(engineeringDoc);
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
	public Object update(HttpServletRequest request, EngineeringDoc engineeringDoc, String fileType, String inputName) throws Exception {
		
		//根据操作系统类型获取上传文件目录
		String path = this.getPath();
		
		//上传工程单据文件
		List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
		if(filePathList!=null && filePathList.size()>0) {
			String filePath = filePathList.get(0);
			System.out.println("上传图片路径："+filePath);
			filePath = FileUploadUtil.subImgPath(filePathList.get(0));
			System.out.println("截取后上传图片路径："+filePath);
			engineeringDoc.setDocPath(filePath);
		}
		
		engineeringDocService.updateByPrimaryKeySelective(engineeringDoc);
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
				engineeringDocService.deleteByPrimaryKey(id);
				//植入的BUG,用于测试
				//long x=1/0;  
			}
		}catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 
	 * 		为null时是管理员角色，查询所有；不为null时是工程公司角色，只查询此工程公司创建的安装工程
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;//操作员ID
		if (userBean!=null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for(SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}
			
			//indexOf() 返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
			if(roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_ADMIN)==-1) {
				operatorId = userBean.getId();//操作员ID
			}
			
		}
		return operatorId;
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