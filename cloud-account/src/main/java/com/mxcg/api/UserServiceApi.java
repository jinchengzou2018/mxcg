
package com.mxcg.api;


import com.baomidou.mybatisplus.service.IService;
import com.mxcg.common.exception.CloudException;
import com.mxcg.entity.User;
import com.mxcg.model.UserInfo;

/**
 * 用户信息 服务类

 */
public interface UserServiceApi extends IService<User> {
    /**
     * 根据用户名查询(多对多,自动填充roles)
     * @param username
     * @return
     */
    User findByUsername(String username) throws CloudException;

    /**
     * 删除用户关联的角色(tb_user_role)
     * @param userId
     */
    void deleteUserAssociatedRoles(Long userId);

    /**
     * 获取用户信息：包含详细的角色、权限信息
     * @param username
     * @return
     */
    UserInfo getInfo(String username);

}
