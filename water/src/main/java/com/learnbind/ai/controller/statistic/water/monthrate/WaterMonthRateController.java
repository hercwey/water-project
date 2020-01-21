package com.learnbind.ai.controller.statistic.water.monthrate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.model.MeterTree;
import com.learnbind.ai.service.meters.MeterTreeService;
import com.learnbind.ai.service.statistic.StatisticService;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.statistic.water.monthlossrate
 *
 * @Title: WaterMonthRateController.java
 * @Description: 漏失率-月统计
 *
 * @author lenovo
 * @date 2019年9月2日 上午12:24:47
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class WaterMonthRateController {
	private static Log log = LogFactory.getLog(WaterMonthRateController.class);
	private static final String TEMPLATE_PATH = "statistic/water/monthrate/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	
	/**
	 * @Fields meterTreeService：表计关系(父子-关系)服务
	 */
	@Autowired
	MeterTreeService meterTreeService;
	
	//------------------抄表汇总-月------------------------
	/**
	 * @Title: statisticWaterMonthRateMain
	 * @Description: 漏失率-月统计	 
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/water/monthrate/main")
	public String statisticWaterMonthRateMain(Model model) {
		return TEMPLATE_PATH + "statistic_water_month_rate_main";
	}
	
	/**
	 * @Title: statisticWaterMonthRateTable
	 * @Description: 		查询-指定月漏失率-原始数据-抄表记录  (自抄表记录中查询)
	 * @param pageNum  		页号
	 * @param pageSize 		页大小
	 * @param searchCond   查询条件  yyyy-mm
	 * @param model
	 * @return 
	 * 		数据格式:
	 * 			[
	 * 				{
	 * 					readType: "全部",     // 可选值:  全部,己抄,未抄,抄表率
	 * 					amount:   xxx    // 数量
	 * 				},
	 * 				{}
	 * 			]
	 */
	@RequestMapping(value = "/water/monthrate/table")
	public String statisticWaterMonthRateTable(Integer pageNum, 
											  Integer pageSize,
											  String searchCond, 
											  String traceIds, 
											  Model model) {
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}
		
		log.debug("searchCond"+searchCond);
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String,Object>> readRecordList = new ArrayList<>();		
		readRecordList=statisticService.searchReadMeterRecordPerMonth(searchCond, traceIds);  //查询抄表记录		
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(readRecordList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		// 传递如下数据至前台页面
		model.addAttribute("readRecordList", readRecordList);  	//列表数据
		model.addAttribute("pageInfo", pageInfo);  				//分页数据
		model.addAttribute("searchCond", searchCond);  			//查询条件回传			
		
		return TEMPLATE_PATH+"statistic_water_month_rate_table";
	}
	
	
	/**
	 * @Title: fmtFloat
	 * @Description: 格式化浮点数,保留两部小数
	 * @param f  需格式化的数据
	 * @return 格式化的字符串
	 */
	private String fmtFloat(float f) {
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
		String fStr = df.format(f);//返回的是String类型
		return fStr;
	}
	
	/**
	 * @Title: statisticReadMeterMonthChart
	 * @Description: 漏失率统计
	 * @param period  期间  yyyy-mm
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/water/monthrate/chart")
	
	public String statisticWaterMonthRateChart(String period, Model model) {
		//以下三个节点的数量应相同
		//(1)获取指定月份表计父结点  
		List<Map<String,Object>>  parentNodeList=statisticService.getParentNodeListByMonth(period);
		log.debug("父节点个数"+parentNodeList.size());
		
		//(2)获取指定月份抄表记录中,所有父表计下子节点的水量和
		//CHILD_SUM  NODE_ID  METER_TREE_ID
		List<Map<String,Object>>  childAmountOfParentNode=statisticService.statChildAmountOfParentNode(period);
		log.debug("父节点个数-计算子节点水量和"+childAmountOfParentNode.size());
		
		//(3)获取各非叶结点的水量
		List<Map<String,Object>>  parentNodeAmountList=statisticService.getParentNodeAmountListByMonth(period);
		log.debug("父节点个数-节点水量"+parentNodeAmountList.size());
		
		//(4)构造非叶子结点之间的父子关系.  (根据MeterTree)
		//(4.1)计算各叶子节点的层次关系  可以自(2)中列表获取,或是自(3)中列表获取	`	
		List<Map<String,Object>>  noLeafNodeList=constructMeterTree(parentNodeAmountList,childAmountOfParentNode);
		// model.addAttribute("zNodes", noLeafNodeList);
		
		//TEST DATA
		//实际应用中将以下代码屏蔽
		List<Map<String,Object>> treeNodeList=genTreeNodes();  //String locationMapListJson = JSON.toJSONString(treeNodeList);
		model.addAttribute("zNodes", treeNodeList);
		
		return TEMPLATE_PATH + "statistic_water_month_rate_bar";
	}
	
	/**
	 * @Title: genTreeNodes
	 * @Description: 生成模拟数据
	 * @return 
	 */
	private List<Map<String,Object>>  genTreeNodes(){
		List<Map<String,Object>> treeNodeList=new ArrayList<>();			
		Map<String,Object> rootNode=new HashMap<>();
		rootNode.put("name", "rootNode");
		rootNode.put("id", "1");
		rootNode.put("pid","0");		
		treeNodeList.add(rootNode);
		
		Map<String,Object> subNode1=new HashMap<>();
		subNode1.put("name", "subNode");
		subNode1.put("id", "2");
		subNode1.put("pid","1");
		treeNodeList.add(subNode1);
		
		
		Map<String,Object> subNode2=new HashMap<>();
		subNode2.put("name", "subNode");
		subNode2.put("id", "3");
		subNode2.put("pid","1");
		treeNodeList.add(subNode2);
		return treeNodeList;
	}
	
	/**
	 * @Title: constructMeterTree
	 * @Description: 构造非叶节点的父子关系. 建立Tree,为Ztree提供数据
	 * 				  自MeterTree中查询各节点之间的关系. 				  
	 * @param parentNodeAmountList   非叶节点水量
	  		 结构如下:	  
	  		[
		  		{
		  			REAL_AMOUNT:  xxx,  此节点水量和
		  			METER_RECORD.*      //其中包括相应的METER_TREE_ID   		
		  		},
		  		{}
	 * 		]
	 * 				
	 * @param childAmountOfParentNode  非叶节点下子结点水量
	  		 结构如下:
	  		[
		  		{
		  			CHILD_SUM:  xxx,  此节点下所有直接子结点的水量和
		  			NODE_ID:    yyy   节点ID(非叶结点)  meterId
		  		},
		  		{}
	  		]
	  			
	 * @return 
	 * 		
	 */
	List<Map<String,Object>>  constructMeterTree(List<Map<String,Object>> parentNodeAmountList,List<Map<String,Object>> childAmountOfParentNode){
		//先查询各结点之间的层次关系.并置于一个列表中		
		List<Map<String,Object>>  treeNodeList=new ArrayList<>();	
		//(1)扫描列表,并确定各节点之间父子关系
		for(int i=0;i<parentNodeAmountList.size();i++) {
			Map<String,Object> node=parentNodeAmountList.get(i);
			BigDecimal nodeAmount=(BigDecimal)node.get("REAL_AMOUNT");  //计量表计-水量
			long meterId=(Long)node.get("METER_ID");					//计量表计ID
			long meterTreeId=(Long)node.get("METER_TREE_ID");			//计量表计METER_TREE_ID
			
			//查询计量表计下所有子表水量
			Map<String,Object> subNodesAmountMap=getSubNodesAmount(meterId,childAmountOfParentNode);  //获取某个非叶子节点的水量
			if(subNodesAmountMap!=null) {
				Map<String,Object> treeNode=new HashMap<>();
				treeNode.put("id", meterId);     //节点ID
				//search parent node from meter_tree
				Long pid=getParendId(meterId,meterTreeId);				
				treeNode.put("pid", pid);		//节点的PID		
				treeNode.put("name", "name");   //节点名称 (可以获取水表的名称)			
				
				//计算节点的漏失率
				//(1)当前节点下所有节点的水量和
				BigDecimal lossRate=new BigDecimal(0);   //节点的漏失率
				BigDecimal childSum=getChildSumAmount(meterId,childAmountOfParentNode);  //当前节点下所有子节点的水量合计  children's sum amount
				lossRate=calcLossRate(nodeAmount,childSum);   //计算漏失率
				
				treeNode.put("lossRate", lossRate);
				
				treeNodeList.add(treeNode);
			}			
		}	
		
		return treeNodeList;
		
	}
	
	/**
	 * @Title: calcLossRate
	 * @Description: 计算漏失率
	 * @param nodeAmount  节点水量 
	 * @param subNodeSumAmount  此节点下子节点水量和
	 * @return 
	 */
	private BigDecimal calcLossRate(BigDecimal nodeAmount,BigDecimal subNodeSumAmount) {
		BigDecimal sub=BigDecimalUtils.subtract(nodeAmount, subNodeSumAmount);
		BigDecimal rate=BigDecimalUtils.divide(sub,nodeAmount);
		return rate;
	}
	
	
	/**
	 * @Title: getParendId
	 * @Description: 查询指定节点的父结点ID ---PID
	 * @param meterId  节点ID
	 * @param meterTreeId  节点在meterTree中的ID  此为KEY
	 * @return 
	 */
	private Long  getParendId(Long meterId,Long meterTreeId) {
//		MeterTree searchObj=new MeterTree();
//		searchObj.setMeterId(meterId);  //此条件无效
//		searchObj.setMeterId(meterTreeId);
		
		//MeterTree resultObj=meterTreeService.selectOne(searchObj);
		
		MeterTree resultObj=meterTreeService.selectByPrimaryKey(meterTreeId);  //根据主KEY进行查询
		
		
		Long resultPid=-1L;
		if(resultObj!=null) {
			resultPid=resultObj.getPid(); 
		}
		return resultPid;
		
	}
	
	/**
	 * @Title: getChildSumAmount
	 * @Description: 获取指定节点下所有子节点的合计水量
	 * @param meterId  表计ID
	 * @param childAmountOfParentNode 非叶节点下所有子表计水量合计列表
	 * @return 
	 * 		如果找到相应节点则返回子表水量合计,否则返回-1;
	 */
	private BigDecimal getChildSumAmount(Long meterId,List<Map<String,Object>> childAmountOfParentNode) {
		BigDecimal childSumAmount=new BigDecimal(-1);
		for(int i=0;i<childAmountOfParentNode.size();i++) {
			Map<String,Object> tempMap=childAmountOfParentNode.get(i);
			Long meterIdx=(Long)tempMap.get("NODE_ID");
			
			if(meterId==meterIdx) {
				childSumAmount=(BigDecimal)tempMap.get("CHILD_SUM");
				break;
			}
		}		
		return childSumAmount;		
	}
	
	
	
	/**
	 * @Title: getSubNodesAmount
	 * @Description: 
	 * @param meterId 表计ID
	 * @param childAmountOfParentNode  非叶节点列表(每个节点表示其下子节点水量和)
	 * @return 
	 * 		如果查询到则返回相应的表计对象,否则返回null;
	 */
	private Map<String,Object> getSubNodesAmount(Long meterId,List<Map<String,Object>> childAmountOfParentNode){
		Map<String,Object>  meterNodeMap=null;
		
		for(int i=0;i<childAmountOfParentNode.size();i++) {
			Map<String,Object> tempMap=childAmountOfParentNode.get(i);
			Long nodeId=(Long)tempMap.get("NODE_ID");  //非叶节点ID
			
			if(meterId==nodeId) {
				meterNodeMap=tempMap;
				break;
			}
		}
		return meterNodeMap;
	}
	
	
	
	/**
	 * @Title: getNodeLevel
	 * @Description: 获取结点的层级关系
	 * 	  (1)按查询条件自MeterTree查询,获取其PARENT_ID
	 * 	  (2)if PARENT_ID=0 then level=1;  //顶层结点
	 * 	  (3)if PARENT_ID<>0 then 继续查询直到
	 * @param meterTreeId   meterTreeId	 
	 * @return 
	 */
	private int getNodeLevel(long meterTreeId,List<Map<String,Object>> parentNodeList) {
		int level=0;
		MeterTree meterTree=searchMeterTree(meterTreeId);
		Long parentId=meterTree.getPid();
		while(1==1) {
			if(parentId==0) {
				level=level+1;
				return level;
			}
			else {
				level=level+1;
				getMeterTreeIdOfNode(parentId,parentNodeList);
			}
		}
	}
	
	/**
	 * @Title: getMeterTreeIdOfNode
	 * @Description: 根据表计ID查询   meterTreeId
	 * @param meterId
	 * @param parentNodeList
	 * @return
	 * 		如果查询到则返回
	 * 		否则返回-1;
	 */
	private Long getMeterTreeIdOfNode(long meterId,List<Map<String,Object>> parentNodeList) {
		long foundMeterTreeId=-1;
		for(int i=0;i<parentNodeList.size();i++) {
			Map<String,Object> tempMap=parentNodeList.get(i);
			Map<String,Object> node=parentNodeList.get(i);
			long meterIdx=(Long)node.get("METER_ID");
			long meterTreeIdx=(Long)node.get("METER_TREE_ID");
			
			if(meterIdx==meterId) {
				foundMeterTreeId=meterTreeIdx;
				break;
			}			
		}
		
		return foundMeterTreeId;
		
	}
	
	
	
	/**
	 * @Title: searchMeterTree
	 * @Description: 自MeterTree中查询指定的结点
	 * @param meterId      此参数暂时无效.
	 * @param meterTreeId  meterTreeId 主键(primary key)
	 * @return 
	 */
	private MeterTree searchMeterTree(long meterTreeId) {
		//MeterTree searchObj=new MeterTree();
		//searchObj.setId(meterTreeId);
		//searchObj.setMeterId(meterId);
		return meterTreeService.selectByPrimaryKey(meterTreeId);
		
	}
	
	
	
}
