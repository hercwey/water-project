package com.learnbind.ai.controller.metertopo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.controller.meters.MetersController;

@Controller
@RequestMapping(value = "/topo")
public class MeterTopoController {

	private static Log log = LogFactory.getLog(MetersController.class);
	private static final String TEMPLATE_PATH = "topo/"; // 页面	
	
	@RequestMapping(value = "/test")
	private String topoTest() {
		return TEMPLATE_PATH + "metertopo";
	}	
	
	/**
	 * @Title: topoEditor
	 * @Description:加载拓扑图编辑器主页
	 * @return 
	 */
	@RequestMapping(value="editor")
	private String topoEditor() {
		return TEMPLATE_PATH + "topologyEditor";
	}
	
	/**
	 * @Title: topoLoading
	 * @Description: 加载等待页面
	 * @return 
	 */
	@RequestMapping(value="loading")
	private String topoLoading() {
		return TEMPLATE_PATH + "loading";
	}
	
	/**
	 * @Title: topoHelp
	 * @Description: 加载帮助页面 
	 * @return 
	 */
	@RequestMapping(value="help")
	private String topoHelp() {
		return TEMPLATE_PATH + "help";
	}
	
	/**
	 * @Title: topoTopology
	 * @Description: 加载拓扑结构图数据
	 * @return 
	 */
	@RequestMapping(value="topology")
	private String topoTopology() {
		return TEMPLATE_PATH + "topology";
	}
	
	/**
	 * @Title: saveTopoTopology
	 * @Description: 保存拓扑结构图
	 * @param topologyJSON  
	 * @param templateId
	 * @param topologyId
	 * @return 
	 */
	@RequestMapping(value="topologyManage/saveTopologyJSON")
	@ResponseBody
	private Object saveTopoTopology(String topologyJSON,String templateId,String topologyId) {
		log.debug("需要保存的TOPO数据为:"+topologyJSON);
		log.debug("需要保存的TOPO templateId is:"+templateId);
		log.debug("需要保存的TOPO topologyId is:"+topologyId);
		
		Map<String,String > response=new HashMap<>();
		response.put("errorInfo", "ok");
		
		return response;		
	}
	
	@RequestMapping(value="topologyManage/deleteAllNodes")
	@ResponseBody
	private Object deleteAlllNodes(String templateId) {
		log.debug("需要删除的拓扑图为:"+templateId);
		Map<String,String > response=new HashMap<>();
		response.put("errorInfo", "ok");
		return response;
	} 
	
	/**
	 * @Title: delNodeById
	 * @Description: 删除指定节点
	 * @param id
	 * @param type
	 * @param dataType
	 * @return 
	 */
	@RequestMapping(value="topologyManage/deleteNodeById")
	@ResponseBody
	private Object delNodeById(String id,String type,String dataType) {
		log.debug("需要删除的拓扑图结点id为:"+id);
		log.debug("需要删除的拓扑图结点type为:"+type);
		log.debug("需要删除的拓扑图结点dataType为:"+dataType);
		Map<String,String > response=new HashMap<>();
		response.put("errorInfo", "ok");
		
		return response;
	}
	
	
	/**
	 * @Title: loadTopoTopology
	 * @Description: 加载拓扑图
	 * @param templateId
	 * @param topologyId
	 * @param topoLevel
	 * @param parentLevel
	 * @return 
	 */
	@RequestMapping(value="/topologyManage/loadTopologyJSON")	
	private String loadTopologyJSON(String templateId,String topologyId,String topoLevel,String parentLevel) {
		log.debug("需要加载的TOPO templateId is:"+templateId);
		log.debug("需要保存的TOPO topologyId is:"+topologyId);
		log.debug("需要保存的TOPO topoLevel is:"+topoLevel);
		log.debug("需要保存的TOPO parentLevel is:"+parentLevel);
		return TEMPLATE_PATH + "topology";
	}
	
	/**
	 * @Title: getAllTemplates
	 * @Description: 获取所有模板
	 * @return 
	 */
	@RequestMapping(value="/topology/getAllTemplates")
	private String getAllTemplates() {
		return TEMPLATE_PATH + "topology";
	}	
	
	/**
	 * @Title: getNode
	 * @Description: 读取某个结点
	 * @param id
	 * @return 
	 */
	@RequestMapping(value="/topology/getNode")
	@ResponseBody
	private Object getNode(String id) {
		Map<String,String > response=new HashMap<>();
		response.put("errorInfo", "ok");		
		return response;
		//return TEMPLATE_PATH + "topology";
	}
		
	
	/**
	 * @Title: saveNode
	 * @Description: 保存某个结点
	 * @param id
	 * @param nextLevel
	 * @return 
	 */
	@RequestMapping(value="/topology/saveNode")
	@ResponseBody
	private Object saveNode(String id,String nextLevel) {
		Map<String,String > response=new HashMap<>();
		response.put("errorInfo", "ok");		
		return response;
		//return TEMPLATE_PATH + "topology";
	}
	
	
}
