package com.learnbind.ai.service.user;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface UsersService extends IBaseService<SysUsers, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysUsers> searchUser(String searchCond);
	
	/**
	 * @Title: getUserListByRoleCode
	 * @Description: 根据角色编码或条件查询用户
	 * @param roleCode
	 * @param searchCond
	 * @return 
	 */
	public List<SysUsers> getUserListByRoleCode(String roleCode, String searchCond);
	
    /** 
    	* @Title: addUser 
    	* @Description: 增加用户 
    	* @param @param user
    	* @param @return     
    	* @return int    返回类型 
    	* @throws 
    */
    public int addUser(SysUsers user);

    /** 
    	* @Title: findAllUser 
    	* @Description: 查询用户 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<SysUsers> findAllUser(int pageNum, int pageSize);
    
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public UserBean selectUserByUsername(String username);
}
