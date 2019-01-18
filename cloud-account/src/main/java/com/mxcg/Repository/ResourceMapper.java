package com.mxcg.Repository;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mxcg.entity.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:10
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {
    /**
     * 查询用户资源
     * @param userId
     * @return
     */
    List<Resource> findByUserId(@Param("userId") Long userId);

    /**
     * 查询角色资源（用于鉴权,url != null）
     * @param roleCode
     * @return
     */
    List<Resource> findWithUrlNotNullByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 获取用户的所有菜单(不含按钮、其他资源, 即type==0)
     * @param username
     * @return
     */
    List<Resource> getAllMenuByUsername(@Param("username") String username);
}
