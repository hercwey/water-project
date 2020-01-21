package com.learnbind.ai.controller.psmeter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.MeterTreeNodeBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.MeterTree;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MeterTreeService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.util.pinyin4j.Pinyin4jUtils;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.psmeter
 *
 * @Title: PSController.java
 * 			P: parent
 * 			S: sub(son)
 * @Description: 表计父子关系维护控制器
 *
 * @author lenovo
 * @date 2019年10月4日 下午9:07:19
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/psmeter")
public class MeterTreeController {

	private static Log log = LogFactory.getLog(MeterTreeController.class);
	private static final String TEMPLATE_PATH = "psmeter/"; // 页面
	
	//结点类型定义
	//在数据字典中进行定义
	private static final String METER_TREE_NODE_TYPE_CLASSIFY="CLASSIFY";   //分类结点
	private static final String METER_TREE_NODE_TYPE_METER="METER";  //表计结点
	
	
	/**
	 * @Fields metersService： 表计档案
	 */
	@Autowired
	private MetersService metersService; 
	
	
	/**
	 * @Fields locationService：地理位置服务
	 */
	@Autowired
	private LocationService locationService;// 地理位置服务
	
	
	/**
	 * @Fields meterTreeService：meter tree service(表计关系树服务)
	 */
	@Autowired
	private MeterTreeService meterTreeService; // meter tree service
	/**
	 * @Fields dataDictService：数据字典服务
	 */
	@Autowired
	private DataDictService dataDictService;
	
