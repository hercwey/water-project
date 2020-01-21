package com.learnbind.ai.service.meters;

import java.util.List;

import com.learnbind.ai.bean.MeterTreeNodeBean;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterTree;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterTreeService.java
 * @Description: 表计父子关系service接口类
 *
 * @author Administrator
 * @date 2019年9月2日 下午12:57:00
 * @version V1.0 
 *
 */
/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterTreeService.java
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 * @author lenovo
 * @date 2019年10月10日 上午10:11:01
 * @version V1.0 
 *
 */
public interface MeterTreeService extends IBaseService<MeterTree, Long> {
	
	/**
	 * @Title: getChildListById
	 * @Description: 查询指定结点下的子结点
	 * @param id 节点ID
	 * @return 
	 * 		返回（MeterTree实体类数据和IS_PARENT属性）
	 * 	
	 */
	public List<MeterTreeNodeBean> getChildListById(Long id);
	
	/**
	 * @Title: getMaxSortValueOfSibling
	 * @Description: 查询指定结点直接孩子的最大排序号
	 * @param parentId  结点的父结点id
	 * @return 
	 * 		返回指定结点直接子结点的最大序号 (序号自0开始)
	 * 		返回结果:null或是integer 		
	 */
	public Integer getMaxSortValueOfSibling(Long parentId);
	
	/**
	 * @Title: deleteMeterTreeNodeByTraceIds
	 * @Description: 根据traceIds删除树结点
	 * @param traceIds 父结点路径
	 * @return 
	 * 		删除影响的条数
	 */
	public int deleteMeterTreeNodeByTraceIds(String traceIds);
	
	/**
	 * @Title: addNodeSortValueOfSilbing
	 * @Description: 将指定的兄弟结点向后移动(sort value)
	 * @param startSortValue  sortValue开始
	 * @param includeStart    true:闭区间; false:开区间
	 * @param endSortValue	   sortValue结束
	 * @param includeEnd      true:闭区间; false:开区间
	 * @param addValue	   需要增加的值,此值可正可负
	 * @param pid	父结点id,对其直接子结点向后移动
	 * @return 
	 * 		所影响的记录个数
	 */
	public int addNodeSortValueOfSilbing(Integer startSortValue,Integer includeStart, 
										Integer endSortValue, Integer includeEnd,
										Integer addValue,Long pid);	
	
	
	/**
	 * @Title: adjustTreeNodeSilbingPos
	 * @Description: 调整以pid为父节点的所有兄弟节点的位置
	 * @param pid 父节点id
	 * @param startSortValue  兄弟节点中的起始位置
	 * @param includeStart  是否包括起始位置
	 * @return 
	 * 		影响的行数
	 */
	public int adjustTreeNodeSilbingPos(Long pid,Integer startSortValue,Integer includeStart);
	
	/**
	 * @Title: updateSubTreeLevel
	 * @Description: 更新子树各结点level
	 * @param subTreeRootNewLevel  子树root新层次级别值
	 * @param subTreeRootOldLevel	子树root移动前层次级别值
	 * @param subTreeRootTraceIds	子树root移动前traceIds
	 * @return 
	 * 		影响的记录条数
	 */
	public int updateSubTreeLevel(Integer subTreeRootNewLevel,Integer subTreeRootOldLevel,String subTreeRootTraceIds);
	
	
	
	/**
	 * @Title: updateSubTreeTraceIds
	 * @Description: 更新子树各结点traceIds
	 * @param subTreeRootNewTraceIds 	子树root新的traceIds
	 * @param subTreeRootOldTraceIds	子树root旧的traceIds
	 * @param subTreeRootTraceIds		子树root旧的traceIds(用作查询条件)
	 * @return 
	 * 		更新影响的记录行数
	 */
	public int updateSubTreeTraceIds(String subTreeRootNewTraceIds,String subTreeRootOldTraceIds,String subTreeRootTraceIds);
	
	/**
	 * @Title: getOneBySearchCond
	 * @Description: 查询符合条件的记录（返回第一条数据）
	 * @param searchCond	条件（小区-楼号-单元-门牌号）
	 * @return 
	 * 		返回MeterTree对象
	 */
	public MeterTree getFirstBySearchCond(String searchCond);
	
	/**
	 * @Title: getOneBySearchCond
	 * @Description: 查询符合条件的记录
	 * @param searchCond	条件（小区-楼号-单元-门牌号）
	 * @param id	地理位置ID
	 * @param action	-1=上一个；1=下一个
	 * @return 
	 * 		返回MeterTree对象
	 */
	public MeterTree getOneBySearchCond(String searchCond, Long id, Integer action);
	
	
}
