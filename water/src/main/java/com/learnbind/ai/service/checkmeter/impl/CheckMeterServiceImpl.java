package com.learnbind.ai.service.checkmeter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.dao.SysCheckMeterMapper;
import com.learnbind.ai.dao.SysRolesMapper;
import com.learnbind.ai.dao.SysWaterPriceMapper;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysRolesRights;
import com.learnbind.ai.model.SysUsersRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.checkmeter.CheckMeterService;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.rolesrights.RolesRightsService;
import com.learnbind.ai.service.usersroles.UsersRolesService;
import com.learnbind.ai.service.waterprice.WaterPriceService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.checkmeter.impl
 *
 * @Title: CheckMeterServiceImpl.java
 * @Description: 服务实现
 *
 * @author Thinkpad
 * @date 2019年5月15日 下午5:35:59
 * @version V1.0 
 *
 */
@Service
public class CheckMeterServiceImpl extends AbstractBaseService<SysCheckMeter, Long> implements  CheckMeterService {
	
	@Autowired
	public SysCheckMeterMapper sysCheckMeterMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CheckMeterServiceImpl(SysCheckMeterMapper mapper) {
		this.sysCheckMeterMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCheckMeter
	 * @Description: 根据条件查询检测配置
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.checkmeter.CheckMeterService#searchCheckMeter(java.lang.String)
	 */
	@Override
	public List<SysCheckMeter> searchCheckMeter(String searchCond) {
		return sysCheckMeterMapper.searchCheckMeter(searchCond);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertCheckMeter
	 * @Description: 添加检测配置信息
	 * @param checkMeter
	 * @return 
	 * @see com.learnbind.ai.service.checkmeter.CheckMeterService#insertCheckMeter(com.learnbind.ai.model.SysCheckMeter)
	 */
	@Override
	public int insertCheckMeter(SysCheckMeter checkMeter) {
		return sysCheckMeterMapper.insertSelective(checkMeter);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateCheckMeter
	 * @Description: 修改检测配置信息
	 * @param checkMeter
	 * @return 
	 * @see com.learnbind.ai.service.checkmeter.CheckMeterService#updateCheckMeter(com.learnbind.ai.model.SysCheckMeter)
	 */
	@Override
	public int updateCheckMeter(SysCheckMeter checkMeter) {
		return sysCheckMeterMapper.updateByPrimaryKeySelective(checkMeter);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteCheckMeter
	 * @Description: 根据id删除用户检测配置
	 * @param checkMeterId
	 * @return 
	 * @see com.learnbind.ai.service.checkmeter.CheckMeterService#deleteCheckMeter(long)
	 */
	@Override
	@Transactional
	public int deleteCheckMeter(long checkMeterId) {
		try {
			int rows = sysCheckMeterMapper.deleteByPrimaryKey(checkMeterId);
			if(rows>0) {
				SysCheckMeter checkMeter = new SysCheckMeter();
				checkMeter.setId(checkMeterId);
				}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<SysCheckMeter> findAllCheckMeter(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<SysCheckMeter> checkMeterList = sysCheckMeterMapper.selectAll();
		PageInfo result = new PageInfo(checkMeterList);
		return result;
	}


}
