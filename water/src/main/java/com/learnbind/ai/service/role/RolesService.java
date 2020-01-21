package com.learnbind.ai.service.role;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface RolesService extends IBaseService<SysRoles, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysRoles> searchRole(String searchCond);
	
    /** 
    	* @Title: insertRole 
    	* @Description: 增加 
    	* @param @param role
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int insertRole(SysRoles role);
    
    /**
     * 修改
     * @param role
     * @return
     */
    public int updateRole(SysRoles role);
    
    /**
     * 删除
     * @param roleId
     * @return
     */
    public int deleteRole(long roleId);

    /** 
    	* @Title: findAllRole 
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<SysRoles> findAllRole(int pageNum, int pageSize);
    
    /**
     * @Title: getRoleListByUserId
     * @Description: 根据用户ID查询用户角色
     * @param userId
     * @return 
     */
    public List<SysRoles> getRoleListByUserId(Long userId); 
}
