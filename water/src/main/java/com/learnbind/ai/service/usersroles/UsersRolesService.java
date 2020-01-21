package com.learnbind.ai.service.usersroles;

import com.learnbind.ai.model.SysUsersRoles;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface UsersRolesService extends IBaseService<SysUsersRoles, Long> {

    /**
     * @Title: insertList
     * @Description: 批量增加
     * @param userId
     * @param roleIds
     * @return 
     */
    public int insertList(Long userId, String roleIds);
    
    /**
     * @Title: getRolesByUserId
     * @Description: 根据用户id获取角色
     * @param userId
     * @return 
     */
    public String getRolesByUserId(Long userId);

}
