package com.learnbind.ai.controller.statclassify;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.bean.StatClassifyBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.StatClassify;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.statclassify.StatClassifyService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.statclassify
 *
 * @Title: StatClassifyController.java
 * @Description: 统计分类前端控制器
 *
 * @author Administrator
 * @date 2019年12月27日 下午09:22:54
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat-classify")
public class StatClassifyController {

	private static Log log = LogFactory.getLog(StatClassifyController.class);

	private static final String TEMPLATE_PATH = "statclassify/"; // 页面

	@Autowired
	private StatClassifyService statClassifyService;// 地理位置服务
	@Autowired
	private DataDictService dataDictService;// 数据字典服务
	@Autowired
	private LocationService locationService;// 地理位置服务

	/**
	 * @Title: starter
	 * @Description: 起始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "classify_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {

		// 查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.CLASSIFY_NODE_TYPE);
		model.addAttribute("dictList", dictList);
		//List<Location> locationList = locationService.getBlockListByPid(null);
		//model.addAttribute("locationList", locationList);

		return TEMPLATE_PATH + "classify_main";
	}
	
	/**
	 * @Title: getNodes
	 * @Description: ztree异步请求获取节点
	 * @param response
	 * @param model
	 * @param id 
	 */
	@RequestMapping(value = "/get-nodes", produces = "application/json")
	public void getNodes(HttpServletResponse response, Model model, Long id) {
		
		try {
			
			if(id==null){
				id = 0l;
			}
			
			List<StatClassifyBean> classifyList = statClassifyService.getChildListById(id);
			String classifyListJson = JSON.toJSONString(classifyList);
			
			response.setContentType("text/text;charset=utf-8"); 
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(classifyListJson);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @param id
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, Long id) {

		StatClassify classify = statClassifyService.selectByPrimaryKey(id);
		model.addAttribute("classify", classify);
		
		// 查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.CLASSIFY_NODE_TYPE);
		model.addAttribute("dictList", dictList);
		List<Location> locationBlockList = locationService.getBlockListByPid(null);
		model.addAttribute("locationBlockList", locationBlockList);
		
		StatClassify parentClassify = null;
		if(classify!=null) {
			parentClassify = statClassifyService.selectByPrimaryKey(classify.getPid());
		}
		
		model.addAttribute("parentClassify", parentClassify);

		return TEMPLATE_PATH + "classify_dialog_edit";
	}

	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param request
	 * @param classify
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(HttpServletRequest request, StatClassify classify) {

		classify.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		int rows = statClassifyService.insertSelective(classify);
		if (rows>0) {
			Map<String, Object> respMap = RequestResultUtil.getResultAddSuccess();
			respMap.put("newNodes", classify);
			return respMap;
		}
		return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param request
	 * @param classify
	 * @return 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(HttpServletRequest request, StatClassify classify) {

		int row = statClassifyService.updateByPrimaryKeySelective(classify);
		if (row > 0) {
			Map<String, Object> respMap = RequestResultUtil.getResultUpdateSuccess();
			respMap.put("newNodes", classify);
			return respMap;
		} else {
			return RequestResultUtil.getResultUpdateWarn();
		}
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(HttpServletRequest request, Long id) {
		StatClassify classify = new StatClassify();
		classify.setId(id);
		classify.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		int row = statClassifyService.updateByPrimaryKeySelective(classify);
		if (row > 0) {
			return RequestResultUtil.getResultDeleteSuccess();
		} else {
			return RequestResultUtil.getResultDeleteWarn();
		}
	}	
	
	/**
	 * @Title: updateDropClassify
	 * @Description: 拖拽后更新数据库
	 * @param request
	 * @param classifyId
	 * @param parentClassifyId
	 * @return 
	 */
	@RequestMapping(value = "/update-drop-classify", produces = "application/json")
	@ResponseBody
	public Object updateDropClassify(HttpServletRequest request, Long classifyId, Long parentClassifyId) {

		try {
			//parentClassifyId==null时表示拖拽到了根节点
			if(parentClassifyId==null) {
				parentClassifyId = 0l;
			}
			int rows = statClassifyService.updateDropClassify(classifyId, parentClassifyId);
			if (rows > 0) {
				return RequestResultUtil.getResultSaveSuccess();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultSaveWarn();
		
	}
	
}