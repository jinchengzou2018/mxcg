package com.mxcg.db.jpa.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mxcg.core.page.PageParameter;
import com.mxcg.db.jpa.entity.BaseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;



public interface BaseEntityService<T extends BaseEntity, ID extends Serializable>
{

    T save(T entity);

    List<T> save(Iterable<T> entities);

    void delete(ID key);

    void delete(Iterable<ID> keys);
    
    T getOne(ID key);

    T findOne(ID key);
    
    List<T> findAll(Iterable<ID> ids);
    
    List<T> findAll(ArrayList<ID> ids);

    List<T> findAll();

    Page<T> findAll(Example<T> example, PageParameter pageParameter);
    
}
