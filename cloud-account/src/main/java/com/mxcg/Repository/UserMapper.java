package com.mxcg.Repository;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mxcg.entity.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:19
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

@Repository
public interface UserMapper extends BaseMapper<User> {

      /**
       * 根据用户名查询(多对多,自动填充roles)
       * @param username
       * @return
       */
      User findByUsername(@Param("username") String username);

      /**
       * 删除用户关联的角色(cloud_user_role)
       * @param userId
       */
      void deleteUserAssociatedRoles(@Param("userId") Long userId);

}
