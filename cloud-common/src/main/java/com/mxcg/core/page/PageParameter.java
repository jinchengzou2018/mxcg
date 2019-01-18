package com.mxcg.core.page;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * 
 * 分页参数
 * 

 */
public class PageParameter implements Pageable, Serializable
{
    private static final long serialVersionUID = -1712046545145736046L;
    
    private int page;
    
    private int size;
    
    private PageSort pageSort;
    
    public PageParameter()
    {
    }

    /**
     * 通过{@link Pageable} 创建一个 {@link PageParameter}.
     * 
     * @param pageable 分页参数
     */
    public PageParameter(Pageable pageable)
    {
        page = pageable.getPageNumber();
        size = pageable.getPageSize();
        pageSort = new PageSort(pageable.getSort());
    }
    
    /**
     * 创建一个 {@link PageParameter}. {@code page} 设为0，返回第一页
     * 
     * @param page 从0开始的页号
     * @param size 每页大小
     */
    public PageParameter(int page, int size)
    {
        this(page, size, null);
    }
    
    /**
     * 创建一个带排序参数的 {@link PageParameter}.
     * 
     * @param page 从0开始的页号
     * @param size 每页大小
     * @param direction 排序的顺序 {@link PageSort}， 可以为 {@literal null}.
     * @param properties 排序的属性，不可为 {@literal null} 或者空串.
     */
    public PageParameter(int page, int size, Direction direction, String... properties)
    {
        this(page, size, new PageSort(direction, properties));
    }

    /**
     * 创建一个带排序参数的 {@link PageParameter}.
     * 
     * @param page 从0开始的页号
     * @param size 每页大小
     * @param sort 排序条件，可以为 {@literal null}.
     */
    public PageParameter(int page, int size, PageSort sort)
    {
        if (page < 0)
        {
            throw new IllegalArgumentException("页号必须大约等于0!");
        }
        
        if (size < 1)
        {
            throw new IllegalArgumentException("页大小必须大于0!");
        }
        
        this.page = page;
        this.size = size;
        this.pageSort = sort;
    }

    /**
     * 创建一个 {@link PageParameter}. {@code page} 设为0，返回第一页
     * 
     * @param page 从0开始的页号
     * @param size 每页大小
     *
     * @since 2.0
     */
    public static PageParameter of(int page, int size) {
        return of(page, size, PageSort.unsorted());
    }

    /**
     * 创建一个带排序参数的 {@link PageParameter}.
     * 
     * @param page 从0开始的页号
     * @param size 每页大小
     * @param sort 排序条件，可以为 {@literal null}.
     * @since 2.0
     */
    public static PageParameter of(int page, int size, PageSort sort) {
        return new PageParameter(page, size, sort);
    }

    /**
     * 创建一个带排序参数的 {@link PageParameter}.
     * 
     * @param page 从0开始的页号
     * @param size 每页大小
     * @param direction 排序的顺序 {@link PageSort}， 可以为 {@literal null}.
     * @param properties 排序的属性，不可为 {@literal null} 或者空串.
     * @since 2.0
     */
    public static PageParameter of(int page, int size, Direction direction, String... properties) {
        return of(page, size, PageSort.by(direction, properties));
    }

    /**
     * 排序参数
     * 
     * @return
     */
    @Override
    public Sort getSort()
    {
        return pageSort==null?Sort.unsorted() :pageSort.toSort();
    }

    /**
     * 下一页
     * 
     * @return
     */
    @Override
    public Pageable next()
    {
        return new PageParameter(getPageNumber() + 1, getPageSize(), getPageSort());
    }

    /**
     * 上一页
     * 
     * @return
     */
    public PageParameter previous()
    {
        return getPageNumber() == 0 ? this : new PageParameter(getPageNumber() - 1, getPageSize(), getPageSort());
    }

    /**
     * 第一页
     * 
     * @return
     */
    @Override
    public Pageable first()
    {
        return new PageParameter(0, getPageSize(), getPageSort());
    }
    
    /* 
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return String.format("Page request [number: %d, size %d, sort: %s]",
            getPageNumber(),
            getPageSize(),
            pageSort == null ? PageSort.unsorted().toString() : pageSort.toString());
    }
    
    /**
     * 页大小
     */
    @Override
    public int getPageSize()
    {
        return size;
    }
    
    /** 
     * 页号
     */
    @Override
    public int getPageNumber()
    {
        return page;
    }
    
    /**
     * 偏移量
     */
    @Override
    public long getOffset()
    {
        return (long) page * (long) size;
    }
    
    /** 
     * 有没有前一页
     */
    @Override
    public boolean hasPrevious()
    {
        return page > 0;
    }
    
    /**
     * 上一页或者第一页
     */
    @Override
    public Pageable previousOrFirst()
    {
        return hasPrevious() ? previous() : first();
    }
    
    public int getPage()
    {
        return page;
    }
    
    public void setPage(int page)
    {
        this.page = page;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public void setSize(int size)
    {
        this.size = size;
    }

    public PageSort getPageSort()
    {
        return pageSort;
    }

    public void setPageSort(PageSort pageSort)
    {
        this.pageSort = pageSort;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + page;
        result = prime * result + ((pageSort == null) ? 0 : pageSort.hashCode());
        result = prime * result + size;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PageParameter other = (PageParameter)obj;
        if (page != other.page) return false;
        if (pageSort == null)
        {
            if (other.pageSort != null) return false;
        }
        else if (!pageSort.equals(other.pageSort)) return false;
        if (size != other.size) return false;
        return true;
    }
    
}