	/**
	 * @Title: psmeterStarter
	 * @Description: 起始页
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	private String psmeterStarter() {
		return TEMPLATE_PATH + "psmeter_starter";
	}
	
	/**
	 * @Title: psmetermain
	 * @Description: 表计父子关系维护主页
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String psmetermain(Model model) {
		return TEMPLATE_PATH + "psmeter_main";
	}
	
	/**
	 * @Title: meterTableMain
	 * @Description: Location tree node's meter table
	 * 				地理位置树下结点所对应的表计列表主页
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/meterTableMain")
	public String meterTableMain(Model model) {
		return TEMPLATE_PATH + "location_meter_main";
	}
	
	/**
	 * @Title: metersTable
	 * @Description: 表计列表
	 * @param model
	 * @param functionModule 功能模块标识：用于页面显示不同功能
	 * @param pageNum        页号
	 * @param pageSize       页大小
	 * @param searchCond     查询条件
	 * @return
	 */
	@RequestMapping(value = "/mtable")
	public String metersTable(Model model, 
							Integer pageNum, Integer pageSize, 
							String searchCond, Long locationId,
							String traceIds) {
	
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = 6;  //默认分页大小
		}
	
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//List<Meters> meterList = metersService.getMeterListByLocationId(locationId, searchCond);
		List<Meters> meterList = metersService.getMeterListByTraceIds(traceIds, searchCond);
		PageInfo<Meters> pageInfo = new PageInfo<>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)		
		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(Meters meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("meterTypeValue", getDataDictValue(EnumDataDictType.METER_TYPE.getCode(), meter.getMeterType()));//水表类型
			meterMap.put("factoryValue", getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMapList.add(meterMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterMapList); // 列表数据
	
		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		//model.addAttribute("pageNum", pageNum);
		//model.addAttribute("pageSize", pageSize);
	
		// 查询条件回传
		model.addAttribute("searchCond", searchCond);
	
		return TEMPLATE_PATH + "meter_table";
	}
	
	/**
	 * @Title: search
	 * @Description: 查询地理位置
	 * @param model
	 * @param searchCond
	 * @param id
	 * @param action	-1=上一个；1=下一个
	 * @return 
	 */
	@RequestMapping(value = "/search", produces = "application/json")
	@ResponseBody
	public Object search(Model model, String searchCond, Long id, int action) {
		
		try {
			
			//searchCond = Pinyin4jUtils.toPyUppercase(searchCond);//查询条件转拼音
			log.info("meter tree search : "+searchCond);
			 MeterTree meterTreeNode=null;
			
			if(id==null || action==0){
				//返回第一个符合条件的记录				
				meterTreeNode = meterTreeService.getFirstBySearchCond(searchCond);
			} else {
				meterTreeNode = meterTreeService.getOneBySearchCond(searchCond, id, action);
			}
			
			Map<String, Object> respMap = RequestResultUtil.getResultSuccess("查询成功！");
			respMap.put("location", meterTreeNode);
			return respMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("查询异常，请重新操作！");
	}

	/**
	 * @Title: getNodes
	 * @Description: ztree异步请求获取节点(Meter tree)
	 * @param response
	 * @param model
	 * @param id   tree node's id
	 */
	@RequestMapping(value = "/get-nodes", produces = "application/json")
	public void getNodes(HttpServletResponse response, Model model, Long id) {
		try {
			if(id==null){
				id = 0l;
			}
			List<MeterTreeNodeBean> locationList = meterTreeService.getChildListById(id);
			for(MeterTreeNodeBean treeNode:locationList) {
				if(treeNode.getNodeType().equalsIgnoreCase(METER_TREE_NODE_TYPE_METER)) {
					treeNode.setIcon("/images/meter_24px.png");
				}
			}
			
			String locationMapListJson = JSON.toJSONString(locationList);
			
			response.setContentType("text/text;charset=utf-8"); 
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(locationMapListJson);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Title: dropTreeNode
	 * @Description: 自location tree中向meter tree中拖放结点
	 * @param id  Location tree node's id
	 * @param traceIds  Location tree node's traceids 
	 * @return 
	 * 			map--->success or error
	 */
	@RequestMapping(value = "/dropTreeNode", produces = "application/json")
	@ResponseBody
	public Object dropTreeNode(Long id,String traceIds) {
		log.debug("插入的节点id为:"+id);
		log.debug("插入的节点traceIds为:"+traceIds);
		
		//(0.1)定义traceIds与id之间的映射(用于查询结点parent'id) 
		//traceIds---->id
		Map<String,Long> traceIds2newIdMap=new HashMap<>();
		//(0.2)定义traceIds与level之间的映射(用于查询结点的level) 
		//traceIds---->level
		Map<String,Integer> traceIds2LevelMap=new HashMap<>();		
		//(0.3)定义traceIds与new traceIds之间的映射(用于查询结点parent's traceIds)		
		//traceIds---->new traceIds
		Map<String,String> traceIds2newTraceIdsMap=new HashMap<>();		
		
		//(1)自location tree中获取指定节点及其所有子节点列表
		List<Location> locationList= locationService.getLocationByTraceIds(traceIds);		
		
		//(2)按一定的规则将这些节点置于meterTree中.
		for(Location location:locationList) {
			//(2.1)获取待插入节点的父结点ID
			String parentTrace=getParentTrace(location.getTraceIds());
			Long pid=traceIds2newIdMap.get(parentTrace);
			if(pid==null) {
				pid=0L;
			}			
			//(2.2根据location node向meterTree中插入结点
			Long nodeId=insertNode2MeterTree(location,pid);
			traceIds2newIdMap.put(location.getTraceIds(), nodeId);
			
			String treeNodeTraceIds =genTraceIds(nodeId,pid,location,traceIds2newTraceIdsMap);
			Integer treeNodeLevel=genTreeNodeLevel(nodeId,pid,location,traceIds2LevelMap);	
			
			Integer treeNodeSortValue=genTreeNodeSortValue(pid);
			updateNodeProperty(nodeId,treeNodeTraceIds,treeNodeLevel,treeNodeSortValue);
			
		}
		return RequestResultUtil.getResultAddSuccess("create tree node ok!");
	}
	
	@RequestMapping(value = "/moveMeterTreeNode", produces = "application/json")
	@ResponseBody
	public Object moveTreeNode(Long nodeId,Long targetNodeId,String moveType) {
		final String MOVE_TYPE_INNER="inner";//移动某个节点下
		final String MOVE_TYPE_PREV="prev";  //同级移动
		final String MOVE_TYPE_NEXT="next";  //同级移动
		
		log.debug("move node id is"+nodeId);
		log.debug("move node  target id is"+targetNodeId);
		log.debug("move type is"+moveType);
		
		MeterTree movedNode=meterTreeService.selectByPrimaryKey(nodeId);
		MeterTree targetNode=meterTreeService.selectByPrimaryKey(targetNodeId);
		
		switch (moveType) {
		case MOVE_TYPE_INNER:   //成为子节点
			//将结点放置到其它的节点下时.
			putTreeNode2TargetTreeNode(movedNode,targetNode);
			break;
		case MOVE_TYPE_PREV:			
		case MOVE_TYPE_NEXT:
			//只需要交换两条记录间的排序号,其它的不必处理.
			//exchangeSortValue(nodeId,targetNodeId);
			boolean sameLevel=isSameLevel(movedNode,targetNode);  //判定是否为同级别移动
			if(sameLevel) {
				log.debug("同级别之间拖放结节......");
				sameLevelMove(movedNode,targetNode,moveType);
			}
			else {  //移动的结点与目标节点不同级别
				diffLevelMove(movedNode,targetNode,moveType);
				log.debug("不同级别之间拖放结节......");
			}
			break;
		}
		
		return RequestResultUtil.getResultAddSuccess("move tree node ok!");
	}
	
	/**
	 * @Title: diffLevelMove
	 * @Description: 不同级别节点移动
	 * @param movedNode	被移动节点
	 * @param targetNode  目标节点
	 * @param moveType 移动类型
	 */
	private void diffLevelMove(MeterTree movedNode,MeterTree targetNode,String moveType) {
		final String MOVE_TYPE_PREV="prev";  //同级移动
		final String MOVE_TYPE_NEXT="next";  //同级移动
		
		//根据参考点的情况进行处理.
		switch(moveType) {
		case MOVE_TYPE_NEXT:
			//(1)更新被移动结点的pid
			updateMovedNodePid(movedNode.getId(),targetNode.getPid());
			//(2)更新被移动结点的sort_value
			meterTreeService.adjustTreeNodeSilbingPos(targetNode.getPid(),targetNode.getSortValue(),0);  //(2.1)移动结点
			updateMovedNodeSortValue(movedNode.getId(),targetNode.getSortValue()+1);  //(2.2)设置被移动结点
			//(3)设置结点node_level			
			updateMovedNodesLevel(movedNode,targetNode);
			//(4)更新traceIds			
			updateMovedNodesTraceIds(movedNode,targetNode);
			break;
		case MOVE_TYPE_PREV:
			//(1)更新被移动结点的pid
			updateMovedNodePid(movedNode.getId(),targetNode.getPid());
			//(2)更新被移动结点的sort_value
			meterTreeService.adjustTreeNodeSilbingPos(targetNode.getPid(),targetNode.getSortValue(),1);  //(2.1)移动结点
			updateMovedNodeSortValue(movedNode.getId(),targetNode.getSortValue());  //(2.2)设置被移动结点
			//(3)设置结点node_level			
			updateMovedNodesLevel(movedNode,targetNode);			
			//(4)更新traceIds  最有可能出错误的位置			
			updateMovedNodesTraceIds(movedNode,targetNode);
			break;			
		}
		
	}
	
	private void updateMovedNodesLevel(MeterTree movedNode,MeterTree targetNode) {
		Integer movedNodeOldLevel=movedNode.getNodeLevel();
		Integer movedNodeNewLevel=targetNode.getNodeLevel(); //与target级别相同.
		String movedNodeTraceIds=movedNode.getTraceIds();
		meterTreeService.updateSubTreeLevel(movedNodeNewLevel,movedNodeOldLevel,movedNodeTraceIds);
	}
	
	private void updateMovedNodesTraceIds(MeterTree movedNode,MeterTree targetNode) {
		String targetNodeTraceIds=targetNode.getTraceIds();
		String prefixTraceIds=StringUtils.replace(targetNodeTraceIds, targetNode.getId().toString(), "");
		String movedNodeNewTraceIds=prefixTraceIds+""+movedNode.getId();
		String movedNodeOldTraceIds=movedNode.getTraceIds();
		meterTreeService.updateSubTreeTraceIds(movedNodeNewTraceIds, movedNodeOldTraceIds, movedNodeOldTraceIds);
	}
	
	
	private void updateMovedNodePid(Long id,Long pid) {
		MeterTree updateObj=new MeterTree();
		updateObj.setId(id);
		updateObj.setPid(pid);
		meterTreeService.updateByPrimaryKeySelective(updateObj);		
	}
	private void updateMovedNodeSortValue(Long id,Integer sortValue) {
		MeterTree updateObj=new MeterTree();
		updateObj.setId(id);
		updateObj.setSortValue(sortValue);
		meterTreeService.updateByPrimaryKeySelective(updateObj);
	}
	
	
	/**
	 * @Title: putTreeNode2TargetTreeNode
	 * @Description: 将treeNode置于targetTreeNode
	 * @param movedNode	      需要移动的结点
	 * @param targetNode  目标结点,同时为父结点 
	 */
	private void putTreeNode2TargetTreeNode(MeterTree movedNode,MeterTree targetNode) {
		//Long targetNodeId=targetNode.getId();   //目标结点ID,用于作为被移动结点的父结点.
		Long nodeId=movedNode.getId();
		
		//需要处理的属性如下:		
		//被移动节点的排序号发生变化.
		//更新移动节点序号根结点序号(父结点的最后一个)		
		updateSubtreeRootSortValue(movedNode.getId(),targetNode.getId());
		//修改子树根结点pid
		updateSubTreeRootPID(nodeId,targetNode.getId());
		//被移动节点的level发生变化,从而引起其子节点的level发生变化.
		//updateSubTreeLevel(int subTreeRootOldLevel,int parentNodeLevel, String subTreeRootTraceIds)
		updateSubTreeLevel(movedNode.getNodeLevel(),
						   targetNode.getNodeLevel(),
						   movedNode.getTraceIds());		
		//被移动节点的父节点发生变化,从而后引起其pid发生变化,继而自身及其子结点的traceIds发生变化		
		updateSubTreeTraceIds(movedNode.getId(),
							targetNode.getTraceIds(),
							movedNode.getTraceIds());		
	}
	
	//更新子树根结点sort值
	private void updateSubtreeRootSortValue(Long movedNodeId, Long targetNodeId) {
		//TODO ADD BUSINESS CODE HERE
		Integer movedNodeNewSortValue=genTreeNodeSortValue(targetNodeId);
		MeterTree updateObj=new MeterTree();
		updateObj.setId(movedNodeId);
		updateObj.setSortValue(movedNodeNewSortValue);	
		meterTreeService.updateByPrimaryKeySelective(updateObj);
	}
	
	/**
	 * @Title: genTraceIdByParentNode
	 * @Description: 根据父结点traceId及子节点ID----生成--->子节点traceIds
	 * @param nodeId  子节点id
	 * @param parentNodeTraceId  父结点tracedIds
	 * @return 
	 */
	private String genTraceIdByParentNode(Long nodeId,String parentNodeTraceId) {
		final String SPLIT_CHAR="-";
		String traceIds="";
		if(StringUtils.isEmpty(parentNodeTraceId)) {
			traceIds=Long.toString(nodeId);
		}
		else {
			traceIds=parentNodeTraceId+SPLIT_CHAR+Long.toString(nodeId);
		}
		
		return traceIds;
	}
	
	/**
	 * @Title: updateSubTreeRootPID
	 * @Description: 更新子树根结点PID
	 * @param rootNodeId	子树根结点ID
	 * @param pid PID(父结点ID)
	 */
	private void updateSubTreeRootPID(Long rootNodeId,Long pid) {
		MeterTree updateObj=new MeterTree();
		updateObj.setId(rootNodeId);
		updateObj.setPid(pid);
		meterTreeService.updateByPrimaryKeySelective(updateObj);
	}
	
	//更新子树各结点level  sub tree node's level	
	//          MeterTree movedNode,MeterTree targetNode)
	private void updateSubTreeLevel(int subTreeRootOldLevel,
									int parentNodeLevel, 
									String subTreeRootTraceIds) {
		int subTreeRootNewLevel=parentNodeLevel+1;  //父结点level+1----设置--->子树根结点level
		meterTreeService.updateSubTreeLevel(subTreeRootNewLevel,
											subTreeRootOldLevel,
											subTreeRootTraceIds);	
	}
	
	//更新sub tree node's traceIds
	private void updateSubTreeTraceIds(Long movedNodeId,
										String partentNodeTraceIds,
										String subTreeRootOldTraceIds) {				
		//生成被移动节点新的traceIds		
		String subTreeRootNewTraceIds=genTraceIdByParentNode(movedNodeId,partentNodeTraceIds);
		meterTreeService.updateSubTreeTraceIds(subTreeRootNewTraceIds,
												subTreeRootOldTraceIds,
												subTreeRootOldTraceIds);
	}
		
	/**
	 * @Title: sameLevelMove
	 * @Description: 同级别结点移动
	 * @param movedNode
	 * @param targetNode 
	 */
	private void sameLevelMove(MeterTree movedNode,MeterTree targetNode,String moveType) {
		final String MOVE_TYPE_PREV="prev";  //同级移动
		final String MOVE_TYPE_NEXT="next";  //同级移动
		
		int movedNodeSortValue=movedNode.getSortValue();
		int targetNodeSortValue=targetNode.getSortValue();
		Long pid=movedNode.getPid();
		int addValue=0;
		
		//确定是向上移动节点还是向下移动节点
		if(movedNodeSortValue>targetNodeSortValue) {  //结点向上移动
			addValue=1;
		}
		else {
			addValue=-1;							//节点向下移动
		}
		
		//根据参考点的情况进行处理.
		switch(moveType) {
		case MOVE_TYPE_PREV:
			switch(addValue) {
			case 1:  //结点向上移动,位于参考节点的prev(上面)
				adjustTreeNodePos(targetNodeSortValue,1,
								  movedNodeSortValue,0,
								  addValue,pid);				
				movedNode.setSortValue(targetNode.getSortValue());
				meterTreeService.updateByPrimaryKeySelective(movedNode);
				break;
			case -1: //结点向下移动,位于参考节点的prev(上面)
				adjustTreeNodePos(movedNodeSortValue,0,
						targetNodeSortValue,1,
						  addValue,pid);				
				movedNode.setSortValue(targetNode.getSortValue());
				meterTreeService.updateByPrimaryKeySelective(movedNode);
				break;
			}
			break;
		case MOVE_TYPE_NEXT:
			switch(addValue) {
			case 1:   //结点向上移动,位于参考节点的next(下面)
				adjustTreeNodePos(targetNodeSortValue,0,
									movedNodeSortValue,0,
						  			addValue,pid);
				movedNode.setSortValue(targetNode.getSortValue()+1);
				meterTreeService.updateByPrimaryKeySelective(movedNode);
				break;
			case -1:	//向下移动节点,且位于targetNode的next(下面)
				adjustTreeNodePos(movedNodeSortValue,0,
						targetNodeSortValue,1,
						  addValue,pid);				
				movedNode.setSortValue(targetNode.getSortValue());
				meterTreeService.updateByPrimaryKeySelective(movedNode);
				break;
			}
			break;			
		}
		
	}
	
	/**
	 * @Title: adjustTreeNodePos
	 * @Description: 调整节点位置
	 * @param startSortValue	起始位置
	 * @param endSortValue 		结束位置
	 */
	private void adjustTreeNodePos(Integer startSortValue,Integer includeStart,
									Integer endSortValue, Integer includeEnd,
									Integer addValue,Long pid) {
		meterTreeService.addNodeSortValueOfSilbing( startSortValue, includeStart,
													endSortValue,includeEnd, 
													addValue,pid);
	}
	
	/**
	 * @Title: isSameLevel
	 * @Description: 判定两个结点是否为同一级
	 * @param movedNode  被移动节点
	 * @param targetNode 目标节点
	 * @return 
	 * 		true:如果两个节点属于同一级
	 * 		else false;
	 */
	private boolean isSameLevel(MeterTree movedNode,MeterTree targetNode) {
		//如果两个节点的父结点相同则为同一级		
		//对于Long类型需采用euqals来进行比较.(原因:数据已经打包)
		if(movedNode.getPid().equals(targetNode.getPid()))
			return true;
		else
			return false;		
	}
		
	/**
	 * @Title: genTraceIds
	 * @Description: 生成traceIds
	 * @param nodeId
	 * @param pid
	 * @param location
	 * @param traceIds2newTraceIdsMap
	 * @return 
	 */
	private String genTraceIds(Long nodeId,Long pid,
								Location location,
								Map<String,String> traceIds2newTraceIdsMap) {
		String traceIds="";
		if(pid==0) {				
			//updateNodeProperty1(nodeId,Long.toString(nodeId));
			traceIds=Long.toString(nodeId);
			traceIds2newTraceIdsMap.put(location.getTraceIds(), nodeId.toString());
		}
		else {  //非根结点时
			String parentTrace1=getParentTrace(location.getTraceIds());
			String newParentTraceIds=traceIds2newTraceIdsMap.get(parentTrace1);  //查询父结点new trace ids
			if(newParentTraceIds==null) {
				log.debug("parent node's traceids error"); 
			}
			else {
				String newNodeTraceIds=newParentTraceIds+"-"+nodeId.toString();
				traceIds2newTraceIdsMap.put(location.getTraceIds(), newNodeTraceIds);
				traceIds=newNodeTraceIds;
			}				
		}
		return traceIds;
	}
	
	/**
	 * @Title: genTreeNodeLevel
	 * @Description: 生成node level
	 * @param nodeId
	 * @param pid
	 * @param location
	 * @param traceIds2LevelMap
	 * @return 
	 */
	private Integer genTreeNodeLevel(Long nodeId,Long pid,
									Location location,
									Map<String,Integer> traceIds2LevelMap) {
		Integer treeNodeLevel=1;
		if(pid==0) {				
			treeNodeLevel=1;
			traceIds2LevelMap.put(location.getTraceIds(), 1);
		}
		else {  //非根结点时
			String parentTrace1=getParentTrace(location.getTraceIds());
			Integer parentLevel=traceIds2LevelMap.get(parentTrace1);  //查询父结点new trace ids
			if(parentLevel==null) {
				log.debug("parent node's level error"); 
			}
			else {
				Integer nodeLevel=parentLevel+1;				
				treeNodeLevel=nodeLevel;
				traceIds2LevelMap.put(location.getTraceIds(), nodeLevel);
			}				
		}
		
		return treeNodeLevel;
	}
	
	/**
	 * @Title: genTreeNodeSortValue
	 * @Description: 生成sort value  
	 * 			根据指定节点下直接子节点的最大排序值向下排序	 
	 * @param pid  结点id
	 * @return 
	 */
	private Integer genTreeNodeSortValue(Long pid) {
		Integer sortValue=0;		
		Integer maxValue=getMaxSortValueOfSibling(pid);		
		if(maxValue!=null) {			
			sortValue=maxValue+1;
		}				
		return sortValue;
	}
	
	
	/**
	 * @Title: getMaxSortValueOfSibling
	 * @Description: 查询与指定结点同级结点的最大排序号
	 * @param parentId  结点的父结点id
	 * @return 
	 * 		返回指定结点直接子结点的最大序号 (序号自0开始)
	 * 		返回结果:null或是integer 		
	 */
	private Integer getMaxSortValueOfSibling(Long parentId) {
		return meterTreeService.getMaxSortValueOfSibling(parentId);
		
	}
	
	
	private void updateNodeProperty(Long nodeId,String newTraceIds,
									Integer nodeLevel,Integer sortValue) {
		MeterTree node=new MeterTree();
		node.setId(nodeId);
		node.setTraceIds(newTraceIds);
		node.setNodeLevel(nodeLevel);
		node.setSortValue(sortValue);
		meterTreeService.updateByPrimaryKeySelective(node);
	}
	
	/**
	 * @Title: dropMeter
	 * @Description: 自LocationTree meter table拖放记录到meter tree
	 * @param meterId  表计ID
	 * @param meterPlace 表计地理位置(用做meter node的名称)
	 * @return 
	 * 		success or error    JSON格式
	 */
	@RequestMapping(value = "/dropMeter", produces = "application/json")
	@ResponseBody
	public Object dropMeter(Long meterId,String meterPlace) {
		log.debug("拖放的表计id is:"+meterId);
		log.debug("拖放的表计地址 is:"+meterPlace);
		
		//Long pid=0;  //已经确定
		//traceIds  采用自身ID即可
		//sort_value 采用PID查询即可.
		//node_level =1
		
		dropMeter2MeterTree(meterId,meterPlace);		
		return RequestResultUtil.getResultAddSuccess("create tree node ok!");
	}
	
	/**
	 * @Title: meterInfo
	 * @Description: 显示表计信息
	 * @param meterId  表计id
	 * @return  表计信息页面
	 */
	@RequestMapping(value = "/meterInfo")
	public String meterInfo(Long meterId,Model model) {
		String page="";
		if(meterId!=null) {
			Meters meter=metersService.selectByPrimaryKey(meterId);
			model.addAttribute("meter",meter);
			page="meter_info";
		}
		else {
			page="no_meter_info";
		}
		
		return TEMPLATE_PATH + page;
	}
	
	/**
	 * @Title: treeNodeRename
	 * @Description: 树结点重命名
	 * @param nodeId	树结点id
	 * @param newName	树结点new name
	 * @return 
	 * 		return success or error   JSON 
	 */
	@RequestMapping(value = "/treeNodeRename", produces = "application/json")
	@ResponseBody
	public Object treeNodeRename(Long nodeId,String newName) {
		updateTreeNodeName(nodeId,newName);
		return RequestResultUtil.getResultAddSuccess("create tree node ok!");
	}
	
	/**
	 * @Title: treeNodeRemove
	 * @Description: remove meter tree node
	 * @param nodeId	结点id
	 * @param traceIds 被移除结点之父结点路径(以'-'分隔)
	 * @return 
	 * 	JSON:   success or error
	 */
	@RequestMapping(value = "/treeNodeRemove", produces = "application/json")
	@ResponseBody
	public Object treeNodeRemove(Long nodeId) {
		deleteTreeNode(nodeId);
		return RequestResultUtil.getResultAddSuccess("create tree node ok!");
	}
	
	/**
	 * @Title: deleteTreeNode
	 * @Description: 删除树结点
	 * @param nodeId	结点id
	 * @param traceIds 被移除结点之父结点路径(以'-'分隔)
	 * @return
	 * 		删除结点时影响的数据行数 
	 */
	private int deleteTreeNode(Long nodeId) {
		int rows=0;
		MeterTree meterTree =meterTreeService.selectByPrimaryKey(nodeId);
		if(meterTree!=null) {
			String traceIds=meterTree.getTraceIds();
			rows=meterTreeService.deleteMeterTreeNodeByTraceIds(traceIds);
		}
		
		return rows;
	}
	
	/**
	 * @Title: updateTreeNodeRename
	 * @Description: 更新树结点名称
	 * @param nodeId  结点id
	 * @param newName  结点的新名称
	 * @return 
	 * 				更新所影响的记录个数
	 */
	private int updateTreeNodeName(Long nodeId,String newName) {
		MeterTree node=new MeterTree();
		node.setId(nodeId);
		node.setName(newName);
		return meterTreeService.updateByPrimaryKeySelective(node);		
	}
	
	
	
	/**
	 * @Title: addMeter2MeterTree
	 * @Description: 向Meter tree中加入表计
	 * @param meterId   表计ID
	 * @param meterPlace 表计地理位置
	 */
	private void dropMeter2MeterTree(Long meterId,String meterPlace) {
		MeterTree treeNode=new MeterTree();
		Long pid=0l;
		
		treeNode.setName(meterPlace);
		treeNode.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		treeNode.setPid(pid);		
		//根据location tree node type变换成meter tree node type
		String treeNodeType=METER_TREE_NODE_TYPE_METER;
		treeNode.setMeterId(meterId);
		treeNode.setNodeType(treeNodeType);
		
		//插入记录
		meterTreeService.insertSelective(treeNode);
		
		//更新记录
		Long nodeId=treeNode.getId();				
		updateMeterTreeNode(nodeId, pid);
		
	}	
	
	/**
	 * @Title: updateMeterTreeNode
	 * @Description: 更新meter tree node's property
	 * @param nodeId  meter tree node'id
	 * @param pid meter tree's pid
	 * @return 
	 * 		更新记录的条数.
	 */
	private int updateMeterTreeNode(Long nodeId,Long pid) {
		String traceIds=Long.toString(nodeId);  //采用自身ID即可
		Integer sortValue=this.genTreeNodeSortValue(pid);		
		Integer nodeLevel =1;  //sort_value 采用PID查询即可.
		
		MeterTree updateObj=new MeterTree();
		updateObj.setId(nodeId);
		updateObj.setTraceIds(traceIds);
		updateObj.setSortValue(sortValue);
		updateObj.setNodeLevel(nodeLevel);
		return meterTreeService.updateByPrimaryKeySelective(updateObj);
	}
	
	
	/**
	 * @Title: getParentTrace
	 * @Description: 获取祖先结点的id轨迹(以-为分隔符)
	 * @param traceIds
	 * @return 
	 */
	private String getParentTrace(String traceIds) {
		String parentTrace="";
		String[] idArr=traceIds.split("-");
		int arrLen=idArr.length;
		for(int i=0;i<arrLen-1;i++) {
			if(i<arrLen-2) {
				parentTrace=parentTrace+idArr[i]+"-";
			}				
			else {
				parentTrace=parentTrace+idArr[i];
			}
		}
		return parentTrace;
	}
	
	/**
	 * @Title: insertNode2MeterTree
	 * @Description: 根据Location结点向meterTree中插入结点
	 * @param locationTreeNode  location节点
	 * @param pid  需要插入节点的父节点ID(在meter tree中)
	 * @return 插入结点的id
	 * 		
	 */
	private Long insertNode2MeterTree(Location locationTreeNode,Long pid) {
		/*
		 * final String LOCATION_TREE_NODE_TYPE_BLOCK="BLOCK"; final String
		 * LOCATION_TREE_NODE_TYPE_BUILD="BUILDING"; final String
		 * LOCATION_TREE_NODE_TYPE_UNIT="UNIT"; final String
		 * LOCATION_TREE_NODE_TYPE_ROOM="ROOM";
		 */
		
		MeterTree treeNode=new MeterTree();
		treeNode.setName(locationTreeNode.getName());   //将location node中的地理位置作为meter tree node's name
		treeNode.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		treeNode.setPid(pid);
		//根据location tree node type-----变换成----->meter tree node type
		String treeNodeType=convertTreeNodeType(locationTreeNode.getLocalNodeType());		
		treeNode.setNodeType(treeNodeType);
		
		//如果是表计节点类型时,则获取表计信息
		if(treeNodeType.equalsIgnoreCase(METER_TREE_NODE_TYPE_METER)) {
			Long meterId=getMeterOfLocation(locationTreeNode.getId());
			if(meterId!=null)
				treeNode.setMeterId(meterId);
			else
				log.debug("未找到表计信息.....");
		}	
		
		//向数据库中插入记录
		meterTreeService.insertSelective(treeNode);
		Long nodeId=treeNode.getId();
		log.debug("插入结点的ID为:"+nodeId);
		
		return nodeId;
	}
	
	@Autowired
	private LocationMeterService locationMeterService;
	
	private Long getMeterOfLocation(Long locationId) {
		Long meterId=null;
		LocationMeter searchObj=new LocationMeter();
		searchObj.setLocationId(locationId);
		searchObj.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationMeter> locationMeterList= locationMeterService.select(searchObj);
		if(locationMeterList.size()>0) {
			meterId=locationMeterList.get(0).getMeterId();
		}
		return meterId;
	}
	
	/**
	 * @Title: convertTreeNodeType
	 * @Description: 转换location tree node type为meter tree node type
	 * @param localNodeType  Location tree node type
	 * @return  meter tree node type
	 * 		case LOCATION_TREE_NODE_TYPE_BLOCK:
			case LOCATION_TREE_NODE_TYPE_BUILD:
			case LOCATION_TREE_NODE_TYPE_UNIT:
					treeNodeType=METER_TREE_NODE_TYPE_CLASSIFY;  //分类节点
					
			case LOCATION_TREE_NODE_TYPE_ROOM:
				treeNodeType=METER_TREE_NODE_TYPE_METER;		//分类节点
			
	 */
	//@SuppressWarnings("unused")
	private String convertTreeNodeType(String localNodeType) {
		final String LOCATION_TREE_NODE_TYPE_BLOCK="BLOCK";
		final String LOCATION_TREE_NODE_TYPE_BUILD="BUILDING";
		final String LOCATION_TREE_NODE_TYPE_UNIT="UNIT";
		final String LOCATION_TREE_NODE_TYPE_ROOM="ROOM";
		
		String treeNodeType="";
		switch (localNodeType){
			case LOCATION_TREE_NODE_TYPE_BLOCK:
			case LOCATION_TREE_NODE_TYPE_BUILD:
			case LOCATION_TREE_NODE_TYPE_UNIT:
					treeNodeType=METER_TREE_NODE_TYPE_CLASSIFY;
				break;
			case LOCATION_TREE_NODE_TYPE_ROOM:
				treeNodeType=METER_TREE_NODE_TYPE_METER;
				break;
			default:
				treeNodeType=METER_TREE_NODE_TYPE_CLASSIFY;
				break;
		}
		return treeNodeType;
	}
	
	//----------------------------内部方法------------------------
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
		
		
		public static void main(String[] args) {
			String traceIds="1-2-3";
			String parentTrace="";
			String[] idArr=traceIds.split("-");
			int arrLen=idArr.length;
			for(int i=0;i<arrLen-1;i++) {
				if(i<arrLen-2) {
					parentTrace=parentTrace+idArr[i]+"-";
				}				
				else {
					parentTrace=parentTrace+idArr[i];
				}
			}
			/*
			 * if(arrLen-1>=0) { parentTrace=parentTrace+idArr[arrLen-1]; }
			 */
			System.out.println("the result is:"+parentTrace);
			
		}
	
	
	
	
}
