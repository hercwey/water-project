package com.learnbind.ai.service.rolesrights;

import com.learnbind.ai.model.SysRolesRights;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface RolesRightsService extends IBaseService<SysRolesRights, Long> {

    /**
     * 	增加角色权限
     * @param roleId
     * @param privilegeIds
     * @return
     */
    public int insertRolesRights(Long roleId, String privilegeIds);

}
