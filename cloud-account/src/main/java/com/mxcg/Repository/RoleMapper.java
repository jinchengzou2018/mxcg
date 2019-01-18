package com.mxcg.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.mxcg.entity.Role;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:16
 * @description：${description}
 * @modified By：
 * @version: $version$
 */

@Repository
public interface  RoleMapper  extends BaseMapper<Role> {


    List<Role> findByUserId(@Param("userId") Long userId);
}
