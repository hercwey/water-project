package com.learnbind.ai.controller.knowlibrary;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.KnowLibrary;
import com.learnbind.ai.model.MeterRecordPhoto;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.knowlibrary.KnowLibraryService;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.knowlibrary
 *
 * @Title: KnowLibraryController.java
 * @Description: 知识库管理
 *
 * @author Thinkpad
 * @date 2019年7月27日 上午9:16:44
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/know-content")
public class KnowLibraryContentController {
	private static Log log = LogFactory.getLog(KnowLibraryContentController.class);
	private static final String TEMPLATE_PATH = "know_library/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private KnowLibraryService knowLibraryService;  //知识库管理
	@Autowired
	private DataDictService dataDictService;
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	

	/** 
	*	@Title: roleStarter 
	*	@Description: 起始页面
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/starter")
	public String starter(Model model, String knowType) {
		String title = "";
		if(StringUtils.equals(knowType, "BUSINESS_PROCESS")) {
			title = "业务流程";
		} else if (StringUtils.equals(knowType, "BUSINESS_OUTLETS")) {
			title = "营业网点";
		} else if (StringUtils.equals(knowType, "WATER_QUALITY_REPORT")) {
			title = "水质报告";
		} else if (StringUtils.equals(knowType, "WATER_KNOWLEDGE")) {
			title = "用水常识";
		} else if (StringUtils.equals(knowType, "NOTIFY_INFORMATION")) {
			title = "通知信息";
		} else if (StringUtils.equals(knowType, "KNOW_LIBRARY_MANAGE")) {
			title = "知识库管理";
		} else if (StringUtils.equals(knowType, "KNOW_LIBRARY_SELECT")) {
			title = "知识库查询";
		}
		model.addAttribute("knowType", knowType);
		model.addAttribute("title", title);
		return TEMPLATE_PATH + "know_starter";
	}

	/**
	 * @Title: search
	 * @Description: 主界面:控制面板及列表容器 
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String search(Model model, String knowType, String title) {
		model.addAttribute("knowType", knowType);
		model.addAttribute("title", title);
		return TEMPLATE_PATH + "know_main";
	}

	/**
	 * @Title: table
	 * @Description: 列表界面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, String knowType, String label) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<KnowLibrary> knowList = new ArrayList<>();
		if(StringUtils.equals(knowType, "KNOW_LIBRARY_SELECT")) {
			knowList = knowLibraryService.searchAllKnowLibrary(searchCond, label);
		} else {
			knowList = knowLibraryService.searchKnowLibrary(searchCond, knowType);
		}
		 
		PageInfo<List<KnowLibrary>> pageInfo = new PageInfo(knowList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> knowMapList = new ArrayList<>();
		for(KnowLibrary knowLibrary : knowList) {
			Map<String, Object> knowMap = EntityUtils.entityToMap(knowLibrary);
			knowMap.put("publicDateStr", knowLibrary.getPublicDateStr());//发布日期
			knowMap.put("knowTypeStr", this.getDataDictValue(EnumDataDictType.KNOW_LIBRARY_TYPE.getCode(), knowLibrary.getType()));//知识库类型类型
			
			knowMapList.add(knowMap);
		}

		// 传递如下数据至前台页面
		model.addAttribute("knowList", knowMapList);  //列表数据
		model.addAttribute("knowType", knowType);  //列表数据
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "know_table";
	}
	

	/**
	 * @Title: loadAddItem
	 * @Description: 新增界面选项卡
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-add-item")
	public String loadAddItem(Model model, String knowType) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String author = userBean.getRealname();
		model.addAttribute("author",author);
 		model.addAttribute("knowType",knowType);
		
		//加载数据字典-知识库标签
		List<DataDict> labelList = dataDictService.getListByTypeCode(DataDictType.KNOW_LIBRARY_LABELS);
		model.addAttribute("labelList",labelList);
		return TEMPLATE_PATH + "know_item_table";
	}
	
	/**
	 * @Title: addKnowLibrary
	 * @Description: 增加知识库
	 * @param knowLibrary
	 * @return 
	 */
	@RequestMapping(value = "/insert")
	@ResponseBody
	public Object addKnowLibrary(HttpServletRequest request, KnowLibrary knowLibrary, String fileType, String inputName) {
		//根据操作系统类型获取上传文件目录
		String path = this.getPath();
		//上传图片
		List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
		String filePath = "";
		if(filePathList!=null && filePathList.size()>0) {
			filePath = filePathList.get(0);
			System.out.println("上传图片路径："+filePath);
			filePath = FileUploadUtil.subImgPath(filePathList.get(0));
			System.out.println("截取后上传图片路径："+filePath);
		}
		
		knowLibrary.setPublicDate(new Date());
		knowLibrary.setImgPath(filePath);
		int row = knowLibraryService.insertSelective(knowLibrary);
		if (row > 0)
			return JSON.toJSONString(RequestResultUtil.getResultUploadSuccess());
		else
			return JSON.toJSONString(RequestResultUtil.getResultUploadWarn());
	}

