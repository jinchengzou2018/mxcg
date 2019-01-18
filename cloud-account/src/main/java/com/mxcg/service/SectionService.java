package com.mxcg.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mxcg.Repository.SectionMapper;
import com.mxcg.api.SectionServiceApi;
import com.mxcg.entity.Section;
import org.springframework.stereotype.Service;

/**
 * @author ：zoujincheng
 * @date ：Created in 2019/1/2 21:52
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
@Service
public class SectionService   extends ServiceImpl<SectionMapper, Section> implements SectionServiceApi {
}
