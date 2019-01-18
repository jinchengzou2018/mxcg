package com.mxcg.jpa;


import com.mxcg.core.page.PageParameter;
import lombok.Data;

/**
 * 
 * 查询参数
 * 
 * @author  wyw
 * @version  [版本号, 2018年9月14日]
 */
@Data
public class FindRequest<S>
{
    private S bean;
    
    private PageParameter pageParameter;
    
    
}