	/**
	 * @Title: deleteKnowLibrary
	 * @Description: 删除
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteKnowLibrary(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				knowLibraryService.deleteByPrimaryKey(id);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/**
	 * @Title: loadModiKnowItem
	 * @Description:编辑知识库
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-modi-item")
	public String loadModiKnowItem(Long itemId, String knowType,  Model model) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String author = userBean.getRealname();
		model.addAttribute("author",author);
		//读取需要编辑的条目
		KnowLibrary currItem = knowLibraryService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		model.addAttribute("knowType",knowType);
		//加载数据字典-知识库标签
		List<DataDict> labelList = dataDictService.getListByTypeCode(DataDictType.KNOW_LIBRARY_LABELS);
		model.addAttribute("labelList",labelList);
		
		return TEMPLATE_PATH + "know_item_table";
	}

	/**
	 * @Title: updateKnowLibrary
	 * @Description: 更新知识库
	 * @param knowLibrary
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object updateKnowLibrary(HttpServletRequest request, KnowLibrary knowLibrary, String fileType, String inputName, String imgPath) throws Exception {
		if(StringUtils.isNotBlank(knowLibrary.getImgPath()) || StringUtils.isNotEmpty(knowLibrary.getImgPath())) {
			knowLibrary.setImgPath(imgPath);
			
		} else {
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			//上传图片
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			String filePath = "";
			if(filePathList!=null && filePathList.size()>0) {
				filePath = filePathList.get(0);
				System.out.println("上传图片路径："+filePath);
				filePath = FileUploadUtil.subImgPath(filePathList.get(0));
				System.out.println("截取后上传图片路径："+filePath);
			}
			knowLibrary.setImgPath(filePath);
		}
		
		int rows = knowLibraryService.updateByPrimaryKeySelective(knowLibrary);
		if(rows>0) {
			return JSON.toJSONString(RequestResultUtil.getResultUpdateSuccess());
		}
		return JSON.toJSONString(RequestResultUtil.getResultUpdateWarn());
	}
	
	
	/**
	 * @Title: deletePhoto
	 * @Description: 删除图片
	 * @param request
	 * @param model
	 * @param id
	 * @param path
	 * @return 
	 */
	@RequestMapping(value = "/delete-photo")
	@ResponseBody
	public Object deletePhoto(HttpServletRequest request, Model model, Long id, String path) {
		
		try {
			KnowLibrary knowLibrary = knowLibraryService.selectByPrimaryKey(id);
			knowLibrary.setImgPath("");
			int rows = knowLibraryService.updateByPrimaryKeySelective(knowLibrary);
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
	
	/**
	 * @Title: getDataDictValue
	 * @Description: 根据数据字典类型编码和字典编码查询
	 * @param typeCode
	 * @param key
	 * @return 
	 */
	private String getDataDictValue(String typeCode, String key) {
		
		if(StringUtils.isBlank(typeCode) && StringUtils.isBlank(key)) {
			return null;
		}
		
		DataDict dict = new DataDict();
		if(StringUtils.isNotBlank(typeCode)) {
			dict.setTypeCode(typeCode);
		}
		dict.setKey(key);
		List<DataDict> dictList = dataDictService.select(dict);
		if(dictList!=null && dictList.size()>0) {
			dict = dictList.get(0);
		}
		
		if(dict!=null) {
			return dict.getValue();
		}
		return null;
	}

}