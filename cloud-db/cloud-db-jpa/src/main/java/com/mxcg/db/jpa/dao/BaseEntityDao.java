package com.mxcg.db.jpa.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.Map;

import com.mxcg.core.page.PageParameter;
import com.mxcg.core.page.PageResult;
import com.mxcg.db.jpa.entity.BaseEntity;
import com.mxcg.db.jpa.service.BaseEntityService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.Assert;



public abstract class BaseEntityDao<T extends BaseEntity, ID extends Serializable> implements BaseEntityService<T, ID>
{
    
    @Override
    public T save(T entity)
    {
        return jpaRepository().save(entity);
    }
    
    @Override
    public T getOne(ID key)
    {
        Optional<T> o = jpaRepository().findById(key);
        return o.isPresent() ? o.get() : null;
    }
    
    @Override
    public T findOne(ID key)
    {
        T t = this.getOne(key);
        
        List<T> list = new ArrayList<T>();
        if (t != null) list.add(t);
        leftJoinData(list);
        return t;
    }
    
    @Override
    public List<T> findAll()
    {
        List<T> list = null;
        if (jpaSpecificationExecutor() != null)
        {
            Specification<T> spec = makeSpecification(null);
            list = jpaSpecificationExecutor().findAll(spec);
        }
        else
        {
            list = jpaRepository().findAll();
        }
        leftJoinData(list);
        return list;
    }
    
    @Override
    public List<T> findAll(Iterable<ID> ids)
    {
        List<T> list = jpaRepository().findAllById(ids);
        leftJoinData(list);
        return list;
    }
    
    @Override
    public List<T> findAll(ArrayList<ID> ids)
    {
        List<T> list = jpaRepository().findAllById(ids);
        leftJoinData(list);
        return list;
    }
    
    @Override
    public Page<T> findAll(Example<T> example, PageParameter pageParameter)
    {
        if (jpaSpecificationExecutor() != null)
        {
            Specification<T> spec = makeSpecification(example);
            Page<T> page = jpaSpecificationExecutor().findAll(spec, pageParameter);
            leftJoinData(page.getContent());
            return new PageResult<>(page);
        }
        else
        {
            Page<T> page = jpaRepository().findAll(example, pageParameter);
            leftJoinData(page.getContent());
            return new PageResult<>(page);
        }
    }
    
    private Specification<T> makeSpecification(Example<T> example)
    {
        if(example == null)
            return null;
        else
        {
            Specification<T> spec = new ExampleSpecification<T>(example);
            Specification<T> filterSpec = filter();
            if(filterSpec != null)
                return spec.and(filterSpec);
            else
                return spec;
        }
    }
    
    //增加过滤
    protected Specification<T> filter()
    {
        return null;
    }
    
    @Override
    public List<T> save(Iterable<T> entitis)
    {
        List<T> result = new ArrayList<>();
        for (T entity : entitis)
        {
            result.add(this.save(entity));
        }
        return result;
    }
    
    @Override
    public void delete(ID key)
    {
        jpaRepository().deleteById(key);
    }
    
    @Override
    public void delete(Iterable<ID> keys)
    {
        for (ID key : keys)
        {
            this.delete(key);
        }
    }
    
    protected abstract JpaRepository<T, ID> jpaRepository();
    
    protected JpaSpecificationExecutor<T> jpaSpecificationExecutor()
    {
        return null;
    }
    
    protected void leftJoinData(List<T> list)
    {
        
    }
    
    protected void addindex(Map<Long, List<Integer>> indexs, int idx, Long key)
    {
        if (key != null)
        {
            List<Integer> l = indexs.get(key);
            if (l == null) l = new ArrayList<>();
            l.add(idx);
            indexs.put(key, l);
        }
    }
    
    protected void addindex(Map<String, List<Integer>> indexs, int idx, String key)
    {
        if (key != null)
        {
            List<Integer> l = indexs.get(key);
            if (l == null) l = new ArrayList<>();
            l.add(idx);
            indexs.put(key, l);
        }
    }
    
    private static class ExampleSpecification<T> implements Specification<T>
    {
        
        /**
         * 注释内容
         */
        private static final long serialVersionUID = -3637361482504248397L;
        
        private final Example<T> example;
        
        /**
         * Creates new {@link ExampleSpecification}.
         *
         * @param example
         */
        ExampleSpecification(Example<T> example)
        {
            
            Assert.notNull(example, "Example must not be null!");
            this.example = example;
        }
        
        /*
         * (non-Javadoc)
         * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery, javax.persistence.criteria.CriteriaBuilder)
         */
        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb)
        {
            return QueryByExamplePredicateBuilder.getPredicate(root, cb, example);
        }
    }
}
