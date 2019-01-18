package com.mxcg.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

/**
 * 
 * 分页结果集
 * 

 */
public class PageResult<T> implements Page<T>, Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 8276568612624662484L;
    
    private List<T> content = new ArrayList<T>();
    
    private long total;
    
    private PageParameter pageable;
    
    public PageResult()
    {
        
    }
    
    public PageResult(Page<T> page)
    {
        content = page.getContent();
        pageable = new PageParameter(page.getNumber(), page.getSize(), new PageSort(page.getSort()==null?new Sort(Sort.DEFAULT_DIRECTION,"pkey"):page.getSort()));
        total = page.getTotalElements();
    }
    
    /**
     * 通过内容和{@link PageParameter}创建{@link PageResult}.
     * 
     * @param content 本页的内容, 不可为 {@literal null}.
     * @param pageable 分页参数, 可以为 {@literal null}.
     */
    public PageResult(List<T> content, PageParameter pageable)
    {
        
        Assert.notNull(content, "Content must not be null!");
        
        this.content.addAll(content);
        this.pageable = pageable;
    }
    
    /**
     * 通过内容和{@link PageParameter}创建{@link PageResult}.
     * 
     * @param content 本页的内容, 不可为 {@literal null}.
     * @param pageable 分页参数, 可以为 {@literal null}.
     * @param total 总数量. 
     */
    public PageResult(List<T> content, PageParameter pageable, long total)
    {
        
        this(content, pageable);
        
        this.pageable = pageable;
        this.total = !content.isEmpty() && pageable != null && pageable.getOffset() + pageable.getPageSize() > total
            ? pageable.getOffset() + content.size() : total;
    }
    
    /**
     * 通过内容创建{@link PageResult}.
     * 
     * @param content 不可为 {@literal null}.
     */
    public PageResult(List<T> content)
    {
        this(content, null, null == content ? 0 : content.size());
    }

    /**
     * 总页数
     * 
     * @return 总页数
     */
    @Override
    public int getTotalPages()
    {
        return getSize() == 0 ? 1 : (int)Math.ceil((double)total / (double)getSize());
    }

    /**
     * 总数量
     * 
     * @return 总数量
     */
    @Override
    public long getTotalElements()
    {
        return total;
    }

    /**
     * 是否还有下一 {@link PageResult}.
     * 
     * @return 是否还有下一 {@link PageResult}.
     */
    @Override
    public boolean hasNext()
    {
        return getNumber() + 1 < getTotalPages();
    }

    /**
     * 是否最后一页.
     * 
     * @return
     */
    @Override
    public boolean isLast()
    {
        return !hasNext();
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        
        String contentType = "UNKNOWN";
        List<T> content = getContent();
        
        if (content.size() > 0)
        {
            contentType = content.get(0).getClass().getName();
        }
        
        return String.format("Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
    }

    /**
     * 页号.
     * 
     * @return
     */
    @Override
    public int getNumber()
    {
        return pageable == null ? 0 : pageable.getPageNumber();
    }

    /**
     * 页大小.
     * 
     * @return
     */
    @Override
    public int getSize()
    {
        return pageable == null ? 0 : pageable.getPageSize();
    }

    /**
     * 内容数量.
     * 
     * @return
     */
    @Override
    public int getNumberOfElements()
    {
        return content.size();
    }

    /**
     * 是否有前一 {@link PageResult}.
     * 
     * @return 是否有前一 {@link PageResult}.
     */
    @Override
    public boolean hasPrevious()
    {
        return getNumber() > 0;
    }

    /**
     * 是否第一页.
     * 
     * @return
     */
    @Override
    public boolean isFirst()
    {
        return !hasPrevious();
    }
    

    /**
     * 下一页
     * 
     */
    @Override
    public Pageable nextPageable()
    {
        return hasNext() ? getPageable().next() : Pageable.unpaged();
    }
    

    /**
     * 前一页
     * 
     */
    @Override
    public Pageable previousPageable()
    {
        
        if (hasPrevious())
        {
            return getPageable().previousOrFirst();
        }
        
        return Pageable.unpaged();
    }
    

    /**
     * 是否有内容
     * 
     */
    @Override
    public boolean hasContent()
    {
        return !content.isEmpty();
    }
    

    /**
     * 获取内容
     * 
     */
    @Override
    public List<T> getContent()
    {
        return Collections.unmodifiableList(content);
    }
    

    /**
     * 排序方式
     * 
     */
    @Override
    public Sort getSort()
    {
        return pageable == null ? Sort.unsorted() : pageable.getSort();
    }
    

    /**
     * 迭代器
     * 
     */
    @Override
    public Iterator<T> iterator()
    {
        return content.iterator();
    }
    
    public long getTotal()
    {
        return total;
    }
    
    public void setTotal(long total)
    {
        this.total = total;
    }

    @Override
    public Pageable getPageable()
    {
        return pageable == null ? Pageable.unpaged() : pageable;
    }
    
    public void setPageable(PageParameter pageable)
    {
        this.pageable = pageable;
    }
    
    public void setContent(List<T> content)
    {
        this.content = content;
    }

    /* 
     * (non-Javadoc)
     * @see org.springframework.data.domain.Slice#transform(org.springframework.core.convert.converter.Converter)
     */
    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        
        PageParameter p = new PageParameter(getPageable());
        return new PageResult<>(getConvertedContent(converter),p, total);
    }
    

    /**
     * Applies the given {@link Function} to the content of the {@link Chunk}.
     * 
     * @param converter must not be {@literal null}.
     * @return
     */
    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {

        Assert.notNull(converter, "Function must not be null!");

        return this.stream().map(converter::apply).collect(Collectors.toList());
    }

}
