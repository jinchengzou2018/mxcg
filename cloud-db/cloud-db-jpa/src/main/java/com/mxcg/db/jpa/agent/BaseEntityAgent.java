package com.mxcg.db.jpa.agent;


import com.mxcg.core.Result;
import com.mxcg.core.exception.TofocusException;
import com.mxcg.core.page.PageParameter;
import com.mxcg.db.jpa.entity.BaseEntity;
import com.mxcg.db.jpa.service.BaseEntityService;
import com.mxcg.jpa.FindRequest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



public abstract class BaseEntityAgent<T extends BaseEntity, ID extends Serializable> implements BaseEntityService<T, ID>
{
    @Override
    public T save(T entity)
    {
        Result<T> result = (Result<T>) invoke(jpaAccess(), "save", entity.getClass(), entity);
        if(!result.isSuccess())
            throw new TofocusException(result.getCode(), result.getDescript());
        return result.getResult();
    }
    

    @Override
    public T getOne(ID key)
    {
        Result<T> result = (Result<T>) invoke(jpaAccess(), "findOne", key.getClass(), key);
        if(!result.isSuccess())
            throw new TofocusException(result.getCode(), result.getDescript());
        return result.getResult();
    }

    @Override
    public T findOne(ID key)
    {
        T t = this.getOne(key);
        List<T> list = new ArrayList<T>();
        list.add(t);
        leftJoinData(list);
        return t;
    }

    @Override
    public List<T> findAll()
    {
        Result<List<T>> result = (Result<List<T>>) invoke(jpaAccess(), "findAll", null, null);
        if(!result.isSuccess())
            throw new TofocusException(result.getCode(), result.getDescript());
        List<T> list = result.getResult();
        leftJoinData(list);
        return list;
    }

    @Override
    public List<T> findAll(Iterable<ID> ids)
    {
        Result<List<T>> result = (Result<List<T>>) invoke(jpaAccess(), "findAll", ids.getClass(), ids);
        if(!result.isSuccess())
            throw new TofocusException(result.getCode(), result.getDescript());
        List<T> list = result.getResult();
        leftJoinData(list);
        return list;
    }
    
    @Override
    public List<T> findAll(ArrayList<ID> ids)
    {
        return findAll(ids, false);
    }

    public List<T> findAll(ArrayList<ID> ids, boolean simple)
    {
        Result<List<T>> result = (Result<List<T>>) invoke(jpaAccess(), "findAll", ids.getClass(), ids);
        if(!result.isSuccess())
            throw new TofocusException(result.getCode(), result.getDescript());
        List<T> list = result.getResult();
        if(!simple) leftJoinData(list);
        return list;
    }
    
    @Override
    public Page<T> findAll(Example<T> example, PageParameter pageParameter)
    {
        FindRequest<T> findRequest = new FindRequest<>();
        findRequest.setPageParameter(pageParameter);
        findRequest.setBean(example.getProbe());
        Result<Page<T>> result = (Result<Page<T>>) invoke(jpaAccess(), "findAll", findRequest.getClass(), findRequest);
        if(!result.isSuccess())
            throw new TofocusException(result.getCode(), result.getDescript());
        Page<T> page = result.getResult();
        leftJoinData(page.getContent());
        return page;
    }

    @Override
    public List<T> save(Iterable<T> entitis)
    {
        List<T> result = new ArrayList<>();
        for(T entity : entitis)
        {
            result.add(this.save(entity));
        }
        return result;
    }

    @Override
    public void delete(ID key)
    {
        Result<Boolean> result = (Result<Boolean>) invoke(jpaAccess(), "delete", key.getClass(), key);
        if(!result.isSuccess())
            throw new TofocusException(result.getCode(), result.getDescript());
    }

    @Override
    public void delete(Iterable<ID> keys)
    {
        for(ID key : keys)
        {
            this.delete(key);
        }
    }
    
    protected abstract Object jpaAccess();

    private Object invoke(Object jpaAccess, String methodname,  Class<?> parameterType,  Object arg)
    {
        Object r = null;
        try
        {
            if(arg != null)
            {
                Method method =jpaAccess.getClass().getMethod(methodname, parameterType);
                r = method.invoke(jpaAccess, arg);
            }
            else
            {
                Method method =jpaAccess.getClass().getMethod(methodname);
                r = method.invoke(jpaAccess);
            }
        }
        catch (Exception e)
        {
            throw new TofocusException(e);
        }
        return r;
    }

    protected void leftJoinData(List<T> list)
    {
        
    }
}
